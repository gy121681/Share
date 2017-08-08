package com.shareshenghuo.app.user.network.response;

import java.util.List;

import com.shareshenghuo.app.user.network.bean.CircleInfo;

public class MyCircleResponse extends BaseResponse {
	
	public MyCircle data;
	
	public class MyCircle {
		public List<CircleInfo> join_list;
		public List<CircleInfo> create_list;
	}
}
