package com.shareshenghuo.app.user.network.bean;

public class WithdrawLogInfo {
	public int id;
	public int user_id;
	public double fee;
	public String alipay_account;
	public int status;		//0未处理 1已处理 2处理失败
	public int source;		//1支付宝 2微信
	public long create_time;
	public long update_time;
	public int user_type;	//1 用户 2商家
	public String mobile;
}
