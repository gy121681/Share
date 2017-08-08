package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyPagerAdapter extends PagerAdapter {
	
	private List<View> data;
	
	public MyPagerAdapter(List<View> data) {
		super();
		this.data = data;
		
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(data.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(data.get(position), 0);
		return data.get(position);
	}

}
