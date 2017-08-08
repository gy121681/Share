package com.shareshenghuo.app.user.network.response;

import java.util.List;

import com.shareshenghuo.app.user.network.bean.PhotoInfo;
import com.shareshenghuo.app.user.network.response.BaseResponse;

public class ShopPhotoResponse extends BaseResponse {

	public ShopPhoto data;
	
	public class ShopPhoto {
		public int diannei_count;
		public List<PhotoInfo> diannei_photo;
		
		public int fuwu_count;
		public List<PhotoInfo> fuwu_photo;
		
		public int mendian_count;
		public List<PhotoInfo> mendian_photo;
		
		public int qita_count;
		public List<PhotoInfo> qita_photo;
		
		public int zhoubian_count;
		public List<PhotoInfo> zhoubian_photo;
	}
}
