package com.shareshenghuo.app.shop.network.bean;

public class ShopInfoBean {
	
	public String id;
	public String city_id;
	public String shop_type_id;
	public String shop_child_type_id;
	public String shop_type_name;
	public String shop_child_type_name;
	public String logo;
	public String shop_name;
	public String mobile;
	public String introduction;
	public String address;
	public String shop_photos;//门店
	public String store_environment_photos;//店内
	public String services_photos;//服务
	public String other_photos;//其他
	public String shop_type;//介绍
	public String content;//内容
	public String shop_address;
	public String risk_money;
//	public String bankCode;
//	public String bankAddress;
	
	public String bank_name;
	public String alipay_account;
	public String bank_user_name;
	public String bank_mobile;
	public String bank_code;
	public String bank_address;
	public String is_consumption;
	public String expiration_date;
	public String temp_risk_money;
	
	
	

	
	@Override
	public String toString() {
		return "ShopInfoBean [id=" + id + ", city_id=" + city_id
				+ ", shop_type_id=" + shop_type_id + ", shop_child_type_id="
				+ shop_child_type_id + ", shop_type_name=" + shop_type_name
				+ ", shop_child_type_name=" + shop_child_type_name + ", logo="
				+ logo + ", shop_name=" + shop_name + ", mobile=" + mobile
				+ ", introduction=" + introduction + ", address=" + address
				+ "]";
	}

}
