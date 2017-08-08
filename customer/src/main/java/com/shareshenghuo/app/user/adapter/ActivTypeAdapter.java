package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.CategoryInfo;

public class ActivTypeAdapter extends CommonAdapter<CategoryInfo> {

	public ActivTypeAdapter(Context context, List<CategoryInfo> data) {
		super(context, data, R.layout.item_activ_type);
	}

	@Override
	public void conver(ViewHolder holder, CategoryInfo item, int position) {
		holder.setImageByURL(R.id.ivActivTypeIcon, item.type_icon);
		holder.setText(R.id.tvActivTypeName, item.type_name);
	}
}
