package com.share.app.entity.response;

/**
 * Created by Snow on 2017/7/25.
 */

public class ManagerShopNumBean {

    /**
     * areaType : 区域类型,1:省;2:市;3:区
     * areaCode : 市或区Code
     *  areaName : 市或区名称
     * shopNum : 市或区商家数量
     */

    private String areaType;
    private String areaCode;
    private String areaName;
    private String shopNum;

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getShopNum() {
        return shopNum;
    }

    public void setShopNum(String shopNum) {
        this.shopNum = shopNum;
    }
}
