package com.shareshenghuo.app.shop.networkapi;

import android.text.TextUtils;

import com.lidroid.xutils.http.client.HttpRequest;
import com.shareshenghuo.app.shop.network.request.AddBankCardRequest2;
import com.shareshenghuo.app.shop.network.request.BankInfoRequest;
import com.shareshenghuo.app.shop.network.response.BankInfoResponse;
import com.shareshenghuo.app.shop.network.response.MyBankCardResponse2;
import com.shareshenghuo.app.shop.util.ParseUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Snow on 2017/7/20.
 */

public class Request {

    public static final String TAG = Request.class.getSimpleName();

    public static final HttpRequest.HttpMethod POST = HttpRequest.HttpMethod.POST;
    public static final HttpRequest.HttpMethod GET = HttpRequest.HttpMethod.GET;


    /**
     * 查询银行卡信息
     * @param cardId
     * @param userId
     * @param callbackObject
     */
    public static void getServiceQueryBankCardInfo(String cardId, String userId, final CallbackObject<BankInfoResponse> callbackObject){
        String url = Api.URL + "oneCity/service/queryBankCardInfo";
        final BankInfoRequest req = new BankInfoRequest();
        req.card_no = cardId;
        req.user_id = userId;
        RequestManager.request(req, POST, url,
                new CallbackString() {
                    @Override
                    public void onFailure(String msg) {
                        callbackObject.onFailure(msg);
                    }

                    @Override
                    public void onSuccess(String data) {
                        if (TextUtils.isEmpty(data)) {
                            callbackObject.onFailure(data);
                        } else {
                            try {
                                JSONObject nood = new JSONObject(data);
                                if (nood.getInt("error_code") == 0) {
                                    callbackObject.onSuccess(ParseUtil.parseObject(data, BankInfoResponse.class));
                                } else {
                                    callbackObject.onFailure(nood.getString("reason"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                callbackObject.onFailure("");
                            }
                        }
                    }

                    @Override
                    public void onNetError(int code, String msg) {
                        callbackObject.onNetError(code, msg);
                    }
                });
    }

    public static void getServiceAddBankCard(String userId,
                                             String cardNo,
                                             String cardType,
                                             String bankName,
                                             String userName,
                                             String personNo,
                                             String bankMobile,
                                             String validYear,
                                             String validMonth,
                                             String safeCode,
                                             CallbackObject<MyBankCardResponse2> callbackObject) {
        String url = Api.URL + "oneCity/service/addBankCard";
        AddBankCardRequest2 req = new AddBankCardRequest2();
        req.user_id = userId;
        req.user_type = "2";
        req.card_no = cardNo;
        req.card_type = cardType;
        req.bank_name = bankName;
        req.USER_NAME = userName;
        req.PERSON_NO = personNo;
        req.bank_mobile = bankMobile;
        req.valid_year = validYear;
        req.valid_month = validMonth;
        req.safe_code = safeCode;
        RequestManager.request(req, POST, url,
                RequestManager.getRequstCallback(callbackObject, MyBankCardResponse2.class ));
    }

}
