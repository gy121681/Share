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
import com.shareshenghuo.app.user.adapter.SearchArticleResultAdapter;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.network.bean.ArticleInfo;
import com.shareshenghuo.app.user.network.request.SearchArticleRequest;
import com.shareshenghuo.app.user.network.response.ArticleListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.ViewUtil;

/**
 * @author hang
 * 生活圈搜索
 */
public class SearchArticleActivity extends BaseTopActivity implements OnClickListener, OnItemClickListener {
	
	private EditText edKey;
	private ListView lvResult;

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
		ArticleInfo item = (ArticleInfo) adapterView.getItemAtPosition(position);
		Intent it = new Intent(this, ArticleDetailActivity.class);
		it.putExtra("articleInfo", item);
		it.putExtra("articleId", item.id);
		startActivity(new Intent(it));
	}
	
	public void search(final String keyword) {
		ProgressDialogUtil.showProgressDlg(this, "搜索中");
		SearchArticleRequest req = new SearchArticleRequest();
		req.page_no = "1";
		req.page_size = "100";
		req.city_id = CityManager.getInstance(this).getCityId()+"";
		req.serach_title = keyword;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_SEARCH_ARTICLE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(SearchArticleActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				ArticleListResponse bean = new Gson().fromJson(resp.result, ArticleListResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					lvResult.setAdapter(new SearchArticleResultAdapter(SearchArticleActivity.this, bean.data));
				}
			}
		});
	}
}
