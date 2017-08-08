package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.AreaListAdapter;
import com.shareshenghuo.app.user.network.bean.AreaInfo;
import com.shareshenghuo.app.user.network.request.AreaListRequest;
import com.shareshenghuo.app.user.network.response.AreaListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @author hang
 * 区域选择列表
 */
public class AreaListActivity extends BaseTopActivity implements OnItemClickListener {
	
	private ListView lvData;
	
	private int cityId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_area_list);
		init();
		loadData();
	}
	
	public void init() {
		cityId = getIntent().getIntExtra("cityId", 0);
		
		initTopBar("区域选择");
		lvData = getView(R.id.lvArea);
		lvData.setOnItemClickListener(this);
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		AreaListRequest req = new AreaListRequest();
		req.city_id = cityId+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_AREA_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(AreaListActivity.this);
			}
	
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				final AreaListResponse bean = new Gson().fromJson(resp.result, AreaListResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					if(bean.data==null || bean.data.size()<=0) {
						T.showShort(AreaListActivity.this, "暂无可选区域");
						return;
					}
					lvData.setAdapter(new AreaListAdapter(AreaListActivity.this, bean.data));
				} else {
					T.showShort(AreaListActivity.this, bean.result_desc);
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
		AreaInfo item = (AreaInfo) adapterView.getItemAtPosition(position);
		Intent data = new Intent();
		data.putExtra("areaId", item.id);
		data.putExtra("areaName", item.area_name);
		setResult(RESULT_OK, data);
		finish();
	}
}
