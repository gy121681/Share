package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.shareshenghuo.app.shop.adapter.VipManageAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.VipInfo;
import com.shareshenghuo.app.shop.network.request.VipListRequest;
import com.shareshenghuo.app.shop.network.response.VipListResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;

/**
 * @author hang
 * 会员管理
 */
public class VipManageActivity extends BaseTopActivity
	implements OnItemClickListener, OnRefreshListener2<ListView>, OnClickListener {
	
	private static final int REQ_VIP_DETAIL = 0x100;
	
	private EditText edKeyword;
	private ImageView ivSearch;
	private PullToRefreshListView lvData;
	
	private VipManageAdapter adapter;
	private int pageNo = 1;
	private int pageSize = 30;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vip_manage);
		initView();
		loadData();
	}
	
	public void initView() {
		initTopBar("会员管理");
		btnTopRight2.setVisibility(View.VISIBLE);
		btnTopRight2.setBackgroundResource(R.drawable.ic_im);
		
		edKeyword = getView(R.id.edSearch);
		ivSearch = getView(R.id.ivSearch);
		lvData = getView(R.id.lvData);
		
		edKeyword.setText(getIntent().getStringExtra("keyword"));
		ViewUtil.setEditWithClearButton(edKeyword, R.drawable.img_cancel);
		lvData.setMode(Mode.BOTH);
		
		ivSearch.setOnClickListener(this);
		lvData.setOnRefreshListener(this);
		lvData.setOnItemClickListener(this);
	}
	
	public void loadData() {
		VipListRequest req = new VipListRequest();
		req.shop_id = UserInfoManager.getUserInfo(this).shop_id+"";
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
				T.showNetworkError(VipManageActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				VipListResponse bean = new Gson().fromJson(resp.result, VipListResponse.class);
				if(Api.SUCCEED == bean.result_code);
//					updateView(bean.data);//会员列表已修改
			}
		});
	}
	
//	public void updateView(List<VipInfo> data) {//会员列表已修改
//		if(pageNo==1 || adapter==null) {
//			adapter = new VipManageAdapter(this, data);
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
		Intent it = new Intent(this, VipDetailActivity.class);
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
		case R.id.ivSearch:
			if(ViewUtil.checkEditEmpty(edKeyword, "请输入关键字"))
				return;
			onPullDownToRefresh(lvData);
			break;
		}
	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		if(resCode==RESULT_OK && reqCode==REQ_VIP_DETAIL) {
			onPullDownToRefresh(lvData);
		}
	}
}
