package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;


import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.activity.realname.RealnameStepIDCardActivity;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.FenfuPayRequset;
import com.shareshenghuo.app.user.network.bean.GetShopPayInfoBean;
import com.shareshenghuo.app.user.network.bean.MyBankCardBean;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.network.bean.WebLoadActivity;
import com.shareshenghuo.app.user.network.request.AccountInfoRequest;
import com.shareshenghuo.app.user.network.request.CardQuickPayRequset;
import com.shareshenghuo.app.user.network.request.UpdateUserInfoRequest;
import com.shareshenghuo.app.user.network.response.AccountInfoRespones;
import com.shareshenghuo.app.user.network.response.AutResponse;
import com.shareshenghuo.app.user.network.response.BankCardListResponse;
import com.shareshenghuo.app.user.network.response.GetShopPayInfoResponse;
import com.shareshenghuo.app.user.network.response.bankCardQuickPayResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.Util;
import com.shareshenghuo.app.user.util.ViewUtil;
import com.shareshenghuo.app.user.widget.dialog.BankListChoiceDialog;
import com.shareshenghuo.app.user.widget.dialog.EidtDialog;
import com.shareshenghuo.app.user.widget.dialog.OnEditDialogChlicListener;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.user.widget.dialog.TwoButtonDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentnewActivity extends BaseTopActivity implements OnClickListener {

    public final static int PAY_REQ = 10001;

    private LinearLayout ll_card;
    private TextView tv_name, tv_type, cardname, pro;
    private RatingBar rb_normal;
    private ImageView cardimg;
    private EditText balance;
    private List<MyBankCardBean> datas;
    private BankListChoiceDialog multiChoiceDialog;
    private String bankname, banknum = "", shopId = "", discountTypes = "", mchId = "";
    private String mchIds = "", shopnames = "";
    private TwoButtonDialog downloadDialog;
    private String cre_ids = "", mobiles = "", valid_years = "", valid_months = "", s_codes = "",
            sign_nos = "", clsLognos = "", user_names = "";
    private EidtDialog doubleWarnDialogs;
    public static Activity context;
    private BankListChoiceDialog.Builder multiChoiceDialogBuilder;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.payment_new_activity);
        context = this;
        shopId = getIntent().getStringExtra("shopId");
        discountTypes = getIntent().getStringExtra("discountType");
        mchId = getIntent().getStringExtra("mchId");

        initview();
        // TODO 2017/07/27 修改，不需要请求绑定的银行卡
        // loadData();
        loadUserInfo();
        userAccountInfoQuery();
    }

    private void initpay() {
        // TODO Auto-generated method stub
        UserInfo userInfo = UserInfoManager.getUserInfo(this);
        if (userInfo.certification_status != null && !userInfo.certification_status.equals("2")) {
            initDialog("为了保证您的资金安全\n在交易前需进行实名认证", "取消", "确定");
        }
    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        initpay();
        // TODO 2017/07/27 修改，不需要请求绑定的银行卡
        //loadData();
    }


    private void userAccountInfoQuery() {
        // TODO Auto-generated method stub

//		ProgressDialogUtil.showProgressDlg(this, "请稍后");
        AccountInfoRequest req = new AccountInfoRequest();
        req.otherGroup = "USER_MONEY";
        RequestParams params = new RequestParams("utf-8");
        try {
            params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new HttpUtils().send(HttpMethod.POST, Api.GENEROLIST, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                ProgressDialogUtil.dismissProgressDlg();
                T.showNetworkError(PaymentnewActivity.this);
            }

            @Override
            public void onSuccess(ResponseInfo<String> resp) {
                ProgressDialogUtil.dismissProgressDlg();
                Log.e("", "" + resp.result);
                AccountInfoRespones bean = new Gson().fromJson(resp.result, AccountInfoRespones.class);
                if (Api.SUCCEED == bean.result_code) {
                    if (bean.data != null && bean.data.size() > 0) {
                        String USER_DAY_MONEY = "";
                        String USER_ONCE_MONEY = "";
                        for (int i = 0; i < bean.data.size(); i++) {
                            if (bean.data.get(i).other_key.equals("USER_DAY_MONEY")) {
                                USER_DAY_MONEY = bean.data.get(i).other_value;
                            }
                            if (bean.data.get(i).other_key.equals("USER_ONCE_QUICKPAY_MONEY")) {
                                USER_ONCE_MONEY = bean.data.get(i).other_value;
                            }

                            pro.setText("消费单笔限额:" + Util.getfotmatnum(USER_ONCE_MONEY, false, 1) + "," + "日限额:" + Util.getfotmatnum(USER_DAY_MONEY, false, 1));
                        }
                    }

//					T.showShort(ExchangeAcyivity.this, "提交成功");
//					inittext(bean.data);
                } else {
                    T.showShort(PaymentnewActivity.this, bean.result_desc);
                }
            }

        });
    }


    public void bankCardQuickPay() {


        if (TextUtils.isEmpty(banknum)) {
            T.showShort(getApplicationContext(), "请选择银行");
            return;
        }
        if (TextUtils.isEmpty(balance.getText())) {
            T.showShort(getApplicationContext(), "请填写金额");
            return;
        }


        try {
            double bala = Double.parseDouble(balance.getText().toString());
            if (bala <= 0) {
                T.showShort(getApplicationContext(), "金额不能为0");
                return;
            } else if (bala < 0.1) {
                T.showShort(getApplicationContext(), "金额不能为小于0.1");
                return;
            }
        } catch (Exception e) {
            // TODO: handle exception
            T.showShort(getApplicationContext(), "金额有误");
            return;
        }


        ProgressDialogUtil.showProgressDlg(this, "请稍后");
        ProgressDialogUtil.setCancelable(false);

        CardQuickPayRequset req = new CardQuickPayRequset();
        req.userId = UserInfoManager.getUserInfo(this).id + "";
        req.mchId = mchIds;
        req.cardid = banknum;
        req.truename = user_names;
        req.cre_id = cre_ids;
        req.mobile = mobiles;
        req.valid_year = valid_years;
        req.valid_month = valid_months;
        req.s_code = s_codes;
        req.money = String.valueOf((int) (Double.parseDouble(balance.getText().toString()) * 100));
        req.sign_no = sign_nos;

        Log.e("", " - - - - -" + req.toString());
        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        new HttpUtils().send(HttpMethod.POST, Api.BANKCARDQUICKPAY, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                ProgressDialogUtil.dismissProgressDlg();
                T.showNetworkError(PaymentnewActivity.this);
            }

            @Override
            public void onSuccess(ResponseInfo<String> resp) {
                ProgressDialogUtil.dismissProgressDlg();
                Log.e("", "" + resp.result);
                bankCardQuickPayResponse bean = new Gson().fromJson(resp.result, bankCardQuickPayResponse.class);
                if (Api.SUCCEED == bean.result_code) {
                    if (bean.data.RSPCOD.equals("000000")) {
//						T.showShort(getApplicationContext(), bean.data.RSPMSG);
//						T.showShort(getApplicationContext(), bean.data.RSPMSG);
                        clsLognos = bean.data.clsLogno;
                        showdilog();
                    } else {
                        T.showShort(getApplicationContext(), bean.data.RSPMSG);
//						showdilog();
                    }
                }
            }
        });
    }

    private void onfirm(String code) {
        // TODO Auto-generated method stub
        ProgressDialogUtil.showProgressDlg(this, "请稍后");
        ProgressDialogUtil.setCancelable(false);
        CardQuickPayRequset req = new CardQuickPayRequset();
        req.clsLogno = clsLognos;
        req.sms_code = code;
        req.sign_no = sign_nos;

        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(req.toJson()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        new HttpUtils().send(HttpMethod.POST, Api.BANKCARDQUICKPAYCONFIRM, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                ProgressDialogUtil.dismissProgressDlg();
                T.showNetworkError(PaymentnewActivity.this);
            }

            @Override
            public void onSuccess(ResponseInfo<String> resp) {
                ProgressDialogUtil.dismissProgressDlg();
                Log.e("", "" + resp.result);
                AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
                if (Api.SUCCEED == bean.result_code) {
                    Intent it = new Intent(PaymentnewActivity.this, PayIsCompleteAvtivty.class);
                    if (bean.data.RSPCOD.equals("000000")) {
//							T.showShort(getApplicationContext(), bean.data.RSPMSG);
//							207305
                        it.putExtra("tag", "1");
                        it.putExtra("colsno", clsLognos);
                        it.putExtra("balac", balance.getText().toString());
                        it.putExtra("name", shopnames);
                        it.putExtra("msg", bean.data.RSPMSG);
                        finish();
                        startActivity(it);

                    } else {
                        it.putExtra("tag", "0");
                        it.putExtra("balac", balance.getText().toString());
                        it.putExtra("colsno", clsLognos);
                        it.putExtra("name", shopnames);
                        it.putExtra("msg", bean.data.RSPMSG);
                        startActivity(it);
//							T.showShort(getApplicationContext(), bean.data.RSPMSG);
                    }
                }
            }
        });
    }

    private void setvcode() {
        // TODO Auto-generated method stub

        CardQuickPayRequset req = new CardQuickPayRequset();
        req.clsLogno = clsLognos;

        Log.e("", " - - - - -" + req.toString());
        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(req.toJson()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        new HttpUtils().send(HttpMethod.POST, Api.SMSRESEND, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                ProgressDialogUtil.dismissProgressDlg();
                T.showNetworkError(PaymentnewActivity.this);
            }

            @Override
            public void onSuccess(ResponseInfo<String> resp) {
                ProgressDialogUtil.dismissProgressDlg();
                Log.e("", "" + resp.result);
                AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
                if (Api.SUCCEED == bean.result_code) {
                    if (bean.data.RSPCOD.equals("000000")) {
                        T.showShort(getApplicationContext(), bean.data.RSPMSG);
                    } else {
                        T.showShort(getApplicationContext(), bean.data.RSPMSG);
                    }
                }
            }
        });
    }


    public void loadUserInfo() {
//		ProgressDialogUtil.showProgressDlg(activity, "");


        UpdateUserInfoRequest req = new UpdateUserInfoRequest();
//		EncryptionUtil.getEncryptionstring(req.user_id);
//		req.isEncrypt = "1";
//		req.info = EncryptionUtil.getEncryptionstring(req.user_id);
        req.shopId = shopId;
        req.discountType = discountTypes;
        req.mchId = mchIds;
        Log.e("", "shopId - - - - -" + shopId);
        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(req.toJson()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        new HttpUtils().send(HttpMethod.POST, Api.QUERYSHOPINFOCONTROLLER, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                ProgressDialogUtil.dismissProgressDlg();
                T.showNetworkError(PaymentnewActivity.this);
            }

            @Override
            public void onSuccess(ResponseInfo<String> resp) {
                ProgressDialogUtil.dismissProgressDlg();
                Log.e("", "" + resp.result);
                GetShopPayInfoResponse bean = new Gson().fromJson(resp.result, GetShopPayInfoResponse.class);
                if (Api.SUCCEED == bean.result_code) {
                    updateView(bean.data);
//					if(bean.data.RSPCOD.equals("000000")){
//						T.showShort(getApplicationContext(), bean.data.RSPMSG);

//					}else{
//						T.showShort(getApplicationContext(), bean.data.RSPMSG);
//					}
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PAY_REQ){
            finish();
        }
    }

    public void loadPay1() {
        // TODO 2017/07/2 注释掉，不需要选择银行
//		if(TextUtils.isEmpty(banknum)){
//			T.showShort(getApplicationContext(), "请选择银行");
//			return;
//		}
        if (TextUtils.isEmpty(balance.getText())) {
            T.showShort(getApplicationContext(), "请填写金额");
            return;
        }


        try {
            double bala = Double.parseDouble(balance.getText().toString());
            if (bala <= 0) {
                T.showShort(getApplicationContext(), "金额不能为0");
                return;
            } else if (bala < 0.1) {
                T.showShort(getApplicationContext(), "金额不能为小于0.1");
                return;
            }
        } catch (Exception e) {
            // TODO: handle exception
            T.showShort(getApplicationContext(), "金额有误");
            return;
        }


//        String url = Api.HOST + "oneCity/jdPay/qrCodeUnifiedPay?" + "userId=" + UserInfoManager.getUserInfo(this).id +
//                "&mchId=" + mchIds + "&cardid=" + banknum + "&truename=" + user_names + "&cre_id=" +
//                cre_ids + "&money=" + String.valueOf((int) (Double.parseDouble(balance.getText().toString()) * 100));
//        String url = Api.HOST + "oneCity/tongLianPay/pay?" + "userId=" + UserInfoManager.getUserInfo(this).id +
//                "&tranAmt=" + String.valueOf((int) (Double.parseDouble(balance.getText().toString()))) +
//                "&mchId=" + "100510019484";
                String url = Api.HOST + "oneCity/tongLianPay/pay?" + "userId=" + UserInfoManager.getUserInfo(this).id +
                "&tranAmt=" + String.valueOf((int) (Double.parseDouble(balance.getText().toString()))) +
                "&mchId=" + mchIds;
        Intent about = new Intent(PaymentnewActivity.this, WebLoadActivity.class);
        about.putExtra("title", "充值");
        about.putExtra("url", url);
        startActivityForResult(about,PAY_REQ);
//		Log.e("", ""+url);
//		finish();
    }

    public void loadPay() {


        if (TextUtils.isEmpty(banknum)) {
            T.showShort(getApplicationContext(), "请选择银行");
            return;
        }
        if (TextUtils.isEmpty(balance.getText())) {
            T.showShort(getApplicationContext(), "请填写金额");
            return;
        }

        ProgressDialogUtil.showProgressDlg(this, "请稍候");
        FenfuPayRequset req = new FenfuPayRequset();
//		EncryptionUtil.getEncryptionstring(req.user_id);
//		req.isEncrypt = "1";
//		req.info = EncryptionUtil.getEncryptionstring(req.user_id);
        req.mchId = mchIds;
        req.userId = UserInfoManager.getUserInfo(this).id + "";
        req.totalPrice = String.valueOf((int) (Double.parseDouble(balance.getText().toString()) * 100));
        req.bankAccount = banknum;
        Log.e("", " - - - - = = = =  " + req.toString());
        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(req.toJson()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        new HttpUtils().send(HttpMethod.POST, Api.FENFUPAYGETPARAM, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                ProgressDialogUtil.dismissProgressDlg();
                T.showNetworkError(PaymentnewActivity.this);
            }

            @Override
            public void onSuccess(ResponseInfo<String> resp) {
                ProgressDialogUtil.dismissProgressDlg();
                Log.e("", "" + resp.result);
                GetShopPayInfoResponse bean = new Gson().fromJson(resp.result, GetShopPayInfoResponse.class);
                if (Api.SUCCEED == bean.result_code) {
                    if (bean.data.RSPCOD.equals("000000")) {
//						T.showShort(getApplicationContext(), bean.data.RSPMSG);

                        Intent about = new Intent(PaymentnewActivity.this, WebLoadActivity.class);
                        about.putExtra("title", "充值");
                        about.putExtra("url", bean.data.returnUrl);
                        startActivity(about);
                    } else
                        T.showShort(getApplicationContext(), bean.data.RSPMSG);
                }
            }
        });
    }


    private void showdilog() {
        // TODO Auto-generated method stub
        doubleWarnDialogs = new EidtDialog(PaymentnewActivity.this, R.style.CustomDialog, "请输入预留手机验证码", "", "", "确定", "", new OnEditDialogChlicListener() {

            @Override
            public void onClick(View v, String a) {
                // TODO Auto-generated method stub
                switch (v.getId()) {
                    case R.id.llWalletRecharge:


                        if (a.length() <= 0) {
                            Toast.makeText(getApplicationContext(), "请输入短信验证码",
                                    Toast.LENGTH_SHORT).show();
//						ToastCustom.showMessage(getApplicationContext(), "请输入正确划拨数量");
                        } else {
                            onfirm(a);
                            doubleWarnDialogs.dismiss();
                        }
                        break;
                    case R.id.btn_right:

                        doubleWarnDialogs.dismiss();
                        InputMethodManager m = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                        break;
                    case R.id.btnGetVCode:
                        setvcode();
                        break;
                    default:
                        break;
                }
            }
        }, 0);
        doubleWarnDialogs.setCancelable(false);
        doubleWarnDialogs.setCanceledOnTouchOutside(false);
        doubleWarnDialogs.show();
    }

    private void updateView(GetShopPayInfoBean data) {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(data.shopName)) {
            tv_name.setText(data.shopName);
            shopnames = data.shopName;
        }

        if (!TextUtils.isEmpty(data.mchId)) {
            mchIds = data.mchId;
        }
        if (!TextUtils.isEmpty(data.discountType)) {
            if (data.discountType.equals("1")) {
                tv_type.setText("25%激励模式");
                rb_normal.setStepSize((float) 1.25);
            } else if (data.discountType.equals("2")) {
                rb_normal.setStepSize((float) 2.50);
                tv_type.setText("50%激励模式");
            } else if (data.discountType.equals("3")) {
                rb_normal.setStepSize((float) 5.0);
                tv_type.setText("100%激励模式");
            }
        }
    }

    private void initview() {
        // TODO Auto-generated method stub
        initTopBar("收银台");
        getView(R.id.ll_card).setOnClickListener(this);
        getView(R.id.llWalletRecharge).setOnClickListener(this);
        cardimg = getView(R.id.cardimg);
        balance = getView(R.id.balance);
        tv_name = getView(R.id.tv_name);
        tv_type = getView(R.id.tv_type);
        cardname = getView(R.id.cardname);
        rb_normal = getView(R.id.rb_normal);
        pro = getView(R.id.pro);

        rb_normal.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//            	T.showLong(PaymentnewActivity.this, String.valueOf(rating));
            }
        });

