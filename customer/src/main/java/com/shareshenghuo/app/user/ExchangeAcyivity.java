package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.activity.realname.RealnameStepIDCardActivity;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.AccountInfoBean;
import com.shareshenghuo.app.user.network.bean.MyBankCardBean;
import com.shareshenghuo.app.user.network.request.AccountInfoRequest;
import com.shareshenghuo.app.user.network.request.NumRequest;
import com.shareshenghuo.app.user.network.request.UserWithDrawRequest;
import com.shareshenghuo.app.user.network.response.AccountInfoRespones;
import com.shareshenghuo.app.user.network.response.BankCardListResponse;
import com.shareshenghuo.app.user.network.response.NumResponse;
import com.shareshenghuo.app.user.network.response.WithdrawalsResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.MD5Utils;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.Util;
import com.shareshenghuo.app.user.util.ViewUtil;
import com.shareshenghuo.app.user.widget.dialog.BankListChoiceDialog;
import com.shareshenghuo.app.user.widget.dialog.MyEditDialog;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.user.widget.dialog.TwoButtonDialog;
import com.shareshenghuo.app.user.widget.dialog.onMyaddTextListener;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
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

public class ExchangeAcyivity extends BaseTopActivity {
	
	private TextView tv_explain, tv_arr,tv_rate_pro,tv_tax,tv_tax1,tv_surplus,tv_withdrawals_num,card_no,bank_name,tv_withdrawals,tv_withdrawals_num1;
	private EditText ed_balance;
	private String WITHDRAW_FEE = "",text = "'";//提现手续费
	private ImageView  bank_img;
	private String INCOME_TAX = "";//个人所得税
	private Button llWalletRecharge;
	private MyEditDialog doubleWarnDialog1;
	private String filialMoney ;
	private String filialMoneys ;
	private String MIN_WITHDRAW = "";//最小金额
	private TextView tv_pro;	
	private TwoButtonDialog downloadDialog;
	private BankListChoiceDialog multiChoiceDialog;
	private  BankListChoiceDialog.Builder multiChoiceDialogBuilder;
	private RelativeLayout re_bankcard;
	private List<MyBankCardBean> datas;
	private String bankname,banknum = "";
	private double  rate = 0.7;
	private RelativeLayout tv_repop;
	private List<RateTypeBean> typelist;
	private String filialMoneyNew = "";
	private String type = "1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exchange);
//		filialMoney = getIntent().getStringExtra("filialMoney");
		initView();
		userAccountInfoQuery();
		getStatisticsData() ;
		
	}
	
	public  class RateTypeBean{
		public  String rate;
		public  String beizhu;
		public String check;
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		
		loadData();
	}
	
	public void initrenale(){

		
		if(UserInfoManager.getUserInfo(ExchangeAcyivity.this).certification_status==null||
		!UserInfoManager.getUserInfo(ExchangeAcyivity.this).certification_status.equals("2")){
			
			if(UserInfoManager.getUserInfo(this).is_can_certification.equals("0")){
				initDialog1("实名认证失败,请联系客服", " 确定", "");
				return;
			}
			initrename();
			return;
		}else{
			
		}
	}
	
	private void initDialog1(String content,String left,String right) {
		// TODO Auto-generated method stub
		downloadDialog = new TwoButtonDialog(this, R.style.CustomDialog,
				"", content, left, right,true,new OnMyDialogClickListener() {
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
			downloadDialog.show();
		}
	
	public void loadData() {

		MyBankCardBean req = new MyBankCardBean();
		req.user_id = UserInfoManager.getUserInfo(this).id+"";
		req.user_type = "1";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETWITHDRAWCARDS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
//				refreshscrollview.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
//				refreshscrollview.onRefreshComplete();
				Log.e("", ""+resp.result);
				BankCardListResponse bean = new Gson().fromJson(resp.result, BankCardListResponse.class);
				if(Api.SUCCEED == bean.result_code)
					datas = bean.data;
				if(datas.size()<=0){
					
					if(UserInfoManager.getUserInfo(ExchangeAcyivity.this).certification_status.equals("2")){
						initDialog("没有卡用作兑换,是否去添加一张储蓄卡?", "取消", "确定");
					}
					
					bank_name.setText("添加一张银行卡");
				}else{
//					bank_name.setText("选择银行卡");
					if(datas.get(0).bank_name!=null){
						bankname = datas.get(0).bank_name;
						bank_name.setText(bankname);
					}
					if(datas.get(0).card_no!=null){
						banknum = datas.get(0).card_no;
					}
					
					if(!TextUtils.isEmpty(banknum)&&banknum.length()>4){
					card_no.setText(banknum.substring(0, 4)+" **** **** "+banknum.substring(banknum.length()-4));
					}
					
//					int teg = 
					datas.get(0).bos = true;
					
					if(bankname!=null){
						ViewUtil.setbank(bank_img, bankname);
					}
					
				}
			}
		});
	}
	
	

	private void initView() {
		// TODO Auto-generated method stub
		initTopBar("兑换");
		initrenale();
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("兑换记录");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(ExchangeAcyivity.this,ExchangeFmListAcyivity.class);
				startActivity(it);
			}
		});
		tv_repop=  getView(R.id.tv_repop);
		tv_arr = getView(R.id.tv_arr);
		tv_rate_pro = getView(R.id.tv_rate_pro);
		tv_withdrawals= getView(R.id.tv_withdrawals);
		tv_explain = getView(R.id.tv_explain);
		tv_tax = getView(R.id.tv_tax);
		tv_tax1 = getView(R.id.tv_tax1);
		bank_name = getView(R.id.bank_name);
		ed_balance = getView(R.id.ed_balance);
		bank_img = getView(R.id.bank_img);
		tv_surplus = getView(R.id.tv_surplus);
		card_no = getView(R.id.card_no);
		tv_pro = getView(R.id.tv_pro);
		re_bankcard = getView(R.id.re_bankcard);
		tv_pro.setText(Html.fromHtml("<u>说明  </u>"));
		tv_withdrawals_num1 = getView(R.id.tv_withdrawals_num1);
		
		tv_withdrawals_num = getView(R.id.tv_withdrawals_num);
