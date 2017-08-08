package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.network.request.UpdateUserInfoRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author hang
 *	签到
 */
public class SignActivity extends BaseTopActivity implements OnClickListener {
	
	private Button btnSign;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		initView();
	}
	
	public void initView() {
		btnSign = getView(R.id.btnSign);
		
		btnSign.setOnClickListener(this);
		findViewById(R.id.btnSignBack).setOnClickListener(this);
		
		UserInfo userInfo = UserInfoManager.getUserInfo(this);
		if(userInfo.is_sign==1) {
			btnSign.setEnabled(false);
			btnSign.setText("已签到");
		}
		setText(R.id.tvSignDay, "第"+(userInfo.sign_count+1)+"天");
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnSignBack:
			finish();
			break;
			
		case R.id.btnSign:
			sign();
			break;
		}
	}
	
	public void sign() {
		ProgressDialogUtil.showProgressDlg(this, "签到中");
		UpdateUserInfoRequest req = new UpdateUserInfoRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_SIGN, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(SignActivity.this, "签到成功");
					btnSign.setEnabled(false);
					btnSign.setText("已签到");
				} else {
					T.showShort(SignActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(SignActivity.this);
			}
		});
	}
}
