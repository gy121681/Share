package com.shareshenghuo.app.user.adapter;

import java.util.ArrayList;
import java.util.List;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.AreasBean;
import com.shareshenghuo.app.user.network.bean.ScreenChildBean;
import com.shareshenghuo.app.user.widget.dialog.GridDialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class ShAdapter extends CommonAdapter<ScreenChildBean>  {
	
	public  List<ScreenChildBean> data;
	public int positions;
	public Context contexts;
	
	public ShAdapter(Context context, List<ScreenChildBean> data,int position) {
		super(context, data, R.layout.item_grids);
		this.contexts = context;
		this.positions = position;
		this.data = data;
	}
	
	public List<ScreenChildBean>  getdata(){
		
		return data;
	}
	
	public void setchex(int position){
		
	}

	@Override
	public void conver(ViewHolder holder, final ScreenChildBean item, final int position) {
		holder.setText(R.id.tvHotCity, item.child_configure_name);
		final CheckBox button = holder.getView(R.id.tvHotCity);
		button.setChecked(item.ischex);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				if(button.isClickable()){
//					Log.e("", "isClickable");
//					button.setClickable(false);
//				}else{
//					button.setClickable(true);
//				}
				if(button.isChecked()){
//					button.setChecked(false);
					GridDialog.data.get(positions).child_filter_configures_list.get(position).ischex = true;
//					notifyDataSetChanged();
				}else{
//					button.setChecked(true);
					GridDialog.data.get(positions).child_filter_configures_list.get(position).ischex = false;
//					notifyDataSetChanged();
				}
			}
		});
		
	}
	
	public List<AreasBean> getCheckedItem() {
		List<AreasBean> areas = new ArrayList<AreasBean>();
		for (int i = 0; i < getCount(); i++) {
			View view = this.getView(i, null, null);
			CheckBox box = (CheckBox)view.findViewById(R.id.tvHotCity);
			areas.get(i).bos = box.isChecked();
		}
		return areas;
	}
}
