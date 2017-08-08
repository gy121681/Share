package com.shareshenghuo.app.user.network.bean;

import java.io.Serializable;
import java.util.List;

public class ProdInfo implements Serializable {

	public int id;
	public int city_id;
	public int shop_id;
	public String product_name;
	public String produce_desc;
	public int product_type_id;
	public String product_type;
	public int zhe_kou_level;
	public int point; // 获得积分
	public int is_can_use_coupon;
	public int limit_count; // 限制购买数量
	public int sale_count; // 销量
	public String product_url;
	public String product_photo;
	public String product_content;
	public int all_product_repertory; // 总库存
	public long create_time;
	public String thum_photo; // 缩略图
	public String unit; // 单位
	public int order_count; // 销量
	public double default_old_price; // 原价
	public double default_new_price; // 现价
	public int current_time; // 抢购剩余时间 单位秒
	public String shop_logo;
	public List<CommentInfo> product_comment_list;

	// 抢购
	public int is_grab; // 是否抢购
	public long grab_time;
	public double grab_price; // 抢购价格
	public int grab_count; // 已抢购数
	public int grab_limit_count;// 抢购限制数量
	public int grab_all_count; // 抢购显示的库存

	// 商品分类
	public int default_format_id;
	public int default_child_format_id;

	// 购物车
	public int shop_car_count;
	public int product_id;
	public String shop_name;
	public int format_id;
	public int child_format_id;
	public String format_name;

	public boolean isChecked;
	
	//新
	
	public String photo;
	public String photo_num;
	public String name;
	public String description;
	public String price;
	public String model;
	
	
	
}
