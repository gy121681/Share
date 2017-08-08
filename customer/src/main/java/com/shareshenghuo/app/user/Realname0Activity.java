package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.activity.realname.RealnameStepIDCardActivity;
import com.shareshenghuo.app.user.app.CityLifeApp;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.request.UpdateUserInfoRequest;
import com.shareshenghuo.app.user.network.response.LoginResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.user.widget.dialog.TwoButtonDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class Realname0Activity extends BaseTopActivity{
	private TextView real_pointimg,real_pointimg1,real_pointimg2,tv_pro;
	private Button llWalletRecharge;
	private LinearLayout realname_pro,ll_function,ll_info;
	private TwoButtonDialog downloadDialog;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.realname1_layout);
		CityLifeApp.getInstance().addActivity(this);
		initview();
		
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		loadUserInfo();
	
	}

	public void loadUserInfo() {
//		ProgressDialogUtil.showProgressDlg(activity, "");
		
		
		
		UpdateUserInfoRequest req = new UpdateUserInfoRequest();
//		EncryptionUtil.getEncryptionstring(req.user_id);
//		req.isEncrypt = "1";
//		req.info = EncryptionUtil.getEncryptionstring(req.user_id);
		req.user_id = UserInfoManager.getUserId(this)+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		new HttpUtils().send(HttpMethod.POST, Api.URL_GET_USERINFO, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				if(this != null)
					T.showNetworkError(Realname0Activity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				LoginResponse bean = new Gson().fromJson(resp.result, LoginResponse.class);
				if(Api.SUCCEED==bean.result_code ) {
					UserInfoManager.saveUserInfo(Realname0Activity.this, bean.data);
//					updateView(bean.data);
				}
			}
		});
	}
	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("实名认证");
		real_pointimg = getView(R.id.real_pointimg);
		real_pointimg1 = getView(R.id.real_pointimg1);
		real_pointimg2 = getView(R.id.real_pointimg2);
		realname_pro = getView(R.id.realname_pro);
		realname_pro.setVisibility(View.GONE);
		ll_function = getView(R.id.ll_function);
		ll_info = getView(R.id.ll_info);
		ll_info.setVisibility(View.GONE);
		ll_function.setVisibility(View.VISIBLE);
		tv_pro = getView(R.id.tv_pro);
		tv_pro.setText("完成身份认证,享受高级服务");
		real_pointimg.setBackgroundResource(R.drawable.newreal_point);
		llWalletRecharge = getView(R.id.llWalletRecharge);
		llWalletRecharge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String type = UserInfoManager.getUserInfo(Realname0Activity.this).certification_step;
				if(!TextUtils.isEmpty(type)){
					Intent it = new Intent();
//					if(type.equals("0")){
						it.setClass(Realname0Activity.this,RealnameStepIDCardActivity.class);
						startActivity(it);
//					}else if(type.equals("1")){
//						it.setClass(Realname0Activity.this,Realname2Activity.class);
//						startActivity(it);
//					}else if(type.equals("2")){
//						it.setClass(Realname0Activity.this,RealnameSenseActivity.class);
//						startActivity(it);
//					}else if(type.equals("3")){
//						it.setClass(Realname0Activity.this,Realname4Activity.class);
//						it.putExtra("tag", "1");
//						startActivity(it);
//						finish();
//					}
				}
			}
		});
		
		llTopBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					initDialog("确定退出实名认证?", "取消", "确定");
			}
		});
	}
	
	private void initDialog(String content,String left,String right) {
		downloadDialog = new TwoButtonDialog(Realname0Activity.this, R.style.CustomDialog,
				"", content, left, right,true,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							finish();
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
		}
	/**
	 * 监听返回按钮
	 */
//	@Override
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			initDialog("确定退出实名认证?", "取消", "确定");
		}
		return true;
	}
}
