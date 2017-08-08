package com.shareshenghuo.app.user.networkapi;

import com.shareshenghuo.app.user.networkapi.Callback;

/**
 * Created by Snow on 2017/7/21.
 */

public interface CallbackObject<T> extends Callback<T> {

    void onFailure(String msg);

}
