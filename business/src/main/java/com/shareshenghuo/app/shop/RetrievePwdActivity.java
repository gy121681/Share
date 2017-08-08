package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.shareshenghuo.app.shop.network.bean.UserInfo;
import com.shareshenghuo.app.shop.network.request.RetrievePwdRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.MD5Utils;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.PwdUtils;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;
import com.shareshenghuo.app.shop.widget.CountDownButton;

/**
 * @author hang
 * 找回/修改密码
 */
public class RetrievePwdActivity extends BaseTopActivity implements OnClickListener {
	
	private EditText edAccount;
	private EditText edVCode;
	private EditText edPwd1, edPwd2;
	private CountDownButton btnVCode;
	private Button btnSubmit;
	
	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retrieve_pwd);
		initView();
	}
	
	public void initView() {
		edAccount = getView(R.id.edMobile);
		edVCode = getView(R.id.edVCode);
		edPwd1 = getView(R.id.edPassword1);
		edPwd2 = getView(R.id.edPassword2);
		btnVCode = getView(R.id.btnGetVCode);
		btnSubmit = getView(R.id.btnSubmit);
		
		userInfo = UserInfoManager.getUserInfo(this);
		if(userInfo == null) {
			initTopBar("找回密码");
		} else {
			initTopBar("修改密码");
			edAccount.setText(userInfo.band_mobile);
			edAccount.setEnabled(false);
		}
		
		btnVCode.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
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
			if(TextUtils.isEmpty(edAccount.getText()) || edAccount.getText().length()!=11) {
				ViewUtil.showEditError(edAccount, "请输入正确的手机号");
				return;
			}
			btnVCode.getVCode(edAccount.getText().toString(), null);
			break;
			
		case R.id.btnSubmit:
			submit();
			break;
		}
	}
	
	public void submit() {
		if(TextUtils.isEmpty(edAccount.getText()) || edAccount.getText().length()!=11) {
			ViewUtil.showEditError(edAccount, "请输入正确的手机号");
			return;
		}
		if(ViewUtil.checkEditEmpty(edVCode, "请输入验证码"))
			return;
		if(TextUtils.isEmpty(edPwd1.getText()) || edPwd1.getText().length()<6) {
			ViewUtil.showEditError(edPwd1, "请输入6-20位密码");
			return;
		}
		if(TextUtils.isEmpty(edPwd2.getText()) || !edPwd2.getText().toString().equals(edPwd1.getText().toString())) {
			ViewUtil.showEditError(edPwd2, "两次密码输入不一致");
			return;
		}
		
		ProgressDialogUtil.showProgressDlg(this, "提交中");
		final RetrievePwdRequest req = new RetrievePwdRequest();
		req.account = edAccount.getText().toString();
		req.password = PwdUtils.getEncripyPwd(edPwd1.getText().toString(), 3);
		req.msg_id = btnVCode.getVCodeId();
		req.msg_code = edVCode.getText().toString();
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_RETRIEVE_PWD, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showShort(RetrievePwdActivity.this, "修改失败");
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(RetrievePwdActivity.this, "修改成功");
					if(userInfo != null) {
						String pwd = req.password;
						for(int i=0; i<3; i++);
							pwd = MD5Utils.getMD5String(pwd);
						UserInfoManager.setUserPwd(RetrievePwdActivity.this, pwd);
					}
					finish();
				} else {
					T.showShort(RetrievePwdActivity.this, bean.result_desc);
				}
			}
		});
	}
}
