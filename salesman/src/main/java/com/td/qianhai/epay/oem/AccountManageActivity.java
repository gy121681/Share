package com.td.qianhai.epay.oem;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.bumptech.glide.Glide;
import com.share.app.entity.response.Constans;
import com.share.app.entity.response.SalesmanInfoResponse;
import com.share.app.event.SalesmanInfoRefreshEvent;
import com.share.app.model.SalesmanInfoModel;
import com.share.app.network.CallbackObject;
import com.share.app.network.Request;
import com.share.app.utils.ProgressDialogUtil;
import com.share.app.utils.UrlUtils;
import com.share.app.utils.WaringDialogUtils;
import com.td.qianhai.epay.oem.activity.bank.BankCardActivity;
import com.td.qianhai.epay.oem.activity.bank.BankCardAddActivity;
import com.td.qianhai.epay.oem.activity.realname.RealnameStepIDCardActivity;
import com.td.qianhai.epay.oem.activity.realname.RealnameStepSetpwdActivity;
import com.td.qianhai.epay.oem.activity.realname.RealnameStepVerifyMobileActivity;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.unlock.SetUnlockPasswordActivity;
import com.td.qianhai.epay.oem.unlock.StringUtil;
import com.td.qianhai.epay.oem.views.CircleImageView;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.TwoButtonDialogStyle2;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.epay.utils.DateUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import static com.share.app.utils.StrUtils.hideBankCarNO;
import static com.share.app.utils.StrUtils.hideIdCard;
import static com.share.app.utils.StrUtils.hideMobile;
import static com.share.app.utils.StrUtils.hideName;

/**
 * 帐号管理界面
 *
 * @author liangge
 */
public class AccountManageActivity extends BaseActivity implements
        OnClickListener {
    /**
     * Activity
     */
    public static Activity activity;
    /**
     * 商户id、用户状态
     */
    private String attStr, tagsts, usermobile;// custId,
    /**
     * 开户银行信息修改、手机号码变更申请、商户信息修改、银行卡信息变更申请、实名认证
     */
    private RelativeLayout updateBankInfo, updateDealerData,
            requestBankInfoChange, realNameAuthentication, btn_touch_password, btn_touch_password_set,
            btn_uppay_password, btn_reg_pay_password, btn_touch_bankphone, btn_unbindkard;
    private TextView tv_content;
    private TextView p_out;
    private OneButtonDialogWarn warnDialog;
    private Editor editor;
    private CheckBox checs;

    private CircleImageView mImgPhoto;
    private TextView mTvRealName;
    private TextView mTvMobile;
    private TextView mTvIdCard;
    private TextView mTvAuthenticationStatus;
    private TextView mTvAgentArea;
    private TextView mTvBankCardNO;

    private SalesmanInfoResponse mSalesmanInfoResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.menu_account_manage_new);
        AppContext.getInstance().addActivity(this);
        //获取业务员信息
        querySalesmanInfo();
        mImgPhoto = getView(R.id.img_head_photo);
        mTvRealName = getView(R.id.tv_real_name);
        mTvMobile = getView(R.id.tv_mobile);
        mTvIdCard = getView(R.id.tv_idcard_number);
        mTvAuthenticationStatus = getView(R.id.tv_authentication_status);
        mTvAgentArea = getView(R.id.tv_agent_area);
        mTvBankCardNO = getView(R.id.tv_bank_card_no);

        activity = this;
        // custId = ((AppContext) this.getApplication()).getCustId();
        editor = MyCacheUtil.setshared(this);
        usermobile = MyCacheUtil.getshared(this).getString("usermobile", "");
        attStr = MyCacheUtil.getshared(this).getString("MERSTS", "");
        tagsts = MyCacheUtil.getshared(this).getString("STS", "");
        findViewById(R.id.btn_menu_account_manage_query_info)
                .setOnClickListener(this);
        p_out = (TextView) findViewById(R.id.p_out);
        p_out.setOnClickListener(this);
        checs = (CheckBox) findViewById(R.id.checs);
        btn_touch_password = (RelativeLayout) findViewById(R.id.btn_touch_password);
        btn_touch_password.setOnClickListener(this);
        btn_touch_password_set = (RelativeLayout) findViewById(R.id.btn_touch_password_set);
        btn_touch_password_set.setOnClickListener(this);
        updateBankInfo = (RelativeLayout) findViewById(R.id.btn_menu_account_manage_bank_info_update);
        updateBankInfo.setOnClickListener(this);
        updateDealerData = (RelativeLayout) findViewById(R.id.btn_menu_account_manage_update_dealer_data);
        updateDealerData.setOnClickListener(this);
        requestBankInfoChange = (RelativeLayout) findViewById(R.id.btn_menu_bank_info_update_request);
        requestBankInfoChange.setOnClickListener(this);
        realNameAuthentication = (RelativeLayout) findViewById(R.id.btn_menu_account_manage_real_name_authentication);
        realNameAuthentication.setOnClickListener(this);
        btn_uppay_password = (RelativeLayout) findViewById(R.id.btn_uppay_password);
        btn_uppay_password.setOnClickListener(this);
        btn_reg_pay_password = (RelativeLayout) findViewById(R.id.btn_reguppay_password);
        btn_reg_pay_password.setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title_contre)).setText("账户管理");
        btn_touch_bankphone = (RelativeLayout) findViewById(R.id.btn_touch_bankphone);
        btn_touch_bankphone.setOnClickListener(this);
        btn_unbindkard = (RelativeLayout) findViewById(R.id.btn_unbindkard);
        btn_unbindkard.setOnClickListener(this);

        if (MyCacheUtil.getshared(this).getString("isgesture", "").equals("1")) {
            checs.setChecked(true);
        } else {
            checs.setChecked(false);
        }
        checs.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean ischeck) {
                // TODO Auto-generated method stub
                if (ischeck) {
                    editor.putString("isgesture", "1");
                    editor.commit();

                } else {
                    editor.putString("isgesture", "0");
                    editor.commit();
                }
            }
        });
        /** 变更手机号 */
        getView(R.id.layout_change_mobile).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                applyToChangeMobile();
            }
        });

