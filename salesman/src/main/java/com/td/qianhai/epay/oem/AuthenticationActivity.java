package com.td.qianhai.epay.oem;

import com.td.qianhai.epay.oem.activity.realname.RealnameStepIDCardActivity;
import com.td.qianhai.epay.oem.beans.AppContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AuthenticationActivity extends BaseActivity{
	
	private TextView tv_au_button;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.authentication_activity);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		((TextView) findViewById(R.id.tv_title_contre)).setText("实名认证");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		tv_au_button = (TextView) findViewById(R.id.tv_au_button);
		tv_au_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent it = new Intent(AuthenticationActivity.this,AuthenticationActivity1.class);
				Intent it = new Intent(AuthenticationActivity.this,RealnameStepIDCardActivity.class);
				startActivity(it);
				finish();
			}
		});
	}
}
