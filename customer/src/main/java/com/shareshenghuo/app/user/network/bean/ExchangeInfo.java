package com.shareshenghuo.app.user.network.bean;

public class ExchangeInfo {
	public int id;
	public int gift_id;
	public int user_id;
	public int type;
	public int gift_type;
	public String gift_name;
	public String nick_name;
	public int status;			// 0 待确认  1已兑换
	public long create_time;
	public String real_name;
	public String mobile;
	public String address;
	public String user_account;
	public int all_point;		// 总消费积分
	public int point;			// 兑换积分
}
