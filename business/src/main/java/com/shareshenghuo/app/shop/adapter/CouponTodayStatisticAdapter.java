package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.CouponTodayStatisticInfo;
import com.shareshenghuo.app.shop.util.DateUtil;

public class CouponTodayStatisticAdapter extends CommonAdapter<CouponTodayStatisticInfo> {

	public CouponTodayStatisticAdapter(Context context, List<CouponTodayStatisticInfo> data) {
		super(context, data, R.layout.item_statistic_detail);
	}

	@Override
	public void conver(ViewHolder holder, CouponTodayStatisticInfo item, int position) {
		holder.setText(R.id.tvStatisticAccount, item.account);
		holder.setText(R.id.tvStatisticAmount, item.coupon_name);
		holder.setText(R.id.tvStatisticDate, item.sdate);
	}
}
