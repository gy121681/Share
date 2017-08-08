package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.HistoryDataBeans;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.util.Util;

public class HistoryrecordAdapter  extends CommonAdapter<HistoryDataBeans>{
	
	public HistoryrecordAdapter(Context context, List<HistoryDataBeans> data) {
		super(context, data, R.layout.history_record_list_item);
	}

	@Override
	public void conver(ViewHolder holder, final HistoryDataBeans item, int position) {
//		holder.setImageByURL(R.id.ivCommentAvatar, item.user_photo);
//		holder.setText(R.id.tv_amount, item.amount);
//		holder.setText(R.id.tv_time, DateUtil.getTime(item.create_time, 0));
		
		
		holder.setText(R.id.tv_time, DateUtil.getStrTime(item.payDate+""));
		holder.setText(R.id.tv_amont, "+ "+Util.getnum(item.sumTotalFee,false));
//		holder.getView(R.id.ivCommentNice).setVisibility(item.ranking<=5? View.VISIBLE:View.GONE);
		
	}

//	@Override
//	public void conver(ViewHolder holder, BusinessBean item, int position) {
//		// TODO Auto-generated method stub
//		
//	}
}
