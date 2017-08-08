package com.shareshenghuo.app.shop.networkapi;

import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.shareshenghuo.app.shop.network.request.BaseRequest;
import com.shareshenghuo.app.shop.util.ParseUtil;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Snow on 2017/7/21.
 */

public class RequestManager {

    private static String TAG = "RequestManager";

    public static  <T> CallbackString getRequstCallback(final CallbackObject<T> callbackObject, final Class<T> clazz) {
        CallbackString callbackString = new CallbackString() {
            @Override
            public void onSuccess(String data) {
                if (callbackObject != null) {
                    if (TextUtils.isEmpty(data)) {
                        callbackObject.onSuccess(null);
                    } else {
                        callbackObject.onSuccess(ParseUtil.parseObject(data, clazz));
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                if (callbackObject != null) {
                    callbackObject.onFailure(msg);
                }
            }

            @Override
            public void onNetError(int code, String msg) {
                if (callbackObject != null) {
                    callbackObject.onNetError(code, msg);
                }
            }
        };
        return callbackString;
    }



    public static void request(final BaseRequest req, HttpRequest.HttpMethod method, String url, final CallbackString callBack) {
        if (req == null) return;
        RequestParams params = new RequestParams("utf-8");
        try {
            params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Url：" + url);
        Log.d(TAG, "Params：" + req.toJson());
        new HttpUtils().send(method, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (callBack == null) return;
                if (responseInfo.statusCode == 200 && !TextUtils.isEmpty(responseInfo.result)) {
                    Log.d(TAG, "Response：" + responseInfo.result);
                    try {
                        JSONObject root = new JSONObject(responseInfo.result);
                        if (root.getInt("result_code") == Api.SUCCEED){
                            //网络请求成功
                            callBack.onSuccess(root.getString("data"));
                        } else {
                            callBack.onFailure(root.getString("result_desc"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callBack.onFailure(responseInfo.result);
                    }
                } else {
                    callBack.onNetError(responseInfo.statusCode, responseInfo.result);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e(TAG, e.getMessage());
                if (callBack != null) {
                    callBack.onNetError(e.getExceptionCode(), s);
                }
            }
        });
    }

}
