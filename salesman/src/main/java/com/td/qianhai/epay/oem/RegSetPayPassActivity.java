package com.td.qianhai.epay.oem;

import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.share.app.network.CallbackString;
import com.share.app.network.Request;
import com.share.app.utils.ProgressDialogUtil;
import com.share.app.utils.PwdUtils;
import com.share.app.utils.WaringDialogUtils;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;

/**
 * 设置支付密码(找回密码)
 * 
 * @author Administrator
 * 
 */
public class RegSetPayPassActivity extends BaseActivity implements OnClickListener {

	private EditText et_pw1, et_pw2;
	private Button btn_next;
	private TextView tv_left,tv_title_contre;
	private String possword, possword2, actid;
	private String custpwd;

	private String mMobile;
	private String mMsgId;
	private String mMsgCode;

	void initView() {
//		actid = ((AppContext) RegSetPayPassActivity.this.getApplication())
//				.getMobile();
		Bundle bundle = getIntent().getExtras();
		mMobile = bundle.getString("mobile");
		mMsgId = bundle.getString("msgId");
		mMsgCode = bundle.getString("msgCode");

		actid = MyCacheUtil.getshared(this).getString("Mobile", "");
		et_pw1 = (EditText) findViewById(R.id.et_pw1);
		et_pw2 = (EditText) findViewById(R.id.et_pw2);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		tv_left = (TextView) findViewById(R.id.bt_title_left);
		tv_title_contre = (TextView)findViewById(R.id.tv_title_contre);
		tv_title_contre.setText("设置支付密码");
		tv_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.virtual_account_set_pass);
		custpwd = ((AppContext)getApplication()).getCustPass();
		AppContext.getInstance().addActivity(this);
		initView();
	}

	void verify() {
		possword = et_pw1.getText().toString();
		if (possword == null || possword.equals("")) {
			Toast.makeText(getApplicationContext(),"请输入支付密码",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(RegSetPayPassActivity.this, "请输入支付密码");
			return;
		} else if (possword.length() < 6) {
			Toast.makeText(getApplicationContext(),"密码长度不一致,请重新输入",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(RegSetPayPassActivity.this, "密码长度不一致,请重新输入!");
			return;
		}

		possword2 = et_pw2.getText().toString();
		if (possword2 == null || possword2.equals("")) {
			Toast.makeText(getApplicationContext(),"请输入支付密码",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(RegSetPayPassActivity.this, "请输入支付密码");
			return;
		} else if (!possword.equals(possword2)) {
			Toast.makeText(getApplicationContext(),"两次密码不一致,请重新输入",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(RegSetPayPassActivity.this, "两次密码不一致,请重新输入!");
			return;
		}
		if(custpwd.equals(possword)){
			Toast.makeText(getApplicationContext(),"支付密码不能与登录密码一致",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(RegSetPayPassActivity.this, "支付密码不能与登录密码一致!");
			return;
		}
//		oldUpdatePayPwd();
		requestUpdatePayPwd(mMobile, possword, mMsgId, mMsgCode);
	}

	/**
	 * 修改支付密码
	 * @param mobile
	 * @param pwd
	 * @param msgId
	 * @param msgCode
	 */
	private void requestUpdatePayPwd(String mobile, String pwd, String msgId, String msgCode) {
		pwd = PwdUtils.getEncripyPwd(pwd, 3);
        ProgressDialogUtil.showProgressDlg(this, "");
		Request.getSalesmanFindPayPassword(mobile, pwd, msgId, msgCode,
				new CallbackString() {
					@Override
					public void onFailure(String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        WaringDialogUtils.showWaringDialog(RegSetPayPassActivity.this, msg, null);
					}

					@Override
					public void onSuccess(String data) {
                        ProgressDialogUtil.dismissProgressDlg();
                        toast("操作成功");
                        finish();
					}

					@Override
					public void onNetError(int code, String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        WaringDialogUtils.showWaringDialog(RegSetPayPassActivity.this, "网络异常", null);
					}
				});
	}

	private void oldUpdatePayPwd() {
		SetPayPassTask task = new SetPayPassTask();
		task.execute(HttpUrls.PAY_UPDATE + "", actid, possword);
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
			return NetCommunicate.getMidatc(HttpUrls.PAY_UPDATE, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (Entity.STATE_OK
						.equals(result.get(Entity.RSPCOD).toString())) {
					Toast.makeText(getApplicationContext(),"重设支付密码成功",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(RegSetPayPassActivity.this, "重设支付密码成功");
//					Intent it = new Intent(RegSetPayPassActivity.this,
//							MenuActivity.class);
//					startActivity(it);
					finish();
				} else {
					Toast.makeText(getApplicationContext(),result
							.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(RegSetPayPassActivity.this, result
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
}
