package com.shareshenghuo.app.user.network.request;

import com.shareshenghuo.app.user.network.request.BaseRequest;

public class FractionRequest extends BaseRequest {
	
	public String userId;
	public String userType;
	public String type;
	public String startDate;
	public String endDate;
	public String pageNo;
	public String pageSize;
	public String queryType;
	
	//----临时加入
	public String investmentType;
}
