package com.shareshenghuo.app.user.network.bean;

public class OrderProdInfo {
	public int id;
	public int order_id;
	public int product_id;
	public String product_name;
	public int product_count;
	public double per_price;
	public String level_desc;
	public double zhe_kou;			//折扣  10表示不打折，此时客户端不显示
	public double zhe_kou_price;
	public String product_format_id;
	public String child_format_id;
	public String format_name;
	public String thum_photo;
}
