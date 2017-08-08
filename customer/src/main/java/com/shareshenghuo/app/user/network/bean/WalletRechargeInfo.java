package com.shareshenghuo.app.user.network.bean;

public class WalletRechargeInfo {
	public int id;
	public int user_id;
	public double total_fee;
	public long create_time;
	public long update_time;
	public int pay_type;
	public int status;		//0待支付  1支付成功
}
