package com.shareshenghuo.app.user.network.request;

import com.shareshenghuo.app.user.network.request.BaseRequest;

public class AddToCartRequest extends BaseRequest {
	public String user_id;
	public String product_id;
	public String shop_id;
	public String product_count;
	public String product_format_id;
	public String child_format_id;		//子规格  可选
}
