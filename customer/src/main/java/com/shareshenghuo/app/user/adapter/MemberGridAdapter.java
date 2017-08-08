package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.GroupMemberInfo;

public class MemberGridAdapter extends CommonAdapter<GroupMemberInfo> {

	public MemberGridAdapter(Context context, List<GroupMemberInfo> data) {
		super(context, data, R.layout.item_member);
	}

	@Override
	public void conver(ViewHolder holder, GroupMemberInfo item, int position) {
		holder.setImageByURL(R.id.civMemberAvatar, item.user_photo);
		holder.setText(R.id.tvMemberName, item.user_name);
	}
}
