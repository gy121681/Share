package com.shareshenghuo.app.user.network.bean;

import java.io.Serializable;

public class ShopInfo implements Serializable {
	public int id;
	public int city_id;
	public int shop_type_id;
	public int shop_child_type_id;
	public String shop_type_name;		//一级分类名
	public String shop_child_type_name;	//二级分类名
	public String logo;
	public String big_logo;
	public String shop_name;
	public String mobile;
	public String introduction;			//简介
	public String address;
	public double longitude;
	public double latitude;
	public String certificate_photo;	// 证书，多个逗号隔开
	public int is_integrity;			// 是否诚信商家 （可选）
	public int is_recommend;
	public int is_authentication;		// 是否认证 （可选）
	public int is_muslim;				// 是否清真 （可选）
	public int is_push;					// 是否派送 （可选）
	public int status;
	public long create_time;
	public int browse_count;	// 浏览量
	public int comment_count;	// 评论数
	public String consumption_per_person;	// 人均消费
	public String content;
	public int is_have_coupon;	//是否有优惠券
	public int collect_count;
	public double range;		// 距离  单位km
	public double praise_rate;	// 好评率 1表示100%
	public int is_have_active;	// 是否有活动
	public int is_collect;
	public double lowest_fee;
	public double coupon_fee;	// 满lowest_fee减coupon_fee
	public String area_name;
	public String shop_photo;
	public String shop_end_time;
	public String shop_open_time;
	
	public String is_discount_type_one;
	public String is_discount_type_two;
	public String is_discount_type_three ;
	public String is_consumption;
	
}