//        if(!TextUtils.isEmpty(discountTypes)){
//        	if(discountTypes.equals("1")){
//    			tv_type.setText("25%激励模式");
//    			rb_normal.setRating((float)1.25);
//    		}else if(discountTypes.equals("2")){
//    			rb_normal.setRating((float)2.50);
//    			tv_type.setText("50%激励模式");
//    		}else if(discountTypes.equals("3")){
//    			rb_normal.setRating((float)5.0);
//    			tv_type.setText("100%激励模式");
//    		}
//        }
    }

    public void loadData() {

        MyBankCardBean req = new MyBankCardBean();
        req.user_id = UserInfoManager.getUserInfo(this).id + "";
        req.user_type = "1";
        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(req.toJson()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new HttpUtils().send(HttpMethod.POST, Api.FINDBINDCARDS, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
//				refreshscrollview.onRefreshComplete();
            }

            @Override
            public void onSuccess(ResponseInfo<String> resp) {
//				refreshscrollview.onRefreshComplete();
                Log.e("", "" + resp.result);
                BankCardListResponse bean = new Gson().fromJson(resp.result, BankCardListResponse.class);
                if (Api.SUCCEED == bean.result_code)
                    datas = bean.data;

                if (datas.size() > 0) {

                    if (!TextUtils.isEmpty(datas.get(0).bank_name)) {
                        bankname = datas.get(0).bank_name;
                    }
                    if (!TextUtils.isEmpty(datas.get(0).card_no)) {
                        banknum = datas.get(0).card_no;
                    }
                    if (!TextUtils.isEmpty(datas.get(0).person_no)) {
                        cre_ids = datas.get(0).person_no;
                    }
                    if (!TextUtils.isEmpty(datas.get(0).bank_mobile)) {
                        mobiles = datas.get(0).bank_mobile;
                    }

                    if (!TextUtils.isEmpty(datas.get(0).safe_code)) {
                        s_codes = datas.get(0).safe_code;
                    }
                    if (!TextUtils.isEmpty(datas.get(0).valid_year)) {
                        valid_years = datas.get(0).valid_year;
                    }
                    if (!TextUtils.isEmpty(datas.get(0).valid_month)) {
                        valid_months = datas.get(0).valid_month;
                    }
                    if (!TextUtils.isEmpty(datas.get(0).user_name)) {
                        user_names = datas.get(0).user_name;
                    }
//					bankname = datas.get(0).bank_name;
//					banknum = datas.get(0).card_no;
//					cre_ids  = datas.get(0).person_no;
//					mobiles = datas.get(0).bank_mobile;
//					s_codes = datas.get(0).safe_code;
//					valid_years  =   datas.get(0).valid_year;
//					valid_months = datas.get(0).valid_month;
////					sign_nos =  datas.get((int)arg3).sign_no;
                    if (banknum.length() > 4) {
                        cardname.setText(bankname + "尾号(" + banknum.substring(banknum.length() - 4) + ")");
                    } else {
                        cardname.setText(bankname);
                    }

//					int teg = 

                    for (int i = 0; i < datas.size(); i++) {
                        datas.get(i).bos = false;
                    }
                    datas.get(0).bos = true;
                    if (bankname != null) {
                        ViewUtil.setbank(cardimg, bankname);
                    }


//					cardname.setText("添加一张银行卡");
                } else {
                    cardname.setText("添加一张银行卡");
//					cardname.setText("卡名称卡号");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.ll_card:
                if (datas != null && datas.size() <= 0) {
                    Intent it = new Intent(PaymentnewActivity.this, AddBankCardActivity.class);
                    it.putExtra("tag", "1");
                    startActivity(it);
//				startActivity(new Intent(PaymentnewActivity.this,AddBankCardActivity.class));
//				showMultiChoiceDialog(view);
//				loadData();
                    return;
                } else {
                    showMultiChoiceDialog();
                }
                break;
            case R.id.llWalletRecharge:
                // TODO 2017/07/27 修改，不需要选择银行卡
//			if(TextUtils.isEmpty(banknum)&&datas!=null&&datas.size()>0){
//				showMultiChoiceDialog();
//			}
//						if(TextUtils.isEmpty(banknum)&&datas!=null&&datas.size()>0){
//				showMultiChoiceDialog();
//			}
                loadPay1();
//			bankCardQuickPay();
                break;
            default:
                break;
        }

    }

    public void showMultiChoiceDialog() {
        if (datas != null && datas.size() > 0) {
            multiChoiceDialogBuilder = new BankListChoiceDialog.Builder(PaymentnewActivity.this, "1");
            multiChoiceDialog = multiChoiceDialogBuilder.setTitle("请选择一张银行卡")
                    .setMultiChoiceItems(datas, new BaklistitemListener(), true)
                    .setPositiveButton("添加新卡支付", new PositiveClickListener()).setNegativeButton("取消", null).create();
            multiChoiceDialog.show();
        } else {
            loadData();
        }

    }

    class PositiveClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            multiChoiceDialog.dismiss();
            Intent it = new Intent(PaymentnewActivity.this, AddBankCardActivity.class);
            it.putExtra("tag", "1");
            startActivity(it);
//			startActivity(new Intent(PaymentnewActivity.this,AddBankCardActivity.class));
//			String s = "您选择了:";
//			List<MyBankCardBean> boos = multiChoiceDialogBuilder.getCheckedItems();
//			for (int i = 0; i < boos.size(); i++) {
//				if (boos.get(i).bos) {
//					alert(getApplication(), s);
//				} 
//			}
        }
    }

    class BaklistitemListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // TODO Auto-generated method stub

            if (datas.get((int) arg3).is_quickpay != null && datas.get((int) arg3).is_quickpay.equals("0")) {
                return;
            }
            multiChoiceDialog.dismiss();
            if (!TextUtils.isEmpty(datas.get((int) arg3).bank_name)) {
                bankname = datas.get((int) arg3).bank_name;
            }
            if (!TextUtils.isEmpty(datas.get((int) arg3).card_no)) {
                banknum = datas.get((int) arg3).card_no;
            }
            if (!TextUtils.isEmpty(datas.get((int) arg3).person_no)) {
                cre_ids = datas.get((int) arg3).person_no;
            }
            if (!TextUtils.isEmpty(datas.get((int) arg3).bank_mobile)) {
                mobiles = datas.get((int) arg3).bank_mobile;
            }

            if (!TextUtils.isEmpty(datas.get((int) arg3).safe_code)) {
                s_codes = datas.get((int) arg3).safe_code;
            }
            if (!TextUtils.isEmpty(datas.get((int) arg3).valid_year)) {
                valid_years = datas.get((int) arg3).valid_year;
            }
            if (!TextUtils.isEmpty(datas.get((int) arg3).valid_month)) {
                valid_months = datas.get((int) arg3).valid_month;
            }

            if (!TextUtils.isEmpty(datas.get((int) arg3).user_name)) {
                user_names = datas.get((int) arg3).user_name;
            }


