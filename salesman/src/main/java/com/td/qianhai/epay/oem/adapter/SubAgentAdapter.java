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

/**
 * 
 */
public class SubAgentAdapter extends BaseAdapter {
	/*当前的上下文对象*/
	private Context context;
	/*flightBean对象集合*/
	private ArrayList<HashMap<String, Object>> list;
	
	private int positions = -1;
	
	private boolean flag = false;
	

	public SubAgentAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
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
					R.layout.sub_agent_lit_item, null);
			holder.agt_name_tv = (TextView) convertView
					.findViewById(R.id.agt_name_tv);
			holder.agt_phone_tv = (TextView) convertView
					.findViewById(R.id.agt_phone_tv);
			holder.agt_rate_tv = (TextView) convertView
					.findViewById(R.id.agt_rate_tv);
			holder.agr_number_tv = (TextView) convertView
					.findViewById(R.id.agr_number_tv);
			holder.mImgLabel = (ImageView) convertView.findViewById(R.id.img_label);
			holder.mImgLabel.setVisibility(View.VISIBLE);
			holder.mLayoutRate = (ViewGroup) convertView.findViewById(R.id.layout_rate);
			holder.mLayoutRate.setVisibility(View.GONE);
			convertView.setTag(holder);
		}
		
		
		HashMap map = list.get(position);
		if(map.get("mercnam")!=null){
			if(map.get("mercnam").toString().equals("null")){
				holder.agt_name_tv .setText("未知");
			}else{
				holder.agt_name_tv .setText(map.get("mercnam").toString());
			}
		}else{
			holder.agt_name_tv .setText("未知");
		}
		if(map.get("merphonenumber")!=null){
			holder.agt_phone_tv .setText(map.get("merphonenumber").toString());
		}else{
			holder.agt_phone_tv .setText("");
		}
		
		String isseniormember = map.get("isseniormember").toString();
		String isretailers = map.get("isretailers").toString();
		String issaleagt = map.get("issaleagt").toString();
		String isgeneralagent = map.get("isgeneralagent").toString();
		
		if(isgeneralagent.equals("1")){
			holder.agt_rate_tv .setText("代理商");
			holder.mImgLabel.setImageResource(R.drawable.share_s_homepage_member_label_label_agent);
		}else{
			if(issaleagt.equals("1")){
				holder.agt_rate_tv .setText("分销商");
				holder.mImgLabel.setImageResource(R.drawable.share_s_homepage_member_label_label_distributor);
			}else{
				if(isretailers.equals("1")){
					holder.agt_rate_tv .setText("零售商");
					holder.mImgLabel.setImageResource(R.drawable.share_s_homepage_member_retail);
				}else{
					if(isseniormember.equals("1")){
						holder.agt_rate_tv .setText("高级会员");
						holder.mImgLabel.setImageResource(R.drawable.share_s_homepage_member_senior);
					}else{
						holder.agt_rate_tv .setText("普通用户");
						holder.mImgLabel.setImageResource(R.drawable.share_s_homepage_member_senior1 );
					}
				}
			}
		}
		
		
		
		
		/*视图内容获取与加载*/
		return convertView;
		
		
	}

	class CabinViewHolder {
		TextView agt_name_tv, agt_phone_tv, agt_rate_tv,agr_number_tv;
		ImageView mImgLabel;
		ViewGroup mLayoutRate;
	}
}
