package com.td.qianhai.epay.oem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.td.qianhai.epay.oem.beans.AppContext;

public class QrCodeActivity extends Activity{
	
	
	private TextView backs,content;
	
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.qr_activty);
		AppContext.getInstance().addActivity(this);
		intview();
	}

	private void intview() {
		
		backs = (TextView) findViewById(R.id.bt_title_left);
		content = (TextView) findViewById(R.id.tv_title_contre);
		content.setText("二维码");
		backs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				finish();
				
			}
		});
		
	}
	
}
