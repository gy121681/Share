package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.adapter.QueryOrderPayAdapter;
import com.td.qianhai.epay.oem.adapter.ViewPagerAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.OrderPayBean;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.PullToRefreshListView;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.oem.views.dialog.ChooseDialog;
import com.td.qianhai.epay.oem.views.dialog.OrderDetailDialog;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnBackDialogClickListener;
import com.td.qianhai.epay.utils.StringUtils;

public class DealRecordresultActivity extends BaseActivity implements
OnPageChangeListener {

	private PullToRefreshListView listView;

	private QueryOrderPayAdapter adapter;
	private QueryOrderPayAdapter adapter1;
	private Intent intent;
	private Bundle bundle;
	private String startDate, endDate;// mobile,
	private ArrayList<OrderPayBean> payBeans;
	private ArrayList<OrderPayBean> countPayBeans;
	private OrderPayBean bean;
	
	private ArrayList<OrderPayBean> payBeans1;
	private ArrayList<OrderPayBean> countPayBeans1;
	private OrderPayBean bean1;
	private ChooseDialog chooseDialog;
	private String custId;
	private String mMode = "01";
	private String tn;
	private static final int PLUGIN_NOT_INSTALLED = -1;
	private static final int PLUGIN_NEED_UPGRADE = 2;
	private int isInstall;
	private boolean isInstallResult;
	private View lvBlog_footer;
	private TextView lvBlog_foot_more;
	private ProgressBar lvBlog_foot_progress;
	private View lvBlog_footer1;
	private TextView lvBlog_foot_more1;
	private ProgressBar lvBlog_foot_progress1;
	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;
	private int isLoad;
	private int pageIndex = 1;// 当前页
	private int pageCount = 10;// 每页显示数
	private int tolcnt; // 总记录数
	private int tolcnt1; // 总记录数
	private int totalPages = 1;// 总页数
	private int totalPages1 = 1;// 总页数
	private int pageIndex1 = 1;// 当前页
	private int pageCount1 = 10;// 每页显示数
	private int isLoad1;
	private OrderDetailDialog orderDetailDialog;
	private String phone;
	private boolean tags = false;
//	private String ordernum;
	
	
	private ViewPager viewPager;// 页卡内容
	private ViewPagerAdapter pageradapter;
	private ImageView imageView,go_back;// 动画图片
	private TextView textView1, textView2;
	private ArrayList<View> views;// Tab页面列表
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private View view1, view2;// 各个页卡
	private int screenX, screenY;
	private ImageView img;
	// 界面管理器
	private DisplayMetrics display;
	private LayoutInflater inflater;
	private PullToRefreshListView listview1;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.deal_record_result);
		display = this.getResources().getDisplayMetrics();
		inflater = getLayoutInflater();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenX = metrics.widthPixels;
		screenY = metrics.heightPixels;
		initView();
		initImageView();
	}

	private void initView() {
		
		view1 = inflater.inflate(R.layout.layout_listviews, null);
		view2 = inflater.inflate(R.layout.layout_listviews1, null);
		
		views = new ArrayList<View>();
		
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		
		viewPager.setOnPageChangeListener(this);
		
		textView1 = (TextView) findViewById(R.id.title_a);
		
		textView1.setTextColor(Color.BLACK);
		textView2 = (TextView) findViewById(R.id.title_b);

		viewPager = (ViewPager) findViewById(R.id.viewpager);

		textView1.setOnClickListener(new MyOnClickListener(0));

		textView2.setOnClickListener(new MyOnClickListener(1));
		

		
//		phone = ((AppContext) getApplication()).getMobile();
		phone = MyCacheUtil.getshared(this).getString("Mobile", "");
		custId = MyCacheUtil.getshared(this).getString("Mobile", "");
//		custId = ((AppContext) getApplication()).getCustId();
		countPayBeans = new ArrayList<OrderPayBean>();
		countPayBeans1 = new ArrayList<OrderPayBean>();
		listView = (PullToRefreshListView)view1.findViewById(R.id.deal_record_result_listview);
		listview1 = (PullToRefreshListView)view2.findViewById(R.id.deal_record_result_listview1);
		views.add(listview1);
		views.add(view1);
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});

		((TextView) findViewById(R.id.tv_title_contre)).setText("充值信息");
		
		
		lvBlog_footer = getLayoutInflater().inflate(R.layout.listview_footer,
				null);
		
		lvBlog_foot_more = (TextView) lvBlog_footer
				.findViewById(R.id.listview_foot_more);
		lvBlog_foot_progress = (ProgressBar) lvBlog_footer
				.findViewById(R.id.listview_foot_progress);
		
		lvBlog_footer1= getLayoutInflater().inflate(R.layout.listview_footer1,
				null);
		
		lvBlog_foot_more1 = (TextView) lvBlog_footer
				.findViewById(R.id.listview_foot_more);
		lvBlog_foot_progress1 = (ProgressBar) lvBlog_footer
				.findViewById(R.id.listview_foot_progress);
		listView.addFooterView(lvBlog_footer);// 添加底部视图 必须在setAdapter前
		listview1.addFooterView(lvBlog_footer1);

		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				listView.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (payBeans == null || payBeans.isEmpty())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(lvBlog_footer) == view
							.getLastVisiblePosition())
						scrollEnd = true;

					System.out.println("是否到底部了" + scrollEnd);
				} catch (Exception e) {
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(listView.getTag());
				if (scrollEnd && lvDataState == LISTVIEW_DATA_MORE) {
					listView.setTag(LISTVIEW_DATA_LOADING);
					lvBlog_foot_more.setText("加载中···");
					System.out.println("加载更多");
					lvBlog_foot_progress.setVisibility(View.VISIBLE);
					// 当前pageIndex
					payBeans = null;
					pageIndex++;
					// 列表查询
					QueryOrderRecorTask orderRecorTask = new QueryOrderRecorTask();
					orderRecorTask.execute(HttpUrls.ORDER_QUERY_D + "",custId,
							pageIndex + "", pageCount + "", startDate, endDate);
					lvBlog_foot_progress.setVisibility(View.GONE);
				} else {
					lvBlog_foot_progress.setVisibility(View.GONE);
					lvBlog_foot_more.setText("所有数据加载完毕");
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				listView.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
		
		listview1.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				listview1.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (payBeans1 == null || payBeans1.isEmpty())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(lvBlog_footer1) == view
							.getLastVisiblePosition())
						scrollEnd = true;

					System.out.println("是否到底部了" + scrollEnd);
				} catch (Exception e) {
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(listview1.getTag());
				if (scrollEnd && lvDataState == LISTVIEW_DATA_MORE) {
					listview1.setTag(LISTVIEW_DATA_LOADING);
					lvBlog_foot_more1.setText("加载中···");
					System.out.println("加载更多");
					lvBlog_foot_progress1.setVisibility(View.VISIBLE);
					// 当前pageIndex
					payBeans1 = null;
					pageIndex1++;
					// 列表查询
					QueryOrderRecorTask1 orderRecorTask1 = new QueryOrderRecorTask1();
					orderRecorTask1.execute(HttpUrls.ORDER_QUERY_OVER + "", custId,
							pageIndex1 + "", pageCount1 + "", startDate, endDate);
					lvBlog_foot_progress1.setVisibility(View.GONE);
				} else {
					lvBlog_foot_progress1.setVisibility(View.GONE);
					lvBlog_foot_more1.setText("所有数据加载完毕");
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				listview1.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
		listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				System.out.println("下拉刷新");
				payBeans = null;
				countPayBeans = new ArrayList<OrderPayBean>();
				pageIndex = 1;
				// 列表查询
				QueryOrderRecorTask orderRecorTask = new QueryOrderRecorTask();
				orderRecorTask.execute(HttpUrls.ORDER_QUERY_D + "", custId,
						pageIndex + "", pageCount + "", startDate, endDate);
			}
		});
		
		listview1.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				System.out.println("下拉刷新");
				payBeans1 = null;
				countPayBeans1 = new ArrayList<OrderPayBean>();
				pageIndex1 = 1;
				// 列表查询
				QueryOrderRecorTask1 orderRecorTask1 = new QueryOrderRecorTask1();
				orderRecorTask1.execute(HttpUrls.ORDER_QUERY_OVER + "", custId,
						pageIndex1 + "", pageCount1 + "", startDate, endDate);
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				
				if (arg2 <= countPayBeans.size()) {
					bean = countPayBeans.get((int)id);
					if (bean.getTransts().equals("00")
							|| bean.getTransts().equals("02")
							|| bean.getTransts().equals("99")) {
						chooseDialog = new ChooseDialog(
								DealRecordresultActivity.this,
								R.style.CustomDialog,
								new OnBackDialogClickListener() {

									@Override
									public void OnBackClick(View v, String str,
											int position) {
										// TODO Auto-generated method stub

										if (position == 0) {
											OrderDetailTask  Task =  new OrderDetailTask();
											Task.execute(HttpUrls.ORDER_PAY+"",phone,bean.getPrdordNo(),"1");
											
										} else {
											
											
										}
										chooseDialog.dismiss();
									}
								}, "功能选择", DealRecordresultActivity.this
										.getResources().getStringArray(
												R.array.orderitem));
						chooseDialog.show();
					} else {
						chooseDialog = new ChooseDialog(
								DealRecordresultActivity.this,
								R.style.CustomDialog,
								new OnBackDialogClickListener() {

									@Override
									public void OnBackClick(View v, String str,
											int position) {
										// TODO Auto-generated method stub
										chooseDialog.dismiss();
									}
								}, "功能选择", DealRecordresultActivity.this
										.getResources().getStringArray(
												R.array.queryorder));
						chooseDialog.show();
					}
				}
			}
		});
		listview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				
