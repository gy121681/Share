package com.share.app.entity.request;

import com.share.app.entity.BaseRequest;

/**
 * @author Administrator
 *
 */
public class SetPaypwdRequest extends BaseRequest {
	public String payPassword;
	public String userShopId;
	public String userType;
	public String msgId;
	public String msgCode;
	public String type;
	@Override
	public String toString() {
		return "SetPaypwdRequest [payPassword=" + payPassword + ", userShopId="
				+ userShopId + ", userType=" + userType + ", msgId=" + msgId
				+ ", msgCode=" + msgCode + "]";
	}
	
}
