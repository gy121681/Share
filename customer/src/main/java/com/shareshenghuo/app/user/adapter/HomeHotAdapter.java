package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.ProdInfo;

public class HomeHotAdapter extends CommonAdapter<ProdInfo> {

	public HomeHotAdapter(Context context, List<ProdInfo> data) {
		super(context, data, R.layout.item_home_hot);
	}

	@Override
	public void conver(ViewHolder holder, ProdInfo item, int position) {
		holder.setImageByURL(R.id.ivHomeHotItemPic, item.thum_photo);
		holder.setText(R.id.tvHomeHotItemTitle, item.product_name);
		holder.setText(R.id.tvHomeHotItemDesc, item.produce_desc);
		holder.setText(R.id.tvHomeHotItemPrice, item.default_new_price+"元/"+item.unit);
	}
}
