package com.shareshenghuo.app.shop.network.response;

import java.util.List;

import com.shareshenghuo.app.shop.network.bean.StatisticDetailInfo;
import com.shareshenghuo.app.shop.network.bean.StatisticMonthInfo;

public class StatisticMonthResponse extends BaseResponse {
	
	public StatisticMonth data;
	
	public class StatisticMonth {
		public double all_fee;		//总额
		public List<StatisticMonthInfo> result_list;
	}
}
