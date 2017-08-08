package com.shareshenghuo.app.user.network.request;

import com.shareshenghuo.app.user.network.request.BaseRequest;

public class CityActivListRequest extends BaseRequest {
	public String city_id;
	public String user_id;		//可选
	public String longitude;
	public String latitude;
	public String order_type;	//排序 1距离 2好评 3人气 4关注
	public String page_no;
	public String page_size;
	public String is_authentication;	//是否认证 （可选）
	public String is_integrity;			//是否诚信商家 （可选
	public String is_muslim;			//是否清真 （可选
	public String is_push;				//是否派送（可选
	public String active_type;			//活动分类（可选
	public String area_id;				//区域（可选
}
