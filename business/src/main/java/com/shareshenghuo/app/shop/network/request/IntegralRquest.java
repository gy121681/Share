package com.shareshenghuo.app.shop.network.request;

public class IntegralRquest extends BaseRequest{
	
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
	public String integralType;
	@Override
	public String toString() {
		return "IntegralRquest [userId=" + userId + ", userType=" + userType
				+ ", operbType=" + operbType + ", opersType=" + opersType
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", pageNo=" + pageNo + ", pageSize=" + pageSize + "]";
	}
	
}
