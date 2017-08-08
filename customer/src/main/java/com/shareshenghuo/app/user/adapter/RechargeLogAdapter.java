package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.RechargeLogInfo;
import com.shareshenghuo.app.user.util.DateUtil;

public class RechargeLogAdapter extends CommonAdapter<RechargeLogInfo> {

	public RechargeLogAdapter(Context context, List<RechargeLogInfo> data) {
		super(context, data, R.layout.item_recharge_log);
	}

	@Override
	public void conver(ViewHolder holder, RechargeLogInfo item, int position) {
		holder.setText(R.id.tvRechargeDesc, item.use_desc);
		holder.setText(R.id.tvRechargeDate, DateUtil.getTime(item.create_time, 0));
		holder.setText(R.id.tvRechargeFee, item.fee);
	}

}
