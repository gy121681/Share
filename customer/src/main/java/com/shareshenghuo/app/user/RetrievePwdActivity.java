package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.util.PwdUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.network.request.RetrievePwdRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.MD5Utils;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.Util;
import com.shareshenghuo.app.user.util.ViewUtil;
import com.shareshenghuo.app.user.widget.CountDownButton;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.user.widget.dialog.TwoButtonDialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author hang
 * 找回/修改密码
 */
public class RetrievePwdActivity extends BaseTopActivity implements OnClickListener {
	
	private EditText edAccount;
	private TextView tvMobile;
	private ViewGroup mGroupMobile;
	private EditText edVCode;
	private EditText edPwd1, edPwd2;
	private CountDownButton btnVCode;
	private Button btnSubmit;
	private TwoButtonDialog downloadDialog;
	private UserInfo userInfo;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retrieve_pwd);
		initView();
	}
	
	public void initView() {
		edAccount = getView(R.id.edMobile);
		tvMobile = getView(R.id.tvmobile);
		mGroupMobile = getView(R.id.layout_mobile);
		edVCode = getView(R.id.edVCode);
		edPwd1 = getView(R.id.edPassword1);
		edPwd2 = getView(R.id.edPassword2);
		btnVCode = getView(R.id.btnGetVCode);
		btnSubmit = getView(R.id.btnSubmit);
		
		userInfo = UserInfoManager.getUserInfo(this);
		if(userInfo == null) {
			initTopBar("找回登录密码");
			mGroupMobile.setVisibility(View.GONE);
		} else {
			initTopBar("修改登录密码");
			getView(R.id.layout_account).setVisibility(View.GONE);
			mGroupMobile.setVisibility(View.VISIBLE);
			edAccount.setText(userInfo.mobile);
			tvMobile.setHint(String.format("验证手机号：%s", userInfo.mobile));
			edAccount.setEnabled(false);
			edAccount.setVisibility(View.GONE);
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
			T.showShort(RetrievePwdActivity.this, "请输入正确的手机号");
//			ViewUtil.showEditError(edAccount, "请输入正确的手机号");
			return;
		}

		if(TextUtils.isEmpty(edPwd1.getText()) || edPwd1.getText().length()<6) {
			T.showShort(RetrievePwdActivity.this, "请输入6-18位密码");
//			ViewUtil.showEditError(edPwd1, "请输入6-20位密码");
			return;
		}
		if(!Util.pwdutil(edPwd1.getText().toString())){
			initDialog("密码必须是6-18位英文字母、数字或字符组成(不能是纯数字或纯字母)", "确定","");
//			T.showShort(RetrievePwdActivity.this, "密码安全等级不够，请重新设置");
			return;
		}
		if(ViewUtil.checkEditEmpty(edVCode, "请输入验证码"))
			return;
		
		if(TextUtils.isEmpty(edPwd2.getText()) || !edPwd2.getText().toString().equals(edPwd1.getText().toString())) {
			
			T.showShort(RetrievePwdActivity.this, "两次密码输入不一致");
//			ViewUtil.showEditError(edPwd2, "两次密码输入不一致");
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
						for(int i=0; i<3; i++)
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
	
	private void initDialog(String content,String left,String right) {
		// TODO Auto-generated method stub
		downloadDialog = new TwoButtonDialog(RetrievePwdActivity.this, R.style.CustomDialog,
				"", content, left, right,true,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							edPwd1.setText("");
							edPwd2.setText("");
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							edPwd1.setText("");
							edPwd2.setText("");
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
		}
}
