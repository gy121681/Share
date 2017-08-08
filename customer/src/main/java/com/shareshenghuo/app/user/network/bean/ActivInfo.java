package com.shareshenghuo.app.user.network.bean;

import java.io.Serializable;

public class ActivInfo implements Serializable {
	public int id;	//收藏接口这里表示收藏id，活动接口表示活动id
	public int active_id;	//收藏接口才返回该字段
	public int shop_id;
	public int city_id;
	public String active_title;
	public int active_type_id;
	public String active_type;
	public long effective_start_time;
	public long effective_end_time;
	public String address;
	public double longitude;
	public double latitude;
	public String content;
	public int is_top;
	public int status;
	public long create_time;
	public int join_count;
	public int is_join;			// 是否参加
	public String thum_photo;	//列表大图 单张
	public double range;		//距离
	public int browse_count;
	public int is_collect;
}
