package com.shareshenghuo.app.user.senceidcard;

import com.sensetime.service.STService;

import android.app.Application;

public class MyApplication extends Application {
	
//	#error // 请先填写您所使用的app id和app secret
	private static final String APP_ID = "b329ed69d52b4ff396622f11f1dfd4b8";
	private static final String APP_SECRET = "ca54559bd0b741d99b8e831c5a7ae277";
	
	@Override
	public void onCreate() {
		super.onCreate();
		STService.getInstance(this).activateInBackground(APP_ID, APP_SECRET);
	}

}
