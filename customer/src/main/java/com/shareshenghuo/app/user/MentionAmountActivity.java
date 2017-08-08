package com.shareshenghuo.app.user;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.widget.CircleProgressView;

import android.os.Bundle;

public class MentionAmountActivity extends BaseTopActivity{
	
	
	private CircleProgressView circleProgressbar;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.mention_amount_activity);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("我的额度");

		circleProgressbar = (CircleProgressView) findViewById(R.id.circleProgressbar);

		circleProgressbar.setProgress(80);
	}

}
