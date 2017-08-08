package com.shareshenghuo.app.user.network.bean;

import com.shareshenghuo.app.user.network.request.BaseRequest;

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
