package com.shareshenghuo.app.user.network.response;

import java.util.List;

import com.shareshenghuo.app.user.network.bean.ProdInfo;

public class HomeProdListResponse extends BaseResponse {
	
	public HomeProdList data;
	
	public class HomeProdList {
		public List<ProdInfo> product_list_by_limit;
		public List<ProdInfo> product_list_by_host;
	} 
}
