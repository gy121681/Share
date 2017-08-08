package com.share.app.utils;

import android.text.TextUtils;

import com.td.qianhai.epay.oem.beans.HttpUrls;

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
        return HttpUrls.IMG_HOST+url;
    }


}
