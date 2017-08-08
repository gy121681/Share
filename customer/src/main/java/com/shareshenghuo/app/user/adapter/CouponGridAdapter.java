package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.CouponInfo;

public class CouponGridAdapter extends CommonAdapter<CouponInfo> {

	public CouponGridAdapter(Context context, List<CouponInfo> data) {
		super(context, data, R.layout.item_shop_coupon);
	}

	@Override
	public void conver(ViewHolder holder, CouponInfo item, int position) {
		holder.setText(R.id.tvItemCouponFee, item.coupon_fee+"");
		holder.setText(R.id.tvItemCouponDesc, "满"+item.lowest_limit+"使用");
	}
}
