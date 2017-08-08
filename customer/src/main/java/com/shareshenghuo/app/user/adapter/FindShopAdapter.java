package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.ShopInfo;
import com.shareshenghuo.app.user.util.Utility;

public class FindShopAdapter extends CommonAdapter<ShopInfo> {

	public FindShopAdapter(Context context, List<ShopInfo> data) {
		super(context, data, R.layout.item_find_shop);
	}

	@Override
	public void conver(ViewHolder holder, ShopInfo item, int position) {
		holder.setImageByURL(R.id.ivShopPhoto, Utility.getFirstString(item.shop_photo));
		holder.setText(R.id.tvShopName, item.shop_name);
		holder.setText(R.id.tvShopType, "类型："+item.shop_type_name);
		holder.setText(R.id.tvShopTel, "电话："+item.mobile);
	}
}
