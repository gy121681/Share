package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.mpay.utils.DateUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PEAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> list;
	private int tag;

	public PEAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
		this.list = list;
		this.mContext = context;

	}

	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public Object getItem(int arg0) {

		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		HashMap<String, Object> maps = list.get(position);
//		if (convertView == null) {
			holder = new ViewHolder();
			if(maps.get("TYP") != null){
				
				if(list.get(position).get("TYP").toString().equals("2")){
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.pe_list_item1, null);
				}else{
					
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.pe_list_item, null);
				}
			}
			
//			if(convertView==null){
//			convertView = LayoutInflater.from(mContext).inflate(
//					R.layout.pe_list_item, null);
			holder.tv_dayof = (TextView) convertView
					.findViewById(R.id.tv_dayof);
			holder.tv_dateof = (TextView) convertView
					.findViewById(R.id.tv_dateof);
			holder.tv_numberof = (TextView) convertView
					.findViewById(R.id.tv_numberof);
			holder.tv_moneyof = (TextView) convertView
					.findViewById(R.id.tv_moneyof);
			holder.lin_bg = (LinearLayout) convertView.findViewById(R.id.lin_bg);
			convertView.setTag(holder);
//			}else{
//				holder = (ViewHolder) convertView.getTag();
//			}
//			convertView = LayoutInflater.from(mContext).inflate(
//					R.layout.pe_list_item, null);
//			holder.tv_dayof = (TextView) convertView
//					.findViewById(R.id.tv_dayof);
//			holder.tv_dateof = (TextView) convertView
//					.findViewById(R.id.tv_dateof);
//			holder.tv_numberof = (TextView) convertView
//					.findViewById(R.id.tv_numberof);
//			holder.tv_moneyof = (TextView) convertView
//					.findViewById(R.id.tv_moneyof);
//			holder.lin_bg = (LinearLayout) convertView.findViewById(R.id.lin_bg);
//			convertView.setTag(holder);
			//
//		} 
//	else {
//			holder = (ViewHolder) convertView.getTag();
//		}

		
		

		if (maps.get("RDAY") != null) {
			holder.tv_dayof.setText(maps.get("RDAY").toString());
		}
		if (maps.get("MEMBERNUM") != null) {
				holder.tv_numberof.setText(maps.get("MEMBERNUM").toString());
				
		}
		
		if (maps.get("INCOMAMT") != null) {
			Double money = Double.parseDouble(maps.get("INCOMAMT").toString());
			holder.tv_moneyof.setText(money/100+"");
		}
//		if (maps.get("INCOMAMT") != null) {
//			holder.tv_moneyof.setText(maps.get("INCOMAMT").toString());
//		}
		if (maps.get("RYEARMONTH") != null) {
			holder.tv_dateof.setText(maps.get("RYEARMONTH").toString());
		}

		if(maps.get("TYP") != null){
			
			if(list.get(position).get("TYP").toString().equals("2")){
				if (maps.get("RYEARMONTH") != null) {
					holder.tv_dateof.setText(maps.get("RYEARMONTH").toString()+"年");
				}
			}else{
				if (maps.get("RYEARMONTH") != null) {
					String a = maps.get("RYEARMONTH").toString();
					holder.tv_dateof.setText(a.substring(0,4)+"年"+a.substring(a.length()-2)+"月");
				}
			}
		}
		
		return convertView;
	}

	class ViewHolder {
		TextView tv_dayof;
		TextView tv_dateof;
		TextView tv_numberof;
		TextView tv_moneyof;
		LinearLayout lin_bg;
	}
}
