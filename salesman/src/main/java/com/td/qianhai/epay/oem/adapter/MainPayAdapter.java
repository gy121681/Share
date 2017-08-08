package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.beans.MianPayBean;

public class MainPayAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<MianPayBean> list;

	public MainPayAdapter(Context context, ArrayList<MianPayBean> list) {
		this.mContext = context;
		this.list = list;

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
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.main_pay_listitem, null);
			holder.bankimgs = (ImageView) convertView
					.findViewById(R.id.bank_images);
			holder.bankname = (TextView) convertView
					.findViewById(R.id.bank_names);
			convertView.setTag(holder);
		}
		
		MianPayBean  info = list.get(position);
		holder.bankimgs .setImageResource(info.getImagesid());
		holder.bankname.setText(info.getBankname());

		return convertView;
	}

	class ViewHolder {
		TextView bankname;
		ImageView bankimgs;
	}
}
