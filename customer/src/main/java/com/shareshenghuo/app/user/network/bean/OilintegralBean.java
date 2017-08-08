package com.shareshenghuo.app.user.network.bean;

import java.util.List;

public class OilintegralBean {
	
	public String channel_name;
	public String  account_name;
	public String  card_no;
	public String  total_fee;
	public String cls_amt;
	public long  create_time;
	public String opers_type;
	public String integral_num;
	public String integral_balance;
	public String collection_date;
	
	public List<OilintegralBean> rather_integral;
	
	public List<RechargeBean> oil_card_periods_list;
	
}
