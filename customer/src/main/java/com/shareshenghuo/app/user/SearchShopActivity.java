package com.shareshenghuo.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.ShopInfo;
import com.shareshenghuo.app.user.util.ViewUtil;

/**
 * @author hang
 * 搜索商家
 */
public class SearchShopActivity extends BaseTopActivity implements OnClickListener, OnItemClickListener {
	
	private EditText edKey;
	private ListView lvResult;
	
//	private boolean tag;	// false 列表数据为搜索记录    true 搜索结果 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_city);
		initView();
	}
	
	public void initView() {
		initTopBar("搜索");
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
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long id) {
		ShopInfo item = (ShopInfo) adapterView.getItemAtPosition(position);
		Intent it = new Intent(this, ShopDetailActivity.class);
		it.putExtra("shopId", item.id);
		startActivity(new Intent(it));
	}
	
	public void search(final String keyword) {
//		Intent it = new Intent(SearchShopActivity.this, MainActivity.class);
		MainActivity.name = keyword;
		((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchShopActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		finish();
//		it.putExtra("search_name", keyword);
//		it.putExtra("shop_type_id", item.id);
//		startActivity(it);
		
		
//		ProgressDialogUtil.showProgressDlg(this, "搜索中");
//		SearchShopRequest req = new SearchShopRequest();
//		req.page_no = "1";
//		req.page_size = "100";
//		CityManager cm = CityManager.getInstance(this);
//		req.city_id = cm.cityInfo.id+"";
//		req.latitude = cm.latitude+"";
//		req.longitude = cm.longitude+"";
//		req.search_name = keyword;
//		RequestParams params = new RequestParams("utf-8");
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_SEARCH_SHOP, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(SearchShopActivity.this);
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				ShopListResponse bean = new Gson().fromJson(resp.result, ShopListResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					lvResult.setAdapter(new ShopListAdapter(SearchShopActivity.this, bean.data));
//				}
//			}
//		});
	}
}
