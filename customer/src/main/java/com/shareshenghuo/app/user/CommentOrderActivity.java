package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.AddPhotoGridAdapter;
import com.shareshenghuo.app.user.adapter.CommentProdAdapter;
import com.shareshenghuo.app.user.network.bean.OrderProdInfo;
import com.shareshenghuo.app.user.network.request.CommentOrderRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.TransferTempDataUtil;
import com.shareshenghuo.app.user.widget.MyGridView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;

public class CommentOrderActivity extends BaseTopActivity {
	
	private ListView lvProd;
	
	private CommentProdAdapter adapter;
	private AddPhotoGridAdapter photoAdapter;
	
	private List<OrderProdInfo> prodList;
	private int orderId;
	private int shopId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_order);
		getData();
		initView();
	}
	
	public void getData() {
		prodList = (List<OrderProdInfo>) TransferTempDataUtil.getInstance().getData();
		TransferTempDataUtil.getInstance().recycle();
		orderId = getIntent().getIntExtra("orderId", 0);
		shopId = getIntent().getIntExtra("shopId", 0);
	}
	
	public void initView() {
		initTopBar("评价晒单");
		
		lvProd = getView(R.id.lvCommentProd);
		adapter = new CommentProdAdapter(this, prodList);
		lvProd.setAdapter(adapter);
		
		getView(R.id.btnSubmitComment).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				submit();
			}
		});
	}
	
	public void submit() {
		ProgressDialogUtil.showProgressDlg(this, "发表中");
		CommentOrderRequest req = new CommentOrderRequest();
		req.order_id = orderId+"";
		req.shop_id = shopId+"";
		for(int i=0; i<adapter.getCount(); i++) {
			RatingBar rtScore = (RatingBar) adapter.itemView[i].findViewById(R.id.rtProdScore);
			EditText edContent = (EditText) adapter.itemView[i].findViewById(R.id.edProdContent);
			MyGridView gvPhoto = (MyGridView) adapter.itemView[i].findViewById(R.id.gvProdShow);
			AddPhotoGridAdapter photoAdapter = (AddPhotoGridAdapter) gvPhoto.getAdapter();
			req.addComment(adapter.getItem(i).product_id, rtScore.getProgress(), edContent.getText().toString(), photoAdapter.getPhotoUrls());
		}
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_COMMENT_ORDER, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(CommentOrderActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(CommentOrderActivity.this, "评论成功");
					setResult(RESULT_OK);
					finish();
				} else {
					T.showShort(CommentOrderActivity.this, bean.result_desc);
				}
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(adapter.photoAdapter != null)
			adapter.photoAdapter.onActivityResult(requestCode, resultCode, data);
	}
}
