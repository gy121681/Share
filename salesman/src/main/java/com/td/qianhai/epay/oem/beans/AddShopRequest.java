package com.td.qianhai.epay.oem.beans;

import com.google.gson.Gson;

public class AddShopRequest {
	
	public String account;
	public String password;
	public String legal_person_name;
	public String legal_person_mobile;
	public String legal_person_email;
	public String legal_person_no;
	public String collect_type;
	public String handle_id;
	public String manager_name;
	public String manager_role;
	public String manager_mobile;
	public String manager_no;
	public String collect_shop_id;
	public String bankName;
	public String bankKey;
	
	
	
	@Override
	public String toString() {
		return "AddShopRequest [account=" + account + ", password=" + password
				+ ", legal_person_name=" + legal_person_name
				+ ", legal_person_mobile=" + legal_person_mobile
				+ ", legal_person_email=" + legal_person_email
				+ ", legal_person_no=" + legal_person_no + ", collect_type="
				+ collect_type + ", handle_id=" + handle_id + ", manager_name="
				+ manager_name + ", manager_role=" + manager_role
				+ ", manager_mobile=" + manager_mobile + ", manager_no="
				+ manager_no + ", collect_shop_id=" + collect_shop_id
				+ ", bankName=" + bankName + ", bankKey=" + bankKey + "]";
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

}
