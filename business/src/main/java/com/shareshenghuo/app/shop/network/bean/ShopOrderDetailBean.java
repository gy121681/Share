package com.shareshenghuo.app.shop.network.bean;

import java.util.List;


public class ShopOrderDetailBean {
	public String shop_name;
	public String shop_address;
	public String legal_person_name;
	public String discount_types;
	public String risk_money;
	public String shop_order_num;
	public String shop_order_num_new;
	public List<PaystatusListBean> pay_status_list;
	public List<PaystatusListBean> discount_rate_list;
	@Override
	public String toString() {
		return "ShopOrderDetailBean [shop_name=" + shop_name
				+ ", shop_address=" + shop_address + ", legal_person_name="
				+ legal_person_name + ", discount_types=" + discount_types
				+ ", risk_money=" + risk_money + ", shop_order_num="
				+ shop_order_num + ", pay_status_list=" + "]";
	}
}
