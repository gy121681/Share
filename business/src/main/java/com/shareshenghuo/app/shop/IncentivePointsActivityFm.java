package com.shareshenghuo.app.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shareshenghuo.app.shop.fragment.BaseFragment1;
import com.shareshenghuo.app.shop.fragment.IncentivePoints1Fragment;
import com.shareshenghuo.app.shop.fragment.IncentivePoints2Fragment;
import com.shareshenghuo.app.shop.fragment.IncentivePointsFragment;
import com.shareshenghuo.app.shop.widget.MyTabView;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * @author hang
 * 秀点
 */
public class IncentivePointsActivityFm extends BaseFragment1 {
	
	private MyTabView tabView;
	
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_incentivepoints1;
	}
	
	@Override
	protected void init(View rootView) {
		initView();
	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_incentivepoints);
//		initView();
//	}
	
	public void initView() {
//		initTopBar("可兑换秀点");
		tabView =getView(R.id.tabFavorites);
		
		List<Map<String,Integer>> titles = new ArrayList<Map<String,Integer>>();
		Map<String,Integer> map = new HashMap<String, Integer>();
		map.put("激励秀点", null);
		titles.add(map);
		map = new HashMap<String, Integer>();
		map.put("推荐秀点", null);
		titles.add(map);
//		map = new HashMap<String, Integer>();
//		map.put("获赠秀点", null);
//		titles.add(map);
		
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(new IncentivePoints1Fragment());
		fragments.add(new IncentivePointsFragment());
//		fragments.add(new IncentivePoints2Fragment());
		
		tabView.createView(titles, fragments, getActivity().getSupportFragmentManager());
	}
}
