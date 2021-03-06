package com.shareshenghuo.app.user.fragment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.WithdrawLogAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.WithdrawLogInfo;
import com.shareshenghuo.app.user.network.request.WithdrawLogRequest;
import com.shareshenghuo.app.user.network.response.WithdrawLogResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * @author hang
 *	提现记录
 */
public class WithdrawLogFragment extends Fragment implements OnRefreshListener2<ListView> {
	
	private PullToRefreshListView lvData;
	
	private WithdrawLogAdapter adapter;
	
	private int pageNo = 1;
	private int pageSize = 10;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_listview, container, false);
		initView(rootView);
		return rootView;
	}
	
	public void initView(View root) {
		lvData = (PullToRefreshListView) root.findViewById(R.id.lvShop);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		onPullDownToRefresh(lvData);
	}
	
	public void loadData() {
		WithdrawLogRequest req = new WithdrawLogRequest();
		req.user_id = UserInfoManager.getUserId(getActivity())+"";
		req.user_type = "1";
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_WITHDRAW_LOG, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				WithdrawLogResponse bean = new Gson().fromJson(resp.result, WithdrawLogResponse.class);
				if(Api.SUCCEED == bean.result_code)
					updateView(bean.data);
			}
		});
	}
	
	public void updateView(List<WithdrawLogInfo> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new WithdrawLogAdapter(getActivity(), data);
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
