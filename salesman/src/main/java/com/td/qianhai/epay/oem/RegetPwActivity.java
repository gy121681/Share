package com.td.qianhai.epay.oem;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.share.app.network.CallbackString;
import com.share.app.network.Request;
import com.share.app.utils.MD5Utils;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.CountDownButton;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.epay.utils.Common;

/**
 * 找回密码
 * 
 * @author liangge
 * 
 */
public class RegetPwActivity extends BaseActivity {
	/** 手机号码、身份证号码、验证码 */
	private EditText etphone, etverif;
	/** 获取验证码、提交找回密码 */
	private Button btngetverif, btnsubmit;
	/** 用户手机、终端号 */
	private String userMobile, psamId;
	/** 提示 */
	private OneButtonDialogWarn warnDialog;

	/** 返回、title中间内容 */
	private TextView bt_Back, tv_title_contre;

	private CountDownButton mCountDownBtn;

	private void showDialog(String msg) {
		warnDialog = new OneButtonDialogWarn(RegetPwActivity.this,
				R.style.CustomDialog, "提示", msg, "确定",
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						warnDialog.dismiss();
					}
				});
		warnDialog.show();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 10:
				showDialog("获取验证码失败");
				btngetverif.setEnabled(true);
				break;
			case 11:
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
//				Toast.makeText(getApplicationContext(),
//						map.get("RSPMSG").toString(), Toast.LENGTH_SHORT)
//						.show();
				if (map.get("RSPCOD").toString().equals("000000")) {
					Common.timing(btngetverif);
					if(map.get("RSPMSG")!=null){
						Toast.makeText(getApplicationContext(),map.get("RSPMSG").toString(),
								Toast.LENGTH_SHORT).show();
//						ToastCustom.showMessage(RegetPwActivity.this,map.get("RSPMSG").toString());
					}
					
				} else if (map.get("RSPCOD").toString().equals("400002")) {
					btngetverif.setEnabled(true);
					showDialog("商户被禁用，请联系工作人员");
				} else {
					btngetverif.setEnabled(true);
					if(map.get("RSPMSG")!=null){
						Toast.makeText(getApplicationContext(),map.get("RSPMSG").toString(),
								Toast.LENGTH_SHORT).show();
//						ToastCustom.showMessage(RegetPwActivity.this,map.get("RSPMSG").toString());
					}
				}

				break;
			case 1:
				showDialog("提交失败");
				break;
			case 2:
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map2 = (HashMap<String, Object>) msg.obj;
				Toast.makeText(getApplicationContext(),
						map2.get("RSPMSG").toString(), Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reget_pw_new);
		AppContext.getInstance().addActivity(this);
		// 获取终端号
		psamId = ((AppContext) getApplication()).getPsamId();
		init();
	}

	public void init() {
		etphone = (EditText) findViewById(R.id.et_reget_mobiles);
//		etidcard = (EditText) findViewById(R.id.et_reget_cert_no);
		etverif = (EditText) findViewById(R.id.et_reget_verif_code);
		btngetverif = (Button) findViewById(R.id.btn_reget_get_verif_code);
		btnsubmit = (Button) findViewById(R.id.btn_reget_submit);
		tv_title_contre = (TextView) findViewById(R.id.tv_title_contre);
		bt_Back = (TextView) findViewById(R.id.bt_title_left);
		bt_Back.setText("");
		tv_title_contre.setText("找回密码");
		bt_Back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mCountDownBtn = (CountDownButton) findViewById(R.id.btn_verif_code);
		mCountDownBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String mobile = etphone.getText().toString();
				if (mobile == null || (mobile != null && mobile.equals(""))) {
					Toast.makeText(getApplicationContext(), "请输入手机号码",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (mobile.length() != 11) {
					Toast.makeText(getApplicationContext(), "手机号码位数错误",
							Toast.LENGTH_SHORT).show();
					return;
				}
				mCountDownBtn.getVCode(mobile, null);
			}
		});

		btngetverif.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String mobile = etphone.getText().toString();
				if (mobile == null || (mobile != null && mobile.equals(""))) {
					Toast.makeText(getApplicationContext(), "请输入手机号码",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (mobile.length() != 11) {
					Toast.makeText(getApplicationContext(), "手机号码位数错误",
							Toast.LENGTH_SHORT).show();
					return;
				}
//				final String idcard = etidcard.getText().toString();
//				if (idcard == null || (idcard != null && idcard.equals(""))) {
//					Toast.makeText(getApplicationContext(), "请输入身份证号",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//				String identifyID = null;
//				try {
//					identifyID = Checkingroutine.IDCard.IDCardValidate(idcard);
//					Log.v("test", "id:" + idcard);
//				} catch (ParseException e) {
//
//					e.printStackTrace();
//				}
//				if (!"".equals(identifyID)) {
//					Toast.makeText(getApplicationContext(), identifyID,
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
				btngetverif.setEnabled(false);
				// 验证码倒计时
				final String[] values = { HttpUrls.REGET_PW_VERIF + "", mobile,
						"", "", "2" };

				new Thread(new Runnable() {

					@Override
					public void run() {

						HashMap<String, Object> result = NetCommunicate
								.getVerification(HttpUrls.REGET_PW_VERIF,
										values);
						Message msg = new Message();
						if (result == null) {
							msg.what = 10;
							msg.obj = result;
						} else {
							msg.what = 11;
							msg.obj = result;
							
						}
						mHandler.sendMessage(msg);
						
					}
				}).start();
				return;
			}
		});
		btnsubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				verifCommit();
			}
		});
		OnKeyListener keyListener = new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					verifCommit();
					return true;
				}
				return false;
			}
		};
		etverif.setOnKeyListener(keyListener);
	}

	public void verifCommit() {
		userMobile = etphone.getText().toString();
		if (userMobile == null || (userMobile != null && userMobile.equals(""))) {
			Toast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_SHORT).show();
			return;
		} else if (userMobile.length() != 11) {
			Toast.makeText(getApplicationContext(), "手机号码位数错误", Toast.LENGTH_SHORT).show();
			return;
		}

