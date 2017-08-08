package com.td.qianhai.epay.oem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.interfaces.onMyaddTextListener;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.MyEditDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

public class BuyCodeActivity extends BaseActivity{
	
	private TextView tv_num1,tv_num3,tv_blance1,btn_buy_code;
	private String oemid,mobiles,isvip,isgeneralagent,issaleagt,isretailers;
	private ArrayList<HashMap<String, Object>> mList;
	private OneButtonDialogWarn warnDialog;
	private  MyEditDialog doubleWarnDialog;
	private String tag = "";
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_cade);
		AppContext.getInstance().addActivity(this);
		isretailers = MyCacheUtil.getshared(this).getString("ISRETAILERS", "");
		issaleagt = MyCacheUtil.getshared(this).getString("ISSALEAGT", "");
		isgeneralagent = MyCacheUtil.getshared(this).getString("ISGENERALAGENT", "");
		isvip = MyCacheUtil.getshared(this).getString("ISSENIORMEMBER", "");
		oemid =  MyCacheUtil.getshared(this).getString("OEMID", "");
		mobiles = MyCacheUtil.getshared(this).getString("Mobile", "");

		
		initview();
		
	}
	private void initview() {
		((TextView) findViewById(R.id.tv_title_contre)).setText("购买激活码");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		tv_num1 = (TextView) findViewById(R.id.tv_num1);
		tv_num3 = (TextView) findViewById(R.id.tv_num3);
		tv_blance1 = (TextView) findViewById(R.id.tv_blance1);
		btn_buy_code = (TextView) findViewById(R.id.btn_buy_code);
		mList = new ArrayList<HashMap<String,Object>>();
		if(isgeneralagent.equals("1")){
			tag = "GENERALAGENT";
		}else{
			if(issaleagt.equals("1")){
				tag = "SALEAGT";
			}else{
				if(isretailers.equals("1")){
					tag = "RETAILERS";
				}else{
					btn_buy_code.setEnabled(false);
					btn_buy_code.setText("无购买权限");
				}
			}
		}
		
		loadMore();
		
		btn_buy_code.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				shwpaypwd();
				
			}
		});
	}
	
	private void loadMore() {
		showLoadingDialog("正在查询中...");
		new Thread(run).start();
	}
	
	Runnable run = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = null ;
			try {
				list = NetCommunicate.executeHttpPostnull1(
						HttpUrls.DISTRIBUTOR+oemid,HttpKeys.DISTRIBUTOR);
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
			
			if (list != null) {
						mList.addAll(list);
					if(mList.size()<=0||mList==null){
						
						msg.what = 2;
					}else{
						msg.what = 1;
					}

			} else {
				loadingDialogWhole.dismiss();
				msg.what = 3;
			}
			loadingDialogWhole.dismiss();
			handler.sendMessage(msg);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String a1 = "0";
				String a2 = "0";
				String a3  = "0";
				for (int i = 0; i < mList.size(); i++) {
					if(mList.get(i).get("alias").toString().equals(tag)){
						 a1 = mList.get(i).get("buyNum").toString();
						 a2 = mList.get(i).get("buyPrice").toString();
						 a3 = mList.get(i).get("buyTotalPrice").toString();
					}
				}
				tv_num1.setText(a1+"个");
				try {
					tv_num3.setText(Double.parseDouble(a2)/100+"元/个");
				} catch (Exception e) {
					// TODO: handle exception
					tv_num3.setText("不可购买");
				}
				try {
					tv_blance1.setText(Double.parseDouble(a3)/100+"元");
				} catch (Exception e) {
					// TODO: handle exception
					tv_blance1.setText("0元");	
				}
				btn_buy_code.setEnabled(true);
				break;
			case 2:
				
				Toast.makeText(getApplicationContext(),"未获取到信息",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(getApplicationContext(),"网络不给力,请检查网络设置",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};
	
	
protected void shwpaypwd() {
		doubleWarnDialog = new MyEditDialog(BuyCodeActivity.this,
				R.style.MyEditDialog, "请输入支付密码", "请输入支付密码", "确认", "取消", "",
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {

						switch (v.getId()) {
						case R.id.btn_right:
							doubleWarnDialog.dismiss();
							InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
							break;
						case R.id.btn_left:
							break;
						default:
							break;
						}
						
					}
				},
		new onMyaddTextListener() {
			
			@Override
			public void refreshActivity(String paypwd) {
				
				if (paypwd == null || paypwd.equals("")) {
					Toast.makeText(getApplicationContext(), "请输入支付密码",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(
//							ActivationCodeManageActivity.this,
//							"请输入支付密码！");
					return;
				}
				if (paypwd.length() < 6 || paypwd.length() > 15) {
					Toast.makeText(getApplicationContext(), "输入的密码长度有误,请输入6个数字、字母或特殊符号",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(
//							ActivationCodeManageActivity.this,
//							"输入的密码长度有误,请输入6个数字、字母或特殊符号！");
					return;
				}
				
					BuyActcode  atask = new BuyActcode();
					atask.execute(HttpUrls.BUYCODE + "", mobiles,paypwd,"");
				}
				
				
		});
		doubleWarnDialog.setCancelable(false);
		doubleWarnDialog.setCanceledOnTouchOutside(false);
		doubleWarnDialog.show();
}
	class BuyActcode extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在处理中。。。");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate.getVerificationMidatc(HttpUrls.BUYCODE,
					values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (Entity.STATE_OK
						.equals(result.get(Entity.RSPCOD).toString())) {
					doubleWarnDialog.dismiss();
					warnDialog = new OneButtonDialogWarn(BuyCodeActivity.this,
							R.style.CustomDialog, "提示", result.get(
									Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									warnDialog.dismiss();
									finish();
								}
							});
					warnDialog.show();
				} else {
					doubleWarnDialog.dismiss();
					Toast.makeText(getApplicationContext(),
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();
				}
			}
			super.onPostExecute(result);
		}
	}

}
