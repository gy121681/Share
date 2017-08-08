package com.shareshenghuo.app.shop.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shareshenghuo.app.shop.BaseTopActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.widget.MyTabView;
import com.shareshenghuo.app.shop.widget.MyTabView.PageChangeListener;

public class DobusinessFragmentItem1 extends Fragment{
	
	private MyTabView tabView;
	private TextView tv_title;
	private TextView tv_choosedate;
	private View view;
	
	
//	@Override
//	protected int getLayoutId() {
//		// TODO Auto-generated method stub
//		return R.layout.dobusiness_fragment_item1;
//	}
//
//	@Override
//	protected void init(View rootView) {
//		
//        ViewGroup parent = (ViewGroup) rootView.getParent();  
//        if (parent != null) {  
//            parent.removeView(rootView);  
//        }   
//		
//		initview();
//	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
             parent.removeView(view);
		return view;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.dobusiness_fragment_item1, null, false);
		initview();
	}
	
//	
//	@Override
//	protected void onCreate(Bundle arg0) {
//		// TODO Auto-generated method stub
//		super.onCreate(arg0);
//		setContentView(R.layout.dobusiness_fragment_item1);
//		
//		initview();
//	}

	private void initview() {
		// TODO Auto-generated method stub
		
		
		tabView = (MyTabView) view.findViewById(R.id.tabFavorites);
		tv_choosedate = (TextView) view.findViewById(R.id.tv_choosedate);
		tv_choosedate.setVisibility(View.GONE);
		List<Map<String,Integer>> titles = new ArrayList<Map<String,Integer>>();
		Map<String,Integer> map = new HashMap<String, Integer>();
		map.put("今日营业额",null);
//		map.put("激励积分", null);
		titles.add(map);
		map = new HashMap<String, Integer>();
		map.put("历史营业额",null);
//		map.put("共享积分", null);
		titles.add(map);
		
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(new TodaydataFragment());
		fragments.add(new HistoryDataFragment());
//		fragments.add(new IncentivePoints2Fragment());
		
		tabView.createView(titles, fragments, getActivity().getSupportFragmentManager());
		tabView.setPageChangeListener(new PageChangeListener() {
			
			@Override
			public void onPageChanged(int index, String tabTitle) {
				// TODO Auto-generated method stub
				if(index==1){
					tv_choosedate.setVisibility(View.VISIBLE);
				}else{
					tv_choosedate.setVisibility(View.GONE);
				}
			}
		});
		
		tv_choosedate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				HistoryDataFragment.context.setdate();
			}
		});
		
	}

}
