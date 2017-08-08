package com.shareshenghuo.app.user.fragment;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import cn.jpush.android.api.JPushInterface;

import com.shareshenghuo.app.user.fragment.BaseFragment;
import com.shareshenghuo.app.user.LoginActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.request.LoginQuickRequest;
import com.shareshenghuo.app.user.network.response.LoginResponse;
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

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginQuickFragment extends BaseFragment implements OnClickListener {

	private EditText edAccount;
	private EditText edVCode;
	private CountDownButton btnVCode;
	private Button btnLogin;
	private ImageView del_img;
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_login_quick;
	}

	@Override
	protected void init(View rootView) {
		edAccount = (EditText) rootView.findViewById(R.id.edAccount);
		edVCode = (EditText) rootView.findViewById(R.id.edVCode);
		btnVCode = (CountDownButton) rootView.findViewById(R.id.btnGetVCode);
		btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
		
		btnVCode.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		
		del_img = (ImageView) rootView.findViewById(R.id.del_img);
		edAccount.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					del_img.setVisibility(View.VISIBLE);
				}else{
					del_img.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		del_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				edAccount.setText("");
				del_img.setVisibility(View.GONE);
			}
		});
		
		edAccount.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(edAccount.getText().length()>0){
					del_img.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		btnVCode.onStart();
	}

	@Override
	public void onStop() {
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
			
		case R.id.btnLogin:
			login();
			break;
		}
	}
	
	public void login() {
		if(TextUtils.isEmpty(edAccount.getText()) || edAccount.getText().length()!=11) {
			ViewUtil.showEditError(edAccount, "请输入正确的手机号");
			return;
		}
		if(ViewUtil.checkEditEmpty(edVCode, "请输入验证码"))
			return;
		
		ProgressDialogUtil.showProgressDlg(activity, "登录中");
		btnLogin.setEnabled(false);
		LoginQuickRequest req = new LoginQuickRequest();
		req.account = edAccount.getText().toString();
		req.registration_id = JPushInterface.getRegistrationID(activity);
		req.msg_code = edVCode.getText().toString();
		req.msg_id = btnVCode.getVCodeId();
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_LOGIN_QUICK, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				btnLogin.setEnabled(true);
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				btnLogin.setEnabled(true);
				LoginResponse bean = new Gson().fromJson(resp.result, LoginResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					((LoginActivity)activity).login(bean.data);
				} else {
					T.showShort(activity, bean.result_desc);
				}
			}
		});
	}
} 
