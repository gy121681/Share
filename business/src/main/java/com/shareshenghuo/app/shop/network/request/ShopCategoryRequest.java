package com.shareshenghuo.app.shop.network.request;


public class ShopCategoryRequest extends BaseRequest{
	
	public String shopId;
	public String name;
	public String photo;
	public String id;
	public String description;
	public String price;
	public String model;
	public String typeId;
	public String typeIds;
	public String sorts;
	public String ids;
	public String photoType;
	@Override
	public String toString() {
		return "ShopCategoryRequest [shopId=" + shopId + ", name=" + name
				+ ", photo=" + photo + ", id=" + id + ", description="
				+ description + ", price=" + price + ", model=" + model
				+ ", typeId=" + typeId + ", typeIds=" + typeIds + ", sorts="
				+ sorts + ", ids=" + ids + ", photoType=" + photoType + "]";
	}
}
