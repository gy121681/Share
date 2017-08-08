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
import com.shareshenghuo.app.shop.network.request.SetPaypwdRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.network.response.SetPaypwdResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.MD5Utils;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;
import com.shareshenghuo.app.shop.widget.CountDownButton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FindPaypassActivity extends BaseTopActivity {

	private EditText edPassword2, edPassword3, edMobile, edVCode;
	private Button btnSubmit;
	private String tag = "1";
	private CountDownButton btnGetVCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findpaypass_activity);
		tag = getIntent().getStringExtra("tag");
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		edMobile = getView(R.id.edMobile);
		edVCode = getView(R.id.edVCode);
		btnGetVCode = getView(R.id.btnGetVCode);
		edPassword2 = getView(R.id.edPassword2);
		edPassword3 = getView(R.id.edPassword3);
		btnSubmit = getView(R.id.btnSubmit);
		if (tag!=null) {
			initTopBar("重置支付密码");
			edPassword2.setHint("请输入新支付密码");
		} else {
			initTopBar("设置支付密码");
			edPassword2.setHint("请输入支付密码");
		}



		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				setpwd();

			}
		});
		btnGetVCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String phone = UserInfoManager.getUserInfo(FindPaypassActivity.this).band_mobile;
				btnGetVCode.getVCode(phone, null);

			}
		});
	}

	private void setpwd() {
		// TODO Auto-generated method stub
		
		if(ViewUtil.checkEditEmpty(edPassword2, "不能为空"))
			return;
		if(ViewUtil.checkEditEmpty(edPassword3, "不能为空"))
			return;
		
		if(edPassword2.getText().toString().length()<6){
			ViewUtil.showError(edPassword2,"6位数字");
			return;
		}
		if(edPassword3.getText().toString().length()<6){
			ViewUtil.showError(edPassword3,"6位数字");
			return;
		}
		
		if(!edPassword2.getText().toString().equals(edPassword3.getText().toString())){
			ViewUtil.showError(edPassword3, "两次密码不一致");
			return;
		}
		if(ViewUtil.checkEditEmpty(edVCode, "请输入验证码"))
			return;
		
		String pwd = "";
		pwd = edPassword3.getText().toString();
		ProgressDialogUtil.showProgressDlg(this, "提交中");
		final SetPaypwdRequest req = new SetPaypwdRequest();
		for(int i=0; i<3; i++){
			pwd = MD5Utils.getMD5String(pwd);
		}
		req.payPassword = pwd;
		req.userShopId = UserInfoManager.getUserInfo(this).shop_id+"";
		req.msgId = btnGetVCode.getVCodeId();
		req.msgCode = edVCode.getText().toString();
		req.userType = "2";
		
		Log.e("", " 0  - - - -   "+req.toString());
		
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.SETPAYPASSWORD, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showShort(FindPaypassActivity.this, "失败");
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				SetPaypwdResponse bean = new Gson().fromJson(resp.result, SetPaypwdResponse.class);
				Log.e("", " 1  - - - -   "+resp.result);
				if(Api.SUCCEED == bean.result_code) {
					
					if(bean.data.RSPCOD.equals("000000")){
						T.showShort(FindPaypassActivity.this, bean.data.RSPMSG);
						finish();
					}else{
						T.showShort(FindPaypassActivity.this,bean.data.RSPMSG);
					}
				
//					if(userInfo != null) {
//						String pwd = req.password;
//						for(int i=0; i<3; i++);
//							pwd = MD5Utils.getMD5String(pwd);
//						UserInfoManager.setUserPwd(FindPaypassActivity.this, pwd);
//					}
					
				} else {
					T.showShort(FindPaypassActivity.this, bean.result_desc);
				}
			}
		});
		
	}
}
