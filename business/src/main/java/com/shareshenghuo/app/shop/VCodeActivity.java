package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.shareshenghuo.app.shop.network.request.RetrievePwdRequest;
import com.shareshenghuo.app.shop.network.request.WithdrawRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.Arith;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;
import com.shareshenghuo.app.shop.widget.CountDownButton;

public class VCodeActivity extends BaseTopActivity implements OnClickListener {
	
	private TextView tvMobile;
	private EditText edVCode;
	private CountDownButton btnVCode;
	private Button btnNext;

	private String mobile;
	
	private int source;
	private String account;
	private double money;
	
	private UserInfo user;
	
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
		
		mobile = UserInfoManager.getUserInfo(this).band_mobile;
		tvMobile.setText(mobile);
		
		source = getIntent().getIntExtra("source", 1);
		account = getIntent().getStringExtra("account");
		money = getIntent().getDoubleExtra("money", 0);
		
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
					withdraw();
				} else {
					T.showShort(VCodeActivity.this, bean.result_desc);
				}
			}
		});
	}
	
	public void withdraw() {
		ProgressDialogUtil.showProgressDlg(this, "提交中");
		user = UserInfoManager.getUserInfo(this);
		WithdrawRequest req = new WithdrawRequest();
		req.user_id = user.shop_id;
		req.fee = money+"";
		req.alipay_account = account;
		req.user_type = "2";
		req.source = source+"";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_WITHDRAW, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					user.money = Arith.sub(user.money, money);
					user.with_drawals_ing_fee = Arith.add(user.with_drawals_ing_fee, money);
					UserInfoManager.saveUserInfo(VCodeActivity.this, user);
					
					Intent it = new Intent(VCodeActivity.this, WithdrawResultActivity.class);
					it.putExtra("source", source);
					it.putExtra("account", account);
					it.putExtra("money", money);
					startActivity(it);
					setResult(RESULT_OK);
					finish();
				} else {
					T.showShort(VCodeActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(VCodeActivity.this);
			}
		});
	}
}
