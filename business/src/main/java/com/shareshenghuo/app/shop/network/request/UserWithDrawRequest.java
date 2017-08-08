package com.shareshenghuo.app.shop.network.request;

public class UserWithDrawRequest extends BaseRequest {
	
	public String userShopId;
	public String userType;
	public String payPassword;
	public String filialMoney;
	public String wdType;
	@Override
	public String toString() {
		return "UserWithDrawRequest [userShopId=" + userShopId + ", userType="
				+ userType + ", payPassword=" + payPassword + ", filialMoney="
				+ filialMoney + ", wdType=" + wdType + "]";
	}
}
