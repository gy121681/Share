package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.CommentListAdapter;
import com.shareshenghuo.app.user.network.bean.CommentInfo;
import com.shareshenghuo.app.user.network.request.ProdCommentsRequest;
import com.shareshenghuo.app.user.network.request.ShopCommentRequest;
import com.shareshenghuo.app.user.network.response.ShopCommentResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.T;

/**
 * @author hang
 * 商家/商品评论列表
 */
public class CommentListActivity extends BaseTopActivity implements OnRefreshListener2<ListView> {
	
	private static final int REQ_SUBMIT_COMMENT = 0x101;
	
	private PullToRefreshListView lvData;
	
	private CommentListAdapter adapter;
	private int pageNo= 1;
	private int pageSize = 15;
	
	private int shopId = -1;
	private int prodId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_list);
		initView();
		loadData();
	}
	
	public void initView() {
		shopId = getIntent().getIntExtra("shopId", -1);
		prodId = getIntent().getIntExtra("prodId", -1);
		
		initTopBar("更多评论");
		if(shopId != -1) {
            btnTopRight1.setVisibility(View.VISIBLE);
            btnTopRight1.setText("评论");
            btnTopRight1.setBackgroundResource(0);
            btnTopRight1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent it = new Intent(CommentListActivity.this, CommentActivity.class);
					it.putExtra("shopId", shopId);
					startActivityForResult(it, REQ_SUBMIT_COMMENT);
				}
			});
		}
		
		lvData = getView(R.id.lvShopComments);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
	}
	
	public void loadData() {
		if(shopId != -1)
			loadShopComment();
		else
			loadProdComment();
	}
	
	/**
	 * 获取商家评论
	 */
	public void loadShopComment() {
		ShopCommentRequest req = new ShopCommentRequest();
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		req.shop_id = shopId+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_SHOP_COMMENTS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
				T.showNetworkError(CommentListActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				ShopCommentResponse bean = new Gson().fromJson(resp.result, ShopCommentResponse.class);
				if(Api.SUCCEED == bean.result_code)
					updateView(bean.data);
			}
		});
	}
	
	/**
	 * 获取商品评论
	 */
	public void loadProdComment() {
		ProdCommentsRequest req = new ProdCommentsRequest();
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		req.product_id = prodId+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_PROD_COMMENTS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
				T.showNetworkError(CommentListActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				ShopCommentResponse bean = new Gson().fromJson(resp.result, ShopCommentResponse.class);
				if(Api.SUCCEED == bean.result_code)
					updateView(bean.data);
			}
		});
	}
	
	public void updateView(List<CommentInfo> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new CommentListAdapter(this, data);
			lvData.setAdapter(adapter);
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		if(resultCode == RESULT_OK) {
			switch(requestCode) {
			case REQ_SUBMIT_COMMENT:
				onPullDownToRefresh(lvData);
				break;
			}
		}
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
