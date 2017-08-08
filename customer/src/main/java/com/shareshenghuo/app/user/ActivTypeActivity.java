package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.ActivTypeAdapter;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.network.bean.CategoryInfo;
import com.shareshenghuo.app.user.network.request.AreaListRequest;
import com.shareshenghuo.app.user.network.response.CategoryResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;

/**
 * @author hang
 * 选择活动类型
 */
public class ActivTypeActivity extends BaseTopActivity implements OnItemClickListener {
	
	private GridView gvType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activ_type);
		initView();
		loadData();
	}
	
	public void initView() {
		initTopBar("选择分类");
		gvType = getView(R.id.gvType);
		gvType.setOnItemClickListener(this);
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		AreaListRequest req = new AreaListRequest();
		req.city_id = CityManager.getInstance(this).cityInfo.id+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_ACTIV_TYPE_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ActivTypeActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				CategoryResponse bean = new Gson().fromJson(resp.result, CategoryResponse.class);
				if(Api.SUCCEED == bean.result_code)
					gvType.setAdapter(new ActivTypeAdapter(ActivTypeActivity.this, bean.data));
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
		CategoryInfo item = (CategoryInfo) adapterView.getItemAtPosition(position);
		Intent data = new Intent();
		data.putExtra("typeId", item.id);
		setResult(RESULT_OK, data);
		finish();
	}
}
