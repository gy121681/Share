package com.shareshenghuo.app.user.network.request;

public class RecommendedmemberRequest extends BaseRequest{
	
	
	public String userId;	
	public String userType;
	public String qrytype;
	public String searchName;
	public String startDate;
	public String endDate;
	public String pageNo;
	public String pageSize;
	@Override
	public String toString() {
		return "RecommendedmemberRequest [userId=" + userId + ", userType="
				+ userType + ", qrytype=" + qrytype + ", searchName="
				+ searchName + ", startDate=" + startDate + ", endDate="
				+ endDate + ", pageNo=" + pageNo + ", pageSize=" + pageSize
				+ "]";
	}
}
