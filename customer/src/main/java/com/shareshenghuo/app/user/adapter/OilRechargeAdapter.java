package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.OilintegralBean;
import com.shareshenghuo.app.user.util.DateUtil;
import com.shareshenghuo.app.user.util.Util;
import com.shareshenghuo.app.user.util.Utility;


public class OilRechargeAdapter extends CommonAdapter<OilintegralBean>{
	
	private boolean tag = false;
	
	public OilRechargeAdapter(Context context, List<OilintegralBean> data) {
		super(context, data, R.layout.oilrecharge_item);
	}

	@Override
	public void conver(ViewHolder holder, final OilintegralBean item, int position) {
		String card = "";
		if(item.card_no!=null&&item.card_no.length()>10){
			 card = item.card_no.substring( item.card_no.length()-4);
			holder.setText(R.id.tv1, "卡号: ****"+card);
		}
		TextView tv_info = holder.getView(R.id.tv_info);
		final ListView listviews = holder.getView(R.id.listviews);
		if(item.oil_card_periods_list!=null&&item.oil_card_periods_list.size()>0){
			OilRechargeChildAdapter adapter = new OilRechargeChildAdapter(mContext, item.oil_card_periods_list,card,item.account_name);
			listviews.setAdapter(adapter);
			Utility.setListViewHeightBasedOnChildren(listviews);
		}
		tv_info.setVisibility(View.VISIBLE);
		
//		DecimalFormat df = new DecimalFormat("###.00"); 
//		String price = "";
//		try {
//			price = df.format(Double.parseDouble(item.total_fee)/100);
//		} catch (Exception e) {
//			// TODO: handle exception
//			price = "0.00";
//		}
		
		holder.setText(R.id.tv2, item.account_name);
		holder.setText(R.id.tv5, "+"+Util.getfotmatnum(item.cls_amt, true,1)+"元");
		holder.setText(R.id.tv3, DateUtil.getTime(item.create_time,0));
		
		tv_info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!tag){
					listviews.setVisibility(View.VISIBLE);
					tag = true;
				}else{
					listviews.setVisibility(View.GONE);
					tag  = false;
				}
			}
		});
	}
}