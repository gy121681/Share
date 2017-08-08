package com.shareshenghuo.app.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.fragment.IncentivePoints1Fragment;
import com.shareshenghuo.app.user.fragment.IncentivePointsFragment;
import com.shareshenghuo.app.user.fragment.IncentivePointsGYL1Fragment;
import com.shareshenghuo.app.user.fragment.IncentivePointsGYLFragment;
import com.shareshenghuo.app.user.util.Util;
import com.shareshenghuo.app.user.widget.MyTabView;

/**
 * @author hang 秀点
 */
public class IncentivePointsActivity extends BaseTopActivity {

	private MyTabView tabView;
	private String tag;
	private TextView top_title_text;// 中间文字
	private String k_num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_incentivepoints);
		k_num = getIntent().getStringExtra("k_num");
		tag = getIntent().getStringExtra("tag");
		initView();
	}

	public void initView() {
		if (tag != null && tag.equals("1")) {
			initTopBar("产业链可兑换秀点");
		} else {
			initTopBar("秀儿可兑换秀点");
		}
		
		top_title_text = getView(R.id.top_title_text);
		top_title_text.setText("可兑换秀点总额:0");
		top_title_text.setTag("可兑换秀点总额:");
		if (k_num != null) {
			top_title_text.setText(top_title_text.getTag().toString()
					+ Util.getnum(k_num, false) + "");//
		}
		tabView = (MyTabView) findViewById(R.id.tabFavorites);

		List<Map<String, Integer>> titles = new ArrayList<Map<String, Integer>>();
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("激励秀点", null);
		titles.add(map);
		map = new HashMap<String, Integer>();
		map.put("推荐秀点", null);
		titles.add(map);
		// map = new HashMap<String, Integer>();
		// map.put("获赠秀点", null);
		// titles.add(map);

		List<Fragment> fragments = new ArrayList<Fragment>();
		if (tag != null && tag.equals("1")) {
			fragments.add(new IncentivePointsGYLFragment());
			fragments.add(new IncentivePointsGYL1Fragment());
		} else {
			fragments.add(new IncentivePoints1Fragment());
			fragments.add(new IncentivePointsFragment());
		}

		// fragments.add(new IncentivePoints2Fragment());

		tabView.createView(titles, fragments, getSupportFragmentManager());
	}
}
