package com.shareshenghuo.app.user.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.OilintegralBean;
import com.shareshenghuo.app.user.util.DateUtil;
import com.shareshenghuo.app.user.util.Util;

import android.content.Context;
import android.widget.ListView;


public class OilIntegralAdapter extends CommonAdapter<OilintegralBean>{
	
	public OilIntegralAdapter(Context context, List<OilintegralBean> data) {
		super(context, data, R.layout.oilintegra_item);
	}

	@Override
	public void conver(ViewHolder holder, final OilintegralBean item, int position) {
		ListView listview = holder.getView(R.id.listview);
		if(item.card_no!=null&&item.card_no.length()>10){
			String card = item.card_no.substring( item.card_no.length()-4);
			holder.setText(R.id.tv1, "卡号: ****"+card);
		}
		if(item.opers_type.equals("02")){
			holder.setText(R.id.tv4,"支付积分");
		}else if(item.opers_type.equals("03")){
			holder.setText(R.id.tv4,"奖励积分");
		}else if(item.opers_type.equals("04")){
			holder.setText(R.id.tv4,"销售积分");
		}else if(item.opers_type.equals("05")){
			holder.setText(R.id.tv4,"代理积分");
		}
//		holder.getView(R.id.ll1);
		
		holder.setText(R.id.tv2, item.account_name);
		
		DecimalFormat df = new DecimalFormat("###.00"); 
		String price = "";
		try {
			price = df.format(Double.parseDouble(item.total_fee)/100);
		} catch (Exception e) {
			// TODO: handle exception
			price = "0.00";
		}
		holder.setText(R.id.tv3, " "+price+"元");
		holder.setText(R.id.tv5,  DateUtil.getTime(item.create_time,0)+"");
		holder.setText(R.id.tv6, "+"+Util.getnum(item.integral_num,false));
		
		if(item.rather_integral!=null){
			listview.setAdapter(new oilIntegTypeListAdapter(mContext, item.rather_integral));
		}

	}
}