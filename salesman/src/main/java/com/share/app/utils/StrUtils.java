package com.share.app.utils;

import android.text.TextUtils;

/**
 * Created by Snow on 2017/8/1.
 */

public class StrUtils {


    /**
     * 隐藏名称
     *
     * @param name
     * @return
     */
    public static String hideName(String name) {
        String result = name;
        if (!TextUtils.isEmpty(name)) {
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append(name.substring(0, 1));
            sBuilder.append("*");
            result = sBuilder.toString();
        }
        return result;
    }


    /**
     * 隐藏电话号码
     *
     * @param mobile
     * @return
     */
    public static String hideMobile(String mobile) {
        String result = mobile;
        if (!TextUtils.isEmpty(result)) {
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append(mobile.substring(0, 2)).append("****").append(mobile.substring(7));
            result = sBuilder.toString();
        }
        return result;
    }

    /**
     * 隐藏身份证号
     *
     * @param idCard
     * @return
     */
    public static String hideIdCard(String idCard) {
        String result = idCard;
        if (!TextUtils.isEmpty(result) && idCard.length() > 9) {
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append(result.substring(0, 5));
            for (int i = 0, count = result.length() - 9; i < count; i++) {
                sBuilder.append("*");
            }
            sBuilder.append(result.substring(result.length() - 4));
            result = sBuilder.toString();
        }
        return result;
    }

    /**
     * 隐藏银行卡号
     * @param bankNO
     * @return
     */
    public static String hideBankCarNO(String bankNO) {
        String result = bankNO;
        if (bankNO != null && bankNO.length() > 4) {
            StringBuilder sBuilder = new StringBuilder();
            for(int i = 0, count = bankNO.length() - 4; i < count; i++){
                sBuilder.append("*");
            }
            sBuilder.append(bankNO.substring(bankNO.length() - 4));
            result = sBuilder.toString();
        }
        return result;
    }

}
