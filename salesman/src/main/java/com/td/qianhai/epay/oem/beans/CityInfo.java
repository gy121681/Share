package com.td.qianhai.epay.oem.beans;

import java.io.Serializable;

public class CityInfo implements Serializable {
	public int id;
	public int province_id;
	public String city_name;
	public long create_time;
	public String jian_pin;
	public String quan_pin;
	public int is_hot;
	public int is_default; //0表示定位到的城市 1表示没有定位到，此时客户端提示未开通对应城市，并默认选择提供的城市信息
}
