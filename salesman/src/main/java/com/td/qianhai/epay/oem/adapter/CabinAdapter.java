package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.beans.FlightBean;

/**
 * cabinAdapter
 * @author liangge
 * 
 */
public class CabinAdapter extends BaseAdapter {
	/*当前的上下文对象*/
	private Context context;
	/*flightBean对象集合*/
	private ArrayList<FlightBean> list;
	/*flightBean对象*/
	private FlightBean flightBean;

	public CabinAdapter(Context context, ArrayList<FlightBean> list) {
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
					R.layout.air_single_cabinlist_item, null);
			holder.tv_cabin = (TextView) convertView
					.findViewById(R.id.tv_cabin);
			holder.tv_discount = (TextView) convertView
					.findViewById(R.id.tv_discount);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			holder.i_listimg = (ImageView) convertView
					.findViewById(R.id.i_listimg);
			convertView.setTag(holder);
		}
		/*显示item最上面的分割线*/
		if(position==0){
			holder.i_listimg.setVisibility(View.VISIBLE);
		}else{
			if(holder.i_listimg.getVisibility()==View.VISIBLE){
				holder.i_listimg.setVisibility(View.GONE);
			}
		}
		/*视图内容获取与加载*/
		flightBean = list.get(position);
		holder.tv_cabin.setText(flightBean.getSpaceType());
		holder.tv_discount.setText(flightBean.getDiscount());
		holder.tv_price.setText(flightBean.getPrice());
		return convertView;
	}

	/**
	 * CabinAdapter视图操作类
	 * @author liangge
	 *
	 */
	class CabinViewHolder {
		TextView tv_cabin, tv_discount, tv_price;
		ImageView i_listimg;
	}
}
