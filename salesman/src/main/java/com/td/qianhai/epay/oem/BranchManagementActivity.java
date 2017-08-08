package com.td.qianhai.epay.oem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.td.qianhai.epay.oem.adapter.ReceivablesListAdapter2;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;

public class BranchManagementActivity extends BaseActivity{
	private ArrayList<HashMap<String, Object>> mList;
	private String mobile,shopName;
	private TextView cb_title_contre;
	private ListView listView;
	private myListAdapter adapter;
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.branch_management_activity);
		AppContext.getInstance().addActivity(this);
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		Intent it = getIntent();
		 shopName = it.getStringExtra("shopName");
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
		((TextView) findViewById(R.id.tv_title_contre)).setText("分店管理");
		((TextView) findViewById(R.id.bt_title_right)).setVisibility(View.GONE);
		cb_title_contre = (TextView) findViewById(R.id.bt_title_right1);
		cb_title_contre.setText("建立分店");
		cb_title_contre.setVisibility(View.VISIBLE);
//		cb_title_contre.setBackgroundResource(null);
		listView = (ListView) findViewById(R.id.mylist);
		mList = new ArrayList<HashMap<String,Object>>();
		adapter = new myListAdapter(this, mList);
		listView.setAdapter(adapter);
		
		if (mList.size() <= 0) {
			// 加载数据
			loadMore();
		    }
		
		
		
		
		cb_title_contre.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(BranchManagementActivity.this,NewBranchActivirty.class);
				it.putExtra("shopName", shopName);
				startActivity(it);
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final int id = (int)arg3;
				// TODO Auto-generated method stub
				arg1.findViewById(R.id.tv_4).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(mList.get(id).get("subMobile")!=null){
							Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + mList.get(id).get("subMobile").toString()));
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}
					}
				});
			}
		});
	}
	
	
	private void loadMore() {
//		WechatListBeanlist.clear();
		new Thread(run).start();
		
		
	}
	
	Runnable run = new Runnable() {

		@Override
		public void run() {
			try {
				String[] values = {mobile};
				ArrayList<HashMap<String, Object>> List = NetCommunicate.executeHttpPostgetjsonlist(HttpUrls.BRANCHMANAGEMENT,
						HttpKeys.BRANCHMANAGEMENT_BACK,HttpKeys.BRANCHMANAGEMENT_ASK,values,0);
				mList.addAll(List);
//				WechatListBeanlist.addAll(list);
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
//			if (list != null) {
//				mList.addAll(list);
				if (mList.size() <= 0 || mList == null) {

					msg.what = 2;
				} else {
					msg.what = 1;
				}
//			} else {
//				
//				msg.what = 3;
//			}
//			isThreadRun = false;
			
			handler.sendMessage(msg);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				break;
			case 2:
				adapter.notifyDataSetChanged();
				break;
			case 3:
				break;
			default:
				break;
			}
		};
	};
	
	public class myListAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<HashMap<String, Object>> list;
		private String subMchId;

		public myListAdapter(Context context,
				ArrayList<HashMap<String, Object>> list) {
			this.context = context;
			this.subMchId = subMchId;
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			RadioViewHolder holder = null;
			if (convertView != null) {
				holder = (RadioViewHolder) convertView.getTag();
			} else {
				holder = new RadioViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.branchmamager_list_item, null);
				holder.tv_1 = (TextView) convertView
						.findViewById(R.id.tv_1);
				holder.tv_2 = (TextView) convertView
						.findViewById(R.id.tv_2);
				holder.tv_3 = (TextView) convertView
						.findViewById(R.id.tv_3);
				convertView.setTag(holder);
				
			}
			
			HashMap<String, Object> maps = list.get(position);
				
			if(maps.get("subMobile")!=null){
				holder.tv_3.setText(maps.get("subMobile").toString());
			}
			if(maps.get("shopname")!=null){
				holder.tv_1.setText(maps.get("shopname").toString());
			}
			if(maps.get("mercnam")!=null){
				holder.tv_2.setText(maps.get("mercnam").toString());
			}
			
			return convertView;
		}

		
		class RadioViewHolder {
			TextView tv_1,tv_2,tv_3;
		}

	}

}
