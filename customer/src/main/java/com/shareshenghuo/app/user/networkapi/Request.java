package com.shareshenghuo.app.user.networkapi;

import android.text.TextUtils;

import com.shareshenghuo.app.user.network.bean.MyBankCardBean;
import com.shareshenghuo.app.user.network.request.AddBankCardRequest2;
import com.shareshenghuo.app.user.network.request.BankInfoRequest;
import com.shareshenghuo.app.user.network.request.RealnameAuthenticationRequest;
import com.shareshenghuo.app.user.network.request.SetPaypwdRequest;
import com.shareshenghuo.app.user.network.request.SubmitIdCardInformationRequest;
import com.shareshenghuo.app.user.network.response.BankInfoResponse;
import com.shareshenghuo.app.user.network.response.MyBankCardResponse;
import com.shareshenghuo.app.user.network.response.MyBankCardResponse2;
import com.shareshenghuo.app.user.network.response.RealnameAuthenticationResponse;
import com.shareshenghuo.app.user.network.response.SetPaypwdResponse2;
import com.shareshenghuo.app.user.network.response.SubmitIdCardInformationResponse;
import com.shareshenghuo.app.user.util.ParseUtil;
import com.lidroid.xutils.http.client.HttpRequest;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.networkapi.CallbackObject;
import com.shareshenghuo.app.user.networkapi.CallbackString;

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
     * 实名认证
     *
     * @param userId
     * @param userType
     * @param realName
     * @param idCardNo
     * @param idCardPositivePhoto
     * @param idCardNagetivePhoto
     * @param callbackString
     */
    public static void getServiceSubmitIdCardInformation(String userId,
                                                         String userType,
                                                         String realName,
                                                         String idCardNo,
                                                         String idCardPositivePhoto,
                                                         String idCardNagetivePhoto, CallbackObject<SubmitIdCardInformationResponse> callbackString) {
        SubmitIdCardInformationRequest reqParams = new SubmitIdCardInformationRequest();
        reqParams.userId = userId;
        reqParams.userType = userType;
        reqParams.realName = realName;
        reqParams.idCardNo = idCardNo;
        reqParams.idCardPositivePhoto = idCardPositivePhoto;
        reqParams.idCardNagetivePhoto = idCardNagetivePhoto;
        String url = Api.URL + "oneCity/service/submitIdCardInformation";
        RequestManager.request(reqParams, POST, url,
                RequestManager.getRequstCallback(callbackString, SubmitIdCardInformationResponse.class));
    }

    /**
     * 设置支付密码
     *
     * @param password
     * @param userShopId
     * @param msgId
     * @param msgCode
     */
    public static void getServiceSetPayPassword(String password,
                                                String userShopId,
                                                String msgId,
                                                String msgCode,
                                                CallbackObject<SetPaypwdResponse2> callbackObject) {
        SetPaypwdRequest req = new SetPaypwdRequest();
        req.type = "1";
        req.payPassword = password;
        req.userShopId = userShopId;
        req.msgId = msgId;
        req.msgCode = msgCode;
        req.userType = "1";
        String url = Api.URL + "oneCity/service/setPayPassword";
        RequestManager.request(req, POST, url,
                RequestManager.getRequstCallback(callbackObject, SetPaypwdResponse2.class));
    }

    /**
     * 实名认证
     * @param userId
     * @param callbackObject
     */
    public static void getServiceRealNameAuthentication(String userId,
                                                        CallbackObject<RealnameAuthenticationResponse> callbackObject){
        String url = Api.URL + "oneCity/service/realNameAuthentication";
        RealnameAuthenticationRequest req = new RealnameAuthenticationRequest();
        req.userId = userId;
        req.userType = "1";
        RequestManager.request(req, POST, url,
                RequestManager.getRequstCallback(callbackObject, RealnameAuthenticationResponse.class));
    }


    /**
     * 原查询银行卡信息
     * @param userId
     * @param userType
     * @param bankName
     * @param cardNo
     */
    public static void getBankName(String userId, String userType, String bankName, String cardNo, CallbackObject<MyBankCardResponse> callbackObject){
        String url = Api.GETBANKNAME;
        MyBankCardBean req = new MyBankCardBean();
        req.user_id = userId;
        req.user_type = userType;
        req.bank_name = bankName;
        req.card_no = cardNo;
        RequestManager.request(req, POST, url,
                RequestManager.getRequstCallback(callbackObject, MyBankCardResponse.class));
    }

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
        req.user_type = "1";
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
