package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.AreaInfo;

public class AreaListAdapter extends CommonAdapter<AreaInfo> {

	public AreaListAdapter(Context context, List<AreaInfo> data) {
		super(context, data, R.layout.item_city);
	}

	@Override
	public void conver(ViewHolder holder, AreaInfo item, int position) {
		holder.setText(R.id.tvCity, item.area_name);
	}
}
