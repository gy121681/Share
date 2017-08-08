package com.shareshenghuo.app.shop.network.request;

public class ShopInfoRequest extends BaseRequest{
	
	public String user_id;
	public String longitude;
	public String latitude;
	public String shop_id;
	public String shopId;
	public String bankCode;
	public String bankAddress;
	@Override
	public String toString() {
		return "ShopInfoRequest [user_id=" + user_id + ", longitude="
				+ longitude + ", latitude=" + latitude + ", shop_id=" + shop_id
				+ ", shopId=" + shopId + ", bankCode=" + bankCode
				+ ", bankAddress=" + bankAddress + "]";
	}
}
