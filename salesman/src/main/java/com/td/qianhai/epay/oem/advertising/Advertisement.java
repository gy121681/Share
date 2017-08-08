package com.td.qianhai.epay.oem.advertising;


/**
 * 广告实体类
 * 
 * 
 */
public class Advertisement {

	private String imageUrl; // 图片地址
	private String linkUrl; // 链接地址
	private String title; // 标题
	
	public Advertisement(String imageUrl, String linkUrl, String title) {
		super();
		this.imageUrl = imageUrl;
		this.linkUrl = linkUrl;
		this.title = title;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
