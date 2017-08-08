package com.td.qianhai.epay.oem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.td.qianhai.epay.oem.adapter.EtcListAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.dateutil.ScreenInfo;
import com.td.qianhai.epay.oem.dateutil.WheelMain;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class EtcListAcitvity extends BaseActivity implements  OnScrollListener{
	private EtcListAdapter adapter;
	private int page = 1; // 页数
	private int allPageNum = 0; // 总页数
	private int PAGE_SIZE = 10;
	private boolean isThreadRun = false; // 加载数据线程运行状态
	private ListView listview;
	private LayoutInflater inflater;
	private ArrayList<HashMap<String, Object>> mList;
	private String mercnum,year = "",month = "";
	private CheckBox cb_title_contre;
	private View view;
	private WheelMain wheelMain;
	private PopupWindow mPopupWindowDialog;
	private Button determine,cacel;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.etc_list_activity);
		AppContext.getInstance().addActivity(this);
		mercnum =  MyCacheUtil.getshared(this).getString("MercNum", "");
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		String datess = initdate(1);
		year = datess.substring(0, 4);
		month = datess.substring(4, 6);
		
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		cb_title_contre = (CheckBox) findViewById(R.id.cb_title_contre);
		cb_title_contre.setText(initdate(2));
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		cb_title_contre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showdate();
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
		listview.setOnScrollListener(this);
		adapter = new EtcListAdapter(this, mList);
		listview.setAdapter(adapter);
		
		if (mList.size() == 0) {
			// 加载数据
			loadMore();
		}
		
	}
	
	private String initdate(int a){
		SimpleDateFormat formatter = null; 
		if(a==1){
			formatter = new SimpleDateFormat ("yyyyMM");
		}else{
			formatter = new SimpleDateFormat ("yyyy年MM月"); 
		}
		
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间 
		String str = formatter.format(curDate); 
		return str;
	}
	
	private void loadMore() {
		if (page != 1 && page > allPageNum) {
			Toast.makeText(getApplicationContext(), "没有更多记录了",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(this, "没有更多记录了");
//			moreView.setVisibility(View.GONE);
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
			ArrayList<HashMap<String, Object>> list = null;

				String[] values = { HttpUrls.ETCLIST+"",mercnum,allPageNum+"",PAGE_SIZE+"",year,month };
				list = NetCommunicate.getList(HttpUrls.ETCLIST, values,HttpKeys.ETCLIST_BACK);

			Message msg = new Message();

			if (list != null) {
				mList.addAll(list);
				if (list.size() <= 0 || list == null) {

					msg.what = 2;
				} else {
					msg.what = 1;
				}
				
				int allNum = 0;
				if(list.size()>0){
				if(list.get(0).get("TOLCNT")!=null&&!list.get(0).get("TOLCNT").equals("null")){
					 allNum = Integer.parseInt(list.get(0).get("TOLCNT").toString());
				}
				}

				if (allNum % PAGE_SIZE != 0) {
					allPageNum = allNum / PAGE_SIZE + 1;
				} else {
					allPageNum = allNum / PAGE_SIZE;
				}
				page++;
				
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
				break;
			case 2:
				adapter.notifyDataSetChanged();
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
	
	private void showdate() {
//		setLayoutY(lin_1, 0);
		
//		Calendar calendar = Calendar.getInstance();
//
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH);
//		int day = 1;
//		int hour = calendar.get(Calendar.HOUR_OF_DAY);
//		int min = calendar.get(Calendar.MINUTE);
		
		
		Calendar calendar = Calendar.getInstance();

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		
		view = inflater.inflate(R.layout.choose_dialog, null);
		setPopupWindowDialog();

		ScreenInfo screenInfo = new ScreenInfo(EtcListAcitvity.this);
		wheelMain = new WheelMain(view, 10);
		wheelMain.screenheight = screenInfo.getHeight();
		wheelMain.initDateTimePicker(year, month, day, hour, min);

		if (mPopupWindowDialog != null) {
			mPopupWindowDialog.showAtLocation(
					findViewById(R.id.lin_1), Gravity.BOTTOM
							| Gravity.CENTER_HORIZONTAL, 0, 0);
		}

		bottomBtn();
		
	}
	
	protected void setPopupWindowDialog() {
		// TODO Auto-generated method stub
		determine = (Button) view.findViewById(R.id.textview_dialog_album);
		cacel = (Button) view.findViewById(R.id.textview_dialog_cancel);

		mPopupWindowDialog = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindowDialog.setAnimationStyle(R.style.popwin_anim_style);  
		mPopupWindowDialog.setFocusable(false);
		mPopupWindowDialog.update();
		mPopupWindowDialog.setBackgroundDrawable(new BitmapDrawable(
				getResources(), (Bitmap) null));
//		mPopupWindowDialog.setBackgroundDrawable(getResources().getDrawable(R.drawable.air_city_button));
		mPopupWindowDialog.setOutsideTouchable(true);
//		mPopupWindowDialog.setClippingEnabled(false);
	}
	
	
	protected void bottomBtn() {
		// TODO Auto-generated method stub
		determine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if (mPopupWindowDialog != null
						&& mPopupWindowDialog.isShowing()) {
					mPopupWindowDialog.dismiss();
//					setLayoutY(lin_1, 0);
				}
				
			}
		});

		cacel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String dates = wheelMain.getTime();
				if(wheelMain.getTime().length()>=8){
					String a = dates.substring(0, 4);
					String b = dates.substring(4, 6);
					String c = dates.substring(dates.length()-2);
					cb_title_contre.setText(a+"年"+b+"月");
					year = a;
					month = b;
					mList.clear();
					page = 1;
					allPageNum = 0;
					loadMore();
				}
				
				if (mPopupWindowDialog != null
						&& mPopupWindowDialog.isShowing()) {
					mPopupWindowDialog.dismiss();
//					setLayoutY(lin_1, 0);
				}
			}
		});
	}
	
	private int lastItem;// 当前显示的最后一项
	@Override
	public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int arg3) {
		// TODO Auto-generated method stub
		lastItem = firstVisibleItem + visibleItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == SCROLL_STATE_IDLE) {
			if (lastItem == mList.size()) {
				loadMore();
			}
		}
		
	}

}
