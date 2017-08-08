package com.shareshenghuo.app.user.network.request;

public class CollectRequest extends BaseRequest {
	public String collect_id;		//要收藏的商家或者活动id
	public String user_id;
	public String collect_type;		//收藏类型 1商家 2活动
}
