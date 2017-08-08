package com.td.qianhai.epay.oem.beans;

import com.google.gson.Gson;

public class GetcityRequst {
	
	public String province_code;
	public String city_code;
	public String parent_id;
	public String province_id;
	public String city_id;
	public String parentCode;
	
	
	public String toJson() {
		return new Gson().toJson(this);
	}
}
