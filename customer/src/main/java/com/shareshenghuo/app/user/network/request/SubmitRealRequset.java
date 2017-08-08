package com.shareshenghuo.app.user.network.request;

public class SubmitRealRequset extends BaseRequest{
	
	public String user_id;
	public String real_name;
	public String person_no;
	public String id_card_positive_photo;
	public String id_card_nagetive_photo;
	public double latitude;
	public double longitude;
	public String sex;
	public String national;
	public String address;
	public String action_urls;
	public String signOrgan;
	public String validity;
	public String image_id;
	@Override
	public String toString() {
		return "SubmitRealRequset [user_id=" + user_id + ", real_name="
				+ real_name + ", person_no=" + person_no
				+ ", id_card_positive_photo=" + id_card_positive_photo
				+ ", id_card_nagetive_photo=" + id_card_nagetive_photo
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", sex=" + sex + ", national=" + national + ", address="
				+ address + ", action_urls=" + action_urls + "]";
	}
	
}
