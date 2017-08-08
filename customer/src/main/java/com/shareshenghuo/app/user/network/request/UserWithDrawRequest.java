package com.shareshenghuo.app.user.network.request;

public class UserWithDrawRequest extends BaseRequest {
	
	public String userShopId;
	public String userType;
	public String payPassword;
	public String filialMoney;
	public String user_id;
	public String bank_name;
	public String card_no;
	public String pay_password;
	public String filial_money;
	public String user_type;
	public String wdType;
	public String bankName;
	public String cardNo;
	@Override
	public String toString() {
		return "UserWithDrawRequest [userShopId=" + userShopId + ", userType="
				+ userType + ", payPassword=" + payPassword + ", filialMoney="
				+ filialMoney + ", user_id=" + user_id + ", bank_name="
				+ bank_name + ", card_no=" + card_no + ", pay_password="
				+ pay_password + ", filial_money=" + filial_money
				+ ", user_type=" + user_type + ", wdType=" + wdType + "]";
	}
	
}
