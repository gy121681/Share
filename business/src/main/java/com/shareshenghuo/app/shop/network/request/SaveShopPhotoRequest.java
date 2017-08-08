package com.shareshenghuo.app.shop.network.request;

public class SaveShopPhotoRequest extends BaseRequest{
	
	public String shopId;
	public String shopPhotoUrls;
	public String storeEnvironmentPhotoUrls;
	public String servicesPhotoUrls;
	public String otherPhotoUrl;
	public String logo;
	public String mobile;
	public String content;
	public String photo_type;
	@Override
	public String toString() {
		return "SaveShopPhotoRequest [shopId=" + shopId + ", shopPhotoUrls="
				+ shopPhotoUrls + ", storeEnvironmentPhotoUrls="
				+ storeEnvironmentPhotoUrls + ", servicesPhotoUrls="
				+ servicesPhotoUrls + ", otherPhotoUrl=" + otherPhotoUrl
				+ ", logo=" + logo + ", mobile=" + mobile + ", content="
				+ content + "]";
	}
	
}
