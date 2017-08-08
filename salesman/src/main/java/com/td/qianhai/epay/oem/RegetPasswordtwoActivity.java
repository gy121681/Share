package com.td.qianhai.epay.oem;

import java.util.HashMap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.epay.utils.AppManager;

/**
 * 重置新密码
 * @author liangge
 *
 */
public class RegetPasswordtwoActivity extends BaseActivity {
	/**输入密码、再次输入新密码*/
	private EditText etpw, etpwa;
	/**提交bt*/
	private Button btnsub;
	/**手机号码*/
	private String mobile;
	private String verifyCode;
	private String msgId;
	/** 返回、title中间内容 */
	private TextView bt_Back,tv_title_contre;

	private OneButtonDialogWarn warnDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reget_passwordtwo_new);
		AppContext.getInstance().addActivity(this);
		init();
	}

	public void init() {
		etpw = (EditText) findViewById(R.id.etnew_pw);
		etpwa = (EditText) findViewById(R.id.etnew_pw_again);
		btnsub = (Button) findViewById(R.id.btn_commit);
		tv_title_contre=(TextView)findViewById(R.id.tv_title_contre);
		bt_Back = (TextView) findViewById(R.id.bt_title_left);
		tv_title_contre.setText("重置密码");
		bt_Back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btnsub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				newpw();
			}
		});
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		mobile = bundle.getString("mobile");
		verifyCode = bundle.getString("verify");
		msgId = bundle.getString("msgId");
		OnKeyListener keyListener = new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					newpw();
					return true;
				}
				return false;
			}
		};
		etpwa.setOnKeyListener(keyListener);
	}
	
	/**
	 * 提交新密码到服务器
	 */
	private void newpw() {
		String newPassword = etpw.getText().toString();
		if (newPassword == null
				|| (newPassword != null && newPassword.equals(""))) {
			Toast.makeText(getApplicationContext(), "请输入新密码", Toast.LENGTH_SHORT).show();
			return;
		} else if (newPassword.length() < 6) {
			Toast.makeText(getApplicationContext(), "密码长度应为6-15位", Toast.LENGTH_SHORT).show();
			return;
		}

		String againNewPassword = etpwa.getText().toString();
		if (againNewPassword == null
				|| (againNewPassword != null && againNewPassword.equals(""))) {
			Toast.makeText(getApplicationContext(), "请再次输入新密码", Toast.LENGTH_SHORT).show();
			return;
		}

		if (!againNewPassword.equals(newPassword)) {
			Toast.makeText(getApplicationContext(), "新密码不一致", Toast.LENGTH_SHORT).show();
			return;
		}
		modifyPassword(mobile, newPassword, msgId, verifyCode);
//		RevisePasswordTask task = new RevisePasswordTask();
//		task.execute(HttpUrls.REGET_PASSWORD + "", mobile, newPassword);
	}

	private void modifyPassword(String account, String pwd, String msgId, String msgCode){
		showLoadingDialog("");
		Request.getSalesmanFindPassword(account, pwd, msgId, msgCode, new CallbackString() {
			@Override
			public void onFailure(String msg) {
				dismissLoadingDialog();
				toast(msg);
			}

			@Override
			public void onSuccess(String data) {
				dismissLoadingDialog();
				toast("修改成功");
			}

			@Override
			public void onNetError(int code, String msg) {
				dismissLoadingDialog();
				warnDialog = new OneButtonDialogWarn(RegetPasswordtwoActivity.this,
						R.style.CustomDialog, "提示", "网络异常", "确定",
						new OnMyDialogClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								warnDialog.dismiss();
							}
						});
				warnDialog.show();
			}
		});
	}

	/**
	 * 新密码重置Task
	 * @author liangge
	 *
	 */
	class RevisePasswordTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog("正在努力加载...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2] };
			return NetCommunicate.get(HttpUrls.REGET_PASSWORD, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					Toast.makeText(getApplicationContext(), "修改成功",
							Toast.LENGTH_SHORT).show();
					AppManager.getAppManager().finishActivity(
							RegetPwActivity.class);
					AppManager.getAppManager().finishActivity(
							RegetPasswordtwoActivity.class);
				} else {
					Toast.makeText(getApplicationContext(),
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();
				}
			}
			super.onPostExecute(result);
		}
	}
}
