package com.shareshenghuo.app.user.network.request;

import java.util.ArrayList;
import java.util.List;

import com.shareshenghuo.app.user.network.bean.OrderProd;

public class CheckProdZhekouRequest extends BaseRequest {
	
	public String user_id;
	public String shop_id;
	public String order_id;
	public String city_id;
	public String city_name;
	public String area_id;		//选填 外卖订单用
	public String area_name;	//选填 外卖订单用
	public String address;		//选填 外卖订单用
	public String real_name;
	public String mobile;
	public String desk_no;		//选填 桌号
	public String coupon_id;	//没有优惠劵 填0
	public String coupon_fee;	//没有优惠劵 填0
	public String order_type;	//1外卖 2到店消费
	public String order_all_fee;
	public String order_real_fee;
	public String all_count;
	public String remarks;		//选填
	
	public List<OrderProd> product_list = new ArrayList<OrderProd>();
	
	public void addProd(int prodId, String formatId, String childFormatId, int count, String name, String photo, double price) {
		OrderProd prod = new OrderProd();
		prod.product_id = prodId+"";
		prod.format_id = formatId+"";
		prod.child_format_id = childFormatId+"";
		prod.product_count = count+"";
		prod.product_name = name;
		prod.product_photo = photo;
		prod.per_price = price+"";
		product_list.add(prod);
	}
}
