package com.shareshenghuo.app.user;

import android.os.Bundle;

import com.shareshenghuo.app.user.R;

/**
 * @author hang
 * 关于我们
 */
public class AboutActivity extends BaseTopActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initView();
	}
	
	public void initView() {
		initTopBar("关于我们");
	}
}
