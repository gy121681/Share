//package com.shareshenghuo.app.user;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.shareshenghuo.app.user.fragment.WalletRechargeLogFragment;
//import com.shareshenghuo.app.user.fragment.WithdrawLogFragment;
//import MyTabView;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//
//public class WalletDetailActivity extends BaseTopActivity {
//	
//	private MyTabView tabView;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_wallet_detail);
//		init();
//	}
//	
//	public void init() {
//		initTopBar("余额明细");
//		
//		tabView = getView(R.id.tabWallet);
//		
//		List<Map<String,Integer>> titles = new ArrayList<Map<String,Integer>>();
//		Map<String,Integer> map = new HashMap<String, Integer>();
//		map.put("充值记录", null);
//		titles.add(map);
//		map = new HashMap<String, Integer>();
//		map.put("消费记录", null);
//		titles.add(map);
//		map = new HashMap<String, Integer>();
//		map.put("提现记录", null);
//		titles.add(map);
//		
//		List<Fragment> fragments = new ArrayList<Fragment>();
//		WalletRechargeLogFragment f1 = new WalletRechargeLogFragment();
//		f1.type = 2;
//		fragments.add(f1);
//		WalletRechargeLogFragment f2 = new WalletRechargeLogFragment();
//		f2.type = 1;
//		fragments.add(f2);
//		fragments.add(new WithdrawLogFragment());
//		tabView.createView(titles, fragments, getSupportFragmentManager());
//	}
//}
