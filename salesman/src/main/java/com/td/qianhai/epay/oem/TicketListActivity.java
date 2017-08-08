package com.td.qianhai.epay.oem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.PullToRefreshLayout;
import com.td.qianhai.epay.oem.views.PullToRefreshLayout.OnRefreshListener;
import com.td.qianhai.mpay.utils.DateUtil;

public class TicketListActivity extends BaseActivity{
	
	private ArrayList<HashMap<String, Object>> mList;
	private String mercnum;
	private int PageNum = 1;
	private int NumPerPag = 10;
	private ListView listView;
	private myListAdapter adapter;
	private PullToRefreshLayout refreshlaout;
	private int tag = 0;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticket_list_activity);
		AppContext.getInstance().addActivity(this);
		mercnum = MyCacheUtil.getshared(this).getString("MercNum", "");
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		((TextView) findViewById(R.id.tv_title_contre)).setText("小票列表");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		listView = (ListView) findViewById(R.id.content_view);
		mList = new ArrayList<HashMap<String,Object>>();
		adapter = new myListAdapter(this, mList);
		listView.setAdapter(adapter);
		
		
		refreshlaout =  (PullToRefreshLayout) findViewById(R.id.refresh_view);
		
		refreshlaout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				// TODO Auto-generated method stub
				tag = 1;
				mList.clear();
				adapter.notifyDataSetChanged();
				PageNum = 1;
				loadMore();
				
			}
			
			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				// TODO Auto-generated method stub
				tag = 2;
				loadMore();
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent it = new Intent(TicketListActivity.this,TicketInfoAvtivirt.class);
				it.putExtra("shopname", mList.get((int)arg3).get("shopname").toString());
				it.putExtra("orderno", mList.get((int)arg3).get("orderno").toString());
				it.putExtra("amount", mList.get((int)arg3).get("amount").toString());
				it.putExtra("paytime", mList.get((int)arg3).get("paytime").toString());
				it.putExtra("paytype", mList.get((int)arg3).get("paytype").toString());
				it.putExtra("shoptype", mList.get((int)arg3).get("shoptype").toString());
				startActivity(it);
				
			}
		});
		
		if (mList.size() <= 0) {
			// 加载数据
			loadMore();
		    }
	}
	
	private void loadMore() {
//		WechatListBeanlist.clear();
		new Thread(run).start();
		
		
	}
	
	Runnable run = new Runnable() {

		@Override
		public void run() {
			try {
				String[] values = {mercnum,PageNum+"",NumPerPag+""};
				ArrayList<HashMap<String, Object>> List = NetCommunicate.executeHttpPostgetjsonlist(HttpUrls.TICKETLIST,
						HttpKeys.TICKETLIST_BACK,HttpKeys.TICKETLIST_ASK,values,0);
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
				PageNum++;
			
			handler.sendMessage(msg);
		}
	};

	private Handler handler = new Handler() {
		
		public void handleMessage(Message msg) {
			if(tag == 1){
				refreshlaout.refreshFinish(0);
			}else if(tag == 2){
				refreshlaout.loadmoreFinish(0);
			}
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

		public myListAdapter(Context context,
				ArrayList<HashMap<String, Object>> list) {
			this.context = context;
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
						R.layout.receivableslist_item, null);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_type = (TextView) convertView
						.findViewById(R.id.tv_type);
				holder.tv_money = (TextView) convertView
						.findViewById(R.id.tv_money);
				holder.lin_1 = (LinearLayout) convertView
						.findViewById(R.id.lin_1);
				
				holder.imgs = (ImageView) convertView
				.findViewById(R.id.imgs);
				convertView.setTag(holder);
				
			}
			
			HashMap<String, Object> maps = list.get(position);
			if(maps.get("paytype")!=null){
				String a = maps.get("paytype").toString();
				if(a.equals("1")){
					holder.imgs.setImageResource(R.drawable.wechat_listitem_img);
				}else{
					holder.imgs.setImageResource(R.drawable.zhi);
				}
			}
			
			holder.lin_1 .setVisibility(View.GONE);
	          if(maps.get("paytime")!=null){
	        		String aa = maps.get("paytime").toString();
	        		holder.tv_type.setTextSize(12);
	        		holder.tv_type.setText(DateUtil.formatDateLong(aa));
	          }
	  		if(maps.get("amount")!=null){
				
				String a = String .format("%.2f",Double.parseDouble(maps.get("amount").toString())/100);
				holder.tv_money.setText("+"+a);
//				NumberFormat nf = new DecimalFormat("###,###.##");
//				holder.tv_money.setText("+"+nf.format(Double.parseDouble(a)));
//				holder.tv_money.setText("+"+maps.get("totalFee").toString());
			}
//			if(maps.get("subMobile")!=null){
//				holder.tv_3.setText(maps.get("subMobile").toString());
//			}
//			if(maps.get("shopname")!=null){
//				holder.tv_1.setText(maps.get("shopname").toString());
//			}
//			if(maps.get("mercnam")!=null){
//				holder.tv_2.setText(maps.get("mercnam").toString());
//			}
			
			return convertView;
		}

		class RadioViewHolder {
			TextView tv_time,tv_name,tv_type,tv_money;
			LinearLayout  lin_1 ;
			ImageView imgs;
		}
	}
}
