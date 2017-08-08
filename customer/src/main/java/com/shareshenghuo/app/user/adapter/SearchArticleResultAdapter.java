package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.ArticleInfo;

public class SearchArticleResultAdapter extends CommonAdapter<ArticleInfo> {

	public SearchArticleResultAdapter(Context context, List<ArticleInfo> data) {
		super(context, data, R.layout.item_city);
	}

	@Override
	public void conver(ViewHolder holder, ArticleInfo item, int position) {
		holder.setText(R.id.tvCity, item.title);
	}
}
