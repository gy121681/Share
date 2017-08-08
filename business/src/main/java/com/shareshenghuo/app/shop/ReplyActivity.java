package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.request.AddCommentRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;

public class ReplyActivity extends BaseTopActivity {
	
	private EditText edContent;
	
	private int life_circle_id;
	private int reply_id;
	private String reply_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reply);
		init();
	}
	
	public void init() {
		life_circle_id = getIntent().getIntExtra("life_circle_id", 0);
		reply_id = getIntent().getIntExtra("reply_id", 0);
		reply_name = getIntent().getStringExtra("reply_name");
		
		initTopBar("回复评论");
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("提交");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				commit();
			}
		});
		
		edContent = getView(R.id.edReplyContent);
		edContent.setHint("回复"+reply_name);
	}
	
	public void commit() {
		if(ViewUtil.checkEditEmpty(edContent, "请输入回复内容"))
			return;
		
		ProgressDialogUtil.showProgressDlg(this, "提交中");
		AddCommentRequest req = new AddCommentRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.life_circle_id = life_circle_id+"";
		req.content = edContent.getText().toString();
		req.is_reply = "1";
		req.reply_name = reply_name;
		req.reply_id = reply_id+"";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_ADD_COMMENT, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ReplyActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(ReplyActivity.this, "回复成功");
					finish();
				} else {
					T.showShort(ReplyActivity.this, bean.result_desc);
				}
			}
		});
	}
}
