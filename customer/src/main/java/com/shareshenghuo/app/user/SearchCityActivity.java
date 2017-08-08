package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.CityListAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.CityInfo;
import com.shareshenghuo.app.user.network.request.SearchRequest;
import com.shareshenghuo.app.user.network.response.SearchCityResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.ViewUtil;

public class SearchCityActivity extends BaseTopActivity implements OnClickListener, OnItemClickListener {
	
	private EditText edKey;
	private ListView lvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_city);
		initView();
	}
	
	public void initView() {
		initTopBar("城市选择");
		edKey = getView(R.id.edSearchKeyWord);
		lvResult = getView(R.id.lvSearchResult);
		
		findViewById(R.id.btnSearch).setOnClickListener(this);
		lvResult.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnSearch:
			if(ViewUtil.checkEditEmpty(edKey, "请输入关键字"))
				return;
			search(edKey.getText().toString());
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
		CityInfo info = (CityInfo) parent.getAdapter().getItem(position);
		String user = UserInfoManager.getHISTORY(SearchCityActivity.this);
		StringBuffer buff = new StringBuffer();
		buff.append(info.city_name+","+info.id+";");
		if(user!=null&&user.length()>0){
			String[] name = user.split(";");
			
			if(name.length<CityListActivity.num){
				for (int i = 0; i < name.length; i++) {
					String [] city = name[i].split(",");
					buff.append(city[0]+","+city[1]+";");
				}
			}else if(name.length==CityListActivity.num){
				for (int i = 0; i < name.length-1; i++) {
					String [] city = name[i].split(",");
					buff.append(city[0]+","+city[1]+";");
				}
			}
		}
		UserInfoManager.setHISTORY(SearchCityActivity.this, buff.toString());
		Intent data = new Intent();
		data.putExtra("cityInfo", info);
		setResult(RESULT_OK, data);
		finish();
	}
	
	public void search(String keyword) {
		ProgressDialogUtil.showProgressDlg(this, "搜索");
		SearchRequest req = new SearchRequest();
		req.search_name = keyword;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_SEARCH_CITY, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(SearchCityActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				SearchCityResponse bean = new Gson().fromJson(resp.result, SearchCityResponse.class);
				if(Api.SUCCEED == bean.result_code)
					lvResult.setAdapter(new CityListAdapter(SearchCityActivity.this, bean.data));
			}
		});
	}
}
