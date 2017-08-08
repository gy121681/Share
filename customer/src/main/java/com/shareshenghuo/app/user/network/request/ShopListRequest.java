package com.shareshenghuo.app.user.network.request;

public class ShopListRequest extends BaseRequest {
	public String city_id;
	public String longitude;
	public String latitude;
	public String shop_child_type_id;		//二级分类id
	public String order_type;				//排序 1距离 2好评 3人气 4关注
	public String page_no;
	public String page_size;
	public String is_authentication;		//是否认证 （可选）
	public String is_consumption;		//是否认证 （可选）
	public String is_integrity;				//是否诚信商家 （可选
	public String is_muslim;				//是否清真 （可选
	public String is_push;					//是否派送（可选
	public String is_recommend;				//是否推荐（可选
	public String is_have_coupon;			//是否有优惠劵（可选
	public String is_have_active;			//是否有活动（可选
	public String area_id;					//区域（可选
	public String shop_type_id;
	public String is_discount_type_one;
	public String is_discount_type_two;
	public String is_discount_type_three ;
	public String search_name;
	@Override
	public String toString() {
		return "ShopListRequest [city_id=" + city_id + ", longitude="
				+ longitude + ", latitude=" + latitude
				+ ", shop_child_type_id=" + shop_child_type_id
				+ ", order_type=" + order_type + ", page_no=" + page_no
				+ ", page_size=" + page_size + ", is_authentication="
				+ is_authentication + ", is_integrity=" + is_integrity
				+ ", is_muslim=" + is_muslim + ", is_push=" + is_push
				+ ", is_recommend=" + is_recommend + ", is_have_coupon="
				+ is_have_coupon + ", is_have_active=" + is_have_active
				+ ", area_id=" + area_id + ", shop_type_id=" + shop_type_id
				+ ", is_discount_type_one=" + is_discount_type_one
				+ ", is_discount_type_two=" + is_discount_type_two
				+ ", is_discount_type_three=" + is_discount_type_three
				+ ", search_name=" + search_name + "]";
	}
}
