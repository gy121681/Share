package com.shareshenghuo.app.shop.fragment;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.ExchangeFmListAcyivity;
import com.shareshenghuo.app.shop.ExchangeListAcyivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.AccountInfoBean;
import com.shareshenghuo.app.shop.network.request.AccountInfoRequest;
import com.shareshenghuo.app.shop.network.request.NumRequest;
import com.shareshenghuo.app.shop.network.request.UserWithDrawRequest;
import com.shareshenghuo.app.shop.network.response.AccountInfoRespones;
import com.shareshenghuo.app.shop.network.response.NumResponse;
import com.shareshenghuo.app.shop.network.response.WithdrawalsResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.MD5Utils;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.Util;
import com.shareshenghuo.app.shop.util.ViewUtil;
import com.shareshenghuo.app.shop.widget.dialog.MyEditDialog;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;
import com.shareshenghuo.app.shop.widget.dialog.onMyaddTextListener;

public class ExchangFragment2 extends BaseFragment{
	
	private TextView tv_explain, tv_arr,tv_rate_pro,tv_tax,tv_tax1,tv_surplus,tv_withdrawals_num,card_no,bank_name;
	private EditText ed_balance;
	private String WITHDRAW_FEE = "",text = "";//提现手续费
	private String INCOME_TAX = "";//个人所得税
	private Button llWalletRecharge;
	private MyEditDialog doubleWarnDialog1;
	private String filialMoney;
	private String MIN_WITHDRAW = "";//最小金额
	private ImageView bank_img;
	private RelativeLayout tv_repop;
	private TextView tv_pro,tv_withdrawals;	
	private TwoButtonDialog downloadDialog;
	private double  rate = 0.7;
	private List<RateTypeBean> typelist;
	private Button btnTopRight1;
	private TextView tvTopTitle;
	private String type = "1";
	@Override
	protected int getLayoutId() {
		return R.layout.exchange;
	}

	@Override
	protected void init(View rootView) {
		
		initView();
		userAccountInfoQuery();
		getStatisticsData() ;
		
	}
	
	public  class RateTypeBean{
		public  String rate;
		public  String beizhu;
		public String check;
	}

	private void initView() {
		btnTopRight1 = getView(R.id.btnTopRight1);
		tvTopTitle = getView(R.id.tvTopTitle);
		tvTopTitle.setText("兑换");
		tvTopTitle.setVisibility(View.VISIBLE);
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("兑换记录");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(activity,ExchangeFmListAcyivity.class);
				it.putExtra("tag", "1");
				startActivity(it);
			}
		});
		tv_repop=  getView(R.id.tv_repop);
		tv_arr = getView(R.id.tv_arr);
		tv_rate_pro = getView(R.id.tv_rate_pro);
		tv_withdrawals= getView(R.id.tv_withdrawals);
		tv_explain = getView(R.id.tv_explain);
		tv_tax = getView(R.id.tv_tax);
		bank_name = getView(R.id.bank_name);
		bank_img = getView(R.id.bank_img);
		card_no = getView(R.id.card_no);
		tv_tax1 = getView(R.id.tv_tax1);
		ed_balance = getView(R.id.ed_balance);
		tv_surplus = getView(R.id.tv_surplus);
		tv_withdrawals_num = getView(R.id.tv_withdrawals_num);
		tv_pro = getView(R.id.tv_pro);
		tv_pro.setText(Html.fromHtml("<u>说明 </u>"));
		
		llWalletRecharge = getView(R.id.llWalletRecharge);
		if(!TextUtils.isEmpty(UserInfoManager.getUserInfo(activity).card_no)){
			String card = UserInfoManager.getUserInfo(activity).card_no;
			if(card.length()>4){
				card_no.setText(card.substring(0, 4)+" **** **** "+card.substring(card.length()-4));
			}
			
		}
		
		if(!TextUtils.isEmpty(UserInfoManager.getUserInfo(activity).bank_name)){
			String bank = UserInfoManager.getUserInfo(activity).bank_name;
			bank_name.setText(bank);
			ViewUtil.setbank(bank_img,bank);
		}
		
		tv_pro.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initPro();
			}
		});
		
		
		
		tv_repop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				 List<String> l = new ArrayList<String>();
