package com.shareshenghuo.app.user.network.request;

import com.shareshenghuo.app.user.network.request.BaseRequest;

public class AddrEditRequest extends BaseRequest {
	public String user_id;		//新增地址
	public String address_id;	//修改地址
	public String real_name;
	public String mobile;
	public String city_id;
	public String city_name;
	public String area_id;
	public String area_name;
	public String address;
	public String status;	//  1默认 0非默认
}
