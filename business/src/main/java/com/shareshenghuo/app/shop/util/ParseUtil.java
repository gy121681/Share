package com.shareshenghuo.app.shop.util;

import com.alibaba.fastjson.JSON;

/**
 * Created by Snow on 2017/7/21.
 */

public class ParseUtil {

    public static <T> T parseObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

}
