package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
import android.widget.ListView;

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
import com.shareshenghuo.app.user.network.request.MyCouponRequest;
import com.shareshenghuo.app.user.network.response.CouponListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;

/**
 * @author hang
 * 我的优惠券
 */
public class CouponListActivity extends BaseTopActivity {
	
	private ListView lvData;
	
	private int shopId;
	private double orderPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon_list);
		initView();
		loadData();
	}
	
	public void initView() {
		initTopBar("我的优惠券");
		lvData = (ListView) findViewById(R.id.lvCoupon);
		
		shopId = getIntent().getIntExtra("shopId", 0);
		orderPrice = getIntent().getDoubleExtra("orderPrice", 0);
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		MyCouponRequest req = new MyCouponRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		if(shopId > 0) {
			req.shop_id = shopId+"";
			req.order_price = orderPrice+"";
		}
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_MY_COUPON, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(CouponListActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				CouponListResponse bean = new Gson().fromJson(resp.result, CouponListResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					CouponListAdapter adapter = new CouponListAdapter(CouponListActivity.this, bean.data);
					if(shopId > 0)
						adapter.click = true;
					lvData.setAdapter(adapter);
				}
			}
		});
	}
}
