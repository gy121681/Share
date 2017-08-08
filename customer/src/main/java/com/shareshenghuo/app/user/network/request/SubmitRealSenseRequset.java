package com.shareshenghuo.app.user.network.request;

import com.shareshenghuo.app.user.network.request.BaseRequest;

public class SubmitRealSenseRequset extends BaseRequest {
	
	public String action_urls;
	public String user_id;
	public String real_name;
	public String person_no;
	public String self_image_id;
	@Override
	public String toString() {
		return "SubmitRealSenseRequset [action_urls=" + action_urls
				+ ", user_id=" + user_id + ", real_name=" + real_name
				+ ", person_no=" + person_no + ", self_image_id="
				+ self_image_id + "]";
	}
}
