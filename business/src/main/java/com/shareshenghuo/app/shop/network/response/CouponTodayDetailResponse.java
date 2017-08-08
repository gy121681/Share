package com.shareshenghuo.app.shop.network.response;

import java.util.List;

import com.shareshenghuo.app.shop.network.bean.CouponTodayStatisticInfo;

public class CouponTodayDetailResponse extends BaseResponse {

	public CouponTodayDetail data;
	
	public class CouponTodayDetail {
		public int all_fee;
		public List<CouponTodayStatisticInfo> result_list;
	}
}
