package com.shareshenghuo.app.shop.function.mallSettled;

import java.io.Serializable;

public class MallStatus implements Serializable {
	private int status;// 状态
	private int result_code;// 数据code
	private String result_desc;// 请求数据情况

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getResult_code() {
		return result_code;
	}

	public void setResult_code(int result_code) {
		this.result_code = result_code;
	}

	public String getResult_desc() {
		return result_desc;
	}

	public void setResult_desc(String result_desc) {
		this.result_desc = result_desc;
	}
@Override
public String toString() {
	// TODO Auto-generated method stub
	return "status:"+status+",result_code:"+result_code+",result_desc:"+result_desc;
}
}
