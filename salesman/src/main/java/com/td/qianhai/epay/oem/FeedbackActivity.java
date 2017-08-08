package com.td.qianhai.epay.oem;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class FeedbackActivity extends BaseActivity {

	private TextView backs, content;

	private EditText fb_edit1, fb_edit2;

	private Button fb_submit;

	private String feedbackstr,mobile,username,emails,feedcontent,ctype;
	
	private OneButtonDialogWarn warnDialog ;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		AppContext.getInstance().addActivity(this);
//		mobile = ((AppContext)getApplication()).getMobile();
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		initview();

	}

	private void initview() {
		fb_edit1 = (EditText) findViewById(R.id.fb_edit1);
		fb_edit2 = (EditText) findViewById(R.id.fb_edit2);
		fb_submit = (Button) findViewById(R.id.fb_submit);
		backs = (TextView) findViewById(R.id.bt_title_left);
		backs.setText("返回");
		content = (TextView) findViewById(R.id.tv_title_contre);
		content.setText("反馈消息");
		backs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				finish();

			}
		});
		feedbackstr = fb_edit1.getText().toString();

		if (feedbackstr == null || feedbackstr.equals("")) {

			fb_submit.setEnabled(false);
		}
		
		// 意见不能为空
		fb_edit1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					fb_submit.setEnabled(true);
				}else{
					fb_submit.setEnabled(false);
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

		fb_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(),"即将开通",
//						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(FeedbackActivity.this,
//						"即将开通");
				feedcontent = fb_edit1.getText().toString();
//				emails = fb_edit2.getText().toString();
//				
				FeedbackTask task = new FeedbackTask();
				task.execute(HttpUrls.FEEDBACK + "", mobile,"3",feedcontent);
				
//				getET
				

			}
		});
	}
	
	
	class FeedbackTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			showLoadingDialog("正在提交...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			// TODO Auto-generated method stub
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate.get(HttpUrls.FEEDBACK,
					values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				System.out.println(Entity.RSPCOD);
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					warnDialog = new OneButtonDialogWarn(FeedbackActivity.this,
							R.style.CustomDialog, "提示",
							result.get(Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									finish();
									warnDialog.dismiss();
								}
							});
					warnDialog.setCancelable(false);
					warnDialog.setCanceledOnTouchOutside(false);
					warnDialog.show();
				} else {
					Toast.makeText(getApplicationContext(),result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(FeedbackActivity.this,
//							result.get(Entity.RSPMSG).toString());
				}
			}
			super.onPostExecute(result);
		}
}
}