//				if (arg2 <= countPayBeans1.size()) {
				if(((int)id)==-1){
					
				}else{
					bean1 = countPayBeans1.get((int)id);
					Intent it = new Intent(DealRecordresultActivity.this,OderOverDeteilActivity.class);
					it.putExtra("State", "1");
					Bundle  bundle = new Bundle();
					bundle.putSerializable("bean",bean1);
					it.putExtras(bundle);
					startActivity(it);
				}
					
//					if (bean1.getTransts().equals("00")
//							|| bean1.getTransts().equals("02")
//							|| bean1.getTransts().equals("99")) {
//						chooseDialog = new ChooseDialog(
//								DealRecordresultActivity.this,
//								R.style.CustomDialog,
//								new OnBackDialogClickListener() {
//
//									@Override
//									public void OnBackClick(View v, String str,
//											int position) {
										// TODO Auto-generated method stub
										

//										if (position == 1) {
//											OrderDetailTask  Task =  new OrderDetailTask();
//											Task.execute(HttpUrls.ORDER_PAY+"",phone,bean1.getPrdordNo(),"1");
//											
//											tn = bean1.getTn();
//										} else if (position == 0) {
//											QueryOrderDetailTask task = new QueryOrderDetailTask();
//											task.execute(HttpUrls.ORDER_QUERY
//													+ "", custId,
//													bean1.getPrdordNo(),
//													bean1.getOrderTim(), "1");
//										}
//										chooseDialog.dismiss();
//									}
//								}, "功能选择", DealRecordresultActivity.this
//										.getResources().getStringArray(
//												R.array.queryorderitem));
//						chooseDialog.show();
//					} else {
//						chooseDialog = new ChooseDialog(
//								DealRecordresultActivity.this,
//								R.style.CustomDialog,
//								new OnBackDialogClickListener() {
//
//									@Override
//									public void OnBackClick(View v, String str,
//											int position) {
										// TODO Auto-generated method stub

