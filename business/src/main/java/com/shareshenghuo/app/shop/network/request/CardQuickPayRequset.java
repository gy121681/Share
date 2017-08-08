package com.shareshenghuo.app.shop.network.request;

public class CardQuickPayRequset extends BaseRequest{
	
	
	public String userId;
	public String mchId;
	public String cardid;
	public String truename;
	public String cre_id;
	public String mobile;
	public String valid_year;
	public String valid_month;
	public String s_code;
	public String money;
	public String sign_no;
	public String clsLogno;
	public String sms_code;
	@Override
	public String toString() {
		return "CardQuickPayRequset [userId=" + userId + ", mchId=" + mchId
				+ ", cardid=" + cardid + ", truename=" + truename + ", cre_id="
				+ cre_id + ", mobile=" + mobile + ", valid_year=" + valid_year
				+ ", valid_month=" + valid_month + ", s_code=" + s_code
				+ ", money=" + money + ", sign_no=" + sign_no + ", clsLogno="
				+ clsLogno + ", sms_code=" + sms_code + "]";
	}
	
}
