package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.ActivInfo;

public class ShopActivListAdapter extends CommonAdapter<ActivInfo> {

	public ShopActivListAdapter(Context context, List<ActivInfo> data) {
		super(context, data, R.layout.item_shop_activ);
	}

	@Override
	public void conver(ViewHolder holder, ActivInfo item, int position) {
		holder.setText(R.id.tvItemActivTitle, item.active_title);
		holder.setText(R.id.tvItemActivDesc, "活动进行中 | "+item.join_count+"人报名");
	}

}
