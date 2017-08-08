package com.shareshenghuo.app.user.network.request;

public class RegistRequest extends BaseRequest {

	public String account;
	public String password;
	public String msg_id;
	public String msg_code;
	public String registration_id;	//极光设备号
	public String invitation_code;	//选填 邀请码 
	public String user_id;		// 选填 第三方登录的时候 返回的id
	
	public String longitude;
	public String latitude;
}