//		tv_surplus.setText(Util.getIntnum(filialMoney,false)+"");
//		tv_withdrawals_num.setText(Util.getIntnum(filialMoney,false)+"");
		llWalletRecharge = getView(R.id.llWalletRecharge);
//		if(!TextUtils.isEmpty(UserInfoManager.getUserInfo(this).card_no)&&UserInfoManager.getUserInfo(this).card_no.length()>4){
//			String card = UserInfoManager.getUserInfo(this).card_no;
//			card_no.setText(card.substring(0, 4)+" **** **** "+card.substring(card.length()-4));
//		}
//		if(!TextUtils.isEmpty(UserInfoManager.getUserInfo(this).bank_name)){
//			String bank = UserInfoManager.getUserInfo(this).bank_name;
//			bank_name.setText(bank);
//			setbank(bank_img,bank);
//		}
//		tv_withdrawals.setText("1个工作日到账(T+1)\n0.7%手续费,最少5个秀点");
		
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
		
		
		tv_pro.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initPro();
			}
		});
		re_bankcard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(UserInfoManager.getUserInfo(ExchangeAcyivity.this).is_can_certification.equals("0")){
					initDialog1("实名认证失败,请联系客服", " 确定", "");
					return;
				}
				
				if(UserInfoManager.getUserInfo(ExchangeAcyivity.this).certification_status==null||
				!UserInfoManager.getUserInfo(ExchangeAcyivity.this).certification_status.equals("2")){
					initrename();
					return;
				}
				if(datas!=null&&datas.size()<0){
//					if(UserInfoManager.getUserInfo(ExchangeAcyivity.this).certification_status==null||
//					!UserInfoManager.getUserInfo(ExchangeAcyivity.this).certification_status.equals("2")){
//						initrename();
//					}else{
						startActivity(new Intent(ExchangeAcyivity.this,AddBankCardActivity.class));
//						Intent it= new Intent(ExchangeAcyivity.this,AddBankCardActivity.class);
//						it.putExtra("tag", "2");
//						startActivity(it);
//					}
					

//					startActivity(new Intent(ExchangeAcyivity.this,AddBankCardActivity.class));
//					showMultiChoiceDialog(view);
//					loadData();
//					return;
				}else{
					if(datas!=null&&datas.size()>0){
						showMultiChoiceDialog();
					}else{
						startActivity(new Intent(ExchangeAcyivity.this,AddBankCardActivity.class));
//						Intent it= new Intent(ExchangeAcyivity.this,AddBankCardActivity.class);
//						it.putExtra("tag", "2");
//						startActivity(it);
					}
				}
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
//							String a = ((b-(b*((Double.parseDouble(INCOME_TAX))/100)))-(Double.parseDouble(WITHDRAW_FEE)/100))+"";
//							if(b*rate<5){
//							tv_tax.setText("实际到账   : "+(b-5)+"元");
//							BigDecimal mData = new BigDecimal(b-5).setScale(2, BigDecimal.ROUND_HALF_UP);
//							tv_tax.setText("实际到账   : "+mData+"元");
//							}else{
//							String a = String.valueOf(b-b*rate);
//							BigDecimal mData = new BigDecimal(a).setScale(2, BigDecimal.ROUND_HALF_UP);
//							tv_tax.setText("实际到账   : "+mData+"元");
//							}
							
							
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
				
				initrenale();
