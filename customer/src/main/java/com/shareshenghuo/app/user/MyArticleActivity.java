package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.LifeListAdapter;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.ArticleInfo;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.network.request.ArticleListRequest;
import com.shareshenghuo.app.user.network.response.ArticleListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.Utility;

/**
 * @author hang
 * 我发布的文章
 */
public class MyArticleActivity extends BaseTopActivity implements OnRefreshListener2<ScrollView> {
	
	private PullToRefreshScrollView scrollView;
	private ListView lvData;
	
	private int pageNo = 1;
	private int pageSize = 10;
	private LifeListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_article);
		initView();
		loadData();
	}
	
	public void initView() {
		findViewById(R.id.ivBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		scrollView = getView(R.id.svMyArticle);
		lvData = (ListView) findViewById(R.id.lvData);
		
		UserInfo userInfo = UserInfoManager.getUserInfo(this);
		setImageByURL(R.id.ivUserAvatar, userInfo.user_photo);
		setText(R.id.tvUserName, userInfo.nick_name);
		
		scrollView.setMode(Mode.PULL_FROM_END);
		scrollView.setOnRefreshListener(this);
	}
	
	public void loadData() {
		ArticleListRequest req = new ArticleListRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.city_id = CityManager.getInstance(this).getCityId()+"";
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_ARTICLE_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				scrollView.onRefreshComplete();
				T.showNetworkError(MyArticleActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				scrollView.onRefreshComplete();
				ArticleListResponse bean = new Gson().fromJson(resp.result, ArticleListResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					updateView(bean.data);
					
					if(bean.data.size() > 0)
						setText(R.id.tvArticleCount, "已发表文章："+bean.data.get(0).life_circle_count+"篇");
				}
			}
		});
	}
	
	public void updateView(List<ArticleInfo> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new LifeListAdapter(this, data);
			lvData.setAdapter(adapter);
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
		Utility.setListViewHeightBasedOnChildren(lvData);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		loadData();
	}
}
