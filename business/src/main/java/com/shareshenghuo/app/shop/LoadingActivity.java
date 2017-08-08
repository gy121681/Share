package com.shareshenghuo.app.shop;

import android.os.Bundle;
import android.os.Handler;

import com.shareshenghuo.app.shop.R;

public class LoadingActivity extends BaseTopActivity {
	
	public static LoadingActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_loading);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				finish();
			}
		}, 2000);
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}
}
