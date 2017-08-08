package com.shareshenghuo.app.shop.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

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
import com.shareshenghuo.app.shop.ExchangeListInfoAcyivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.ExchangeListAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.ExchangeListBean;
import com.shareshenghuo.app.shop.network.request.ExchangeListRequest;
import com.shareshenghuo.app.shop.network.response.ExchangeListResponse;
import com.shareshenghuo.app.shop.networkapi.Api;

public class ExchangListFragment2 extends BaseFragment implements OnRefreshListener2<ListView>{
	
	private PullToRefreshListView lvData;
	private int pageNo = 1;
	private int pageSize = 10;
	private ExchangeListAdapter adapter;
	private List<ExchangeListBean> list = new ArrayList<ExchangeListBean>();
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.exchange_list;
	}

	@Override
	protected void init(View rootView) {
		// TODO Auto-generated method stubxl
		initView();
		loadData();
	}
	private void initView() {
//		initTopBar("兑换记录");
		// TODO Auto-generated method stub
		lvData = getView(R.id.lvShop);
		getView(R.id.rootview).setVisibility(View.GONE);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		
		lvData.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent it = new Intent(getActivity(),ExchangeListInfoAcyivity.class);
				if(list!=null&&list.get((int)arg3).id!=null){
					it.putExtra("id", list.get((int)arg3).id);
					startActivity(it);
				}
			}
		});

	}
	
	  
		public void loadData() {
			ExchangeListRequest req = new ExchangeListRequest();
			req.userShopId = UserInfoManager.getUserInfo(getActivity()).shop_id+"";
			req.userType = "2";
			req.wdSts = "";
			req.drawInfoType = "0";
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
					Log.e("", ""+resp.result);
					if(Api.SUCCEED == bean.result_code)
						if(bean.data!=null){
							updateView(bean.data);
						}
						
						if(bean.data!=null&&bean.data.size()>0){
							list.addAll(bean.data);
						}
				}
			});
			
			
		}
		
		public void updateView(List<ExchangeListBean> data) {

			if(pageNo==1 || adapter==null) {
				adapter = new ExchangeListAdapter(getActivity(), data);
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
