package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.ArticleCommentsAdapter;
import com.shareshenghuo.app.user.manager.ImageLoadManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.ArticleInfo;
import com.shareshenghuo.app.user.network.request.ArticleLikeRequest;
import com.shareshenghuo.app.user.network.response.ArticleDetailResponse;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.network.response.ArticleDetailResponse.ArticleDetail;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.Arith;
import com.shareshenghuo.app.user.util.BitmapTool;
import com.shareshenghuo.app.user.util.DateUtil;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.Utility;

public class ArticleDetailActivity extends BaseTopActivity implements OnClickListener, OnItemClickListener {
	
	private ArticleInfo articleInfo;
	
	private LinearLayout llPhoto;
	private ListView lvComment;
	private CheckBox cbLike;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_detail);
		initView();
		loadData();
	}
	
	public void initView() {
		articleInfo = (ArticleInfo) getIntent().getSerializableExtra("articleInfo");
		
		initTopBar("详情");
		llPhoto = getView(R.id.llArticlePhoto);
		lvComment = getView(R.id.lvArticleComment);
		cbLike = getView(R.id.cbArticleLike);
		
		cbLike.setOnClickListener(this);
		findViewById(R.id.tvArticleComment).setOnClickListener(this);
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		ArticleLikeRequest req = new ArticleLikeRequest();
		req.life_circle_id = articleInfo.id+"";
		req.user_id = UserInfoManager.getUserId(this)+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_ARTICLE_DETAIL, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				ArticleDetailResponse bean = new Gson().fromJson(resp.result, ArticleDetailResponse.class);
				if(Api.SUCCEED == bean.result_code && bean.data != null)
					updateView(bean.data);
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ArticleDetailActivity.this);
			}
		});
	}
	
	public void updateView(ArticleDetail data) {
		if(data.life_circle != null) {
			articleInfo = data.life_circle;
			cbLike.setChecked(articleInfo.is_like == 1);
			setText(R.id.tvArticleTitle, articleInfo.title);
			setText(R.id.tvArticleAuthor, articleInfo.nick_name);
			setText(R.id.tvArticleDate, DateUtil.getTime(articleInfo.create_time, 0));
			setText(R.id.tvArticleBrowseCount, articleInfo.browse_count+"");
			setText(R.id.tvArticleCommentCount, articleInfo.comment_count+"");
			setText(R.id.tvArticleLikeCount, articleInfo.user_like_count+"");
			setText(R.id.tvArticleContent, articleInfo.content);
			getView(R.id.ivArticleNice).setVisibility(articleInfo.ranking<=5? View.VISIBLE:View.GONE);
			if(!TextUtils.isEmpty(articleInfo.photo)) {
				updatePhotoList(articleInfo.photo.split(","));
			}
		}
		if(data.comment_list!=null) {
			lvComment.setAdapter(new ArticleCommentsAdapter(this, data.comment_list));
			lvComment.setOnItemClickListener(this);
			Utility.setListViewHeightBasedOnChildren(lvComment);
		}
	}
	
	private void updatePhotoList(String[] photos) {
		if(photos!=null && photos.length>0) {
			for(String item : photos) {
				final ImageView img = new ImageView(this);
				img.setScaleType(ScaleType.CENTER_CROP);
				llPhoto.addView(img);
				ImageLoadManager.getInstance(this).loadImage(item, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap bm) {
						double h = Double.valueOf(bm.getHeight());
						double w = Double.valueOf(bm.getWidth());
						double scale = Arith.div(h, w);
						int height = (int) Arith.mul(Double.valueOf(BitmapTool.getScreenWidthPX(ArticleDetailActivity.this)), scale);
						LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, height);
						img.setLayoutParams(lp);
						img.setImageBitmap(bm);
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
					}
				});
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.cbArticleLike:
			like(cbLike.isChecked());
			break;
			
		case R.id.tvArticleComment:
			Intent it = new Intent(ArticleDetailActivity.this, ArticleCommentListActivity.class);
			it.putExtra("articleId", articleInfo.id);
			startActivity(it);
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent it = new Intent(ArticleDetailActivity.this, ArticleCommentListActivity.class);
		it.putExtra("articleId", articleInfo.id);
		startActivity(it);
	}
	
	/**
	 * 点赞
	 */
	public void like(final boolean isCheck) {
		ProgressDialogUtil.showProgressDlg(this, "");
		ArticleLikeRequest req = new ArticleLikeRequest();
		req.life_circle_id = articleInfo.id+"";
		req.user_id = UserInfoManager.getUserId(this)+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_ARTICLE_LIKE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ArticleDetailActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(ArticleDetailActivity.this, "成功");
				} else {
					T.showShort(ArticleDetailActivity.this, bean.result_desc);
					cbLike.setChecked(!isCheck);
				}
			}
		});
	}
}
