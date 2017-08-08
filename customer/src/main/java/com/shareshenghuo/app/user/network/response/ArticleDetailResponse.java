package com.shareshenghuo.app.user.network.response;

import java.util.List;

import com.shareshenghuo.app.user.network.bean.ArticleCommentInfo;
import com.shareshenghuo.app.user.network.bean.ArticleInfo;

public class ArticleDetailResponse extends BaseResponse {
	
	public ArticleDetail data;
	
	public class ArticleDetail {
		public ArticleInfo life_circle;
		public List<ArticleCommentInfo> comment_list;
		public int is_like;
	}
}
