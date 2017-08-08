package com.shareshenghuo.app.shop.network.request;

public class NumRequest  extends BaseRequest{
	
	public String userType;
	public String userId;
	public String notice_type;
	public String shop_id;
	public String account;
	public String fee_rate;
	public String total_fee;
	public String ids;
	public String money;
	public String shopId;
	@Override
	public String toString() {
		return "NumRequest [userType=" + userType + ", userId=" + userId
				+ ", notice_type=" + notice_type + ", shop_id=" + shop_id
				+ ", account=" + account + ", fee_rate=" + fee_rate
				+ ", total_fee=" + total_fee + ", ids=" + ids + ", money="
				+ money + ", shopId=" + shopId + "]";
	}
}
