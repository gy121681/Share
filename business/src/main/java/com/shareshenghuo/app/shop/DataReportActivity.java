package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.ViewTotalReportActivity.FROM;
import com.shareshenghuo.app.shop.adapter.DataReportAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.request.ShopRequest;
import com.shareshenghuo.app.shop.network.response.StatisticListResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author hang
 * 数据报表
 */
public class DataReportActivity extends BaseTopActivity implements OnClickListener {
	
	private ListView lvData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_report);
		initView();
		loadData();
	}
	
	public void initView() {
		initTopBar("数据报表");
		lvData = getView(R.id.lvData);
		findViewById(R.id.llViewRechargeTotal).setOnClickListener(this);
		findViewById(R.id.llViewConsumeTotal).setOnClickListener(this);
		findViewById(R.id.llViewVipData).setOnClickListener(this);
		findViewById(R.id.llViewPointLog).setOnClickListener(this);
		findViewById(R.id.llViewCouponUse).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.llViewRechargeTotal:
			Intent it1 = new Intent(this, ViewTotalReportActivity.class);
			it1.putExtra("from", FROM.RECHARGE_TOTAL);
			startActivity(it1);
			break;
			
		case R.id.llViewConsumeTotal:
			Intent it2 = new Intent(this, ViewTotalReportActivity.class);
			it2.putExtra("from", FROM.CONSUME_TOTAL);
			startActivity(it2);
			break;
			
		case R.id.llViewPointLog:
			Intent it3 = new Intent(this, ViewTotalReportActivity.class);
			it3.putExtra("from", FROM.POINT_USE);
			startActivity(it3);
			break;
			
		case R.id.llViewCouponUse:
			Intent it4 = new Intent(this, ViewTotalReportActivity.class);
			it4.putExtra("from", FROM.COUPON_USE);
			startActivity(it4);
			break;
			
		case R.id.llViewVipData:
			startActivity(new Intent(this, VipDataReportActivity.class));
			break;
		}
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "");
		ShopRequest req = new ShopRequest();
		req.shop_id = UserInfoManager.getUserInfo(this).shop_id+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_STATISTIC_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(DataReportActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				StatisticListResponse bean = new Gson().fromJson(resp.result, StatisticListResponse.class);
				if(Api.SUCCEED == bean.result_code)
					lvData.setAdapter(new DataReportAdapter(DataReportActivity.this, bean.data));
			}
		});
	}
}
