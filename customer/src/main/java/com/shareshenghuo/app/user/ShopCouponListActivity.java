package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.CouponListAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.request.ReceiveCardRequest;
import com.shareshenghuo.app.user.network.response.CouponListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;

import android.os.Bundle;
import android.widget.ListView;

public class ShopCouponListActivity extends BaseTopActivity {
	
	private ListView lvData;
	
	private int shopId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_coupon_list);
		initView();
		loadData();
	}
	
	public void initView() {
		initTopBar("优惠券");
		lvData = getView(R.id.lvCoupon);
		
		shopId = getIntent().getIntExtra("shopId", 0);
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		ReceiveCardRequest req = new ReceiveCardRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.shop_id = shopId+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_SHOP_COUPON, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ShopCouponListActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				CouponListResponse bean = new Gson().fromJson(resp.result, CouponListResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					lvData.setAdapter(new CouponListAdapter(ShopCouponListActivity.this, bean.data));
				}
			}
		});
	}
}
