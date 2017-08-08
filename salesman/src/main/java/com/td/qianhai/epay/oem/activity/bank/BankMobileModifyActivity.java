package com.td.qianhai.epay.oem.activity.bank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.share.app.network.CallbackString;
import com.share.app.network.Request;
import com.share.app.utils.ProgressDialogUtil;
import com.share.app.utils.WaringDialogUtils;
import com.td.qianhai.epay.oem.BaseActivity;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.utils.Checkingroutine;

/**
 * Created by Snow on 2017/7/28.
 */

public class BankMobileModifyActivity extends BaseActivity {

    public static final String DATA_BANK_CARD_NO = "bankCardNO";

    private String mBankCardNO = "";

    private TextView mTvBankCard;
    private EditText mEdtMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bank_mobile);
        handIntent(getIntent());
        initData();
        initView();
        setEvent();
    }

    private void handIntent(Intent intent){
        mBankCardNO = intent.getStringExtra(DATA_BANK_CARD_NO);
    }

    private void initData(){

    }

    private void initView(){
        mTvBankCard = getView(R.id.tv_bankcard);
        mEdtMobile = getView(R.id.edt_mobile);
        mTvBankCard.setText(mBankCardNO);
        TextView title = getView(R.id.tv_title_contre);
        title.setText("预留手机号变更");
        getView(R.id.bt_title_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setEvent(){
        getView(R.id.btn_edit_bankmobile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = getMobile().trim();
                if (!isMobileNO(mobile)) {
                    toast("手机号码格式有误");
                    return;
                }
                if (mobile == null || (mobile != null && mobile.equals(""))) {
                    toast("请输入手机号码");
                    return;
                }
                if (!Checkingroutine.isNumeric(mobile)) {
                    toast("手机号码只能输入数字");
                    return;
                }
                if (mobile.length() != 11) {
                    toast("手机号码必须为11位数字");
                    return;
                }
                changeBankMobile(mBankCardNO, mobile);
            }
        });
    }

    private String getMobile(){
        return mEdtMobile.getText().toString();
    }

    private void changeBankMobile(String bankNo, String mobile){
        ProgressDialogUtil.showProgressDlg(getActivity(), "");
        Request.getUsercenterUserChangeBankMobile(bankNo, mobile,
                new CallbackString() {
                    @Override
                    public void onFailure(String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        WaringDialogUtils.showWaringDialog(getActivity(), msg, null);
                    }

                    @Override
                    public void onSuccess(String data) {
                        ProgressDialogUtil.dismissProgressDlg();
                        toast("操作成功");
                        finish();
                    }

                    @Override
                    public void onNetError(int code, String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        WaringDialogUtils.showWaringDialog(getActivity(), "网络异常", null);
                    }
                });
    }

}