//			bankname = datas.get((int)arg3).bank_name;
//			banknum = datas.get((int)arg3).card_no;
//			cre_ids  = datas.get((int)arg3).person_no;
//			mobiles = datas.get((int)arg3).bank_mobile;
//			s_codes = datas.get((int)arg3).safe_code;
//			valid_years  =   datas.get((int)arg3).valid_year;
//			valid_months = datas.get((int)arg3).valid_month;
//			sign_nos =  datas.get((int)arg3).sign_no;
            if (banknum.length() > 4) {
                cardname.setText(bankname + "尾号(" + banknum.substring(banknum.length() - 4) + ")");
            } else {
                cardname.setText(bankname);
            }


//			int teg = 

            for (int i = 0; i < datas.size(); i++) {
                datas.get(i).bos = false;
            }
            datas.get((int) arg3).bos = true;
            if (bankname != null) {
                ViewUtil.setbank(cardimg, bankname);
            }
        }
    }

    private void initDialog(String content, String left, String right) {
        // TODO Auto-generated method stub
        downloadDialog = new TwoButtonDialog(PaymentnewActivity.this, R.style.CustomDialog,
                "尊敬的会员", content, left, right, true, new OnMyDialogClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (v.getId()) {
                    case R.id.Button_OK:
                        downloadDialog.dismiss();
                        finish();
                        break;
                    case R.id.Button_cancel:
                        startActivity(new Intent(PaymentnewActivity.this, RealnameStepIDCardActivity.class));
                        downloadDialog.dismiss();
                    default:
                        break;
                }
            }
        });
        downloadDialog.show();
        downloadDialog.setCanceledOnTouchOutside(false);
        downloadDialog.setCancelable(false);
    }
}
