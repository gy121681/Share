package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;

import com.td.qianhai.epay.oem.adapter.PEAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.PromotionEarningBean;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PromotionEarningActivity extends BaseActivity implements OnScrollListener{
	
    private ArrayList<HashMap<String, Object>> mList;
    private ListView listView;
	private int page = 1; // 总页数
	private PEAdapter adapter;
	private int allPageNum = 0;
	private int PAGE_SIZE = 10;
	private View moreView;
	private View emptyView;
	private LayoutInflater inflater;
	private boolean isThreadRun = false; 
	private String agtid;
	private View view;  
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promotion_earnings);
		AppContext.getInstance().addActivity(this);
		inflater = LayoutInflater.from(this);
		agtid = MyCacheUtil.getshared(this).getString("Mobile", "");
		
		initview();
	}

	private void initview() {
		((TextView) findViewById(R.id.tv_title_contre)).setText("推广收益明细");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		
		mList = new ArrayList<HashMap<String, Object>>();
		moreView = inflater.inflate(R.layout.load, null);
		listView = (ListView) findViewById(R.id.pro_listview);
		
		if (mList.size() == 0) {
			emptyView = inflater.inflate(R.layout.progress_view, null);
			emptyView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			((ViewGroup) listView.getParent()).addView(emptyView);
			listView.setEmptyView(emptyView);
			// 加载数据
			loadMore();
		}
		adapter = new PEAdapter(this, mList);
		listView.addFooterView(moreView);
		listView.setOnScrollListener(this);
		listView.setAdapter(adapter);
	}
	private void loadMore() {
		// TODO Auto-generated method stub
		if (page != 1 && page >= allPageNum) {
			Toast.makeText(getApplicationContext(),"没有更多记录了",
					Toast.LENGTH_SHORT).show();
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
			String[] values = { String.valueOf(HttpUrls.PROMOTIONEARNINGS),
					agtid,String.valueOf(page), String.valueOf(PAGE_SIZE),"",""};
			PromotionEarningBean entitys = NetCommunicate.getPromotionEarningBean(
					HttpUrls.PROMOTIONEARNINGS, values);

			Message msg = new Message();
			if (entitys != null) {
				list = entitys.list;
				if (list != null && list.size() != 0) {
					list = entitys.list;
					mList.addAll(list);
					
					msg.what = 1;
					
					int allNum = Integer.parseInt(entitys.getTolcnt());

					if (allNum % PAGE_SIZE != 0) {
						allPageNum = allNum / PAGE_SIZE + 1;
					} else {
						allPageNum = allNum / PAGE_SIZE;
					}
					
					if(mList.size()<=0||mList==null){
						
						msg.what = 2;
					}else{
						msg.what = 1;
					}
					page++;
				}else{
					loadingDialogWhole.dismiss();
					msg.what = 2;
				}

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
				
				moreView.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
				break;
			case 2:
				
				if (moreView != null) {
					listView.setVisibility(View.GONE);
					emptyView.setVisibility(View.GONE);
					moreView.setVisibility(View.GONE);
				}
				Toast.makeText(getApplicationContext(),"没有获取到您的订单信息",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				emptyView.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(),"订单信息获取失败",
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
}
