package com.shareshenghuo.app.user.network.request;

public class FavorityListRequest extends BaseRequest {
	public String user_id;
	public String collect_type;	//收藏类型 1商家 2活动
	public String page_no;
	public String page_size;
}
