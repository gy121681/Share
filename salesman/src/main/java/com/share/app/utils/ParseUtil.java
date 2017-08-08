package com.share.app.utils;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by Snow on 2017/7/24.
 */

public class ParseUtil {

    public static <T> T parseObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    public static <T> String toJson(T data){
        return JSON.toJSONString(data);
    }

    public static <T>List<T> parseList(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

}
