package com.share.app.entity.response;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by Snow on 2017/7/25.
 */

public class ManagerShopResponse {


    /**
     * shop_total_num : 总数
     * List : [{"areaType":"区域类型,1:省;2:市;3:区","areaCode":"市或区Code"," areaName":"市或区名称","shopNum":"市或区商家数量"}]
     */

    public String shop_total_num;
    @JSONField(name = "List")
    public List<ManagerShopNumBean> listX;

}
