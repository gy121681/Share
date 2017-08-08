package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.adapter.MyCirecleAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.MyCircleBean;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;

public class MyCircleActivity extends BaseActivity implements
		OnScrollListener{
	
	private int page = 1; // 页数
	private int allPageNum = 0; // 总页数
	private int PAGE_SIZE = 20;
	private ArrayList<HashMap<String, Object>> mList;
	private ListView listView;
	private String mobile;
	private View moreView;
	private View emptyView;
	private boolean isThreadRun = false; // 加载数据线程运行状态
	private TextView null_data,tv_profit,tv_propty;
	private MyCircleBean entitys;
	private MyCirecleAdapter adapter;
	private LayoutInflater inflater;
	private ImageView im_mycircle;


	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.activity_mycircle);
//		mobile = ((AppContext)getApplication()).getMobile();
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		inintview();
		
	}
	
	private void inintview() {
		((TextView) findViewById(R.id.tv_title_contre)).setText("我的推广收益");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		
		inflater = LayoutInflater.from(this);
		mList = new ArrayList<HashMap<String, Object>>();
		null_data = (TextView) findViewById(R.id.null_data);
		listView = (ListView) findViewById(R.id.mylist);
		
		im_mycircle = (ImageView) findViewById(R.id.im_mycircle);
		tv_profit = (TextView) findViewById(R.id.tv_profit);
		tv_propty = (TextView) findViewById(R.id.tv_propty);
		im_mycircle.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(MyCircleActivity.this,PromotionEarningActivity.class);
				startActivity(it);
			}
		});	
		
		moreView = inflater.inflate(R.layout.load, null);
		listView.addFooterView(moreView);
		moreView.setVisibility(View.GONE);
		listView.setOnScrollListener(this);
		
		

		
	if (mList.size() == 0) {
		emptyView = inflater.inflate(R.layout.progress_view, null);
		emptyView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		((ViewGroup) listView.getParent()).addView(emptyView);
		listView.setEmptyView(emptyView);
		// 加载数据
		loadMore();
	    }
	
		adapter = new MyCirecleAdapter(this, mList, 0);
		listView.setAdapter(adapter);
	}
	private void loadMore() {
		if (page != 1 &&page > allPageNum) {
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
//			ArrayList<HashMap<String, Object>> list2 = new ArrayList<HashMap<String, Object>>();
			String[] values = { String.valueOf(HttpUrls.MYCIRCLE),
					mobile, String.valueOf(page), String.valueOf(PAGE_SIZE)};
			 entitys = NetCommunicate.getMyCircleBean(
					HttpUrls.MYCIRCLE, values);
			Message msg = new Message();
			if (entitys != null) {
				list = entitys.list;
				if(entitys.getTolcnt()!=null&&!entitys.getTolcnt().equals("null")){
					
					int allNum = Integer.parseInt(entitys.getTolcnt());
					if (allNum % PAGE_SIZE != 0) {
						allPageNum = allNum / PAGE_SIZE + 1;
					} else {
						allPageNum = allNum / PAGE_SIZE;
					}
				}
				


				if (list != null && list.size() != 0) {
					mList.addAll(list);
					if(mList.size()<=0||mList==null){
						msg.what = 2;
					}else{
						msg.what = 1;
					}

					page++;
				} else {
					
					loadingDialogWhole.dismiss();
					msg.what = 2;
				}
			}
			 else {
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
				if(entitys!=null){
					if(entitys.getTotshramt()!=null&&!entitys.getTotshramt().equals("0")&&!entitys.getTotshramt().equals("")){
						im_mycircle.setBackgroundResource(R.drawable.img_profit);
						tv_profit.setText(Double.parseDouble(entitys.getTotshramt())/100+"");
						tv_propty.setVisibility(View.VISIBLE);
					}else{
						im_mycircle.setBackgroundResource(R.drawable.num_profit);
					}
				}
				
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
				Toast.makeText(getApplicationContext(),"没有更多记录",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(MyCircleActivity.this,
//						"没有更多记录");
				break;
			case 3:
				emptyView.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(),"加载获取失败",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(MyCircleActivity.this,
//						"加载获取失败");
				break;
			default:
				break;
			}
		};
	};
	
	private int lastItem;// 当前显示的最后一项

	@Override
	public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int arg3) {
		// TODO Auto-generated method stub
		lastItem = firstVisibleItem + visibleItemCount - 1; // 减1是因为上面加了个addFooterView
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == SCROLL_STATE_IDLE) {
			if (lastItem == mList.size()) {
				moreView.setVisibility(View.VISIBLE);
				loadMore();
			}
		}
	}

}
