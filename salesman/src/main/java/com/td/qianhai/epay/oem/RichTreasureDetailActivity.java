package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.adapter.RichTreasureDealRecordsAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.RichTreasureBillBean;
import com.td.qianhai.epay.oem.beans.TransactionTypeBean;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;

public class RichTreasureDetailActivity extends BaseActivity implements
		OnScrollListener {

	private String startDate, endDate, mobile;
	private Intent intent;
	private Bundle bundle;
	private TranslateAnimation taLeft, taRight, taTop, taBlow;
	private View moreView;
	private View emptyView;

	private int page = 1; // 页数
	private int allPageNum = 0; // 总页数
	private int PAGE_SIZE = 20;
	private boolean isThreadRun = false; // 加载数据线程运行状态
	private ListView listView;
	private RichTreasureDealRecordsAdapter adapter;
	private ArrayList<HashMap<String, Object>> mList;
	private ArrayList<HashMap<String, Object>> mTypeList;
	private LayoutInflater inflater;
	private int tags;
	private TextView null_data;
	private View view;
	private LayoutInflater mInflater;
	private MyAdapter mAdapter;
	private PopupWindow pop;
	private GridView gv;
	private String operstypid = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		intent = getIntent();
		bundle = intent.getExtras();
		tags = bundle.getInt("tag");
		startDate = bundle.getString("startDate");
		endDate = bundle.getString("endDate");
		setContentView(R.layout.regular_list);
		AppContext.getInstance().addActivity(this);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		((TextView) findViewById(R.id.tv_title_contre)).setText("交易明细");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		findViewById(R.id.bt_title_right).setVisibility(View.GONE);
		findViewById(R.id.bt_title_right1).setVisibility(View.VISIBLE);
		findViewById(R.id.bt_title_right1).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (pop.isShowing()) {
							pop.dismiss();
						} else {
							pop.showAsDropDown(v);
							if (mTypeList != null && mTypeList.size() > 0) {

							} else {
								new Thread(run1).start();
							}

						}
					}

				});

		initPopupWindow();
		InitAnima();
		// mobile = ((AppContext) getApplication()).getMobile();
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		mList = new ArrayList<HashMap<String, Object>>();
		mTypeList = new ArrayList<HashMap<String, Object>>();
		null_data = (TextView) findViewById(R.id.null_data);
		adapter = new RichTreasureDealRecordsAdapter(this, mList, tags);
		inflater = LayoutInflater.from(this);
		moreView = inflater.inflate(R.layout.load, null);
		if (moreView == null) {
			System.out.println("moreView==null");
		}
		listView = (ListView) findViewById(R.id.rich_detail_list);
		listView.addFooterView(moreView);
		moreView.setVisibility(View.GONE);
		listView.setOnScrollListener(this);
		listView.setAdapter(adapter);
		if (mList.size() == 0) {
			emptyView = inflater.inflate(R.layout.progress_view, null);
			emptyView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			((ViewGroup) listView.getParent()).addView(emptyView);
			listView.setEmptyView(emptyView);
			// 加载数据
			loadMore();
		}

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				// TODO Auto-generated method stub

				Intent it = new Intent(RichTreasureDetailActivity.this,
						OrderInfoAcitvity.class);
				String odernames = "";
				String ids = "";
				String money = "";
				if(mList.get((int) id).get("OPERSTYPNAM")!=null){
					 odernames = mList.get((int) id).get("OPERSTYPNAM").toString();
				}
				if(mList.get((int) id).get("ALOGNO")!=null){
					 ids = mList.get((int) id).get("ALOGNO").toString();
				}
				if(mList.get((int) id).get("SUMAMT")!=null){
					 money = mList.get((int) id).get("SUMAMT").toString();
				}
				
				
				
				it.putExtra("id", ids);
				it.putExtra("money", money);
				it.putExtra("odernames", odernames);
				startActivity(it);

			}
		});

		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				// TODO Auto-generated method stub
				pop.dismiss();
				if (mTypeList.get((int) id).get("OPERSTYPID") != null) {
					operstypid = mTypeList.get((int) id).get("OPERSTYPID")
							.toString();
				} else {
					operstypid = "";
				}
				page = 1;
				PAGE_SIZE = 20;
				mList.clear();
				loadMore();
				allPageNum = 0;
			}
		});
	}

	// /**
	// * 查询致富宝账单
	// *
	// * @author liangge
	// *
	// */
	// class RichTreasureDetailTask extends
	// AsyncTask<String, Integer, RichTreasureBillBean> {
	// @Override
	// protected void onPreExecute() {
	//
	// super.onPreExecute();
	// }
	//
	// @Override
	// protected RichTreasureBillBean doInBackground(String... params) {
	// String[] values = { params[0], params[1], params[2], params[3],
	// params[4], params[5] };
	// return NetCommunicate.getRichTreasureBill(
	// HttpUrls.RICH_TREASURE_DETAIL, values);
	// }
	//
	// @Override
	// protected void onPostExecute(RichTreasureBillBean result) {
	// if (result != null) {
	// if (result.getRspcod().equals(Entity.STATE_OK)) {
	//
	// } else {
	//
	// }
	// super.onPostExecute(result);
	// }
	// }
	// }
	Runnable run1 = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String[] values = { String.valueOf(HttpUrls.TRANSACTIONTYPE) };
			TransactionTypeBean entitys = NetCommunicate
					.getTransactionTypeBean(HttpUrls.TRANSACTIONTYPE, values);
			Message msg = new Message();
			if (entitys != null) {
				ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
				list = entitys.list;
				if (list != null && list.size() != 0) {
					mTypeList.addAll(list);
					msg.what = 4;
					handler.sendMessage(msg);
				} else {
					msg.what = 5;
				}
			} else {
				msg.what = 6;
			}
		}
	};

	private void loadMore() {
		if (page != 1 && page > allPageNum) {
			Toast.makeText(getApplicationContext(), "没有更多记录了",
					Toast.LENGTH_SHORT).show();
			// ToastCustom.showMessage(this, "没有更多记录了");
			moreView.setVisibility(View.GONE);
			return;
		}
		if (!isThreadRun) {
			isThreadRun = true;
			showLoadingDialog("正在查询中...");
			new Thread(run).start();
		}

	}

	Runnable run = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			// ArrayList<HashMap<String, Object>> list2 = new
			// ArrayList<HashMap<String, Object>>();
			String[] values = { String.valueOf(HttpUrls.RICH_TREASURE_DETAIL),
					mobile, String.valueOf(page), String.valueOf(PAGE_SIZE),
					startDate, endDate, operstypid };
			RichTreasureBillBean entitys = NetCommunicate.getRichTreasureBill(
					HttpUrls.RICH_TREASURE_DETAIL, values);

			Message msg = new Message();
			if (entitys != null) {
				list = entitys.list;
				if (list != null && list.size() != 0) {
					mList.addAll(list);
					// if(tags==1){
					// for (int i = 0; i < list.size(); i++) {
					// if(list.get(i).get("OPERBTYP").toString().equals("0")){
					// if(!list.get(i).get("OPERSTYP").toString().equals("04")){
					// if(!list.get(i).get("OPERSTYP").toString().equals("02")){
					// mList.add(list.get(i));
					// }
					// }
					// }
					// }
					// }else if(tags==2){
					// for (int j = 0; j < list.size(); j++) {
					// if(list.get(j).get("OPERSTYP").toString().equals("21")){
					// //
					// if(!list.get(j).get("OPERSTYP").toString().equals("22")){
					// //
					// if(!list.get(j).get("OPERSTYP").toString().equals("25")){
					// mList.add(list.get(j));
					// }
					//
					// // }
					// // }
					// }
					// }else if(tags==3){
					// Log.e("", "33333333");
					// for (int a = 0; a < list.size(); a++) {
					// if(list.get(a).get("OPERSTYP").toString().equals("04")){
					// mList.add(list.get(a));
					// }
					// }
					// }else if(tags==4){
					// for (int a = 0; a < list.size(); a++) {
					// if(list.get(a).get("OPERSTYP").toString().equals("02")){
					// mList.add(list.get(a));
					// }
					// }
					// }else if(tags==5){
					// for (int a = 0; a < list.size(); a++) {
					// if(list.get(a).get("OPERSTYP").toString().equals("22")){
					// mList.add(list.get(a));
					// }
					// }
					//
					// }else if(tags==6){
					// for (int a = 0; a < list.size(); a++) {
					// if(list.get(a).get("OPERSTYP").toString().equals("25")){
					// mList.add(list.get(a));
					// }
					// }
					//
					// }else if(tags==7){
					// for (int a = 0; a < list.size(); a++) {
					// if(list.get(a).get("OPERSTYP").toString().equals("24")){
					// mList.add(list.get(a));
					// }
					// }
					// }else if(tags==8){
					// for (int a = 0; a < list.size(); a++) {
					// if(list.get(a).get("OPERSTYP").toString().equals("26")){
					// mList.add(list.get(a));
					// }
					// }
					// }else if(tags==9){
					// for (int a = 0; a < list.size(); a++) {
					// if(list.get(a).get("OPERSTYP").toString().equals("27")){
					// mList.add(list.get(a));
					// }
					// }
				} else {
					loadingDialogWhole.dismiss();
					msg.what = 2;

				}
				int allNum = 0;
				if (entitys.getTolcnt() != null
						&& !entitys.getTolcnt().equals("null")) {
					allNum = Integer.parseInt(entitys.getTolcnt());
				}

				if (allNum % PAGE_SIZE != 0) {
					allPageNum = allNum / PAGE_SIZE + 1;
				} else {
					allPageNum = allNum / PAGE_SIZE;
				}

				if (mList.size() <= 0 || mList == null) {

					msg.what = 2;
				} else {
					msg.what = 1;
				}

				page++;
			} else {
				loadingDialogWhole.dismiss();
				msg.what = 3;
			}
			// }
			// else {
			// loadingDialogWhole.dismiss();
			// msg.what = 3;
			// }
			isThreadRun = false;
			loadingDialogWhole.dismiss();
			handler.sendMessage(msg);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				null_data.setVisibility(View.GONE);

				moreView.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
				break;
			case 2:

				if (moreView != null) {
					listView.setVisibility(View.GONE);
					emptyView.setVisibility(View.GONE);
					moreView.setVisibility(View.GONE);
				}
				null_data.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), "没有获取到您的订单信息",
						Toast.LENGTH_SHORT).show();
				// ToastCustom.showMessage(RichTreasureDetailActivity.this,
				// "没有获取到您的订单信息");
				break;
			case 3:
				emptyView.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(), "订单信息获取失败",
						Toast.LENGTH_SHORT).show();
				// ToastCustom.showMessage(RichTreasureDetailActivity.this,
				// "订单信息获取失败");
				break;
			case 4:
				mAdapter = new MyAdapter();
				gv.setAdapter(mAdapter);
				break;
			case 5:
				Toast.makeText(getApplicationContext(), "订单类型获取失败",
						Toast.LENGTH_SHORT).show();
				break;
			case 6:
				Toast.makeText(getApplicationContext(), "网络异常",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

	private int lastItem;// 当前显示的最后一项

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1; // 减1是因为上面加了个addFooterView
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			if (lastItem == mList.size()) {
				moreView.setVisibility(View.VISIBLE);
				loadMore();
			}
		}
	}

	private void initPopupWindow() {
		view = mInflater.inflate(R.layout.transaction_type_pop, null);
		gv = (GridView) view.findViewById(R.id.gridView2);
		pop = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
//		pop.setFocusable(true);
		pop.setTouchable(true);
		pop.setOutsideTouchable(true);
		pop.update();

	}

	private class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTypeList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mTypeList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.tran_type_item, null);
				holder = new ViewHolder();
				holder.textview1 = (TextView) convertView
						.findViewById(R.id.textviewss);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			HashMap<String, Object> text = mTypeList.get(position);
