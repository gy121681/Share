package com.shareshenghuo.app.shop.network.bean;

public class FilialobeBean {

	public String money_num;
	public String balance;
	public String operb_type;
	public String opers_type;
	public long create_time;
	public String filial_piety;
	public String nickname;
	public String total_fee;
	public String body;
	public String fee_rate;//比例

	@Override
	public String toString() {

		return "money_num:" + money_num + ",balance:" + balance
				+ ",operb_type:" + operb_type + ",opers_type:" + opers_type
				+ ",create_time:" + create_time + ",filial_piety:"
				+ filial_piety + ",nickname:" + nickname + ",total_fee:"
				+ total_fee + ",body" + body+",fee_rate"+fee_rate;
	}
}
