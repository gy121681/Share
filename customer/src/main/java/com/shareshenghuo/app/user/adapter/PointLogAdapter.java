package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.PointLogInfo;
import com.shareshenghuo.app.user.util.DateUtil;

public class PointLogAdapter extends CommonAdapter<PointLogInfo> {

	public PointLogAdapter(Context context, List<PointLogInfo> data) {
		super(context, data, R.layout.item_exchange);
	}

	@Override
	public void conver(ViewHolder holder, PointLogInfo item, int position) {
		holder.setText(R.id.tvExchangeName, item.point_name);
		holder.setText(R.id.tvExchangePoint, item.get_point+"");
		holder.setText(R.id.tvExchangeStatus, DateUtil.getTime(item.create_time, 0));
	}

}
