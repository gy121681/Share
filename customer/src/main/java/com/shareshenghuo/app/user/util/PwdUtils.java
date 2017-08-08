package com.shareshenghuo.app.user.util;

import com.shareshenghuo.app.user.util.MD5Utils;

/**
 * Created by Snow on 2017/7/25.
 */

public class PwdUtils {

    /**
     * 加密密码
     * @param pwd
     * @param count 加密次数 > 1
     * @return
     */
    public static String getEncripyPwd(String pwd, int count) {
        for(int i = 0; i < count; i++) {
            pwd = MD5Utils.getMD5String(pwd);
        }
        return pwd;
    }

}
