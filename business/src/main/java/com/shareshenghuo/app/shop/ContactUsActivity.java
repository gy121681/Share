package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.google.android.gms.internal.ed;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.request.FeedbackRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.Utility;
import com.shareshenghuo.app.shop.util.ViewUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ContactUsActivity extends BaseTopActivity implements OnClickListener {
	
	private String tel = "02888265063";
	private String qq = "2694425802";
	
	private EditText edContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_us);
		init();
	}
	
	public void init() {
		initTopBar("意见反馈");
		edContent = getView(R.id.edFeedbackContent);
		
		setText(R.id.tvContactTel, tel);
		setText(R.id.tvContactQQ, qq);
		
		getView(R.id.llContactTel).setOnClickListener(this);
		getView(R.id.llContactQQ).setOnClickListener(this);
		getView(R.id.btnFeedbackCommit).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.llContactTel:
			Utility.call(this, tel);
			break;
			
		case R.id.llContactQQ:
			Utility.openQQChat(this, qq);
			break;
			
		case R.id.btnFeedbackCommit:
			if(ViewUtil.checkEditEmpty(edContent, "请输入内容"))
				return;
			commit();
			break;
		}
	}
	
	public void commit() {
		ProgressDialogUtil.showProgressDlg(this, "提交中");
		FeedbackRequest req = new FeedbackRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.content = edContent.getText().toString();
		req.mobile = UserInfoManager.getUserInfo(this).band_mobile;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_FEEDBACK, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ContactUsActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(ContactUsActivity.this, "提交成功");
					edContent.setText("");
				} else {
					T.showShort(ContactUsActivity.this, bean.result_desc);
				}
			}
		});
	}
}
