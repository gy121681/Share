package com.shareshenghuo.app.user.network.bean;

import java.io.Serializable;

public class CardInfo implements Serializable {
	public int id;
	public int shop_id;
	public String user_id;	// user_id不为null 表示已经领取过
	public String card_no;	//卡号
	public double point;	//用户积分
	public double all_point;//累计积分
	public double money;	//用户余额
	public int level;
	public String level_desc;
	public long create_time;
	public String shop_name;	// 会员卡所属店铺名
}
