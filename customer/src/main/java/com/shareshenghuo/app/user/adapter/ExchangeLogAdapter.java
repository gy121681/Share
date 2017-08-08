package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.ExchangeInfo;

public class ExchangeLogAdapter extends CommonAdapter<ExchangeInfo> {
	
	private String[] status = {"待确认", "已兑换"};

	public ExchangeLogAdapter(Context context, List<ExchangeInfo> data) {
		super(context, data, R.layout.item_exchange);
	}

	@Override
	public void conver(ViewHolder holder, ExchangeInfo item, int position) {
		holder.setText(R.id.tvExchangeName, item.gift_name);
		holder.setText(R.id.tvExchangePoint, item.point+"");
		holder.setText(R.id.tvExchangeStatus, status[item.status]);
	}
}
