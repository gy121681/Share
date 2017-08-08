package com.td.qianhai.epay.oem;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.td.qianhai.epay.oem.adapter.BusinessAdapter;
import com.td.qianhai.epay.oem.adapter.TurnoverAdapter;
import com.td.qianhai.epay.oem.adapter.TurnoverAdapter1;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.BusinessBean;
import com.td.qianhai.epay.oem.beans.BusinessResponse;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.TurnoverBean;
import com.td.qianhai.epay.oem.beans.TurnoverRequest;
import com.td.qianhai.epay.oem.beans.TurnoverResponse;
import com.td.qianhai.epay.oem.mail.utils.NumUtil;
import com.td.qianhai.epay.oem.mail.utils.Utility;
import com.td.qianhai.epay.oem.views.PullToRefreshLayout;
import com.td.qianhai.epay.oem.views.PullableListView;
import com.td.qianhai.epay.oem.views.PullToRefreshLayout.OnRefreshListener;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShopNumActivity extends BaseActivity{
	
	private PullToRefreshLayout refresh_view;
	private BusinessAdapter adapter;
	private RelativeLayout ll_title;
	private ListView listview;
	private TextView tv_num,tv_province;
	public int pageNo = 1;
	private TextView tv_date;
	private String dd,area_type = "",area_code ="",trade = "";
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.business_item);
		area_type = getIntent().getStringExtra("area_type");
		area_code = getIntent().getStringExtra("area_code");
		trade = getIntent().getStringExtra("trade");
		
		initview();
		loadData();
	}

	private void initview() {
		// TODO Auto-generated method stub
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);
		ll_title.setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.tv_title_contre)).setText("商家数");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
			});

		listview = (ListView) findViewById(R.id.listview);
		tv_num = (TextView) findViewById(R.id.tv_num);
		tv_province = (TextView) findViewById(R.id.tv_province);
		
		refresh_view = (PullToRefreshLayout)findViewById(R.id.refresh_view);
		refresh_view.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				// TODO Auto-generated method stub
				pageNo = 1;
				loadData();
				
			}
			
			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				// TODO Auto-generated method stub
				refresh_view.loadmoreFinish(0);
			}
		});
		}
		public void loadData() {
		
		TurnoverRequest req = new TurnoverRequest();
//		req.pageNo = pageNo + "";
//		req.pageSize = pageSize + "";
		req.area_type = area_type;
		req.area_code = area_code;
//		if(((BusinessCenterActivity)getActivity()).area_type.equals("1")){
//			req.area_code = ((BusinessCenterActivity)getActivity()).province_id;
//		}else if(((BusinessCenterActivity)getActivity()).area_type.equals("2")){
//			req.area_code = ((BusinessCenterActivity)getActivity()).city_id;
//		}else if(((BusinessCenterActivity)getActivity()).area_type.equals("3")){
//			req.area_code = ((BusinessCenterActivity)getActivity()).area_id;
//		}
		Log.e("", ""+req.toString());
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.FINDAGENTSHOPNUM, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						refresh_view.refreshFinish(0);
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						Log.e("", ""+resp.result);
						refresh_view.refreshFinish(0);
						BusinessResponse bean = new Gson().fromJson(resp.result, BusinessResponse.class);
						if (0 == bean.result_code){
							tv_province.setText(trade+"商家分布");
							tv_num.setText(bean.data.shop_total_num);
							updateView(bean.data.list);
							
						}
					}
				});

	}

	public void updateView(List<BusinessBean> data) {
		if (pageNo == 1 || adapter == null) {
			adapter = new BusinessAdapter(this, data);
			listview.setAdapter(adapter);
		}
		if (pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
		
		Utility.setListViewHeightBasedOnChildren(listview);
	}

}
