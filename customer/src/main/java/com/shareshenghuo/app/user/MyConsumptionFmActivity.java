package com.shareshenghuo.app.user;

import android.os.Bundle;

import com.shareshenghuo.app.user.R;

public class MyConsumptionFmActivity extends BaseTopActivity{
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.my_consumption_fmactivity);
		initTopBar("我的消费");
	}
}
