package com.td.qianhai.epay.oem;

import java.util.HashMap;

import com.td.qianhai.epay.oem.ShouKuanBAvtivity.ShouKuanTask;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddLicensePlateActivity extends BaseActivity {
	private String tag = "",mercnum;
	private Button btn_next;
	private OneButtonDialogWarn warnDialog;
	private EditText tv_number;
	private Spinner sp;
	private String color="";
	private String getnumber,getcolor;
	private LinearLayout lin_color;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.add_license_plate_activity);
		mercnum  = MyCacheUtil.getshared(this).getString("MercNum", "");
		
		Intent it = getIntent();
		tag = it.getStringExtra("tag");
		getnumber = it.getStringExtra("setnumber");
		getcolor = it.getStringExtra("setcolor");
		initview();
	}

	private void initview() {
		tv_number =  (EditText) findViewById(R.id.tv_number);
		btn_next = (Button) findViewById(R.id.btn_next);
		sp = (Spinner) findViewById(R.id.spinner);
		lin_color  = (LinearLayout) findViewById(R.id.lin_color);
		if (tag.equals("0")) {
			btn_next.setText("绑定车牌");
			
		} else {
			Log.e("", " - - "+getnumber);
			tv_number.setText(getnumber);
			btn_next.setText("解绑车牌");
			if(getcolor==null){
				lin_color.setVisibility(View.GONE);
			}else{
				if(getcolor.equals("blue")){
					sp.setSelection(1,true);
				}else if(getcolor.equals("white")){
					sp.setSelection(2,true);
				}else if(getcolor.equals("yellow")){
					sp.setSelection(3,true);
				}else if(getcolor.equals("black")){
					sp.setSelection(4,true);
				}
			}
			
			sp.setEnabled(false);
			tv_number.setEnabled(false);
		}
		// TODO Auto-generated method stub
		((TextView) findViewById(R.id.tv_title_contre)).setText("车牌信息");
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					color = "";
					break;
				case 1:
					color = "blue";
					break;
				case 2:
					color = "white";
					break;
				case 3:
					color = "yellow";
					break;
				case 4:
					color = "black";
					break;

				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (tag.equals("1")) {
					Unbundling();
				} else {
					bundling();
				}
			}
		});
	}
	private void Unbundling() {
		// TODO Auto-generated method stub
		
		String number = tv_number.getText().toString();
		
		UnbundlingTask unbundling = new UnbundlingTask();

		unbundling.execute(HttpUrls.UNBUNDLING + "",mercnum,number);
	}
	private void bundling() {
		// TODO Auto-generated method stub
		String number = tv_number.getText().toString();
		if(number==null||number.equals("")){
			warnDialog = new OneButtonDialogWarn(
					AddLicensePlateActivity.this, R.style.CustomDialog,
					"提示", "请填写正确车牌号", "确定",
					new OnMyDialogClickListener() {
						@Override
						public void onClick(View v) {
							warnDialog.dismiss();
						}
					});
			warnDialog.show();
			warnDialog.setCancelable(false);
			warnDialog.setCanceledOnTouchOutside(false);
			return;
		}
		if(color.equals("")){
			warnDialog = new OneButtonDialogWarn(
					AddLicensePlateActivity.this, R.style.CustomDialog,
					"提示", "请选择车牌颜色", "确定",
					new OnMyDialogClickListener() {
						@Override
						public void onClick(View v) {
							warnDialog.dismiss();
						}
					});
			warnDialog.show();
			warnDialog.setCancelable(false);
			warnDialog.setCanceledOnTouchOutside(false);
			return;
		}
		
		BundlingTask bundling = new BundlingTask();

		bundling.execute(HttpUrls.OWNERLICENSEPLATE + "",mercnum,number,color);
	}
	
	class UnbundlingTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在加载...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {

			String[] values = { params[0], params[1], params[2] };
			return NetCommunicate.getMidatc(HttpUrls.UNBUNDLING,
					values);

		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();

			if (result != null) {
				if (result.get(Entity.RSPCOD) != null
						&& result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					warnDialog = new OneButtonDialogWarn(
							AddLicensePlateActivity.this, R.style.CustomDialog,
							"提示", result.get(Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									finish();
									warnDialog.dismiss();
								}
							});
					warnDialog.show();
					warnDialog.setCancelable(false);
					warnDialog.setCanceledOnTouchOutside(false);

				} else {
					warnDialog = new OneButtonDialogWarn(
							AddLicensePlateActivity.this, R.style.CustomDialog,
							"提示", result.get(Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									finish();
									warnDialog.dismiss();
								}
							});
					warnDialog.show();
					warnDialog.setCancelable(false);
					warnDialog.setCanceledOnTouchOutside(false);
				}
			} else {
				Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络连接",
						Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);

		}

	}
	
	class BundlingTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在加载...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {

			String[] values = { params[0], params[1], params[2],params[3] };
			return NetCommunicate.getMidatc(HttpUrls.OWNERLICENSEPLATE,
					values);

		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();

			if (result != null) {
				if (result.get(Entity.RSPCOD) != null
						&& result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					warnDialog = new OneButtonDialogWarn(
							AddLicensePlateActivity.this, R.style.CustomDialog,
							"提示", result.get(Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									finish();
									warnDialog.dismiss();
								}
							});
					warnDialog.show();
					warnDialog.setCancelable(false);
					warnDialog.setCanceledOnTouchOutside(false);

				} else {
					warnDialog = new OneButtonDialogWarn(
							AddLicensePlateActivity.this, R.style.CustomDialog,
							"提示", result.get(Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									finish();
									warnDialog.dismiss();
								}
							});
					warnDialog.show();
					warnDialog.setCancelable(false);
					warnDialog.setCanceledOnTouchOutside(false);
				}
			} else {
				Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络连接",
						Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);

		}

	}

}
