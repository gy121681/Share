package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.beans.FlightBean;

@SuppressLint("ResourceAsColor")
public class AircraftInfoAdapter extends BaseAdapter {

	private Context context; // 上下文对象
	private ArrayList<FlightBean> list; // 航班信息list
	private FlightBean flightBean; // 航班信息

	public AircraftInfoAdapter(Context context, ArrayList<FlightBean> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		AircraftInfoViewHandler viewHandle = null;
		if (convertView != null) {
			viewHandle = (AircraftInfoViewHandler) convertView.getTag();
		} else {
			viewHandle = new AircraftInfoViewHandler();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.air_single_city_item_n, null);
			viewHandle.tv_aviationName = (TextView) convertView
					.findViewById(R.id.listview1);
			viewHandle.tv_flightNumber = (TextView) convertView
					.findViewById(R.id.listview2);
			viewHandle.tv_aircraftName = (TextView) convertView
					.findViewById(R.id.listview3);
			viewHandle.tv_takeOffDate = (TextView) convertView
					.findViewById(R.id.listview4);
			viewHandle.tv_arriveDate = (TextView) convertView
					.findViewById(R.id.listview5);
			viewHandle.tv_takeOfSite = (TextView) convertView
					.findViewById(R.id.listview6);
			viewHandle.tv_arriveSite = (TextView) convertView
					.findViewById(R.id.listview7);
			viewHandle.tv_price = (TextView) convertView
					.findViewById(R.id.listview8);
			viewHandle.tv_spaceType = (TextView) convertView
					.findViewById(R.id.listview10);
			viewHandle.tv_discount = (TextView) convertView
					.findViewById(R.id.listview9);
			convertView.setTag(viewHandle);
		}
		flightBean = list.get(position);
		viewHandle.tv_aviationName.setText(flightBean.getAviationName());
		viewHandle.tv_flightNumber.setText(flightBean.getFlightNumber());
		viewHandle.tv_aircraftName.setText(flightBean.getAircraftName());
		viewHandle.tv_takeOffDate.setText(flightBean.getTakeOffDate());
		viewHandle.tv_arriveDate.setText(flightBean.getArriveDate());
		viewHandle.tv_takeOfSite.setText(flightBean.getTakeOfSite());
		viewHandle.tv_arriveSite.setText(flightBean.getArriveSite());
		viewHandle.tv_price.setText(flightBean.getPrice());
		viewHandle.tv_spaceType.setText(flightBean.getSpaceType());
		viewHandle.tv_discount.setText(flightBean.getDiscount());
		;
		return convertView;
	}

	private class AircraftInfoViewHandler {
		TextView tv_aviationName, tv_flightNumber, tv_aircraftName,
				tv_takeOffDate, tv_arriveDate, tv_takeOfSite, tv_arriveSite,
				tv_price, tv_spaceType, tv_discount;
	}

}
