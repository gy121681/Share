package com.shareshenghuo.app.user.network.request;

import com.shareshenghuo.app.user.network.request.BaseRequest;

public class IntegralRquest extends BaseRequest {
	
	public String userId;
	public String userType;
	public String operbType;
	public String opersType;
	public String startDate;
	public String endDate;
	public String pageNo;
	public String pageSize;
	public String queryType;
	public String channelType;
	
public String integralType;//临时添加,0可,1待激励
	@Override
	public String toString() {
		return "IntegralRquest [userId=" + userId + ", userType=" + userType
				+ ", operbType=" + operbType + ", opersType=" + opersType
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", pageNo=" + pageNo + ", pageSize=" + pageSize
				+ ", queryType=" + queryType + ", channelType=" + channelType
				+",integralType"+integralType+ "]";
	}
	
}
