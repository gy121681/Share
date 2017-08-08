package com.shareshenghuo.app.user.network.response;

import java.util.List;

import com.shareshenghuo.app.user.network.bean.ShopInfo;

public class HomeShopListResponse extends BaseResponse {

	public HomeShopList data;
	
	public class HomeShopList {
		public List<ShopInfo> shop_list_by_range;
		public List<ShopInfo> shop_list_by_recomment;
	}
}
