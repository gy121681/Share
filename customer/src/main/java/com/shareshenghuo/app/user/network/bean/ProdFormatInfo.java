package com.shareshenghuo.app.user.network.bean;

import java.util.List;

public class ProdFormatInfo {
	public int id;
	public int product_id;
	public String format_name;
	public double product_new_price;	//现价
	public double product_old_price;	//原价
	public int product_repertory;		//库存
	public int status;					//1默认规格  0 非默认
	public int parent_id;
	public String thum_photo;
	public List<ProdFormatInfo> child_format_list;
}
