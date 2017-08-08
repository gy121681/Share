package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;

import com.td.qianhai.epay.oem.adapter.MyProfitAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.ShareDetailsBean;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfitActivity extends BaseActivity implements OnClickListener,OnScrollListener{
	
	private CheckBox cb_title_contre;
	
    private View view;  
    private PopupWindow pop;  
    private TextView pro_pop_1,pro_pop_2,pro_pop_3,pro_pop,bt_title_left,bt_title_right1,pro_pop_4,pro_pop_5,pro_pop_6,pro_pop_7;
    private Drawable drawable,drawable1;
    private MyProfitAdapter adapter;
    private ArrayList<HashMap<String, Object>> mList;
    private ListView listView;
	private int page = 1; // 总页数
	private int allPageNum = 0;
	private int PAGE_SIZE = 10;
	private View moreView;
	private View emptyView;
	private LayoutInflater inflater;
	private boolean isThreadRun = false; 
	private String agtid,shrtype = "";
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myprofit_layout);
		AppContext.getInstance().addActivity(this);
		inflater = LayoutInflater.from(this);
		agtid = MyCacheUtil.getshared(this).getString("Mobile", "");
		
		initView();
		initPopupWindow();
		
		
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

	private void initView() {
		// TODO Auto-generated method stub
		mList = new ArrayList<HashMap<String, Object>>();
		moreView = inflater.inflate(R.layout.load, null);
		listView = (ListView) findViewById(R.id.pro_listview);
		bt_title_right1 = (TextView) findViewById(R.id.bt_title_right1);
		bt_title_right1.setOnClickListener(this);
		bt_title_left = (TextView) findViewById(R.id.bt_title_left);
		bt_title_left.setOnClickListener(this);
		adapter = new MyProfitAdapter(this, mList);
		if (mList.size() == 0) {
			emptyView = inflater.inflate(R.layout.progress_view, null);
			emptyView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			((ViewGroup) listView.getParent()).addView(emptyView);
			listView.setEmptyView(emptyView);
			// 加载数据
			loadMore();
		}
		cb_title_contre = (CheckBox) findViewById(R.id.cb_title_contre);
		cb_title_contre.setOnClickListener(this);
		listView.addFooterView(moreView);
		listView.setOnScrollListener(this);
		listView.setAdapter(adapter);
	}
	
    private void initPopupWindow() {  
        view = this.getLayoutInflater().inflate(R.layout.profite_popupwindow, null);  
        pro_pop = (TextView) view.findViewById(R.id.pro_pop);
        pro_pop_1 = (TextView) view.findViewById(R.id.pro_pop_1);
        pro_pop_2 = (TextView) view.findViewById(R.id.pro_pop_2);
        pro_pop_3 = (TextView) view.findViewById(R.id.pro_pop_3);
        pro_pop_4= (TextView) view.findViewById(R.id.pro_pop_4);
        pro_pop_5= (TextView) view.findViewById(R.id.pro_pop_5);
        pro_pop_6= (TextView) view.findViewById(R.id.pro_pop_6);
        pro_pop_7= (TextView) view.findViewById(R.id.pro_pop_7);
    	pro_pop_4.setVisibility(View.GONE);
    	pro_pop_5.setVisibility(View.GONE);
    	pro_pop_6.setVisibility(View.GONE);
    	pro_pop_7.setVisibility(View.GONE);
        pop = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT,  
                ViewGroup.LayoutParams.WRAP_CONTENT);  
        drawable = getResources().getDrawable(R.drawable.bluechecked);
        drawable1 = getResources().getDrawable(R.drawable.blueonchecked);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        drawable1.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        pop.setOutsideTouchable(true);  
        pop.setOutsideTouchable(true);
        pro_pop.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
                pro_pop.setCompoundDrawables(null, null, drawable, null);
                pro_pop_1.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_2.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_3.setCompoundDrawables(null, null, drawable1, null);
                cb_title_contre.setText("收益汇总");
                shrtype = "";
                page = 1;
                mList.clear();
                loadMore();
            }  
        });
        pro_pop_1.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
                pro_pop.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_1.setCompoundDrawables(null, null, drawable, null);
                pro_pop_2.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_3.setCompoundDrawables(null, null, drawable1, null);
                cb_title_contre.setText("费率收益");
                shrtype = "0";
                page = 1;
                mList.clear();
                loadMore();
            }  
        });  
        pro_pop_2.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
                pro_pop.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_1.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_2.setCompoundDrawables(null, null, drawable, null);
                pro_pop_3.setCompoundDrawables(null, null, drawable1, null);
                cb_title_contre.setText("闪提收益");
                shrtype = "1";
                page = 1;
                mList.clear();
                loadMore();
            }  
        }); 
        pro_pop_4.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
                pro_pop.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_1.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_2.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_3.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_4.setCompoundDrawables(null, null, drawable, null);
                cb_title_contre.setText("三级分销收益");
                shrtype = "3";
                page = 1;
                mList.clear();
                loadMore();
            }  
        }); 
        
        
        pro_pop_3.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
                pro_pop.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_1.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_2.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_3.setCompoundDrawables(null, null, drawable, null);
                cb_title_contre.setText("提现收益");
                shrtype = "2";
                page = 1;
                mList.clear();
                loadMore();
            }  
        }); 
    } 
    
    
    
    
    
    Runnable run = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//			ArrayList<HashMap<String, Object>> list2 = new ArrayList<HashMap<String, Object>>();
			String[] values = { String.valueOf(HttpUrls.INCOMEDETAIL),
					agtid, shrtype,String.valueOf(page), String.valueOf(PAGE_SIZE) };
			ShareDetailsBean entitys = NetCommunicate.getShareDetailsBean(
					HttpUrls.INCOMEDETAIL, values);

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
						Log.e("", "all11 = = = = == "+allPageNum);
					} else {
						allPageNum = allNum / PAGE_SIZE;
						Log.e("", "all22 = = = = == "+allPageNum);
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_title_right1:
            if (pop.isShowing()) {  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
            } else {  
            	 cb_title_contre.setChecked(true);
                pop.showAsDropDown(v);  
            }
			break;
		case R.id.bt_title_left:
			finish();
		default:
			break;
		}
		
	}

}
