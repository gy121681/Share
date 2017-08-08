package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.MyConsumBean;
import com.shareshenghuo.app.user.util.DateUtil;
import com.shareshenghuo.app.user.util.Util;

public class MyConsumptionAdapter extends CommonAdapter<MyConsumBean>{
	
	public MyConsumptionAdapter(Context context, List<MyConsumBean> data) {
		super(context, data, R.layout.myconsumption_list_item);
	}

	@Override
	public void conver(ViewHolder holder, final MyConsumBean item, int position) {
//		holder.setImageByURL(R.id.ivCommentAvatar, item.user_photo);
		holder.setText(R.id.tv_amount, "￥"+Util.getfotmatnum(item.total_fee, true,1));
//		holder.setText(R.id.tv_time, DateUtil.getTime(item.create_time, 0));
		holder.setText(R.id.tv_time, " "+DateUtil.getTime(item.create_time, 0));
		holder.setText(R.id.tv_name, item.body);
		
		if(item.discount_type!=null&&item.discount_type.equals("1")){
			holder.getView(R.id.rate_img).setTag("1");  
			
			if(holder.getView(R.id.rate_img).getTag().equals("1")){
				holder.setImageResource(R.id.rate_img, R.drawable.share_c_consume_series_25);
				holder.setText(R.id.tv_rate, "25%");
			}
			
		}else if(item.discount_type!=null&&item.discount_type.equals("2")) {
			holder.getView(R.id.rate_img).setTag("2");  
			if(holder.getView(R.id.rate_img).getTag().equals("2")){
				holder.setImageResource(R.id.rate_img, R.drawable.share_c_consume_series_50);
				holder.setText(R.id.tv_rate, "50%");
			}
			
		}else if(item.discount_type!=null&&item.discount_type.equals("3")) {
			holder.getView(R.id.rate_img).setTag("3");  
			if(holder.getView(R.id.rate_img).getTag().equals("3")){
				holder.setImageResource(R.id.rate_img, R.drawable.share_c_consume_series_100);
				holder.setText(R.id.tv_rate, "100%");
			}
			
		}else{
			holder.setImageResource(R.id.rate_img, R.drawable.share_c_consume_series_25);
			holder.setText(R.id.tv_rate, "25%");
		}
//		holder.setText(R.id.tv_num, item.num+"");
//		holder.getView(R.id.ivCommentNice).setVisibility(item.ranking<=5? View.VISIBLE:View.GONE);
		
	}
}