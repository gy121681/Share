package com.td.qianhai.epay.oem;

import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.share.app.entity.response.Constans;
import com.share.app.network.CallbackString;
import com.share.app.network.Request;
import com.share.app.utils.ProgressDialogUtil;
import com.share.app.utils.WaringDialogUtils;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.CountDownButton;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.utils.Checkingroutine;
import com.td.qianhai.epay.utils.Common;
import com.td.qianhai.epay.utils.DateUtil;

/**
 * 手机号码变更申请
 *
 * @author liangge
 */
public class RequestPhonenumberChengesActivity extends BaseActivity {

    public static final String DATA_MOBILE = "mobile";

    /**
     * 新手机号码、再次输入新手机号码、验证码
     */
    private EditText newPhonenumber, againNewPhonenumber, alterVerifCode;
    /**
     * 提交申请、验证码获取
     */
    private Button rpSubmit;
    private CountDownButton btVerif;
    /**
     * 商户ID、终端号
     */
    private String custId, psamId, mrecnum;
    /**
     * 旧手机号码
     */
    private TextView oldPhonenumber, tv_title;
    /**
     * 旧手机号码、手机号码、再次输入的手机号码、验证码
     */
    private String oldPhone, mobile, mobileAgain, verifNo, codlnum;
    /**
     * 返回
     */
    private TextView bt_Back;

    private String mMobile;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phonenumber_chenge);
        editor = MyCacheUtil.setshared(this);
        AppContext.getInstance().addActivity(this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 10:
                    // 获取验证码失败
                    ToastCustom.showMessage(getApplicationContext(), "获取验证码失败",
                            Toast.LENGTH_SHORT);
                    btVerif.setEnabled(true);
                    break;
                case 11:
                    HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
                    ToastCustom.showMessage(getApplicationContext(),
                            map.get("RSPMSG").toString(), Toast.LENGTH_SHORT);

                    if (map.get("RSPCOD").toString().equals("09999")) {
                        btVerif.setEnabled(true);

                    } else if (map.get("RSPCOD").toString().equals("000000")) {
                        Common.timing(btVerif);
                    } else {
                        btVerif.setEnabled(true);
                    }
                    break;
            }
        }

        ;
    };

    private void initView() {
        custId = MyCacheUtil.getshared(this).getString("Mobile", "");
        codlnum = MyCacheUtil.getshared(this).getString("Mobile", "");
        mrecnum = MyCacheUtil.getshared(this).getString("MercNum", "");
        mMobile = getIntent().getStringExtra(DATA_MOBILE);
//		custId = ((AppContext) this.getApplication()).getCustId();
        psamId = ((AppContext) getApplication()).getPsamId();
//		mrecnum = ((AppContext) getApplication()).getMercNum();
//		codlnum = ((AppContext) getApplication()).getMobile();
        newPhonenumber = (EditText) findViewById(R.id.tv_new_phonenumber);
        againNewPhonenumber = (EditText) findViewById(R.id.tv_verify_new_phonenumber);
        oldPhonenumber = (TextView) findViewById(R.id.tv_old_phonenumber);
        oldPhonenumber.setEnabled(false);
        rpSubmit = (Button) findViewById(R.id.btn_chenge_phonenumber_submit);
        alterVerifCode = (EditText) findViewById(R.id.et_alterphone_verif_code);
        btVerif = (CountDownButton) findViewById(R.id.btn_alterphone_get_verif_code);
        tv_title = (TextView) findViewById(R.id.tv_title_contre);
        tv_title.setText("手机号码变更申请");
        bt_Back = (TextView) findViewById(R.id.bt_title_left);
        bt_Back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        btVerif.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (DateUtil.isFastDoubleClick()) {
                    return;
                } else {
                    // 获取验证码
                    getVerifCode();

                }
            }
        });

        /** 提交申请 */
        rpSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (DateUtil.isFastDoubleClick()) {
                    return;
                } else {
                    // TODO Auto-generated method stub
                    oldPhone = oldPhonenumber.getText().toString();

                    if (oldPhone == null || (oldPhone != null && oldPhone.equals(""))) {

                        oldPhone = codlnum;
                    }

                    mobile = newPhonenumber.getText().toString();
                    if (mobile == null || (mobile != null && mobile.equals(""))) {
                        // ToastCustom.showMessage(this, "请输入手机号码",
                        // Toast.LENGTH_SHORT);
                        newPhonenumber.requestFocus();
                        ToastCustom.showMessage(
                                RequestPhonenumberChengesActivity.this,
                                "手机号码不能为空", Toast.LENGTH_SHORT);
                        return;
                    } else if (!Checkingroutine.isNumeric(mobile)) {
                        ToastCustom.showMessage(
                                RequestPhonenumberChengesActivity.this,
                                "只能输入数字", Toast.LENGTH_SHORT);
                        return;
                    } else if (mobile.length() != 11) {
                        ToastCustom.showMessage(
                                RequestPhonenumberChengesActivity.this,
                                "手机号码必须为11位数字", Toast.LENGTH_SHORT);
                        return;
                    }

                    mobileAgain = againNewPhonenumber.getText().toString();

                    if (mobileAgain == null
                            || (mobileAgain != null && mobileAgain.equals(""))) {
                        againNewPhonenumber.requestFocus();
                        ToastCustom.showMessage(
                                RequestPhonenumberChengesActivity.this,
                                "手机号码不能为空", Toast.LENGTH_SHORT);
                        return;
                    } else if (!mobileAgain.equals(mobile)) {
                        againNewPhonenumber.requestFocus();
                        ToastCustom.showMessage(
                                RequestPhonenumberChengesActivity.this,
                                "两次手机号码不一致", Toast.LENGTH_SHORT);
                        return;
                    }

                    if (oldPhone.equals(mobile)) {
                        ToastCustom.showMessage(
                                RequestPhonenumberChengesActivity.this,
                                "新手机号码和旧手机号码一致,请检查后在修改!", Toast.LENGTH_SHORT);
                        return;
                    }
                    verifNo = alterVerifCode.getText().toString();

                    if (verifNo == null
                            || (verifNo != null && verifNo.equals(""))) {
                        ToastCustom.showMessage(
                                RequestPhonenumberChengesActivity.this,
                                "验证码不能为空!", Toast.LENGTH_SHORT);
                    }
                    Log.e("", "  没输入手机号  " + oldPhone);
                    /** 提交申请 */
//                    oldChangeMobile();
                    updateMobile(oldPhone, mobile, btVerif.getVCodeId(), verifNo);
                }
            }
        });
