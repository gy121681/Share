package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.request.FindPayPwdRequest;
import com.shareshenghuo.app.user.network.response.SetPaypwdResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.MD5Utils;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.Util;
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
import android.widget.Button;
import android.widget.EditText;

public class SetPayPassActivity extends BaseTopActivity{
	
	private EditText edPassword1,edPassword2,edPassword3;
	private Button btnSubmit;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpaypass_avctivity);
        initView();
    }

	private void initView() {
		// TODO Auto-generated method stub
		initTopBar("修改支付密码");
		edPassword1 = getView(R.id.edPassword1);
		edPassword2 = getView(R.id.edPassword2);
		edPassword3 = getView(R.id.edPassword3);
		btnSubmit = getView(R.id.btnSubmit);
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getpaypwd();
				
			}


		});
	}
	
	private void getpaypwd() {
		// TODO Auto-generated method stub

		if(ViewUtil.checkEditEmpty(edPassword1, "不能为空"))
			return;

		
		if(ViewUtil.checkEditEmpty(edPassword2, "不能为空"))
			return;
		
		if(Util.paypwd(edPassword2.getText().toString())){
			T.showShort(SetPayPassActivity.this,"密码安全等级不够，请重新设置");
			return;
		}
		
		if(ViewUtil.checkEditEmpty(edPassword3, "不能为空"))
			return;
		
		if(edPassword1.getText().toString().length()<6){
			ViewUtil.showError(edPassword1,"6位数字");
			return;
		}
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
		
		
		String pwd = "";
		String oldpwd = "";
		oldpwd = edPassword1.getText().toString();
		pwd = edPassword3.getText().toString();
		
		ProgressDialogUtil.showProgressDlg(this, "提交中");
		 FindPayPwdRequest req = new FindPayPwdRequest();
		for(int i=0; i<3; i++){
			pwd = MD5Utils.getMD5String(pwd);
			oldpwd = MD5Utils.getMD5String(oldpwd);
		}
		req.oldPayPassword = oldpwd;
		req.newPayPassword = pwd;
		req.userShopId = UserInfoManager.getUserInfo(this).id+"";
		req.userType = "1";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.UPDATEPAYPASSWORD, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showShort(SetPayPassActivity.this, "失败");
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				SetPaypwdResponse bean = new Gson().fromJson(resp.result, SetPaypwdResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					
					if(bean.data.RSPCOD.equals("000000")){
						T.showShort(SetPayPassActivity.this, bean.data.RSPMSG);
						finish();
					}else{
						T.showShort(SetPayPassActivity.this,bean.data.RSPMSG);
					}
				
//					if(userInfo != null) {
//						String pwd = req.password;
//						for(int i=0; i<3; i++);
//							pwd = MD5Utils.getMD5String(pwd);
//						UserInfoManager.setUserPwd(FindPaypassActivity.this, pwd);
//					}
					
				} else {
					T.showShort(SetPayPassActivity.this, bean.result_desc);
				}
			}
		});
		
		
	}
}
