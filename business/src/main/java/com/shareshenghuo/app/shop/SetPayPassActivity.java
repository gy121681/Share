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
import com.shareshenghuo.app.shop.network.request.FindPayPwdRequest;
import com.shareshenghuo.app.shop.network.request.SetPaypwdRequest;
import com.shareshenghuo.app.shop.network.response.SetPaypwdResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.MD5Utils;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;

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
		initTopBar("修改支付密码");
		edPassword1 = getView(R.id.edPassword1);
		edPassword2 = getView(R.id.edPassword2);
		edPassword3 = getView(R.id.edPassword3);
		btnSubmit = getView(R.id.btnSubmit);
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				getpaypwd();
			}
		});
	}
	
	private void getpaypwd() {
		if(ViewUtil.checkEditEmpty(edPassword1, "不能为空"))
			return;
		if(ViewUtil.checkEditEmpty(edPassword2, "不能为空"))
			return;
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
		req.userShopId = UserInfoManager.getUserInfo(this).shop_id+"";
		req.userType = "2";
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
