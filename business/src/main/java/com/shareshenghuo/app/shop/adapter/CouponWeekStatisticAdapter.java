package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.CouponWeekStatisticInfo;

public class CouponWeekStatisticAdapter extends CommonAdapter<CouponWeekStatisticInfo> {

	public CouponWeekStatisticAdapter(Context context, List<CouponWeekStatisticInfo> data) {
		super(context, data, R.layout.item_statistic_month);
	}

	@Override
	public void conver(ViewHolder holder, CouponWeekStatisticInfo item, int position) {
		holder.setText(R.id.tvStatisticDate, item.key);
		holder.setText(R.id.tvStatisticAmount, item.all_fee+"");
	}
}
