package com.share.app.entity.request;

import com.share.app.entity.BaseRequest;

/**
 * Created by Snow on 2017/7/26.
 */

public class PayPwdUpdateRequest extends BaseRequest {


    public String userShopId;
    public String oldPayPassword;
    public String newPayPassword;
    public String userType;

}
