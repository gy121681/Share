package com.td.qianhai.epay.oem.beans;

import com.google.gson.Gson;

public class TurnoverRequest {
	
	public String phone;
	
	public String area_type;
	public String is_search;
	public String trade_date_begin;
	public String trade_date_end;
	public String province_code;
	public String city_code;
	public String area_code;
	public String page_no;
	public String page_size;
	public String trade_date;

	@Override
	public String toString() {
		return "TurnoverRequest [phone=" + phone + ", area_type=" + area_type
				+ ", is_search=" + is_search + ", trade_date_begin="
				+ trade_date_begin + ", trade_date_end=" + trade_date_end
				+ ", province_code=" + province_code + ", city_code="
				+ city_code + ", area_code=" + area_code + ", page_no="
				+ page_no + ", page_size=" + page_size + ", trade_date="
				+ trade_date + "]";
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

}
