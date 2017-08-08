package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.network.request.UpdateUserInfoRequest;
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

/**
 * @author hang
 * 修改/绑定手机
 */
public class BindMobileActivity extends BaseTopActivity implements OnClickListener {
	
	private EditText edMobile;
	private EditText edVCode;
	private CountDownButton btnVCode;
	
	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_mobile);
		init();
	}
	
	public void init() {
		userInfo = UserInfoManager.getUserInfo(this);
		
		initTopBar("修改手机号");
		edMobile = getView(R.id.edMobile);
		edVCode = getView(R.id.edVCode);
		btnVCode = getView(R.id.btnGetVCode);
		
		btnVCode.setOnClickListener(this);
		findViewById(R.id.btnBind).setOnClickListener(this);
		
//		if(userInfo != null)
//			edMobile.setText(userInfo.mobile);
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
			
			modifyMobile(edMobile.getText().toString());
			break;
		}
	}
	
	/**
	 * 修改手机号
	 */
	public void modifyMobile(final String value) {
		ProgressDialogUtil.showProgressDlg(this, "");
		UpdateUserInfoRequest req = new UpdateUserInfoRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.mobile = value;
		req.msg_id = btnVCode.getVCodeId();
		req.msg_code = edVCode.getText().toString();
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPDATE_USER, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				if(resp.statusCode==200 && resp.result!=null) {
					BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
					T.showShort(BindMobileActivity.this, bean.result_desc);
					if(Api.SUCCEED == bean.result_code) {
						T.showShort(BindMobileActivity.this, "修改成功");
						userInfo.mobile = value;
						UserInfoManager.saveUserInfo(BindMobileActivity.this, userInfo);
						setResult(RESULT_OK);
						finish();
					} else {
						T.showShort(BindMobileActivity.this, bean.result_desc);
					}
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(BindMobileActivity.this);
			}
		});
	}
}
