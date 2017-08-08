package com.shareshenghuo.app.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.fragment.FavorityActivFragment;
import com.shareshenghuo.app.user.fragment.FavorityShopFragment;
import com.shareshenghuo.app.user.widget.MyTabView;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @author hang
 * 收藏夹
 */
public class FavoritesActivity extends BaseTopActivity {
	
	private MyTabView tabView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);
		initView();
	}
	
	public void initView() {
		initTopBar("我的收藏");
		tabView = (MyTabView) findViewById(R.id.tabFavorites);
		
		List<Map<String,Integer>> titles = new ArrayList<Map<String,Integer>>();
		Map<String,Integer> map = new HashMap<String, Integer>();
		map.put("收藏的商家", null);
		titles.add(map);
		map = new HashMap<String, Integer>();
		map.put("收藏的优惠活动", null);
		titles.add(map);
		
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(new FavorityShopFragment());
		fragments.add(new FavorityActivFragment());
		
		tabView.createView(titles, fragments, getSupportFragmentManager());
	}
}
