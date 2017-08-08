package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.OrderStatusAdapter;
import com.shareshenghuo.app.user.network.request.UpdateOrderRequest;
import com.shareshenghuo.app.user.network.response.OrderStatusResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.os.Bundle;
import android.widget.ListView;

/**
 * @author hang
 * 订单状态列表
 */
public class OrderStatusActivity extends BaseTopActivity {
	
	private ListView lvData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_status);
		init();
		loadData();
	}
	
	public void init() {
		initTopBar("订单状态");
		lvData = getView(R.id.lvOrderStatus);
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		UpdateOrderRequest req = new UpdateOrderRequest();
		req.order_id = getIntent().getIntExtra("orderId", 0)+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_ORDER_STATUS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(OrderStatusActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				OrderStatusResponse bean = new Gson().fromJson(resp.result, OrderStatusResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					lvData.setAdapter(new OrderStatusAdapter(OrderStatusActivity.this, bean.data));
				} else {
					T.showShort(OrderStatusActivity.this, bean.result_desc);
				}
			}
		});
	}
}
