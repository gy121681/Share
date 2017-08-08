package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.ExcitationBean;


import android.content.Context;


public class ConsumptionAdapter extends CommonAdapter<ExcitationBean>{
	
	public ConsumptionAdapter(Context context, List<ExcitationBean> data) {
		super(context, data, R.layout.consumption_list_item);
	}

	@Override
	public void conver(ViewHolder holder, final ExcitationBean item, int position) {
//		holder.setImageByURL(R.id.ivCommentAvatar, item.user_photo);
		if(position==0){
			holder.setImageResource(R.id.img, R.drawable.share_b_public_rebate3);
		}else if(position==1){
			holder.setImageResource(R.id.img, R.drawable.share_b_public_rebate2);
		}else if(position==2){
			holder.setImageResource(R.id.img, R.drawable.share_b_public_rebate1);
		}
		
		holder.setText(R.id.tv_amount, "交易总额\n"+item.amount);
//		holder.setText(R.id.tv_time, DateUtil.getTime(item.create_time, 0));
//		holder.setText(R.id.tv_time, item.time);
//		holder.setText(R.id.tv_toamounts, item.num);
		
//		holder.getView(R.id.ivCommentNice).setVisibility(item.ranking<=5? View.VISIBLE:View.GONE);
		
	}
}