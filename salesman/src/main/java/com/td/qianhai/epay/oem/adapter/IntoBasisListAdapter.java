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

public class IntoBasisListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> list;

	public IntoBasisListAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
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
					R.layout.into_basis_model_item, null);
			holder.tvDptmnam = (TextView) convertView
					.findViewById(R.id.tv_product_name);
			holder.tvDptrate = (TextView) convertView
					.findViewById(R.id.tv_interest_rates);
			// holder.tvDptcycle = (TextView) convertView
			// .findViewById(R.id.tv_period);
			holder.tvDptmin = (TextView) convertView
					.findViewById(R.id.tv_minimum_amt);
			holder.tvSvakind = (TextView) convertView
					.findViewById(R.id.tv_into_basis_type);
			holder.tvCurrency = (TextView) convertView
					.findViewById(R.id.tv_into_currency);
			convertView.setTag(holder);
		}
		// System.out.println(list.get(position).get("DPTCYCLE").toString());
		String dptmnam = list.get(position).get("DPTMNAM").toString();
		String dptrate = list.get(position).get("DPTRATE").toString();
		// String dptcycle = list.get(position).get("DPTCYCLE").toString();
		String currency = list.get(position).get("CURRENCY").toString();
		Double money = Double.parseDouble(list.get(position).get("DPTMIN")
				.toString());

		holder.tvDptmnam.setText(dptmnam);
		holder.tvDptrate.setText(dptrate + "%");
		// holder.tvDptcycle.setText(dptcycle);
		holder.tvDptmin.setText(money / 100 + "元");
		if (currency != null && currency.equals("01")) {
			holder.tvCurrency.setText("人民币");
		} else {
			holder.tvCurrency.setText("外币");
		}
		String operstyp = list.get(position).get("SVAKIND").toString();
		if (operstyp.equals("01")) {
			holder.tvSvakind.setText("整存整取");
		} else if (operstyp.equals("02")) {
			holder.tvSvakind.setText("零存整取");
		} else if (operstyp.equals("03")) {
			holder.tvSvakind.setText("存本取息");
		} else if (operstyp.equals("04")) {
			holder.tvSvakind.setText("活期存款");
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvDptmnam;
		TextView tvDptrate;
		TextView tvDptcycle;
		TextView tvDptmin;
		TextView tvSvakind;
		TextView tvCurrency;
	}
}
