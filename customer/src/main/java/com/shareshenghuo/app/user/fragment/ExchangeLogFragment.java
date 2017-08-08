package com.shareshenghuo.app.user.fragment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.ExchangeLogAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.ExchangeInfo;
import com.shareshenghuo.app.user.network.request.RechargeLogRequest;
import com.shareshenghuo.app.user.network.response.ExchangeLogResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author hang
 * 生活币兑换记录
 */
public class ExchangeLogFragment extends Fragment implements OnRefreshListener2<ListView> {

	private PullToRefreshListView lvData;
	private TextView tvTotalPoint;
	
	private ExchangeLogAdapter adapter;
	
	private int pageNo = 1;
	private int pageSize = 10;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_exchange_log, container, false);
		initView(rootView);
		return rootView;
	}
	
	public void initView(View root) {
		lvData = (PullToRefreshListView) root.findViewById(R.id.lvEchangeLog);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		
		tvTotalPoint = (TextView) root.findViewById(R.id.tvExchangeTotalPoint);
		
		loadData();
	}
	
	public void loadData() {
		RechargeLogRequest req = new RechargeLogRequest();
		req.user_id = UserInfoManager.getUserId(getActivity())+"";
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_EXCHANGE_LOG, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				ExchangeLogResponse bean = new Gson().fromJson(resp.result, ExchangeLogResponse.class);
				if(Api.SUCCEED == bean.result_code && getActivity()!=null) {
					updateView(bean.data);
					
					if(bean.data.size() > 0)
						tvTotalPoint.setText("消费总额："+bean.data.get(0).all_point+"币");
				}
			}
		});
	}
	
	public void updateView(List<ExchangeInfo> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new ExchangeLogAdapter(getActivity(), data);
			lvData.setAdapter(adapter);
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData();
	}
}
