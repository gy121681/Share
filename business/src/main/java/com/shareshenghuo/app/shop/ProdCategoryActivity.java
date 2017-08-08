package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.ProdCategoryAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.ProdTypeInfo;
import com.shareshenghuo.app.shop.network.request.ShopRequest;
import com.shareshenghuo.app.shop.network.response.ProdTypeResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;

public class ProdCategoryActivity extends BaseTopActivity implements OnItemClickListener {
	
	private ListView lvData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prod_category);
		init();
		loadProdType();
	}
	
	public void init() {
		initTopBar("商品分类");
		lvData = getView(R.id.lvData);
		lvData.setOnItemClickListener(this);
	}
	
	public void loadProdType() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		ShopRequest req = new ShopRequest();
		req.shop_id = UserInfoManager.getUserInfo(this).shop_id+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_PROD_TYPE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ProdCategoryActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				ProdTypeResponse bean = new Gson().fromJson(resp.result, ProdTypeResponse.class);
				if(Api.SUCCEED==bean.result_code) {
					lvData.setAdapter(new ProdCategoryAdapter(ProdCategoryActivity.this, bean.data));
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
		ProdTypeInfo item = (ProdTypeInfo) adapterView.getItemAtPosition(position);
		Intent data = new Intent();
		data.putExtra("prodTypeId", item.id);
		data.putExtra("prodTypeName", item.type_name);
		setResult(RESULT_OK, data);
		finish();
	}
}
