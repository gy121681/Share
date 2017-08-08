package com.shareshenghuo.app.user.network.response;

import java.util.List;

import com.shareshenghuo.app.user.network.bean.CityInfo;

public class CityListResponse extends BaseResponse {

	public CityList data;
	
	public class CityList {
		public List<CityInfo> hot_city_list;
		public List<CityInfo> city_list;
	}
}
