package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.SysMsgfeedbackAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.FeedBackListBean;
import com.shareshenghuo.app.user.network.bean.WebLoadActivity;
import com.shareshenghuo.app.user.network.request.SysMsgRequest;
import com.shareshenghuo.app.user.network.response.FeedBackListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.T;
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
		initTopBar("反馈回复");
		lvData = getView(R.id.lvSysMsg);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		btnTopRight1.setVisibility(View.GONE);
		btnTopRight1.setText("联系客服");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent about = new Intent(SysMsgFeedbackListActivity.this, WebLoadActivity.class);
				about.putExtra("title", "在线客服");
				about.putExtra("url", Api.CUSTOMERSERVICE);
				startActivity(about);
			}
		});
	}
	
	public void loadData() {
		SysMsgRequest req = new SysMsgRequest();
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.user_type = "1";
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
