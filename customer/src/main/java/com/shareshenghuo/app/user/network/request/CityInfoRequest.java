package com.shareshenghuo.app.user.network.request;

import com.shareshenghuo.app.user.network.request.BaseRequest;

public class CityInfoRequest extends BaseRequest {
	public double longitude;
	public double latitude;
	@Override
	public String toString() {
		return "CityInfoRequest [longitude=" + longitude + ", latitude="
				+ latitude + "]";
	}
}
