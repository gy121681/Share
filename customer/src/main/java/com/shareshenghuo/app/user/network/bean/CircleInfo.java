package com.shareshenghuo.app.user.network.bean;

import java.io.Serializable;
import java.util.List;

public class CircleInfo implements Serializable {
	public int id;	//group_id 用于加入和退出群组，详情功能用
	public String im_gourp_id;	//环信群组id
	public String im_gourp_photo;	//圈子头像
	public String group_name;
	public String introduction;
	public long create_time;
	public int status;
	public String owner;
	public int owner_id;
	public int is_join;		//是否加入（详情有效）
	
	public List<GroupMemberInfo> group_user_list;
}
