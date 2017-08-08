package com.shareshenghuo.app.shop.network.bean;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Id;

public class UserInfo implements Serializable {

	@Id
	public int _id;

	public int id; // 帐号id 后续接口有用 名为：admin_id
	public String account;
	public String shop_id;
	public String band_mobile; // 绑定手机号 如果为null 需要跳到绑定界面
	public String user_photo;
	public String nick_name;
	public double today_income; // 今日收入
	public double all_income; // 总收入
	public int city_id;
	public int area_id;
	public double money; // 可提现
	public double with_drawals_ing_fee; // 提现中
	public double with_drawals_ed_fee; // 已提现
	public String alipay_account;
	public int source; // 1 支付宝 2微信
	public int is_set_pay_passwrod; // 是否设置过支付密码
	public String bank_name;
	public String legal_person_name;// 法人
	public String legal_person_no;// 法人身份
	public String card_no;// 卡号
	
	public String mall_login_name;// 商家登录账号
	public String mall_login_password;// 商家登录密码
	public String mall_url;// 商家地址
	public String app_id;// 应用id
	public String mall_shop_id;// 商家店铺id

	@Override
	public String toString() {
		return "UserInfo [_id=" + _id + ", id=" + id + ", account=" + account
				+ ", shop_id=" + shop_id + ", band_mobile=" + band_mobile
				+ ", user_photo=" + user_photo + ", nick_name=" + nick_name
				+ ", today_income=" + today_income + ", all_income="
				+ all_income + ", city_id=" + city_id + ", area_id=" + area_id
				+ ", money=" + money + ", with_drawals_ing_fee="
				+ with_drawals_ing_fee + ", with_drawals_ed_fee="
				+ with_drawals_ed_fee + ", alipay_account=" + alipay_account
				+ ", source=" + source + ", is_set_pay_passwrod="
				+ is_set_pay_passwrod + ", bank_name=" + bank_name
				+ ",mall_login_name" + mall_login_name + ",mall_login_password"
				+ mall_login_password + ",mall_url" + mall_url + ",app_id"
				+ app_id + ",mall_shop_id" + mall_shop_id + "]";
	}
}