//										chooseDialog.dismiss();
//									}
//								}, "功能选择", DealRecordresultActivity.this
//								
//										.getResources().getStringArray(
//												R.array.queryorder));
//						chooseDialog.show();
//					}
//				}
			}
		});
//		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//					int arg2, long id) {
//				bean = countPayBeans.get((int)id);
//				if (arg2 <= countPayBeans.size()) {
//					
//					System.out.println(bean.getTn());
//					if (bean.getTransts().equals("00")
//							|| bean.getTransts().equals("02")
//							|| bean.getTransts().equals("99")) {
//						chooseDialog = new ChooseDialog(
//								DealRecordresultActivity.this,
//								R.style.CustomDialog,
//								new OnBackDialogClickListener() {
//
//									@Override
//									public void OnBackClick(View v, String str,
//											int position) {
//										// TODO Auto-generated method stub
//
//										if (position == 1) {
//											OrderDetailTask  Task =  new OrderDetailTask();
//											Task.execute(HttpUrls.ORDER_PAY+"",phone,bean.getPrdordNo(),"1");
//											
//											tn = bean.getTn();
//										} else if (position == 0) {
//											QueryOrderDetailTask task = new QueryOrderDetailTask();
//											task.execute(HttpUrls.ORDER_QUERY
//													+ "", custId,
//													bean.getPrdordNo(),
//													bean.getOrderTim(), "1");
//										}
//										chooseDialog.dismiss();
//									}
//								}, "功能选择", DealRecordresultActivity.this
//										.getResources().getStringArray(
//												R.array.queryorderitem));
//						chooseDialog.show();
//					} else {
//						chooseDialog = new ChooseDialog(
//								DealRecordresultActivity.this,
//								R.style.CustomDialog,
//								new OnBackDialogClickListener() {
//
//									@Override
//									public void OnBackClick(View v, String str,
//											int position) {
//										// TODO Auto-generated method stub
//										chooseDialog.dismiss();
//									}
//								}, "功能选择", DealRecordresultActivity.this
//										.getResources().getStringArray(
//												R.array.queryorder));
//						chooseDialog.show();
//					}
//				}
//
//				return true;
//			}
//		});
		intent = getIntent();
		bundle = intent.getExtras();
		if (bundle != null) {
			startDate = bundle.getString("startDate");
			endDate = bundle.getString("endDate");
		}
		// mobile = ((AppContext) getApplicationContext()).getMobile();
		// 列表查询
		
		QueryOrderRecorTask1 orderRecorTask1 = new QueryOrderRecorTask1();
		orderRecorTask1.execute(HttpUrls.ORDER_QUERY_OVER + "", custId, pageIndex1
				+ "", pageCount1 + "", startDate, endDate);
		
		QueryOrderRecorTask orderRecorTask = new QueryOrderRecorTask();
		orderRecorTask.execute(HttpUrls.ORDER_QUERY_D + "", custId, pageIndex
				+ "", pageCount + "", startDate, endDate);
		
		initadapter();

	}

	class QueryOrderRecorTask extends AsyncTask<String, Integer, OrderPayBean> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog("正在查询中...");
		}

		@Override
		protected OrderPayBean doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3],
					params[4], params[5] };
			return NetCommunicate.getQueryOrderPay(HttpUrls.ORDER_QUERY_D,
					values);
		}

		@Override
		protected void onPostExecute(OrderPayBean result) {
			loadingDialogWhole.dismiss();
			
			if (result != null) {
				if (result.getRspcod().equals(Entity.STATE_OK)) {
					payBeans = new ArrayList<OrderPayBean>();
					for (int i = 0; i < result.list.size(); i++) {
						HashMap<String, Object> map = result.list.get(i);
						bean = new OrderPayBean();
						bean.setMobile(result.getMobile());
						if(map.get("ORDERNUM")!=null){
							bean.setPrdordNo(map.get("ORDERNUM").toString());
						}
						
						bean.setOrderTim(map.get("ORDERTIM").toString());
						if (i == 0)
							if (null != map.get("QN"))
								bean.setQn(map.get("QN").toString());
						bean.setOrdAtm(map.get("ORDAMT").toString());
						if (null != map.get("TN"))
							bean.setTn(map.get("TN").toString());
						if (null != map.get("CLSLOGNO"))
						bean.setOrderNo(map.get("CLSLOGNO").toString());
						if (null != map.get("TRANSTS"))
						bean.setTransts(map.get("TRANSTS").toString());
						if (null != map.get("PRDORDTYPE"))
						bean.setPrdordType(map.get("PRDORDTYPE").toString());
						payBeans.add(bean);
					}
					countPayBeans.addAll(payBeans);
					if (isLoad == 0) {
						adapter = new QueryOrderPayAdapter(
								DealRecordresultActivity.this, countPayBeans);
						listView.setAdapter(adapter);
						tolcnt = Integer.parseInt(result.getTolcnt());
						if (tolcnt % pageCount == 0) {
							totalPages = tolcnt / pageCount;
						} else {
							totalPages = tolcnt / pageCount + 1;
						}
						isLoad = 1;
					} else {
						listView.onRefreshComplete();
						listView.setSelection(0);
						adapter.setOrderBeans(countPayBeans);
						adapter.notifyDataSetChanged();
					}
					if (pageIndex < totalPages) {
						listView.setTag(LISTVIEW_DATA_MORE);
					}

				} else {
					ToastCustom.showMessage(DealRecordresultActivity.this,
							result.getRspmsg().toString(), Toast.LENGTH_SHORT);
				}
				
			}
			super.onPostExecute(result);
		}
	}
	
	
	class QueryOrderRecorTask1 extends AsyncTask<String, Integer, OrderPayBean> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected OrderPayBean doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3],
					params[4], params[5] };
			return NetCommunicate.getQueryOrderPay(HttpUrls.ORDER_QUERY_OVER,
					values);
		}

		@Override
		protected void onPostExecute(OrderPayBean result) {
			if (result != null) {
				if (result.getRspcod().equals(Entity.STATE_OK)) {
					payBeans1 = new ArrayList<OrderPayBean>();
					for (int i = 0; i < result.list.size(); i++) {
						HashMap<String, Object> map = result.list.get(i);
						bean1 = new OrderPayBean();
						bean1.setMobile(result.getMobile());
						if(map.get("CLSLOGNO")!=null){
							bean1.setPrdordNo(map.get("CLSLOGNO").toString());
						}
						
						bean1.setOrderTim(map.get("TXNDAT").toString());
						if (i == 0)
							if (null != map.get("QN"))
								bean1.setQn(map.get("QN").toString());
						bean1.setOrdAtm(map.get("TXNAMT").toString());
						if (null != map.get("TN"))
							bean.setTn(map.get("TN").toString());
						if (null != map.get("CLSLOGNO"))
						bean1.setOrderNo(map.get("CLSLOGNO").toString());
						if (null != map.get("TRANSTS"))
						bean1.setTransts(map.get("TRANSTS").toString());
						if (null != map.get("CLSSTS"))
						bean1.setPrdordType(map.get("CLSSTS").toString());
						if (null != map.get("CLSDAT"))
						bean1.setClsdat(map.get("CLSDAT").toString());
//						
						if (null != map.get("FEERAT"))
						bean1.setFeerat(map.get("FEERAT").toString());
						if (null != map.get("TN"))
						bean1.setClsamt(map.get("CLSAMT").toString());
						if (null != map.get("FEEAMT"))
						bean1.setFrramt(map.get("FEEAMT").toString());
						if (null != map.get("CLSSTS"))
						bean1.setClssts(map.get("CLSSTS").toString());
						if(map.get("FEERAT")!=null){
							bean1.setFeerat(map.get("FEERAT").toString());
						}
						payBeans1.add(bean1);
					}
					countPayBeans1.addAll(payBeans1);
					if (isLoad1 == 0) {
						adapter1 = new QueryOrderPayAdapter(
								DealRecordresultActivity.this, countPayBeans1);
						listview1.setAdapter(adapter1);
						tolcnt1 = Integer.parseInt(result.getTolcnt());
						if (tolcnt1 % pageCount1 == 0) {
							totalPages1 = tolcnt1 / pageCount1;
						} else {
							totalPages1 = tolcnt1 / pageCount1 + 1;
						}
						isLoad1 = 1;
					} else {
						listview1.onRefreshComplete();
						listview1.setSelection(0);
						adapter1.setOrderBeans(countPayBeans1);
						adapter1.notifyDataSetChanged();
					}
					if (pageIndex1 < totalPages1) {
						listview1.setTag(LISTVIEW_DATA_MORE);
					}

				} else {
					ToastCustom.showMessage(DealRecordresultActivity.this,
							result.getRspmsg().toString(), Toast.LENGTH_SHORT);
				}
			}
			super.onPostExecute(result);
		}
	}
	class QueryOrderDetailTask extends
	AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog("正在查询中...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3],
					params[4] };
			return NetCommunicate.getPay(HttpUrls.ORDER_QUERY, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				// if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
				// System.out.println("成功");
				// } else {
				// }
				if (result.get("ORDERNUM") != null) {

					String orderStatus = result.get("RSPMSG").toString();
					String ordermoney = result.get("ORDAMT").toString();
					String orderNo = result.get("ORDERNUM").toString();
					String orderTime = result.get("ORDERTIME").toString();
					String phone = result.get("PHONENUMBER").toString();

					if (ordermoney.length() == 1) {
						ordermoney = "0.0" + ordermoney;
					} else if (ordermoney.length() == 2) {
						ordermoney = "0." + ordermoney;
					} else {
						ordermoney = (ordermoney.substring(0,
								ordermoney.length() - 2)
								+ "." + ordermoney.substring(ordermoney
								.length() - 2));
					}
					String year = orderTime.substring(0, 4);
					String month = orderTime.substring(4, 6);
					String day = orderTime.substring(6, 8);
					String time = orderTime.substring(8, 10);
					String minute = orderTime.substring(10, 12);
					String seconds = orderTime.substring(12, 14);
					orderTime = year + "-" + month + "-" + day + " " + time
							+ ":" + minute + ":" + seconds;

					orderDetailDialog = new OrderDetailDialog(
							DealRecordresultActivity.this,
							R.style.CustomDialog, orderStatus, ordermoney,
							orderNo, orderTime, phone);
					orderDetailDialog.show();
				} else {
					ToastCustom.showMessage(DealRecordresultActivity.this,
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT);
				}
			}
	super.onPostExecute(result);
}
}
	

	class OrderDetailTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog("正在加载中...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3]};
			return NetCommunicate.getPay(HttpUrls.ORDER_PAY, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			
			loadingDialogWhole.dismiss();
			if (result != null) {
				
				if(result.get("RSPCOD").equals(Entity.STATE_OK)){
					
					Intent it = new Intent(DealRecordresultActivity.this,OnlineWeb.class);
					 it.putExtra("urlStr", result.get("REURL").toString());
					 startActivity(it);
					
				}else{
					ToastCustom.showMessage(DealRecordresultActivity.this,
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT);
				}

			}else{
				ToastCustom.showMessage(DealRecordresultActivity.this,
						"获取数据失败,请检查网络",
						Toast.LENGTH_SHORT);
			}
			super.onPostExecute(result);
		}
	}
	
	/**
	 * 头标点击监听
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}

	}
	/**
	 * viewpager滑动图片
	 */
	public void initImageView() {

		imageView = (ImageView) findViewById(R.id.main_cursor);
		bmpW = screenX/3;
//
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) imageView
				.getLayoutParams();
		
		linearParams.width = screenX/2 ;
		imageView.setLayoutParams(linearParams);
		 offset = (screenX / 2-bmpW)/2;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量

		Animation animation = new TranslateAnimation(one * currIndex, one
				* arg0, 0, 0);// 显然这个比较简洁，只有一行代码。
		currIndex = arg0;
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(300);
		imageView.startAnimation(animation);
		
		if (arg0 == 0) {
			textView2.setTextColor(Color.GRAY);
			textView1.setTextColor(Color.BLACK);
		} else {
			tags = true;
			if(tags){
				
			}else{

			}

			textView1.setTextColor(Color.GRAY);
			textView2.setTextColor(Color.BLACK);
		}
		
	}
	
	private void initadapter() {
		pageradapter = new ViewPagerAdapter(views);

		viewPager.setAdapter(pageradapter);
		
	}
	
}
