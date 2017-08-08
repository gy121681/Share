package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import cn.jpush.android.api.JPushInterface;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.request.RegistRequest;
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

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 * 第三方登录绑定手机号
 */
public class BindActivity extends BaseTopActivity implements OnClickListener {
	
	private EditText edMobile;
	private EditText edVCode;
	private EditText edReferral;
	private CountDownButton btnVCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind);
		init();
	}
	
	public void init() {
		initTopBar("绑定手机号");
		edMobile = getView(R.id.edMobile);
		edVCode = getView(R.id.edVCode);
		edReferral = getView(R.id.edReferralCode);
		btnVCode = getView(R.id.btnGetVCode);
		
		btnVCode.setOnClickListener(this);
		findViewById(R.id.btnBind).setOnClickListener(this);
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
			if(TextUtils.isEmpty(edMobile.getText()) || edMobile.getText().length()!=11) {
				ViewUtil.showEditError(edMobile, "请输入正确的手机号");
				return;
			}
			btnVCode.getVCode(edMobile.getText().toString(), null);
			break;
			
		case R.id.btnBind:
			if(TextUtils.isEmpty(edMobile.getText()) || edMobile.getText().length()!=11) {
				ViewUtil.showEditError(edMobile, "请输入正确的手机号");
				return;
			}
			if(ViewUtil.checkEditEmpty(edVCode, "请输入验证码"))
				return;
			bind();
			break;
		}
	}
	
	public void bind() {
		ProgressDialogUtil.showProgressDlg(this, "绑定中");
		RegistRequest req = new RegistRequest();
		req.latitude = CityManager.getInstance(this).latitude+"";
		req.longitude = CityManager.getInstance(this).longitude+"";
		req.account = edMobile.getText().toString();
		req.msg_id = btnVCode.getVCodeId();
		req.msg_code = edVCode.getText().toString();
		req.invitation_code = edReferral.getText().toString();
		req.registration_id = JPushInterface.getRegistrationID(this);
		req.user_id = UserInfoManager.getUserId(this)+"";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_REGISTER, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(BindActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(BindActivity.this, "绑定成功");
					setResult(RESULT_OK);
					finish();
				} else {
					T.showShort(BindActivity.this, bean.result_desc);
				}
			}
		});
	}
}