//		queryMobileInfo();
        oldPhonenumber.setText(mMobile);
    }

    /**
     * 业务员变更手机号
     *
     * @param oldMobile
     * @param newMobile
     * @param msgId
     * @param msgCode
     */
    private void updateMobile(String oldMobile, String newMobile, String msgId, String msgCode) {
        ProgressDialogUtil.showProgressDlg(getActivity(), "");
        Request.getSalesmanChangeUserMobile(oldMobile, newMobile, msgId, msgCode,
                new CallbackString() {
                    @Override
                    public void onFailure(String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        WaringDialogUtils.showWaringDialog(getActivity(), msg, null);
                    }

                    @Override
                    public void onSuccess(String data) {
                        ProgressDialogUtil.dismissProgressDlg();
                        ((AppContext) getApplication()).setMobile(mobile);
                        editor.putString(Constans.Login.MOBILE, mobile);
                        editor.putString("userp", mobile);
                        editor.commit();
                        finish();
                    }

                    @Override
                    public void onNetError(int code, String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        WaringDialogUtils.showWaringDialog(getActivity(), msg, null);
                    }
                });
    }

    private void oldChangeMobile() {
        PhonenumberInfoChangeTask task = new PhonenumberInfoChangeTask();
        task.execute(HttpUrls.APPLY_PHONENUMBER_CHANGES + "",
                mobile, oldPhone, verifNo, mrecnum);
    }

    /* 查看旧手机号码 */
    private void queryMobileInfo() {
        PhonenumberInfoTask task = new PhonenumberInfoTask();
        task.execute(HttpUrls.BUSSINESS_INFO + "", custId, "4", "0");
    }

    /**
     * 提交申请Task
     *
     * @author liangge
     */
    class PhonenumberInfoChangeTask extends
            AsyncTask<String, Integer, HashMap<String, Object>> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            showLoadingDialog("正在提交资料...");
        }

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            // TODO Auto-generated method stub
            String[] values = {params[0], params[1], params[2], params[3], params[4]};
            return NetCommunicate.get(HttpUrls.APPLY_PHONENUMBER_CHANGES,
                    values);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            loadingDialogWhole.dismiss();
            if (result != null) {

                // 注册成功
                if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
                    Intent intent = new Intent(
                            RequestPhonenumberChengesActivity.this,
                            NewRealNameAuthenticationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("intentObj", "RequestPhonenumberChenges");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    ToastCustom.showMessage(RequestPhonenumberChengesActivity.this,
                            result.get(Entity.RSPMSG).toString(),
                            Toast.LENGTH_SHORT);
//					showSingleWarnDialog("提交失败!");
                }

            } else {
                showSingleWarnDialog("fail!");
            }
            super.onPostExecute(result);
        }
    }

    /**
     * 查看手机号码Task
     *
     * @author Administrator
     */
    class PhonenumberInfoTask extends
            AsyncTask<String, Integer, HashMap<String, Object>> {

        @Override
        protected void onPreExecute() {
            showLoadingDialog("正在查询商户信息...");
        }

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            String[] values = {params[0], params[1], params[2], params[3]};
            return NetCommunicate.get(HttpUrls.BUSSINESS_INFO, values);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            loadingDialogWhole.dismiss();
            if (result != null) {
                if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {

                    String phone = result.get("PHONENUMBER").toString();
                    String setphone = phone.substring(0, 3);
                    String getphone = phone.substring(phone.length() - 4);
                    oldPhonenumber.setHint(setphone + "****" + getphone);

                } else {
                    ToastCustom.showMessage(
                            RequestPhonenumberChengesActivity.this,
                            result.get(Entity.RSPMSG).toString(),
                            Toast.LENGTH_SHORT);
                }
            }
            super.onPostExecute(result);
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifCode() {

        oldPhone = oldPhonenumber.getText().toString();

        mobile = newPhonenumber.getText().toString();
        if (mobile == null || (mobile != null && mobile.equals(""))) {
            // ToastCustom.showMessage(this, "请输入手机号码",
            // Toast.LENGTH_SHORT);
            newPhonenumber.requestFocus();
            ToastCustom.showMessage(RequestPhonenumberChengesActivity.this,
                    "手机号码不能为空", Toast.LENGTH_SHORT);
            return;
        } else if (!Checkingroutine.isNumeric(mobile)) {
            ToastCustom.showMessage(RequestPhonenumberChengesActivity.this,
                    "只能输入数字", Toast.LENGTH_SHORT);
            return;
        } else if (mobile.length() != 11) {
            ToastCustom.showMessage(RequestPhonenumberChengesActivity.this,
                    "手机号码必须为11位数字", Toast.LENGTH_SHORT);
            return;
        }

        mobileAgain = againNewPhonenumber.getText().toString();

        if (mobileAgain == null
                || (mobileAgain != null && mobileAgain.equals(""))) {
            againNewPhonenumber.requestFocus();
            ToastCustom.showMessage(RequestPhonenumberChengesActivity.this,
                    "手机号码不能为空", Toast.LENGTH_SHORT);
            return;
        } else if (!mobileAgain.equals(mobile)) {
            againNewPhonenumber.requestFocus();
            ToastCustom.showMessage(RequestPhonenumberChengesActivity.this,
                    "两次手机号码不一致", Toast.LENGTH_SHORT);
            return;
        }

        if (oldPhone.equals(mobile)) {
            ToastCustom.showMessage(RequestPhonenumberChengesActivity.this,
                    "新手机号码和旧手机号码一致,请检查后在获取验证码!", Toast.LENGTH_SHORT);
            return;
        }
        btVerif.getVCode(mobile, null);

//		btVerif.setEnabled(false);
//		final String[] values = {
//				HttpUrls.QUERY_PHONENUMBER_CHANGES_VERIFCODE + "", mobile,codlnum };
//
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//
//				HashMap<String, Object> result = NetCommunicate.get(
//						HttpUrls.QUERY_PHONENUMBER_CHANGES_VERIFCODE, values);
//
//				Log.e("", " = = = ="+result.toString());
//				Message msg = new Message();
//				if (result == null) {
//					msg.what = 10;
//					msg.obj = result;
//				} else {
//					msg.what = 11;
//					msg.obj = result;
//				}
//				mHandler.sendMessage(msg);
//			}
//		}).start();

    }
}
