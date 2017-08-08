package com.shareshenghuo.app.shop.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.CommodityManageBean;
import com.shareshenghuo.app.shop.network.bean.ExcitationBean;
import com.shareshenghuo.app.shop.network.bean.FilialobeBean;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.util.Util;

import android.content.Context;
import android.view.View;


public class CommodityManageAdapter extends CommonAdapter<CommodityManageBean>{
	
	public CommodityManageAdapter(Context context, List<CommodityManageBean> data) {
		super(context, data, R.layout.commodity_manage_item);
	}
		
	@Override
	public void conver(ViewHolder holder, final CommodityManageBean item, int position) {
		
		DecimalFormat df = new DecimalFormat("###,###.00"); 
		String price = "";
		try {
			price = df.format(Double.parseDouble(item.price));
		} catch (Exception e) {
			// TODO: handle exception
			price = "0.00";
		}
		if(price.substring(0, 1).equals(".")){
			price = "0"+price;
		}
		
		holder.setText(R.id.tv_time, item.create_time_string);
		holder.setText(R.id.shop_name, item.name);
		holder.setText(R.id.shop_imfo, item.description);
		holder.setText(R.id.shop_balance, "¥ "+price);//Util.getfotmatnum(item.price, true, 2));
		holder.setImageByURL(R.id.shop_img, item.photo);
	}
}