package com.shareshenghuo.app.user.network.request;

import com.shareshenghuo.app.user.network.request.BaseRequest;

public class FilialobeRequest extends BaseRequest {
	
	public String userId;
	public String userType;
	public String operbType;
	public String opersType;
	public String startDate;
	public String moneyType;
	public String endDate;
	public String pageNo;
	public String pageSize;
	@Override
	public String toString() {
		return "FilialobeRequest [userId=" + userId + ", userType=" + userType
				+ ", operbType=" + operbType + ", opersType=" + opersType
				+ ", startDate=" + startDate + ", moneyType=" + moneyType
				+ ", endDate=" + endDate + ", pageNo=" + pageNo + ", pageSize="
				+ pageSize + "]";
	}
}
