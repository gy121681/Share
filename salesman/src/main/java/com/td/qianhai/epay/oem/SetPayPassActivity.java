package com.td.qianhai.epay.oem;

import java.util.HashMap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.fragmentmanager.FmMainActivity;

/**
 * 设置支付密码
 * 
 * @author Administrator
 * 
 */
public class SetPayPassActivity extends BaseActivity implements OnClickListener {

	private EditText et_pw1, et_pw2;
	private Button btn_next;
	private String possword, possword2, actid;
	private OneButtonDialogWarn warnDialog;
	private TextView tv_left;
	private String custpwd;
	
	
	void initView() {
		((TextView) findViewById(R.id.tv_title_contre)).setText("设置支付密码");
		custpwd = ((AppContext)getApplication()).getCustPass();
//		actid = ((AppContext) SetPayPassActivity.this.getApplication())
//				.getMobile();
		actid = MyCacheUtil.getshared(this).getString("Mobile", "");
		et_pw1 = (EditText) findViewById(R.id.et_pw1);
		et_pw2 = (EditText) findViewById(R.id.et_pw2);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		tv_left = (TextView) findViewById(R.id.bt_title_left);
		tv_left.setVisibility(View.GONE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.virtual_account_set_pass);
		AppContext.getInstance().addActivity(this);
		initView();
	}

	void verify() {
		possword = et_pw1.getText().toString();
		if (possword == null || possword.equals("")) {
			Toast.makeText(getApplicationContext(),"请输入支付密码",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(SetPayPassActivity.this, "请输入支付密码");
			return;
		} else if (possword.length() < 6) {
			Toast.makeText(getApplicationContext(),"密码长度不一致,请重新输入",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(SetPayPassActivity.this, "密码长度不一致,请重新输入!");
			return;
		}
		possword2 = et_pw2.getText().toString();
		if (possword2 == null || possword2.equals("")) {
			Toast.makeText(getApplicationContext(),"请输入支付密码",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(SetPayPassActivity.this, "请输入支付密码");
			return;
		} else if (!possword.equals(possword2)) {
			Toast.makeText(getApplicationContext(),"两次密码不一致,请重新输入",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(SetPayPassActivity.this, "两次密码不一致,请重新输入!");
			return;
		}
		if(custpwd.equals(possword)){
			Toast.makeText(getApplicationContext(),"支付密码不能与登录密码一致",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(SetPayPassActivity.this, "支付密码不能与登录密码一致");
			return;
		}
		SetPayPassTask task = new SetPayPassTask();
		task.execute(HttpUrls.SET_PAY_PASS + "", actid, possword);
	}

	/**
	 * 设置支付密码AsyncTask
	 * 
	 * @author Administrator
	 * 
	 */
	class SetPayPassTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在处理中。。。");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2] };
			return NetCommunicate.getMidatc(HttpUrls.SET_PAY_PASS, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (Entity.STATE_OK
						.equals(result.get(Entity.RSPCOD).toString())) {
					Toast.makeText(getApplicationContext(),"设置支付密码成功",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(SetPayPassActivity.this, "设置支付密码成功");
					Intent it = new Intent(SetPayPassActivity.this,FmMainActivity.class);
					startActivity(it);
//					UserActivity.context.finish();
					finish();
				} else {
					Toast.makeText(getApplicationContext(),result
							.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(SetPayPassActivity.this, result
//							.get(Entity.RSPMSG).toString());
				}
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_next:
			verify();
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			SpannableString msp = new SpannableString("取消设置支付密码，将不可进行充值提现,确定退出?");
			showDoubleWarnDialog(msp);


			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void doubleWarnOnClick(View v) {
		// TODO Auto-generated method stub
		super.doubleWarnOnClick(v);
		switch (v.getId()) {
		case R.id.btn_left:
			doubleWarnDialog.dismiss();

			break;
		case R.id.btn_right:
			Intent it = new Intent(SetPayPassActivity.this,UserActivity.class);
			startActivity(it);
			doubleWarnDialog.dismiss();
			break;

		default:
			break;
		}
	}
}
