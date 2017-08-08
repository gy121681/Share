package com.shareshenghuo.app.shop.network.request;

public class RechargeConsumeRequest extends BaseRequest {
	public String admin_id;
	public String user_id;
	public String search_name;	//user_id或者search_name必选一个
	public String shop_id;
	public String type;			//1积分消费 2金额消费  3充值
	public String fee;			//消费金额（积分）
	public String product_name;	//type=1或者2  选填 (多个商品用,分隔)
	public String pay_type = "1";		//1 卡内余额   2 现金消费  选填
}
