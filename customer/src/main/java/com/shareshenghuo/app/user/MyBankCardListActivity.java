package com.shareshenghuo.app.user;

import android.os.Bundle;

import com.shareshenghuo.app.user.R;

public class MyBankCardListActivity extends BaseTopActivity{
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.mybankcardlist_activity);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("我的银行卡");
	}
}
