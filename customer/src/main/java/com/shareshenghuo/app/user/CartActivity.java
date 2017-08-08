package com.shareshenghuo.app.user;

import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.WindowManager;

import com.shareshenghuo.app.user.R;


public class CartActivity extends BaseTopActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cart);
		
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
        	getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
	}
}
