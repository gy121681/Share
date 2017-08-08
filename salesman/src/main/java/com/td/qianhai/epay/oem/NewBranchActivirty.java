package com.td.qianhai.epay.oem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewBranchActivirty extends BaseActivity {

	private String mobile,shopName;
	
	private TextView tv_1,btn_scree;
	
	private EditText ed_1,ed_2,ed_3;
	
	private HashMap<String, Object> result = null; 
	
	private OneButtonDialogWarn warnDialog;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_branch_activirty);
		AppContext.getInstance().addActivity(this);
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		Intent it = getIntent();
		shopName= it.getStringExtra("shopName");
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((TextView) findViewById(R.id.tv_title_contre)).setText("建立分店");
		tv_1 = (TextView) findViewById(R.id.tv_1);
		if(shopName!=null){
			tv_1.setText(shopName);
		}
	
		ed_1 = (EditText) findViewById(R.id.ed_1);
		ed_2 = (EditText) findViewById(R.id.ed_2);
		ed_3 = (EditText) findViewById(R.id.ed_3);
		btn_scree = (TextView) findViewById(R.id.btn_scree);
		
		btn_scree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(ed_1.getText()==null||ed_1.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "请填写分店名称", Toast.LENGTH_SHORT).show();
					return;
				}
				if(ed_2.getText()==null||ed_2.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "请填写分店手机号", Toast.LENGTH_SHORT).show();
					return;
				}
				if(ed_3.getText()==null||ed_3.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "请填写店主姓名", Toast.LENGTH_SHORT).show();
					return;
				}
				
				   new Thread(run1).start();
			}
		});
	}
	
	Runnable run1 = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = null;
			
			try {
				
				String[] values = {mobile,ed_3.getText().toString(),ed_2.getText().toString(),ed_1.getText().toString()};
				result = NetCommunicate.executeHttpPostnulls(HttpUrls.NEWBRANCH,
						HttpKeys.NEWBRANCH_BACK,HttpKeys.NEWBRANCH_ASK,values);
			} catch (HttpHostConnectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Message msg = new Message();
			if(result!=null){
				
				msg.what = 1;
			}else{
				msg.what = 5;
			}
			handler1.sendMessage(msg);
			
		}
	};
	
	private Handler handler1 = new Handler() {
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case 1:
			if(result.get("RSPCOD").toString().equals("000000")){
				
				warnDialog = new OneButtonDialogWarn(NewBranchActivirty.this,
						R.style.CustomDialog, "提示", result.get("RSPMSG").toString(), "确定",
						new OnMyDialogClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								warnDialog.dismiss();
								finish();
							}
						});
				warnDialog.show();
			}else if(result.get("RSPCOD").toString().equals("000001")){
				warnDialog = new OneButtonDialogWarn(NewBranchActivirty.this,
						R.style.CustomDialog, "提示", result.get("RSPMSG").toString(), "确定",
						new OnMyDialogClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								warnDialog.dismiss();
							}
						});
				warnDialog.show();
				
			}else if(result.get("RSPCOD").toString().equals("000002")){
				warnDialog = new OneButtonDialogWarn(NewBranchActivirty.this,
						R.style.CustomDialog, "提示", result.get("RSPMSG").toString(), "确定",
						new OnMyDialogClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								warnDialog.dismiss();
							}
						});
				warnDialog.show();
				
			}else if(result.get("RSPCOD").toString().equals("000003")){
				warnDialog = new OneButtonDialogWarn(NewBranchActivirty.this,
						R.style.CustomDialog, "提示", result.get("RSPMSG").toString(), "确定",
						new OnMyDialogClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								warnDialog.dismiss();
							}
						});
				warnDialog.show();
				
			}else if(result.get("RSPCOD").toString().equals("000004")){
				warnDialog = new OneButtonDialogWarn(NewBranchActivirty.this,
						R.style.CustomDialog, "提示", result.get("RSPMSG").toString(), "确定",
						new OnMyDialogClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								warnDialog.dismiss();
							}
						});
				warnDialog.show();
			}else if(result.get("RSPCOD").toString().equals("000005")){
				warnDialog = new OneButtonDialogWarn(NewBranchActivirty.this,
						R.style.CustomDialog, "提示", result.get("RSPMSG").toString(), "确定",
						new OnMyDialogClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								warnDialog.dismiss();
							}
						});
				warnDialog.show();
			}
			break;
		case 2:
			break;
		case 5:
			break;
		default:
			break;
		}
	};
};
	
}
