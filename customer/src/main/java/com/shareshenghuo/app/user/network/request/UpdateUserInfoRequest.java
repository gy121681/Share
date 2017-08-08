package com.shareshenghuo.app.user.network.request;

import com.shareshenghuo.app.user.network.request.BaseRequest;

public class UpdateUserInfoRequest extends BaseRequest {
	public String user_id;
	public String real_name;
	public String nick_name;
	public String user_photo;
	public String person_no;
	public String mobile;
	public String msg_id;		//电话号码更新 需加msg_id 和 msg_code 验证短信
	public String msg_code;
	public String password;
	public String old_pay_password;	//原支付密码
	public String pay_password;
	public String city_id;		//每当用户选择城市的时候 更新一次
	public String shopId;
	public String discountType;
	public String mchId;
	@Override
	public String toString() {
		return "UpdateUserInfoRequest [user_id=" + user_id + ", real_name="
				+ real_name + ", nick_name=" + nick_name + ", user_photo="
				+ user_photo + ", person_no=" + person_no + ", mobile="
				+ mobile + ", msg_id=" + msg_id + ", msg_code=" + msg_code
				+ ", password=" + password + ", old_pay_password="
				+ old_pay_password + ", pay_password=" + pay_password
				+ ", city_id=" + city_id + ", shopId=" + shopId
				+ ", discountType=" + discountType + ", mchId=" + mchId + "]";
	}
}
