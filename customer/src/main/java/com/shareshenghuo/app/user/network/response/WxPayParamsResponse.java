package com.shareshenghuo.app.user.network.response;

public class WxPayParamsResponse extends BaseResponse {
	
	public WxPayParams data;
	
	public class WxPayParams {
		public long timestamp;
		public String nonce_str;
		public String package_str;
		public String sign_type;
		public String pay_sign;
		public String appid;
		public String partnerid;
	}
}
