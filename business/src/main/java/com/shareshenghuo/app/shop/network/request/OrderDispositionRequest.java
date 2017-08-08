package com.shareshenghuo.app.shop.network.request;

public class OrderDispositionRequest extends BaseRequest{
	
	public String page_no;
	public String page_size;
	public String shop_id;
	public String pay_status;
	public String discount_rate;
	public String pay_date;
	public String type;
	@Override
	public String toString() {
		return "OrderDispositionRequest [page_no=" + page_no + ", page_size="
				+ page_size + ", shop_id=" + shop_id + ", pay_status="
				+ pay_status + ", discount_rate=" + discount_rate
				+ ", pay_date=" + pay_date + "]";
	}
}
