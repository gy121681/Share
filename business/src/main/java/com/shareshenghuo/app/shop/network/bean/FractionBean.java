package com.shareshenghuo.app.shop.network.bean;

public class FractionBean {
	
//	public String type;
//	public String filial_num;
//	public long create_time;
//	public String integral;
//	public String project_name;
//	public String trade_type;
//	
//	public String is_special_investment;
//	public String user_shop_name;
	
	public String type;
	public String operb_type;
	public String filial_num;
	public long create_time;
	public String integral;
	public String project_name;
	public String trade_type;
	public String opers_type;
	public String is_special_investment;
	public String user_shop_name;
	@Override
	public String toString() {
		return "FractionBean!type:" + type + ",operb_type" + operb_type
				+ ",filial_num" + filial_num + ",create_time" + create_time
				+ ",integral" + integral + ",project_name" + project_name
				+ ",trade_type" + trade_type + ",opers_type" + opers_type
				+ ",is_special_investment" + is_special_investment
				+ ",user_shop_name" + user_shop_name;
	}
}
