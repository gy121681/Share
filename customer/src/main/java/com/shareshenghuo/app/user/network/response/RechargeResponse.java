package com.shareshenghuo.app.user.network.response;

import com.shareshenghuo.app.user.network.response.BaseResponse;

public class RechargeResponse extends BaseResponse {
	
	public OrderNoInfo data;
	
	public class OrderNoInfo {
		public String order_no;
	}
}
