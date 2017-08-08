package com.shareshenghuo.app.shop.network.request;

/**
 * @author Administrator
 *
 */
public class SetPaypwdRequest extends BaseRequest{
	public String payPassword;
	public String userShopId;
	public String userType;
	public String msgId;
	public String msgCode;
	@Override
	public String toString() {
		return "SetPaypwdRequest [payPassword=" + payPassword + ", userShopId="
				+ userShopId + ", userType=" + userType + ", msgId=" + msgId
				+ ", msgCode=" + msgCode + "]";
	}
	
}
