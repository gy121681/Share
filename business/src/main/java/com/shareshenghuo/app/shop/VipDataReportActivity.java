package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.request.StatisticConsumeDetailRequest;
import com.shareshenghuo.app.shop.network.response.StatisticConsumeResponse;
import com.shareshenghuo.app.shop.network.response.StatisticConsumeResponse.StatisticConsume;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;

public class VipDataReportActivity extends BaseTopActivity implements OnCheckedChangeListener, OnClickListener {
	
	private TextView tvToday;
	private TextView tvWeek;
	private TextView tvMonth;
	private TextView tvTotal;
	
	private int type = 1;	//1用户量 2收藏量 0自定义

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vip_data_report);
		init();
	}
	
	public void init() {
		initTopBar("会员数据报表");
		tvToday = getView(R.id.tvTodayCount);
		tvWeek = getView(R.id.tvWeekCount);
		tvMonth = getView(R.id.tvMonthCount);
		tvTotal = getView(R.id.tvTotalCount);
		
		getView(R.id.layoutDatePicker).setOnClickListener(this);
		((RadioGroup) findViewById(R.id.rgVipData)).setOnCheckedChangeListener(this);
		
		loadData();
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int id) {
		switch(id) {
		case R.id.rbMemberCount:
			type = 1;
			loadData();
			break;
			
		case R.id.rbCollectCount:
			type = 2;
			loadData();
			break;
		}
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "");
		StatisticConsumeDetailRequest req = new StatisticConsumeDetailRequest();
		req.shop_id = UserInfoManager.getUserInfo(this).shop_id+"";
		req.type = type+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_STATISTIC_VIP, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(VipDataReportActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				StatisticConsumeResponse bean = new Gson().fromJson(resp.result, StatisticConsumeResponse.class);
				if(Api.SUCCEED == bean.result_code && bean.data!=null) {
					StatisticConsume entity = bean.data;
					tvToday.setText((int)entity.today+"");
					tvWeek.setText((int)entity.week+"");
					tvMonth.setText((int)entity.month+"");
					tvTotal.setText((int)entity.all+"");
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.layoutDatePicker:
			startActivity(new Intent(this, VipDateFilterActivity.class));
			break;
		}
	}
}
