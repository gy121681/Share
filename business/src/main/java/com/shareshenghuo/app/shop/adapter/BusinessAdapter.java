package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.BusinessBean;
import com.shareshenghuo.app.shop.network.bean.BusinessBeans;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.util.Util;

import android.content.Context;
import android.util.Log;
import android.view.View;


public class BusinessAdapter extends CommonAdapter<BusinessBeans>{
	
	public BusinessAdapter(Context context, List<BusinessBeans> data) {
		super(context, data, R.layout.business_list_item);
	}

	@Override
	public void conver(ViewHolder holder, final BusinessBeans item, int position) {
//		holder.setImageByURL(R.id.ivCommentAvatar, item.user_photo);
//		holder.setText(R.id.tv_amount, item.amount);
//		holder.setText(R.id.tv_time, DateUtil.getTime(item.create_time, 0));
		holder.getView(R.id.tv_type).setBackgroundResource(R.drawable.bg_arc_green);
		if(item.trade_source!=null&&item.trade_source.equals("0")){
			holder.setText(R.id.tv_type, "扫码");
			holder.getView(R.id.tv_type).setBackgroundResource(R.drawable.bg_arc_pink);			
		}else if(item.trade_source!=null&&item.trade_source.equals("6")){
			holder.setText(R.id.tv_type, "录单");
			holder.getView(R.id.tv_type).setBackgroundResource(R.drawable.bg_arc_green);
		}else if(item.trade_source!=null&&item.trade_source.equals("4")){
			holder.getView(R.id.tv_type).setBackgroundResource(R.drawable.bg_arc_pink);
			holder.setText(R.id.tv_type, "扫码");
		}else{
			holder.getView(R.id.tv_type).setBackgroundResource(R.drawable.bg_arc_green);
			holder.getView(R.id.tv_type).setVisibility(View.GONE);
		}
		holder.setText(R.id.tv_time, DateUtil.getTime(item.create_time,0));
		holder.setText(R.id.tv_name, item.nickname);
		holder.setText(R.id.tv_order, "***"+item.clslogno.substring(item.clslogno.length()-6));
		holder.setText(R.id.tv_amont, "+ "+Util.getnum(item.total_fee,false));
		if(item.discount_type.equals("1")){
			holder.getView(R.id.img).setTag("1");
			if(holder.getView(R.id.img).getTag().equals("1")){
				holder.setImageResource(R.id.img, R.drawable.consume_series_25);
			}
			
		}else if(item.discount_type.equals("2")){
			holder.getView(R.id.img).setTag("2");
			if(holder.getView(R.id.img).getTag().equals("2")){
				holder.setImageResource(R.id.img, R.drawable.consume_series_50);
			}
		}else if(item.discount_type.equals("3")){
			holder.getView(R.id.img).setTag("3");
			if(holder.getView(R.id.img).getTag().equals("3")){
				holder.setImageResource(R.id.img, R.drawable.consume_series_100);
			}
		}
//		holder.getView(R.id.ivCommentNice).setVisibility(item.ranking<=5? View.VISIBLE:View.GONE);
		
	}

//	@Override
//	public void conver(ViewHolder holder, BusinessBean item, int position) {
//		// TODO Auto-generated method stub
//		
//	}
}