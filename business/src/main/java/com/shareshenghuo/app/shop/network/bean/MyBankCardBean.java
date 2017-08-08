package com.shareshenghuo.app.shop.network.bean;

import com.shareshenghuo.app.shop.network.request.BaseRequest;


public class MyBankCardBean extends BaseRequest{
	
	public String bank_name;
	
	public String card_type;
	public String card_no;
	public String tag;
	public String RSPCOD;
	public String RSPMSG;
	public String user_id;
	public String user_type;
	public String is_quickpay;
	public String is_support_withdraw;
	public boolean bos;
	
	public String sign_no;
	public String valid_year;
	public String valid_month;
	public String safe_code;
	public String bank_mobile;
	public String person_no;
	public String user_name;

	@Override
	public String toString() {
		return "MyBankCardBean [bank_name=" + bank_name + ", card_type="
				+ card_type + ", card_no=" + card_no + ", tag=" + tag
				+ ", RSPCOD=" + RSPCOD + ", RSPMSG=" + RSPMSG + ", user_id="
				+ user_id + ", user_type=" + user_type + ", is_quickpay="
				+ is_quickpay + ", bos=" + bos + ", sign_no=" + sign_no
				+ ", valid_year=" + valid_year + ", valid_month=" + valid_month
				+ ", safe_code=" + safe_code + ", bank_mobile=" + bank_mobile
				+ ", person_no=" + person_no + ", user_name=" + user_name + "]";
	}

}
