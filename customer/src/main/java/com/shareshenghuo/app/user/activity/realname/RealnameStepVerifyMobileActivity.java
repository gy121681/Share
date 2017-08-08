package com.shareshenghuo.app.user.activity.realname;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shareshenghuo.app.user.BaseTopActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.event.RealNameVerifyCompleteEvent;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.Constans;
import com.shareshenghuo.app.user.network.response.RealnameAuthenticationResponse;
import com.shareshenghuo.app.user.network.response.SetPaypwdResponse2;
import com.shareshenghuo.app.user.networkapi.Request;
import com.shareshenghuo.app.user.networkapi.CallbackObject;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.widget.CountDownButton;

import org.greenrobot.eventbus.EventBus;

/**
 * 实名认证，验证手机号界面
 * Created by Snow on 2017/7/19.
 */

public class RealnameStepVerifyMobileActivity extends BaseTopActivity {

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
        initTopBar("实名认证");
        initData();
        initView();
        setEvent();
    }

    private void initData(){
        mMobile = UserInfoManager.getUserInfo(this).mobile;
        mPayPassword = getIntent().getStringExtra(DATA_PAY_PASSWORD);
    }

    private void initView(){
        mBtnCountDown = getView(R.id.btn_count_down);
        mTvMobile = getView(R.id.tv_mobile);
        mTvMobile.setText(mMobile);
        mEdtVerifyCode = getView(R.id.edt_verify_code);
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
        ProgressDialogUtil.showProgressDlg(this, "提交中");
        Request.getServiceSetPayPassword(mPayPassword, UserInfoManager.getUserInfo(this).id + "",
                mBtnCountDown.getVCodeId(), getVerifyCode(), new CallbackObject<SetPaypwdResponse2>() {
                    @Override
                    public void onSuccess(SetPaypwdResponse2 data) {
                        if (TextUtils.equals(Constans.RSPCOD_SUCCESS, data.RSPCOD)) {
                            // 设置支付密码成功
                            reuqestAuthentication();
                            return;
                        }
                        ProgressDialogUtil.dismissProgressDlg();
                        toast(data.RSPMSG);
                    }

                    @Override
                    public void onFailure(String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        toast("设置失败，请重试");
                    }

                    @Override
                    public void onNetError(int code, String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        toast("网络异常");
                    }
                });
    }

    private void reuqestAuthentication(){
        Request.getServiceRealNameAuthentication(UserInfoManager.getUserInfo(this).id + "",
                new CallbackObject<RealnameAuthenticationResponse>() {
                    @Override
                    public void onFailure(String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        toast(msg);
                    }

                    @Override
                    public void onSuccess(RealnameAuthenticationResponse data) {
                        ProgressDialogUtil.dismissProgressDlg();
                        if (data.RSPCOD.equals("000000")) {
                            toast(data.RSPMSG);
                            startActivity(new Intent(RealnameStepVerifyMobileActivity.this, RealnameStepCompleteActivity.class));
                            EventBus.getDefault().post(new RealNameVerifyCompleteEvent());
                            finish();
                        } else {
                            toast(data.RSPMSG);
                        }
                    }

                    @Override
                    public void onNetError(int code, String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
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

