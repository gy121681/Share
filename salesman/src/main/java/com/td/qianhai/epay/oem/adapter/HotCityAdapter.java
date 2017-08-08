package com.td.qianhai.epay.oem.adapter;

import java.util.List;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.beans.AreaInfoBean;
import com.td.qianhai.epay.oem.views.sortlistview.SortModel;

import android.content.Context;
import android.text.TextUtils;


public class HotCityAdapter extends CommonAdapter<AreaInfoBean> {

	public HotCityAdapter(Context context, List<AreaInfoBean> data) {
		super(context, data, R.layout.item_hot_city);
	}

	@Override
	public void conver(ViewHolder holder, AreaInfoBean item, int position) {
		if(item.province_came!=null){
			if(item.area_type.equals("2")){
				holder.setText(R.id.tvHotCity, item.province_came+"\n("+item.city_name+")");
			}else if(item.area_type.equals("3")){
				holder.setText(R.id.tvHotCity, item.province_came+"\n("+item.area_name+")");
			}else{
				holder.setText(R.id.tvHotCity, item.province_came);
			}
		}else if(item.area_type.equals("2")){
			holder.setText(R.id.tvHotCity, item.city_name);
		}else if(item.area_type.equals("3")){
			holder.setText(R.id.tvHotCity, item.area_name);
		}
		
//		if(item.area_type.equals("1")){
//			holder.setText(R.id.tvHotCity, item.province_came);
//		}else if(item.area_type.equals("2")){
//			holder.setText(R.id.tvHotCity, item.city_name);
//		}else if(item.area_type.equals("3")){
//			holder.setText(R.id.tvHotCity, item.area_name);
//		}else{
//			holder.setText(R.id.tvHotCity, "未知");
//		}
	}
}
