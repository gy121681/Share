package com.shareshenghuo.app.shop.network.request;

public class ContactListRequest extends BaseRequest {
	public String user_type;	//1用户 2商户
	public String user_id;
	public String page_size;
	public String page_no;
	public String search_name;	//选填
}
