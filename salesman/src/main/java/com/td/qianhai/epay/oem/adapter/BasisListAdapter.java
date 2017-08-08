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
import com.td.qianhai.mpay.utils.DateUtil;

public class BasisListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> list;

	public BasisListAdapter(Context context,
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
					R.layout.basis_detail_item, null);
			holder.tvEarnings = (TextView) convertView
					.findViewById(R.id.tv_earnings);
			holder.tvBasisMoney = (TextView) convertView
					.findViewById(R.id.tv_basis_amt);
			holder.tvOpaccountDate = (TextView) convertView
					.findViewById(R.id.tv_opaccount_date);
			holder.tvBasisState = (TextView) convertView
					.findViewById(R.id.tv_basis_state);
			holder.tvBasisType = (TextView) convertView
					.findViewById(R.id.tv_basis_type);
			convertView.setTag(holder);
		}
		Double money = Double.parseDouble(list.get(position).get("PRINCIPAL")
				.toString());
		holder.tvBasisMoney.setText(money / 100 + "元");
		Double expinterest = Double.parseDouble(list.get(position)
				.get("EXPINTEREST").toString());
		holder.tvEarnings.setText(expinterest / 100 + "元");
		String date = DateUtil.strToFormatStr(list.get(position).get("OPENDAT")
				.toString(),"yyyy-MM-dd");
		holder.tvOpaccountDate.setText(date);
		String sts = list.get(position).get("MODSTS").toString();
		if ("1".equals(sts)) {
			holder.tvBasisState.setText("正常");
		} else if ("2".equals(sts)) {
			holder.tvBasisState.setText("停息");
		} else if ("3".equals(sts)) {
			holder.tvBasisState.setText("异常");
		} else if ("4".equals(sts)) {
			holder.tvBasisState.setText("失败");
		}
		String operstyp = list.get(position).get("SVAKIND").toString();
		if (operstyp.equals("01")) {
			holder.tvBasisType.setText("整存整取");
		} else if (operstyp.equals("02")) {
			holder.tvBasisType.setText("零存整取");
		} else if (operstyp.equals("03")) {
			holder.tvBasisType.setText("存本取息");
		} else if (operstyp.equals("04")) {
			holder.tvBasisType.setText("活期存款");
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvEarnings;
		TextView tvBasisMoney;
		TextView tvOpaccountDate;
		TextView tvBasisState;
		TextView tvBasisType;
	}
}
