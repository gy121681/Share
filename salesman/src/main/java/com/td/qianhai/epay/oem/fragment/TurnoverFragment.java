package com.td.qianhai.epay.oem.fragment;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.td.qianhai.epay.oem.BusinessCenterActivity;
import com.td.qianhai.epay.oem.CotyledonActivity;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.BusinessCenterActivity.OnArticleSelectedListener;
import com.td.qianhai.epay.oem.adapter.TurnoverAdapter;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.TurnoverBean;
import com.td.qianhai.epay.oem.beans.TurnoverRequest;
import com.td.qianhai.epay.oem.beans.TurnoverResponse;
import com.td.qianhai.epay.oem.views.PullToRefreshLayout;
import com.td.qianhai.epay.oem.views.PullToRefreshLayout.OnRefreshListener;
import com.td.qianhai.epay.oem.views.PullableListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class TurnoverFragment extends BaseFragment implements OnArticleSelectedListener{
	
	private View view;
	private PullableListView listview;
	private PullToRefreshLayout refresh_view;
	private TurnoverAdapter adapter;
	public  int pageNo = 1;
	private int pageSize = 10;
	private String is_search = "0";
	private List<TurnoverBean> data;
	public static  OnArticleSelectedListener mListener;
	public static  TurnoverFragment contxt;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
//			view = inflater.inflate(R.layout.turnover_list_fragment, container,false);
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
		view = inflater.inflate(R.layout.turnover_list_fragment, null, false);
//		mListener = ((BusinessCenterActivity)getActivity()).mListener;
		contxt = this;
		initview();
		loadData();
//		mListener = new OnArticleSelectedListener() {
//			
//			@Override
//			public void onArticleSelected(int tag) {
//				// TODO Auto-generated method stub
//				pageNo = 1;
//				loadData();
//			}
//		};
	}
	
    @Override
     public void onActivityCreated(Bundle savedInstanceState) {
    		super.onActivityCreated(savedInstanceState);
     }
	
	private void initdata() {
		// TODO Auto-generated method stub
		List<TurnoverBean> data  = new ArrayList<TurnoverBean>();
		for (int i = 0; i < 5; i++) {
			TurnoverBean b = new TurnoverBean();
			data.add(b);
		}
		updateView(data);
		
	}
	private void initview() {
		// TODO Auto-generated method stub
		listview = (PullableListView) view.findViewById(R.id.listview);
		refresh_view = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
		refresh_view.setOnRefreshListener(new OnRefreshListener() {
			
			
			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				//refresh_view.loadmoreFinish(0);//wyp修改,隐藏了
				loadData();
			}

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				pageNo = 1;
				loadData();
//				refresh_view.refreshFinish(0);
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(data!=null){
					if(!data.get((int)arg3).area_type.equals("3")){
						Intent it = new Intent(getActivity(),CotyledonActivity.class);
						it.putExtra("area_type", data.get((int)arg3).area_type);
						it.putExtra("area_code", data.get((int)arg3).area_code);
						it.putExtra("trade_date", data.get((int)arg3).trade_date);
						startActivity(it);
					}
				}
			}
		});
	}
	
	public void loadData() {
		TurnoverRequest req = new TurnoverRequest();
		req.page_no = pageNo + "";
		req.page_size = pageSize + "";
		req.is_search = is_search;
		req.area_type = ((BusinessCenterActivity)getActivity()).area_type;
		req.area_code = ((BusinessCenterActivity)getActivity()).area_id;
		req.city_code = ((BusinessCenterActivity)getActivity()).city_id;
		req.province_code =  ((BusinessCenterActivity)getActivity()).province_id;
		req.trade_date_begin = "";
		req.trade_date_end = "";
		Log.e("", ""+req.toString());
		System.out.println("=====营业额请求:"+req.toString());
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.FINDAGENTTRADE, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						refresh_view.refreshFinish(0);
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						refresh_view.refreshFinish(0);
						Log.e("", " - - - -  " + resp.result);
						TurnoverResponse bean = new Gson().fromJson(
								resp.result, TurnoverResponse.class);
						if (0 == bean.result_code)
						updateView(bean.data);
						data = bean.data;
					}
				});

	}

	public void updateView(List<TurnoverBean> data) {
		if (pageNo == 1 || adapter == null) {
			adapter = new TurnoverAdapter(getActivity(), data);
			listview.setAdapter(adapter);
		}
		if (pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}
	@Override
	public void onArticleSelected(int tag) {
		// TODO Auto-generated method stub
		
		Log.e("", " = = =   "+tag);
	}
}
