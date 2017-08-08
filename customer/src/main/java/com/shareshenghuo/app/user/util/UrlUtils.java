package com.shareshenghuo.app.user.util;

import android.text.TextUtils;

import com.shareshenghuo.app.user.networkapi.Api;

/**
 * Created by Snow on 2017/7/20.
 */

public class UrlUtils {

    public static String getUrl(String url){
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        if (url.contains("http")) {
            return url;
        }
        return Api.IMG_HOST+url;
    }


}
