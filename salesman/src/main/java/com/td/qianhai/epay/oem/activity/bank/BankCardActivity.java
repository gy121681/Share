package com.td.qianhai.epay.oem.activity.bank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.share.app.entity.response.AutResponse;
import com.share.app.entity.response.Constans;
import com.share.app.entity.response.MyBankCardBean;
import com.share.app.event.SalesmanInfoRefreshEvent;
import com.share.app.network.Api;
import com.share.app.utils.BankLogoUtils;
import com.share.app.utils.ProgressDialogUtil;
import com.share.app.utils.StrUtils;
import com.share.app.utils.WaringDialogUtils;
import com.td.qianhai.epay.oem.BaseActivity1;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.RequestCardInfoChangeActivity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.views.dialog.TwoButtonDialog;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

import org.apache.http.entity.StringEntity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;

/**
 * 我的银行卡界面
 * Created by Snow on 2017/7/27.
 */

public class BankCardActivity extends BaseActivity1 {

    public static final String DATA_BANK_NAME = "bankName;";
    public static final String DATA_BANK_NO = "bankCard";

    private String mBankName;
    private String mBankCardNO;

    private TextView mTvBankName;
    private TextView mTvBankCardNO;
    private Button mBtnUnBind;
    private SwipeLayout mSwipeLayout;
    private ImageView mImgLogo;

    private TwoButtonDialog twoBtnDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card);
        EventBus.getDefault().register(this);
        handleIntent(getIntent());
        initData();
        initView();
        setEvent();
    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        mBankName = intent.getStringExtra(DATA_BANK_NAME);
        mBankCardNO = intent.getStringExtra(DATA_BANK_NO);
    }

    private void initData() {

    }

    private void initView() {
        TextView tvTitle = getView(R.id.tvTopTitle);
        tvTitle.setText("银行卡");
        mTvBankName = getView(R.id.tv_bank_name);
        mTvBankCardNO = getView(R.id.tv_bank_card_no);
        mTvBankName.setText(mBankName);
        mTvBankCardNO.setText(StrUtils.hideBankCarNO(mBankCardNO));
        mSwipeLayout = getView(R.id.swipe_LinearLayout1);
        mBtnUnBind = getView(R.id.btn_unbind);
        mImgLogo = getView(R.id.img_bank_logo);
        mImgLogo.setImageResource(BankLogoUtils.getLogo(mBankName));
    }

    private void setEvent() {
        getView(R.id.llTopBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /* 银行卡变更 */
        getView(R.id.btn_change_bank_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RequestCardInfoChangeActivity.class));
            }
        });
        /* 解绑银行卡 */
        getView(R.id.btn_unbind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String card = "";
                if (mBankCardNO != null && mBankCardNO.length() > 4) {
                    card = mBankCardNO.substring(mBankCardNO.length() - 4);
                }
                twoBtnDialog = new TwoButtonDialog(getActivity(), R.style.CustomDialog,
                        "", "确定解绑该卡尾号(" + card + ")?", "否", "是", new OnMyDialogClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        switch (v.getId()) {
                            case R.id.Button_OK:
                                twoBtnDialog.dismiss();
                                break;
                            case R.id.Button_cancel:
                                unBindBankCard();
                                twoBtnDialog.dismiss();
                            default:
                                break;
                        }
                    }
                });
                twoBtnDialog.show();
            }
        });
        /* 修改预留手机号 */
        getView(R.id.btn_update_bank_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BankMobileModifyActivity.class);
                intent.putExtra(BankMobileModifyActivity.DATA_BANK_CARD_NO, mBankCardNO);
                startActivity(intent);
            }
        });


    }

    private void unBindBankCard() {

        MyBankCardBean req = new MyBankCardBean();
        req.user_id = MyCacheUtil.getshared(this).getString(Constans.Login.USERID, "");
        req.user_type = "3";
        req.card_no = mBankCardNO;
        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = HttpUrls.URL + "oneCity/service/deleteBindCards";
        new HttpUtils().send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                ProgressDialogUtil.dismissProgressDlg();
                WaringDialogUtils.showWaringDialog(getActivity(), "网络异常", null);
            }

            @Override
            public void onSuccess(ResponseInfo<String> resp) {
                ProgressDialogUtil.dismissProgressDlg();
                AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
                if(Api.SUCCEED == bean.getResult_code()) {

                    if(bean.data!=null&&bean.data.RSPCOD.equals("000000")){
                        EventBus.getDefault().post(new SalesmanInfoRefreshEvent());
                        toast("解绑成功");
                        finish();
                    }else{
                        toast(bean.data.RSPMSG);
                    }
                } else {
                    toast(bean.getResult_desc());
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(SalesmanInfoRefreshEvent event) {
        finish();
    }
}
