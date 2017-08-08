package com.td.qianhai.epay.oem;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.ChooseDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnBackDialogClickListener;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class NewAgentActivity extends BaseActivity implements OnClickListener{

	private EditText ed_newagent;
	
	private TextView contacts,new_agent_set_rate,phone_error,new_agent_withdrawals_rate;
	
	private LinearLayout lin_agent,lin_rate_l,lin_rate_w;
	
	private   int REQUEST_CONTACT = 1;
	
	private String phone;
	
	private ProgressBar load_phonr_pro;
	
	private ImageView load_phonr_img;
	
	private RelativeLayout  relalay;
	
	private Button btn_newagent_bg;
	
	private OneButtonDialogWarn warnDialog ;
	
	private String [] rlist,wlist;
	
	private  ChooseDialog chooseDialog;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_agent);
//		phone = ((AppContext)getApplication()).getMobile();
		phone = MyCacheUtil.getshared(this).getString("Mobile", "");
		rlist = ((AppContext)getApplication()).getRatelist();
		wlist =  ((AppContext)getApplication()).getFlashlist();
		initview();
	}

	private void initview() {
		((TextView) findViewById(R.id.tv_title_contre)).setText("新建代理商");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		
		ed_newagent = (EditText) findViewById(R.id.ed_newagent);
		lin_rate_w = (LinearLayout) findViewById(R.id.lin_rate_w);
		lin_rate_w.setOnClickListener(this);
		contacts = (TextView) findViewById(R.id.contact);
		new_agent_withdrawals_rate = (TextView) findViewById(R.id.new_agent_withdrawals_rate);
		contacts.setOnClickListener(this);
		new_agent_set_rate = (TextView) findViewById(R.id.new_agent_set_rate);
		if(rlist.length>0){
			new_agent_set_rate.setText(rlist[0]);
		}
		if(wlist.length>0){
			new_agent_withdrawals_rate.setText(wlist[0]);
		}
		lin_agent = (LinearLayout) findViewById(R.id.lin_agent);
		lin_agent.setOnClickListener(this);
		load_phonr_pro = (ProgressBar) findViewById(R.id.load_phonr_pro);
		load_phonr_img = (ImageView) findViewById(R.id.load_phonr_img);
		relalay = (RelativeLayout) findViewById(R.id.relalay);
		phone_error = (TextView) findViewById(R.id.phone_error);
		phone_error.setOnClickListener(this);
		btn_newagent_bg = (Button) findViewById(R.id.btn_newagent_bg);
		lin_rate_l = (LinearLayout) findViewById(R.id.lin_rate_l);
		lin_rate_l.setOnClickListener(this);
		btn_newagent_bg.setOnClickListener(this);
		btn_newagent_bg.setEnabled(false);
		
		ed_newagent.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()==11){
					btn_newagent_bg.setEnabled(true);
					String p = ed_newagent.getText().toString();
					verification(p);
				}else{
					btn_newagent_bg.setEnabled(false);
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
	}
	
	private void verification(String s) {
		// TODO Auto-generated method stub
		VerificationTask vtask = new VerificationTask();
		vtask.execute(HttpUrls.VERIFICATIONPHONENUM + "", phone, s);
		
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.contact:
			Intent intent = new Intent();

			intent.setAction(Intent.ACTION_PICK);

			intent.setData(ContactsContract.Contacts.CONTENT_URI);

			startActivityForResult(intent, REQUEST_CONTACT);
			break;
			
		case R.id.btn_newagent_bg:
			
			String p = ed_newagent.getText().toString();
			if(p.length()>=11){
				
				String rate = new_agent_set_rate.getText().toString();
				String rate1 = new_agent_withdrawals_rate.getText().toString();
				NewAgentTask otask = new NewAgentTask();

				otask.execute(HttpUrls.NEWAGENT + "", phone, p,rate.substring(0,rate.length()-1),rate1.substring(0,rate1.length()-1));
			}

			break;
			
		case R.id.phone_error:
			relalay.setVisibility(View.VISIBLE);
			load_phonr_pro.setVisibility(View.GONE);
			load_phonr_img.setVisibility(View.GONE);
			ed_newagent.setVisibility(View.VISIBLE);
			phone_error.setVisibility(View.GONE);
			ed_newagent.requestFocus();
			break;
		case R.id.lin_rate_l:
			
			PopUpBox();
			break;
		case R.id.lin_rate_w:
			
			PopUpBox1();
			break;
			
		default:
			break;
		}
		
	}
	
	
	private void PopUpBox1() {
		
		chooseDialog = new ChooseDialog(
				NewAgentActivity.this,
				R.style.CustomDialog,
				new OnBackDialogClickListener() {

					@Override
					public void OnBackClick(View v, String str,
							int position) {
						new_agent_withdrawals_rate.setText(str);
//						// TODO Auto-generated method stub
						chooseDialog.dismiss();
					}
				}, "闪提费率", wlist);
		chooseDialog.show();
		
	}

	private void PopUpBox() {
		chooseDialog = new ChooseDialog(
				NewAgentActivity.this,
				R.style.CustomDialog,
				new OnBackDialogClickListener() {

					@Override
					public void OnBackClick(View v, String str,
							int position) {
						new_agent_set_rate.setText(str);
//						// TODO Auto-generated method stub
						chooseDialog.dismiss();
					}
				}, "设置费率", rlist);
		chooseDialog.show();
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_CONTACT) {
			if (resultCode == RESULT_OK) {
				if (data == null) {
					return;
				}

				Uri result = data.getData();
				
				 ContentResolver reContentResolverol = getContentResolver();
				
                Cursor c = managedQuery(result, null, null, null, null);
                c.moveToFirst();
                
                String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
                        null, 
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, 
                        null, 
                        null);
                while (phone.moveToNext()) {
                    String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    
                    
                    Pattern p = Pattern.compile("\\s*|\t|\r|\n|-");  
                    Matcher m = p.matcher(usernumber);  
                    String dest = m.replaceAll("");  
                    usernumber = usernumber.replace("-","");  
                    ed_newagent.setText(dest);
                }
			}
		}
	}
	
	class VerificationTask extends AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			load_phonr_pro.setVisibility(View.VISIBLE);
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2]};
			return NetCommunicate
					.getAgentMidatc(HttpUrls.VERIFICATIONPHONENUM, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			if (result != null) {
				if (Entity.STATE_OK.equals(result.get(Entity.RSPCOD).toString())) {
					
					load_phonr_pro.setVisibility(View.GONE);
					load_phonr_img.setVisibility(View.VISIBLE);
					
				}else if(result.get(Entity.RSPCOD).toString().equals("000011")){
					
					load_phonr_pro.setVisibility(View.GONE);
					load_phonr_img.setVisibility(View.GONE);
					phone_error.setVisibility(View.VISIBLE);
					phone_error.setText("该用户已是代理");
					ed_newagent.setVisibility(View.GONE);
					ed_newagent.setText("");
					relalay.setVisibility(View.GONE);
					
				}else if(result.get(Entity.RSPCOD).toString().equals("000012")){
					load_phonr_pro.setVisibility(View.GONE);
					load_phonr_img.setVisibility(View.GONE);
					phone_error.setVisibility(View.VISIBLE);
					phone_error.setText("该用户未审核通过");
					ed_newagent.setVisibility(View.GONE);
					ed_newagent.setText("");
					relalay.setVisibility(View.GONE);
					
				}else {
					load_phonr_pro.setVisibility(View.GONE);
					load_phonr_img.setVisibility(View.GONE);
					ed_newagent.setText("");
					Toast.makeText(getApplicationContext(),result
							.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(NewAgentActivity.this, result
//							.get(Entity.RSPMSG).toString());
				}
			}
			super.onPostExecute(result);
		}
	}
	
	class NewAgentTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在处理中。。。");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3],params[4] };
			return NetCommunicate.getAgentMidatc(HttpUrls.NEWAGENT, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (Entity.STATE_OK.equals(result.get(Entity.RSPCOD).toString())) {
					
					warnDialog = new OneButtonDialogWarn(NewAgentActivity.this,
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
					Toast.makeText(getApplicationContext(),result
							.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(NewAgentActivity.this,
//							result.get(Entity.RSPMSG).toString());
				}
			}
			super.onPostExecute(result);
		}
	}
}
