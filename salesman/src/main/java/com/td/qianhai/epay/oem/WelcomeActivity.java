package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.qianhai.epay.oem.unlock.UnlockLoginActivity;
import com.td.qianhai.epay.oem.views.SystemBarTintManager;

public class WelcomeActivity extends Activity {

	private ViewPager viewpager = null;
	private List<View> list = null;
	private ImageView[] img = null;
	String type = "0";
	private TextView btn_welcome;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_welcome);
		
		Intent it = getIntent();
		type = it.getStringExtra("type");
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(false);
//		tintManager.setStatusBarTintResource(R.color.eo);
		
		viewpager = (ViewPager) findViewById(R.id.wc_viewpager);
		
		list = new ArrayList<View>();
		list.add(getLayoutInflater().inflate(R.layout.atab_1, null));
		list.add(getLayoutInflater().inflate(R.layout.atab_2, null));
		View v = getLayoutInflater().inflate(R.layout.atab_3, null);
		list.add(v);
		btn_welcome= (TextView) v.findViewById(R.id.btn_welcome);

		img = new ImageView[list.size()];
		LinearLayout layout = (LinearLayout) findViewById(R.id.wc_viewGroup);
		for (int i = 0; i < list.size(); i++) {
			img[i] = new ImageView(WelcomeActivity.this);
			if (0 == i) {
				img[i].setImageResource(R.drawable.resize1);
			} else {
				img[i].setImageResource(R.drawable.resize2);
			}
			img[i].setPadding(0, 0, 20, 0);
			layout.addView(img[i]);
		}
		viewpager.setAdapter(new ViewPagerAdapter(list));
		viewpager.setOnPageChangeListener(new ViewPagerPageChangeListener());
		
		btn_welcome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent it = new Intent();
				if(type.equals("0")){
					it.setClass(WelcomeActivity.this, UserActivity.class);
					startActivity(it);
					finish();
				}else{
					it.setClass(WelcomeActivity.this, UnlockLoginActivity.class);
					startActivity(it);
					finish();
				}
				
			}
		});
		
		
		
	}
	
	@TargetApi(19) 
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	class ViewPagerPageChangeListener implements OnPageChangeListener {

		/*
		 * state：网上通常说法：1的时候表示正在滑动，2的时候表示滑动完毕了，0的时候表示什么都没做，就是停在那；
		 * 我的认为：1是按下时，0是松开，2则是新的标签页的是否滑动了
		 * (例如：当前页是第一页，如果你向右滑不会打印出2，如果向左滑直到看到了第二页，那么就会打印出2了)；
		 * 个人认为一般情况下是不会重写这个方法的
		 */
		@Override
		public void onPageScrollStateChanged(int state) {
		}

		/*
		 * page：看名称就看得出，当前页； positionOffset：位置偏移量，范围[0,1]；
		 * positionoffsetPixels：位置像素，范围[0,屏幕宽度)； 个人认为一般情况下是不会重写这个方法的
		 */
		@Override
		public void onPageScrolled(int page, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int page) {
			// 更新图标
			for (int i = 0; i < list.size(); i++) {
				if (page == i) {
					img[i].setImageResource(R.drawable.resize1);
				} else {
					img[i].setImageResource(R.drawable.resize2);
				}
			}
		}
	}

	class ViewPagerAdapter extends PagerAdapter {

		private List<View> list = null;

		public ViewPagerAdapter(List<View> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(list.get(position));
			return list.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

}
