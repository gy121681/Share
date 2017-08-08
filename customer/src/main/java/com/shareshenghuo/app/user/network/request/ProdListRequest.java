package com.shareshenghuo.app.user.network.request;

public class ProdListRequest extends BaseRequest {
	public String product_type_id;		//可选 产品分类id
	public String search_name;			//可选 查询字段
	public String city_id;
	public String shop_id;
	public String page_no;
	public String page_size;
	public String shopId;
	public String goodsTypeId;
	public String typeId;
	@Override
	public String toString() {
		return "ProdListRequest [product_type_id=" + product_type_id
				+ ", search_name=" + search_name + ", city_id=" + city_id
				+ ", shop_id=" + shop_id + ", page_no=" + page_no
				+ ", page_size=" + page_size + ", shopId=" + shopId
				+ ", goodsTypeId=" + goodsTypeId +"typeId = = " +typeId+"]";
	}
	
}
