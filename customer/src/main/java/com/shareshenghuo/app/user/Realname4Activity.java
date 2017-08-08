package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.activity.realname.RealnameStepIDCardActivity;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.request.UpdateUserInfoRequest;
import com.shareshenghuo.app.user.network.response.LoginResponse;
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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Realname4Activity extends BaseTopActivity{
	private TextView tv_pro,tv_pro1;
	private Button llWalletRecharge,llWalletRecharge1;
	private String tag = "",msg = "";
	private TextView tvsimg;
	private TextView real_pointimg,real_pointimg1,real_pointimg2;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.realname4_layout);
		tag = getIntent().getStringExtra("tag");
		msg = getIntent().getStringExtra("msg");
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("实名认证");
		tv_pro = getView(R.id.tv_pro);
		llWalletRecharge = getView(R.id.llWalletRecharge);
		llWalletRecharge1 = getView(R.id.llWalletRecharge1);
		real_pointimg = getView(R.id.real_pointimg);
		real_pointimg1 = getView(R.id.real_pointimg1);
		real_pointimg2 = getView(R.id.real_pointimg2);
		real_pointimg.setBackgroundResource(R.drawable.bg_arc_blue);
		real_pointimg.setTextColor(getResources().getColor(R.color.white));
		real_pointimg1.setTextColor(getResources().getColor(R.color.white));
		real_pointimg2.setTextColor(getResources().getColor(R.color.white));
		real_pointimg1.setBackgroundResource(R.drawable.bg_arc_blue);
		real_pointimg2.setBackgroundResource(R.drawable.bg_arc_blue);
		findViewById(R.id.tvs1).setBackgroundResource(R.color.bg_top_bar);
		findViewById(R.id.tvs2).setBackgroundResource(R.color.bg_top_bar);
		
		
		tvsimg = getView(R.id.tvsimg);
		tv_pro1 = getView(R.id.tv_pro1);
		if(msg!=null){
			tv_pro.setText(msg);
//			tv_pro.setTextColor(getResources().getColor(R.color.red));
		}

		if(tag.equals("0")){
			tv_pro1.setVisibility(View.GONE);
			tvsimg.setBackgroundResource(R.drawable.refail);
			llWalletRecharge.setText("确定");
			llWalletRecharge1.setVisibility(View.GONE);
			llWalletRecharge.setBackgroundResource(R.drawable.selector_btn_pink);
			tv_pro.setTextColor(getResources().getColor(R.color.bg_top_bar));
		}
		
		loadUserInfo();
	

		llWalletRecharge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(tag.equals("0")){
//					loadUserInfo();
					Intent it = new Intent(Realname4Activity.this,RealnameStepIDCardActivity.class);
					startActivity(it);
					finish();
				}else{
					finish();
//					loadUserInfo();
//					finish();
				}
			}
		});
		
		llWalletRecharge1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(Realname4Activity.this,AddBankCardActivity.class);
				startActivity(it);
			}
		});
	}
	
	public void loadUserInfo() {
//		ProgressDialogUtil.showProgressDlg(activity, "");
		
		
		UpdateUserInfoRequest req = new UpdateUserInfoRequest();
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
					T.showNetworkError(Realname4Activity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				LoginResponse bean = new Gson().fromJson(resp.result, LoginResponse.class);
				if(Api.SUCCEED==bean.result_code ) {
					UserInfoManager.saveUserInfo(Realname4Activity.this, bean.data);
				}else{
					
				}
			}
		});
	}
}
