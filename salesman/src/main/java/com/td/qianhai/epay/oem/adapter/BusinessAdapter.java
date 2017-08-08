package com.td.qianhai.epay.oem.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.amap.api.services.core.PoiItem;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.beans.BusinessBean;
import com.td.qianhai.epay.oem.beans.TurnoverBean;

public class BusinessAdapter extends CommonAdapter<BusinessBean> {

	public BusinessAdapter(Context context, List<BusinessBean> data) {
		super(context, data, R.layout.business_list_item);
	}

	@Override
	public void conver(ViewHolder holder, BusinessBean item, int position) {
		holder.setText(R.id.tv_city, item.area_name);
		holder.setText(R.id.tv_num, item.shop_num);
		
	}
}
