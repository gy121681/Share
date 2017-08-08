package com.shareshenghuo.app.user.network.request;

public class OrderListRequest extends BaseRequest {
	public String user_id;
	public String page_no;
	public String page_size;
	public String status;		//0全部 1待付款 2待收货 3待评价 4已完成
}
