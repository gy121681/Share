package com.shareshenghuo.app.user.network.response;

import java.util.List;

import com.shareshenghuo.app.user.network.bean.ProdInfo;
import com.shareshenghuo.app.user.network.bean.ShopInfo;

public class HomeDataResponse extends BaseResponse {
	
	public HomeData data;
	
	public class HomeData {
		public List<ProdInfo> product_list_by_hot;	//热门商品列表
		public List<ShopInfo> shop_list_by_recomment;//人气好店
	}
}
