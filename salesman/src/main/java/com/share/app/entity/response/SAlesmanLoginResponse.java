package com.share.app.entity.response;

import java.util.List;

/**
 * Created by Snow on 2017/7/24.
 */

public class SAlesmanLoginResponse {


    /**
     * id :
     * mobile :  手机号
     * mercnum : 商家编号
     * mercnam :  商家名
     * type : 类型，0：普通，1：代理商，2：区域经理
     * level :  级别，0：普通，1：高级
     * photo :  头像
     * password :  登录密码
     * payPassword :  支付密码
     * certificationStep : 认证步数,0:还未开始,1:已验证身份证信息,2:已设置支付密码,3:已认证
     * certificationStatus :  认证状态,0:未认证,1:认证未通过,2:认证通过
     * realName : 真实姓名
     * personNo : 身份证号码
     * agentType : 代理类型 1：省代，2：市代，3：区代
     * agentAreaList : ["代理区域"]
     */

    private String id;
    private String mobile;
    private String mercnum;
    private String mercnam;
    private String type;
    private String level;
    private String photo;
    private String password;
    private String payPassword;
    private String certificationStep;
    private String certificationStatus;
    private String realName;
    private String personNo;
    private String agentType;
    private List<String> agentAreaList;

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

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getCertificationStep() {
        return certificationStep;
    }

    public void setCertificationStep(String certificationStep) {
        this.certificationStep = certificationStep;
    }

    public String getCertificationStatus() {
        return certificationStatus;
    }

    public void setCertificationStatus(String certificationStatus) {
        this.certificationStatus = certificationStatus;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPersonNo() {
        return personNo;
    }

    public void setPersonNo(String personNo) {
        this.personNo = personNo;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public List<String> getAgentAreaList() {
        return agentAreaList;
    }

    public void setAgentAreaList(List<String> agentAreaList) {
        this.agentAreaList = agentAreaList;
    }
}
