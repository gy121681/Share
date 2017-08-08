package com.shareshenghuo.app.shop.network.request;

public class BusinesstodayRequest extends BaseRequest{
	
	public String shopId;
	public String payDate;
	public String pageNo;
	public String pageSize;
	public String endDate;
	public String startDate;
	@Override
	public String toString() {
		return "BusinesstodayRequest [shopId=" + shopId + ", payDate="
				+ payDate + ", pageNo=" + pageNo + ", pageSize=" + pageSize
				+ ", endDate=" + endDate + ", startDate=" + startDate + "]";
	}

}
