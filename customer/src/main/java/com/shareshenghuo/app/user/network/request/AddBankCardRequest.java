package com.shareshenghuo.app.user.network.request;

import com.shareshenghuo.app.user.network.request.BaseRequest;

public class AddBankCardRequest extends BaseRequest {
	
	public String user_id;
	public String user_type;
	public String bank_name;
	public String card_no;
	public String english_abbr;
	public String is_quickpay;
	public String card_type;
	public String bank_mobile;
	public String valid_year;
	public String valid_month;
	public String safe_code;
	
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
