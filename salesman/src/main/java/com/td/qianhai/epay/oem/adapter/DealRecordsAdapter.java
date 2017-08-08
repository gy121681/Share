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
import com.td.qianhai.epay.utils.DateUtil;

public class DealRecordsAdapter extends BaseAdapter{
	
	private Context mContext;
	private ArrayList<HashMap<String, Object>> list;
	
	
	public DealRecordsAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
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
		if(convertView != null){
			holder = (ViewHolder) convertView.getTag();
		}else {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.deal_records_item, null);
			holder.tvTradName = (TextView) convertView.findViewById(R.id.tv_deal_records_item_name);
			holder.tvTradMoney = (TextView) convertView.findViewById(R.id.tv_deal_records_item_money);
			holder.tvTradDate = (TextView) convertView.findViewById(R.id.tv_deal_records_item_date);
			holder.tvTradStatus = (TextView) convertView.findViewById(R.id.tv_deal_records_item_status);
			convertView.setTag(holder);
		}
		holder.tvTradName.setText(list.get(position).get("TRANNAM").toString());
		Double money = Double.parseDouble(list.get(position).get("TXNAMT").toString());
		holder.tvTradMoney.setText(money/100+"元");
		String date = DateUtil.parse(list.get(position).get("SYSDAT").toString());
		holder.tvTradDate.setText(date);
		String sts = list.get(position).get("TXNSTS").toString();
		if("S".equals(sts)){
			holder.tvTradStatus.setText("成功");
		}else if("R".equals(sts)){
			holder.tvTradStatus.setText("已撤销");
		}else{
			holder.tvTradStatus.setText("失败");
		}
		//holder.tvTradStatus.setText(list.get(position).get("TXNSTS").toString());
		//convertView.setMinimumHeight(60);
		
		return convertView;
	}
	
	class ViewHolder{
		TextView tvTradName;
		TextView tvTradMoney;
		TextView tvTradDate;
		TextView tvTradStatus;
	}
}
