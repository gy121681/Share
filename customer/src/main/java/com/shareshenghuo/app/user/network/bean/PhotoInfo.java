package com.shareshenghuo.app.user.network.bean;

public class PhotoInfo {
	public int id;
	public int shop_id;
	public String shop_photo;
	public int photo_type;
	public long create_time;
	@Override
	public String toString() {
		return "PhotoInfo [id=" + id + ", shop_id=" + shop_id + ", shop_photo="
				+ shop_photo + ", photo_type=" + photo_type + ", create_time="
				+ create_time + "]";
	}
}
