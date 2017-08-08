package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.app.CityLifeApp;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.network.request.SetPaypwdRequest;
import com.shareshenghuo.app.user.network.response.SetPaypwdResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.AnimationUtil;
import com.shareshenghuo.app.user.util.MD5Utils;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.Util;
import com.shareshenghuo.app.user.widget.CountDownButton;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.user.widget.dialog.TwoButtonDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Realname2Activity extends BaseTopActivity{
	
	private TextView real_pointimg,real_pointimg1,real_pointimg2,tv_mobile,tv_pro,tv_pro1,tv_proerr;
	private EditText paypwd, paypwd1, paypwd2, paypwd3, paypwd4, paypwd5,edVCode;
	private String a = "", b = "", c = "", d =  "", e = "", f = "";
	private String pwd1 = "",pwd2 = "";
	private Button llWalletRecharge;
	private LinearLayout ll_verification,ll_pwd;
	private CountDownButton btnGetVCode;
	private TwoButtonDialog downloadDialog;
	public String mobiles = "",idcard,idname;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.realname2_layout);
		CityLifeApp.getInstance().addActivity(this);
		
		Intent it = getIntent();
		idcard = it.getStringExtra("idcard");
		idname = it.getStringExtra("idname");
		mobiles = UserInfoManager.getUserInfo(Realname2Activity.this).mobile;
		initview();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		btnGetVCode.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		btnGetVCode.onStop();
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("实名认证");
		real_pointimg = getView(R.id.real_pointimg);
		real_pointimg1 = getView(R.id.real_pointimg1);
		real_pointimg2 = getView(R.id.real_pointimg2);
		tv_mobile = getView(R.id.tv_mobile);
		tv_proerr = getView(R.id.tv_proerr);
		ll_verification = getView(R.id.ll_verification);
		btnGetVCode = getView(R.id.btnGetVCode);
		edVCode = getView(R.id.edVCode);
		ll_pwd = getView(R.id.ll_pwd);
		tv_pro = getView(R.id.tv_pro);
		tv_pro1 = getView(R.id.tv_pro1);
		btnGetVCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(!TextUtils.isEmpty(mobiles)){
					btnGetVCode.getVCode(mobiles, null);
				}
			}
		});
		real_pointimg.setBackgroundResource(R.drawable.bg_arc_blue);
		real_pointimg1.setBackgroundResource(R.drawable.bg_arc_blue);
		real_pointimg.setTextColor(getResources().getColor(R.color.white));
		real_pointimg1.setTextColor(getResources().getColor(R.color.white));
		findViewById(R.id.tvs1).setBackgroundResource(R.color.bg_top_bar);
		
		paypwd = (EditText) findViewById(R.id.searchCa);
		paypwd1 = (EditText) findViewById(R.id.searchCb);
		paypwd2 = (EditText) findViewById(R.id.searchCc);
		paypwd3 = (EditText) findViewById(R.id.searchCd);
		paypwd4 = (EditText) findViewById(R.id.searchCe);
		paypwd5 = (EditText) findViewById(R.id.searchCf);
		llWalletRecharge = getView(R.id.llWalletRecharge);
		
		paypwd.requestFocus();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		paypwd.setTransformationMethod(new AsteriskPasswordTransformationMethod());
		paypwd1.setTransformationMethod(new AsteriskPasswordTransformationMethod());
		paypwd2.setTransformationMethod(new AsteriskPasswordTransformationMethod());
		paypwd3.setTransformationMethod(new AsteriskPasswordTransformationMethod());
		paypwd4.setTransformationMethod(new AsteriskPasswordTransformationMethod());
		paypwd5.setTransformationMethod(new AsteriskPasswordTransformationMethod());

		initfocus();
		
		llTopBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					Signout();
			}
		});
		
		llWalletRecharge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub


				setpwd(pwd1);
				