//				 l.add("1个工作日到账(T+1)\n0.7%手续费,最少5个秀点");
//				 l.add("3个工作日到账(T+3)\n0.3%手续费,最少5个秀点");
//				 l.add("7个工作日到账(T+7)\n0.1%手续费,最少5个秀点");
				showPopupWindow(tv_rate_pro,typelist, 0,tv_arr);
			}
		});
		
		ed_balance.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					try {
						double b = Double.parseDouble(ed_balance.getText().toString());
						if(b>=Double.parseDouble(MIN_WITHDRAW)/100){
//						tv_tax.setVisibility(View.VISIBLE);
//						String a = ((b-(b*((Double.parseDouble(INCOME_TAX))/100)))-(Double.parseDouble(WITHDRAW_FEE)/100))+"";
//						BigDecimal mData = new BigDecimal(a).setScale(2, BigDecimal.ROUND_HALF_UP);
//						tv_tax.setText("实际到账   : "+mData+"元");
						
						
						tv_tax.setVisibility(View.VISIBLE);
//						String a = ((b-(b*((Double.parseDouble(INCOME_TAX))/100)))-(Double.parseDouble(WITHDRAW_FEE)/100))+"";
					
						if(b*(rate/100)<=5){
							BigDecimal mData = new BigDecimal(b-5).setScale(2, BigDecimal.ROUND_HALF_UP);
							tv_tax.setText("实际到账   : "+mData+"元");
						}else{
							String a = String.valueOf(b-b*(rate/100));
							BigDecimal mData = new BigDecimal(a).setScale(2, BigDecimal.ROUND_HALF_UP);
							tv_tax.setText("实际到账   : "+mData+"元");
						}
						
						}else{
							tv_tax.setVisibility(View.GONE);
						}
					} catch (Exception e) {
						// TODO: handle exception
						ViewUtil.showError(ed_balance, "金额有误");
					}
					
				}else {
					tv_tax.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		llWalletRecharge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(ViewUtil.checkEditEmpty(ed_balance, "请输入兑换秀点"))
					return;
				if(filialMoney!=null&&Double.parseDouble(ed_balance.getText().toString())>Double.parseDouble(filialMoney)/100){
					ViewUtil.showError(ed_balance, "秀点不足");
					return;
				}
				
				if(Double.parseDouble(ed_balance.getText().toString())<Double.parseDouble(MIN_WITHDRAW)/100){
					ViewUtil.showError(ed_balance, "最低兑换秀点:"+Double.parseDouble(MIN_WITHDRAW)/100);
					return;
				}

				
				double db =  Double.parseDouble(ed_balance.getText().toString());
				
				if(db%100!=0){
					
					  ViewUtil.showError(ed_balance, "100整数倍");
					  return;
				}
//				  if( (db/(double)100) > (db/100) ){ 
//				       //如果true 证明 i不能被100整除 
//					
//					 
//				     }else{ 
//				       //能被100整除 
//				     } 
				
				shwpaypwd();
			}
		});
		
	}
	
	public void getStatisticsData() {
		NumRequest req = new NumRequest();
		req.userId = UserInfoManager.getUserInfo(activity).shop_id+"";
		req.userType = "2";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETBUNBER, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				NumResponse bean = new Gson().fromJson(resp.result, NumResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					
					tv_surplus.setText(Util.getnum(bean.data.filialMoney,false)+"");//tv_surplus,原剩余秀点
					System.out.println("=====秀儿秀点兑换:"+bean.data.stayFilialMoney);
					tv_withdrawals_num.setText(Util.getnum(bean.data.stayFilialMoney,false)+"");
//					if(Double.parseDouble(bean.data.filialMoney)/100>=100){
//						tv_withdrawals_num.setText((int)((Double.parseDouble(bean.data.filialMoney)/10000))+"00");
//					}else{
//						tv_withdrawals_num.setText("0");
//					}
							
//							Integer.parseInt(Util.getIntnum(bean.data.filialMoney,false)+"")/100);
				}
			}
		});
	}
	
	private void userAccountInfoQuery() {
		// TODO Auto-generated method stub
		
		ProgressDialogUtil.showProgressDlg(activity, "请稍后");
		AccountInfoRequest req = new AccountInfoRequest();
		req.otherGroup = "WITHDRAW";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GENEROLIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				AccountInfoRespones bean = new Gson().fromJson(resp.result, AccountInfoRespones.class);
				if(Api.SUCCEED == bean.result_code) {
//					T.showShort(ExchangeAcyivity.this, "提交成功");
					inittext(bean.data);
				} else {
					T.showShort(activity, bean.result_desc);
				}
			}

		});
	}
	
	private void initdata(String pwd) {
		// TODO Auto-generated method stub
		
//		String type = "1";
//		if(rate==0.1){
//			type = "3";
//		}else if(rate==0.3){
//			type = "2";
//		}else if(rate==0.7){
//			type = "1";
//			
//		}
		
		double b = Double.parseDouble(ed_balance.getText().toString());
		
		ProgressDialogUtil.showProgressDlg(activity, "请稍后");
		UserWithDrawRequest req = new UserWithDrawRequest();
		req.userShopId = UserInfoManager.getUserInfo(activity).shop_id+"";
		req.userType = "2";
		req.filialMoney = ((int)(b*100))+"";
		req.wdType = type;
		for(int i=0; i<3; i++)
			pwd = MD5Utils.getMD5String(pwd);
		
		req.payPassword = pwd;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.USERWITHDRAW1, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				WithdrawalsResponse bean = new Gson().fromJson(resp.result, WithdrawalsResponse.class);

				if(Api.SUCCEED == bean.result_code) {
					if(doubleWarnDialog1!=null){
						doubleWarnDialog1.dismiss();
					}
					if(bean.data.RSPCOD.equals("000000")){
						T.showShort(activity, bean.data.RSPMSG);
						activity.finish();
					}else{
						T.showShort(activity, bean.data.RSPMSG);
					}
				} else {
					T.showShort(activity, bean.result_desc);
				}
			}

		});
		
	}
	private void inittext(List<AccountInfoBean> data) {
		// TODO Auto-generated method stub
		typelist = new ArrayList<RateTypeBean>();
		if(data==null||data.size()<=0){
			return;
		}
		
		String MAX_WITHDRAW = "";//最大可提现金额
		String WITHDRAW_START_TIME = "";//提现开始
		String WITHDRAW_END_TIME = "";//提现结束结束
		
		for (int i = 0; i < data.size(); i++) {
			if(data.get(i).other_key.equals("WITHDRAW_FEE")){
				WITHDRAW_FEE = data.get(i).other_value;
			}
			if(data.get(i).other_key.equals("INCOME_TAX")){
				INCOME_TAX = data.get(i).other_value;
			}
			if(data.get(i).other_key.equals("MAX_WITHDRAW")){
				MAX_WITHDRAW = data.get(i).other_value;
			}
			if(data.get(i).other_key.equals("WITHDRAW_START_TIME")){
				WITHDRAW_START_TIME = data.get(i).other_value;
			}
			if(data.get(i).other_key.equals("WITHDRAW_END_TIME")){
				WITHDRAW_END_TIME = data.get(i).other_value;
			}
			if(data.get(i).other_key.equals("MIN_WITHDRAW")){
				MIN_WITHDRAW = data.get(i).other_value;
			}
			
			if(data.get(i).other_key.equals("WITHDRAWALS_EXPLAIN")){
				text = data.get(i).other_value;
			}
			
			if(data.get(i).other_key.equals("T1_WITHDRAW")){
				RateTypeBean r = new RateTypeBean();
				r.beizhu = data.get(i).beizhu;
				r.rate = data.get(i).other_value;
				r.check = data.get(i).other_key;
				typelist.add(r);
			}
			if(data.get(i).other_key.equals("T3_WITHDRAW")){
				RateTypeBean r = new RateTypeBean();
				r.beizhu = data.get(i).beizhu;
				r.rate = data.get(i).other_value;
				r.check = data.get(i).other_key;
				typelist.add(r);
				
			}
			if(data.get(i).other_key.equals("T7_WITHDRAW")){
				RateTypeBean r = new RateTypeBean();
				r.beizhu = data.get(i).beizhu;
				r.rate = data.get(i).other_value;
				r.check = data.get(i).other_key;
				typelist.add(r);
			}
		}
		
		
		tv_rate_pro .setText(typelist.get(0).rate+"%手续费,最少5个秀点");
		tv_withdrawals.setText(typelist.get(0).beizhu);
		rate = Double.parseDouble(typelist.get(0).rate);
		tv_explain.setText(text);
//		tv_explain.setText("\n1.单笔最多可兑换"+Double.parseDouble(MAX_WITHDRAW)/100+"秀点\n2.每笔兑换扣除手续费"+Double.parseDouble(WITHDRAW_FEE)/100+"秀点\n3.秀点仅每月"+WITHDRAW_START_TIME+"-"+WITHDRAW_END_TIME+"日允许兑换，其他时间不允许兑换\n4.代扣税=兑换秀点×税率("+INCOME_TAX+"%"+")\n5.预计到账时间：T+1");
	}
	
	protected void shwpaypwd() {
		doubleWarnDialog1 = new MyEditDialog(activity,
				R.style.loading_dialog, "兑换", "请输入支付密码", "确认", "取消", ed_balance.getText().toString(),
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {

						switch (v.getId()) {
						case R.id.btn_right:
							doubleWarnDialog1.dismiss();
							InputMethodManager m=(InputMethodManager)activity. getSystemService(Context.INPUT_METHOD_SERVICE);
							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
							break;
						case R.id.btn_left:
							String paypwd = doubleWarnDialog1.getpaypwd();

							if (paypwd == null || paypwd.equals("")) {
								Toast.makeText(activity,"请输入支付密码",
										Toast.LENGTH_SHORT).show();
								return;
							}
							if (paypwd.length() < 6 || paypwd.length() > 15) {
								return;
							}
							break;
						default:
							break;
						}
					}

				},
		new onMyaddTextListener() {
			
			@Override
			public void refreshActivity(String paypwd) {
				
				if (paypwd == null || paypwd.equals("")) {
					Toast.makeText(activity,"请输入支付密码",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (paypwd.length() < 6 || paypwd.length() > 15) {
					return;
				}
				
				initdata(paypwd);
				
			}
		});
		doubleWarnDialog1.setCancelable(false);
		doubleWarnDialog1.setCanceledOnTouchOutside(false);
		doubleWarnDialog1.show();
		
	}
	
private void setbank(ImageView v,String bank){
		
	if (bank!= null&&bank.length()>=2) {
		if (bank.contains("招商")) {
			v.setImageResource(R.drawable.ps_cmb);
		}else if(bank.contains("农业")) {
			v.setImageResource(R.drawable.ps_abc);
		}else if(bank.contains("农行")){
			v.setImageResource(R.drawable.ps_abc);
		}else if(bank.contains("北京")){
			v.setImageResource(R.drawable.ps_bjb);
		}else if(bank.equals("中国银行")){
//			if(bank.substring(0,4).equals("中国银行")){
//				v.setImageResource(R.drawable.ps_ccb);
//			}else{
				v.setImageResource(R.drawable.ps_boc);
//			}
			
		}else if(bank.contains("建设")){
			v.setImageResource(R.drawable.ps_ccb);
		}else if(bank.contains("光大")){
			v.setImageResource(R.drawable.ps_cebb);
		}else if(bank.contains("兴业")){
			v.setImageResource(R.drawable.ps_cib);
		}else if(bank.contains("中信")){
			v.setImageResource(R.drawable.ps_citic);
		}else if(bank.contains("民生")){
			v.setImageResource(R.drawable.ps_cmbc);
		}else if(bank.contains("交通")){
			v.setImageResource(R.drawable.ps_comm);
		}else if(bank.contains("华夏")){
			v.setImageResource(R.drawable.ps_hxb);
		}else if(bank.contains("广东发展")){
			v.setImageResource(R.drawable.ps_gdb);
		}else if(bank.contains("广发")){
			v.setImageResource(R.drawable.ps_gdb);
		}else if(bank.contains("邮政")){
			v.setImageResource(R.drawable.ps_psbc);
		}else if(bank.contains("邮储")){
			v.setImageResource(R.drawable.ps_psbc);
		}else if(bank.contains("工商")){
			v.setImageResource(R.drawable.ps_icbc);
		}else if(bank.contains("平安")){
			v.setImageResource(R.drawable.ps_spa);
		}else if(bank.contains("浦东")){
			v.setImageResource(R.drawable.ps_spdb);
		}else if(bank.contains("工商")){
			v.setImageResource(R.drawable.ps_icbc);
		}else if(bank.contains("上海")){
			v.setImageResource(R.drawable.ps_sh);
		}else{
			v.setImageResource(R.drawable.ps_unionpay);
		}
		
	}else{
		v.setImageResource(R.drawable.ps_unionpay);
	}
	
	
}

public void initPro(){

	
	downloadDialog = new TwoButtonDialog(activity, R.style.CustomDialog,
			"再消费秀点说明", "再消费秀点不能直接兑换,只能在平台消费使用", "知道了", "",false,new OnMyDialogClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					switch (v.getId()) {
					case R.id.Button_OK:
						downloadDialog.dismiss();
						break;
					case R.id.Button_cancel:
						
						downloadDialog.dismiss();
					default:
						break;
					}
				}
			});
	downloadDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode,
				KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_SEARCH) {
				return true;
			} else {
				return true; // 默认返回 false
			}
		}
	});
	downloadDialog.setCanceledOnTouchOutside(false);
	
		downloadDialog.show();
		
	
}

