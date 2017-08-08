package com.shareshenghuo.app.user.network.request;

public class NumRequest  extends BaseRequest{
	
	public String userType;
	public String userId;
	public String notice_type;
	
	@Override
	public String toString() {
		return "NumRequest [userType=" + userType + ", userId=" + userId +"]";
	}
	
}
