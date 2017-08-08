package com.share.app.utils;

import android.support.annotation.DrawableRes;

import com.td.qianhai.epay.oem.R;

/**
 * Created by Snow on 2017/7/28.
 */

public class BankLogoUtils {


    public static @DrawableRes int getLogo(String bankName){

        if (bankName!= null&&bankName.length()>=2) {
            if (bankName.contains("招商")) {
                return R.drawable.ps_cmb;
            }else if(bankName.contains("农业")) {
                return R.drawable.ps_abc;
            }else if(bankName.contains("农行")){
                return R.drawable.ps_abc;
            }else if(bankName.contains("北京")){
                return R.drawable.ps_bjb;
            }else if(bankName.equals("中国银行")){
                return R.drawable.ps_boc;
            }else if(bankName.contains("建设")){
                return R.drawable.ps_ccb;
            }else if(bankName.contains("光大")){
                return R.drawable.ps_cebb;
            }else if(bankName.contains("兴业")){
                return R.drawable.ps_cib;
            }else if(bankName.contains("中信")){
                return R.drawable.ps_citic;
            }else if(bankName.contains("民生")){
                return R.drawable.ps_cmbc;
            }else if(bankName.contains("交通")){
                return R.drawable.ps_comm;
            }else if(bankName.contains("华夏")){
                return R.drawable.ps_hxb;
            }else if(bankName.contains("广东发展")){
                return R.drawable.ps_gdb;
            }else if(bankName.contains("广发")){
                return R.drawable.ps_gdb;
            }else if(bankName.contains("邮政")){
                return R.drawable.ps_psbc;
            }else if(bankName.contains("邮储")){
                return R.drawable.ps_psbc;
            }else if(bankName.contains("工商")){
                return R.drawable.ps_icbc;
            }else if(bankName.contains("平安")){
                return R.drawable.ps_spa;
            }else if(bankName.contains("浦东")){
                return R.drawable.ps_spdb;
            }else if(bankName.contains("工商")){
                return R.drawable.ps_icbc;
            }else if(bankName.contains("上海")){
                return R.drawable.ps_sh;
            }else{
                return R.drawable.ps_unionpay;
            }
        }
        return R.drawable.ps_unionpay;


    }

}
