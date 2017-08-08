package com.shareshenghuo.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.app.CityLifeApp;

public class Realname1Activity extends BaseTopActivity{
	private TextView real_pointimg,real_pointimg1,real_pointimg2;
	private Button llWalletRecharge;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.realname1_layout);
		CityLifeApp.getInstance().addActivity(this);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("实名认证");
		real_pointimg = getView(R.id.real_pointimg);
		real_pointimg1 = getView(R.id.real_pointimg1);
		real_pointimg2 = getView(R.id.real_pointimg2);
		real_pointimg.setBackgroundResource(R.drawable.newreal_point);
		llWalletRecharge = getView(R.id.llWalletRecharge);
		llWalletRecharge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(Realname1Activity.this,Realname2Activity.class);
				startActivity(it);
			}
		});
	}
}
