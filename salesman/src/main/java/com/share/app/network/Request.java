package com.share.app.network;

import android.text.TextUtils;

import com.share.app.entity.request.ActivationCodeRequest;
import com.share.app.entity.request.ActivationdeListRequest;
import com.share.app.entity.request.AddBankCardRequest2;
import com.share.app.entity.request.AgentInfoRequest;
import com.share.app.entity.request.BankCardChangeRequest;
import com.share.app.entity.request.BankInfoRequest;
import com.share.app.entity.request.BankMobileChangeRequest;
import com.share.app.entity.request.FindPasswordRequest;
import com.share.app.entity.request.LoginPwdUpdateRequest;
import com.share.app.entity.request.ManagerShopRequest;
import com.share.app.entity.request.ManagerTradeRequest;
import com.share.app.entity.request.MobileChangeRequest;
import com.share.app.entity.request.MobileCheckRequest;
import com.share.app.entity.request.PayPwdFindRequest;
import com.share.app.entity.request.PayPwdUpdateRequest;
import com.share.app.entity.request.RealnameAuthenticationRequest;
import com.share.app.entity.request.ManagerInfoRequest;
import com.share.app.entity.request.RegistRequest;
import com.share.app.entity.request.SalesmanLoginRequest;
import com.share.app.entity.request.SalesmanRequest;
import com.share.app.entity.request.SetPaypwdRequest;
import com.share.app.entity.request.SubmitIdCardInformationRequest;
import com.share.app.entity.request.SubordinateListRequest;
import com.share.app.entity.request.UnActiveCodeRequest;
import com.share.app.entity.response.ActivationCodeResponse;
import com.share.app.entity.response.ActivationdeListResponse;
import com.share.app.entity.response.BankInfoResponse;
import com.share.app.entity.response.ManagerShopResponse;
import com.share.app.entity.response.ManagerTradeResponse;
import com.share.app.entity.response.MsgResponse;
import com.share.app.entity.response.MyBankCardResponse2;
import com.share.app.entity.response.RealnameAuthenticationResponse;
import com.share.app.entity.response.ManagerInfoResponse;
import com.share.app.entity.response.SAlesmanLoginResponse;
import com.share.app.entity.response.SalesmanInfoResponse;
import com.share.app.entity.response.SetPaypwdResponse2;
import com.share.app.entity.response.SubmitIdCardInformationResponse;
import com.share.app.entity.response.SubordinateListResponse;
import com.share.app.entity.response.UnActiveCodeResponse;
import com.share.app.utils.ParseUtil;
import com.lidroid.xutils.http.client.HttpRequest;
import com.td.qianhai.epay.oem.beans.GetAgentInfoBean;
import com.td.qianhai.epay.oem.beans.HttpUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Snow on 2017/7/20.
 */

public class Request {

    public static final String TAG = Request.class.getSimpleName();
    public static final String URL = HttpUrls.URL;

    public static final HttpRequest.HttpMethod POST = HttpRequest.HttpMethod.POST;
    public static final HttpRequest.HttpMethod GET = HttpRequest.HttpMethod.GET;

