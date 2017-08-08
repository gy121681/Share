package com.td.qianhai.epay.oem.activity.realname;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.share.app.event.RealNameVerifyCompleteEvent;
import com.share.app.utils.PwdUtils;
import com.td.qianhai.epay.oem.BaseActivity1;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.views.PasswordEditBox;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 实名认证，设置交易密码界面
 * Created by Snow on 2017/7/19.
 */

public class RealnameStepSetpwdActivity extends BaseActivity1 {

    private PasswordEditBox mPasswordEditBox;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_realname_step_setpwd);
        EventBus.getDefault().register(this);
        initData();
        initView();
        setEvent();
    }

    private void initData(){

    }

    private void initView(){
        mPasswordEditBox = getView(R.id.edt_pay_password);
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
        mPasswordEditBox.requestFocus();
        getView(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payPwd = getPayPwd();
                if (TextUtils.isEmpty(payPwd)) {
                    toast("请输入交易密码");
                    mPasswordEditBox.setText("");
                    return;
                }
                if (payPwd.length() != 6) {
                    toast("交易密码输入有误");
                    mPasswordEditBox.setText("");
                    return;
                }
                Intent intent = new Intent(RealnameStepSetpwdActivity.this, RealnameStepVerifyMobileActivity.class);
                intent.putExtra(RealnameStepVerifyMobileActivity.DATA_PAY_PASSWORD, PwdUtils.getEncripyPwd(payPwd, 3));
                startActivity(intent);
            }
        });
        mPasswordEditBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !TextUtils.isEmpty(getPayPwd())) {
                    mPasswordEditBox.setText("");
                }
            }
        });
    }

    private void setViewVisiable(View view, boolean visiable) {
        if (view == null) {
            return;
        }
        view.setVisibility(visiable ? View.VISIBLE : View.GONE);
    }

    private String getPayPwd(){
        return mPasswordEditBox.getText().toString().trim();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(RealNameVerifyCompleteEvent event) {
        finish();
    }
}

