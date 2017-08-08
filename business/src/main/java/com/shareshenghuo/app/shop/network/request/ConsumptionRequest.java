package com.shareshenghuo.app.shop.network.request;

public class ConsumptionRequest extends BaseRequest{
	
	public String shop_id;
	public String payDate;


	@Override
	public String toString() {
		return "ConsumptionRequest [shop_id=" + shop_id + ", payDate="
				+ payDate + "]";
	}
	 
}
