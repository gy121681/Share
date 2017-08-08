package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.ExcitationBean;
import com.shareshenghuo.app.shop.network.bean.FilialobeBean;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.util.Util;

import android.content.Context;


public class IncentivePointsAdapter extends CommonAdapter<FilialobeBean>{
	
	
//	public void clear(){
//		this.clear();
//		this.notifyDataSetChanged();
//	}
	
	public IncentivePointsAdapter(Context context, List<FilialobeBean> data) {
		super(context, data, R.layout.incentivepoints_list_item);
	}

	@Override
	public void conver(ViewHolder holder, final FilialobeBean item, int position) {
//		holder.setImageByURL(R.id.ivCommentAvatar, item.user_photo);
		
		
		holder.setText(R.id.tv_time, DateUtil.getTime(item.create_time, 0));
		holder.setText(R.id.tv_name, item.nickname);
		if(item.total_fee!=null){
			holder.setText(R.id.tv_amount, Util.getnum(item.total_fee, true));
		}
		if(item.operb_type.equals("0")){
			holder.setText(R.id.tv_num, "+"+Util.getnum(item.money_num, false));
		}else{
			holder.setText(R.id.tv_num, "-"+Util.getnum(item.money_num,false));
		}
		
//		holder.setText(R.id.tv_time, DateUtil.getTime(item.create_time, 0));
		
//		holder.getView(R.id.ivCommentNice).setVisibility(item.ranking<=5? View.VISIBLE:View.GONE);
		
	}
}