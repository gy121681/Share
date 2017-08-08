package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;
import java.util.List;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.mail.utils.CBluetoothDeviceContext;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 联迪搜索设备Adapter
 * 
 * @author liangge
 * 
 */
public class LDSearshAdapter extends BaseAdapter {

	private Context context;
	private List<CBluetoothDeviceContext> discoveredDevices;

	public LDSearshAdapter(Context c,
			List<CBluetoothDeviceContext> discoveredDevices) {
		super();
		context = c;
		this.discoveredDevices = discoveredDevices;
	}

	// public void clear() {
	// bluetoothName = null;
	// this.notifyDataSetChanged();
	// }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return discoveredDevices.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return discoveredDevices.get(position);
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
		System.out.println(parent.getChildCount());
		if (convertView != null) {
			holder = (RadioViewHolder) convertView.getTag();
		} else {
			holder = new RadioViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.ld_search_list, null);
			holder.deviceName = (TextView) convertView
					.findViewById(R.id.tv_name);
			/*
			 * holder.tvMac = (TextView) convertView .findViewById(R.id.tv_mac);
			 */
			convertView.setTag(holder);
		}
		// holder.deviceName.setText("点击连接:" +
		// bluetoothName[position]);discoveredDevices
		System.out.println(position);
		CBluetoothDeviceContext deviceContext=discoveredDevices.get(position);
		holder.deviceName.setText("点击连接:" + deviceContext.name);
		System.out.println("点击连接:" + discoveredDevices.size() + "编号："
				+ position);
		/* holder.tvMac.setText(deviceInfos.get(position).getIdentifier()); */
		return convertView;
	}

	class RadioViewHolder {
		TextView deviceName, tvMac;
	}
}
