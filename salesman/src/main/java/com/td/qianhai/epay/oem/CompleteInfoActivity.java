package com.td.qianhai.epay.oem;

import java.util.HashMap;

import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.MyTextWatcher;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 信息补全
 * 
 * @author Administrator
 * 
 */

public class CompleteInfoActivity extends BaseActivity {

	/** 标题名称 */
	private TextView btn_edit_bankmobile;
	private EditText et_edit_bankcard,et_edit_mobile;
	private String cardnum,mobile;
	private OneButtonDialogWarn warnDialog;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comlete);
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		Intent it = getIntent();
		cardnum = it.getStringExtra("card");
		initview();

	}

	/**
	 * 初始化view
	 */
	private void initview() {
		((TextView) findViewById(R.id.tv_title_contre)).setText("变更银行预留手机号");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		et_edit_bankcard = (EditText) findViewById(R.id.et_edit_bankcard);
		et_edit_bankcard.setText(cardnum);
		et_edit_bankcard.setEnabled(false);
		et_edit_mobile = (EditText) findViewById(R.id.et_edit_mobile);
		btn_edit_bankmobile =(TextView) findViewById(R.id.btn_edit_bankmobile);
		et_edit_bankcard.addTextChangedListener(new MyTextWatcher(et_edit_bankcard));
	
		et_edit_mobile.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				String a = et_edit_bankcard.getText().toString();
				if(s.length()==11){
					btn_edit_bankmobile.setEnabled(true);
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
		
		btn_edit_bankmobile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String phone  = et_edit_mobile.getText().toString();
				UpdatecardTask  task = new UpdatecardTask();
				task.execute(HttpUrls.UPDATEBANKCARD + "", mobile,cardnum,phone);
			}
		});
	}
	class UpdatecardTask extends
	AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在处理中。。。");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2],params[3]};
			return NetCommunicate.getAgentMidatc(HttpUrls.UPDATEBANKCARD,
					values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (Entity.STATE_OK
						.equals(result.get(Entity.RSPCOD).toString())) {
					warnDialog = new OneButtonDialogWarn(CompleteInfoActivity.this,
							R.style.CustomDialog, "提示",
							result.get(Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									
									warnDialog.dismiss();
									finish();
								}
							});
					warnDialog.setCancelable(false);
					warnDialog.setCanceledOnTouchOutside(false);
					warnDialog.show();
				} else {
					Toast.makeText(getApplicationContext(),
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();
					// ToastCustom.showMessage(AgentListActivity.this,
					// result.get(Entity.RSPMSG).toString());
				}
			}
			super.onPostExecute(result);
		}
	}
}
