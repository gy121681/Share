package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.UserLevelInfo;

public class UserLevelAdapter extends CommonAdapter<UserLevelInfo> {

	public UserLevelAdapter(Context context, List<UserLevelInfo> data) {
		super(context, data, R.layout.item_user_level);
	}

	@Override
	public void conver(ViewHolder holder, UserLevelInfo item, int position) {
		holder.setText(R.id.tvLevelName, item.level_name);
	}
}
