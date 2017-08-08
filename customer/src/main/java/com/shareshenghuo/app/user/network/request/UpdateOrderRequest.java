package com.shareshenghuo.app.user.network.request;

public class UpdateOrderRequest extends BaseRequest {
	public String order_id;
	public String user_id;
	public String status;		//0 订单取消 1下单确认 6确认收货 8退款
	public String pay_type;		//1余额 2微信支付 3支付宝支付 4线下支付（现金支付）
	public String desk_no;
}
