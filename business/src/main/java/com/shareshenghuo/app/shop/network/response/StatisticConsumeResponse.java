package com.shareshenghuo.app.shop.network.response;

public class StatisticConsumeResponse extends BaseResponse {
	
	public StatisticConsume data;
	
	public class StatisticConsume {
		public double month;
		public double yesterday;
		public double today;
		public double all;
		public double week;
	}
}
