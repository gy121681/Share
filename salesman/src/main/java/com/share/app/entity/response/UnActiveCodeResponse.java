package com.share.app.entity.response;

/**
 * Created by Snow on 2017/7/28.
 */

public class UnActiveCodeResponse {


    /**
     * id : 52
     * act_id : 165165010310
     * pro_id : 激活码编号
     * city_id : 1-65-1
     * area_id : 1-65-1-650103
     * type : 1
     * status : 0
     * own_user_id : 14
     * used_user_id :
     * create_time :
     * activation_time :
     */

    private int id;
    private String act_id;
    private String pro_id;
    private String city_id;
    private String area_id;
    private String type;
    private String status;
    private String own_user_id;
    private String used_user_id;
    private String create_time;
    private String activation_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAct_id() {
        return act_id;
    }

    public void setAct_id(String act_id) {
        this.act_id = act_id;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwn_user_id() {
        return own_user_id;
    }

    public void setOwn_user_id(String own_user_id) {
        this.own_user_id = own_user_id;
    }

    public String getUsed_user_id() {
        return used_user_id;
    }

    public void setUsed_user_id(String used_user_id) {
        this.used_user_id = used_user_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getActivation_time() {
        return activation_time;
    }

    public void setActivation_time(String activation_time) {
        this.activation_time = activation_time;
    }
}
