package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.CategoryInfo;

public class ShopListHotAdapter extends CommonAdapter<CategoryInfo> {

	public ShopListHotAdapter(Context context, List<CategoryInfo> data) {
		super(context, data, R.layout.item_category1);
	}

	@Override
	public void conver(ViewHolder holder, CategoryInfo item, int position) {
		if(TextUtils.isEmpty(item.icon))
			holder.setImageResource(R.id.ivCategoryIcon, R.drawable.share_c_business_details_photo_default);
		else
			holder.setImageByURL(R.id.ivCategoryIcon, item.icon);
		holder.setText(R.id.tvCategoryName, item.type_name);
	}
}