//				Intent it  = new Intent(Realname2Activity.this,RealnameSenseActivity.class);
//				startActivity(it);

			}
		});
	}
	
	
	private void setpwd(String pwd) {
		// TODO Auto-generated method stub
		
		
		if(TextUtils.isEmpty(edVCode.getText())){
			T.showShort(Realname2Activity.this,"请输入短信验证码");
			return;
		}
		
		ProgressDialogUtil.showProgressDlg(this, "提交中");
		final SetPaypwdRequest req = new SetPaypwdRequest();
		for(int i=0; i<3; i++){
			pwd = MD5Utils.getMD5String(pwd);
		}
		req.type = "1";
		req.payPassword = pwd;
		req.userShopId = UserInfoManager.getUserInfo(this).id+"";
		req.msgId = btnGetVCode.getVCodeId();
		req.msgCode = edVCode.getText().toString();
		req.userType = "1";
		
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.SETPAYPASSWORD, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showShort(Realname2Activity.this, "失败");
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				SetPaypwdResponse bean = new Gson().fromJson(resp.result, SetPaypwdResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					if(bean.data.RSPCOD.equals("000000")){
						ll_verification.setVisibility(View.GONE);
						ll_pwd.setVisibility(View.VISIBLE);
						T.showShort(Realname2Activity.this, bean.data.RSPMSG);
						ll_verification.setVisibility(View.GONE);
						ll_pwd.setVisibility(View.VISIBLE);
						pwd1 = "";
						pwd2 = "";
						initnull();
						tv_pro.setText("请设置交易密码");
						tv_pro1.setText("该交易密码在支付、兑换时使用");
						Intent it  = new Intent(Realname2Activity.this,RealnameSenseActivity.class);
						it.putExtra("idcard", idcard);
						it.putExtra("idname", idname);
						startActivity(it);
					}else{
//						ll_verification.setVisibility(View.GONE);
//						ll_pwd.setVisibility(View.VISIBLE);
//						pwd1 = "";
//						pwd2 = "";
//						initnull();
						T.showShort(Realname2Activity.this,bean.data.RSPMSG);
					}
				
//					if(userInfo != null) {
//						String pwd = req.password;
//						for(int i=0; i<3; i++);
//							pwd = MD5Utils.getMD5String(pwd);
//						UserInfoManager.setUserPwd(FindPaypassActivity.this, pwd);
//					}
					
				} else {
					T.showShort(Realname2Activity.this, bean.result_desc);
				}
			}
		});
		
	}

	private void initfocus() {
		// TODO Auto-generated method stub
		
		paypwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					a = paypwd.getText().toString();
					paypwd.clearFocus();
//					paypwd.setEnabled(false);
//					paypwd1.setEnabled(true);
					paypwd1.requestFocus();

				}

			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		paypwd1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

				b = paypwd1.getText().toString();
				if (s.length() == 1) {
					
					paypwd1.clearFocus();
//					paypwd2.setEnabled(true);
					paypwd2.requestFocus();
//					paypwd1.setEnabled(false);
				} else {
					
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		paypwd2.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (s.length() == 1) {
					c = paypwd2.getText().toString();
					paypwd2.clearFocus();
//					paypwd3.setEnabled(true);
					paypwd3.requestFocus();
//					paypwd2.setEnabled(false);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		paypwd3.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (s.length() == 1) {
					d = paypwd3.getText().toString();
					paypwd3.clearFocus();
//					paypwd4.setEnabled(true);
					paypwd4.requestFocus();
//					paypwd3.setEnabled(false);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		paypwd4.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (s.length() == 1) {
					e = paypwd4.getText().toString();
					paypwd4.clearFocus();
//					paypwd5.setEnabled(true);
					paypwd5.requestFocus();
//					paypwd4.setEnabled(false);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
		paypwd5.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					f = paypwd5.getText().toString();
//					paypwd5.setEnabled(false);
					if(getpaypwd().length()<6){
						pwd1 = "";
						pwd2 = "";
						initnull();
						tv_proerr.setText("密码必须是6位");
						AnimationUtil.BtnSpecialAnmations1(Realname2Activity.this, tv_proerr, 500,500);
						return;
					}
					
					if(Util.paypwd(getpaypwd())){
						pwd1 = "";
						initnull();
						tv_proerr.setText("密码安全等级不够，请重新设置");
						AnimationUtil.BtnSpecialAnmations1(Realname2Activity.this, tv_proerr, 500,500);
						return;
					}
					
					if(TextUtils.isEmpty(pwd1)){
						pwd1 = getpaypwd();
						initnull();
						tv_proerr.setText("");
						tv_pro.setText("确认交易密码");
						tv_pro1.setText("请再次输入");
						return;
					}
					
					if(TextUtils.isEmpty(pwd2)){
						pwd2 = getpaypwd();
					}
					
					if(!pwd1.equals(pwd2)){
						pwd1 = "";
						pwd2 = "";
						initnull();
						tv_proerr.setText("两次密码不一致,请重新输入");
						tv_pro.setText("请设置交易密码");
						tv_pro1.setText("该交易密码在支付、兑换时使用");
						AnimationUtil.BtnSpecialAnmations1(Realname2Activity.this, tv_proerr, 500,500);
//						T.showShort(Realname2Activity.this, "两次密码不一致,请重新输入");
						return;
					}
					
					
					
//					Intent it  = new Intent(Realname2Activity.this,RealnameStepIDCardActivity.class);
//					startActivity(it);
//					if(!TextUtils.isEmpty(pwd2)){
					
					
					if(!TextUtils.isEmpty(mobiles)){
						UserInfo userInfo = UserInfoManager.getUserInfo(Realname2Activity.this);
						tv_mobile.setText(mobiles);
						btnGetVCode.getVCode(mobiles, null);	
					}else{
//						UserInfo userInfo = UserInfoManager.getUserInfo(Realname2Activity.this);
//						tv_mobile.setText(userInfo.mobile);
//						btnGetVCode.getVCode(userInfo.mobile, null);	
					}
					((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(Realname2Activity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//					}
					ll_verification.setVisibility(View.VISIBLE);
					ll_pwd.setVisibility(View.GONE);
					
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
		
	}
	
	
	private void initnull() {
		// TODO Auto-generated method stub
		paypwd.requestFocus();
		paypwd.setText("");
		paypwd1.setText("");
		paypwd2.setText("");
		paypwd3.setText("");
		paypwd4.setText("");
		paypwd5.setText("");
	}
	
	public String getpaypwd() {
		
//		pwd1 = a + b + c + d + e + f;

		return a + b + c + d + e + f;

	}
	
	public String getpaypwd1() {
//		pwd2 = a + b + c + d + e + f;
		return a + b + c + d + e + f;

	}

	public class AsteriskPasswordTransformationMethod extends
		PasswordTransformationMethod {
		@Override
	public CharSequence getTransformation(CharSequence source, View view) {
		return new PasswordCharSequence(source);
	}

	private class PasswordCharSequence implements CharSequence {
	private CharSequence mSource;

	public PasswordCharSequence(CharSequence source) {
		mSource = source; // Store char sequence

	}

	@Override
	public char charAt(int index) {
		return '*'; // This is the important part
	}

	@Override
	public int length() {
		return mSource.length(); // Return default
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return mSource.subSequence(start, end); // Return default
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

	}
	
	@Override  
    public boolean dispatchKeyEvent(KeyEvent event) {  
		if(event.getKeyCode() == KeyEvent.KEYCODE_DEL && event.getAction() != KeyEvent.ACTION_UP){
        	if(paypwd.isFocused()){
        		paypwd.setText("");
        	}else if(paypwd1.isFocused()){
//        		paypwd.setEnabled(true);
        		paypwd1.clearFocus();
        		paypwd.requestFocus();
//        		paypwd1.setEnabled(false);
        		paypwd.setText("");
        	}else if(paypwd2.isFocused()){
//        		paypwd1.setEnabled(true);
        		paypwd2.clearFocus();
        		paypwd1.requestFocus();
//        		paypwd2.setEnabled(false);
        		paypwd1.setText("");
        	}
        	else if(paypwd3.isFocused()){
//        		paypwd2.setEnabled(true);
        		paypwd3.clearFocus();
        		paypwd2.requestFocus();
//        		paypwd3.setEnabled(false);
        		paypwd2.setText("");
        	}
        	else if(paypwd4.isFocused()){
//        		paypwd3.setEnabled(true);
        		paypwd4.clearFocus();
        		paypwd3.requestFocus();
//        		paypwd4.setEnabled(false);
        		paypwd3.setText("");
        	}
        	else if(paypwd5.isFocused()){
        		if(paypwd5.getText().toString()!=null&&!paypwd5.getText().toString().equals("")){
        			paypwd5.setText("");
        		}else{
//            		paypwd4.setEnabled(true);
                		paypwd5.clearFocus();
                		paypwd4.requestFocus();
//                		paypwd5.setEnabled(false);
                		paypwd4.setText("");
        		}

        		
        	}
         	else if(edVCode.isFocused()){
         		edVCode.setText("");
        	}
            return false;  
        }
        return super.dispatchKeyEvent(event);  
    } 
	
	
	public void Signout(){

		
		downloadDialog = new TwoButtonDialog(Realname2Activity.this, R.style.CustomDialog,
				" 尊敬的会员", "您确定退出设置交易密码吗?", "取消", "确定",true,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							downloadDialog = null;
							break;
						case R.id.Button_cancel:
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
	
//	private void initDialog(String content,String left,String right) {
//		// TODO Auto-generated method stub
//		downloadDialog = new TwoButtonDialog(Realname2Activity.this, R.style.CustomDialog,
//				"", content, left, right,true,new OnMyDialogClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						switch (v.getId()) {
//						case R.id.Button_OK:
//							downloadDialog.dismiss();
//							break;
//						case R.id.Button_cancel:
//							finish();
//							downloadDialog.dismiss();
//						default:
//							break;
//						}
//					}
//				});
//			downloadDialog.show();
//	}
	

	private void initDialog(String content,String left,String right) {
		// TODO Auto-generated method stub
		downloadDialog = new TwoButtonDialog(Realname2Activity.this, R.style.CustomDialog,
				"尊敬的会员", content, left, right,true,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							finish();
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
		}
	
	/**
	 * 监听返回按钮
	 */
//	@Override
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			initDialog("确定设置交易密码?", "取消", "确定");
		}
		return true;
	}
	
}
