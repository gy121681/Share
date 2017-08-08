package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.IntegralBean;
import com.shareshenghuo.app.shop.util.DateUtil;


import android.content.Context;


public class TransferActivityListAdapter extends CommonAdapter<IntegralBean> {

	public TransferActivityListAdapter(Context context, List<IntegralBean> data) {
		super(context, data, R.layout.item_transfer_list);
	}

	@Override
	public void conver(ViewHolder holder, final IntegralBean item, int position) {

		holder.setText(R.id.tv_time, DateUtil.getTime(item.create_time,0));
		
		if(item.is_special_investment.equals("2")){
			holder.setText(R.id.tv_amount, "代理服务费: "+item.project_name);
			holder.setText(R.id.tv_num, "-"+item.investment_num);
		}else if(item.is_special_investment.equals("1")){
			String name  = item.real_name;
			if(name.length()>=2){
				name ="*"+name.substring(1, name.length());
			}
			holder.setText(R.id.tv_amount, "转让人: "+name);
			holder.setText(R.id.tv_num, "+"+item.investment_num);
		}
	}
}