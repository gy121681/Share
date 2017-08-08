package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.StatisticDetailInfo;
import com.shareshenghuo.app.shop.network.bean.StatisticMonthInfo;
import com.shareshenghuo.app.shop.util.DateUtil;

public class StatisticDetailsAdapter extends CommonAdapter<StatisticDetailInfo> {

	public StatisticDetailsAdapter(Context context, List<StatisticDetailInfo> data) {
		super(context, data, R.layout.item_statistic_detail);
	}

	@Override
	public void conver(ViewHolder holder, StatisticDetailInfo item, int position) {
		holder.setText(R.id.tvStatisticAccount, item.account);
		holder.setText(R.id.tvStatisticAmount, item.fee+"");
		holder.setText(R.id.tvStatisticDate, item.sdate);
	}
}
