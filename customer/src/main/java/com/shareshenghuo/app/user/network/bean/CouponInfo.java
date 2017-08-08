package com.shareshenghuo.app.user.network.bean;

import java.io.Serializable;

public class CouponInfo implements Serializable {
	public int id;
	public int shop_id;
	public String coupon_name;
	public double coupon_fee;
	public double lowest_limit;
	public long effective_start_time;
	public long effective_end_time;
	public int is_push;
	public int coupon_status;		//1【未兑换】    2【未使用】   3【已使用】     4【已过期】
	public String user_id;			//不为null 表示已经领取过
	public int coupon_type;
	public long create_time;
	public String order_no;
	public String use_time;
	public int exchange_count;		//兑换张数
	public int coupon_count;		//优惠劵总数
	public int user_level;			// level>0表示会员专享
	public int is_have = 1;				//是否领取 1是 0否
	public int parent_id;
}
