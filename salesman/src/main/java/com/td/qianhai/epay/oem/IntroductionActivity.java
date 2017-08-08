package com.td.qianhai.epay.oem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.td.qianhai.epay.oem.beans.AppContext;

public class IntroductionActivity extends Activity{
	
	private  TextView title_tv,content_tv;
	private TextView tv_finishs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_introduction);
		AppContext.getInstance().addActivity(this);
		Intent it = getIntent();
		
		tv_finishs = (TextView) findViewById(R.id.tv_finish);
		
		
		title_tv = (TextView) findViewById(R.id.title_tv);
		
		content_tv = (TextView) findViewById(R.id.content_tv);
		
		String tag = it.getStringExtra("tag");
		
		String title = it.getStringExtra("title");
		
		String content = it.getStringExtra("description");
		
		
		if(tag!=null&&!tag.equals("")){
			
			title_tv.setText(getResources().getString(R.string.introduction_name));
			content_tv.setText(getResources().getString(R.string.introduction));
			
		}else{
			
			if (content != null) {
				
				title_tv.setText("消息");
				content_tv.setText(content);
				
			} else if (((AppContext) getApplication()).getMsgcontent() != null) {
				
				title_tv.setText(((AppContext) getApplication()).getMsgtitle());
				content_tv.setText(((AppContext) getApplication()).getMsgcontent());
				
			}
		}

		
		
		tv_finishs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

}
