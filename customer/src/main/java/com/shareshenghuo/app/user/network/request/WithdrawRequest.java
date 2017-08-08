package com.shareshenghuo.app.user.network.request;

import com.shareshenghuo.app.user.network.request.BaseRequest;

public class WithdrawRequest extends BaseRequest {
	public String user_id;
	public String fee;
	public String alipay_account;
	public String user_type;	//1 用户 2商家
	public String source;		//1支付宝 2微信
}
