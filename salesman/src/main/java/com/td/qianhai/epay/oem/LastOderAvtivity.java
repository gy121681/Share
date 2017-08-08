package com.td.qianhai.epay.oem;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.utils.Common;

public class LastOderAvtivity extends BaseActivity{
	
	private String balace,mobile,name,clslogno,idcard;
	private int imgid;
	
	private TextView balace_tv,mobile_tv,name_tv,tv_go2;
	
	private ImageView bank_img;
	
	private EditText e_pay12;
	
	private Button tv_go_num;
	
	private Editor editor;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.last_order_activity);
		editor = MyCacheUtil.setshared(LastOderAvtivity.this);
		Intent it = getIntent();
		 balace = it.getStringExtra("balance");
		 mobile = it.getStringExtra("mobile");
		 name = it.getStringExtra("name");
		 imgid = it.getIntExtra("imgid", 0);
		 clslogno = it.getStringExtra("clslogno");
		
		initview();
		
	}

	private void initview() {
		((TextView) findViewById(R.id.tv_title_contre)).setText("短信验证");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		
		balace_tv = (TextView) findViewById(R.id.balace_tv);
		mobile_tv = (TextView) findViewById(R.id.bank_tv);
		name_tv = (TextView) findViewById(R.id.cardtypr_tv);
		bank_img = (ImageView) findViewById(R.id.bank_img);
		tv_go2 = (TextView) findViewById(R.id.tv_go2);
		e_pay12 = (EditText) findViewById(R.id.e_pay12);
		tv_go_num = (Button) findViewById(R.id.tv_go_num);
		Common.timing(tv_go_num);
		
		if(balace!=null){
			balace_tv.setText("¥ "+balace+"元");
		}
		if(mobile!=null){
			mobile_tv.setText(mobile);
		}
		if(name!=null){
			name_tv.setText(name);
		}
			bank_img.setImageResource(imgid);
			
			e_pay12.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
					if(s.length()==4){
						tv_go2.setEnabled(true);
					}else{
						tv_go2.setEnabled(false);
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
		
			tv_go2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String num = e_pay12.getText().toString();
					
					if(clslogno==null){
						Toast.makeText(getApplicationContext(),"抱歉订单交易失败,请重新交易",
								Toast.LENGTH_SHORT).show();
//						ToastCustom.showMessage(LastOderAvtivity.this,"抱歉订单交易失败,请重新交易");
						return;
					}
					
					OrderTask2 otask = new OrderTask2();
					
					otask.execute(HttpUrls.EPAYNUM + "",num, clslogno);
					
				}
			});
			
			
			tv_go_num.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					tv_go_num.setEnabled(false);
					OrderTask3 otask = new OrderTask3();
					
					otask.execute(HttpUrls.GETEPAYNUM + "",clslogno, mobile);
					
				}
			});
	}
	
	class OrderTask2 extends AsyncTask<String, Integer, HashMap<String, Object>> {
		private AlertDialog dialog;

		@Override
		protected void onPreExecute() {
			AlertDialog.Builder builder = new Builder(LastOderAvtivity.this);
			builder.setCancelable(false);
			dialog = builder.create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			Window mWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = mWindow.getAttributes();
			lp.dimAmount = 0f;
			dialog.setContentView(R.layout.load);
			super.onPreExecute();
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2]};
			return NetCommunicate.getPay(HttpUrls.EPAYNUM, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			dialog.dismiss();
			if (result != null) {
				if (Entity.STATE_OK.equals(result.get(Entity.RSPCOD))) {
//					Intent intent = new Intent(EpayActivity.this,
//							OnlineWeb.class);
//					intent.putExtra("urlStr", result.get("REURL").toString());
//					intent.putExtra("titleStr", "充值");
//					startActivity(intent);
					Intent it = new Intent(LastOderAvtivity.this,MentionNowAcitvity.class);
					it.putExtra("State", 3);
					it.putExtra("res", result.get(Entity.RSPMSG).toString());
					startActivity(it);
					((AppContext)getApplication()).setTxnsts("0");
					editor.putString("Txnsts", "0");
					editor.commit();
					finish();
					
				} else {
					
					Intent it = new Intent(LastOderAvtivity.this,MentionNowAcitvity.class);
					it.putExtra("State", 4);
					it.putExtra("res", result.get(Entity.RSPMSG).toString());
					startActivity(it);
					finish();
				}
			}
			super.onPostExecute(result);
		}
	}
	
	class OrderTask3 extends AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在获取...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2]};
			return NetCommunicate.getPay(HttpUrls.GETEPAYNUM, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (Entity.STATE_OK.equals(result.get(Entity.RSPCOD))) {
//					Intent intent = new Intent(EpayActivity.this,
//							OnlineWeb.class);
//					intent.putExtra("urlStr", result.get("REURL").toString());
//					intent.putExtra("titleStr", "充值");
//					startActivity(intent);
//					ToastCustom.showMessage(LastOderAvtivity.this,
//							result.get(Entity.RSPMSG).toString(),
//							Toast.LENGTH_SHORT);
					

					Common.timing(tv_go_num);
				} else {
					tv_go_num.setEnabled(true);
					ToastCustom.showMessage(LastOderAvtivity.this,
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT);
				}
			}
			super.onPostExecute(result);
		}
	}

}
