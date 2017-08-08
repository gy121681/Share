package com.shareshenghuo.app.user;

import android.os.Bundle;

import com.shareshenghuo.app.user.R;

public class ExcitationMainActivity extends BaseTopActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.excitation_main_activity);
		initTopBar("我的秀心");
	}
}