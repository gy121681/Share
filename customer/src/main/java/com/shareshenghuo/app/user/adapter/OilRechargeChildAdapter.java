package com.shareshenghuo.app.user.adapter;

import java.util.List;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.RechargeBean;
import com.shareshenghuo.app.user.util.Util;

import android.content.Context;
import android.view.View;


public class OilRechargeChildAdapter extends CommonAdapter<RechargeBean>{
	
	public String card_no;
	public String acc_name;
	
	public OilRechargeChildAdapter(Context context, List<RechargeBean> oil_card_periods_list,String card_no,String acc_name) {
		super(context, oil_card_periods_list, R.layout.oilrecharge_item1);
		this.acc_name = acc_name;
		this.card_no = card_no;
	}

	@Override
	public void conver(ViewHolder holder, final RechargeBean item, int position) {
//		if(card_no!=null&&card_no.length()>10){
//			String card = card_no.substring( card_no.length()-4);
//			holder.setText(R.id.tv1, "卡号: ****"+card);
//		}
		holder.setText(R.id.tv1, "卡号: ****"+card_no);
//		DecimalFormat df = new DecimalFormat("###.00"); 
//		String price = "";
//		try {
//			price = df.format(Double.parseDouble(item.fee_amt)/100);
//		} catch (Exception e) {
//			// TODO: handle exception
//			price = "0.00";
//		}
		holder.getView(R.id.tv_num).setVisibility(View.VISIBLE);
		
		
		holder.setText(R.id.tv_num, position+1+"/5");
		
		
		holder.setText(R.id.tv2, acc_name);
		holder.setText(R.id.tv5, "+"+Util.getfotmatnum(item.fee_amt, true,1)+"元");
		holder.setText(R.id.tv3, item.pay_time);
		
	}
}