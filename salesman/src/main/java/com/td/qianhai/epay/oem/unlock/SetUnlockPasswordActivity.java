package com.td.qianhai.epay.oem.unlock;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.BaseActivity;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.unlock.LocusPassWordView.OnCompleteListener;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.utils.AppManager;

/**
 * 设置手势密码界面
 */
public class SetUnlockPasswordActivity extends BaseActivity{
	private LocusPassWordView lpwv;
	private String password;
	private boolean needverify = true;
	private Toast toast;
	private String isrefresh;
	private OneButtonDialogWarn warnDialog;
	private TextView title_name;
	private Editor editor;

	private void showToast(CharSequence message) {
		if (null == toast) {
			toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}

		toast.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.setpassword_activity);
		editor = MyCacheUtil.setshared(SetUnlockPasswordActivity.this);
		Intent it = getIntent();
		isrefresh = it.getStringExtra("refresh");
		lpwv = (LocusPassWordView) this.findViewById(R.id.mLocusPassWordView);
		title_name = (TextView) findViewById(R.id.title_name);
		settitle();
		if (isrefresh != null && isrefresh.equals("refresh")) {
////			ToastCustom.showMessage(SetUnlockPasswordActivity.this, "原手势密码已清空，请输入新手势密码");
//			lpwv.resetPassWord("");
//			editor.putString("usermobile", "");
//			editor.commit();
			title_name.setText("请输入原手势密码");
		}
		lpwv.setOnCompleteListener(new OnCompleteListener() {
			@Override
			public void onComplete(String mPassword) {
				password = mPassword;
				// if (isrefresh!=null&&isrefresh.equals("refresh")) {
				// lpwv.resetPassWord("");
				// } else {
				if(!needverify){
					lpwv.resetPassWord(password);
				}
				if (needverify) {
					if (lpwv.verifyPassword(mPassword)) {
						title_name.setText("密码输入正确,请输入新密码!");
						title_name.setTextColor(getResources().getColor(R.color.black));
//						showToast("密码输入正确,请输入新密码!");
						lpwv.clearPassword();
						lpwv.resetPassWord("");
						needverify = false;
					} else {
						title_name.setText("错误的密码,请输入原密码");
						title_name.setTextColor(getResources().getColor(R.color.red));
//						showToast("错误的密码,请输入原密码!");
						lpwv.clearPassword();
						password = "";
					}
				}
				// }
			}
		});

		OnClickListener mOnClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tvSave:
					if (StringUtil.isNotEmpty(password)) {

						
//						lpwv.clearPassword();
						if(isrefresh != null && isrefresh.equals("refresh")){
							
							if(lpwv.isPasswordEmpty()){
								title_name.setText("请输入新密码");
								title_name.setTextColor(getResources().getColor(R.color.red));
								return;
							}
							lpwv.resetPassWord(password);
							editor.putString("refresh", "");
							editor.putString("usermobile",MyCacheUtil.getshared(SetUnlockPasswordActivity.this).getString("Mobile", ""));
							editor.commit();
							showToast("手势密码设置成功");
							
						}else{
							
							lpwv.resetPassWord(password);
							editor.putString("refresh", "");
							editor.putString("usermobile",MyCacheUtil.getshared(SetUnlockPasswordActivity.this).getString("Mobile", ""));
							editor.commit();
						}

						if (isrefresh != null && isrefresh.equals("refresh")) {
//							new Handler().postDelayed(new Runnable() {
//
//								@Override
//								public void run() {
									finish();
//								}
//							}, 1000);
							
						} else {
//							new Handler().postDelayed(new Runnable() {
//
//								@Override
//								public void run() {
									startActivity(new Intent(
											SetUnlockPasswordActivity.this,
											UnlockLoginActivity.class));
									finish();
//								}
//							}, 1000);
						}

					} else {
						lpwv.clearPassword();
						showToast("密码不能为空,请输入密码.");
					}
					break;
				case R.id.tvReset:
					lpwv.clearPassword();
					break;
				}
			}
		};
		Button buttonSave = (Button) this.findViewById(R.id.tvSave);
		buttonSave.setOnClickListener(mOnClickListener);
		Button tvReset = (Button) this.findViewById(R.id.tvReset);
		tvReset.setOnClickListener(mOnClickListener);
		// 如果密码为空,直接输入密码
		if (lpwv.isPasswordEmpty()) {
			this.needverify = false;
			// showToast("请输入密码");
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	
	private boolean isExit;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (isrefresh != null && isrefresh.equals("refresh")) {
				finish();
			}else{
				
				if (!isExit) {
					isExit = true;
					Toast.makeText(getApplicationContext(), "再按一次退出程序",
							Toast.LENGTH_SHORT).show();
					new Handler().postDelayed(new Runnable() {
						public void run() {
							isExit = false;
						}
					}, 2000);
					;
					return false;
				} else {
					editor.putString("usermobile", "");
					editor.putString("userpwd", "");
					editor.commit();
					((AppContext) getApplication()).setCustId(null); // 商户ID赋为空
					((AppContext) getApplication()).setPsamId(null);
					((AppContext) getApplication()).setMacKey(null);
					((AppContext) getApplication()).setPinKey(null);
					((AppContext) getApplication()).setMerSts(null);
					((AppContext) getApplication()).setMobile(null);
					((AppContext) getApplication()).setEncryPtkey(null);
					((AppContext) getApplication()).setStatus(null);
					((AppContext) getApplication()).setCustPass(null);
					((AppContext) getApplication()).setVersionSerial(null);
					AppManager.getAppManager().AppExit(SetUnlockPasswordActivity.this);
					finish();
				}
				
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	

}
