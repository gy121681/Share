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
 * 
 */
public class PhoneListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, String>> list;

	public PhoneListAdapter(Context context,
			ArrayList<HashMap<String, String>> phonelist) {
		this.context = context;
		this.list = phonelist;
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
					R.layout.phone_list_item, null);
			holder.itemnumber = (TextView) convertView
					.findViewById(R.id.phonenumbers);
			holder.itemtag = (TextView) convertView.findViewById(R.id.phonetags);
			convertView.setTag(holder);
		}
		HashMap<String, String> map = list.get(position);
		String aa = null;
		if (map.get("phonenumbers") != null) {

			holder.itemnumber.setText(map.get("phonenumbers").toString());
			aa = map.get("phonenumbers").toString();
		}
		if (map.get("phonetags")!=null&&map.get("phonetags").equals(aa)) {
			
			holder.itemtag.setText(map.get("phonetags").toString());
		}

		
		return convertView;
	}

	class RadioViewHolder {
		TextView itemnumber;
		TextView itemtag;
	}

}
