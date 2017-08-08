package com.shareshenghuo.app.user.network.request;

public class WithdrawLogRequest extends BaseRequest {
	public String page_no;
	public String page_size;
	public String user_id;
	public String user_type;	// 1 用户 2商家
}
