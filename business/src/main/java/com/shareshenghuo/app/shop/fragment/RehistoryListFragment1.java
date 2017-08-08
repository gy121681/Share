package com.shareshenghuo.app.shop.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.widget.MyOnclicListener;
import com.example.widget.SelectBirthday;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.RehistoryListActivityAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.PaystatusListBean;
import com.shareshenghuo.app.shop.network.bean.RehistoryBean;
import com.shareshenghuo.app.shop.network.bean.ShopOrderDetailBean;
import com.shareshenghuo.app.shop.network.request.NumRequest;
import com.shareshenghuo.app.shop.network.request.OrderDispositionRequest;
import com.shareshenghuo.app.shop.network.response.RehistoryListResponse;
import com.shareshenghuo.app.shop.network.response.ShopOrderDetailResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;

public class RehistoryListFragment1 extends BaseFragment implements OnClickListener,OnRefreshListener2<ListView>{
	
	private SelectBirthday birth;
	private String initstartdate = "";
	private TextView tv1,tv2,tv3;
	private RehistoryListActivityAdapter adapter;
	private int pageNo = 1;
	private int pageSize = 10;
	private PullToRefreshListView lvData;
	private List<PaystatusListBean> pay_status_list;
	private List<PaystatusListBean> discount_rate_list;
	public String pay_statusid = "", discount_rateid = "" ,pay_date = "";
	private View views;
	private int statusBarHeight1;
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.rehistory_list_activity1;
	}

	@Override
	protected void init(View rootView) {
		initstartdate = DateUtil.gettosevenday(false);
		
		int resourceId = getResources().getIdentifier("status_bar_height","dimen", "android");
		if (resourceId > 0) {// 根据资源ID获取响应的尺寸值
			statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
		}else{
			statusBarHeight1 = 20;
		}
		
		initview();
		getStatisticsData();
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		loadData();
	}

	private void initview() {
		// TODO Auto-generated method stub
		getView(R.id.lllayout1s).setOnClickListener(this);
		getView(R.id.lllayout2s).setOnClickListener(this);
		getView(R.id.lllayout3).setOnClickListener(this);
		views = getView(R.id.views);
		tv1 = getView(R.id.tv1);
		tv2 = getView(R.id.tv2);
		tv3 = getView(R.id.tv3);
		lvData = getView(R.id.lvShop);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		
	}
	
	public void getStatisticsData() {
		NumRequest req = new NumRequest();
		req.shop_id = UserInfoManager.getUserInfo(activity).shop_id+"";
		
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETSHOPORDERDETAILS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				Log.e("", " - - --  ");
				ShopOrderDetailResponse bean = new Gson().fromJson(resp.result, ShopOrderDetailResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					init(bean.data);
				}
			}
		});
	}
	
	private void init(ShopOrderDetailBean data) {
		// TODO Auto-generated method stub
		pay_status_list = data.pay_status_list;
		discount_rate_list = data.discount_rate_list;
//		for (int i = 0; i < data.discount_rate_list.size(); i++) {
//			list.add(data.discount_rate_list.get(i).name);
//		}
		
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(activity, "请稍候");
		OrderDispositionRequest req = new OrderDispositionRequest();
		req.shop_id = UserInfoManager.getUserInfo(activity).shop_id+"";
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		req.pay_status = pay_statusid;
		req.discount_rate  = discount_rateid;
		req.pay_date = pay_date;
		req.type = "1";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.SHOPPAYCLSINFLISTSNEW, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				lvData.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				lvData.onRefreshComplete();
				Log.e("", " = = =  "+resp.result);
				RehistoryListResponse bean = new Gson().fromJson(resp.result, RehistoryListResponse.class);
				if(Api.SUCCEED == bean.result_code)
					updateView(bean.data);
//					Utility.setListViewHeightBasedOnChildren(lvMsg);
			}
		});
	}
    
	public void updateView(List<RehistoryBean> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new RehistoryListActivityAdapter(activity, data);
			lvData.setAdapter(adapter);
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}
	

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lllayout1s:
			if(pay_status_list!=null){
				showPopupWindow(getView(R.id.lllayout1s), pay_status_list,0);
			}
			
			break;
		case R.id.lllayout2s:
			if(discount_rate_list!=null){
				showPopupWindow(getView(R.id.lllayout2s), discount_rate_list,1);
			}
			break;
		case R.id.lllayout3:
			setdate();
			break;
		default:
			break;
		}
	}
	
	
	public void setdate(){
//		if(getActivity()==null){
//			return;
//		}
		birth = new SelectBirthday(activity,initstartdate,new MyOnclicListener() {

			public void MyOnclicListener(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.submit:
					initstartdate = birth.getdata();
					tv3.setText(initstartdate);
					pay_date = initstartdate.replace("-", "");
					if(DateUtil.gettosevenday(false).equals(birth.getdata())){
						tv3.setText("今日");
					}
					birth.dismiss();
					onPullDownToRefresh(lvData);
//					pageNo = 1;
//					loadData();
					break;
				case R.id.cancel:
					birth.dismiss();
					break;
				default:
					break;
				}
			}
		});
		birth.showAtLocation(activity.findViewById(R.id.root),
				Gravity.BOTTOM, 0, 0);
	}
	
	 private void showPopupWindow(View view,List<PaystatusListBean> listbean,final int tag) {
			final List<String> list = new ArrayList<String>();
			final List<String> list1 = new ArrayList<String>();
		 	if(tag==0){
		 		list.add("全部状态");
		 		list1.add("");
		 	}else if(tag==1){
		 		list.add("全部");
		 		list1.add("");
		 	}
		 	
		 	for (int i = 0; i < listbean.size(); i++) {
		 		list.add(listbean.get(i).name);
		 		list1.add(listbean.get(i).id);
			}

	        // 一个自定义的布局，作为显示的内容
	        View contentView = LayoutInflater.from(activity).inflate(
	                R.layout.currency_pop, null);
	        // 设置按钮的点击事件
	        
			ListView listview = (ListView) contentView.findViewById(R.id.poplist);
			ArrayAdapter popadapter = new ArrayAdapter<String>(activity, R.layout.pop_name,list);
			listview.setAdapter(popadapter);
			

	        final PopupWindow popupWindow = new PopupWindow(contentView,
	        		getView(R.id.lllayout1s).getWidth(), LayoutParams.WRAP_CONTENT, true);
	        popupWindow.setTouchable(true);
	        
	        popupWindow.setTouchInterceptor(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
	        });
	        
	        ColorDrawable dw = new ColorDrawable(00000000);  
	        //设置SelectPicPopupWindow弹出窗体的背景  
	        popupWindow.setBackgroundDrawable(dw);  
