package com.shareshenghuo.app.user.network.request;

public class ArticleListRequest extends BaseRequest {
	public String user_id;	//选填，填了用户id表示查看自己发布的圈子列表
	public String city_id;
	public String page_no;
	public String page_size;
}
