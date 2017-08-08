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
import android.widget.TextView;

public class MyProfitAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> list;
	private int tag;

	public MyProfitAdapter(Context context,
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

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.myprofit_list_item, null);
			holder.tvTradMoney = (TextView) convertView
					.findViewById(R.id.tv_trading_amt);
			holder.tvTradDate = (TextView) convertView
					.findViewById(R.id.tv_operation_time);
			holder.tvTradStatus = (TextView) convertView
					.findViewById(R.id.tv_trading_state);
			holder.tvStlType = (TextView) convertView
					.findViewById(R.id.tv_type);
			holder.tv_info_state = (TextView) convertView
					.findViewById(R.id.tv_info_state);
			convertView.setTag(holder);
			//
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HashMap<String, Object> maps = list.get(position);
		if (maps.get("AGTSHRAMT") != null) {
			Double money = Double.parseDouble(maps.get("AGTSHRAMT").toString());
			holder.tvTradMoney.setText(money / 100 + "元");
		}
		if (maps.get("RES") != null) {
//			if (maps.get("RES").toString().equals("1")) {
				holder.tv_info_state.setText(maps.get("RES").toString());

//			} else if (maps.get("RES").toString().equals("2")) {
//				holder.tv_info_state.setText("来自收款宝");
//			}
		}
		if (maps.get("TXNDATE") != null) {
			String aa = maps.get("TXNDATE").toString();
			if (aa.length() >= 8) {
				holder.tvTradDate.setText(aa.substring(0, 4) + "-"
						+ aa.subSequence(4, 6) + "-"
						+ aa.substring(aa.length() - 2));
			} else {
				holder.tvTradDate.setText(aa);
			}

		}
		if (maps.get("TXNDATE") != null) {
			if (maps.get("SHRTYPE").toString().equals("0")) {
				holder.tvStlType.setText("费率收益");
			} else {
				holder.tvStlType.setText("闪提收益");
			}
		}
		if (maps.get("TOTTXNAMT") != null) {
			Double money = Double.parseDouble(maps.get("TOTTXNAMT").toString());
			holder.tvTradStatus.setText(money / 100 + "元");
		}

		return convertView;
	}

	class ViewHolder {
		TextView tvTradName;
		TextView tvTradMoney;
		TextView tvTradDate;
		TextView tvTradStatus;
		TextView tvStlType;
		TextView tv_info_state;
	}
}
