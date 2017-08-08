package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;

/**
 * 结算方式的dialog
 * 
 * @author liangge
 * 
 */
public class ClearingAdapter1 extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, Object>> list;

	public ClearingAdapter1(Context context,
			ArrayList<HashMap<String, Object>> list) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		RadioViewHolder holder = null;
		if (convertView != null) {
			holder = (RadioViewHolder) convertView.getTag();
		} else {
			holder = new RadioViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.radio_list_item1, null);
			holder.itemName = (TextView) convertView
					.findViewById(R.id.radio_item_text);
			holder.itemName1 = (TextView) convertView
					.findViewById(R.id.radio_item_text1);
			convertView.setTag(holder);
		}
		HashMap<String, Object> maps = list.get(position);
//		if(((AppContext)context.getApplicationContext()).getNocr().equals(maps.get("FEERATNO").toString())){
//		}else{
			double money = Double.parseDouble(maps.get("PRICE").toString());
			holder.itemName.setText(money / 100 + "元");
			holder.itemName1.setText(maps.get("FEERATE").toString()+"%");
//		}
		
		return convertView;
	}

	class RadioViewHolder {
		TextView itemName;
		TextView itemName1;
	}

}