//			OPERSTYPID
			if(text.get("OPERSTYPID")!=null&&text.get("OPERSTYPID").toString().equals("35")){
				holder.textview1.setText("宝币兑换");
			}else{
				holder.textview1.setText(text.get("OPERSTYPNAM").toString());
			}
			

			Random ran = new Random();
			int rand = ran.nextInt(4);
			switch (rand) {
			case 0:
				convertView.startAnimation(taLeft);
				break;
			case 1:
				convertView.startAnimation(taRight);
				break;
			case 2:
				convertView.startAnimation(taTop);
				break;
			case 3:
				convertView.startAnimation(taBlow);
				break;
			}
			return convertView;
		}

		class ViewHolder {
			public TextView textview1;
		}
	}

	private void InitAnima() {
		// TODO Auto-generated method stub

		// push_left_in=AnimationUtils.loadAnimation(this, R.anim.push_left_in);
		// push_right_in=AnimationUtils.loadAnimation(this,
		// R.anim.push_right_in);
		// slide_top_to_bottom=AnimationUtils.loadAnimation(this,
		// R.anim.slide_top_to_bottom);
		// slide_bottom_to_top=AnimationUtils.loadAnimation(this,
		// R.anim.slide_bottom_to_top);

		taLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		taRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		taTop = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		taBlow = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		taLeft.setDuration(1000);
		taRight.setDuration(1000);
		taTop.setDuration(1000);
		taBlow.setDuration(1000);
	}
}
