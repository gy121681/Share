package com.shareshenghuo.app.shop;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.MyPagerAdapter;
import com.shareshenghuo.app.shop.util.PreferenceUtils;

public class GuideActivity extends BaseTopActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
//		if(!PreferenceUtils.getPrefBoolean(this, "first", true)) {
			startActivity(new Intent(GuideActivity.this, LoginActivity.class));
			finish();
			return;
//		}
//		
//		initView();
	}
	
	public void initView() {
		List<View> views = new ArrayList<View>();
		ImageView view1 = (ImageView) LayoutInflater.from(this).inflate(R.layout.activity_loading, null);
		view1.setImageResource(R.drawable.bg_guide_1);
		ImageView view2 = (ImageView) LayoutInflater.from(this).inflate(R.layout.activity_loading, null);
		view2.setImageResource(R.drawable.bg_guide_2);
		ImageView view3 = (ImageView) LayoutInflater.from(this).inflate(R.layout.activity_loading, null);
		view3.setImageResource(R.drawable.bg_guide_3);
		ImageView view4 = (ImageView) LayoutInflater.from(this).inflate(R.layout.activity_loading, null);
		view4.setImageResource(R.drawable.bg_guide_4);
		ImageView view5 = (ImageView) LayoutInflater.from(this).inflate(R.layout.activity_loading, null);
		view5.setImageResource(R.drawable.bg_guide_5);
		
		view5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(GuideActivity.this, LoginActivity.class));
				PreferenceUtils.setPrefBoolean(GuideActivity.this, "first", false);
				finish();
			}
		});
		
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		views.add(view5);
		((ViewPager) getView(R.id.vpGuide)).setAdapter(new MyPagerAdapter(views));
	}
	
//	@Override
//	protected void onPause() {
//		super.onPause();
//		JPushInterface.onPause(this);
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		JPushInterface.onResume(this);
//	}
}
