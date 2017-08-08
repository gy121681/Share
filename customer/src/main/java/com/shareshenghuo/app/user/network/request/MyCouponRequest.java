package com.shareshenghuo.app.user.network.request;

public class MyCouponRequest extends BaseRequest {
	public String user_id;
	public String shop_id;
	public String order_price;
	public String is_default;	//is_default=1 系统返回符合条件的 最高优惠的优惠劵一张
}