@SuppressLint("NewApi")
private void showPopupWindow(final View view, final List<RateTypeBean> typelist1, final int i,final TextView v) {
	if(typelist1==null&&typelist1.size()<=0){
		return;
	}
	 
	final List<String> typelist = new ArrayList<String>();
	
	for (int j = 0; j < typelist1.size(); j++) {
		typelist.add(typelist1.get(j).beizhu);
		
	}
//	 final Drawable drawable_n = getResources().getDrawable(R.drawable.ic_droptop_gray);
//	 final Drawable drawable_b = getResources().getDrawable(R.drawable.ic_dropdown_gray);
//	 drawable_b.setBounds(0, 0, drawable_n.getMinimumWidth(),drawable_n.getMinimumHeight());  //此为必须写的
//	 drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(),drawable_n.getMinimumHeight());  //此为必须写的
//	 ((TextView)view).setCompoundDrawables(null, null, drawable_n, null);
	 v.setBackgroundResource(R.drawable.ic_droptop_gray);
	 
	 
	 

//	 	img_arr.setImageResource(R.drawable.ic_droptop_gray);
//	 	if(typelist==null||typelist.length<=0){
//	 		return;
//	 	}
//	 
//		final List<String> list = new ArrayList<String>();
//		for (int i = 0; i < typelist.length; i++) {
//			String item  = 10-Double.parseDouble(typelist[i])*10+"折(让利"+(int)(Double.parseDouble(typelist[i])*100)+"%)";
//			list.add(item);
//		}
		
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(activity).inflate(
                R.layout.currency_pop1, null);
        
        // 设置按钮的点击事件
		ListView listview = (ListView) contentView.findViewById(R.id.poplist);
		ArrayAdapter popadapter = new ArrayAdapter<String>(activity, R.layout.pop_name1,typelist);
		listview.setAdapter(popadapter);
		

        final PopupWindow popupWindow = new PopupWindow(contentView,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        
        popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
        });
        
        ColorDrawable dw = new ColorDrawable(00000000);  
        //设置SelectPicPopupWindow弹出窗体的背景  
        popupWindow.setBackgroundDrawable(dw);  
