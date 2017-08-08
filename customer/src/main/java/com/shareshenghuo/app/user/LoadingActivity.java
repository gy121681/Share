package com.shareshenghuo.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import cn.jpush.android.api.JPushInterface;

import com.shareshenghuo.app.user.R;

public class LoadingActivity extends BaseTopActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent it = new Intent(LoadingActivity.this,MainActivity.class);
				startActivity(it);
				finish();
			}
		}, 2000);
	}

	@Override
	public void onBackPressed() {
	}
	
    @Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
}
