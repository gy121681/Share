package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.CityInfo;

public class CityListAdapter extends CommonAdapter<CityInfo> {

	public CityListAdapter(Context context, List<CityInfo> data) {
		super(context, data, R.layout.item_city);
	}

	@Override
	public void conver(ViewHolder holder, CityInfo item, int position) {
		holder.setText(R.id.tvCity, item.city_name);
	}
}
