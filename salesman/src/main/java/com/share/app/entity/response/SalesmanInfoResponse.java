package com.share.app.entity.response;

import java.util.List;

/**
 * Created by Snow on 2017/7/27.
 */

public class SalesmanInfoResponse {


    /**
     * id : 用户id
     * mobile : 手机号
     * mercnum : 商家编号
     * mercnam : 名字
     * type : 0, 类型，0：普通，1：代理商，2：区域经理
     * level : 0, 级别，0：普通，1：高级
     * photo : 头像
     * password : 14e1b600b1fd579f47433b88e8d85291
     * pay_password : 支付密码
     * certification_step : 0, 认证步数,0:还未开始,1:已验证身份证信息,2:已设置支付密码,3:认证完成
     * certification_status : 0,认证状态,0:未认证,1:认证未通过,2:认证通过
     * real_name : 真实姓名
     * person_no : 身份证号码
     * agent_type : 代理类型 1：省代，2：市代，3：区代
     * agent_area_list : ["代理区"]
     * bank_name :
     * bank_no :
     */

    private String id;
    private String mobile;
    private String mercnum;
    private String mercnam;
    private String type;
    private String level;
    private String photo;
    private String password;
    private String pay_password;
    private String certification_step;
    private String certification_status;
    private String real_name;
    private String person_no;
    private String agent_type;
    private List<String> agent_area_list;
    private String bank_name;
    private String bank_no;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMercnum() {
        return mercnum;
    }

    public void setMercnum(String mercnum) {
        this.mercnum = mercnum;
    }

    public String getMercnam() {
        return mercnam;
    }

    public void setMercnam(String mercnam) {
        this.mercnam = mercnam;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPay_password() {
        return pay_password;
    }

    public void setPay_password(String pay_password) {
        this.pay_password = pay_password;
    }

    public String getCertification_step() {
        return certification_step;
    }

    public void setCertification_step(String certification_step) {
        this.certification_step = certification_step;
    }

    public String getCertification_status() {
        return certification_status;
    }

    public void setCertification_status(String certification_status) {
        this.certification_status = certification_status;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getPerson_no() {
        return person_no;
    }

    public void setPerson_no(String person_no) {
        this.person_no = person_no;
    }

    public String getAgent_type() {
        return agent_type;
    }

    public void setAgent_type(String agent_type) {
        this.agent_type = agent_type;
    }

    public List<String> getAgent_area_list() {
        return agent_area_list;
    }

    public void setAgent_area_list(List<String> agent_area_list) {
        this.agent_area_list = agent_area_list;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_no() {
        return bank_no;
    }

    public void setBank_no(String bank_no) {
        this.bank_no = bank_no;
    }
}
