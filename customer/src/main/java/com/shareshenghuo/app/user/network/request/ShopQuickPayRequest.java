package com.shareshenghuo.app.user.network.request;

public class ShopQuickPayRequest extends BaseRequest {
	public String shop_id;
	public String pay_all_fee;
	public String pay_real_fee;
	public String pay_type;		//1余额 2微信支付 3支付宝支付
	public String user_id;
}
