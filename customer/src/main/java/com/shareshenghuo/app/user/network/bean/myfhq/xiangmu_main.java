package com.shareshenghuo.app.user.network.bean.myfhq;

/**
 * 
 * @author korea 项目投资的列表
 */
public class xiangmu_main {
	private String id;
	private String project_name;// 项目名字
	private String total_filial_num;
	private String max_filial_num;
	private String min_filial_num;
	private String already_filial_nul;
	private String create_time;// 创建时间
	@Override
	public String toString() {
		return this.project_name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getTotal_filial_num() {
		return total_filial_num;
	}
	public void setTotal_filial_num(String total_filial_num) {
		this.total_filial_num = total_filial_num;
	}
	public String getMax_filial_num() {
		return max_filial_num;
	}
	public void setMax_filial_num(String max_filial_num) {
		this.max_filial_num = max_filial_num;
	}
	public String getMin_filial_num() {
		return min_filial_num;
	}
	public void setMin_filial_num(String min_filial_num) {
		this.min_filial_num = min_filial_num;
	}
	public String getAlready_filial_nul() {
		return already_filial_nul;
	}
	public void setAlready_filial_nul(String already_filial_nul) {
		this.already_filial_nul = already_filial_nul;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
