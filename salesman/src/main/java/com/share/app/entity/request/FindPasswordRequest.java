package com.share.app.entity.request;

import com.share.app.entity.BaseRequest;

/**
 * Created by Snow on 2017/7/24.
 */

public class FindPasswordRequest extends BaseRequest {

    public String account;//：手机号
    public String password;//：密码
    public String msg_id;//：msgId
    public String msg_code;//：验证码

}
