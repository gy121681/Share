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
import com.td.qianhai.epay.oem.adapter.TurnoverAdapter;
import com.td.qianhai.epay.oem.adapter.TurnoverAdapter1;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.TurnoverBean;
import com.td.qianhai.epay.oem.beans.TurnoverRequest;
import com.td.qianhai.epay.oem.beans.TurnoverResponse;
import com.td.qianhai.epay.oem.mail.utils.NumUtil;
import com.td.qianhai.epay.oem.views.PullToRefreshLayout;
import com.td.qianhai.epay.oem.views.PullableListView;
import com.td.qianhai.epay.oem.views.PullToRefreshLayout.OnRefreshListener;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class CotyledonActivity extends BaseActivity{
	
	private PullableListView listview;
	private PullToRefreshLayout refresh_view;
	private TurnoverAdapter1 adapter;
	private int pageNo = 1;
	private TextView tv_date;
	private int pageSize = 10;
	private String dd,area_type = "",area_code ="",trade_date = "";
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.cotyledon_activity);
		area_type = getIntent().getStringExtra("area_type");
		area_code = getIntent().getStringExtra("area_code");
		trade_date = getIntent().getStringExtra("trade_date");
		
		initview();
		loadData();
	}

	private void initview() {
		// TODO Auto-generated method stub
		((TextView) findViewById(R.id.tv_title_contre)).setText("交易额");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
			});
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_date.setText(NumUtil.getStrTime(trade_date));
		listview = (PullableListView) findViewById(R.id.listview);
		refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		refresh_view.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				// TODO Auto-generated method stub
				pageNo = 1;
				loadData();
				refresh_view.refreshFinish(0);
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
		req.area_code = area_code;
		req.area_type = area_type;
		req.trade_date = trade_date;;
		Log.e("", ""+req.toString());
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.VIEWAGENTTRADE, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						refresh_view.refreshFinish(0);
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						refresh_view.refreshFinish(0);
						Log.e("", " - - - -  " + resp.result);
						TurnoverResponse bean = new Gson().fromJson(
								resp.result, TurnoverResponse.class);
						if (0 == bean.result_code){
							updateView(bean.data);
						}
					}
				});

	}

	public void updateView(List<TurnoverBean> data) {
		if (pageNo == 1 || adapter == null) {
			adapter = new TurnoverAdapter1(this, data);
			listview.setAdapter(adapter);
		}
		if (pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}

}
