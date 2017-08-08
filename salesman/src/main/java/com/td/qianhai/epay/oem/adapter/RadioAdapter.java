package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;

/**
 * radio的dialog
 * 
 * @author liangge
 * 
 */
public class RadioAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private int selectorposition=0;

	public RadioAdapter(Context context, ArrayList<HashMap<String, String>> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setSelectorposition(int selectorposition) {
		this.selectorposition = selectorposition;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		RadioViewHolder holder = null;
		if (convertView != null) {
			holder = (RadioViewHolder) convertView.getTag();
		} else {
			holder = new RadioViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.air_dialog_item, null);
			holder.itemName = (TextView) convertView
					.findViewById(R.id.dialog_item_layout_txt1);
			holder.select = (CheckBox) convertView
					.findViewById(R.id.dialog_item_layout_checkbox);
			convertView.setTag(holder);
		}
		
		if(selectorposition==position){
			holder.select.setSelected(true);
			holder.select.setChecked(true);
		}else{
			holder.select.setSelected(false);
			holder.select.setChecked(false);
		}
		
		holder.itemName.setText(list.get(position).get("itemName").toString());
		return convertView;
	}

	class RadioViewHolder {
		TextView itemName;
		CheckBox select;
	}

}
