package com.shareshenghuo.app.shop.network.request;

public class ProdListRequest extends BaseRequest {
	public String product_type_id;		//可选 产品分类id
	public String search_name;			//可选 查询字段
	public String city_id;
	public String shop_id;
	public String page_no;
	public String page_size;
}
