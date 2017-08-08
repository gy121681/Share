package com.shareshenghuo.app.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.fragment.Myconsumption1Fragment;
import com.shareshenghuo.app.user.widget.MyTabView;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @author hang
 * 我的消费
 */
public class ConsumptionActivity extends BaseTopActivity {
	
	private MyTabView tabView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filial);
		initView();
	}
	
	public void initView() {
		initTopBar("我的消费");
		tabView = (MyTabView) findViewById(R.id.tabFavorites);
		
		List<Map<String,Integer>> titles = new ArrayList<Map<String,Integer>>();
		Map<String,Integer> map = new HashMap<String, Integer>();
		map.put("%5激励", null);
		titles.add(map);
		map = new HashMap<String, Integer>();
		map.put("25%激励", null);
		titles.add(map);
		
		List<Fragment> fragments = new ArrayList<Fragment>();
//		fragments.add(new MyConsumptionFragment());
		fragments.add(new Myconsumption1Fragment());
		
		tabView.createView(titles, fragments, getSupportFragmentManager());
	}
}
