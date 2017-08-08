package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.PushLogInfo;
import com.shareshenghuo.app.shop.util.DateUtil;

public class PushLogAdapter extends CommonAdapter<PushLogInfo> {

	private String[] pushStatus = {"待发送", "推送成功", "推送失败"};
	
	public PushLogAdapter(Context context, List<PushLogInfo> data) {
		super(context, data, R.layout.item_push_log);
	}

	@Override
	public void conver(ViewHolder holder, PushLogInfo item, int position) {
		holder.setText(R.id.tvPushContent, item.push_content);
		holder.setText(R.id.tvPushStatus, pushStatus[item.status]);
		holder.setText(R.id.tvPushTime, DateUtil.getTime(item.start_time, 0));
	}
}
