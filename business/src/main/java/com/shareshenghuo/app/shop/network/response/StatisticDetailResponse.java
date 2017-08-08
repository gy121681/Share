package com.shareshenghuo.app.shop.network.response;

import java.util.List;

import com.shareshenghuo.app.shop.network.bean.StatisticDetailInfo;

public class StatisticDetailResponse extends BaseResponse {
	
	public StatisticDetail data;
	
	public class StatisticDetail {
		public double all_fee;		//总额
		public List<StatisticDetailInfo> result_list;
	}
}
