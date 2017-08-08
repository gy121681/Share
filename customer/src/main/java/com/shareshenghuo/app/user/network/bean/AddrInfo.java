package com.shareshenghuo.app.user.network.bean;

import java.io.Serializable;

public class AddrInfo implements Serializable {
	public int id;
	public int user_id;
	public String real_name;
	public String mobile;
	public int city_id;
	public String city_name;
	public int area_id;
	public String area_name;
	public String address;
	public int status;	// 1 默认地址
	public long create_time;
}
