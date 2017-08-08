package com.shareshenghuo.app.user.network.bean;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.shareshenghuo.app.user.BaseTopActivity;
import com.shareshenghuo.app.user.PaymentnewActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.WebLoadFragment;

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

		fragment.setmPaySuccessListener(new WebLoadFragment.OnTLPaySuccessListener() {
			@Override
			public void onpaySuccess() {
				Toast.makeText(WebLoadActivity.this, "支付成功！", Toast.LENGTH_SHORT).show();
				setResult(PaymentnewActivity.PAY_REQ);
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		fragment.goBack();
	}
	
}
