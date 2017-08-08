package com.td.qianhai.epay.oem;


import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.DESKey;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;

//public class MyProfitActivitys extends BaseActivity{
//	
//	private GridView gridview;
//	private ArrayList<HashMap<String, Object>> mList;
//	private MyAdapter adapter;
//	private TextView tv_all,tv_all_si,tv_grade;
//	private SharedPreferences share;
//	private String mobile,pwd,oemid,isretailers,issaleagt,isgeneralagent;
//	private String t1,t2;
//	private ImageView grade_1,grade_2,grade_3;
//	
//	@Override
//	@SuppressLint("NewApi")
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.my_profit_activitys);
//		 share = MyCacheUtil.getshared(this);
//		 mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
//		 oemid = MyCacheUtil.getshared(this).getString("OEMID", "");
//		isretailers = MyCacheUtil.getshared(this).getString("ISRETAILERS", "");
//		issaleagt = MyCacheUtil.getshared(this).getString("ISSALEAGT", "");
//		isgeneralagent = MyCacheUtil.getshared(this).getString("ISGENERALAGENT", "");
//		 
//		 pwd = share.getString("userpwd","");
//		
//		initview();
//	}
//
//	private void initview() {
//		// TODO Auto-generated method stub
//		mList = new ArrayList<HashMap<String,Object>>();
//		gridview = (GridView) findViewById(R.id.gridview);
//		tv_grade = (TextView) findViewById(R.id.tv_grade);
//		grade_1 = (ImageView) findViewById(R.id.grade_1);
//		grade_2 = (ImageView) findViewById(R.id.grade_2);
//		grade_3 = (ImageView) findViewById(R.id.grade_3);
//		((TextView) findViewById(R.id.tv_title_contre)).setText("收益");
//		tv_all = (TextView) findViewById(R.id.tv_all);
//		tv_all_si = (TextView) findViewById(R.id.tv_all_si);
//		findViewById(R.id.bt_title_left).setOnClickListener(
//				new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						finish();
//					}
//				});
//
//		adapter = new MyAdapter(this, mList);
//		gridview.setAdapter(adapter);
//		
//		loadMore();
//		
//		
//		if(isgeneralagent.equals("1")){
//			tv_grade.setText("代理商");
//			grade_1.setVisibility(View.VISIBLE);
//			grade_2.setVisibility(View.VISIBLE);
//			grade_3.setVisibility(View.VISIBLE);
//			return;
//		}
//		if(issaleagt.equals("1")){
//			tv_grade.setText("分销商");
//			grade_2.setVisibility(View.VISIBLE);
//			grade_3.setVisibility(View.VISIBLE);
//			return;
//		}
//		if(isretailers.equals("1")){
//			tv_grade.setText("零售商");
//			grade_3.setVisibility(View.VISIBLE);
//			return;
//		}
//		
//	}
//	
//	
//	private void loadMore() {
//		showLoadingDialog("正在查询中...");
//		new Thread(run).start();
//	}
//
//Runnable run = new Runnable() {
//
//	@Override
//	public void run() {
//		ArrayList<HashMap<String, Object>> list = null ;
//		try {
//			list = NetCommunicate.executeHttpPostnull(
//					HttpUrls.MYPROFIT+initaes(),HttpKeys.MYPROFITBACK);
//			
//		} catch (HttpHostConnectException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		Message msg = new Message();
//		
//		if (list != null) {
//					t1 = String .format("%.2f",Double.parseDouble(list.get(0).get("AMT").toString())/100);
//					t2 = String .format("%.2f",Double.parseDouble(list.get(1).get("AMT").toString())/100);
//					list.remove(0);
//					mList.addAll(list);
//				if(mList.size()<=0||mList==null){
//					
//					msg.what = 2;
//				}else{
//					msg.what = 1;
//				}
//
//		} else {
//			loadingDialogWhole.dismiss();
//			msg.what = 3;
//		}
//		loadingDialogWhole.dismiss();
//		handler.sendMessage(msg);
//	}
//};
//
//private String initaes() {
//	// TODO Auto-generated method stub
//
//	JSONObject jsonObj = new JSONObject();
//	try {
//		jsonObj.put("PHONENUMBER", mobile);
//		jsonObj.put("PASSWORD", pwd);
//		jsonObj.put("OEMID", oemid);
//	} catch (JSONException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	String aa = null;
//	try {
//		aa = DESKey.AES_Encode(jsonObj.toString(),
//				"f15f1ede25a2471998ee06edba7d2e29");
//		aa = URLEncoder.encode(aa);
//
//	} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	return aa;
//}
//
//private Handler handler = new Handler() {
//	public void handleMessage(Message msg) {
//		switch (msg.what) {
//		case 1:
//			
////			String t1 = String .format("%.2f",Double.parseDouble(mList.get(0).get("AMT").toString())/100);
////			String t2 = String .format("%.2f",Double.parseDouble(mList.get(1).get("AMT").toString())/100);
//			tv_all.setText(t1);
//			tv_all_si.setText(t2);
//			mList.remove(0);
////			for (int i = 0; i < mList.size(); i++) {
////				if(mList.get(i).get("OPERSTYP").toString()==null||mList.get(i).get("OPERSTYP").equals("")){
////					mList.remove(i);
////				}
////			}
//			adapter.notifyDataSetChanged();
//			break;
//		case 2:
//			
//			Toast.makeText(getApplicationContext(),"未获取到分销商信息",
//					Toast.LENGTH_SHORT).show();
//			break;
//		case 3:
//			Toast.makeText(getApplicationContext(),"网络不给力,请检查网络设置",
//					Toast.LENGTH_SHORT).show();
//			break;
//		default:
//			break;
//		}
//	};
//};
//	
//	
//	public class MyAdapter extends BaseAdapter {
//
//		private Context mContext;
//		private LayoutInflater inflater;
//		private ArrayList<HashMap<String, Object>> dataList;
//
//		// private class GirdTemp{
//		// ImageView phone_function_pic;
//		// TextView phone_function_name;
//		// }
//		public MyAdapter(Context c,ArrayList<HashMap<String, Object>> dataList) {
//			this.dataList = dataList;
//			mContext = c;
//			inflater = (LayoutInflater) mContext
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		}
//
//		@Override
//		public int getCount() {
//			return dataList.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return position;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder viewHolder;
//			if (convertView == null) {
//				convertView = inflater.inflate(R.layout.myprofit_grid_item, null);
//				viewHolder = new ViewHolder();
//
//				viewHolder.tv_wallet = (TextView) convertView
//						.findViewById(R.id.tv_wallet);
//
//				viewHolder.title = (TextView) convertView
//						.findViewById(R.id.tv_wallet_p);
//
//				convertView.setTag(viewHolder);
//			} else {
//				viewHolder = (ViewHolder) convertView.getTag();
//			}
//			 
//				  HashMap<String, Object> maps = dataList.get(position);
//				  String t = String .format("%.2f",Double.parseDouble(maps.get("AMT").toString())/100);
//				  viewHolder.tv_wallet.setText(t);
//				  viewHolder.title.setText(maps.get("TYPNAM").toString());
//			// viewHolder.
//			return convertView;
//		}
//
//		class ViewHolder {
//			public TextView title;
//			public TextView tv_wallet;
//		}
//
//	}
//
//}
public class MyProfitActivitys extends BaseActivity{
	
