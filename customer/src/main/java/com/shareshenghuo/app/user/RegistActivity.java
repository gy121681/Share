package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import cn.jpush.android.api.JPushInterface;

import com.shareshenghuo.app.user.util.PwdUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.network.request.RegistRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.Util;
import com.shareshenghuo.app.user.util.ViewUtil;
import com.shareshenghuo.app.user.widget.CountDownButton;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.user.widget.dialog.TwoButtonDialog;

public class RegistActivity extends BaseTopActivity implements OnClickListener {

    private EditText edAccount;
    private EditText edVCode;
    private EditText edPwd;
    private EditText edPwd2, edrec_account;
    private EditText edReferralCode;
    private CountDownButton btnVCode;
    private Button btnRegist;
    private TwoButtonDialog downloadDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();
    }

    public void initView() {
        initTopBar("新用户注册");
        edAccount = getView(R.id.edMobile);
        edVCode = getView(R.id.edVCode);
        edPwd = getView(R.id.edPassword);
        edPwd2 = getView(R.id.edPassword2);
        edReferralCode = getView(R.id.edReferralCode);
        btnVCode = getView(R.id.btnGetVCode);
        edrec_account = getView(R.id.edrec_account);
        btnRegist = getView(R.id.btnRegist);

        btnVCode.setOnClickListener(this);
        btnRegist.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        btnVCode.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        btnVCode.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGetVCode:
                if (TextUtils.isEmpty(edAccount.getText()) || edAccount.getText().length() != 11) {
                    ViewUtil.showEditError(edAccount, "请输入正确的手机号");
                    return;
                }
                btnVCode.getVCode(edAccount.getText().toString(), null);
                break;

            case R.id.btnRegist:
                register();
                break;
        }
    }

    public void register() {
        if (TextUtils.isEmpty(edAccount.getText()) || edAccount.getText().length() != 11) {
            T.showShort(RegistActivity.this, "请输入正确的手机号");
//			ViewUtil.showEditError(edAccount, "请输入正确的手机号");
            return;
        }
        if (ViewUtil.checkEditEmpty(edVCode, "请输入验证码"))
            return;
        if (TextUtils.isEmpty(edPwd.getText()) || edPwd.getText().length() < 6) {
            T.showShort(RegistActivity.this, "请输入6-20位密码");
//			ViewUtil.showEditError(edPwd, "请输入6-20位密码");
            return;
        }

        if (!Util.pwdutil(edPwd.getText().toString())) {
            initDialog("密码必须是6-18位英文字母、数字或字符组成(不能是纯数字或纯字母)", "确定", "");

//			T.showShort(RegistActivity.this, "密码安全等级不够，请重新设置");
            return;
        }


//		if(TextUtils.isEmpty(edrec_account.getText()) || edrec_account.getText().length()!=11) {
//			ViewUtil.showEditError(edrec_account, "请输入正确的手机号");
//			return;
//		}
        if (!edPwd.getText().toString().equals(edPwd2.getText().toString())) {
            T.showShort(RegistActivity.this, "确认密码不一致");
//			ViewUtil.showEditError(edPwd2, "确认密码不一致");
            return;
        }
        ProgressDialogUtil.showProgressDlg(this, "注册中");
        RegistRequest req = new RegistRequest();
        req.latitude = CityManager.getInstance(this).latitude + "";
        req.longitude = CityManager.getInstance(this).longitude + "";
        req.account = edAccount.getText().toString();
        req.password = PwdUtils.getEncripyPwd(edPwd.getText().toString(), 3);
        req.msg_id = btnVCode.getVCodeId();
        req.msg_code = edVCode.getText().toString();
        req.invitation_code = edrec_account.getText().toString();
        req.registration_id = JPushInterface.getRegistrationID(this);
        RequestParams params = new RequestParams("utf-8");
        try {
            params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new HttpUtils().send(HttpMethod.POST, Api.URL_REGISTER, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                ProgressDialogUtil.dismissProgressDlg();
                T.showNetworkError(RegistActivity.this);
            }

            @Override
            public void onSuccess(ResponseInfo<String> resp) {
                ProgressDialogUtil.dismissProgressDlg();
                Log.d("Response", resp.result + "");
                BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
                if (Api.SUCCEED == bean.result_code) {
                    T.showShort(RegistActivity.this, "注册成功");
                    finish();
                } else {
                    T.showShort(RegistActivity.this, bean.result_desc);
                }
            }
        });
    }

    private void initDialog(String content, String left, String right) {
        // TODO Auto-generated method stub
        downloadDialog = new TwoButtonDialog(RegistActivity.this, R.style.CustomDialog,
                "", content, left, right, true, new OnMyDialogClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (v.getId()) {
                    case R.id.Button_OK:
                        edPwd.setText("");
                        edPwd2.setText("");
                        downloadDialog.dismiss();
                        break;
                    case R.id.Button_cancel:
                        edPwd.setText("");
                        edPwd2.setText("");
                        downloadDialog.dismiss();
                    default:
                        break;
                }
            }
        });
        downloadDialog.show();
    }
}
