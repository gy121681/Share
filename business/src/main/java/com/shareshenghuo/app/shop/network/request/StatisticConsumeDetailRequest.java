package com.shareshenghuo.app.shop.network.request;

public class StatisticConsumeDetailRequest extends BaseRequest {
	public String shop_id;
	public String type;			//0 积分获得 1积分消费    2金额消费  3充值
	public String query_type;	//today 今天 yesterday昨天 week本周 month 本月 all全部 custom自定义
	public String start_time;	//query_type=custom 必填   "2015-12-01"
	public String end_time;
}