//				if(UserInfoManager.getUserInfo(ExchangeAcyivity.this).certification_status==null||
//						!UserInfoManager.getUserInfo(ExchangeAcyivity.this).certification_status.equals("2")){
//					initrename();
//					return;
//				}

				if(TextUtils.isEmpty(banknum)){
					T.showShort(getApplicationContext(), "请选择银行卡");
					showMultiChoiceDialog();
					return;
				}
				
				
				if(ViewUtil.checkEditEmpty(ed_balance, "请输入兑换秀点"))
					return;
				
				if(filialMoneys!=null&&Double.parseDouble(ed_balance.getText().toString())>Double.parseDouble(filialMoneys)/100){
					ViewUtil.showError(ed_balance, "秀点不足");
					return;
				}
				if(!TextUtils.isEmpty(MIN_WITHDRAW)){
					if(Double.parseDouble(ed_balance.getText().toString())<Double.parseDouble(MIN_WITHDRAW)/100){
						ViewUtil.showError(ed_balance, "最低兑换秀点:"+Double.parseDouble(MIN_WITHDRAW)/100);
						return;
					}
				}

				double db =  Double.parseDouble(ed_balance.getText().toString());
				if(db%100!=0){
					
					  ViewUtil.showError(ed_balance, "100整数倍");
					  return;
				}
				shwpaypwd();
			}
		});
		

		
	}
	
	
	
	public void showMultiChoiceDialog() {
		if(datas!=null&&datas.size()>0){
		multiChoiceDialogBuilder = new BankListChoiceDialog.Builder(ExchangeAcyivity.this,"2");
		 multiChoiceDialog = multiChoiceDialogBuilder.setTitle("请选择一张银行卡")
				.setMultiChoiceItems(datas, new BaklistitemListener(), true)
				.setPositiveButton("添加新卡", new  PositiveClickListener()).setNegativeButton("取消", null).create();
		multiChoiceDialog.show();
		}
	}
	
	class PositiveClickListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			multiChoiceDialog.dismiss();
			initrenale();
//			if(UserInfoManager.getUserInfo(ExchangeAcyivity.this).certification_status==null||
//			!UserInfoManager.getUserInfo(ExchangeAcyivity.this).certification_status.equals("2")){
//				initrename();
//			return;	
//			}
			startActivity(new Intent(ExchangeAcyivity.this,AddBankCardActivity.class));