//		WindowManager.LayoutParams lp = getWindow().getAttributes();
//		lp.alpha = 0.7f;
//		getWindow().setAttributes(lp);
        // 设置好参数之后再show
//        popupWindow.showAtLocation(view,Gravity.BOTTOM,(int) 100,0);
        
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			 popupWindow.showAsDropDown(view,Gravity.NO_GRAVITY,0,0);
		}else{
			 popupWindow.showAtLocation(view,Gravity.BOTTOM,0,0);
		}
        
        listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				 ((TextView)view).setText(typelist.get((int)arg3));
				 rate = Double.parseDouble(typelist1.get((int)arg3).rate);
				 ((TextView)tv_withdrawals).setText(typelist.get((int)arg3));
				 if(typelist1.get((int)arg3).check.equals("T7_WITHDRAW")){
					 type = "3";
					 tv_rate_pro .setText("手续费5秀点");
				 }else if(typelist1.get((int)arg3).check.equals("T3_WITHDRAW")){
					 type = "2";
					 tv_rate_pro .setText(rate+"%手续费,最少5个秀点");
				 }else if(typelist1.get((int)arg3).check.equals("T1_WITHDRAW")){
					 type = "1";
					 tv_rate_pro .setText(rate+"%手续费,最少5个秀点");
				 }
