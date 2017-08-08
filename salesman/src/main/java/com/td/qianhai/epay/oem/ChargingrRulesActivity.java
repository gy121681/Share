package com.td.qianhai.epay.oem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ChargingrRulesActivity extends BaseActivity{

	private TextView tv_feet;
	private String aa;
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent it = getIntent();
		
		 aa = it.getStringExtra("creet");
		
		setContentView(R.layout.activity_chargingr);
		
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		tv_feet = (TextView) findViewById(R.id.tv_feet);
		tv_feet.setText(aa+"%");
		((TextView) findViewById(R.id.tv_title_contre)).setText("收费规则");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		}
}
