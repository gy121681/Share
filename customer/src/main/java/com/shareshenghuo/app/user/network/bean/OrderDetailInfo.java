package com.shareshenghuo.app.user.network.bean;

import java.util.List;

public class OrderDetailInfo {
	public int is_timeout;		//当is_timeout=1 表示订单待支付且优惠劵已经被使用，此时系统自动更新订单的优惠劵为0，客户端应提示用户优惠劵到期，并置优惠劵id=0 。重新调用折扣接口
	public int id;
	public String order_no;
	public int order_type;		//1外卖   2到店消费
	public int user_id;
	public int shop_id;
	public int city_id;
	public String city_name;
	public int area_id;
	public String area_name;
	public String real_name;
	public String mobile;
	public String address;
	public int coupon_id;		//0 未选择优惠券
	public String coupon_name;
	public double coupon_fee;
	public double order_all_fee;
	public double order_real_fee;
	public int status;		//1创建订单（等待付款）  2支付成功 3派送中 4商家已确认 5已上菜 6交易成功 7评价 8用户退款 9退款成功 10退款拒绝 11系统拒绝 0交易取消
	public String remarks;
	public int all_count;
	public int pay_type;	//1余额 2微信支付 3支付宝支付 4线下支付
	public long create_time;
	public String desk_no;
	public String shop_name;
	public List<OrderProdInfo> order_details_list;
}
