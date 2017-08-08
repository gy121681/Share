package com.shareshenghuo.app.shop.network.bean;

import java.io.Serializable;

public class OrderProdInfo implements Serializable {
	public int id;
	public int order_id;
	public int product_id;
	public String product_name;
	public int product_count;
	public double per_price;
	public String level_desc;
	public double zhe_kou;			//折扣  10表示不打折，此时客户端不显示
	public double zhe_kou_price;
	public int product_format_id;
	public int child_format_id;
	public String format_name;
	public String thum_photo;
}
