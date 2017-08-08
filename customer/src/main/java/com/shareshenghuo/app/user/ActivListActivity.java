package com.shareshenghuo.app.user;

import android.os.Bundle;

import com.shareshenghuo.app.user.R;

public class ActivListActivity extends BaseTopActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activ_list);
		initTopBar("优惠活动");
	}
}
