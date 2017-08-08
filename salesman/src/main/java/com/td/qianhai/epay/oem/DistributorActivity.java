package com.td.qianhai.epay.oem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.adapter.DistributorListAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class DistributorActivity extends BaseActivity{
	
	
	private DistributorListAdapter adapter;
	private ListView listView;
	private ArrayList<HashMap<String, Object>> mList;
	private SharedPreferences share;
	private String isretailers,issaleagt,isgeneralagent,sts,isvip,mobiles,oemid;
	private OneButtonDialogWarn warnDialog;
	private TextView tv_pro,tv_avvmot;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.distributor_activity);
		AppContext.getInstance().addActivity(this);
		share = MyCacheUtil.getshared(DistributorActivity.this);
		oemid = MyCacheUtil.getshared(this).getString("OEMID", "");
		isretailers = MyCacheUtil.getshared(this).getString("ISRETAILERS", "");
		issaleagt = MyCacheUtil.getshared(this).getString("ISSALEAGT", "");
		isgeneralagent = MyCacheUtil.getshared(this).getString("ISGENERALAGENT", "");
		isvip = MyCacheUtil.getshared(this).getString("ISSENIORMEMBER", "");
		sts = MyCacheUtil.getshared(this).getString("STS", "");
		mobiles = MyCacheUtil.getshared(this).getString("Mobile", "");
		
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		((TextView) findViewById(R.id.tv_title_contre)).setText("会员管理");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		tv_avvmot = (TextView) findViewById(R.id.tv_avvmot);
		mList = new ArrayList<HashMap<String, Object>>();
		tv_pro = (TextView) findViewById(R.id.tv_pro);
		listView = (ListView) findViewById(R.id.dis_list);
		adapter = new DistributorListAdapter(this, mList,share,isretailers,issaleagt,isgeneralagent,sts,isvip);
		listView.setAdapter(adapter);
		GetWalletInfo  walletinfo = new GetWalletInfo();
		
		walletinfo.execute(HttpUrls.RICH_TREASURE_INFO + "",
				mobiles);
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		GetWalletInfo  walletinfo = new GetWalletInfo();
		
		walletinfo.execute(HttpUrls.RICH_TREASURE_INFO + "",
				mobiles);
	}
	
	private void loadMore() {
		Log.e("", "    - - "+HttpUrls.DISTRIBUTOR+oemid+"?PHONENUMBER="+mobiles);
			showLoadingDialog("正在查询中...");
			new Thread(run).start();
		}
	
	Runnable run = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = null ;
			try {
				list = NetCommunicate.executeHttpPostnull(
						HttpUrls.DISTRIBUTOR+oemid+"?PHONENUMBER="+mobiles,HttpKeys.DISTRIBUTOR);
			
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
				tv_pro.setVisibility(View.VISIBLE);
				adapter.notifyDataSetChanged();
				break;
			case 2:
				
				Toast.makeText(getApplicationContext(),"未获取到分销商信息",
						Toast.LENGTH_SHORT).show();
				tv_pro.setVisibility(View.GONE);
				break;
			case 3:
				Toast.makeText(getApplicationContext(),"网络不给力,请检查网络设置",
						Toast.LENGTH_SHORT).show();
				tv_pro.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		};
	};
	
	
class GetWalletInfo extends AsyncTask<String , Integer, HashMap<String, Object>>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			
			String[] values = { params[0], params[1] };
			return NetCommunicate
					.getMidatc(HttpUrls.RICH_TREASURE_INFO, values);
			
		}
		
		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			if (result != null) {
				if (result.get(Entity.RSPCOD)!=null&&result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					
					if(result.get("AVAAMT").toString()!=null){
						String avaamt = result.get("AVAAMT").toString();
						double d = Double.parseDouble(avaamt);
						String r = String .format("%.2f",d/100);
//						me_result_money.setText(d/100+"");
						tv_avvmot.setText(r);
						}
					mList.clear();
					loadMore();
				} else {
					
					warnDialog = new OneButtonDialogWarn(DistributorActivity.this,
							R.style.CustomDialog, "提示", result.get(Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									finish();
									warnDialog.dismiss();
								}
							});
					if(warnDialog!=null){
						warnDialog.show();
					}
				}
			} else {
				Toast.makeText(getApplicationContext(),"当前网络不给力,请检查网络设置",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(PhonereChargeActivity.this, "数据获取失败,请检查网络连接");
			}
			super.onPostExecute(result);
			
			}
				
		}


}
