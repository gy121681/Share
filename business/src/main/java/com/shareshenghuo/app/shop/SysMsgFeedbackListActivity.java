package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
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
import com.shareshenghuo.app.shop.adapter.SysMsgfeedbackAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.FeedBackListBean;
import com.shareshenghuo.app.shop.network.request.SysMsgRequest;
import com.shareshenghuo.app.shop.network.response.FeedBackListResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.T;

public class SysMsgFeedbackListActivity extends BaseTopActivity implements OnRefreshListener2<ListView> {
	
	private PullToRefreshListView lvData;
	
	private int pageNo = 1;
	private int pageSize = 15;
	private SysMsgfeedbackAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sys_msg_list);
		init();
		loadData();
	}
	
	public void init() {
		initTopBar("反馈消息");
		lvData = getView(R.id.lvSysMsg);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
	}
	
	public void loadData() {
		SysMsgRequest req = new SysMsgRequest();
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		req.user_id = UserInfoManager.getUserInfo(this).shop_id;
		req.user_type = "2";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.FEEDBACKLIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
				T.showNetworkError(SysMsgFeedbackListActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				FeedBackListResponse bean = new Gson().fromJson(resp.result, FeedBackListResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					updateView(bean.data);
				} else {
					T.showShort(SysMsgFeedbackListActivity.this, bean.result_desc);
				}
			}
		});
	}
	
	public void updateView(List<FeedBackListBean> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new SysMsgfeedbackAdapter(this, data);
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
}
