package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;

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
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.OrderListAdapter;
import com.shareshenghuo.app.shop.adapter.PushLogAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.OrderInfo;
import com.shareshenghuo.app.shop.network.bean.PushLogInfo;
import com.shareshenghuo.app.shop.network.request.PushLogRequest;
import com.shareshenghuo.app.shop.network.response.PushLogResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;

public class PushLogActivity extends BaseTopActivity implements OnRefreshListener2<ListView>, OnClickListener {
	
	private EditText edKeyword;
	private PullToRefreshListView lvData;
	
	private PushLogAdapter adapter;
	private int pageNo = 1;
	private int pageSize = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_push_log);
		init();
	}
	
	public void init() {
		initTopBar("推送记录");
		edKeyword = getView(R.id.edSearch);
		lvData = getView(R.id.lvData);
		
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		
		getView(R.id.ivSearch).setOnClickListener(this);
		
		loadData();
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "");
		PushLogRequest req = new PushLogRequest();
		req.shop_id = UserInfoManager.getUserInfo(this).shop_id+"";
		req.search_title = edKeyword.getText().toString();
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_PUSH_LOG, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				lvData.onRefreshComplete();
				T.showNetworkError(PushLogActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				lvData.onRefreshComplete();
				PushLogResponse bean = new Gson().fromJson(resp.result, PushLogResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					updateView(bean.data);
				} else {
					T.showShort(PushLogActivity.this, bean.result_desc);
				}
			}
		});
	}
	
	public void updateView(List<PushLogInfo> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new PushLogAdapter(this, data);
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
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.ivSearch:
			onPullDownToRefresh(lvData);
			break;
		}
	}
}
