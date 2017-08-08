package com.shareshenghuo.app.shop.network.request;

public class OrderListRequest extends BaseRequest {
	public String shop_id;
	public String status;		//选填参数 0全部 1待接单 2进行中 3已结束
	public String order_no;		//选填参数 搜索订单号或者电话
	public String order_type;	//选填参数   1外卖 2到店消费
	public String start_time;	//选填参数 "2015-11-12" 和end_time一起使用
	public String end_time;
	public String page_no;
	public String page_size;
}
