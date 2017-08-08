package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.td.qianhai.epay.oem.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MoneyListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> list;

	public MoneyListAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
		this.list = list;
		this.mContext = context;
		

	}
	
//	@Override    
//	public boolean isEnabled(int position) {     
//	   return false;     
//	}  

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
					R.layout.money_list_item, null);
			holder.money_rate = (TextView) convertView
					.findViewById(R.id.money_rate);
			holder.product_name = (TextView) convertView
					.findViewById(R.id.product_name);
			holder.money_state = (TextView) convertView
					.findViewById(R.id.money_state);
			holder.balance = (TextView) convertView
					.findViewById(R.id.balance);
			holder.tv_buy = (TextView) convertView
					.findViewById(R.id.tv_buy);
			holder.tv_state = (TextView) convertView
					.findViewById(R.id.tv_state);
			holder.reward_img = (ImageView) convertView.findViewById(R.id.reward_img);
			holder.tv_typ = (TextView) convertView.findViewById(R.id.tv_typ);
			holder.product_image = (ImageView) convertView.findViewById(R.id.product_image);
			convertView.setTag(holder);
//
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
				HashMap<String, Object> maps = list.get(position);
				String instatus = maps.get("INSTATUS").toString();
				if(instatus.equals("0")){
					holder.tv_buy.setEnabled(false);
					holder.tv_state.setText("认购结束");
				}
				holder.money_rate.setText(maps.get("DPTRATE").toString()+"%");
				holder.product_name.setText(maps.get("MIDOEMNAM").toString());
				if(maps.get("STATUS")!=null){
					String statua = maps.get("STATUS").toString();
					if(statua.equals("1")){
						holder.tv_state.setText("申购中");
					}else{
						holder.tv_state.setText("已关闭");
					}
				}
				if(maps.get("MIDOEMTYP").toString().equals("1")){
					holder.product_image.setImageResource(R.drawable.april_img);
					holder.reward_img.setImageResource(R.drawable.hight_reward);
					if(maps.get("DPTCYCLETYP").toString().equals("1")){
						if(maps.get("DPTCYCLE").toString().equals("7")){
							holder.tv_typ.setText("每周奖励");
						}
					}else if(maps.get("DPTCYCLETYP").toString().equals("2")){
						holder.tv_typ.setText("每月奖励");
					}
					
				}

				
				if(maps.get("SAVEDAMT")!=null){
					holder.balance.setText(Double.parseDouble(maps.get("SAVEDAMT").toString())/100+"元");
				}
				
		return convertView;
	}

	class ViewHolder {
		TextView money_rate;
		TextView product_name;
		TextView balance;
		TextView money_state;
		TextView tv_buy;
		TextView tv_state,tv_typ;
		ImageView product_image,reward_img;
		
	}
}
