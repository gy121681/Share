package com.shareshenghuo.app.user.network.request;

import java.util.ArrayList;
import java.util.List;

public class CommentOrderRequest extends BaseRequest {
	public String order_id;
	public String shop_id;
	public List<CommentOrder> comment_list = new ArrayList<CommentOrderRequest.CommentOrder>();
	
	public class CommentOrder {
		public String product_id;
		public String score;
		public String content;
		public String comment_photo;
	}
	
	public void addComment(int pordId, int score, String content, String photos) {
		CommentOrder item = new CommentOrder();
		item.product_id = pordId+"";
		item.score = score+"";
		item.content = content;
		item.comment_photo = photos;
		comment_list.add(item);
	}
}
