package com.shareshenghuo.app.shop.network.request;

public class NewMemberResqust extends BaseRequest{
	
	public String userId;
	public String userType;
	public String qrytype;
	public String searchName;
	public String pageNo;
	public String pageSize;
	@Override
	public String toString() {
		return "NewMemberResqust [userId=" + userId + ", userType=" + userType
				+ ", qrytype=" + qrytype + ", startDate=" + searchName
				+ ", pageNo=" + pageNo + ", pageSize=" + pageSize + "]";
	}

}
