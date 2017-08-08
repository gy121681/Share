package com.shareshenghuo.app.user.network.request;

import com.shareshenghuo.app.user.network.request.BaseRequest;

public class AutRequset extends BaseRequest {
	public String userId;
	public String realName;
	public String personNo;
	public String bankName;
	public String cardNo;
	public String provinceCode;
	public String cityCode;
	public String areaCode;
	public String userType;
	public String bankKey;
	@Override
	public String toString() {
		return "AutRequset [userId=" + userId + ", realName=" + realName
				+ ", personNo=" + personNo + ", bankName=" + bankName
				+ ", cardNo=" + cardNo + ", provinceCode=" + provinceCode
				+ ", cityCode=" + cityCode + ", areaCode=" + areaCode + "]";
	}
	
}
