package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.views.AnimationUtil;

/**
 * 
 */
public class SubAgentAdapter1 extends BaseAdapter {
	/*当前的上下文对象*/
	private Context context;
	/*flightBean对象集合*/
	private ArrayList<HashMap<String, Object>> list;
	
	private int positions = -1;
	
	private boolean flag = false;
	

	public SubAgentAdapter1(Context context, ArrayList<HashMap<String, Object>> list) {
		this.context = context;
		this.list = list;
	}

	public void getid(int position) {

		this.positions = position;

		notifyDataSetChanged();

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
					R.layout.sub_agent_lit_item1, null);
			holder.agt_name_tv = (TextView) convertView
					.findViewById(R.id.agt_name_tv);
			holder.agt_phone_tv = (TextView) convertView
					.findViewById(R.id.agt_phone_tv);
			holder.agt_rate_tv = (TextView) convertView
					.findViewById(R.id.agt_rate_tv);
			holder.agr_number_tv = (TextView) convertView
					.findViewById(R.id.agr_number_tv);
			holder.agt_war_tv = (TextView) convertView.findViewById(R.id.agt_war_tv);
			holder.tv_rate1 = (TextView) convertView.findViewById(R.id.tv_rate1);
			holder.tv_rate2 = (TextView) convertView.findViewById(R.id.tv_rate2);
			holder.rv_rate_layout = (LinearLayout) convertView.findViewById(R.id.rv_rate_layout);
			convertView.setTag(holder);
		}
		
		
		HashMap map = list.get(position);
		if(map.get("AGTNAM")!=null){
			holder.agt_name_tv .setText(map.get("AGTNAM").toString());
		}else{
			holder.agt_name_tv .setText("未知");
		}
		
		holder.agt_phone_tv .setText(map.get("ACTID").toString());
		holder.agt_rate_tv .setText(map.get("STRATE").toString()+"%");
		holder.agr_number_tv .setText(map.get("TOTACTCOD").toString());
		holder.agt_war_tv .setText(map.get("URGENTFEE").toString()+"%");
		

		if (positions == position) {
			
			if(flag){
				AnimationUtil.leftToRightAnmation(context, holder.rv_rate_layout);
				holder.rv_rate_layout.setVisibility(View.GONE);
				flag = false;
			}else{
				AnimationUtil.leftToRightAnmation(context, holder.rv_rate_layout);
				holder.rv_rate_layout.setVisibility(View.VISIBLE);
				flag = true;

			}
		}
		/*视图内容获取与加载*/
		return convertView;
		
		
	}

	class CabinViewHolder {
		TextView agt_name_tv, agt_phone_tv, agt_rate_tv,agr_number_tv,agt_war_tv,tv_rate1,tv_rate2;
		LinearLayout rv_rate_layout;
	}
}

