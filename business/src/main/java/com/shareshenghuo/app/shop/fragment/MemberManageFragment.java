package com.shareshenghuo.app.shop.fragment;

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
import com.shareshenghuo.app.shop.MessageActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.VipDetailActivity;
import com.shareshenghuo.app.shop.VipManageActivity;
import com.shareshenghuo.app.shop.adapter.VipManageAdapter;
import com.shareshenghuo.app.shop.manager.MessageManager;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.VipInfo;
import com.shareshenghuo.app.shop.network.request.VipListRequest;
import com.shareshenghuo.app.shop.network.response.VipListResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker.NewMessageCallback;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;
import com.shareshenghuo.app.shop.widget.dialog.QRCodeWindow;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MemberManageFragment extends BaseFragment
	implements OnItemClickListener, OnRefreshListener2<ListView>, OnClickListener, NewMessageCallback{
	
	private static final int REQ_VIP_DETAIL = 0x100;
	
	private ImageView ivIM;
	private EditText edKeyword;
	private ImageView ivSearch;
	private PullToRefreshListView lvData;
	
	private VipManageAdapter adapter;
	private int pageNo = 1;
	private int pageSize = 30;
	
	private NewChatMsgWorker newMsgWatcher;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_member_manage;
	}

	@Override
	protected void init(View rootView) {
		pageNo = 1;
		
		initView();
		loadData();
		
		newMsgWatcher = new NewChatMsgWorker(activity, this);
		newMsgWatcher.startWork();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		ivIM.setBackgroundResource(MessageManager.getUnreadCount() > 0 ? 
				R.drawable.ic_im_notice : R.drawable.ic_im);
	}
	
	public void initView() {
		ivIM = getView(R.id.ivHomeIM);
		edKeyword = getView(R.id.edSearch);
		ivSearch = getView(R.id.ivSearch);
		lvData = getView(R.id.lvData);
		
		ViewUtil.setEditWithClearButton(edKeyword, R.drawable.img_cancel);
		lvData.setMode(Mode.BOTH);

		ivIM.setOnClickListener(this);
		ivSearch.setOnClickListener(this);
		lvData.setOnRefreshListener(this);
		lvData.setOnItemClickListener(this);
	}
	
	public void loadData() {
		VipListRequest req = new VipListRequest();
		req.shop_id = UserInfoManager.getUserInfo(activity).shop_id+"";
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		String keyword = edKeyword.getText().toString();
		if(!TextUtils.isEmpty(keyword))
			req.search_name = keyword;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_VIP_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				VipListResponse bean = new Gson().fromJson(resp.result, VipListResponse.class);
				if(Api.SUCCEED == bean.result_code);
//					updateView(bean.data); //会员列表已修改
			}
		});
	}
	
//	public void updateView(List<VipInfo> data) {//会员列表已修改
//		if(pageNo==1 || adapter==null) {
//			adapter = new VipManageAdapter(activity, data);
//			lvData.setAdapter(adapter);
//		}
//		if(pageNo > 1) {
//			adapter.getmData().addAll(data);
//			adapter.notifyDataSetChanged();
//		}
//		pageNo++;
//	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long id) {
		VipInfo item = (VipInfo) adapterView.getItemAtPosition(position);
		Intent it = new Intent(activity, VipDetailActivity.class);
		it.putExtra("vipInfo", item);
		startActivityForResult(it, REQ_VIP_DETAIL);
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
		case R.id.ivHomeIM:
			ivIM.setBackgroundResource(R.drawable.ic_im);
			startActivity(new Intent(activity, MessageActivity.class));
			break;
			
		case R.id.ivSearch:
			if(ViewUtil.checkEditEmpty(edKeyword, "请输入关键字"))
				return;
			onPullDownToRefresh(lvData);
			break;
		}
	}

	@Override
	public void onActivityResult(int reqCode, int resCode, Intent data) {
		if(resCode==Activity.RESULT_OK && reqCode==REQ_VIP_DETAIL) {
			onPullDownToRefresh(lvData);
		}
	}
	
	@Override
	public void newMessage(int which) {
		ivIM.setBackgroundResource(R.drawable.ic_im_notice);		
	}
}
