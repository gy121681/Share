package com.shareshenghuo.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.dialog.LicenseDialog;
import com.shareshenghuo.app.user.fragment.LoginNormalFragment;
import com.shareshenghuo.app.user.fragment.LoginQuickFragment;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;

public class LoginActivity extends BaseTopActivity 
	implements OnCheckedChangeListener, OnClickListener {
	
	private static final int REQ_BIND_MOBILE = 0x100;

	public boolean isLogout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		isLogout = getIntent().getBooleanExtra("logout", false);
		initView();
	}
	
	public void initView() {
		initTopBar("登录");
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("注册");
		btnTopRight1.setOnClickListener(this);
		
		((RadioGroup)getView(R.id.rgLogin)).setOnCheckedChangeListener(this);
		showContent(new LoginNormalFragment());
	}
	
	public void showContent(Fragment f) {
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.rlLoginContent, f);
		t.commit();
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int id) {
		switch(id) {
		case R.id.rbLoginNormal:
			showContent(new LoginNormalFragment());
			break;
			
		case R.id.rbLoginQuick:
			showContent(new LoginQuickFragment());
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnTopRight1:
			LicenseDialog dialog = new LicenseDialog();
			dialog.setUrl(Api.WAP_URL_REGIST_LICENSE);
			dialog.setListener(new LicenseDialog.LicenseAgreeListener() {
				@Override
				public void agreeLicense() {
					startActivity(new Intent(LoginActivity.this, RegistActivity.class));
				}
			});
			dialog.show(getSupportFragmentManager(), dialog.getClass().getSimpleName());
			break;
		}
	}
	
	public void login(final UserInfo userInfo) {
		if(UserInfoManager.getLoginType(this) != 0) {
			//第三方登录
			if(TextUtils.isEmpty(userInfo.mobile)) {
				//绑定手机号
				UserInfoManager.setUserId(this, userInfo.id);
				UserInfoManager.saveUserInfo(this, userInfo);
				startActivityForResult(new Intent(this, BindActivity.class), REQ_BIND_MOBILE);
				return;
			}
		}
		easeLogin(userInfo);
	}
	
	/**
	 * 登录环信
	 */
	public void easeLogin(final UserInfo userInfo) {
		ProgressDialogUtil.showProgressDlg(this, "登录中...");
		EMChatManager.getInstance().login("c"+userInfo.id, "123456", new EMCallBack() {
			@Override
			public void onSuccess() {
				ProgressDialogUtil.dismissProgressDlg();
				Log.e("", "环信login succeed");
				EMChatManager.getInstance().updateCurrentUserNick(userInfo.nick_name);
				
				UserInfoManager.saveUserInfo(LoginActivity.this, userInfo);
//				if(isLogout) {
//				if(MainActivity.activity!=null){
//					MainActivity.activity.finish();
//				}
					UserInfoManager.setWxType(LoginActivity.this , "0");
//					Intent it = new Intent(LoginActivity.this, MainActivity.class);
//					it.putExtra("logout", isLogout);
//					startActivity(it);
//				}
				if(TextUtils.isEmpty(userInfo.province_code)||
						TextUtils.isEmpty(userInfo.city_code)||
						TextUtils.isEmpty(userInfo.area_code)){
					startActivity(new Intent(LoginActivity.this,PerfectInfoActivity.class));
				}
				
				finish();
			}
			
			@Override
			public void onProgress(int arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				Log.e("", arg1);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				Log.e("", "环信login error"+arg1);
//				Log.e("", "环信login succeed");
				EMChatManager.getInstance().updateCurrentUserNick(userInfo.nick_name);
				UserInfoManager.saveUserInfo(LoginActivity.this, userInfo);
				UserInfoManager.setWxType(LoginActivity.this , "0");
				if(TextUtils.isEmpty(userInfo.province_code)||
						TextUtils.isEmpty(userInfo.city_code)||
						TextUtils.isEmpty(userInfo.area_code)){
					startActivity(new Intent(LoginActivity.this,PerfectInfoActivity.class));
				}
//				UserInfoManager.saveUserInfo(LoginActivity.this, userInfo);
//				if(isLogout) {
//					Intent it = new Intent(LoginActivity.this, MainActivity.class);
//					it.putExtra("logout", isLogout);
//					startActivity(it);
//				}
				finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		if(resCode == RESULT_OK) {
			if(reqCode == REQ_BIND_MOBILE) {
				easeLogin(UserInfoManager.getUserInfo(this));
			}
		} else {
			UserInfoManager.clearUserInfo(this);
		}
	}
}
