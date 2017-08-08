package com.td.qianhai.epay.oem;

import com.td.qianhai.epay.oem.beans.AppContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ConvenienceServicesActivity extends BaseActivity implements OnClickListener{
	
	private LinearLayout phone_charges,lin_etc,lin_add,lin_credit;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.convenience_services_activity);
		AppContext.getInstance().addActivity(this);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		
		phone_charges = (LinearLayout) findViewById(R.id.phone_charges);
		lin_etc = (LinearLayout) findViewById(R.id.lin_etc);
		lin_add = (LinearLayout) findViewById(R.id.lin_add);
		phone_charges.setOnClickListener(this);
		lin_credit = (LinearLayout) findViewById(R.id.lin_credit);
		lin_credit.setOnClickListener(this);
		lin_etc.setOnClickListener(this);
		lin_add.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.phone_charges:
			intent.setClass(ConvenienceServicesActivity.this, PhonereChargeActivity.class);
			startActivity(intent);
			break;
		case R.id.lin_etc:
			intent.setClass(ConvenienceServicesActivity.this, EtcActivity.class);
			startActivity(intent);
//			Toast.makeText(getApplicationContext(), "即将开通", Toast.LENGTH_SHORT).show();
			break;
		case R.id.lin_add:
			Toast.makeText(getApplicationContext(), "敬请期待", Toast.LENGTH_SHORT).show();
			break;
//		case R.id.lin_credit:
//			intent.setClass(ConvenienceServicesActivity.this, CreditToActivity.class);
//			startActivity(intent);
////			Toast.makeText(getApplicationContext(), "即将开通", Toast.LENGTH_SHORT).show();
//			break;
			
		default:
			break;
		}
		
	}

}
