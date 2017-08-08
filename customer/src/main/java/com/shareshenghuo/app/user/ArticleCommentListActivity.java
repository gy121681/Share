package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

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
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.ArticleCommentsAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.ArticleCommentInfo;
import com.shareshenghuo.app.user.network.request.AddCommentRequest;
import com.shareshenghuo.app.user.network.request.ArticleCommentsRequest;
import com.shareshenghuo.app.user.network.response.ArticleCommentsResponse;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.ViewUtil;

/**
 * @author hang
 * 文章评论列表
 */
public class ArticleCommentListActivity extends BaseTopActivity 
	implements OnRefreshListener2<ListView>, OnItemClickListener, OnClickListener {
	
	private PullToRefreshListView lvData;
	private EditText edContent;
	
	private ArticleCommentsAdapter adapter;
	private int pageNo= 1;
	private int pageSize = 15;
	
	private int articleId;
	
	private int replyId;
	private String replyName;
	
	private String START_WITH_STR = "#回复";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_comment_list);
		init();
		loadData();
	}
	
	public void init() {
		articleId = getIntent().getIntExtra("articleId", 0);
		
		initTopBar("评论详情");
		edContent = getView(R.id.edCommentContent);
		
		lvData = getView(R.id.lvComment);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		lvData.setOnItemClickListener(this);
		
		findViewById(R.id.btnCommentSend).setOnClickListener(this);
	}
	
	public void loadData() {
		ArticleCommentsRequest req = new ArticleCommentsRequest();
		req.life_circle_id = articleId+"";
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_ARTICLE_COMMENTS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
				T.showNetworkError(ArticleCommentListActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				ArticleCommentsResponse bean = new Gson().fromJson(resp.result, ArticleCommentsResponse.class);
				if(Api.SUCCEED == bean.result_code)
					updateView(bean.data);
			}
		});
	}
	
	public void updateView(List<ArticleCommentInfo> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new ArticleCommentsAdapter(this, data);
			lvData.setAdapter(adapter);
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData();
	}

	@Override
	public void onClick(View v) {
		if(!UserInfoManager.isLogin(this)) {
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		
		switch(v.getId()) {
		case R.id.btnCommentSend:
			if(ViewUtil.checkEditEmpty(edContent, "请输入内容"))
				return;
			send();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long id) {
		ArticleCommentInfo item = (ArticleCommentInfo) adapterView.getItemAtPosition(position);
		replyId = item.id;
		replyName = item.nick_name;
		edContent.setText("#回复"+replyName+"#"+edContent.getText().toString());
		Selection.setSelection(edContent.getText(), edContent.getText().length());
	}
	
	public void send() {
		ProgressDialogUtil.showProgressDlg(this, "发送中");
		AddCommentRequest req = new AddCommentRequest();
		req.life_circle_id = articleId+"";
		req.user_id = UserInfoManager.getUserId(this)+"";
		String content = edContent.getText().toString();
		if(content.startsWith(START_WITH_STR)) {
			// 回复某人
			int start = content.indexOf("#", 1);
			if(start == -1)
				start = 0;
			req.content = content.substring(start+1, content.length());
			req.is_reply = "1";
			req.reply_id = replyId+"";
			req.reply_name = replyName;
		} else {
			// 评论文章
			req.content = content;
			req.is_reply = "0";
			req.reply_id = "";
			req.reply_name = "";
		}
		Log.e("", " = = =   "+req.toString());
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_ADD_COMMENT, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				Log.e("", " - - - "+resp.result);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(ArticleCommentListActivity.this, "发送成功");
					edContent.setText("");
					onPullDownToRefresh(lvData);
				} else {
					T.showShort(ArticleCommentListActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ArticleCommentListActivity.this);
			}
		});
	}
}
