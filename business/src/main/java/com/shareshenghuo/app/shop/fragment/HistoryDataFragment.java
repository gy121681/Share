package com.shareshenghuo.app.shop.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.example.widget.MyOnclicListener;
import com.example.widget.SelectBirthday;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.TodaydataActivity;
import com.shareshenghuo.app.shop.adapter.HistoryrecordAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.HistoryDataBeans;
import com.shareshenghuo.app.shop.network.request.BusinesstodayRequest;
import com.shareshenghuo.app.shop.network.response.HistoryDataResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryDataFragment extends BaseFragment implements
		OnRefreshListener2<ListView>,DobusinessFragment.OnArticleSelectedListener {

	private int pageNo = 1;
	private int pageSize = 10;
	private RadioButton btn_1, btn_2;
	private HistoryrecordAdapter adapter;
	private PullToRefreshListView lvData;
	private TextView tv_totalbusiness,tv_time;
	private SelectBirthday birth;
	private String initstartdate = "";
	private List<HistoryDataBeans> mlist = new ArrayList<HistoryDataBeans>();
	public static HistoryDataFragment context;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.data_list_fragment;
	}
	
	@Override
	protected void init(View rootView) {
		// TODO Auto-generated method stub
		initstartdate = DateUtil.gettosevenday1();
		context = this;
		pageNo = 1;
		tv_totalbusiness = getView(R.id.tv_totalbusiness);
		tv_totalbusiness.setText("历史营业额:");
		lvData = getView(R.id.lvdata);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		btn_1 = getView(R.id.btn_1);
		tv_time = getView(R.id.tv_time);
		tv_time.setText(DateUtil.gettosevenday1()+"至今");
		btn_1.setChecked(true);
		btn_1.setTextColor(getResources().getColor(R.color.black));
		btn_2 = getView(R.id.btn_2);
		loadData();
		btn_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pageNo = 1;
				loadData();
				btn_1.setTextColor(getResources().getColor(R.color.black));
				btn_2.setTextColor(getResources().getColor(R.color.black));
			}
		});
		btn_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pageNo = 1;
				btn_2.setTextColor(getResources().getColor(R.color.black));
				btn_1.setTextColor(getResources().getColor(R.color.black));
			}
		});
		
		lvData.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent it = new Intent(getActivity(),TodaydataActivity.class);
//				Log.e("", ""+mlist.get(arg2-1).payDate);
//				Log.e("", ""+mlist.get(arg2).payDate);
//				Log.e("", ""+mlist.get((int)arg3).payDate);
				it.putExtra("dates", mlist.get((int)arg3).payDate+"");
				it.putExtra("tag", "1");
				startActivity(it);
			}
		});
		
	}
	
	
	public void setdate(){
//		if(getActivity()==null){
//			return;
//		}
		birth = new SelectBirthday(getActivity(),initstartdate,new MyOnclicListener() {

			public void MyOnclicListener(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.submit:
					initstartdate = birth.getdata();
					tv_time.setText(initstartdate+"至今");
//					Toast.makeText(getActivity().getApplicationContext(), initstartdate, Toast.LENGTH_SHORT).show();
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
		birth.showAtLocation(getActivity().findViewById(R.id.root),
				Gravity.BOTTOM, 0, 0);
	}

	public void loadData() {
		BusinesstodayRequest req = new BusinesstodayRequest();
		req.shopId = UserInfoManager.getUserInfo(getActivity()).shop_id+"";
		req.payDate = "";
		req.startDate = initstartdate.replace("-", "");
		req.endDate = DateUtil.getCDate();
		req.pageNo = pageNo+"";
		req.pageSize = pageSize+"";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.e("", ""+Api.FINDDAYPAYLIST);
		new HttpUtils().send(HttpMethod.POST, Api.FINDDAYPAYLISTNEW, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
//				T.showNetworkError(getApplicationContext());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				HistoryDataResponse bean = new Gson().fromJson(resp.result, HistoryDataResponse.class);
				if(Api.SUCCEED == bean.result_code){
				updateView(bean.data.list);
				
				tv_totalbusiness.setText("历史营业额: "+Util.getnum(bean.data.shopSumMoney, true));
				}
			}
		});

	}


	public void updateView(List<HistoryDataBeans> data) {
		
		
//		if(data!=null&&data.size()>0){
		if (pageNo == 1 || adapter == null) {
			adapter = new HistoryrecordAdapter(activity, data);
			lvData.setAdapter(adapter);
			mlist.clear();
			mlist.addAll(data);
		}
		if (pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
			mlist.addAll(data);
		}
		pageNo++;
//		}else{
//			
//		}
	}

	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	//
	// }

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

		loadData();
	}


	@Override
	public void onArticleSelected() {
		// TODO Auto-generated method stub
		
		setdate();
		
	}

//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		onPullDownToRefresh(lvData);
//	}

}
