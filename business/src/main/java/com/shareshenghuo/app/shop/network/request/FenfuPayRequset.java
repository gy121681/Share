package com.shareshenghuo.app.shop.network.request;


public class FenfuPayRequset extends BaseRequest{
	
	public String mchId;
	public String userId;
	public String totalPrice;
	public String bankAccount;
	@Override
	public String toString() {
		return "FenfuPayRequset [mchId=" + mchId + ", userId=" + userId
				+ ", totalPrice=" + totalPrice + ", bankAccount=" + bankAccount
				+ "]";
	}

}
