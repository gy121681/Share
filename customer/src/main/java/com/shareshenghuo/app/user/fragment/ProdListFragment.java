package com.shareshenghuo.app.user.fragment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.ProdGridAdapter;
import com.shareshenghuo.app.user.fragment.BaseFragment;
import com.shareshenghuo.app.user.manager.AppManager;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.network.bean.ProdInfo;
import com.shareshenghuo.app.user.network.request.ProdListRequest;
import com.shareshenghuo.app.user.network.response.ProdListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.widget.dialog.GuideShopClassifyWindow;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ProdListFragment extends BaseFragment implements OnRefreshListener2<GridView> {

	private PullToRefreshGridView gvData;
	
	private ProdGridAdapter adapter;
	
	public int product_type_id = -1;
	public String keyword;
	public int shop_id;
	
	private int pageNo = 1;
	private int pageSize = 10;
	
	public List<ProdInfo> cacheData;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_prod_list;
	}

	@Override
	protected void init(View rootView) {
		initView();
		onPullDownToRefresh(gvData);
	}
	
	public void initView() {
		gvData = (PullToRefreshGridView) rootView.findViewById(R.id.gvProd);
		gvData.setMode(Mode.BOTH);
		gvData.setOnRefreshListener(this);
	}
	
	public void loadData() {
		if(cacheData != null) {
			// 先使用缓存数据
			adapter = new ProdGridAdapter(activity, cacheData);
			gvData.setAdapter(adapter);
		}
		
		ProdListRequest req = new ProdListRequest();
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		req.shop_id = shop_id+"";
		req.city_id = CityManager.getInstance(activity).getCityId()+"";
		if(product_type_id != -1)
			req.product_type_id = product_type_id+"";
		if(!TextUtils.isEmpty(keyword))
			req.search_name = keyword;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_PROD_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				gvData.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				gvData.onRefreshComplete();
				ProdListResponse bean = new Gson().fromJson(resp.result, ProdListResponse.class);
				if(Api.SUCCEED == bean.result_code && activity != null)
					updateView(bean.data);
			}
		});
	}
	
	public void updateView(List<ProdInfo> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new ProdGridAdapter(activity, data);
			gvData.setAdapter(adapter);
			
			if(adapter.getCount() > 0) {
				getView(R.id.tvNoData).setVisibility(View.GONE);
				
				if(!AppManager.getAddCart()) {
					// 新手指引
					new GuideShopClassifyWindow(activity, AppManager.GUIDE_ADD_CART).show();
					AppManager.setAddCart();
				}
			}
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		cacheData = adapter.getmData();
		pageNo++;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		loadData();
	}
}
