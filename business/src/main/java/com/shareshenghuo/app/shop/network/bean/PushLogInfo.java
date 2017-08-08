package com.shareshenghuo.app.shop.network.bean;

public class PushLogInfo {
	public int id;
	public int city_id;
	public int shop_id;
	public String push_content;
	public int status;		//0待发送 1成功 2失败
	public long create_time;
	public int push_type;
	public long start_time;	//发送时间
}
