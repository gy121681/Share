package com.td.qianhai.epay.oem.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;

public class ProvinceAdapter extends BaseAdapter{
	public String[]  adapter_list;
	private Context context;
	public ProvinceAdapter(String[] selectPicTypeStr, Context context){
		adapter_list = selectPicTypeStr;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return adapter_list.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		
//		LayoutInflater inflater = LayoutInflater.from(AddShopActivity.this);
//		View view = inflater.inflate(R.layout.dailog_item, null);
//		TextView v = (TextView) view.findViewById(R.id.tvs);
//		v.setText(adapter_list[position]);
		
		TextView tv = new TextView(context);
		tv.setWidth(800);
		tv.setTextColor(context.getResources().getColor(R.color.black));
		tv.setPadding(20, 20, 20, 20);
		tv.setTextSize(18);
		tv.setText(adapter_list[position]);
		return tv;
	}
	
}