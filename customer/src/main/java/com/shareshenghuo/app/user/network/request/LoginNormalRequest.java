package com.shareshenghuo.app.user.network.request;

public class LoginNormalRequest extends BaseRequest {
	public String account;
	public String password;
	public String registration_id;	//极光设备号
	@Override
	public String toString() {
		return "LoginNormalRequest [account=" + account + ", password="
				+ password + ", registration_id=" + registration_id + "]";
	}
}
