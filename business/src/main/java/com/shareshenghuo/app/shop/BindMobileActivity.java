package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import cn.jpush.android.api.JPushInterface;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.UserInfo;
import com.shareshenghuo.app.shop.network.request.BindMobileRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;
import com.shareshenghuo.app.shop.widget.CountDownButton;

public class BindMobileActivity extends BaseTopActivity implements OnClickListener {
	
	private EditText edMobile;
	private EditText edVCode;
	private CountDownButton btnVCode;
	private Button btnBind;
	
	private boolean back;	// true 直接返回	false 跳转新界面
	
	private UserInfo userInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_mobile);
		init();
	}
	
	public void init() {
		initTopBar("变更手机号");
		edMobile = getView(R.id.edMobiles);
		edVCode = getView(R.id.edVCode);
		btnVCode = getView(R.id.btnGetVCode);
		btnBind = getView(R.id.btnBind);
		
		btnVCode.setOnClickListener(this);
		btnBind.setOnClickListener(this);
		
		back = getIntent().getBooleanExtra("back", false);
		userInfo = (UserInfo) getIntent().getSerializableExtra("userInfo");
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
			bindMobile();
			break;
		}
	}
	
	public void bindMobile() {
		if(TextUtils.isEmpty(edMobile.getText()) || edMobile.getText().length()!=11) {
			ViewUtil.showEditError(edMobile, "请输入正确的手机号");
			return;
		}
		if(ViewUtil.checkEditEmpty(edVCode, "请输入验证码"))
			return;
		
		ProgressDialogUtil.showProgressDlg(this, "");
		final BindMobileRequest req = new BindMobileRequest();
		req.mobile = edMobile.getText().toString();
		req.msg_id = btnVCode.getVCodeId();
		req.msg_code = edVCode.getText().toString();
		req.shop_id = userInfo.shop_id;
		req.admin_id = userInfo.id+"";
		req.registration_id = JPushInterface.getRegistrationID(this);
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_BIND_MOBILE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(BindMobileActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(BindMobileActivity.this, "绑定成功");
					// 登录环信
					ProgressDialogUtil.showProgressDlg(BindMobileActivity.this, "登录中");
					EMChatManager.getInstance().login("s"+userInfo.shop_id, "123456", new EMCallBack() {
						@Override
						public void onSuccess() {
							ProgressDialogUtil.dismissProgressDlg();
							Log.e("", "环信login succeed");
							userInfo.band_mobile = req.mobile;
							EMChatManager.getInstance().updateCurrentUserNick(userInfo.nick_name);
							UserInfoManager.saveUserInfo(BindMobileActivity.this, userInfo);
							if(!back)
								startActivity(new Intent(BindMobileActivity.this, MainActivity.class));
							finish();
						}
						
						@Override
						public void onProgress(int arg0, String arg1) {
							ProgressDialogUtil.dismissProgressDlg();
							Log.e("", arg1);
						}
						
						@Override
						public void onError(int arg0, String arg1) {
							ProgressDialogUtil.dismissProgressDlg();
							Log.e("", "环信login error "+arg1);
						}
					});
				} else {
					T.showShort(BindMobileActivity.this, bean.result_desc);
				}
			}
		});
	}
}
