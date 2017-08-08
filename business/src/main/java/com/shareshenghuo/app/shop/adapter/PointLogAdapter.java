package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.PointLogInfo;
import com.shareshenghuo.app.shop.util.DateUtil;

public class PointLogAdapter extends CommonAdapter<PointLogInfo> {

	public PointLogAdapter(Context context, List<PointLogInfo> data) {
		super(context, data, R.layout.item_point_log);
	}

	@Override
	public void conver(ViewHolder holder, PointLogInfo item, int position) {
		holder.setText(R.id.tvLogDesc, item.use_desc);
		holder.setText(R.id.tvLogDate, DateUtil.getTime(item.create_time, 0));
		holder.setText(R.id.tvLogAmount, item.fee+"");
	}
}
