package com.shareshenghuo.app.user.network.request;

import com.shareshenghuo.app.user.network.request.BaseRequest;

public class CityRequest extends BaseRequest {
	
	public String parentCode;
	public String user_id;
	public String province_code;
	public String city_code;
	public String area_code;
	@Override
	public String toString() {
		return "CityRequest [parentCode=" + parentCode + ", user_id=" + user_id
				+ ", province_code=" + province_code + ", city_code="
				+ city_code + ", area_code=" + area_code + "]";
	}
}
