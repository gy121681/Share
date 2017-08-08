package com.shareshenghuo.app.user.network.bean;

import java.io.Serializable;
import com.lidroid.xutils.db.annotation.Id;

public class UserInfo implements Serializable {
	
	@Id
	public int _id;
	
	public int id;
	public String account;
	public String user_photo;
	public String real_name;
	public String nick_name;
	public int sex;		// 1男 2女
	public String mobile;
	public String person_no;	// 身份证
	public double money;
	public int point;
	public int coupon_count;	//优惠券数量
	public long create_time;
	public String band_id;		// 绑定识别码
	public int band_type = 0;		// 绑定类型 0 无绑定 1QQ 2微信
	public String registration_id;	// 极光设备id;
	public String code;	// 邀请码
	public int city_id;
	public int status;	//非1 表示被禁
	public int sign_count;	// 连续签到次数
	public int is_sign;	//是否签到
	public int is_set_pay_passwrod;	//是否设置过支付密码
	public String card_no;
	public String wechat_no;
	public String bank_name;
	public String legal_person_name;//法人
	//认证
	public String token;
	public String is_old_user;
	public String certification_status;
	public String certification_step;
	public String is_can_certification;
	public String area_code;
	public String city_code;
	public String province_code;
	
	public String mall_url;
	public String mall_user_id;
	public String mall_user_password;
	public String app_id;
}
