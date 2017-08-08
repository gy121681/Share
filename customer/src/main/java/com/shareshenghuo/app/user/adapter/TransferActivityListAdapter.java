package com.shareshenghuo.app.user.adapter;

import java.util.List;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.IntegralBean;
import com.shareshenghuo.app.user.util.DateUtil;
import android.content.Context;


public class TransferActivityListAdapter extends CommonAdapter<IntegralBean> {

	public TransferActivityListAdapter(Context context, List<IntegralBean> data) {
		super(context, data, R.layout.item_transfer_list);
	}

	@Override
	public void conver(ViewHolder holder, final IntegralBean item, int position) {

		holder.setText(R.id.tv_time, DateUtil.getTime(item.create_time,0));
		holder.setText(R.id.tv_amount, item.shop_name);
		holder.setText(R.id.tv_num, "-"+item.investment_num);
	}
}