package com.shareshenghuo.app.user.network.response;

import java.util.List;

import com.shareshenghuo.app.user.network.bean.ActivInfo;
import com.shareshenghuo.app.user.network.bean.CardInfo;
import com.shareshenghuo.app.user.network.bean.CommentInfo;
import com.shareshenghuo.app.user.network.bean.CouponInfo;
import com.shareshenghuo.app.user.network.bean.ShopInfo;

public class ShopDetailResponse extends BaseResponse {
	
	public ShopDetail data;

	public class ShopDetail {
		public List<ActivInfo> active_info;	// 活动（最近的一条）
		public CardInfo car_info;		// 会员卡 
		public List<CommentInfo> comment_list;	// 评论列表
		public List<CouponInfo> coupon_info;
		public ShopInfo shop_info;
	}
}