//			WindowManager.LayoutParams lp = getWindow().getAttributes();
//			lp.alpha = 0.7f;
//			getWindow().setAttributes(lp);
	        // 设置好参数之后再show
//	        Log.e("", ""+(int)view2.getY());
//	        Log.e("", ""+view2.getHeight());
//	        Log.e("", ""+view2.getTop());
//	        Log.e("", ""+view2.getBottom());
	        popupWindow.showAtLocation(view,Gravity.NO_GRAVITY,view.getLeft(),view.getBottom()+statusBarHeight1);
//	        popupWindow.showAtLocation(view,Gravity.BOTTOM,(int) 100,0);
//	        popupWindow.showAsDropDown(view,Gravity.NO_GRAVITY,0,0);
	        listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					if(tag==0){
						if((int)arg3==0){
							tv1.setText("支付状态");
						}else{
							tv1.setText(list.get((int)arg3));
						}
						pay_statusid = list1.get((int)arg3);
						
					}else if(tag==1){
						discount_rateid =list1.get((int)arg3);
						if((int)arg3==0){
							tv2.setText("让利率");
						}else{
							tv2.setText(list.get((int)arg3));
						}
					}
					
					onPullDownToRefresh(lvData);
					popupWindow.dismiss();
				}
			});
	    }

}
