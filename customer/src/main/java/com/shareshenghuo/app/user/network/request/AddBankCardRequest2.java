package com.shareshenghuo.app.user.network.request;

public class AddBankCardRequest2 extends AddBankCardRequest{

	public String 	USER_NAME;//：用户名字
	public String 	PERSON_NO;//：身份号

	@Override
	public String toString() {
		return "AddBankCardRequest [user_id=" + user_id + ", user_type="
				+ user_type + ", bank_name=" + bank_name + ", card_no="
				+ card_no + ", english_abbr=" + english_abbr + ", is_quickpay="
				+ is_quickpay + ", card_type=" + card_type + ", bank_mobile="
				+ bank_mobile + ", valid_year=" + valid_year + ", valid_month="
				+ valid_month + ", safe_code=" + safe_code + "]";
	}
}
