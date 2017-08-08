package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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
import com.shareshenghuo.app.shop.adapter.ExchangeListAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.ExchangeListBean;
import com.shareshenghuo.app.shop.network.request.ExchangeListRequest;
import com.shareshenghuo.app.shop.network.response.ExchangeListResponse;
import com.shareshenghuo.app.shop.networkapi.Api;

public class ExchangeListAcyivity extends BaseTopActivity implements OnRefreshListener2<ListView>{

	private PullToRefreshListView lvData;
	private int pageNo = 1;
	private int pageSize = 10;
	private ExchangeListAdapter adapter;
	private List<ExchangeListBean> list = new ArrayList<ExchangeListBean>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exchange_list);
		initView();
		loadData();
	}

	private void initView() {
		initTopBar("兑换记录");
		// TODO Auto-generated method stub
		lvData = getView(R.id.lvShop);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		
		lvData.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent it = new Intent(ExchangeListAcyivity.this,ExchangeListInfoAcyivity.class);
				if(list!=null&&list.get((int)arg3).id!=null){
					it.putExtra("id", list.get((int)arg3).id);
					startActivity(it);
				}
			}
		});

	}
	
	  
		public void loadData() {
			ExchangeListRequest req = new ExchangeListRequest();
			req.userShopId = UserInfoManager.getUserInfo(this).shop_id+"";
			req.userType = "2";
			req.wdSts = "";
			req.pageNo = pageNo+"";
			req.pageSize = pageSize+"";
			
			RequestParams params = new RequestParams();
			try {
				params.setBodyEntity(new StringEntity(req.toJson()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			new HttpUtils().send(HttpMethod.POST, Api.WITHDRAWLISTQUERY, params, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					lvData.onRefreshComplete();
				}

				@Override
				public void onSuccess(ResponseInfo<String> resp) {
					lvData.onRefreshComplete();
					ExchangeListResponse bean = new Gson().fromJson(resp.result, ExchangeListResponse.class);
					if(Api.SUCCEED == bean.result_code)
						updateView(bean.data);
						
						if(bean.data.size()>0){
							list.addAll(bean.data);
						}
				}
			});
			
			
		}
		
		public void updateView(List<ExchangeListBean> data) {

			if(pageNo==1 || adapter==null) {
				adapter = new ExchangeListAdapter(ExchangeListAcyivity.this, data);
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
		// TODO Auto-generated method stub
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		loadData();
	}
}