package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.app.Constant;
import com.shareshenghuo.app.user.network.bean.OrderStatusInfo;
import com.shareshenghuo.app.user.util.DateUtil;

public class OrderStatusAdapter extends CommonAdapter<OrderStatusInfo> {

	public OrderStatusAdapter(Context context, List<OrderStatusInfo> data) {
		super(context, data, R.layout.item_order_status);
	}

	@Override
	public void conver(ViewHolder holder, OrderStatusInfo item, int position) {
		holder.setImageResource(R.id.ivOrderStatusIcon, Constant.ORDER_STATUS_ICON[item.order_status]);
		holder.setText(R.id.tvOrderStatusDesc, item.order_operation);
		holder.setText(R.id.tvOrderStatusDate, DateUtil.getTime(item.create_time, 0));
	}
}
