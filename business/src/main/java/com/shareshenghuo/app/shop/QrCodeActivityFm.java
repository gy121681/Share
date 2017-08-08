package com.shareshenghuo.app.shop;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.shareshenghuo.app.shop.widget.SystemBarTintManager;

public class QrCodeActivityFm extends BaseTopActivity{
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.qrcode_activityfm);
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.text_white);
		initTopBar("收款码");
	}

	@Override
	protected void initTopBar(String title) {
		super.initTopBar(title);
		View rootView = findViewById(R.id.layout_root);
	}
}