package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.beans.AppContext;

public class RateDealRecordsAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> list;

	public RateDealRecordsAdapter(Context context,
			ArrayList<HashMap<String, Object>> list, int tag) {
		this.list = list;
		this.mContext = context;
		

	}
	
//	@Override    
//	public boolean isEnabled(int position) {     
//	   return false;     
//	}  

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
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.rate_detail_item, null);
			// holder.tvTradName = (TextView) convertView
			// .findViewById(R.id.tv_operation);
			holder.rate_s = (TextView) convertView
					.findViewById(R.id.rate_s);
			holder.rate_money = (TextView) convertView
					.findViewById(R.id.rate_money);
			holder.rate_type = (TextView) convertView
					.findViewById(R.id.rate_type);
			holder.lin_rate = (LinearLayout) convertView.findViewById(R.id.lin_rate);
			holder.ok_img = (ImageView) convertView.findViewById(R.id.ok_img);
			convertView.setTag(holder);
//
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
				HashMap<String, Object> maps = list.get(position);
				
				
//				Log.e("", "------"+((AppContext)mContext.getApplicationContext()).getNocr());
//				Log.e("", "======"+maps.get("FEERATNO").toString());
				if(((AppContext)mContext.getApplicationContext()).getNocr().equals(maps.get("FEERATNO").toString())){
					
					Log.e("", "------"+((AppContext)mContext.getApplicationContext()).getNocr());
					Log.e("", "======"+maps.get("FEERATNO").toString());
					holder.lin_rate.setBackgroundResource(R.color.logrey);
					holder.lin_rate.setVisibility(View.GONE);
					holder.ok_img.setVisibility(View.VISIBLE);
				}
				
//				holder.rate_money.setText();
				
				double money = Double.parseDouble(maps.get("PRICE").toString());
				
				holder.rate_money.setText(money / 100 + "元");
				
				holder.rate_type.setText(maps.get("FEERATEDESC").toString());
				
				holder.rate_s.setText(maps.get("FEERATE").toString()+"%");
				

		return convertView;
	}

	class ViewHolder {
		TextView rate_s;
		TextView rate_money;
		TextView rate_type;
		LinearLayout lin_rate;
		ImageView ok_img;
		
	}
}
