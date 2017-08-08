package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.ExcitationBean;

import android.content.Context;


public class IncentivePoints2Adapter extends CommonAdapter<ExcitationBean>{
	
	public IncentivePoints2Adapter(Context context, List<ExcitationBean> data) {
		super(context, data, R.layout.incentivepoints2_list_item);
	}

	@Override
	public void conver(ViewHolder holder, final ExcitationBean item, int position) {
//		holder.setImageByURL(R.id.ivCommentAvatar, item.user_photo);
//		holder.setText(R.id.tv_amount, item.amount);
//		holder.setText(R.id.tv_time, DateUtil.getTime(item.create_time, 0));
		holder.setText(R.id.tv_numx, item.num);
		holder.setText(R.id.tv_time, item.time);
//		holder.setText(R.id.tv_name, item.name);
		holder.setText(R.id.tv_num, item.num);
//		holder.getView(R.id.ivCommentNice).setVisibility(item.ranking<=5? View.VISIBLE:View.GONE);
		
	}
}