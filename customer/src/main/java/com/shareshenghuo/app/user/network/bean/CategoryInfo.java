package com.shareshenghuo.app.user.network.bean;

import java.util.List;

/**
 * @author hang
 * 分类
 */
public class CategoryInfo {
	public int id;
	public String type_name;
	public int type_order;
	public int parent_id;
	public long create_time;
	public String icon;		// 商户分类图标
	public String type_icon;//活动分类图标
	public int city_id;
	public String is_hot;
	
	public List<CategoryInfo> child_shop_type_list;

	@Override
	public String toString() {
		return "CategoryInfo [id=" + id + ", type_name=" + type_name
				+ ", type_order=" + type_order + ", parent_id=" + parent_id
				+ ", create_time=" + create_time + ", icon=" + icon
				+ ", type_icon=" + type_icon + ", city_id=" + city_id
				+ ", child_shop_type_list=" + child_shop_type_list + "]";
	}
}
