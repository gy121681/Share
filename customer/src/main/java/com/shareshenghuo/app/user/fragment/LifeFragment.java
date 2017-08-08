package com.shareshenghuo.app.user.fragment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shareshenghuo.app.user.AddArticleActivity;
import com.shareshenghuo.app.user.CircleDetailActivity;
import com.shareshenghuo.app.user.CircleMyActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.SearchArticleActivity;
import com.shareshenghuo.app.user.adapter.CircleGridAdapter;
import com.shareshenghuo.app.user.adapter.LifeListAdapter;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.network.bean.ArticleInfo;
import com.shareshenghuo.app.user.network.bean.CircleInfo;
import com.shareshenghuo.app.user.network.request.ArticleListRequest;
import com.shareshenghuo.app.user.network.response.ArticleListResponse;
import com.shareshenghuo.app.user.network.response.CircleListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.MyGridView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author hang
 * 生活圈
 */
public class LifeFragment extends Fragment 
	implements OnClickListener, OnRefreshListener2<ListView>, OnItemClickListener {
	
	private static final int REQ_ADD_ARTICLE = 0x100;

	private MyGridView gvCircle;
	private TextView tvSwitchCircle;
	private ImageView ivMyCircle;
	private PullToRefreshListView lvData;
	
	private LifeListAdapter adapter;
	private int pageNo;
	private int pageSize;
	
	private int circleIndex; //圈子分页页码 
	
	private View rootView;
	
	private Activity activity;
	
//	@Override
//	protected int getLayoutId() {
//		return R.layout.fragment_life;
//	}
//
//	@Override
//	protected void init(View rootView) {
//		initView();
//		loadData();
//		getCircleList();
//	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = getActivity();
		rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_life, null);
		initView();
		loadData();
		getCircleList();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		
		if (rootView == null) {
			rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_life, null);
		}

		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}
	
	public void initView() {
		gvCircle = (MyGridView) rootView.findViewById(R.id.gvCircle);
		tvSwitchCircle =  (TextView) rootView.findViewById(R.id.tvSwitchCircle);
		ivMyCircle =  (ImageView) rootView.findViewById(R.id.ivMyCircle);
		
		lvData = (PullToRefreshListView) rootView.findViewById(R.id.lvLife);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		
		rootView.findViewById(R.id.ivAddArticle).setOnClickListener(this);
		rootView.findViewById(R.id.btnSearch).setOnClickListener(this);
		tvSwitchCircle.setOnClickListener(this);
		ivMyCircle.setOnClickListener(this);

		gvCircle.setOnItemClickListener(this);
		
		pageNo = 1;
		pageSize = 10;
		circleIndex = 1;
	}
	
	public void getCircleList() {
		ProgressDialogUtil.showProgressDlg(activity, "加载数据");
		ArticleListRequest req = new ArticleListRequest();
		req.page_no = circleIndex+"";
		req.page_size = "3";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_CIRCLE_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				CircleListResponse bean = new Gson().fromJson(resp.result, CircleListResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					gvCircle.setAdapter(new CircleGridAdapter(activity, bean.data));
				} else {
					T.showShort(activity, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(activity);
			}
		});
	}
	
	public void loadData() {
		ArticleListRequest req = new ArticleListRequest();
		req.city_id = CityManager.getInstance(getActivity()).getCityId()+"";
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
				lvData.onRefreshComplete();
				if(getActivity() != null)
					T.showNetworkError(getActivity());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				ArticleListResponse bean = new Gson().fromJson(resp.result, ArticleListResponse.class);
				if(Api.SUCCEED == bean.result_code && getActivity()!=null) {
					updateView(bean.data);
				}
			}
		});
	}
	
	public void updateView(List<ArticleInfo> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new LifeListAdapter(getActivity(), data);
			lvData.setAdapter(adapter);
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.ivAddArticle:
			startActivityForResult(new Intent(getActivity(), AddArticleActivity.class), REQ_ADD_ARTICLE);
			break;
			
		case R.id.btnSearch:
			startActivity(new Intent(getActivity(), SearchArticleActivity.class));
			break;
			
		case R.id.tvSwitchCircle:
			circleIndex++;
			getCircleList();
			break;
			
		case R.id.ivMyCircle:
			startActivity(new Intent(activity, CircleMyActivity.class));
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNo = 1;
		loadData();
		circleIndex = 1;
		getCircleList();
	
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_OK && requestCode==REQ_ADD_ARTICLE)
			onPullDownToRefresh(lvData);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
		CircleInfo item = (CircleInfo) adapterView.getItemAtPosition(position);
		Intent it = new Intent(activity, CircleDetailActivity.class);
		it.putExtra("id", item.id);
		startActivity(it);
	}
}
