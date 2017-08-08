package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.OrderProdInfo;

public class OrderProdDetailAdapter extends CommonAdapter<OrderProdInfo> {

	public OrderProdDetailAdapter(Context context, List<OrderProdInfo> data) {
		super(context, data, R.layout.item_order_prod_detail);
	}

	@Override
	public void conver(ViewHolder holder, OrderProdInfo item, int position) {
		holder.setImageByURL(R.id.ivProdPhoto, item.thum_photo);
		holder.setText(R.id.tvProdName, item.product_name);
		holder.setText(R.id.tvProdPrice, "¥"+item.zhe_kou_price);
		holder.setText(R.id.tvProdCount, "×"+item.product_count);
		holder.setText(R.id.tvProdFormat, item.format_name);
	}
}
