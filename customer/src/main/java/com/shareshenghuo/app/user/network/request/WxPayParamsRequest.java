package com.shareshenghuo.app.user.network.request;

import com.shareshenghuo.app.user.network.request.BaseRequest;

public class WxPayParamsRequest extends BaseRequest {
	public String body;
	public String nonce_str;
	public String spbill_create_ip;
	public String total_fee;
	public String type;
	public String user_id;
	public String order_no;
}
