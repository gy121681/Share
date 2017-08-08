package com.share.app.entity.response;

/**
 * Created by Snow on 2017/7/25.
 */

public class ActivationdeListResponse {

    /**
     * id :
     * mobile : 手机号
     * mercnum : 商户编号
     * mercnam : 姓名
     * photo : 头像
     * level : 级别，0：普通，1：高级
     * date : 日期
     */

    private String id;
    private String mobile;
    private String mercnum;
    private String mercnam;
    private String photo;
    private String level;
    private String date;

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
