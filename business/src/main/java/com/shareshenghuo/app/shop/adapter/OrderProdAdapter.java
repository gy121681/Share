package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.OrderProdInfo;
import com.shareshenghuo.app.shop.util.Arith;

public class OrderProdAdapter extends CommonAdapter<OrderProdInfo> {

	public OrderProdAdapter(Context context, List<OrderProdInfo> data) {
		super(context, data, R.layout.item_order_prod);
	}

	@Override
	public void conver(ViewHolder holder, OrderProdInfo item, int position) {
		holder.setText(R.id.tvProdName, item.product_name+" "+item.format_name);
		holder.setText(R.id.tvProdCount, "×"+item.product_count);
		holder.setText(R.id.tvProdPrice, "¥"+item.per_price);
		if(item.zhe_kou == 10) {
			holder.setText(R.id.tvProdZhekou, "");
			holder.setText(R.id.tvProdZhekouPrice, "");
		} else {
			holder.setText(R.id.tvProdZhekou, item.level_desc+item.zhe_kou+"折");
			holder.setText(R.id.tvProdZhekouPrice, "折后价 ¥"+item.zhe_kou_price);
		}
	}
}
