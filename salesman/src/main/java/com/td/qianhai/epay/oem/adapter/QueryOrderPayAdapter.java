package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.beans.OrderPayBean;

/**
 * 订单支付列表查询Adapter
 * 
 * @author liangge
 * 
 */
public class QueryOrderPayAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<OrderPayBean> orderBeans;
	private OrderPayBean orderBean;

	public void setOrderBeans(ArrayList<OrderPayBean> orderBeans) {
		this.orderBeans = orderBeans;
	}

	public QueryOrderPayAdapter(Context context,
			ArrayList<OrderPayBean> orderBeans) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.orderBeans = orderBeans;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return orderBeans.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		OrderPayViewHandler viewHandler = null;
		if (convertView != null) {
			viewHandler = (OrderPayViewHandler) convertView.getTag();
		} else {
			viewHandler = new OrderPayViewHandler();
			System.out.println("进入的view加载");
			convertView = LayoutInflater.from(context).inflate(
					R.layout.order_query_list_item, null);
			viewHandler.tvOrderStatus = (TextView) convertView
					.findViewById(R.id.tv_orderstatus);
			viewHandler.tvOrdermoney = (TextView) convertView
					.findViewById(R.id.tv_ordermoney);
			viewHandler.orderno = (TextView) convertView
					.findViewById(R.id.orderno);
			viewHandler.ordertimetxt = (TextView) convertView
					.findViewById(R.id.ordertime);
			convertView.setTag(viewHandler);
		}
		orderBean = orderBeans.get(position);
			
		
		if (orderBean.getTransts().equals("0")) {
			viewHandler.tvOrderStatus.setText("支付成功");
		} else if (orderBean.getTransts().equals("1")) {
			viewHandler.tvOrderStatus.setText("初始状态");
		} else if (orderBean.getTransts().equals("2")) {
			viewHandler.tvOrderStatus.setText("超时");
		} else if (orderBean.getTransts().equals("3")) {
			viewHandler.tvOrderStatus.setText("支付失败");
		} else if (orderBean.getTransts().equals("4")) {
			viewHandler.tvOrderStatus.setText("待支付 ");
		} else if (orderBean.getTransts().equals("5")) {
			viewHandler.tvOrderStatus.setText("支付失败");
		} else if (orderBean.getTransts().equals("6")) {
			viewHandler.tvOrderStatus.setText("短信验证失败 ");
		} else if (orderBean.getTransts().equals("7")) {
			viewHandler.tvOrderStatus.setText("付款超时 ");
		} else if (orderBean.getTransts().equals("8")) {
			viewHandler.tvOrderStatus.setText("付款超时");
		} else if (orderBean.getTransts().equals("9")) {
			viewHandler.tvOrderStatus.setText("短信验证超时 ");
		} else if (orderBean.getTransts().equals("10")) {
			viewHandler.tvOrderStatus.setText("");
		}

		if (orderBean.getOrdAtm().length() == 1) {
			viewHandler.tvOrdermoney.setText("0.0" + orderBean.getOrdAtm());
		} else if (orderBean.getOrdAtm().length() == 2) {
			viewHandler.tvOrdermoney.setText("0." + orderBean.getOrdAtm());
		} else {
			viewHandler.tvOrdermoney.setText(orderBean.getOrdAtm().substring(0,
					orderBean.getOrdAtm().length() - 2)
					+ "."
					+ orderBean.getOrdAtm().substring(
							orderBean.getOrdAtm().length() - 2));
		}
		viewHandler.orderno.setText(orderBean.getOrderNo());
		String year = orderBean.getOrderTim().substring(0, 4);
		String month = orderBean.getOrderTim().substring(4, 6);
		String day = orderBean.getOrderTim().substring(6, 8);
		String time = "";
		String t = orderBean.getOrderTim();
		if(t.length()>8){
			time = t.substring(8, 10);
		}else{
			time = t;
		}
		String minute = "";
		String t1 = orderBean.getOrderTim();
		if(t.length()>10){
			minute = t1.substring(10, 12);
		}else{
			minute = t1;
		}
		String seconds = "";
		String t2 = orderBean.getOrderTim();
		if(t.length()>12){
			seconds = t2.substring(12, 14);
		}else{
			seconds = t2;
		}
//		String minute = orderBean.getOrderTim().substring(10, 12);
//		String seconds = orderBean.getOrderTim().substring(12, 14);
		
		viewHandler.ordertimetxt.setText(year + "-" + month + "-" + day
//				+ " " + time + ":" + minute + ":" + seconds
				);
		return convertView;
	}

	class OrderPayViewHandler {
		TextView tvOrderStatus, tvOrdermoney, orderno, ordertimetxt;
	}

}
