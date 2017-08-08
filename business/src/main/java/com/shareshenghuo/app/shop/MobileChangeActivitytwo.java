package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.request.RetrievePwdRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.widget.CountDownButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MobileChangeActivitytwo extends BaseTopActivity{
	
	private EditText edMobile;
	private TextView tvMobile;
	private EditText edVCode;
	private CountDownButton btnVCode;
	private Button btnNext;

	private String mobile;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.mobile_change_activityt);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("验证手机号");
		mobile = UserInfoManager.getUserInfo(this).account;
		edMobile = getView(R.id.edMobiles);
		edMobile.setText(mobile);
		edMobile.setEnabled(false);
		btnVCode = getView(R.id.btnGetVCode);
		btnNext = getView(R.id.btnBind);
		btnNext.setText("验证");
		edVCode = getView(R.id.edVCode);
		btnVCode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				btnVCode.getVCode(edMobile.getText().toString(), null);
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(edVCode.getText())){
					T.showShort(MobileChangeActivitytwo.this, "请输入验证码");
				}
				if(!TextUtils.isEmpty(mobile)){
					checkVCode();
				}
			}
		});
		
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
				T.showNetworkError(MobileChangeActivitytwo.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
//					T.showShort(MobileChangeActivitytwo.this, "验证成功");
					
					Intent bind = new Intent(MobileChangeActivitytwo.this, BindMobileActivity.class);
					bind.putExtra("userInfo", UserInfoManager.getUserInfo(MobileChangeActivitytwo.this));
					bind.putExtra("back", true);
					startActivity(bind);
//					startActivity(new Intent(MobileChangeActivitytwo.this, BindMobileActivity.class));
//					
					finish();
				} else {
					T.showShort(MobileChangeActivitytwo.this, bean.result_desc);
				}
			}
		});
	}
}
