package com.shareshenghuo.app.shop.networkapi;

/**
 * Created by Snow on 2017/7/20.
 */

public interface Callback<T> {
    /**
     * 请求成功，有返回数据
     * @param data
     */
    void onSuccess(T data);

    /**
     * 失败
     * @param msg
     */
    void onFailure(String msg);

    /**
     * 网络请求失败
     * @param code
     * @param msg
     */
    void onNetError(int code, String msg);

}
