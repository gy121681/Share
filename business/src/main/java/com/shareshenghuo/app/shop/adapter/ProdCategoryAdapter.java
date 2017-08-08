package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.ProdTypeInfo;

public class ProdCategoryAdapter extends CommonAdapter<ProdTypeInfo> {

	public ProdCategoryAdapter(Context context, List<ProdTypeInfo> data) {
		super(context, data, R.layout.item_prod_category);
	}

	@Override
	public void conver(ViewHolder holder, ProdTypeInfo item, int position) {
		holder.setText(R.id.tvProdCategory, item.type_name);
	}
}
