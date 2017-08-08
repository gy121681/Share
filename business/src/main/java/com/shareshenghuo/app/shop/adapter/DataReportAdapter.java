package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.StatisticInfo;

public class DataReportAdapter extends CommonAdapter<StatisticInfo> {

	public DataReportAdapter(Context context, List<StatisticInfo> data) {
		super(context, data, R.layout.item_data_report);
	}

	@Override
	public void conver(ViewHolder holder, StatisticInfo item, int position) {
		holder.setText(R.id.tvItemName, item.staistics_name);
		holder.setText(R.id.tvItemToday, item.today_data+"");
		holder.setText(R.id.tvItemYesterday, item.yesterday_data+"");
		holder.setText(R.id.tvItemTotal, item.all_data+"");
	}
}
