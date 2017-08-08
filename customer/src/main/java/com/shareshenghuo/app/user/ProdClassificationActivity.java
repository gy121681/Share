package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.ProdListAdapter;
import com.shareshenghuo.app.user.adapter.ProdTypeListAdapter;
import com.shareshenghuo.app.user.network.bean.ProdInfo;
import com.shareshenghuo.app.user.network.bean.ProdTypeInfo;
import com.shareshenghuo.app.user.network.request.ProdListRequest;
import com.shareshenghuo.app.user.network.request.ShopPhotoRequest;
import com.shareshenghuo.app.user.network.response.ProdListResponse;
import com.shareshenghuo.app.user.network.response.ProdTypeResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
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

/**
 * @author hang
 * 商品分类
 */
public class ProdClassificationActivity extends BaseTopActivity 
	implements OnItemClickListener, OnRefreshListener2<ListView>, OnClickListener {
	
	private ListView lvColumn;
	private PullToRefreshListView lvContent;
	
	private ProdTypeListAdapter columnAdapter;
	private ProdListAdapter contentAdapter;
	
	private int shopId = 0;;
	
	private int typeId;
	
	private int pageNo = 1;
	private int pageSize = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prod_classification);
		initView();
		loadProdType();
	}
	
	public void initView() {
		shopId = getIntent().getIntExtra("shopId", 0);
		
		initTopBar("商品");
		lvColumn = getView(R.id.lvColumn);
		lvContent = getView(R.id.lvContent);
		
		lvContent.setMode(Mode.BOTH);
		
		lvColumn.setOnItemClickListener(this);
//		lvContent.setOnRefreshListener(this);
	
		getView(R.id.btnOpenCart).setOnClickListener(this);
	}
	
	public void loadProdType() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		ShopPhotoRequest req = new ShopPhotoRequest();
		req.shopId = shopId+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.FINDALLGOODSTYPELIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ProdClassificationActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				ProdTypeResponse bean = new Gson().fromJson(resp.result, ProdTypeResponse.class);
				
				if(Api.SUCCEED==bean.result_code && bean.data!=null && bean.data.size()>0) {
					updateColumnView(bean.data);
				} else {
					T.showShort(ProdClassificationActivity.this, "商家暂无任何商品");
				}
			}
		});
	}
	
	public void updateColumnView(List<ProdTypeInfo> data) {
		columnAdapter = new ProdTypeListAdapter(this, data);
		columnAdapter.selectedIndex = 0;
		lvColumn.setAdapter(columnAdapter);
		
		typeId = data.get(0).id;
		pageNo = 1;
		
		loadContentData(typeId);
	}
	
	public void loadContentData(int typeId) {
		ProdListRequest req = new ProdListRequest();
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		req.shopId = shopId+"";
//		req.city_id = CityManager.getInstance(this).getCityId()+"";
		req.goodsTypeId = typeId+"";
		Log.e("", " = = = "+req.toString());
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.FINDGOODSLISTFORGOODTYPE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvContent.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvContent.onRefreshComplete();
				ProdListResponse bean = new Gson().fromJson(resp.result, ProdListResponse.class);
				Log.e("", ""+resp.result);
				if(Api.SUCCEED == bean.result_code)
					updateContentView(bean.data);
			}
		});
	}
	
	public void updateContentView(List<ProdInfo> data) {
		if(pageNo==1 || contentAdapter==null) {
			contentAdapter = new ProdListAdapter(this, data);
			lvContent.setAdapter(contentAdapter);
		}
		if(pageNo > 1) {
			contentAdapter.getmData().addAll(data);
			contentAdapter.notifyDataSetChanged();
		}
		pageNo++;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNo = 1;
		loadContentData(typeId);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		lvContent.onRefreshComplete();
//		loadContentData(typeId);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		columnAdapter.selectedIndex = position;
		columnAdapter.notifyDataSetChanged();
		
		ProdTypeInfo item = (ProdTypeInfo) parent.getItemAtPosition(position);
		pageNo = 1;
		typeId = item.id;
		Log.e("", " - - - - "+item.id);
		loadContentData(typeId);
		
		if(contentAdapter != null) {
			contentAdapter.getmData().clear();
			contentAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnOpenCart:
			startActivity(new Intent(this, CartActivity.class));
			break;
		}
	}
}
