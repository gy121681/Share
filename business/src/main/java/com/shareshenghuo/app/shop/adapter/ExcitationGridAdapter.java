package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.ExcitationTypeBean;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;


public class ExcitationGridAdapter extends CommonAdapter<ExcitationTypeBean> {
	
	private List<ExcitationTypeBean> data;

	public ExcitationGridAdapter(Context context, List<ExcitationTypeBean> data) {
		super(context, data, R.layout.excitation_grid_item);
		this.data = data;
	}

	@Override
	public void conver(ViewHolder holder, ExcitationTypeBean item, int position) {
		
		final int positions = position;
		holder.setText(R.id.tvs, item.POINTSDES);
		TextView box = holder.getView(R.id.check);
		if(data.get(position).ISDEFAULT.equals("0")){
			data.get(position).ischeck = true;
			
		}
		if(data.get(position).ischeck){
			box.setBackgroundResource(R.drawable.checkbox_excitation_true);
		}else{
			box.setBackgroundResource(R.drawable.checkbox_excitation_false);
		}
//		
//		box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//				// TODO Auto-generated method stub
//				if(arg1){
//					setcheck(positions);
//				}
//			}
//		});
		
	}

	public void setcheck(int arg2) {
		// TODO Auto-generated method stub
		
		for (int i = 0; i < data.size(); i++) {
				data.get(i).ischeck = false;
				data.get(i).ISDEFAULT = "1";
		}
		data.get(arg2).ischeck = true;
		notifyDataSetChanged();
	}
	
	public List<ExcitationTypeBean>  getdata(){
		
		return data;
	}
}
