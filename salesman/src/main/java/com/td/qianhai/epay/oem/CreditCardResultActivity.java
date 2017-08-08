package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;

public class CreditCardResultActivity extends BaseActivity {

	private String tag, name, card, id,result;

	private ImageView result_img;

	private TextView result_tv, card_tv, name_tv, btn_credit_result;

	private LinearLayout resule_info_lin,credit_date;

	private boolean flags;
	
	private CheckBox binding_chec;
	
	private Spinner credit_typt_content;
	
	private String date = "1";
	
	private String mercnum;
	
	private String phone;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credit_card_result);
		AppContext.getInstance().addActivity(this);
		phone = MyCacheUtil.getshared(this).getString("Mobile", "");
//		mercnum = ((AppContext)getApplication()).getMercNum();
		mercnum = MyCacheUtil.getshared(this).getString("MercNum", "");
//		phone = ((AppContext)getApplication()).getMobile();

		initview();

	}

	private void initview() {
		Calendar c = Calendar.getInstance();  
				int year = c.get(Calendar.YEAR);  
                int month = c.get(Calendar.MONTH);  
                int  day = c.get(Calendar.DAY_OF_MONTH);
                if(day>=28){
                	  date = "28";
                }else{
                	  date = day+"";
                }
              
		
		((TextView) findViewById(R.id.tv_title_contre)).setText("");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		result_img = (ImageView) findViewById(R.id.result_img);

		result_tv = (TextView) findViewById(R.id.result_tv);

		card_tv = (TextView) findViewById(R.id.card_tv);

		name_tv = (TextView) findViewById(R.id.name_tv);

		btn_credit_result = (TextView) findViewById(R.id.btn_credit_result);

		resule_info_lin = (LinearLayout) findViewById(R.id.resule_info_lin);
		
		binding_chec = (CheckBox) findViewById(R.id.binding_chec);
		
		credit_date = (LinearLayout) findViewById(R.id.credit_date);
		
		credit_typt_content = (Spinner) findViewById(R.id.credit_typt_content);

		Intent it = getIntent();

		tag = it.getStringExtra("tag");

		if (tag.equals("0")) {
			result_img.setImageResource(R.drawable.resize_png_new);
			flags = true;
			name = it.getStringExtra("name");
			card = it.getStringExtra("card");
			
			id = it.getStringExtra("id");
			result = it.getStringExtra("result");
			card_tv.setText("尾号     " + card.substring(card.length() - 4));
			result_tv.setText(result);
			name_tv.setText(name);

		} else if (tag.equals("1")) {
			result_img.setImageResource(R.drawable.resize_png_newerr);
			flags = false;
			result = it.getStringExtra("result");
			result_tv.setText(result);
			resule_info_lin.setVisibility(View.GONE);
			btn_credit_result.setText("返回");
		}else if(tag.equals("2")){
			result_img.setImageResource(R.drawable.resize_png_new);
			result = it.getStringExtra("result");
			flags = false;
			result_tv.setText(result);
			resule_info_lin.setVisibility(View.GONE);
			btn_credit_result.setText("返回");
			
		}

		ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_spinner_item, new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28"});  
		  
	      adapter.setDropDownViewResource(R.layout.sp_item);  
//	</span>//只需在这里设置一句即可setDropDownViewResource  
	      credit_typt_content.setAdapter(adapter);  
	      if(day>=28){
	    	  credit_typt_content.setSelection(27,true);
	      }else{
	    	  credit_typt_content.setSelection(day-1,true);
	      }
	     
	      
	      credit_typt_content.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long id) {
				// TODO Auto-generated method stub
				date = arg2+"";
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		binding_chec.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checble) {
				// TODO Auto-generated method stub
				if(checble){
					credit_date.setVisibility(View.VISIBLE);
				}else{
					credit_date.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		
		btn_credit_result.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (flags) {
					if(binding_chec.isChecked()){
						binding();
					}else{
						finish();
					}
					
				} else {
					finish();
				}
			}
		});
	}
	
	
	
	

	private void binding() {
		
		BindDingCard payTask = new BindDingCard();
		payTask.execute(HttpUrls.BINDINGCARD+ "", id,date,mercnum);

	}
	
	private void Grouping(){
		GroupingPush grouping = new GroupingPush();
		grouping.execute(HttpUrls.GROUPING+ "", phone,date);
	}
	
	/**
	 * 绑定银行
	 * 
	 * 
	 */
	class BindDingCard extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在操作中...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1],params[2],params[3]};
			return NetCommunicate.getPay(HttpUrls.BINDINGCARD,
					values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					AppContext.iscreditcardlist = true;
					Toast.makeText(getApplicationContext(), result.get("RSPMSG").toString(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(CreditCardResultActivity.this, result.get("RSPMSG").toString());

					Grouping();
					finish();
				} else {
					finish();
					Toast.makeText(getApplicationContext(), result.get("RSPMSG").toString(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(CreditCardResultActivity.this, result.get("RSPMSG").toString());
				}
			} else {
				finish();
				Toast.makeText(getApplicationContext(),"fail",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(CreditCardResultActivity.this, "fail");
			}
			super.onPostExecute(result);
		}
	}
	
	/**
	 * 绑定银行
	 * 
	 * 
	 */
	class GroupingPush extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在操作中...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1],params[2]};
			return NetCommunicate.getMidatc(HttpUrls.GROUPING,
					values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					Toast.makeText(getApplicationContext(), result.get("RSPMSG").toString(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(CreditCardResultActivity.this, result.get("RSPMSG").toString());
					
					List<String> tags = new ArrayList<String>();
					tags.add(date);
					AppContext.mPush.setTags(tags);
				} else {
					Toast.makeText(getApplicationContext(), result.get("RSPMSG").toString(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(CreditCardResultActivity.this, result.get("RSPMSG").toString());
				}
			} else {
				finish();
				Toast.makeText(getApplicationContext(),"fail",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(CreditCardResultActivity.this, "fail");
			}
			super.onPostExecute(result);
		}
	}
}
