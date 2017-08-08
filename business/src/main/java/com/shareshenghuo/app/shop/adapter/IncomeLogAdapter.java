package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.IncomeInfo;
import com.shareshenghuo.app.shop.util.DateUtil;

public class IncomeLogAdapter extends CommonAdapter<IncomeInfo> {

	public IncomeLogAdapter(Context context, List<IncomeInfo> data) {
		super(context, data, R.layout.item_income_log);
	}

	@Override
	public void conver(ViewHolder holder, IncomeInfo item, int position) {
		holder.setText(R.id.tvIncomeDesc, item.income_desc);
		holder.setText(R.id.tvIncomeDate, DateUtil.getTime(item.create_time, 0));
		holder.setText(R.id.tvIncomeFee, item.fee+"");
	}
}
