package com.shareshenghuo.app.user.network.request;

import java.util.ArrayList;
import java.util.List;

public class UpdateCartRequest extends BaseRequest {
	
	public List<UpdateCart> car_list = new ArrayList<UpdateCartRequest.UpdateCart>();
	
	public class UpdateCart {
		public String car_id;
		public String count;
	}
	
	public void add(int carId, int count) {
		UpdateCart item = new UpdateCart();
		item.car_id = carId+"";
		item.count = count+"";
		car_list.add(item);
	}
}
