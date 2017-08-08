package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.request.RetrievePwdRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.ViewUtil;
import com.shareshenghuo.app.user.widget.CountDownButton;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class VCodeActivity extends BaseTopActivity implements OnClickListener {
	
	private TextView tvMobile;
	private EditText edVCode;
	private CountDownButton btnVCode;
	private Button btnNext;

	private String mobile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vcode);
		init();
	}
	
	public void init() {
		initTopBar("安全校验");
		tvMobile = getView(R.id.tvMobile);
		edVCode = getView(R.id.edVCode);
		btnVCode = getView(R.id.btnGetVCode);
		btnNext = getView(R.id.btnNext);
		
		mobile = UserInfoManager.getUserInfo(this).mobile;
		tvMobile.setText(mobile);
		
		btnVCode.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		
		btnVCode.getVCode(mobile, null);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		btnVCode.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		btnVCode.onStop();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnGetVCode:
			btnVCode.getVCode(mobile, null);
			break;
			
		case R.id.btnNext:
			if(ViewUtil.checkEditEmpty(edVCode, "请输入验证码"))
				return;
			checkVCode();
			break;
		}
	}
	
	public void checkVCode() {
		ProgressDialogUtil.showProgressDlg(this, "验证中");
		RetrievePwdRequest req = new RetrievePwdRequest();
		req.account = mobile;
		req.msg_id = btnVCode.getVCodeId();
		req.msg_code = edVCode.getText().toString();
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_CHECK_VCODE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String msg) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(VCodeActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(VCodeActivity.this, "验证成功");
					startActivity(new Intent(VCodeActivity.this, ModifyPayPwdActivity.class));
					finish();
				} else {
					T.showShort(VCodeActivity.this, bean.result_desc);
				}
			}
		});
	}
}
