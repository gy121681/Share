package com.td.qianhai.epay.oem.fragment;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.td.qianhai.epay.oem.BusinessCenterActivity;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.ShopNumActivity;
import com.td.qianhai.epay.oem.adapter.BusinessAdapter;
import com.td.qianhai.epay.oem.beans.BusinessBean;
import com.td.qianhai.epay.oem.beans.BusinessResponse;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.TurnoverRequest;
import com.td.qianhai.epay.oem.mail.utils.Utility;
import com.td.qianhai.epay.oem.views.PullToRefreshLayout;
import com.td.qianhai.epay.oem.views.PullToRefreshLayout.OnRefreshListener;

public class BusinessFragment extends BaseFragment {
	
	private View view;
	private ListView listview;
	private PullToRefreshLayout refresh_view;
	private BusinessAdapter adapter;
	public int pageNo = 1;
	private int pageSize = 10;
	private TextView tv_num,tv_province;
	public static BusinessFragment contxt;
	public List<BusinessBean> list;
	
	
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
		contxt = this;
		initview();
		loadData();
	}
	private void initdata() {
		// TODO Auto-generated method stub
		List<BusinessBean> data = new ArrayList<BusinessBean>();
		for (int i = 0; i < 3; i++) {
			BusinessBean b = new BusinessBean();
			data.add(b);
		}
		updateView(data);
	}
	private void initview() {
		listview = (ListView) view.findViewById(R.id.listview);
		tv_num = (TextView) view.findViewById(R.id.tv_num);
		tv_province = (TextView) view.findViewById(R.id.tv_province);
		refresh_view = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
		refresh_view.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				// TODO Auto-generated method stub
				pageNo = 1;
				loadData();
			}
			
			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				// TODO Auto-generated method stub
				refresh_view.loadmoreFinish(0);
				//loadData();
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(list!=null){
					Intent it = new Intent(getActivity(),ShopNumActivity.class);
					it.putExtra("area_type", list.get((int)arg3).area_type);
					it.putExtra("area_code", list.get((int)arg3).area_code);
					it.putExtra("trade", list.get((int)arg3).area_name);
					startActivity(it);
				}
			}
		});
	}
	
	
	
	
public void loadData() {
		tv_province.setText(((BusinessCenterActivity)getActivity()).provinceidname+"商家分布");
		TurnoverRequest req = new TurnoverRequest();
//		req.page_no = pageNo + "";
//		req.page_size = pageSize + "";
		req.area_type = ((BusinessCenterActivity)getActivity()).area_type;
		if(((BusinessCenterActivity)getActivity()).area_type.equals("1")){
			req.area_code = ((BusinessCenterActivity)getActivity()).province_id;
		}else if(((BusinessCenterActivity)getActivity()).area_type.equals("2")){
			req.area_code = ((BusinessCenterActivity)getActivity()).city_id;
		}else if(((BusinessCenterActivity)getActivity()).area_type.equals("3")){
			req.area_code = ((BusinessCenterActivity)getActivity()).area_id;
		}
		System.out.println("=====商家分布查询参数:"+req.toString());
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.FINDAGENTSHOPNUM, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						refresh_view.refreshFinish(0);
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						Log.e("", ""+resp.result);
						refresh_view.refreshFinish(0);
						BusinessResponse bean = new Gson().fromJson(resp.result, BusinessResponse.class);
						if (0 == bean.result_code){
							
							tv_num.setText(bean.data.shop_total_num);
							list = bean.data.list;
							updateView(bean.data.list);
							
						}
					}
				});

	}

	public void updateView(List<BusinessBean> data) {
		if (pageNo == 1 || adapter == null) {
			adapter = new BusinessAdapter(getActivity(), data);
			listview.setAdapter(adapter);
		}
		if (pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
		
		Utility.setListViewHeightBasedOnChildren(listview);
	}
}
