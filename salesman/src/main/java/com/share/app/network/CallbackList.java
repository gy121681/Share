package com.share.app.network;


/**
 * Created by kezhong.
 * QQ:396926020@qq.com
 * on 2017/7/24
 */

public interface CallbackList<T> extends Callback<T> {

	void onFailure(String msg);

}
