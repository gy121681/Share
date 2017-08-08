package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.apache.http.entity.StringEntity;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
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
import com.shareshenghuo.app.shop.network.response.StatisticVipFilterResponse;
import com.shareshenghuo.app.shop.network.response.StatisticVipFilterResponse.StatisticVipFilter;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;

public class VipDateFilterActivity extends BaseTopActivity implements OnClickListener {
	
	private TextView tvStart, tvEnd;
	private TextView tvMemberCount, tvCollectCount, tvTotalMemberCount, tvTotalCollectCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vip_date_filter);
		init();
	}
	
	public void init() {
		initTopBar("自定义筛选");
		tvStart = getView(R.id.tvDateStart);
		tvEnd = getView(R.id.tvDateEnd);
		tvMemberCount = getView(R.id.tvMemberCount);
		tvCollectCount = getView(R.id.tvCollectCount);
		tvTotalMemberCount = getView(R.id.tvTotalMemberCount);
		tvTotalCollectCount = getView(R.id.tvTotalCollectCount);
		
		tvStart.setOnClickListener(this);
		tvEnd.setOnClickListener(this);
		getView(R.id.btnOK).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.tvDateStart:
			showDatePicker(tvStart);
			break;
			
		case R.id.tvDateEnd:
			showDatePicker(tvEnd);
			break;
		
		case R.id.btnOK:
			String start = tvStart.getText().toString();
			String end = tvEnd.getText().toString();
			if(TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
				T.showShort(this, "请选择日期");
				return;
			}
			loadData(start, end);
			break;
		}
	}
	
	public void showDatePicker(final TextView tvDate) {
		Calendar c = Calendar.getInstance();
		new DatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker arg0, int year, int month, int day) {
				tvDate.setText(year+"-"+(month+1)+"-"+day);
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
	}
	
	public void loadData(String startDate, String endDate) {
		ProgressDialogUtil.showProgressDlg(this, "");
		StatisticConsumeDetailRequest req = new StatisticConsumeDetailRequest();
		req.shop_id = UserInfoManager.getUserInfo(this).shop_id+"";
		req.type = "0";
		req.start_time = startDate;
		req.end_time = endDate;
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
				T.showNetworkError(VipDateFilterActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				StatisticVipFilterResponse bean = new Gson().fromJson(resp.result, StatisticVipFilterResponse.class);
				if(Api.SUCCEED == bean.result_code && bean.data!=null) {
					StatisticVipFilter entity = bean.data;
					tvMemberCount.setText(entity.user_count+"");
					tvTotalMemberCount.setText(entity.user_all_count+"");
					tvCollectCount.setText(entity.collect_count+"");
					tvTotalCollectCount.setText(entity.collect_all_count+"");
				} else {
					T.showShort(VipDateFilterActivity.this, bean.result_desc);
				}
			}
		});
	}
}
