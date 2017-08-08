package com.shareshenghuo.app.user;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.UserInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MobileChangeActivity extends BaseTopActivity{
	
	private TextView tvmobile;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.mobile_change_activity);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("手机号码");
		tvmobile = getView(R.id.tvmobile);
		UserInfo userInfo = UserInfoManager.getUserInfo(this);
		tvmobile.setText("您当前的手机号码为: "+userInfo.mobile);
		getView(R.id.llWalletRecharge).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MobileChangeActivity.this,MobileChangeActivitytwo.class));
				finish();
			}
		});
	}
}