    public static void getSalesmanSalesmanLogin(String mobile, String password, CallbackObject<SAlesmanLoginResponse> callbackObject) {
        String url = URL + "oneCity/salesman/salesmanLogin";
        SalesmanLoginRequest req = new SalesmanLoginRequest();
        req.mobile = mobile;
        req.password = password;
        RequestManager.request(req, POST, url, RequestManager.getRequstCallbackObject(callbackObject, SAlesmanLoginResponse.class));
    }


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
        String url = URL + "oneCity/service/submitIdCardInformation";
        RequestManager.request(reqParams, POST, url,
                RequestManager.getRequstCallbackObject(callbackString, SubmitIdCardInformationResponse.class));
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
        req.userType = "3";
        String url = URL + "oneCity/service/setPayPassword";
        RequestManager.request(req, POST, url,
                RequestManager.getRequstCallbackObject(callbackObject, SetPaypwdResponse2.class));
    }

    /**
     * 实名认证
     *
     * @param userId
     * @param callbackObject
     */
    public static void getServiceRealNameAuthentication(String userId,
                                                        CallbackObject<RealnameAuthenticationResponse> callbackObject) {
        String url = URL + "oneCity/service/realNameAuthentication";
        RealnameAuthenticationRequest req = new RealnameAuthenticationRequest();
        req.userId = userId;
        req.userType = "3";
        RequestManager.request(req, POST, url,
                RequestManager.getRequstCallbackObject(callbackObject, RealnameAuthenticationResponse.class));
    }

    /**
     * 查询银行卡信息
     *
     * @param cardId
     * @param userId
     * @param callbackObject
     */
    public static void getServiceQueryBankCardInfo(String cardId, String userId, final CallbackObject<BankInfoResponse> callbackObject) {
        String url = URL + "oneCity/service/queryBankCardInfo";
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


    /**
     * 添加银行止
     *
     * @param userId
     * @param cardNo
     * @param cardType
     * @param bankName
     * @param userName
     * @param personNo
     * @param bankMobile
     * @param validYear
     * @param validMonth
     * @param safeCode
     * @param callbackObject
     */
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
        String url = URL + "oneCity/service/addBankCard";
        AddBankCardRequest2 req = new AddBankCardRequest2();
        req.user_id = userId;
        req.user_type = "3";
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
                RequestManager.getRequstCallbackObject(callbackObject, MyBankCardResponse2.class));
    }

    /**
     * 修改手机号
     *
     * @param account
     * @param password
     * @param msgId
     * @param msgCode
     * @param callbackString
     */
    public static void getSalesmanFindPassword(String account, String password, String msgId, String msgCode, CallbackString callbackString) {
        String url = URL + "oneCity/salesman/salesmanFindPassword";
        FindPasswordRequest req = new FindPasswordRequest();
        req.account = account;
        req.password = password;
        req.msg_id = msgId;
        req.msg_code = msgCode;
        RequestManager.request(req, POST, url, callbackString);

    }

    /**
     * 获取区域经理信息
     *
     * @param userId
     * @param callbackObject
     */
    public static void getBusinessZoneGetRegionalManagerInfo(String userId, CallbackObject<ManagerInfoResponse> callbackObject) {
        String url = URL + "oneCity/salesman/businessZone/getRegionalManagerInfo";
        ManagerInfoRequest request = new ManagerInfoRequest();
        request.userId = userId;
        RequestManager.request(request, POST, url, RequestManager.getRequstCallbackObject(callbackObject, ManagerInfoResponse.class));
    }

    /**
     * 查询区域经理营业数据
     *
     * @param is_search
     * @param trade_date_begin
     * @param trade_date_end
     * @param province_code
     * @param city_code
     * @param area_code
     * @param page_no
     * @param page_size
     * @param callbackList
     */
    public static void getBusinessZoneFindRegionalManagerTrade(String is_search,
                                                               String trade_date_begin,
                                                               String trade_date_end,
                                                               String province_code,
                                                               String city_code,
                                                               String area_code,
                                                               String page_no,
                                                               String page_size,
                                                               CallbackList<List<ManagerTradeResponse>> callbackList) {
        String url = URL + "oneCity/salesman/businessZone/findRegionalManagerTrade";
        ManagerTradeRequest request = new ManagerTradeRequest();
        request.is_search = is_search;
        request.trade_date_begin = trade_date_begin;
        request.trade_date_end = trade_date_end;
        request.province_code = province_code;
        request.city_code = city_code;
        request.area_code = area_code;
        request.page_no = page_no;
        request.page_size = page_size;
        RequestManager.request(request, POST, url, RequestManager.getRequstCallbackList(callbackList, ManagerTradeResponse.class));
    }

    /**
     * 获取区域商家数信息
     *
     * @param areaCode
     * @param callbackObject
     */
    public static void getBusinessZoneFindRegionalManagerShopNum(String areaCode, CallbackObject<ManagerShopResponse> callbackObject) {
        String url = URL + "oneCity/salesman/businessZone/findRegionalManagerShopNum";
        ManagerShopRequest req = new ManagerShopRequest();
        req.area_code = areaCode;
        RequestManager.request(req, POST, url, RequestManager.getRequstCallbackObject(callbackObject, ManagerShopResponse.class));
    }

    /**
     * 获取代理商激活码信息
     *
     * @param userId
     * @param start
     * @param size
     * @param callbackObject
     */
    public static void getMemberManagerActivationCodeInfo(String userId,
                                                          String start,
                                                          String size,
                                                          CallbackObject<ActivationCodeResponse> callbackObject) {
        String url = URL + "oneCity/salesman/memberManager/activationCodeInfo";
        ActivationCodeRequest req = new ActivationCodeRequest();
        req.userId = userId;
        req.start = start;
        req.size = size;
        RequestManager.request(req, POST, url, RequestManager.getRequstCallbackObject(callbackObject, ActivationCodeResponse.class));
    }

    /**
     * 查询已激活列表
     *
     * @param userId
     * @param mercnam
     * @param mobile
     * @param start
     * @param size
     * @param callbackList
     */
    public static void getMemberManagerQueryActivationideList(String userId,
                                                              String mercnam,
                                                              String mobile,
                                                              String start,
                                                              String size,
                                                              CallbackList<List<ActivationdeListResponse>> callbackList) {
        String url = URL + "oneCity/salesman/memberManager/queryActivationdeList";
        ActivationdeListRequest req = new ActivationdeListRequest();
        req.userId = userId;
        req.mercnam = mercnam;
        req.mobile = mobile;
        req.start = start;
        req.size = size;
        RequestManager.request(req, POST, url, RequestManager.getRequstCallbackList(callbackList, ActivationdeListResponse.class));

    }

    /**
     * 会员管理——下级列表查询
     * @param userId
     * @param start
     * @param size
     * @param callbackList
     */
    public static void  getMemberManagerSubordinateList(String userId,
                                                       String name,
                                                       String phone,
                                                       String level,
                                                       String start,
                                                       String size,
                                                       CallbackList<List<SubordinateListResponse>> callbackList){
        String url = URL + "oneCity/salesman/memberManager/subordinateList";
        SubordinateListRequest req = new SubordinateListRequest();
        req.userId = userId;
        req.start = start;
        req.size = size;
        req.level = level;
        RequestManager.request(req, POST, url, RequestManager.getRequstCallbackList(callbackList, SubordinateListResponse.class));
    }

    /**
     * 获取代理商信息
     * @param userId
     * @param callbackObject
     */
    public static void getServiceGetAgentInfo(String userId, CallbackObject<GetAgentInfoBean> callbackObject){
        String url = URL + "oneCity/service/getAgentInfo";
        AgentInfoRequest req = new AgentInfoRequest();
        req.userId = userId;
        RequestManager.request(req, POST, url, RequestManager.getRequstCallbackObject(callbackObject, GetAgentInfoBean.class));
    }

    /**
     * 注册
     * @param mobile
     * @param pwd
     * @param msgId
     * @param msgCode
     * @param activityCode
     * @param callbackString
     */
    public static void getSalesmanSalesmanRegist(String mobile,
                                                 String pwd,
                                                 String msgId,
                                                 String msgCode,
                                                 String activityCode,
                                                 CallbackString callbackString) {
        String url = URL + "oneCity/salesman/salesmanRegister";
        RegistRequest req = new RegistRequest();
        req.mobile = mobile;
        req.password = pwd;
        req.msg_id = msgId;
        req.msg_code = msgCode;
        req.activation_code = activityCode;
        RequestManager.request(req, POST, url, callbackString);
    }

    /**
     * 修改登录密码
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param callbackString
     */
    public static void getSalesmanChangePassword(String userId,
                                           String oldPwd,
                                           String newPwd,
                                           CallbackString callbackString) {
        String url = URL + "oneCity/salesman/salesmanChangePassword";
        LoginPwdUpdateRequest req = new LoginPwdUpdateRequest();
        req.userId = userId;
        req.oldPassword = oldPwd;
        req.newPassword = newPwd;
        RequestManager.request(req, POST, url, callbackString);
    }

    /**
     * 修改支付密码
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param callbackString
     */
    public static void getSalesmanUpdatePayPassword(String userId,
                                                    String oldPwd,
                                                    String newPwd,
                                                    CallbackString callbackString) {
        String url = URL + "oneCity/service/updatePayPassword";
        PayPwdUpdateRequest req = new PayPwdUpdateRequest();
        req.userShopId = userId;
        req.oldPayPassword = oldPwd;
        req.newPayPassword = newPwd;
        req.userType = "3";
        RequestManager.request(req, POST, url, callbackString);
    }

    /**
     * 找回支付密码
     * @param mobile
     * @param password
     * @param msgId
     * @param msgCode
     * @param callbackString
     */
    public static void getSalesmanFindPayPassword(String mobile,
                                                  String password,
                                                  String msgId,
                                                  String msgCode,
                                                  CallbackString callbackString) {
        String url = URL + "oneCity/salesman/salesmanFindPayPassword";
        PayPwdFindRequest req = new PayPwdFindRequest();
        req.mobile = mobile;
        req.password = password;
        req.msg_id = msgId;
        req.msg_code = msgCode;
        RequestManager.request(req, POST, url, callbackString);
    }

    /**
     * 获取业务员信息
     * @param mobile
     * @param callbackObject
     */
    public static void getSalesmanInfo(String mobile, CallbackObject<SalesmanInfoResponse> callbackObject) {
        String url = URL + "oneCity/salesman/getSalesmanInfo";
        SalesmanRequest req = new SalesmanRequest();
        req.mobile = mobile;
        RequestManager.request(req, POST, url, RequestManager.getRequstCallbackObject(callbackObject, SalesmanInfoResponse.class));
    }

    /**
     * 检测手机号是否可注册
     * @param mobile
     * @param callbackString
     */
    public static void getSalesmanCheckMobile(String mobile, CallbackString callbackString) {
        String url = URL + "oneCity/salesman/checkMobile";
        MobileCheckRequest req = new MobileCheckRequest();
        req.mobile = mobile;
        RequestManager.request(req, POST, url, callbackString);
    }

    /**
     * 查询未激活码列表
     * @param userId
     * @param start
     * @param size
     * @param callbackList
     */
    public static void getMemberQueryUnActivationdeList(String userId, String start, String size,
                                                        CallbackList<List<UnActiveCodeResponse>> callbackList){
        String url = URL + "oneCity/salesman/memberManager/queryUnActivationdeList";
        UnActiveCodeRequest req = new UnActiveCodeRequest();
        req.userId = userId;
        req.start = start;
        req.size = size;
        RequestManager.request(req, POST, url, RequestManager.getRequstCallbackList(callbackList, UnActiveCodeResponse.class));
    }

    /**
     * 业务员变更手机号
     * @param oldMobile
     * @param newMobile
     * @param msgId
     * @param msgCode
     * @param callbackString
     */
    public static void getSalesmanChangeUserMobile(String oldMobile,
                                                   String newMobile,
                                                   String msgId,
                                                   String msgCode,
                                                   CallbackString callbackString) {
        String url = URL + "oneCity/salesman/changeUserMobile";
        MobileChangeRequest req = new MobileChangeRequest();
        req.oldMobile = oldMobile;
        req.newMobile = newMobile;
        req.msg_id = msgId;
        req.msg_code = msgCode;
        RequestManager.request(req, POST, url, callbackString);
    }

    /**
     * 用户更改银行预留手机号
     */
    public static void getUsercenterUserChangeBankMobile(String bankNo,
                                                         String mobile,
                                                         CallbackString callbackString) {
        String url = URL + "oneCity/salesman/userCenter/userChangeBankMobile";
        BankMobileChangeRequest req = new BankMobileChangeRequest();
        req.bankNo = bankNo;
        req.mobile = mobile;
        RequestManager.request(req, POST, url, callbackString);
    }

    /**
     * 银行卡变更
     * @param userId
     * @param cardNo
     * @param bankName
     * @param userName
     * @param idCard
     * @param mobile
     * @param callbackObject
     */
    public static void getSalesmanChangeUserBankCard(String userId,
                                                     String cardNo,
                                                     String bankName,
                                                     String userName,
                                                     String idCard,
                                                     String mobile,
                                                     CallbackObject<MsgResponse> callbackObject) {
        String url = URL + "oneCity/salesman/changeUserBankCard";
        BankCardChangeRequest req = new BankCardChangeRequest();
        req.user_id = userId;
        req.card_no = cardNo;
        req.bank_name = bankName;
        req.USER_NAME = userName;
        req.PERSON_NO = idCard;
        req.bank_mobile = mobile;
        RequestManager.request(req, POST, url, RequestManager.getRequstCallbackObject(callbackObject, MsgResponse.class));
    }

}
