package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.adapter.RateDealRecordsAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.RateListBean;
import com.td.qianhai.epay.oem.interfaces.onMyaddTextListener;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.oem.views.dialog.ChooseDialog;
import com.td.qianhai.epay.oem.views.dialog.MyEditDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class RateListActivity extends BaseActivity implements
		OnScrollListener {

	private String startDate, endDate, mobile;
	private Intent intent;
	private Bundle bundle;
	private EditText et_search;
	private View moreView;
	private View emptyView;
	
	private String  rateid;

	private int page = 1; // 页数
	private int allPageNum = 0; // 总页数
	private int PAGE_SIZE = 10;
	private boolean isThreadRun = false; // 加载数据线程运行状态
	private ListView listView;
	private RateDealRecordsAdapter adapter;
	private ArrayList<HashMap<String, Object>> mList;
	private LayoutInflater inflater;
	private int tags;
	private TextView null_data;
	private ChooseDialog chooseDialog;
	private OneButtonDialogWarn warnDialog;
	
	private MyEditDialog doubleWarnDialog;
	
	private Editor editor;
	
	private String nocein;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rate_list_activity);
		AppContext.getInstance().addActivity(this);
		editor = MyCacheUtil.setshared(RateListActivity.this);
		((TextView) findViewById(R.id.tv_title_contre)).setText("费率列表");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
