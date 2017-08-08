package com.shareshenghuo.app.user.network.request;

public class MyCardsRequest extends BaseRequest {
	public String page_size;
	public String page_no;
	public String search_name;	//可选 要查询的商店名称 
	public String user_id;
}
