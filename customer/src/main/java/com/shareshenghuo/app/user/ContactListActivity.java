package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ScrollView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.ContactListAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.ContactInfo;
import com.shareshenghuo.app.user.network.request.ContactListRequest;
import com.shareshenghuo.app.user.network.response.ContactListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.Utility;
import com.shareshenghuo.app.user.widget.swipelistview.SwipeListView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ContactListActivity extends BaseTopActivity
	implements OnRefreshListener2<ScrollView>, OnClickListener {
	
	private EditText edKeyword;
	private PullToRefreshScrollView scrollView;
	private SwipeListView lvContact;
	
	private ContactListAdapter adapter;
	private int pageNo = 1;
	private int pageSize = 30;
	
	private String keyword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		init();
		loadData();
	}
	
	public void init() {
		initTopBar("联系人");
		edKeyword = getView(R.id.edContactKeyword);
		scrollView = getView(R.id.svContact);
		lvContact = getView(R.id.lvContact);
		
		scrollView.setMode(Mode.BOTH);
		scrollView.setOnRefreshListener(this);
		
		lvContact.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		
		getView(R.id.ivContactSearch).setOnClickListener(this);
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "");
		ContactListRequest req = new ContactListRequest();
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		req.user_type = "1";
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.search_name = keyword;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_CONTACT_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				scrollView.onRefreshComplete();
				T.showNetworkError(ContactListActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				scrollView.onRefreshComplete();
				ContactListResponse bean = new Gson().fromJson(resp.result, ContactListResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					updateView(bean.data);
				} else {
					T.showShort(ContactListActivity.this, bean.result_desc);
				}
			}
		});
	}
	
	public void updateView(List<ContactInfo> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new ContactListAdapter(this, data);
			lvContact.setAdapter(adapter);
			Utility.setListViewHeightBasedOnChildren(lvContact);
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		loadData();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.ivContactSearch:
			keyword = edKeyword.getText().toString();
			onPullDownToRefresh(scrollView);
			break;
		}
	}
}
