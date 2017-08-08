package com.td.qianhai.epay.oem;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.td.qianhai.epay.oem.beans.AppContext;

public class GuidelinesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e_pay_guidelines);
		AppContext.getInstance().addActivity(this);
		ScrollView scroll = (ScrollView) findViewById(R.id.scroll);

		scroll.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				finish();
				return true;
			}
		});

	}

	/**
	 * 触屏事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			finish();
		}
		return super.onTouchEvent(event);
	}

}
