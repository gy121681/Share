package com.share.app.model;

import android.support.annotation.Nullable;

import com.share.app.entity.response.SalesmanInfoResponse;

/**
 * Created by Snow on 2017/7/28.
 */

public class SalesmanInfoModel {

    public static SalesmanInfoModel mInstance;

    private SalesmanInfoResponse mSalesmanInfo;

    private SalesmanInfoModel(){

    }

    public static SalesmanInfoModel getInstance(){
        if (mInstance == null) {
            synchronized (SalesmanInfoModel.class) {
                if (mInstance == null) {
                    mInstance = new SalesmanInfoModel();
                }
            }
        }
        return mInstance;
    }

    public void setSalesmanInfo(SalesmanInfoResponse salesmanInfo){
        mSalesmanInfo = salesmanInfo;
    }

    @Nullable
    public SalesmanInfoResponse getSalesmanInfo(){
        return mSalesmanInfo;
    }

    public static void destory(){
        if (mInstance != null) {
            mInstance.mSalesmanInfo = null;
        }
        mInstance = null;
    }
}
