package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.CircleInfo;

public class CircleGridAdapter extends CommonAdapter<CircleInfo> {

	public CircleGridAdapter(Context context, List<CircleInfo> data) {
		super(context, data, R.layout.item_circle);
	}

	@Override
	public void conver(ViewHolder holder, CircleInfo item, int position) {
		holder.setImageByURL(R.id.civCirclePic, item.im_gourp_photo);
		holder.setText(R.id.tvCircleName, item.group_name);
	}
}
