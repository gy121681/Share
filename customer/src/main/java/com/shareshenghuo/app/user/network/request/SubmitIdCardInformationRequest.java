package com.shareshenghuo.app.user.network.request;

/**
 * Created by Snow on 2017/7/20.
 */

public class SubmitIdCardInformationRequest extends BaseRequest {

    /** 用户id */
    public String userId;
    /**  用户类型 1：消费者 */
    public String userType = "1";
    /**  真实名字 */
    public String realName;
    /**  身份证号 */
    public String idCardNo;
    /**  身份证正面照（上传返回地址） */
    public String idCardPositivePhoto;
    /**  身份证背面照（上传返回地址） */
    public String idCardNagetivePhoto;

}
