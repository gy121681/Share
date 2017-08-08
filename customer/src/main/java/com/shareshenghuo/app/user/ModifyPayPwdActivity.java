package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.request.UpdateUserInfoRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.ViewUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ModifyPayPwdActivity extends BaseTopActivity {
	
	private EditText edNew1;
	private EditText edNew2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_pay_pwd);
		initView();
	}
	
	public void initView() {
		initTopBar("修改支付密码");
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("完成");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				submit();
			}
		});
		
		edNew1 = getView(R.id.edPwdNew1);
		edNew2 = getView(R.id.edPwdNew2);
	}
	
	public void submit() {
		if(ViewUtil.checkEditEmpty(edNew1, "请输入新密码") || ViewUtil.checkEditEmpty(edNew2, "请再次输入新密码"))
			return;
		if(edNew1.getText().length() != 6) {
			T.showShort(this, "密码不符合规则");
			return;
		}
		if(!edNew1.getText().toString().equals(edNew2.getText().toString())) {
			T.showShort(this, "新密码输入不一致");
			return;
		}
		
		modifyPayPwd();
	}
	
	public void modifyPayPwd() {
		ProgressDialogUtil.showProgressDlg(this, "修改支付密码");
		UpdateUserInfoRequest req = new UpdateUserInfoRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.pay_password = edNew1.getText().toString();
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
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(ModifyPayPwdActivity.this, "修改成功");
					finish();
				} else {
					T.showShort(ModifyPayPwdActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ModifyPayPwdActivity.this);
			}
		});
	}
}
