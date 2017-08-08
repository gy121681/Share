package com.shareshenghuo.app.shop.network.request;

/**
 * @author hang
 * 添加/回复文章评论
 */
public class AddCommentRequest extends BaseRequest {
	public String life_circle_id;
	public String user_id;
	public String content;
	public String is_reply;		//是否为回复内容
	public String reply_id;
	public String reply_name;	//当is_reply=1 必填
}