//		mobile = ((AppContext) getApplication()).getMobile();
		mList = new ArrayList<HashMap<String, Object>>();
		null_data = (TextView) findViewById(R.id.null_data);
		adapter = new RateDealRecordsAdapter(this, mList,tags);
		inflater = LayoutInflater.from(this);
		moreView = inflater.inflate(R.layout.load, null);
		if(moreView==null){
			System.out.println("moreView==null");
		}
		listView = (ListView) findViewById(R.id.rate_detail_list);
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
				
				
				
				nocein = mList.get((int)id).get("FEERATE").toString();
				
				rateid = mList.get((int)id).get("FEERATNO").toString();
				
				String  ratemoney = mList.get((int)id).get("PRICE").toString();
				
				if(((AppContext)getApplicationContext()).getNocr().equals(rateid)){
					
				}else{
					listView.setEnabled(false);
					
					doubleWarnDialog = new MyEditDialog(RateListActivity.this,
	                        R.style.MyEditDialog,"费率升级", "请输入支付密码", "确认", "取消",ratemoney,
	        				new OnMyDialogClickListener() {
						
						@Override
						public void onClick(View v) {
							
							switch (v.getId()) {
							case R.id.btn_right:
								doubleWarnDialog.dismiss();
								listView.setEnabled(true);
								InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
								break;
							case R.id.btn_left:
								listView.setEnabled(true);
								String paypwd = doubleWarnDialog.getpaypwd();
								
								if (paypwd == null || paypwd.equals("")) {
									Toast.makeText(getApplicationContext(),"请输入支付密码",
											Toast.LENGTH_SHORT).show();
//									ToastCustom.showMessage(RateListActivity.this, "请输入支付密码！");
									return;
								}
								if (paypwd.length() < 6||paypwd.length() > 15) {
									Toast.makeText(getApplicationContext(),"输入的密码长度有误,请输入6～15个数字、字母或特殊符号",
											Toast.LENGTH_SHORT).show();
//									ToastCustom.showMessage(RateListActivity.this,"输入的密码长度有误,请输入6～15个数字、字母或特殊符号！");
									return;
								}
								
								GoRateTask  Task =  new GoRateTask();
								Task.execute(HttpUrls.GO_RATE+"",mobile,rateid,paypwd);
								
								break;
							default:
								break;
							}
						}
					},new onMyaddTextListener() {
						
						@Override
						public void refreshActivity(String paypwd) {
							// TODO Auto-generated method stub
							if (paypwd == null || paypwd.equals("")) {
								Toast.makeText(getApplicationContext(),"请输入支付密码",
										Toast.LENGTH_SHORT).show();
//								ToastCustom.showMessage(RateListActivity.this, "请输入支付密码！");
								return;
							}
							if (paypwd.length() < 6||paypwd.length() > 15) {
								Toast.makeText(getApplicationContext(),"",
										Toast.LENGTH_SHORT).show();
//								ToastCustom.showMessage(RateListActivity.this,"输入的密码长度有误,请输入6个数字、字母或特殊符号！");
								return;
							}
							
							GoRateTask  Task =  new GoRateTask();
							Task.execute(HttpUrls.GO_RATE+"",mobile,"0",rateid,paypwd,"");
						}
					});
			doubleWarnDialog.setCancelable(false);
//			doubleWarnDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//				@Override
//				public boolean onKey(DialogInterface dialog, int keyCode,
//						KeyEvent event) {
//					if (keyCode == KeyEvent.KEYCODE_SEARCH) {
//						return true;
//					} else {
//						return true; // 默认返回 false
//					}
//				}
//			});
			doubleWarnDialog.setCanceledOnTouchOutside(false);
			doubleWarnDialog.show();
	                

//				chooseDialog = new ChooseDialog(
//						RateListActivity.this,
//						R.style.CustomDialog,
//						new OnBackDialogClickListener() {
//
//							@Override
//							public void OnBackClick(View v, String str,
//									int position) {
//
//									GoRateTask  Task =  new GoRateTask();
//									Task.execute(HttpUrls.GO_RATE+"",mobile,rateid);
//									
//									
//								chooseDialog.dismiss();
//							}
//						}, "功能选择", RateListActivity.this
//								.getResources().getStringArray(
//										R.array.gorate));
//				chooseDialog.show();
				}
				
				
				
			}
		});
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		listView.setEnabled(true);
	}
	
	/***
	 * 双按钮提示dialog
	 * 
	 * @param v
	 */
	protected void doubleWarnOnClick(View v) {
//		switch (v.getId()) {
//		case R.id.btn_right:
//			doubleWarnDialog.dismiss();
//			break;
//		case R.id.btn_left:
//			GoRateTask  Task =  new GoRateTask();
//			Task.execute(HttpUrls.GO_RATE+"",mobile,rateid);
//			break;
//		default:
//			break;
//		}
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

	private void loadMore() {
		if (page != 1 && page > allPageNum) {
//			ToastCustom.showMessage(this, "没有更多记录了");
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
			String[] values = { String.valueOf(HttpUrls.RATE_QUERY),mobile};
			RateListBean entitys = NetCommunicate.getRateListBean(
					HttpUrls.RATE_QUERY, values);

			Message msg = new Message();
			if (entitys != null) {
				((AppContext)getApplication()).setNocr(entitys.getNocr());
				list = entitys.list;
						mList.addAll(list);
					if(mList.size()<=0||mList==null){
						
						msg.what = 2;
						Log.e("", "111111");
					}else{
						msg.what = 1;
						Log.e("", "22222");
					}

					page++;
			} else {
				loadingDialogWhole.dismiss();
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
				Toast.makeText(getApplicationContext(),"没有获取到您的订单信息",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"没有获取到您的订单信息");
				break;
			case 3:
				emptyView.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(),"订单信息获取失败",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"订单信息获取失败");
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
	
	
	
	
	class GoRateTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog("正在加载中...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2] , params[3], params[4], params[5]};
			return NetCommunicate.getMidatc(HttpUrls.GO_RATE, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {

			loadingDialogWhole.dismiss();
			if (result != null) {

				if (result.get("RSPCOD").equals(Entity.STATE_OK)) {
					String rate = result.get("NOCARDFEERATE").toString();
					((AppContext) getApplication()).setNocein(rate);
					editor.putString("nocardfeerate", rate);
					
					editor.commit();
					doubleWarnDialog.dismiss();
//					((AppContext)getApplication()).setNocein();
//					Intent it = new Intent(RateListActivity.this,
//							OnlineWeb.class);
//					it.putExtra("urlStr", result.get("REURL").toString());
//					startActivity(it);
					
					warnDialog = new OneButtonDialogWarn(RateListActivity.this,
							R.style.CustomDialog, "提示", result.get(
									Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									
									warnDialog.dismiss();
									finish();
								}
							});

					warnDialog.show();

				} else {
					ToastCustom.showMessage(RateListActivity.this,
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT);
				}

			} else {
				ToastCustom.showMessage(RateListActivity.this, "获取数据失败,请检查网络",
						Toast.LENGTH_SHORT);
			}
			super.onPostExecute(result);
		}
	}
}