	private GridView gridview;
	private ArrayList<HashMap<String, Object>> mList;
	private MyAdapter adapter;
	private TextView tv_all,tv_all_si,tv_grade;
	private SharedPreferences share;
	private String mobile,pwd,oemid,isretailers,issaleagt,isgeneralagent,isvip,isareaagent;
	private String t1,t2,grade = "0";
	private ImageView grade_1,grade_2,grade_3;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_profit_activitys);
		 share = MyCacheUtil.getshared(this);
		 mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		 oemid = MyCacheUtil.getshared(this).getString("OEMID", "");
		 isvip = MyCacheUtil.getshared(this).getString("ISSENIORMEMBER", "");
		 isareaagent = MyCacheUtil.getshared(this).getString("ISAREAAGENT", "");
		isretailers = MyCacheUtil.getshared(this).getString("ISRETAILERS", "");
		issaleagt = MyCacheUtil.getshared(this).getString("ISSALEAGT", "");
		isgeneralagent = MyCacheUtil.getshared(this).getString("ISGENERALAGENT", "");
		 
		 pwd = share.getString("userpwd","");
		
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		mList = new ArrayList<HashMap<String,Object>>();
		((TextView)findViewById(R.id.bt_title_right1)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.bt_title_right1)).setText("明细");
		((TextView)findViewById(R.id.bt_title_right)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.bt_title_right1)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(MyProfitActivitys.this,MyProxyReturnActivity.class);
				it.putExtra("seltype", "");
				it.putExtra("tag", "");
				startActivity(it);
			}
		});
		gridview = (GridView) findViewById(R.id.gridview);
		tv_grade = (TextView) findViewById(R.id.tv_grade);
		grade_1 = (ImageView) findViewById(R.id.grade_1);
		grade_2 = (ImageView) findViewById(R.id.grade_2);
		grade_3 = (ImageView) findViewById(R.id.grade_3);
		((TextView) findViewById(R.id.tv_title_contre)).setText("收益");
		tv_all = (TextView) findViewById(R.id.tv_all);
		tv_all_si = (TextView) findViewById(R.id.tv_all_si);
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});

		adapter = new MyAdapter(this, mList);
		gridview.setAdapter(adapter);
		
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				// TODO Auto-generated method stub
				if(mList.get((int)id).get("OPERSTYP").toString().equals("07")){
					Intent it = new Intent(MyProfitActivitys.this,MyProxyReturnActivity.class);
					it.putExtra("seltype", "2");
					it.putExtra("tag", "");
					startActivity(it);
				}else if(mList.get((int)id).get("OPERSTYP").toString().equals("010")){
					Intent it = new Intent(MyProfitActivitys.this,MyProxyReturnActivity.class);
					it.putExtra("seltype", "");
					it.putExtra("tag", "4");
					startActivity(it);
				}else if(mList.get((int)id).get("OPERSTYP").toString().equals("08")){
					Intent it = new Intent(MyProfitActivitys.this,MyProxyReturnActivity.class);
					it.putExtra("seltype", "");
					it.putExtra("tag", "5");
					startActivity(it);
				}else if(mList.get((int)id).get("OPERSTYP").toString().equals("011")){
					Intent it = new Intent(MyProfitActivitys.this,MyProxyReturnActivity.class);
					it.putExtra("seltype", "");
					it.putExtra("tag", "6");
					startActivity(it);
				}
			}
		});
		tv_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(MyProfitActivitys.this,MyProxyReturnActivity.class);
				it.putExtra("seltype", "");
				it.putExtra("tag", "");
				startActivity(it);
			}
		});
		
		loadMore();
		if(isareaagent!=null&&isareaagent.equals("1")){
			tv_grade.setText("省级代理");
			return;
		}else if(isareaagent!=null&&isareaagent.equals("2")){
			tv_grade.setText("市级代理");
			return;
		}else if(isareaagent!=null&&isareaagent.equals("3")){
			tv_grade.setText("区级代理");
			return;
		}
		if(isgeneralagent.equals("1")){
			grade = "3";
			tv_grade.setText("服务商");
			grade_1.setVisibility(View.VISIBLE);
			grade_2.setVisibility(View.VISIBLE);
			grade_3.setVisibility(View.VISIBLE);
			return;
		}
		if(issaleagt.equals("1")){
			grade = "2";
			tv_grade.setText("服务商");
			grade_2.setVisibility(View.VISIBLE);
			grade_3.setVisibility(View.VISIBLE);
			return;
		}
		if(isretailers.equals("1")){
			grade = "1";
			tv_grade.setText("服务商");
			grade_3.setVisibility(View.VISIBLE);
			return;
		}

		if(isvip.equals("1")){
			tv_grade.setText("高级会员");
			return;
		}
	
		
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
					HttpUrls.MYPROFIT+initaes(),HttpKeys.MYPROFITBACK);
			
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
					t1 = String .format("%.2f",Double.parseDouble(list.get(0).get("AMT").toString())/100);
					t2 = String .format("%.2f",Double.parseDouble(list.get(1).get("AMT").toString())/100);
					list.remove(0);
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

