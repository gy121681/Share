package com.shareshenghuo.app.shop.network.response;

public class StatisticVipFilterResponse extends BaseResponse {

	public StatisticVipFilter data;
	
	public class StatisticVipFilter {
		public int collect_count;
		public int collect_all_count;
		public int user_count;
		public int user_all_count;
	}
}
