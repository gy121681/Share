package com.shareshenghuo.app.shop.network.bean;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.shareshenghuo.app.shop.BaseTopActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.R.id;
import com.shareshenghuo.app.shop.R.layout;

public class WebLoadActivity extends BaseTopActivity {
	
	private String title;
	private String url;
	
	private WebLoadFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_load);
		
		title = getIntent().getStringExtra("title");
		url = getIntent().getStringExtra("url");
		Log.e("", ""+url);
		initTopBar(title);
		
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("关闭");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		fragment = WebLoadFragment.getInstance(url);
		t.replace(R.id.rlContent, fragment);
		t.commit();
		
		llTopBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				fragment.goBack();
			}
		});
		
	}

	@Override
	public void onBackPressed() {
		fragment.goBack();
	}
}
