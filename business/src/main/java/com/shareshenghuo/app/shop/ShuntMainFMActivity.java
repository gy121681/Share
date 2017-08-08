package com.shareshenghuo.app.shop;

import android.os.Bundle;

public class ShuntMainFMActivity extends BaseTopActivity{
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.shunt_main_fmactivity);
		initTopBar("我的秀点");
	}
}