package com.td.qianhai.epay.oem.fragment;


import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.views.PullToRefreshLayout;
import com.td.qianhai.epay.oem.views.PullToRefreshLayout.OnRefreshListener;
import com.td.qianhai.epay.oem.views.PullableListView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FriendFragment extends BaseFragment {
	
	private View view;
	private PullableListView listview;
	private PullToRefreshLayout refresh_view;
	private TextView tv_totnum;
	
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
		view = inflater.inflate(R.layout.business_item, null, false);
		initview();
	}
	private void initview() {
		// TODO Auto-generated method stub
		tv_totnum = (TextView) view.findViewById(R.id.tv_totnum);
		tv_totnum.setText("业务员总数");
//		listview = (PullableListView) view.findViewById(R.id.listview);
		refresh_view = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
		refresh_view.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				// TODO Auto-generated method stub
				refresh_view.refreshFinish(0);
			}
			
			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				// TODO Auto-generated method stub
				refresh_view.loadmoreFinish(0);
			}
		});
	}
}
