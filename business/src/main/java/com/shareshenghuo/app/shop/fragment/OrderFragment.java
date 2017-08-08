package com.shareshenghuo.app.shop.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.SearchOrderActivity;
import com.shareshenghuo.app.shop.widget.MyTabView;

public class OrderFragment extends BaseFragment implements OnClickListener {
	
	private TextView tvTitle;
	private MyTabView tabView;
	
	private List<Fragment> fragments;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_order;
	}

	@Override
	protected void init(View rootView) {
		tvTitle = getView(R.id.tvTopTitle);
		tabView = getView(R.id.tabOrderManage);
		
		List<Map<String,Integer>> titles = new ArrayList<Map<String,Integer>>();
		Map<String,Integer> map = new HashMap<String, Integer>();
		map.put("全部", null);
		titles.add(map);
		map = new HashMap<String, Integer>();
		map.put("待接单", null);
		titles.add(map);
		map = new HashMap<String, Integer>();
		map.put("进行中", null);
		titles.add(map);
		map = new HashMap<String, Integer>();
		map.put("已结束", null);
		titles.add(map);
		
		fragments = new ArrayList<Fragment>();
		fragments.add(new OrderListFragment());
		fragments.add(new OrderListFragment());
		fragments.add(new OrderListFragment());
		fragments.add(new OrderListFragment());
		for(int i=0; i<4; i++)
			((OrderListFragment) fragments.get(i)).status = i;
		
		tabView.createView(titles, fragments, getChildFragmentManager());
		
		tvTitle.setOnClickListener(this);
		getView(R.id.llTopSearch).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.llTopSearch:
			startActivity(new Intent(activity, SearchOrderActivity.class));
			break;
			
		case R.id.tvTopTitle:
			showOrderTypeDlg();
			break;
		}
	}
	
	private String[] items = {"全部订单", "外卖订单", "到店消费"};
	public void showOrderTypeDlg() {
		new AlertDialog.Builder(activity).setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int which) {
				tvTitle.setText(items[which]);
				for(int i=0; i<4; i++) { 
					OrderListFragment f = (OrderListFragment) fragments.get(i);
					f.order_type = which;
					f.onPullDownToRefresh(null);
				}
			}
		})
		.show();
	}
}
