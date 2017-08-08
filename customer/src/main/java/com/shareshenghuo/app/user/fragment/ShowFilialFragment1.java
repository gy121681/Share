package com.shareshenghuo.app.user.fragment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.ExcitationAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.FractionBean;
import com.shareshenghuo.app.user.network.request.FractionRequest;
import com.shareshenghuo.app.user.network.response.FractionResponse;
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

public class ShowFilialFragment1 extends BaseFragment implements
		OnRefreshListener2<ListView> {

	private PullToRefreshListView lvData;
	private int pageNo = 1;
	private int pageSize = 10;
	private ExcitationAdapter adapter;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.newintegral_activity;
	}

	@Override
	protected void init(View rootView) {
		// TODO Auto-generated method stub
		lvData = getView(R.id.lvShop);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		pageNo = 1;
		loadData();
	}

	public void loadData() {

		FractionRequest req = new FractionRequest();
		req.userId = UserInfoManager.getUserInfo(getActivity()).id + "";
		req.queryType = "1";
		req.type = "";
		req.userType = "1";
		req.startDate = "";
		req.endDate = "";
		req.pageNo = pageNo + "";
		req.pageSize = pageSize + "";

		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.NEWFRACTIONLIST, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						lvData.onRefreshComplete();
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						lvData.onRefreshComplete();
						FractionResponse bean = new Gson().fromJson(
								resp.result, FractionResponse.class);
						if (Api.SUCCEED == bean.result_code)
							Log.e("", " - - - -  " + resp.result.toString());
						updateView(bean.data);
					}
				});

	}

	public void updateView(List<FractionBean> data) {
		if (pageNo == 1 || adapter == null) {
			adapter = new ExcitationAdapter(getActivity(), data);
			lvData.setAdapter(adapter);
		}
		if (pageNo > 1) {
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