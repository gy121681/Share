package com.share.app.entity;

import com.share.app.utils.ParseUtil;

/**
 * Created by Snow on 2017/7/24.
 */

public class BaseRequest {

    public String toJson(){
        return ParseUtil.toJson(this);
    }
}
