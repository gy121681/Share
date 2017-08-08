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

public class WechatListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> list;

	public WechatListAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
		this.list = list;
		this.mContext = context;

	}

	// @Override
	// public boolean isEnabled(int position) {
	// return false;
	// }

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
					R.layout.wechat_list_item, null);
			holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
			//
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HashMap<String, Object> maps = list.get(position);

		return convertView;
	}

	class ViewHolder {
		TextView tv_money;
		TextView tv_time;
		TextView tv_type;
		TextView tv_name;

	}
}
