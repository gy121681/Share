package com.shareshenghuo.app.shop.network.request;

public class CardUseLogRequest extends BaseRequest {
	public String user_id;
	public String shop_id;
	public String type;		//1积分消费 2金额消费  3充值
	public String page_no;
	public String page_size;
}
