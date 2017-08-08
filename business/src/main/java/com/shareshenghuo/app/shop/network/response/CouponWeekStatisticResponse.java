package com.shareshenghuo.app.shop.network.response;

import java.util.List;

import com.shareshenghuo.app.shop.network.bean.CouponWeekStatisticInfo;

public class CouponWeekStatisticResponse extends BaseResponse {

	public CouponWeekStatistic data;
	
	public class CouponWeekStatistic {
		public int all_fee;
		public List<CouponWeekStatisticInfo> result_list;
	}
}
