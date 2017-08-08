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
import com.td.qianhai.epay.oem.qrcode.FinishListener;
import com.td.qianhai.mpay.utils.DateUtil;

/**
 * 结算方式的dialog
 * 
 * @author liangge
 * 
 */
public class EtcListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, Object>> list;

	public EtcListAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
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
		RadioViewHolder holder = null;
		if (convertView != null) {
			holder = (RadioViewHolder) convertView.getTag();
		} else {
			holder = new RadioViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.etc_list_item, null);
			holder.tv_1 = (TextView) convertView
					.findViewById(R.id.tvss_1);
			holder.tv_2 = (TextView) convertView
					.findViewById(R.id.tvss_2);
			holder.tv_3 = (TextView) convertView
					.findViewById(R.id.tvss_3);
			holder.tv_4 = (TextView) convertView
					.findViewById(R.id.tvss_4);
			convertView.setTag(holder);
			
		}
		HashMap<String, Object> maps = list.get(position);
		if(maps.get("ADDRESS")!=null){
			holder.tv_1.setText(maps.get("ADDRESS").toString());
		}
		if(maps.get("SUMAMT")!=null){
			
			String a = (Double.parseDouble(maps.get("SUMAMT").toString()))/100+"";
				holder.tv_3.setText("￥"+a);
			
			
		}
		if(maps.get("OPERTIM")!=null){
			String a = "";
			try {
				 a = DateUtil.strToDateToLong(maps.get("OPERTIM").toString());
			} catch (Exception e) {
				// TODO: handle exception
				a = maps.get("OPERTIM").toString();
			}
			
			holder.tv_2.setText(a);
		}
		if(maps.get("OPERSTS")!=null){
			String b = maps.get("OPERSTS").toString();
			if( b.endsWith("1")){
				holder.tv_4.setText("缴费成功");
				holder.tv_3.setTextColor(context.getResources().getColor(R.color.red));
				holder.tv_4.setTextColor(context.getResources().getColor(R.color.prompt));
			}else{
				holder.tv_4.setText("缴费失败");
			}
			
		}
		
			
		return convertView;
	}

	class RadioViewHolder {
		TextView tv_1,tv_2,tv_3,tv_4;
	}

}
