package com.td.qianhai.epay.oem.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;

/**
 * 订单明细专用dialog
 * 
 * @author liangge
 * 
 */
public class OrderDetailDialog extends Dialog {

	private TextView tvOrderStatus, tvOrdermoney, orderno, ordertimetxt,
			tvPhone;
	private String orderStatus;
	private String ordermoney;
	private String orderNo;
	private String orderTime;
	private String phone;

	private void initView() {
		tvOrderStatus = (TextView) findViewById(R.id.tv_orderstatus);
		tvOrdermoney = (TextView) findViewById(R.id.tv_ordermoney);
		orderno = (TextView) findViewById(R.id.orderno);
		ordertimetxt = (TextView) findViewById(R.id.ordertime);
		tvPhone = (TextView) findViewById(R.id.ordercard);
		tvOrderStatus.setText(orderStatus);
		tvOrdermoney.setText(ordermoney);
		orderno.setText(orderNo);
		ordertimetxt.setText(orderTime);
		tvPhone.setText(phone);
	}

	public OrderDetailDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public OrderDetailDialog(Context context, int theme, String orderStatus,
			String ordermoney, String orderNo, String orderTime, String phone) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.orderStatus = orderStatus;
		this.ordermoney = ordermoney;
		this.orderNo = orderNo;
		this.orderTime = orderTime;
		this.phone = phone;
	}

	public OrderDetailDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_query_detail_item);
		initView();
	}
}
