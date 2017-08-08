package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

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
import com.shareshenghuo.app.shop.adapter.BusinessAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.BusinessBeans;
import com.shareshenghuo.app.shop.network.bean.HistoryDataBeans;
import com.shareshenghuo.app.shop.network.request.BusinesstodayRequest;
import com.shareshenghuo.app.shop.network.response.BusinesstodayResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.util.Util;

public class TodaydataActivity extends BaseTopActivity implements  OnClickListener,OnRefreshListener2<ListView>{
	private int pageNo = 1;
	private int pageSize = 10;
	private RadioButton btn_1,btn_2;
	private BusinessAdapter adapter;
	private PullToRefreshListView lvData;
	private TextView tv_totalbusiness;
	private String date = "";
	private String dates = "";
	private String tag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_turnover);
		date = getIntent().getStringExtra("dates");
		tag = getIntent().getStringExtra("tag");
		init();
		loadData();
	}

	protected void init() {
		// TODO Auto-generated method stub
		initTopBar("日营业额");
		tv_totalbusiness = getView(R.id.tv_totalbusiness);
		tv_totalbusiness.setText("营业额:");
		lvData = getView(R.id.lvdata);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		btn_1 = getView(R.id.btn_1);
		btn_1.setChecked(true);
		btn_1.setTextColor(getResources().getColor(R.color.black));
		btn_2 = getView(R.id.btn_2);
		btn_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pageNo =1;
				btn_1.setTextColor(getResources().getColor(R.color.black));
				btn_2.setTextColor(getResources().getColor(R.color.black));
			}
		});
		btn_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pageNo =1;
				btn_2.setTextColor(getResources().getColor(R.color.black));
				btn_1.setTextColor(getResources().getColor(R.color.black));
			}
		});
		
	}
	
	
	public void loadData() {
//		List<BusinessBean> bean = new ArrayList<BusinessBean>();
//		for (int i = 0; i < 10; i++) {
//			BusinessBean data = new BusinessBean();
//			data.amont = "+20000";
//			data.time = "16:22:45";
//			data.oder = "12223665488";
//			data.name  = "哈哈哈";
//			bean.add(data);
//		}
//		updateView(bean);
		String url = Api.FINDSHOPPAYCLSINFLIST;
		if(tag!=null&&tag.equals("1")){
			 url = Api.FINDSHOPPAYCLSINFLISTNEW;
		}else{
			url = Api.FINDSHOPPAYCLSINFLIST;
		}
		
		BusinesstodayRequest req = new BusinesstodayRequest();
		req.shopId = UserInfoManager.getUserInfo(this).shop_id+"";
		req.payDate = date.replace("-", "");
		req.pageNo = pageNo+"";
		req.pageSize = pageSize+"";
		RequestParams params = new RequestParams("utf-8");
//		Log.e("", ""+req);
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
//				T.showNetworkError(getApplicationContext());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				BusinesstodayResponse bean = new Gson().fromJson(resp.result, BusinesstodayResponse.class);
				if(Api.SUCCEED == bean.result_code){
					updateView(bean.data.list);
				tv_totalbusiness.setText(DateUtil.getStrTime(date)+"营业额:"+Util.getnum(bean.data.dayShopSumMoney, true));
				}
			}
		});
		
	}
	
	
	public void updateView(List<BusinessBeans> data) {
		if(data!=null&&data.size()>0){
			if(pageNo==1 || adapter==null) {
				adapter = new BusinessAdapter(this, data);
				lvData.setAdapter(adapter);
			}
			if(pageNo > 1) {
				adapter.getmData().addAll(data);
				adapter.notifyDataSetChanged();
			}
			pageNo++;
		}
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

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
	
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		onPullDownToRefresh(lvData);
//	}

}
