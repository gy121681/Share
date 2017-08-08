package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.Indexablewidget.SortModel;

public class HotCityAdapter extends CommonAdapter<SortModel> {

	public HotCityAdapter(Context context, List<SortModel> data) {
		super(context, data, R.layout.item_hot_city);
	}

	@Override
	public void conver(ViewHolder holder, SortModel item, int position) {
		holder.setText(R.id.tvHotCity, item.name);
	}
}
