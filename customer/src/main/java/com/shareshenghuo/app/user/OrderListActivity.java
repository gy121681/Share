//package com.shareshenghuo.app.user;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//
//import com.shareshenghuo.app.user.R;
//import com.shareshenghuo.app.user.fragment.OrderListFragment;
//import MyTabView;
//
//public class OrderListActivity extends BaseTopActivity {
//	
//	private MyTabView tabView;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_order_list);
//		initView();
//	}
//	
//	public void initView() {
//		initTopBar("我的订单");
//		tabView = (MyTabView) findViewById(R.id.tabOrder);
//		
//		List<Map<String,Integer>> titles = new ArrayList<Map<String,Integer>>();
//		Map<String,Integer> map = new HashMap<String, Integer>();
//		map.put("全部", null);
//		titles.add(map);
//		map = new HashMap<String, Integer>();
//		map.put("待付款", null);
//		titles.add(map);
//		map = new HashMap<String, Integer>();
//		map.put("待收货", null);
//		titles.add(map);
//		map = new HashMap<String, Integer>();
//		map.put("待评价", null);
//		titles.add(map);
//		map = new HashMap<String, Integer>();
//		map.put("退款", null);
//		titles.add(map);
//		
//		List<Fragment> fragments = new ArrayList<Fragment>();
//		OrderListFragment f1 = new OrderListFragment();
//		f1.status = 0;
//		fragments.add(f1);
//		OrderListFragment f2 = new OrderListFragment();
//		f2.status = 1;
//		fragments.add(f2);
//		OrderListFragment f3 = new OrderListFragment();
//		f3.status = 2;
//		fragments.add(f3);
//		OrderListFragment f4 = new OrderListFragment();
//		f4.status = 3;
//		fragments.add(f4);
//		OrderListFragment f5 = new OrderListFragment();
//		f5.status = 5;
//		fragments.add(f5);
//		
//		tabView.createView(titles, fragments, getSupportFragmentManager());
//		tabView.showPageContent(getIntent().getIntExtra("status", 0));
//	}
//}
