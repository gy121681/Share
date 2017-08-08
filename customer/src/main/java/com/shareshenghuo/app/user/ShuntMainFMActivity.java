package com.shareshenghuo.app.user;

import android.os.Bundle;

import com.shareshenghuo.app.user.R;

public class ShuntMainFMActivity extends BaseTopActivity{
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.shunt_main_fmactivity);
		initTopBar("我的秀点");
	}
}