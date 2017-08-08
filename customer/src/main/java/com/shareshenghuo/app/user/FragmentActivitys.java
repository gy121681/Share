package com.shareshenghuo.app.user;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.fragment.HomeFragment;
import com.shareshenghuo.app.user.fragment.LifeFragment;
import com.shareshenghuo.app.user.fragment.MineFragment;
import com.shareshenghuo.app.user.fragment.ShopListFragment;
import com.shareshenghuo.app.user.fragment.ShowDataFragment;


public class FragmentActivitys extends FragmentActivity {
	private FragmentTabHost mTabHost;
	private Class fragmentArray[] = {HomeFragment.class, ShopListFragment.class, ShowDataFragment.class, LifeFragment.class, MineFragment.class};
	private int iconArray[] = {R.drawable.tab_home, R.drawable.tab_shop, R.drawable.tab_cart, R.drawable.tab_life, R.drawable.tab_mine};
	private String titleArray[] = {"tab_home", "tab_shop_list", "tab_cart", "tab_life", "tab_mine"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupTabView();
		addListenner();
	}

//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.tabhost_fg, null);
//	
//	
//		
//		return view;
//	}

//	@Override
//	public void onDestroyView() {
//		super.onDestroyView();
//		
//	}

	private void setupTabView() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
    	
		mTabHost.setup(this,getSupportFragmentManager(), R.id.realtabcontent);
		mTabHost.getTabWidget().setDividerDrawable(null);
//		mTabHost.getTabWidget().setBackgroundResource(R.drawable.tab_background);

		int count = fragmentArray.length;
		for (int i = 0; i < count; i++) {
			TabHost.TabSpec tabSpec = mTabHost.newTabSpec(titleArray[i]).setIndicator(getTabItemView(i));
			mTabHost.addTab(tabSpec, fragmentArray[i], null);			
//			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_item);
		}

	}

	private void addListenner() {
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
//				if ("Tab3".equals(tabId)) {
//					mTitle.setVisibility(View.GONE);
//				}else {
//				}
			}
		});
		
	}

	private View getTabItemView(int index) {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View view = layoutInflater.inflate(R.layout.view_tab_main, null);

		ImageView imageView = (ImageView) view.findViewById(R.id.ivMainTabIcon);
		imageView.setImageResource(iconArray[index]);

		TextView textView = (TextView) view.findViewById(R.id.tvMainTabTitle);
		textView.setText(titleArray[index]);

		return view;
	}
}
