package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.PickProdAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.UserInfo;
import com.shareshenghuo.app.shop.network.request.ProdListRequest;
import com.shareshenghuo.app.shop.network.response.ProdListResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;

public class PickProdActivity extends BaseTopActivity implements OnClickListener {

	private PickProdAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_prod);
		loadData();
		getView(R.id.btnOK).setOnClickListener(this);
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		ProdListRequest req = new ProdListRequest();
		req.page_no = "1";
		req.page_size = "100";
		UserInfo userInfo = UserInfoManager.getUserInfo(this);
		req.shop_id = userInfo.shop_id+"";
		req.city_id = userInfo.city_id+"";
		req.product_type_id = getIntent().getIntExtra("typeId", 0)+"";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_PROD_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(PickProdActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				ProdListResponse bean = new Gson().fromJson(resp.result, ProdListResponse.class);
				if(Api.SUCCEED == bean.result_code && bean.data.size()>0) {
					adapter = new PickProdAdapter(PickProdActivity.this, bean.data);
					((ListView) findViewById(R.id.lvData)).setAdapter(adapter);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnOK:
			if(adapter==null) {
				T.showShort(this, "该分类商品为空");
				return;
			}
			String prodName = adapter.getSelectedName();
			if(TextUtils.isEmpty(prodName)) {
				T.showShort(this, "请选择商品");
				return;
			}
			
			Intent data = new Intent();
			data.putExtra("prodName", prodName);
			setResult(RESULT_OK, data);
			finish();
			break;
		}
	}
}
