package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.CommodityManageBean;
import com.shareshenghuo.app.shop.network.bean.ExcitationBean;
import com.shareshenghuo.app.shop.network.bean.ShopCategoryBean;


import android.content.Context;
import android.util.Log;
import android.widget.CheckBox;


public class PopTypeAdapter extends CommonAdapter<ShopCategoryBean>{
	public List<ShopCategoryBean> data;
	
	public PopTypeAdapter(Context context, List<ShopCategoryBean> data) {
		super(context, data, R.layout.bastextview);
		this.data = data;
	}

	@Override
	public void conver(ViewHolder holder, final ShopCategoryBean item, int position) {
		CheckBox box = holder.getView(R.id.check_del);
		holder.setText(R.id.tvs, item.name);
		if(item.ischeck){
			box.setChecked(true);
		}else{
			box.setChecked(false);
		}
	}
	
	public void update(){
		notifyDataSetChanged();
	}
	
	public  void setcheck(int position) {
		
		if(data.get(position).ischeck){
				data.get(position).ischeck = false;
		}else{
				data.get(position).ischeck = true;
		}
		this.notifyDataSetChanged();
	}
	
	public List<ShopCategoryBean> getchoose(){
		
		return data;
	}
}