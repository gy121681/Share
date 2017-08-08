package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.AssignmentRoleBean;
import com.shareshenghuo.app.user.network.bean.InvestmentProjectBean;
import com.shareshenghuo.app.user.network.request.FilialobeRequest;
import com.shareshenghuo.app.user.network.request.getUserOrShopRequest;
import com.shareshenghuo.app.user.network.response.AutResponse;
import com.shareshenghuo.app.user.network.response.GetUserOrShopResponse;
import com.shareshenghuo.app.user.network.response.InvestmentConfigResponse;
import com.shareshenghuo.app.user.network.response.InvestmentProjectResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.MD5Utils;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
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


import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class FilialTransferActivity extends BaseTopActivity implements OnClickListener{
	
	private TextView tv_choose_type,tv_choose_shop,tv_clear,tv_name,tv_info,tv_nums,tv_nums1,tv_num;
	private EditText ed_num,ed_phone;
	private LinearLayout lin_type,lin_type1,ll_num;
	private List<AssignmentRoleBean> typelists;
	private TwoButtonDialog downloadDialog;
	private  List<InvestmentProjectBean>  shoplists;
	private String num = "";
	private Button llWalletRecharge;
	private boolean isinvestmentTypes = true;
	private String types = "";
	private String investmentProjectId = "";
	private String userOrShopId = "";
	private String userType = "";
	private MyEditDialog doubleWarnDialog1;
	private boolean istran = false;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.flial_transfer_activity);
		num = getIntent().getStringExtra("num");
		initview();
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		ProgressDialogUtil.showProgressDlg(this, "");
		FilialobeRequest req = new FilialobeRequest();
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.INVESTMENTCONTROLLER, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				Log.e("", ""+resp.result.toString());
				InvestmentConfigResponse bean = new Gson().fromJson(resp.result, InvestmentConfigResponse.class);
				if(Api.SUCCEED == bean.result_code){
					if(bean.data.investmentIsOpen.equals("0")){
						initDialog1("抱歉,该功能暂停使用", " 确定", "");
						return;
					}
					typelists = bean.data.assignmentRole;
					tv_info.setText(bean.data.investmentExplain);
				}
			}
		});
		
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("转让");
		tv_choose_type = getView(R.id.tv_choose_type);
		tv_choose_type.setOnClickListener(this);
		tv_choose_shop = getView(R.id.tv_choose_shop);
		tv_choose_shop.setOnClickListener(this);
		lin_type = getView(R.id.lin_type);
		lin_type1 = getView(R.id.lin_type1);
		ed_phone = getView(R.id.ed_phone);
		tv_info = getView(R.id.tv_info);
		tv_clear = getView(R.id.tv_clear);
		tv_clear.setOnClickListener(this);
		tv_name = getView(R.id.tv_name);
		ed_num = getView(R.id.ed_num);
		tv_nums1 = getView(R.id.tv_nums1);
		tv_nums = getView(R.id.tv_nums);
		ed_num.setOnClickListener(this);
		llWalletRecharge = getView(R.id.llWalletRecharge);
		llWalletRecharge.setOnClickListener(this);
		tv_num = getView(R.id.tv_num);
		ll_num = getView(R.id.ll_num);
		tv_num.setText(num);
		ed_phone.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					if(TextUtils.isEmpty(types)){
						T.showShort(getApplicationContext(), "请先选择转让角色");
//						ed_phone.clearFocus();
					}else{
//						ed_phone.requestFocus();
					}
				}
			}
		});
		
		ed_phone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(arg0.length()>0){
					tv_clear.setVisibility(View.VISIBLE);
					if(arg0.length()>=11){
						initname(ed_phone.getText().toString(),types);
					}
				}else{
					tv_name.setText("");
					tv_name.setVisibility(View.GONE);
					tv_clear.setVisibility(View.GONE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		
	}
	
	private void initname(String string, final String types2) {
		// TODO Auto-generated method stub
		
		
		ProgressDialogUtil.showProgressDlg(FilialTransferActivity.this, "");
		getUserOrShopRequest req = new getUserOrShopRequest();
		req.account = string;
		req.userType = types2;
		
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETUSERORSHOPNAME, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				Log.e("", ""+resp.result.toString());
				GetUserOrShopResponse bean = new Gson().fromJson(resp.result, GetUserOrShopResponse.class);
				if(Api.SUCCEED == bean.result_code){
					if(bean.data.RSPCOD.equals("000000")){
						istran = true;
							tv_name.setVisibility(View.VISIBLE);
							tv_name.setText(bean.data.RSPDATA.user_shop_name);
							userType = bean.data.RSPDATA.user_type;
							userOrShopId = bean.data.RSPDATA.user_shop_id;
						
					}else if(bean.data.RSPCOD.equals("000002")){
						istran = false;
						tv_name.setText("未实名认证,不可进行操作");
						tv_name.setVisibility(View.VISIBLE);
						userType = "";
						userOrShopId = "";
					}else {
						istran = false;
						tv_name.setVisibility(View.VISIBLE);
						tv_name.setText(bean.data.RSPMSG);
					}

//					if(types2.equals("1")){
//						if(bean.data.is_cerifcation_status.equals("2")){
//							tv_name.setText(bean.data.user_shop_name);
//						}else{
//							tv_name.setText("未实名认证");
//						}
//					}
					
//					if(bean.data.real_name!=null&&!bean.data.real_name.equals("null")){
						
//						if(types2.equals("1")){
//							tv_name.setText(bean.data.real_name);
//						}else{
//							tv_name.setText(bean.data.legal_person_name);
//						}

//						private String userOrShopId = "";
//						private String userType = "";
						
//					}else{
//						tv_name.setText("未知");
//					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_choose_type:
			if (typelists!=null&&typelists.size()>0) {
				final List<String> typelist  = new ArrayList<String>();
				for (int i = 0; i < typelists.size(); i++) {
					typelist.add(typelists.get(i).beizhu);
				}
				showPopupWindow(tv_choose_type,typelist,0);
			}else{
				init();
				T.showShort(getApplicationContext(), "网络异常请重试");
			}

			break;
			
		case R.id.tv_choose_shop:
			if (shoplists!=null&&shoplists.size()>0) {
				final List<String> typelist1  = new ArrayList<String>();
				for (int i = 0; i < shoplists.size(); i++) {
					typelist1.add(shoplists.get(i).project_name);
				}
				showPopupWindow(tv_choose_shop,typelist1,1);
			}else{
//				initshoptype();
				T.showShort(getApplicationContext(), "请选择转让角色");
			}
			break;
			
		case R.id.tv_clear:
			tv_name.setText("");
			ed_phone.setText("");
			tv_clear.setVisibility(View.GONE);
			break;
		case R.id.llWalletRecharge:
			
			if(num!=null&&Double.parseDouble(num)<=0){
				 T.showShort(getApplicationContext(),"没有剩余秀心");
				 return;
			}

			
	 		if(TextUtils.isEmpty(types)){
				 T.showShort(getApplicationContext(),"请选择转让角色");
				 return;
	 		}
			
		 	if(!isinvestmentTypes){
			 	if(ed_phone.getText()==null||ed_phone.getText().length()<11){
					 T.showShort(getApplicationContext(),"请输入手机号");
					 return;
				 }
			 	
		 	}else{
		 		if(TextUtils.isEmpty(investmentProjectId)){
					 T.showShort(getApplicationContext(),"请选择特定商家");
					 return;
		 		}
		 	}
		 
		 	if(ed_num.getText()==null||ed_num.getText().length()<=0){
			 T.showShort(getApplicationContext(),"请输入转让份数");
			 return;
		 	}
			 
				if(!isinvestmentTypes){
					if(ed_num.getText()!=null&&num!=null&&Double.parseDouble(ed_num.getText().toString())>Double.parseDouble(num)){
						 T.showShort(getApplicationContext(),"剩余秀心不足");
						 return;
					}
			 
		 	}
		 	
//		 	if(!istran){
//				 T.showShort(getApplicationContext(),"不可转让");
//				 return;
//		 	}
			shwpaypwd();
			break;
		default:
			break;
		}
		
	}
	
	protected void shwpaypwd() {
		doubleWarnDialog1 = new MyEditDialog(FilialTransferActivity.this,
				R.style.loading_dialog, "兑换", "请输入支付密码", "确认", "取消", "",
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
				
				Transfer(paypwd);
				
			}
		});
		doubleWarnDialog1.setCancelable(false);
		doubleWarnDialog1.setCanceledOnTouchOutside(false);
		doubleWarnDialog1.show();
		
	}
	
	
	 private void Transfer(String paypwd) {
		// TODO Auto-generated method stub
			ProgressDialogUtil.showProgressDlg(FilialTransferActivity.this, "");
			getUserOrShopRequest req = new getUserOrShopRequest();
			req.investmentUserType = "1";
			req.investmentUserId = UserInfoManager.getUserInfo(this).id+"";
			req.investmentAccount = UserInfoManager.getUserInfo(this).mobile;
			req.filialPietyNum = ed_num.getText().toString();
			if(isinvestmentTypes){
				req.investmentType = "0";
			}else{
				req.investmentType = "1";
			}
			req.userOrShopId = userOrShopId;
			req.investmentProjectId = investmentProjectId;
			req.userType = userType;
			
			for(int i=0; i<3; i++)
				paypwd = MD5Utils.getMD5String(paypwd);
			req.payPassword = paypwd;
			Log.e("", ""+req.toString());
			RequestParams params = new RequestParams();
			try {
				params.setBodyEntity(new StringEntity(req.toJson()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			new HttpUtils().send(HttpMethod.POST, Api.GENERATECONTROLLER, params, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					ProgressDialogUtil.dismissProgressDlg();
				}

				@Override
				public void onSuccess(ResponseInfo<String> resp) {
					ProgressDialogUtil.dismissProgressDlg();
					Log.e("", ""+resp.result.toString());
					AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
					if(Api.SUCCEED == bean.result_code){
						if(bean.data.RSPCOD.equals("000000")){
							initDialog1(bean.data.RSPMSG, "确定", "");
							if(doubleWarnDialog1!=null){
								doubleWarnDialog1.dismiss();
							}
						}else{
							T.showShort(getApplicationContext(), bean.data.RSPMSG);
						}
					}
				}
			});
	}

	private void showPopupWindow(final View view, final List<String> typelist, final int i) {
		 
		 
		 final Drawable drawable_n = getResources().getDrawable(R.drawable.ic_droptop_gray);
		 final Drawable drawable_b = getResources().getDrawable(R.drawable.ic_dropdown_gray);
		 drawable_b.setBounds(0, 0, drawable_n.getMinimumWidth(),drawable_n.getMinimumHeight());  //此为必须写的
		 drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(),drawable_n.getMinimumHeight());  //此为必须写的
		 ((TextView)view).setCompoundDrawables(null, null, drawable_n, null);
		 
		 
		 

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
	        View contentView = LayoutInflater.from(FilialTransferActivity.this).inflate(
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
					 ((TextView)view).setText(typelist.get((int)arg3));
					switch (i) {
					case 0:
							if(typelists.get((int)arg3).other_key.equals("INVESTMENT_SHOP_ISOPEN")){
								lin_type.setVisibility(View.GONE);
								lin_type1.setVisibility(View.VISIBLE);
								isinvestmentTypes = false;
								types = "2";
								ll_num.setVisibility(View.GONE);
							}else if(typelists.get((int)arg3).other_key.equals("INVESTMENT_USER_ISOPEN")){
								lin_type.setVisibility(View.GONE);
								lin_type1.setVisibility(View.VISIBLE);
								isinvestmentTypes = false;
								types = "1";
								ll_num.setVisibility(View.GONE);
							}else if(typelists.get((int)arg3).other_key.equals("INVESTMENT_SPECIAL_ISOPEN")){
								lin_type.setVisibility(View.VISIBLE);
								lin_type1.setVisibility(View.GONE);
								isinvestmentTypes = true;
								types = "3";
								
								tv_name.setText("");
								tv_name.setVisibility(View.GONE);
								initshoptype();
							}
						break;
					case 1:
						ll_num.setVisibility(View.VISIBLE);
						tv_nums.setText(shoplists.get((int)arg3).min_filial_count);
	        			tv_nums1.setText(shoplists.get((int)arg3).max_filial_count);
	        			investmentProjectId = shoplists.get((int)arg3).id;
						break;
					default:
						break;
					}
					
					((TextView)view).setCompoundDrawables(null, null, drawable_b, null);
					popupWindow.dismiss();
				}
			});
	        
	        popupWindow.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					 ((TextView)view).setCompoundDrawables(null, null, drawable_b, null);
				}
			});
	    }
	 

		private void initshoptype() {
			// TODO Auto-generated method stub
			ProgressDialogUtil.showProgressDlg(this, "");
			FilialobeRequest req = new FilialobeRequest();
			RequestParams params = new RequestParams();
			try {
				params.setBodyEntity(new StringEntity(req.toJson()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			new HttpUtils().send(HttpMethod.POST, Api.GETPROJECTCONTROLLER, params, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					ProgressDialogUtil.dismissProgressDlg();
				}

				@Override
				public void onSuccess(ResponseInfo<String> resp) {
					ProgressDialogUtil.dismissProgressDlg();
					Log.e("", ""+resp.result.toString());
					InvestmentProjectResponse bean = new Gson().fromJson(resp.result, InvestmentProjectResponse.class);
					if(Api.SUCCEED == bean.result_code){
						shoplists = bean.data;
//						if(bean.data.investmentIsOpen.equals("0")){
//							initDialog1("抱歉,该功能暂停使用", " 确定", "");
//							return;
//						}
//						typelists = bean.data.assignmentRole;
//						tv_info.setText(bean.data.investmentExplain);
					}
				}
			});
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
								finish();
								break;
							case R.id.Button_cancel:
								downloadDialog.dismiss();
								finish();
							default:
								break;
							}
						}
					});
				downloadDialog.show();
				downloadDialog.setCancelable(false);
			}

}
