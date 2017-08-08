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
import com.shareshenghuo.app.shop.ViewTotalReportActivity.QUERY_TYPE;
import com.shareshenghuo.app.shop.adapter.CouponWeekStatisticAdapter;
import com.shareshenghuo.app.shop.adapter.StatisticDetailsAdapter;
import com.shareshenghuo.app.shop.adapter.StatisticMonthAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.request.StatisticConsumeDetailRequest;
import com.shareshenghuo.app.shop.network.response.CouponWeekStatisticResponse;
import com.shareshenghuo.app.shop.network.response.StatisticDetailResponse;
import com.shareshenghuo.app.shop.network.response.StatisticMonthResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;

import android.os.Bundle;
import android.widget.ListView;

public class StatisticTotalActivity extends BaseTopActivity {
	
	private ListView lvData;
	
	private int from;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistic_total);
		init();
		loadData();
	}
	
	public void init() {
		from = getIntent().getIntExtra("from", FROM.CONSUME_TOTAL);
		
		initTopBar("累计明细");
		lvData = getView(R.id.lvData);
		
		if(from == FROM.COUPON_USE)
			setText(R.id.tvSubItem2, "兑换张数");
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "");
		StatisticConsumeDetailRequest req = new StatisticConsumeDetailRequest();
		req.shop_id = UserInfoManager.getUserInfo(this).shop_id+"";
		req.type = from+"";
		req.query_type = QUERY_TYPE.TOTAL;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String api = Api.URL_STATISTIC_CONSUME_DETAIL;
		if(from == FROM.COUPON_USE)
			api = Api.URL_STATISTIC_COUPON_DETAIL;
		new HttpUtils().send(HttpMethod.POST, api, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				if(from == FROM.COUPON_USE) {
					CouponWeekStatisticResponse bean = new Gson().fromJson(resp.result, CouponWeekStatisticResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						setText(R.id.tvDataTotal, bean.data.all_fee+"");
						lvData.setAdapter(new CouponWeekStatisticAdapter(StatisticTotalActivity.this, bean.data.result_list));
					} else {
						T.showShort(StatisticTotalActivity.this, bean.result_desc);
					}
				} else {
					StatisticMonthResponse bean = new Gson().fromJson(resp.result, StatisticMonthResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						setText(R.id.tvDataTotal, bean.data.all_fee+"");
						lvData.setAdapter(new StatisticMonthAdapter(StatisticTotalActivity.this, bean.data.result_list));
					} else {
						T.showShort(StatisticTotalActivity.this, bean.result_desc);
					}
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(StatisticTotalActivity.this);
			}
		});
	}
}