//			Intent it= new Intent(ExchangeAcyivity.this,AddBankCardActivity.class);
//			it.putExtra("tag", "2");
//			startActivity(it);
		}
	}
	class BaklistitemListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			if(datas.get((int)arg3).is_support_withdraw!=null&&datas.get((int)arg3).is_support_withdraw.equals("0")){
				return;
			}
			multiChoiceDialog.dismiss();
			
			if(datas.get((int)arg3).bank_name!=null){
				bankname = datas.get((int)arg3).bank_name;
				bank_name.setText(bankname);
			}
			if(datas.get((int)arg3).card_no!=null){
				banknum = datas.get((int)arg3).card_no;
			}
			
			
			if(!TextUtils.isEmpty(banknum)&&banknum.length()>4){
			card_no.setText(banknum.substring(0, 4)+" **** **** "+banknum.substring(banknum.length()-4));
			}
			
			for (int i = 0; i < datas.size(); i++) {
				datas.get(i).bos = false;
			}
			datas.get((int)arg3).bos = true;
			
			if(bankname!=null){
				ViewUtil.setbank(bank_img, bankname);
			}
		}
	}
	
	public void getStatisticsData() {
		NumRequest req = new NumRequest();
		req.userId = UserInfoManager.getUserInfo(this).id+"";
		req.userType = "1";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETBUNBER, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				T.showNetworkError(ExchangeAcyivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				NumResponse bean = new Gson().fromJson(resp.result, NumResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					
					filialMoneyNew = bean.data.filialMoneyNew;
					filialMoney = bean.data.filialMoney;
			 		filialMoneys = filialMoneyNew;
					tv_surplus.setText(Util.getnum(filialMoneyNew,false)+"");
					if(Double.parseDouble(filialMoneyNew)/100>=100){
						tv_withdrawals_num.setText((int)((Double.parseDouble(filialMoneyNew)/10000))+"00");
					}else{
						tv_withdrawals_num.setText("0");
					}
					
	
//					tv_surplus.setText(Util.getnum(bean.data.filialMoney,false)+"");
//					
//					if(Double.parseDouble(bean.data.filialMoney)/100>=100){
//						tv_withdrawals_num.setText((int)((Double.parseDouble(bean.data.filialMoney)/10000))+"00");
//					}else{
//						tv_withdrawals_num.setText("0");
//					}

//					tv_withdrawals_num1.setText(Util.getnum(bean.data.consumeFilialMoney,false)+"");//再消费秀点
					
					
//					tv_withdrawals_num.setText(Util.getIntnum(bean.data.filialMoney,false)+"");
				}
			}
		});
	}
	
	private void userAccountInfoQuery() {
		// TODO Auto-generated method stub
		
		ProgressDialogUtil.showProgressDlg(this, "请稍后");
		AccountInfoRequest req = new AccountInfoRequest();
		req.otherGroup = "WITHDRAW";
//		req.otherGroup = "WITHDRAWALS_EXPLAIN";
		
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
				T.showNetworkError(ExchangeAcyivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				Log.e("", ""+resp.result);
				AccountInfoRespones bean = new Gson().fromJson(resp.result, AccountInfoRespones.class);
			
				if(Api.SUCCEED == bean.result_code) {
//					T.showShort(ExchangeAcyivity.this, "提交成功");
					inittext(bean.data);
				} else {
					T.showShort(ExchangeAcyivity.this, bean.result_desc);
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
//		}
//		String url = Api.CONSUMERSWITHDRAW1;
//		
//		if(w_type==0){
//			url = Api.CONSUMERSWITHDRAW1;
//		}else {
//			url = Api.CONSUMERSWITHDRAW1NEW;
//		}
		
		double b = Double.parseDouble(ed_balance.getText().toString());
		
		ProgressDialogUtil.showProgressDlg(this, "请稍后");
		UserWithDrawRequest req = new UserWithDrawRequest();
		req.userShopId= UserInfoManager.getUserInfo(this).id+"";
		req.userType = "1";
		req.bankName = bankname;
		req.cardNo = banknum;
		req.wdType = type;
		req.filialMoney = ((int)(b*100))+"";
		for(int i=0; i<3; i++)
			pwd = MD5Utils.getMD5String(pwd);
		
		req.payPassword = pwd;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.CONSUMERSWITHDRAW1, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ExchangeAcyivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				Log.e("", ""+resp.result);
				ProgressDialogUtil.dismissProgressDlg();
				WithdrawalsResponse bean = new Gson().fromJson(resp.result, WithdrawalsResponse.class);
			
				if(Api.SUCCEED == bean.result_code) {
					if(doubleWarnDialog1!=null){
						doubleWarnDialog1.dismiss();
					}
					if(bean.data.RSPCOD.equals("000000")){
						T.showShort(ExchangeAcyivity.this, bean.data.RSPMSG);
						finish();
					}else{
						T.showShort(ExchangeAcyivity.this, bean.data.RSPMSG);
					}
				} else {
					T.showShort(ExchangeAcyivity.this, bean.result_desc);
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
//	
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
		
		
		
				
		rate = Double.parseDouble(typelist.get(0).rate);
		tv_withdrawals.setText(typelist.get(0).beizhu);
		tv_rate_pro .setText(typelist.get(0).rate+"%手续费,最少5个秀点");
		tv_explain.setText(text);
//		tv_explain.setText("\n1.单笔最多可兑换"+Double.parseDouble(MAX_WITHDRAW)/100+"秀点\n2.每笔兑换扣除手续费"+Double.parseDouble(WITHDRAW_FEE)/100+"秀点\n3.秀点仅每月"+WITHDRAW_START_TIME+"-"+WITHDRAW_END_TIME+"日允许兑换，其他时间不允许兑换\n4.代扣税=兑换秀点×税率("+INCOME_TAX+"%"+")\n5.预计到账时间：T+1");
	}
	
	protected void shwpaypwd() {
		doubleWarnDialog1 = new MyEditDialog(ExchangeAcyivity.this,
				R.style.loading_dialog, "兑换", "请输入支付密码", "确认", "取消", ed_balance.getText().toString(),
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {

						switch (v.getId()) {
						case R.id.btn_right:
							doubleWarnDialog1.dismiss();
							InputMethodManager m=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
							break;
						case R.id.btn_left:
							String paypwd = doubleWarnDialog1.getpaypwd();

							if (paypwd == null || paypwd.equals("")) {
								Toast.makeText(getApplicationContext(),"请输入支付密码",
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
					Toast.makeText(getApplicationContext(),"请输入支付密码",
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
//				if(bank.substring(0,4).equals("中国银行")){
//					v.setImageResource(R.drawable.ps_ccb);
//				}else{
					v.setImageResource(R.drawable.ps_boc);
//				}
				
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

		
		downloadDialog = new TwoButtonDialog(this, R.style.CustomDialog,
				"再消费秀点说明", "再消费秀点不能直接兑换,只能在平台消费使用", "知道了", "",true,new OnMyDialogClickListener() {
					
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
	
	
	public void initrename(){
		if(downloadDialog!=null&&downloadDialog.isShowing()){
			return;
		}

		downloadDialog = new TwoButtonDialog(ExchangeAcyivity.this, R.style.CustomDialog,
				"尊敬的会员", "为保证您的资金安全\n在交易前需进行实名认证", " 取消", "开始认证",true,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							downloadDialog = null;
							break;
						case R.id.Button_cancel:
							startActivity(new Intent(ExchangeAcyivity.this, RealnameStepIDCardActivity.class));
							finish();
							downloadDialog.dismiss();
							downloadDialog = null;
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
//		downloadDialog.setCanceledOnTouchOutside(false);
		
			downloadDialog.show();
		}
	
	
	private void initDialog(String content,String left,String right) {
		// TODO Auto-generated method stub
		downloadDialog = new TwoButtonDialog(ExchangeAcyivity.this, R.style.CustomDialog,
				"", content, left, right,true,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							
							startActivity(new Intent(ExchangeAcyivity.this,AddBankCardActivity.class));
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
		}
	
	
	private void showPopupWindow(final View view, final List<RateTypeBean> typelist1, final int i,final TextView v) {
		
		if(typelist1==null||typelist1.size()<=0){
			return;
			
		}
		
		final List<String> typelist = new ArrayList<String>();
			for (int j = 0; j < typelist1.size(); j++) {
				typelist.add(typelist1.get(j).beizhu);
			}

		
//		if(typelist1!=null&&typelist1.size()>0){
//
//		}else{
//			if(i==100){
//
//			}else {
//				return;
//			}
//
//		}
		 

//		 final Drawable drawable_n = getResources().getDrawable(R.drawable.ic_droptop_gray);
//		 final Drawable drawable_b = getResources().getDrawable(R.drawable.ic_dropdown_gray);
//		 drawable_b.setBounds(0, 0, drawable_n.getMinimumWidth(),drawable_n.getMinimumHeight());  //此为必须写的
//		 drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(),drawable_n.getMinimumHeight());  //此为必须写的
//		 ((TextView)tv_withdrawals).setCompoundDrawables(null, null, drawable_n, null);
		 v.setBackgroundResource(R.drawable.ic_droptop_gray);
		 
		 

//		 	img_arr.setImageResource(R.drawable.ic_droptop_gray);
//		 	if(typelist==null||typelist.length<=0){
//		 		return;
//		 	}
//		 
//			final List<String> list = new ArrayList<String>();
//			for (int i = 0; i < typelist.length; i++) {
//				String item  = 10-Double.parseDouble(typelist[i])*10+"折(让利"+(int)(Double.parseDouble(typelist[i])*100)+"%)";
//				list.add(item);
//			}
			
	        // 一个自定义的布局，作为显示的内容
	        View contentView = LayoutInflater.from(ExchangeAcyivity.this).inflate(
	                R.layout.currency_pop1, null);
	        
	        // 设置按钮的点击事件
			ListView listview = (ListView) contentView.findViewById(R.id.poplist);
			ArrayAdapter popadapter = new ArrayAdapter<String>(this, R.layout.pop_name1,typelist);
			listview.setAdapter(popadapter);
			

	        final PopupWindow popupWindow = new PopupWindow(contentView,
	                android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
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
//			WindowManager.LayoutParams lp = getWindow().getAttributes();
//			lp.alpha = 0.7f;
//			getWindow().setAttributes(lp);
	        // 设置好参数之后再show
//	        popupWindow.showAtLocation(view,Gravity.BOTTOM,(int) 100,0);
	        
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
			 		filialMoneys = filialMoney;
					tv_surplus.setText(Util.getnum(filialMoney,false)+"");
					if(Double.parseDouble(filialMoney)/100>=100){
						tv_withdrawals_num.setText((int)((Double.parseDouble(filialMoney)/10000))+"00");
					}else{
						tv_withdrawals_num.setText("0");
					}
					 
					 if(i==100){
						 ((TextView)view).setText(typelist.get((int)arg3));
//						 	if(w_type==0){
//						 		
//						 		filialMoneys = filialMoneyNew;
//								tv_surplus.setText(Util.getnum(filialMoneyNew,false)+"");
//								if(Double.parseDouble(filialMoneyNew)/100>=100){
//									tv_withdrawals_num.setText((int)((Double.parseDouble(filialMoneyNew)/10000))+"00");
//								}else{
//									tv_withdrawals_num.setText("0");
//								}
//						 		
//						 	}else if(w_type==1){

//						 	}
					 }else{
						 tv_withdrawals.setText(typelist.get((int)arg3));
						 rate = Double.parseDouble(typelist1.get((int)arg3).rate);
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
						
						 if(ed_balance.getText()!=null){
							 setnum();
						 }
					 }
					 
					 
//					((TextView)tv_withdrawals).setCompoundDrawables(null, null, drawable_b, null);
					 v.setBackgroundResource(R.drawable.ic_dropdown_gray);
					popupWindow.dismiss();
				}
			});
	        
	        popupWindow.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					v.setBackgroundResource(R.drawable.ic_dropdown_gray);
//					 ((TextView)tv_withdrawals).setCompoundDrawables(null, null, drawable_b, null);
				}
			});
	    }
	
	public void setnum(){
		 if(ed_balance.getText().toString().length()>0){
				try {
					double b = Double.parseDouble(ed_balance.getText().toString());
					if(b>=Double.parseDouble(MIN_WITHDRAW)/100){
//						String a = ((b-(b*((Double.parseDouble(INCOME_TAX))/100)))-(Double.parseDouble(WITHDRAW_FEE)/100))+"";
//						if(b*rate<5){
//						tv_tax.setText("实际到账   : "+(b-5)+"元");
//						BigDecimal mData = new BigDecimal(b-5).setScale(2, BigDecimal.ROUND_HALF_UP);
//						tv_tax.setText("实际到账   : "+mData+"元");
//						}else{
//						String a = String.valueOf(b-b*rate);
//						BigDecimal mData = new BigDecimal(a).setScale(2, BigDecimal.ROUND_HALF_UP);
//						tv_tax.setText("实际到账   : "+mData+"元");
//						}
						
						
					tv_tax.setVisibility(View.VISIBLE);
//					String a = ((b-(b*((Double.parseDouble(INCOME_TAX))/100)))-(Double.parseDouble(WITHDRAW_FEE)/100))+"";
				
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
	
	
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		closeKeybord(this);
//	}
//	
//    /**
//     * 关闭软键盘
//     * 
//     * @param mEditText输入框
//     * @param mContext上下文
//     */
//    public  void closeKeybord( Context mContext)
//    {
//    	InputMethodManager m=(InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//    	m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//    }
}