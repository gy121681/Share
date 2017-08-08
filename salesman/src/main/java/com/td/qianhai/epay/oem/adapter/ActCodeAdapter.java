package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;

public class ActCodeAdapter extends BaseAdapter {
	/*当前的上下文对象*/
	private Context context;
	/*flightBean对象集合*/
	private ArrayList<HashMap<String, Object>> list;
	
	public ActCodeAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
		this.context = context;
		this.list = list;
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
		/*初始化对象*/
		CabinViewHolder holder = null;
		if (convertView != null) {
			holder = (CabinViewHolder) convertView.getTag();
		} else {
			holder = new CabinViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.actcode_list_item, null);
			holder.tv_acttype = (TextView) convertView
					.findViewById(R.id.tv_acttype);
			holder.tv_code = (TextView) convertView
					.findViewById(R.id.tv_code);
			holder.im_share = (ImageView) convertView
					.findViewById(R.id.im_share);
//			holder.tv_actcode_lastdate = (TextView) convertView
//					.findViewById(R.id.tv_actcode_lastdate);
			convertView.setTag(holder);
		}
		
		
		HashMap  map = list.get(position);
		String type = map.get("ACTCODTYP").toString();
		if(type.equals("0")){
			holder.tv_acttype .setText("试用激活码");
		}else if(type.equals("1")){
			holder.tv_acttype .setText("年激活码");
		}
		holder.tv_code .setText(map.get("ACTCOD").toString());
		
//		String fdate = "";
//		try {
//			 fdate = DateUtil.strToDateToLong( map.get("ACTIVDAT").toString());
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
//		String ldate =  map.get("INVAILDAT").toString();
//		String y = ldate.substring(0,4);
//		String m = ldate.substring(4,ldate.length()-2);
//		String d = ldate.substring(ldate.length()-2);
		
//		holder.tv_actcode_date .setText(fdate);
//		holder.tv_actcode_lastdate.setText(fdate +" 至 "+y+"-"+m+"-"+d);
		
		/*视图内容获取与加载*/
		return convertView;
		
	}

	class CabinViewHolder {
		TextView tv_acttype, tv_code, tv_actcode_date;
		ImageView im_share;
	}
}

