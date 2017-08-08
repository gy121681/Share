package com.td.qianhai.epay.oem;

import java.util.HashMap;

import com.td.qianhai.epay.oem.BussinessInfoActivity.BussinessInfoTask;
import com.td.qianhai.epay.oem.BussinessInfoActivity.ComitAdressTask;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.AnimationUtil;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.oem.views.city.CityPicker;
import com.td.qianhai.epay.oem.views.city.onChoiceCytyChilListener;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddRegionActivity extends BaseActivity implements OnClickListener{
	
	private TextView tv_1,tv_2,tv_3,tv_conmmit,tv_finishs;
	private  CityPicker citypicker;
	private Button btn_next;
	private LinearLayout tv_finish;
	private String bankProvinceid,bankCityid,bankareid,mercnum;
	private OneButtonDialogWarn warnDialog;
	
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addregion_activity);
		
		mercnum = MyCacheUtil.getshared(this).getString("MercNum","");
		initview();
	}

	private void initview() {
		((TextView) findViewById(R.id.tv_title_contre)).setText("完善地区");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "请先提交", Toast.LENGTH_SHORT).show();
					}
				});
		// TODO Auto-generated method stub
		tv_finish = (LinearLayout) findViewById(R.id.tv_finish);
		tv_finishs = (TextView) findViewById(R.id.tv_finishs);
		tv_1 = (TextView) findViewById(R.id.tv_1);
		tv_1.setOnClickListener(this);
		tv_2 = (TextView) findViewById(R.id.tv_2);
		tv_2.setOnClickListener(this);
		tv_3 = (TextView) findViewById(R.id.tv_3);
		tv_3.setOnClickListener(this);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		citypicker = (CityPicker) findViewById(R.id.citypicker);
		tv_conmmit = (TextView) findViewById(R.id.tv_comit);
		tv_conmmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AnimationUtil.GoButton(AddRegionActivity.this, tv_finish);
				tv_finish.setVisibility(View.GONE);
				citypicker.setfirstdata();
			}
		});
		
		tv_finishs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AnimationUtil.GoButton(AddRegionActivity.this, tv_finish);
				tv_finish.setVisibility(View.GONE);
				
			}
		});
		
		citypicker.getcity(new onChoiceCytyChilListener() {
			
			@Override
			public void onClick(String i, String v, String a, String ni, String vi,
					String ai) {
				// TODO Auto-generated method stub
				bankProvinceid = i;
				bankCityid = v;
				bankareid = a;
				try {
					tv_1.setText(ni);
					tv_2.setText(vi);
					tv_3.setText(ai);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
			}
		});
		

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_1:
			AnimationUtil.GoTop(AddRegionActivity.this, tv_finish);
			tv_finish.setVisibility(View.VISIBLE);
			break;
		case R.id.tv_2:
			AnimationUtil.GoTop(AddRegionActivity.this, tv_finish);
			tv_finish.setVisibility(View.VISIBLE);
			break;
		case R.id.tv_3:
			AnimationUtil.GoTop(AddRegionActivity.this, tv_finish);
			tv_finish.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_next:
			
			commit();
			break;
			
		default:
			break;
		}
		
	}

	private void commit() {
		// TODO Auto-generated method stub
		if(tv_1.getText()==null||tv_1.getText().toString().equals("")){
			Toast.makeText(getApplicationContext(), "请选择省市区", Toast.LENGTH_SHORT).show();
			return;
		}
		if(tv_2.getText()==null||tv_2.getText().toString().equals("")){
			Toast.makeText(getApplicationContext(), "请选择省市区", Toast.LENGTH_SHORT).show();
			return;
		}
		ComitAdressTask task = new ComitAdressTask();
		task.execute(HttpUrls.COMITADRESS + "", mercnum,
				bankProvinceid, bankCityid,bankareid);
	}
	

	class ComitAdressTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在加载...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {

			String[] values = { params[0], params[1], params[2], params[3] ,params[4]};
			return NetCommunicate.get(HttpUrls.COMITADRESS, values);

		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD) != null
						&& result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					warnDialog = new OneButtonDialogWarn(AddRegionActivity.this,
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
				}else{
					Toast.makeText(getApplicationContext(), result
							.get(Entity.RSPMSG).toString(), Toast.LENGTH_SHORT).show();
				}
			}
			super.onPostExecute(result);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Toast.makeText(getApplicationContext(), "请先提交", Toast.LENGTH_SHORT).show();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
