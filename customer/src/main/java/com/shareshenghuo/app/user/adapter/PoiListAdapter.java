package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.amap.api.services.core.PoiItem;
import com.shareshenghuo.app.user.R;

public class PoiListAdapter extends CommonAdapter<PoiItem> {

	public PoiListAdapter(Context context, List<PoiItem> data) {
		super(context, data, R.layout.item_poi);
	}

	@Override
	public void conver(ViewHolder holder, PoiItem item, int position) {
		holder.setImageResource(R.id.ivPoiIcon, position==0? R.drawable.icon_82:R.drawable.icon_82);
		holder.setText(R.id.tvPoiName, item.getTitle());
		holder.setText(R.id.tvPoiSnippet, item.getSnippet());
	}
}
