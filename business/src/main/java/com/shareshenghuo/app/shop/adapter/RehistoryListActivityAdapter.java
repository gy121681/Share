package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.RehistoryBean;
import com.shareshenghuo.app.shop.util.Util;


public class RehistoryListActivityAdapter extends CommonAdapter<RehistoryBean>{
	
	public Context mcontext;
	
	public RehistoryListActivityAdapter(Context context, List<RehistoryBean> data) {
		super(context, data, R.layout.rehistory_list_item);
		this.mContext = context;
	}
		
	@Override
	public void conver(ViewHolder holder, final RehistoryBean item, int position) {
		
		holder.setText(R.id.tv_name, item.real_name);
		holder.setText(R.id.tv_phone, item.account);
		//System.out.println("=======产业链历史订单:"+item.create_time_string);
		holder.setText(R.id.tv_time, item.create_time_string);
		holder.getView(R.id.tv_title2).setVisibility(View.VISIBLE);
		
		if(!TextUtils.isEmpty(item.fee_rate)){
			holder.setText(R.id.num, (int)(Double.parseDouble(item.fee_rate)*100)+"%");
		}
		if(!TextUtils.isEmpty(item.total_fee)){
			holder.setText(R.id.tv_consbalacne,"¥"+Util.getnum(item.total_fee, false));
		}
		
		
//		if(!TextUtils.isEmpty(item.fee_rate)){
//			holder.setText(R.id.num, (int)(Double.parseDouble(item.fee_rate)*100)+"%");
//		}
		if(!TextUtils.isEmpty(item.fee_amt)){
			holder.setText(R.id.tv_balance,"¥"+ Util.getnum(item.fee_amt, false));
		}
		TextView status = holder.getView(R.id.tv_status);
		status.setTextColor(mContext.getResources().getColor(R.color.green_h));
		if(item.status.equals("0")){
			holder.setText(R.id.tv_status, "待支付");
			status.setTextColor(mContext.getResources().getColor(R.color.red));
		}else if(item.status.equals("1")){
			holder.setText(R.id.tv_status, "支付成功");
		}else if(item.status.equals("2")){
			status.setTextColor(mContext.getResources().getColor(R.color.red));
			holder.setText(R.id.tv_status, "支付失败");
		}else{
			status.setTextColor(mContext.getResources().getColor(R.color.red));
		}
	}
}