package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.ArticleCommentInfo;
import com.shareshenghuo.app.user.util.DateUtil;

public class ArticleCommentsAdapter extends CommonAdapter<ArticleCommentInfo> {

	public ArticleCommentsAdapter(Context context, List<ArticleCommentInfo> data) {
		super(context, data, R.layout.item_article_comment);
	}

	@Override
	public void conver(ViewHolder holder, ArticleCommentInfo item, int position) {
		holder.setImageByURL(R.id.ivCommentAvatar, item.user_photo);
		holder.setText(R.id.tvCommentName, item.nick_name);
		holder.setText(R.id.tvCommentDate, DateUtil.getTime(item.create_time, 0));
		holder.setText(R.id.tvCommentContent, item.content);
	}
}