//		if(((AppContext)getApplication()).getTxnsts().equals("0")){

//			new Handler().postDelayed(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					Intent it = new Intent(AccountManageActivity.this,IntroductionActivity.class);
//					it.putExtra("title", "温馨提示");
//					it.putExtra("description", "您的账户暂未进行实名认证,为了您的账户安全和对资金的管理或操作,请在账户管理中进行资料补全.");
//					startActivity(it);
//				}
//			}, 1000);

//		}
        if (!StringUtil.isEmpty(usermobile)) {
            btn_touch_password.setVisibility(View.VISIBLE);
            btn_touch_password_set.setVisibility(View.VISIBLE);

        } else {
            btn_touch_password.setVisibility(View.GONE);
            btn_touch_password_set.setVisibility(View.GONE);
        }
        TextView tvBack = (TextView) findViewById(R.id.bt_title_left);
        tvBack.setText("返回");
        tvBack.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        findViewById(R.id.btn_menu_account_manage_update_login_password)
                .setOnClickListener(this);
        // 根据登录成功后的认证状态来显示和隐藏功能按钮
        if (attStr != null) {
            if (attStr.equals("0")) {
                updateBankInfo.setVisibility(View.GONE);
//                requestBankInfoChange.setVisibility(View.VISIBLE);
//                updateDealerData.setVisibility(View.VISIBLE);
                realNameAuthentication.setVisibility(View.GONE);
            } else {
                if (MyCacheUtil.getshared(AccountManageActivity.this).getString("Txnsts", "").equals("1")) {
                    realNameAuthentication.setVisibility(View.GONE);
                }
            }
        }

        /**
         * 实名认证
         */
        getView(R.id.layout_authentication).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSalesmanInfoResponse == null) {
                    return;
                }
                if (TextUtils.equals(Constans.AuthenticationStatus.STATUS_UNAUTHENTICATION, mSalesmanInfoResponse.getCertification_status())
                        || TextUtils.equals(Constans.AuthenticationStatus.STATUS_FAILED, mSalesmanInfoResponse.getCertification_status())){
                    //未实名认证，开始实名认证
                    if (TextUtils.equals(Constans.AuthenticationStep.STEP_UNSTART, mSalesmanInfoResponse.getCertification_step())) {
                        startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                        finish();
                    } else if (TextUtils.equals(Constans.AuthenticationStep.STEP_AUTHENTICATIONED, mSalesmanInfoResponse.getCertification_step())) {
                        startActivity(new Intent(getActivity(), RealnameStepSetpwdActivity.class));
                        finish();
                    } else if (TextUtils.equals(Constans.AuthenticationStep.STEP_SET_PAY_PASSWORD, mSalesmanInfoResponse.getCertification_step())) {
                        Intent intent = new Intent(getActivity(), RealnameStepVerifyMobileActivity.class);
                        intent.putExtra(RealnameStepVerifyMobileActivity.DATA_PAY_PASSWORD, mSalesmanInfoResponse.getPay_password());
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        /**
         * 我的银行卡
         */
        getView(R.id.layout_bank_card).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: 2017/7/27 测试银行卡，暂时屏蔽代码
                if (mSalesmanInfoResponse == null) {
                    return;
                }

//                startActivity(new Intent(getActivity(), RequestCardInfoChangeActivity.class));

                if (!TextUtils.equals(Constans.AuthenticationStatus.STATUS_SUCCESS, mSalesmanInfoResponse.getCertification_status())) {
                    Toast.makeText(getApplicationContext(), "用户权限！请补全资料审核通过后重试",
                            Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mSalesmanInfoResponse.getBank_no())) {
                    //没有银行卡——添加银行卡
                    startActivity(new Intent(getActivity(), BankCardAddActivity.class));
                    finish();
                }else {
                    Intent intent = new Intent(getActivity(), BankCardActivity.class);
                    intent.putExtra(BankCardActivity.DATA_BANK_NAME, mSalesmanInfoResponse.getBank_name());
                    intent.putExtra(BankCardActivity.DATA_BANK_NO, mSalesmanInfoResponse.getBank_no());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (DateUtil.isFastDoubleClick()) {
            return;
        } else {
            final Intent intent = new Intent();
            switch (v.getId()) {
                // 商户信息查询
                case R.id.btn_menu_account_manage_query_info:
                    intent.setClass(this, BussinessInfoActivity.class);
                    startActivity(intent);
                    break;
                // 开户信息修改
                case R.id.btn_menu_account_manage_bank_info_update:
                    if (!attStr.equals("0")) {
                        Toast.makeText(getApplicationContext(), "用户权限！请补全资料审核通过后重试",
                                Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(activity, "用户权限！请补全资料审核通过后重试");
                    }
                    break;
                // 实名认证
                case R.id.btn_menu_account_manage_real_name_authentication:
                    if (attStr.equals("0")) {
                        ToastCustom.showMessage(AccountManageActivity.this,
                                "实名认证已通过", Toast.LENGTH_SHORT);

                    } else if (tagsts.equals("1")) {
                        ToastCustom.showMessage(AccountManageActivity.this,
                                "资料正在审核中，请勿重复操作", Toast.LENGTH_SHORT);
                    } else {
//					if(((AppContext)getApplication()).getSts().equals("1")){
//						ToastCustom.showMessage(AccountManageActivity.this,
//								"用户信息正在审核中", Toast.LENGTH_SHORT);
//					}else{
                        intent.setClass(this, AuthenticationActivity.class);
                        startActivity(intent);
//					}

                    }
                    break;
                // 修改登录密码
                case R.id.btn_menu_account_manage_update_login_password:
                    if (attStr.equals("0")) {
                        intent.setClass(this, RevisePasswordActivity.class);
                        startActivity(intent);
                    } else {
//					ToastCustom.showMessage(activity, "用户权限！请补全资料审核通过后重试");
                        Toast.makeText(getApplicationContext(), "用户权限！请补全资料审核通过后重试",
                                Toast.LENGTH_SHORT).show();
                    }

                    break;
                // 手机号码更改申请
                case R.id.btn_menu_account_manage_update_dealer_data:
                    applyToChangeMobile();

                    break;
                // 银行卡信息变更申请
                case R.id.btn_menu_bank_info_update_request:
                    if (tagsts.equals("4")) {
                        Toast.makeText(getApplicationContext(), "银行卡和手机号不可同时修改，待审核通过重试",
                                Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(AccountManageActivity.this, "银行卡和手机号不可同时修改，待审核通过重试");
                        return;
                    }

                    doubleWarnDialog = new TwoButtonDialogStyle2(AccountManageActivity.this,
                            R.style.CustomDialog, "提示", new SpannableString("银行卡信息变更申请需要审核,未审核前不允许使用提现功能，请知悉"), "确定", "取消",
                            new OnMyDialogClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    switch (v.getId()) {
                                        case R.id.btn_left:
                                            doubleWarnDialog.dismiss();
                                            break;
                                        case R.id.btn_right:
                                            intent.setClass(AccountManageActivity.this, RequestCardInfoChangeActivity.class);
                                            startActivity(intent);
                                            finish();
                                            doubleWarnDialog.dismiss();

                                            break;
                                        default:
                                            break;
                                    }
                                }
                            });
                    doubleWarnDialog.setCancelable(true);
                    doubleWarnDialog.show();

                    break;
                case R.id.btn_touch_password:

                    if (attStr.equals("0")) {
                        intent.setClass(this, SetUnlockPasswordActivity.class);
                        intent.putExtra("refresh", "refresh");
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "未设手势密码",
                                Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(activity, "未设手势密码");
                    }

                    break;
                case R.id.p_out:

                    SpannableString msp = new SpannableString("您确定要退出当前账户?");
                    showDoubleWarnDialog(msp);
                    break;
                case R.id.btn_uppay_password:
                    if (attStr.equals("0")) {
                        intent.setClass(AccountManageActivity.this,
                                UpdatePayPassActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "用户权限！请补全资料审核通过后重试",
                                Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(activity, "用户权限！请补全资料审核通过后重试");
                    }

                    break;
                case R.id.btn_reguppay_password:

                    if (attStr.equals("0")) {
                        intent.setClass(AccountManageActivity.this,
                                RegetPayPwActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "用户权限！请补全资料审核通过后重试",
                                Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(activity, "用户权限！请补全资料审核通过后重试");
                    }
                    break;
                case R.id.btn_touch_bankphone:
                    intent.setClass(AccountManageActivity.this,
                            PayMainActivity.class);
                    intent.putExtra("tag", "0");
                    startActivity(intent);
                    break;
                case R.id.btn_unbindkard:
                    intent.setClass(AccountManageActivity.this,
                            PayMainActivity.class);
                    intent.putExtra("tag", "1");
                    startActivity(intent);
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 申请变更手机号
     */
    private void applyToChangeMobile() {
        if (tagsts.equals("3")) {
            Toast.makeText(getApplicationContext(), "银行卡和手机号不可同时修改，待审核通过重试",
                    Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(AccountManageActivity.this, "银行卡和手机号不可同时修改，待审核通过重试");
            return;
        }
        if (mSalesmanInfoResponse == null) {
            return;
        }
        doubleWarnDialog = new TwoButtonDialogStyle2(AccountManageActivity.this,
                R.style.CustomDialog, "提示", new SpannableString("手机号码变更申请需要审核,未审核前不可进行出款操作，请知悉"), "确定", "取消",
                new OnMyDialogClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        switch (v.getId()) {
                            case R.id.btn_left:
                                doubleWarnDialog.dismiss();

                                break;
                            case R.id.btn_right:
                                Intent intent = new Intent();
                                intent.setClass(AccountManageActivity.this, RequestPhonenumberChengesActivity.class);
                                intent.putExtra(RequestPhonenumberChengesActivity.DATA_MOBILE, mSalesmanInfoResponse.getMobile());
                                startActivity(intent);
                                finish();
                                doubleWarnDialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
        doubleWarnDialog.setCancelable(true);
        doubleWarnDialog.show();
    }


    /**
     * 显示退出登录提示对话框
     *
     * @param msg
     */
    protected void showDoubleWarnDialog(SpannableString msg) {
        doubleWarnDialog = new TwoButtonDialogStyle2(this,
                R.style.CustomDialog, "提示", msg, "退出帐号", "退出应用",
                new OnMyDialogClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        doubleWarnOnClick(v);
                    }
                });
        doubleWarnDialog.setCancelable(true);
        doubleWarnDialog.setCanceledOnTouchOutside(true);
        doubleWarnDialog.show();
    }

    @Override
    protected void doubleWarnOnClick(View v) {

        switch (v.getId()) {
            case R.id.btn_left:
                doubleWarnDialog.dismiss();
                AppContext.getInstance().setCustId(null);
                AppContext.getInstance().setPsamId(null);
                AppContext.getInstance().setMacKey(null);
                AppContext.getInstance().setPinKey(null);
                AppContext.getInstance().setMerSts(null);
                AppContext.getInstance().setMobile(null);
                AppContext.getInstance().setEncryPtkey(null);
                AppContext.getInstance().setStatus(null);
                AppContext.getInstance().setCustPass(null);
                AppContext.getInstance().setVersionSerial(null);
                AppContext.getInstance().setStateaudit(null);
                editor.putString("MERSTS", "");
                editor.putString("AGENTID", "");
                editor.putString("PERSONPIC", "");
                editor.commit();
                AppContext.getInstance().exit();
                break;
            case R.id.btn_right:
                AppContext.getInstance().setCustId(null);
                AppContext.getInstance().setPsamId(null);
                AppContext.getInstance().setMacKey(null);
                AppContext.getInstance().setPinKey(null);
                AppContext.getInstance().setMerSts(null);
                AppContext.getInstance().setMobile(null);
                AppContext.getInstance().setUsername(null);
                AppContext.getInstance().setEncryPtkey(null);
                AppContext.getInstance().setStatus(null);
                AppContext.getInstance().setCustPass(null);
                AppContext.getInstance().setVersionSerial(null);
                AppContext.getInstance().setStateaudit(null);
                editor.putString("MERSTS", "");
                editor.putString("AGENTID", "");
                editor.putString("PERSONPIC", "");
                editor.putString("isopem", "-1");
                editor.commit();
                doubleWarnDialog.dismiss();
                AppContext.getInstance().exit();

                Intent it = new Intent(this, UserActivity.class);
                startActivity(it);
                break;
            default:
                break;
        }
    }

    private void querySalesmanInfo() {
        ProgressDialogUtil.showProgressDlg(this, "数据加载中...");
        String mobile = MyCacheUtil.getshared(this).getString(Constans.Login.MOBILE, "");
        Request.getSalesmanInfo(mobile, new CallbackObject<SalesmanInfoResponse>() {
            @Override
            public void onFailure(String msg) {
                ProgressDialogUtil.dismissProgressDlg();
                WaringDialogUtils.showWaringDialog(getActivity(), msg, null);
            }

            @Override
            public void onSuccess(SalesmanInfoResponse data) {
                ProgressDialogUtil.dismissProgressDlg();
                showSalesmanInfo(data);
            }

            @Override
            public void onNetError(int code, String msg) {
                ProgressDialogUtil.dismissProgressDlg();
                WaringDialogUtils.showWaringDialog(getActivity(), "网络异常", null);
            }
        });
    }

    /**
     * 显示业务员信息
     *
     * @param data
     */
    private void showSalesmanInfo(SalesmanInfoResponse data) {
        mSalesmanInfoResponse = data;
        SalesmanInfoModel.getInstance().setSalesmanInfo(data);

        if (data == null) {
            return;
        }
        Editor editor = MyCacheUtil.setshared(this);
        editor.putString(Constans.Login.REALNAME, mSalesmanInfoResponse.getReal_name());
        editor.putString(Constans.Login.PERSONNO, mSalesmanInfoResponse.getPerson_no());
        editor.commit();

        Glide.with(getActivity()).load(UrlUtils.getUrl(data.getPhoto())).error(R.drawable.share_s_public_head_small_big).into(mImgPhoto);
        mTvRealName.setText(hideName(data.getReal_name()));
        mTvMobile.setText(hideMobile(data.getMobile()));
        mTvIdCard.setText(hideIdCard(data.getPerson_no()));
        mTvAuthenticationStatus.setText(getAuthenticationStatusDesc(data.getCertification_status()));
        mTvAgentArea.setText(getAgentAreaDesc(data.getAgent_area_list()));//显示代理区域
        mTvBankCardNO.setText(hideBankCarNO(data.getBank_no()));
    }

    /**
     * 获取实名认证状态描述
     *
     * @param status
     * @return
     */
    private String getAuthenticationStatusDesc(String status) {
        if (TextUtils.equals(Constans.AuthenticationStatus.STATUS_SUCCESS, status)) {
            //认证通过
            return "已认证";
        } else if (TextUtils.equals(Constans.AuthenticationStatus.STATUS_FAILED, status)) {
            mTvAuthenticationStatus.setText("未通过");
            return "未通过";
        } else if (TextUtils.equals(Constans.AuthenticationStatus.STATUS_UNAUTHENTICATION, status)) {
            mTvAuthenticationStatus.setText("未认证");
            return "未认证";
        }
        return "";
    }

    /**
     * 获取代理区域描述
     * @param areas
     * @return
     */
    private String getAgentAreaDesc(List<String> areas){
        String resutl = "";
        if (areas != null && !areas.isEmpty()) {
            StringBuilder sBuilder = new StringBuilder();
            for (String area : areas) {
                sBuilder.append(area);
            }
            resutl = sBuilder.toString();
        }
        return resutl;
    }

    @Subscribe
    public void onEvent(SalesmanInfoRefreshEvent event) {
        querySalesmanInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