//				 if((int)arg3==0){
//					 rate = 7;
//				 }else if((int)arg3==1){
//					 rate = 3;
//				 }else if((int)arg3==2){
//					 rate = 1;
//				 }
//				 tv_tax.setVisibility(View.GONE);
//				 ed_balance.setText("");
				 if(ed_balance.getText()!=null){
					 setnum();
				 }
//				((TextView)view).setCompoundDrawables(null, null, drawable_b, null);
				 v.setBackgroundResource(R.drawable.ic_dropdown_gray);
				popupWindow.dismiss();
			}
		});
        
        popupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				v.setBackgroundResource(R.drawable.ic_dropdown_gray);
//				 ((TextView)view).setCompoundDrawables(null, null, drawable_b, null);
			}
		});
    }

public void setnum(){
	 if(ed_balance.getText().toString().length()>0){
			try {
				double b = Double.parseDouble(ed_balance.getText().toString());
				if(b>=Double.parseDouble(MIN_WITHDRAW)/100){
//					String a = ((b-(b*((Double.parseDouble(INCOME_TAX))/100)))-(Double.parseDouble(WITHDRAW_FEE)/100))+"";
//					if(b*rate<5){
//					tv_tax.setText("实际到账   : "+(b-5)+"元");
//					BigDecimal mData = new BigDecimal(b-5).setScale(2, BigDecimal.ROUND_HALF_UP);
//					tv_tax.setText("实际到账   : "+mData+"元");
//					}else{
//					String a = String.valueOf(b-b*rate);
//					BigDecimal mData = new BigDecimal(a).setScale(2, BigDecimal.ROUND_HALF_UP);
//					tv_tax.setText("实际到账   : "+mData+"元");
//					}
					
					
				tv_tax.setVisibility(View.VISIBLE);
//				String a = ((b-(b*((Double.parseDouble(INCOME_TAX))/100)))-(Double.parseDouble(WITHDRAW_FEE)/100))+"";
			
				if(b*(rate/100)<=5){
					BigDecimal mData = new BigDecimal(b-5).setScale(2, BigDecimal.ROUND_HALF_UP);
					tv_tax.setText("实际到账   : "+mData+"元");
				}else{
					String a = String.valueOf(b-b*(rate/100));
					BigDecimal mData = new BigDecimal(a).setScale(2, BigDecimal.ROUND_HALF_UP);
					tv_tax.setText("实际到账   : "+mData+"元");
				}

				}else{
					tv_tax.setVisibility(View.GONE);
				}
			} catch (Exception e) {
				// TODO: handle exception
				ViewUtil.showError(ed_balance, "金额有误");
			}

		}else {
			tv_tax.setVisibility(View.GONE);
		}
}

	@Override
	public void onStart() {
		super.onStart();
		getView(R.id.imgTopBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
	}
}
