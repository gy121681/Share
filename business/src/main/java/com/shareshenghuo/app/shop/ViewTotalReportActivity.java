package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.request.StatisticConsumeRequest;
import com.shareshenghuo.app.shop.network.response.StatisticConsumeResponse;
import com.shareshenghuo.app.shop.network.response.StatisticConsumeResponse.StatisticConsume;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;

/**
 * @author hang
 * 查看充值/消费总额
 */
public class ViewTotalReportActivity extends BaseTopActivity implements OnClickListener {
	
	private int from;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_total_report);
		initView();
		loadData();
	}
	
	public void initView() {
		from = getIntent().getIntExtra("from", FROM.CONSUME_TOTAL);
		
		switch(from) {
		case FROM.CONSUME_TOTAL:
			initTopBar("消费总额");
			break;
			
		case FROM.COUPON_USE:
			initTopBar("优惠券使用情况");
			break;
			
		case FROM.POINT_USE:
			initTopBar("积分消费");
			break;
			
		case FROM.RECHARGE_TOTAL:
			initTopBar("充值总额");
			break;
		}
		
		getView(R.id.llDateFilter).setOnClickListener(this);
		getView(R.id.llTodayData).setOnClickListener(this);
		getView(R.id.llWeekData).setOnClickListener(this);
		getView(R.id.llMonthData).setOnClickListener(this);
		getView(R.id.llTotalData).setOnClickListener(this);
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "");
		StatisticConsumeRequest req = new StatisticConsumeRequest();
		req.shop_id = UserInfoManager.getUserInfo(this).shop_id+"";
		if(from == FROM.CONSUME_TOTAL)
			req.type = "2";
		else if(from == FROM.POINT_USE)
			req.type = "1";
		else if(from == FROM.RECHARGE_TOTAL)
			req.type = "3";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String api = Api.URL_STATISTIC_CONSUME;
		if(from == FROM.COUPON_USE)
			api = Api.URL_STATISTIC_COUPON;
		new HttpUtils().send(HttpMethod.POST, api, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ViewTotalReportActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				StatisticConsumeResponse bean = new Gson().fromJson(resp.result, StatisticConsumeResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					updateView(bean.data);
				}
			}
		});
	}
	
	public void updateView(StatisticConsume data) {
		setText(R.id.tvTodayData, data.today+"");
		setText(R.id.tvWeekData, data.week+"");
		setText(R.id.tvMonthData, data.month+"");
		setText(R.id.tvTotalData, data.all+"");
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.llDateFilter:
			Intent it1 = new Intent(this, DateFilterReportActivity.class);
			it1.putExtra("from", from);
			startActivity(it1);
			break;
			
		case R.id.llTodayData:
			Intent it2 = new Intent(this, StatisticDayActivity.class);
			it2.putExtra("from", from);
			startActivity(it2);
			break;
			
		case R.id.llWeekData:
			Intent it3 = new Intent(this, StatisticWeekActivity.class);
			it3.putExtra("from", from);
			startActivity(it3);
			break;
			
		case R.id.llMonthData:
			Intent it4 = new Intent(this, StatisticMonthActivity.class);
			it4.putExtra("from", from);
			startActivity(it4);
			break;
			
		case R.id.llTotalData:
			Intent it5 =new Intent(this, StatisticTotalActivity.class);
			it5.putExtra("from", from);
			startActivity(it5);
			break;
		}
	}
	
	public static class FROM {
		public static final int POINT_USE = 1;
		public static final int CONSUME_TOTAL = 2;
		public static final int RECHARGE_TOTAL = 3;
		public static final int VIP_REPORT = 4;
		public static final int COUPON_USE = 5;
	}
	
	public static class QUERY_TYPE {
		public static String TODAY = "today";
		public static String YESTERDAY = "yesterday";
		public static String WEEK = "week";
		public static String MONTH = "month";
		public static String TOTAL = "all";
		public static String CUSTOM = "custom";	//自定义时间段筛选
	}
}
