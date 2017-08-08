package com.shareshenghuo.app.user.adapter;

import java.util.List;

import com.shareshenghuo.app.user.R;

import android.content.Context;

public class LifePhotoAdapter extends CommonAdapter<String> {

	public LifePhotoAdapter(Context context, List<String> data) {
		super(context, data, R.layout.item_article_photo);
	}

	@Override
	public void conver(ViewHolder holder, String item, int position) {
		holder.setImageByURL(R.id.ivItemPic, item);
	}
}
