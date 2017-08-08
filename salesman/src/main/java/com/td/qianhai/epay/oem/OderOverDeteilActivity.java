package com.td.qianhai.epay.oem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.OrderPayBean;

public class OderOverDeteilActivity extends Activity{
	
	
	private TextView odernum,r_date,counter_Fee,q_date,q_rate,money,counter_fee1,titlename,titleback;
	private OrderPayBean bean;
	private Button btn_order_to;
	private String state;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oder_over_activity);
		AppContext.getInstance().addActivity(this);
		Bundle bundle = getIntent().getExtras();
		
		state = getIntent().getStringExtra("State");
		
		bean = (OrderPayBean) bundle.getSerializable("bean");
		initView();
	}

	private void initView() {

		
		titlename = (TextView) findViewById(R.id.tv_title_contre);
		titlename.setText("订单明细");
		titleback = (TextView) findViewById(R.id.bt_title_left);
		btn_order_to = (Button) findViewById(R.id.btn_order_to);
		if(state.equals("1")){
//			btn_order_to.setVisibility(View.GONE);
		}
		titleback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		
		odernum = (TextView) findViewById(R.id.odernum);
		r_date = (TextView) findViewById(R.id.r_date);
		counter_Fee = (TextView) findViewById(R.id.counter_Fee);
		q_date = (TextView) findViewById(R.id.q_date);
		q_rate = (TextView) findViewById(R.id.q_rate);
		money = (TextView) findViewById(R.id.money);
		counter_fee1 =(TextView) findViewById(R.id.counter_fee1);
		
		if(bean!=null){
			
			if (bean.getOrdAtm().length() == 1) {
				money.setText("0.0" + bean.getOrdAtm());
			} else if (bean.getOrdAtm().length() == 2) {
				money.setText("0." + bean.getOrdAtm());
			} else {
				money.setText(bean.getOrdAtm().substring(0,
						bean.getOrdAtm().length() - 2)
						+ "."
						+ bean.getOrdAtm().substring(
								bean.getOrdAtm().length() - 2));
			}

			
			
			String year = bean.getOrderTim().substring(0, 4);
			String month = bean.getOrderTim().substring(4, 6);
			String day = bean.getOrderTim().substring(6, 8);
			r_date.setText(year + "-" + month + "-" + day);
			String clss = bean.getClssts().toString();
			if(clss.equals("0")){
				counter_fee1.setText("处理中");
			}else{
				counter_fee1.setText("已成功");
			}
			
			String year1 = bean.getOrderTim().substring(0, 4);
			String month1 = bean.getOrderTim().substring(4, 6);
			String day1 = bean.getOrderTim().substring(6, 8);
			q_date.setText(year1 + "-" + month1 + "-" + day1);
			if(bean.getFeerat()!=null){
				q_rate.setText(bean.getFeerat().toString()+"%");
//				if (bean.getFeerat().length() == 1) {
//					q_rate.setText("0.0" + bean.getFeerat().toString()+"%");
//				} else if (bean.getFrramt().length() == 2) {
//					q_rate.setText("0." + bean.getFeerat().toString()+"%");
//				} else {
//					q_rate.setText(bean.getFeerat().substring(0,
//							bean.getFeerat().length() - 2)
////							+ "."
//							+ bean.getFeerat().substring(
//									bean.getFeerat().length() - 2)+"%");
//				}
			}
			
//			q_rate.setText(bean.getFeerat().toString());
			
//			money.setText(bean.getOrdAtm().toString());
			
			
			if (bean.getFrramt().length() == 1) {
				counter_Fee.setText("0.0" + bean.getFrramt().toString());
			} else if (bean.getFrramt().length() == 2) {
				counter_Fee.setText("0." + bean.getFrramt().toString());
			} else {
				counter_Fee.setText(bean.getFrramt().substring(0,
						bean.getFrramt().length() - 2)
						+ "."
						+ bean.getFrramt().substring(
								bean.getFrramt().length() - 2));
			}
//			counter_Fee.setText(bean.getFrramt().toString());
			
			if(bean.getOrderNo()!=null){
				odernum.setText(bean.getOrderNo().toString());
			}else{
				return;
			}
			
		}
		
	}

}
