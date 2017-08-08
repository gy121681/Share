package com.shareshenghuo.app.user.fragment;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.PointRuleAdapter;
import com.shareshenghuo.app.user.network.response.OtherListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
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
 * 生活币规则
 */
public class PointRuleFragment extends Fragment {
	
	private ListView lvData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_point_rule, container, false);
		lvData = (ListView) rootView.findViewById(R.id.lvRule);
		loadData();
		return rootView;
	}
	
	public void loadData() {
		new HttpUtils().send(HttpMethod.GET, Api.URL_POINT_RULE, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				OtherListResponse bean = new Gson().fromJson(resp.result, OtherListResponse.class);
				if(Api.SUCCEED==bean.result_code && getActivity()!=null)
					lvData.setAdapter(new PointRuleAdapter(getActivity(), bean.data));
			}
		});
	}
}
