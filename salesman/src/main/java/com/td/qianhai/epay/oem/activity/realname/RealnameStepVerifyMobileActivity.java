package com.td.qianhai.epay.oem.activity.realname;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.share.app.entity.response.Constans;
import com.share.app.entity.response.Constans.*;
import com.share.app.entity.response.RealnameAuthenticationResponse;
import com.share.app.entity.response.SetPaypwdResponse2;
import com.share.app.event.RealNameVerifyCompleteEvent;
import com.share.app.event.SalesmanInfoRefreshEvent;
import com.share.app.network.CallbackObject;
import com.share.app.network.Request;
import com.share.app.utils.MD5Utils;
import com.td.qianhai.epay.oem.BaseActivity1;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.views.CountDownButton;

import org.greenrobot.eventbus.EventBus;

/**
 * 实名认证，验证手机号界面
 * Created by Snow on 2017/7/19.
 */

public class RealnameStepVerifyMobileActivity extends BaseActivity1 {

    public static final String DATA_PAY_PASSWORD = "payPassword";

    private CountDownButton mBtnCountDown;
    private TextView mTvMobile;
    private EditText mEdtVerifyCode;

    private String mMobile;
    private String mPayPassword;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_realname_step_verify_phone);
        initData();
        initView();
        setEvent();
    }

    private void initData(){
        mMobile = MyCacheUtil.getshared(this).getString(Login.MOBILE, "");
        mPayPassword = getIntent().getStringExtra(DATA_PAY_PASSWORD);
    }

    private void initView(){
        mBtnCountDown = getView(R.id.btn_count_down);
        mTvMobile = getView(R.id.tv_mobile);
        mTvMobile.setText(mMobile);
        mEdtVerifyCode = getView(R.id.edt_verify_code);
        initTitle();
    }


    private void initTitle(){
        TextView mTtile = (TextView) findViewById(R.id.tvTopTitle);
        mTtile.setText("实名认证");
        //下一步
        getView(R.id.llTopBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setEvent(){
        mBtnCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnCountDown.getVCode(mMobile, null);
            }
        });
        mBtnCountDown.performClick();
        //下一步
        getView(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(getVerifyCode())) {
                    toast("请输入短信验证码");
                    return;
                }
                setPayPassword();
            }
        });
    }

    /**
     * 设置支付密码
     */
    private void setPayPassword(){
        Request.getServiceSetPayPassword(mPayPassword, MyCacheUtil.getshared(this).getString(Constans.Login.USERID, ""),
                mBtnCountDown.getVCodeId(), getVerifyCode(), new CallbackObject<SetPaypwdResponse2>() {
                    @Override
                    public void onSuccess(SetPaypwdResponse2 data) {
                        if (TextUtils.equals(Common.C_RSPCOD_SUCCESS, data.RSPCOD)) {
                            // 设置支付密码成功
                            reuqestAuthentication();
                            return;
                        }
                        toast(data.RSPMSG);
                    }

                    @Override
                    public void onFailure(String msg) {
                        toast("设置失败，请重试");
                    }

                    @Override
                    public void onNetError(int code, String msg) {
                        toast("网络异常");
                    }
                });
    }

    private void reuqestAuthentication(){
        Request.getServiceRealNameAuthentication(MyCacheUtil.getshared(this).getString(Constans.Login.USERID, ""),
                new CallbackObject<RealnameAuthenticationResponse>() {
                    @Override
                    public void onFailure(String msg) {
                        toast(msg);
                    }

                    @Override
                    public void onSuccess(RealnameAuthenticationResponse data) {
                        if (data.RSPCOD.equals("000000")) {
                            toast(data.RSPMSG);
                            EventBus.getDefault().post(new RealNameVerifyCompleteEvent());
                            EventBus.getDefault().post(new SalesmanInfoRefreshEvent());
                            startActivity(new Intent(RealnameStepVerifyMobileActivity.this, RealnameStepCompleteActivity.class));
                            finish();
                        } else {
                            toast(data.RSPMSG);
                        }
                    }

                    @Override
                    public void onNetError(int code, String msg) {
                        toast("网络异常");
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBtnCountDown.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBtnCountDown.onStop();
    }

    private String getVerifyCode(){
        return mEdtVerifyCode.getText().toString().trim();
    }


}

