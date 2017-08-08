package com.td.qianhai.epay.oem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import com.td.qianhai.epay.oem.adapter.EtcAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EtcActivity extends BaseActivity {

	private ListView listview;
	private ArrayList<HashMap<String, Object>> mList;
	private EtcAdapter adapter;
	private boolean isThreadRun = false; // 加载数据线程运行状态
	private String mercnum;
	private TextView null_data;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.etc_activity);
		AppContext.getInstance().addActivity(this);
		initview();
		loadMore();
		
	}
	

	private void initview() {
		// TODO Auto-generated method stub
		
		((TextView) findViewById(R.id.bt_title_right)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.bt_title_right1)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.bt_title_right1)).setText("记录");
		((TextView) findViewById(R.id.tv_title_contre)).setText("车牌管理");
		findViewById(R.id.bt_title_right1).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent it = new Intent(EtcActivity.this,EtcListAcitvity.class);
						startActivity(it); 
					}
				});
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		mList = new ArrayList<HashMap<String, Object>>();
		listview = (ListView) findViewById(R.id.mylist);
		null_data = (TextView) findViewById(R.id.null_data);
		adapter = new EtcAdapter(this, mList);
		listview.setAdapter(adapter);
		mercnum  = MyCacheUtil.getshared(this).getString("MercNum", "");
		
	
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				Intent it = new Intent(EtcActivity.this,AddLicensePlateActivity.class);
				it.putExtra("setnumber",mList.get((int)id).get("LICENSENUMBER").toString());
				
				if(mList.get((int)id).get("LICENSECOLOR")!=null){
					it.putExtra("setcolor",mList.get((int)id).get("LICENSECOLOR").toString());
				}
				
				it.putExtra("tag", "1");
				startActivity(it);
			}
		});
		
		null_data.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(EtcActivity.this,AddLicensePlateActivity.class);
				it.putExtra("tag", "0");
				startActivity(it);
			}
		});

		
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
		mList.clear();
		loadMore();
	}
	
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//
//
//	}
	

	private void loadMore() {
		// TODO Auto-generated method stub
		if (!isThreadRun) {
			isThreadRun = true;
			showLoadingDialog("正在查询中...");
			new Thread(run).start();
		}
	}

	Runnable run = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = null;

				String[] values = { HttpUrls.OWNERLICENSEPLATE1+"",mercnum };
				list = NetCommunicate.getList(HttpUrls.OWNERLICENSEPLATE1, values,HttpKeys.OWNERLICENSEPLATE1_BACK);

			Message msg = new Message();

			if (list != null) {
				mList.addAll(list);
				if (list.size() <= 0 || list == null) {

					msg.what = 2;
				} else {
					msg.what = 1;
				}
			} else {

				msg.what = 3;
			}
			isThreadRun = false;
			loadingDialogWhole.dismiss();
			handler.sendMessage(msg);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				listview.setVisibility(View.VISIBLE);
				adapter.notifyDataSetChanged();
				null_data.setVisibility(View.GONE);
				
				break;
			case 2:
				listview.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
				null_data.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), "加载完毕",
						Toast.LENGTH_SHORT).show();

				break;
			case 3:
				Toast.makeText(getApplicationContext(), "网络不给力,请检查网络设置",
						Toast.LENGTH_SHORT).show();

				break;
			default:
				break;
			}
		};
	};

}
