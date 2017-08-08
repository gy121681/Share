package com.shareshenghuo.app.user.network.request;

import com.shareshenghuo.app.user.network.request.BaseRequest;

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
	@Override
	public String toString() {
		return "AddCommentRequest [life_circle_id=" + life_circle_id
				+ ", user_id=" + user_id + ", content=" + content
				+ ", is_reply=" + is_reply + ", reply_id=" + reply_id
				+ ", reply_name=" + reply_name + "]";
	}
}