private String initaes() {
	// TODO Auto-generated method stub

	JSONObject jsonObj = new JSONObject();
	try {
		jsonObj.put("PHONENUMBER", mobile);
		jsonObj.put("PASSWORD", pwd);
		jsonObj.put("OEMID", oemid);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	String aa = null;
	try {
		aa = DESKey.AES_Encode(jsonObj.toString(),
				"f15f1ede25a2471998ee06edba7d2e29");
		aa = URLEncoder.encode(aa);

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return aa;
}

private Handler handler = new Handler() {
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case 1:
			
//			String t1 = String .format("%.2f",Double.parseDouble(mList.get(0).get("AMT").toString())/100);
//			String t2 = String .format("%.2f",Double.parseDouble(mList.get(1).get("AMT").toString())/100);
			tv_all.setText(t1);
			tv_all_si.setText(t2);
			mList.remove(0);
//			for (int i = 0; i < mList.size(); i++) {
//				if(mList.get(i).get("OPERSTYP").toString()==null||mList.get(i).get("OPERSTYP").equals("")){
//					mList.remove(i);
//				}
//			}
			adapter.notifyDataSetChanged();
			break;
		case 2:
			
			Toast.makeText(getApplicationContext(),"未获取到分销商信息",
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
	
	
	public class MyAdapter extends BaseAdapter {

		private Context mContext;
		private LayoutInflater inflater;
		private ArrayList<HashMap<String, Object>> dataList;

		// private class GirdTemp{
		// ImageView phone_function_pic;
		// TextView phone_function_name;
		// }
		public MyAdapter(Context c,ArrayList<HashMap<String, Object>> dataList) {
			this.dataList = dataList;
			mContext = c;
			inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.myprofit_grid_item, null);
				viewHolder = new ViewHolder();

				viewHolder.tv_wallet = (TextView) convertView
						.findViewById(R.id.tv_wallet);

				viewHolder.title = (TextView) convertView
						.findViewById(R.id.tv_wallet_p);
				viewHolder.img_m = (ImageView) convertView.findViewById(R.id.img_m);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			


				  HashMap<String, Object> maps = dataList.get(position);
				  String t = String .format("%.2f",Double.parseDouble(maps.get("AMT").toString())/100);
					 viewHolder.img_m.setVisibility(View.GONE);
			if (maps.get("OPERSTYP").toString().equals("07")
					|| maps.get("OPERSTYP").toString().equals("08")
					|| maps.get("OPERSTYP").toString().equals("010")
					|| maps.get("OPERSTYP").toString().equals("011")) {
						 viewHolder.img_m.setVisibility(View.VISIBLE);
					 }else{
						 viewHolder.img_m.setVisibility(View.GONE);
					 }
//					 if(maps.get("OPERSTYP").toString().equals("010")){
//						 viewHolder.img_m.setVisibility(View.VISIBLE);
//					 }else{
//						 viewHolder.img_m.setVisibility(View.GONE);
//					 }
//					 if(maps.get("OPERSTYP").toString().equals("08")){
//						 viewHolder.img_m.setVisibility(View.VISIBLE);
//					 }else{
//						 viewHolder.img_m.setVisibility(View.GONE);
//					 }
//					 if(maps.get("OPERSTYP").toString().equals("011")){
//						 viewHolder.img_m.setVisibility(View.VISIBLE);
//					 }else{
//						 viewHolder.img_m.setVisibility(View.GONE);
//					 }
//					 
						if(grade.equals("2")){
							if(maps.get("OPERSTYP").toString().equals("011")){
								viewHolder.tv_wallet.setTextColor(mContext.getResources().getColor(R.color.gray));
								
							}
						}else if(grade.equals("1")){
							if(maps.get("OPERSTYP").toString().equals("011")){
								viewHolder.tv_wallet.setTextColor(mContext.getResources().getColor(R.color.gray));
							}
							if(maps.get("OPERSTYP").toString().equals("08")){
								viewHolder.tv_wallet.setTextColor(mContext.getResources().getColor(R.color.gray));
							}
						}else if(grade.equals("0")){
							if(maps.get("OPERSTYP").toString().equals("011")){
								viewHolder.tv_wallet.setTextColor(mContext.getResources().getColor(R.color.gray));
							}
							if(maps.get("OPERSTYP").toString().equals("08")){
								viewHolder.tv_wallet.setTextColor(mContext.getResources().getColor(R.color.gray));
							}
							if(maps.get("OPERSTYP").toString().equals("010")){
								viewHolder.tv_wallet.setTextColor(mContext.getResources().getColor(R.color.gray));
							}
						}
				  viewHolder.tv_wallet.setText(t);
				  viewHolder.title.setText(maps.get("TYPNAM").toString());
			// viewHolder.
			return convertView;
		}

		class ViewHolder {
			public TextView title;
			public TextView tv_wallet;
			private ImageView img_m;
		}

	}

}