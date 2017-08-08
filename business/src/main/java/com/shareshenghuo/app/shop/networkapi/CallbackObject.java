package com.shareshenghuo.app.shop.networkapi;

/**
 * Created by Snow on 2017/7/21.
 */

public interface CallbackObject<T> extends Callback<T> {

    void onFailure(String msg);

}
