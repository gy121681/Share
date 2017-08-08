package com.share.app.entity.response;

/**
 * Created by kezhong.
 * QQ:396926020@qq.com
 * on 2017/7/24
 */

import java.util.List;

/**
 * 区域经理信息数据结构
 */
public class ManagerInfoResponse {


	/**
	 * user_name : 用户名
	 * photo : 头像
	 * province_code : 省编码
	 * province_name : 省名字
	 * city_code : 市编码
	 * city_name : 市名字
	 * areaInfo : [{"area_code":"区县编码","area_name":"区县名字"}]
	 */

	private String user_name;
	private String photo;
	private String province_code;
	private String province_name;
	private String city_code;
	private String city_name;
	private List<AreaInfoBean> areaInfo;

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getProvince_code() {
		return province_code;
	}

	public void setProvince_code(String province_code) {
		this.province_code = province_code;
	}

	public String getProvince_name() {
		return province_name;
	}

	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}

	public String getCity_code() {
		return city_code;
	}

	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public List<AreaInfoBean> getAreaInfo() {
		return areaInfo;
	}

	public void setAreaInfo(List<AreaInfoBean> areaInfo) {
		this.areaInfo = areaInfo;
	}
}
