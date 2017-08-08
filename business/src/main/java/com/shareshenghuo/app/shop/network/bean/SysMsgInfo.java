package com.shareshenghuo.app.shop.network.bean;

public class SysMsgInfo {
	public int id;
	public int reply_id;
	public int user_id;			//不等于0 就是帖子回复
	public int user_type;
	public String content;		//回复内容
	public String create_time;
	public String reply_person;	//回复你的人
	public int life_clrcle_id;	//文章id
	public int status;
	public int is_push;
}
