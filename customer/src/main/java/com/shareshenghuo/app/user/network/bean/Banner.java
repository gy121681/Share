package com.shareshenghuo.app.user.network.bean;

import java.io.Serializable;

public class Banner implements Serializable {

	private static final long serialVersionUID = 8364152313708014095L;
	
	public int id;
	public int city_id;
	public int shop_id;
	public String banner_name;
	public String banner_imgurl;
	public int banner_place;
	public String content;
	public int banner_order;
	public long create_time;
	public int status;
	public int is_go;
}
