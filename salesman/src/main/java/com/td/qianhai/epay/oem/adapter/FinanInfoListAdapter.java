package com.td.qianhai.epay.oem.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class FinanInfoListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> list;

	public FinanInfoListAdapter(Context context,
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
					R.layout.financ_info_listitem, null);
			holder.balance_1 = (TextView) convertView
					.findViewById(R.id.balance_1);
			holder.balance_2 = (TextView) convertView
					.findViewById(R.id.balance_2);
			holder.tv_date = (TextView) convertView
					.findViewById(R.id.tv_date);
			holder.tv_day = (TextView) convertView
					.findViewById(R.id.tv_day);
			holder.tv_type = (TextView) convertView
					.findViewById(R.id.tv_type);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		}
		HashMap<String, Object> map = list.get(position);
		String modsts = map.get("MODSTS").toString();
		if(modsts.equals("1")){
		holder.tv_day.setVisibility(View.VISIBLE);
		holder.tv_type.setBackgroundColor(mContext.getResources().getColor(R.color.greens));
		if(map.get("PRINCIPAL")!=null){
			holder.balance_1.setText(String .format("%.2f",(Double.parseDouble(map.get("PRINCIPAL").toString())/100)));
		}
		if(map.get("EXPSUMAMT")!=null){
			holder.balance_2.setText(String .format("%.2f",(Double.parseDouble(map.get("EXPINTEREST").toString())/100))+"宝币");
		}
		if(map.get("OPENDAT")!=null){
			String date = DateUtil.strToFormatStr(map.get("OPENDAT").toString(), "yyyy-MM-dd");
			holder.tv_date.setText(date);
		}
		if(map.get("DPTMNAM")!=null){
			holder.tv_name .setText(map.get("DPTMNAM").toString());
		}
		
		if(map.get("MODSTS")!=null){
//			String modsts = map.get("MODSTS").toString();
//			if(modsts.equals("1")){
				holder.tv_day.setVisibility(View.VISIBLE);
				holder.tv_type.setText("到期");
//			}else if(modsts.equals("2")){
//				holder.tv_day.setVisibility(View.GONE);
//				holder.tv_type.setText("到期");
//				holder.tv_type.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
//			}else if(modsts.equals("3")){
//				holder.tv_day.setVisibility(View.GONE);
//				holder.tv_type.setText("冻结");
//				holder.tv_type.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
//			}else if(modsts.equals("4")){
//				holder.tv_day.setVisibility(View.GONE);
//				holder.tv_type.setText("到期");
//				holder.tv_type.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
//			}
		}

		String date = DateUtil.strToFormatStr(map.get("ENDDAT").toString(), "yyyy-MM-dd");
		if(map.get("ENDDAT")!=null){
			holder.tv_day.setText(getTwoDay(date,getStringDate())+"天");
		}else{
			holder.tv_day.setText("0天");
		}
		}
		return convertView;
	}
	
	
	public  String getTwoDay(String sj1, String sj2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		try {
		Date date = myFormatter.parse(sj1);
		Date mydate = myFormatter.parse(sj2);
		
		day = (date.getTime()-mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
		return "0";
		}
		return day + "";
		}
	
	public  String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
		}

	class ViewHolder {
		TextView balance_1;
		TextView balance_2;
		TextView tv_date;
		TextView tv_day;
		TextView tv_type;
		TextView tv_name;
	}
}
