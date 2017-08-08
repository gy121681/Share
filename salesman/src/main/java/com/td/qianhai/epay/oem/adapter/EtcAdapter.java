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
import com.td.qianhai.epay.oem.qrcode.FinishListener;

/**
 * 结算方式的dialog
 * 
 * @author liangge
 * 
 */
public class EtcAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, Object>> list;

	public EtcAdapter(Context context,
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
					R.layout.etc_item, null);
			holder.itemName = (TextView) convertView
					.findViewById(R.id.etc_tv);
			convertView.setTag(holder);
		}
		HashMap<String, Object> maps = list.get(position);
		
		if(maps.get("LICENSENUMBER")!=null){
			String aa = maps.get("LICENSENUMBER").toString();
			try {
				holder.itemName.setText(aa);
			} catch (Exception e) {
				// TODO: handle exception
//				holder.itemName = (TextView) convertView
//						.findViewById(R.id.etc_tv);
//				holder.itemName.setText(aa);
			}
			
		}
		if(maps.get("LICENSECOLOR")!=null){
			String bb = maps.get("LICENSECOLOR").toString();
			if(bb.equals("yellow")){
				holder.itemName.setBackgroundResource(R.drawable.licenseplate_y);
			}else if(bb.equals("blue")){
				holder.itemName.setBackgroundResource(R.drawable.licenseplate_b);
			}else if(bb.equals("white")){
				holder.itemName.setTextColor(context.getResources().getColor(R.color.black)) ;
				holder.itemName.setBackgroundResource(R.drawable.licenseplate_w);
			}else if(bb.equals("black")){
				holder.itemName.setBackgroundResource(R.drawable.licenseplate_bk);
			}
		}
		
		return convertView;
	}

	class RadioViewHolder {
		TextView itemName;
	}

}
