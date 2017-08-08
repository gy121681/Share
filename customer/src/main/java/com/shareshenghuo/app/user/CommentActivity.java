package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.AddPhotoGridAdapter;
import com.shareshenghuo.app.user.entity.PhotoBean;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.request.SubmitCommentRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.ViewUtil;

public class CommentActivity extends BaseTopActivity {
	
	private EditText edContent;
	private GridView gvPhoto;
	
	private AddPhotoGridAdapter adapter;
	
	private int shopId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		initView();
	}
	
	public void initView() {
		initTopBar("写评论");
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("提交");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				submit();
			}
		});
		
		shopId = getIntent().getIntExtra("shopId", 0);
		
		edContent = getView(R.id.edCommentContent);
		gvPhoto = getView(R.id.gvCommentPhoto);
		
		List<PhotoBean> data = new ArrayList<PhotoBean>();
		data.add(new PhotoBean());
		adapter = new AddPhotoGridAdapter(this, data, gvPhoto);
		gvPhoto.setAdapter(adapter);
	}
	
	public void submit() {
		if(ViewUtil.checkEditEmpty(edContent, "请输入内容"))
			return;
		
		ProgressDialogUtil.showProgressDlg(this, "提交中");
		SubmitCommentRequest req = new SubmitCommentRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.shop_id = shopId+"";
		req.content = edContent.getText().toString();
		req.comment_photo = adapter.getPhotoUrls();
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_SUBMIT_COMMENT, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(CommentActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(CommentActivity.this, "评论成功");
					setResult(RESULT_OK);
					finish();
				} else {
					T.showShort(CommentActivity.this, bean.result_desc);
				}
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		adapter.onActivityResult(requestCode, resultCode, data);
	}
}
