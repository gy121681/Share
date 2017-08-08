package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.StatisticMonthInfo;

public class StatisticFilterAdapter extends CommonAdapter<StatisticMonthInfo> {

	public StatisticFilterAdapter(Context context, List<StatisticMonthInfo> data) {
		super(context, data, R.layout.item_statistic_detail);
	}

	@Override
	public void conver(ViewHolder holder, StatisticMonthInfo item, int position) {
		holder.setText(R.id.tvStatisticAccount, item.value.account);
		holder.setText(R.id.tvStatisticAmount, item.value.fee+"");
		holder.setText(R.id.tvStatisticDate, item.value.sdate);
	}
}
