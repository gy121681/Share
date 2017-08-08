package com.shareshenghuo.app.shop.network.request;

import com.google.gson.Gson;

public class BaseRequest {

	public String toJson() {
		return new Gson().toJson(this);
	}
}
