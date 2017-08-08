package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.adapter.IncentivePoints1Adapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.FilialobeBean;
import com.shareshenghuo.app.shop.network.request.FilialobeRequest;
import com.shareshenghuo.app.shop.network.response.FilialobeResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.Util;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author wyp
 * @version 创建时间：2017-6-6 上午9:36:24 类说明：待兑换秀点的界面,产业链和秀儿公用
 */
public class ConsumptionXFActivity extends BaseTopActivity implements
		OnRefreshListener2<ListView> {

	private int pageNo = 1;
	private int pageSize = 10;
	private IncentivePoints1Adapter adapter;
	private PullToRefreshListView lvData;
	private TextView tv_cnum;
	private String reconsumption;
	private String tag;
	private TextView tvTopTitle;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.incentivepoints_fragment);
		reconsumption = getIntent().getStringExtra("num");
		tag = getIntent().getStringExtra("tag");
		initview();
		loadData();

	}

	private void initview() {
		getView(R.id.lllayout).setVisibility(View.VISIBLE);
		tvTopTitle=getView(R.id.tvTopTitle);
		if (tag != null && tag.equals("1")) {
			tvTopTitle.setText("产业链待兑换秀点");
		} else {
			tvTopTitle.setText("秀儿待兑换秀点");
		}
		llTopBack=getView(R.id.llTopBack);
		llTopBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		getView(R.id.consumption_xf).setVisibility(View.VISIBLE);
		lvData = getView(R.id.lvShop);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		tv_cnum = getView(R.id.tv_cnum);
		if (reconsumption != null) {
			tv_cnum.setText(Util.getnum(reconsumption, false) + "");
		}
	}

	public void loadData() {
		String url = Api.OBEDIENCELIST;
		if (tag != null && tag.equals("1")) {
			url = Api.OBEDIENCELISTNEW;
		} else {
			url = Api.OBEDIENCELIST;
		}
		FilialobeRequest req = new FilialobeRequest();
		req.userId = UserInfoManager.getUserInfo(this).shop_id + "";
		req.userType = "2";
		req.operbType = "";
		req.moneyType = "1";
		req.opersType = "";
		req.startDate = "";
		req.endDate = "";
		req.pageNo = pageNo + "";
		req.pageSize = pageSize + "";

		Log.e("", "1 " + req.toString());
		Log.e("", "2 " + Api.OBEDIENCELIST);
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, url, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						lvData.onRefreshComplete();
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						Log.e("", "3 " + resp.result);
						lvData.onRefreshComplete();
						FilialobeResponse bean = new Gson().fromJson(
								resp.result, FilialobeResponse.class);
						if (Api.SUCCEED == bean.result_code)
							updateView(bean.data);
					}
				});

	}

	public void updateView(List<FilialobeBean> data) {
		if (pageNo == 1 || adapter == null) {
			adapter = new IncentivePoints1Adapter(this, data);
			lvData.setAdapter(adapter);
		}
		if (pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData();
	}

	@Override
	public void onResume() {
		super.onResume();
		// onPullDownToRefresh(lvData);
	}
}
