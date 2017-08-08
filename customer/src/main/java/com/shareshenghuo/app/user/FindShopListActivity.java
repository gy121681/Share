package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.FindShopAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.ShopInfo;
import com.shareshenghuo.app.user.network.request.ArticleLikeRequest;
import com.shareshenghuo.app.user.network.response.ShopListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;

public class FindShopListActivity extends BaseTopActivity implements OnItemClickListener {
	
	private static final int REQ_ADD_SHOP = 0x101;
	
	private ListView lvData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_shop_list);
		initView();
		loadData();
	}
	
	public void initView() {
		initTopBar("我发现的商家");
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("新增");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(FindShopListActivity.this, EditShopActivity.class), REQ_ADD_SHOP);
			}
		});
		
		lvData = (ListView) findViewById(R.id.lvData);
		lvData.setOnItemClickListener(this);
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "");
		ArticleLikeRequest req = new ArticleLikeRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_FIND_SHOP_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(FindShopListActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				ShopListResponse bean = new Gson().fromJson(resp.result, ShopListResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					lvData.setAdapter(new FindShopAdapter(FindShopListActivity.this, bean.data));
				} else {
					T.showShort(FindShopListActivity.this, bean.result_desc);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		if(resCode==RESULT_OK && reqCode==REQ_ADD_SHOP)
			loadData();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
		ShopInfo item = (ShopInfo) adapterView.getItemAtPosition(position);
		Intent it = new Intent(this, ShopInfoActivity.class);
		it.putExtra("shopInfo", item);
		startActivity(it);
	}
}
