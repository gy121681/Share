package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.OtherInfo;

public class PointRuleAdapter extends CommonAdapter<OtherInfo> {

	public PointRuleAdapter(Context context, List<OtherInfo> data) {
		super(context, data, R.layout.item_city);
	}

	@Override
	public void conver(ViewHolder holder, OtherInfo item, int position) {
		holder.setText(R.id.tvCity, (position+1)+". "+item.beizhu+" +"+item.other_value);
	}

}
