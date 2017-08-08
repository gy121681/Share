package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.FlashSaleProdAdapter;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.network.bean.ProdInfo;
import com.shareshenghuo.app.user.network.request.BannerRequest;
import com.shareshenghuo.app.user.network.request.FlashSaleProdRequest;
import com.shareshenghuo.app.user.network.response.BannerListResponse;
import com.shareshenghuo.app.user.network.response.ProdListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.BannerViewPager;
import com.shareshenghuo.app.user.widget.CountDownTextView;

/**
 * @author hang
 * 限时抢购
 */
public class FlashSaleActivity extends BaseTopActivity implements OnRefreshListener2<ListView>, OnClickListener {
	
	private RelativeLayout rlBannerContainer;
	private BannerViewPager banner;
	private CountDownTextView tvTime;
	private PullToRefreshListView lvData;
	
	private int pageSize = 15;
	private int pageNo = 1;
	private FlashSaleProdAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash_sale);
		initView();
		loadData(pageNo, pageSize);
		getBannerList();
	}
	
	public void initView() {
		initTopBar("限时抢购");
		tvTime = getView(R.id.tvFlashSaleTime);
		rlBannerContainer = getView(R.id.rlBannerContainer);
		banner = getView(R.id.bannerShop);
		
		lvData = getView(R.id.lvFlashSale);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		
		getView(R.id.ivCloseBanner).setOnClickListener(this);
	}
	
	@Override
	public void onPause() {
		banner.stopRoll();
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		banner.startRoll();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		tvTime.stopCountDown();
	}
	
	public void getBannerList() {
		BannerRequest req = new BannerRequest();
		req.city_id = CityManager.getInstance(this).getCityId()+"";
		req.banner_place = "5";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_BANNER_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				if(resp.result!=null) {
					BannerListResponse bean = new Gson().fromJson(resp.result, BannerListResponse.class);
					if(bean.data!=null && bean.data.size()>0) {
						rlBannerContainer.setVisibility(View.VISIBLE);
						banner.createView(bean.data);
						banner.startRoll();
						banner.setRelativeLayoutWidthHeight(0.5);
					}
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}
		});
	}

	public void loadData(int pageNo, int pageSize) {
		FlashSaleProdRequest req = new FlashSaleProdRequest();
		req.city_id = CityManager.getInstance(this).getCityId()+"";
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_FLASH_SALE_PROD, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
				T.showNetworkError(FlashSaleActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				ProdListResponse bean = new Gson().fromJson(resp.result, ProdListResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					if(bean.data!=null && bean.data.size()>0)
						tvTime.startCountDown(bean.data.get(0).current_time);
					
					updateView(bean.data);
				} else {
					T.showShort(FlashSaleActivity.this, bean.result_desc);
				}
			}
		});
	}
	
	public synchronized void updateView(List<ProdInfo> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new FlashSaleProdAdapter(this, data);
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
		loadData(pageNo, pageSize);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData(pageNo, pageSize);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.ivCloseBanner:
			rlBannerContainer.setVisibility(View.GONE);
			banner.stopRoll();
			break;
		}
	}
}
