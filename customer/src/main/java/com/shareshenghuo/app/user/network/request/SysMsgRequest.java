package com.shareshenghuo.app.user.network.request;

public class SysMsgRequest extends BaseRequest {
	public String user_id;
	public String user_type;	//1用户 2商户
	public String page_no;
	public String page_size;
	public String is_set_zero;	//可选参数 1 获取列表的同时  顺便把所有消息置为已读
}