//		final String idcard = etidcard.getText().toString();
//		if (idcard == null || (idcard != null && idcard.equals(""))) {
//			Toast.makeText(getApplicationContext(), "请输入身份证号", Toast.LENGTH_SHORT)
//					.show();
//			return;
//		}
//		String identifyID = null;
//		try {
//			identifyID = Checkingroutine.IDCard.IDCardValidate(idcard);
//			Log.v("test", "id:" + idcard);
//		} catch (ParseException e) {
//
//			e.printStackTrace();
//		}
//		if (!"".equals(identifyID)) {
//			Toast.makeText(RegetPwActivity.this, identifyID, Toast.LENGTH_SHORT)
//					.show();
//			return;
//		}

		String verif = etverif.getText().toString();
		if (verif == null || (verif != null && verif.equals(""))) {
			Toast.makeText(getApplicationContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
			return;
		}
        findvPwd(userMobile, verif);
		// 请求找回密码
//		RegetPwTask task = new RegetPwTask();
//		task.execute(HttpUrls.VERIF_COMMIT + "", userMobile, verif, "2");
	}

	private void findvPwd(String account, String verifyCode){
        Bundle bundle = new Bundle();
        bundle.putString("mobile", account);
        bundle.putString("verify", verifyCode);
        bundle.putString("msgId", mCountDownBtn.getVCodeId());
        Intent it = new Intent(RegetPwActivity.this,
                RegetPasswordtwoActivity.class);
        it.putExtras(bundle);
        startActivity(it);
        finish();
    }

	/**
	 * 获取验证码异步
	 * 
	 * @author liangge
	 * 
	 */
	class RegetPwTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			showLoadingDialog("正在努力加载...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			// TODO Auto-generated method stub
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate.get(HttpUrls.VERIF_COMMIT, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				System.out.println(Entity.RSPCOD);

				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					Log.v("test", "修改密码成功"
							+ result.get(Entity.RSPMSG).toString());
					Toast.makeText(getApplicationContext(), "身份验证成功",
							Toast.LENGTH_SHORT).show();
					Bundle bundle = new Bundle();
					bundle.putString("mobile", userMobile);
					Intent it = new Intent(RegetPwActivity.this,
							RegetPasswordtwoActivity.class);
					it.putExtras(bundle);
					startActivity(it);
					finish();
				} else {
					showDialog("Entity.RSPMSG).toString()");
				}
			}
			super.onPostExecute(result);
		}
	}
}
