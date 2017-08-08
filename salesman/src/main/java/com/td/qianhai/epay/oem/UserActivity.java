package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.share.app.LicenseDialog;
import com.share.app.entity.response.Constans;
import com.share.app.entity.response.SAlesmanLoginResponse;
import com.share.app.network.CallbackObject;
import com.share.app.network.Request;
import com.share.app.entity.response.Constans.*;
import com.share.app.utils.MD5Utils;
import com.share.app.utils.ParseUtil;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.GetImageUtil;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.unlock.StringUtil;
import com.td.qianhai.epay.oem.unlock.UnlockLoginActivity;
import com.td.qianhai.epay.oem.views.AnimationUtil;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.epay.utils.AppManager;
import com.td.qianhai.epay.utils.ChineseUtil;
import com.td.qianhai.fragmentmanager.FmMainActivity;

public class UserActivity extends BaseActivity1 implements OnClickListener {

    /* 提示,登录 */
    private LinearLayout level_t, level_b;
    private EditText et_user, et_pass;
    private Button btn_login;
    private TextView btn_register;
    private OneButtonDialogWarn warnDialog;
    private String userName, passWord;
    private TextView tvRegetPass, e_user_del;
    /**
     * 是否第一次启用致富宝
     */
    private String lognum;
    private CheckBox e_pwd;
    public static Activity context;
    private String appid, userid;
    private ImageView qh_logo_img;
    private Editor editor;
    private SharedPreferences share;
    private LayoutInflater mInflater;
    private List<HashMap<String, Object>> plist;
    private LinearLayout lin_users;
    private CheckBox cb_title_contre, ckAgreeAgreement;
    private HorizontalScrollView hscrollView;
    private boolean isshow = false;
    private int childview = -1;

    private void initView() {
        ckAgreeAgreement = (CheckBox) findViewById(R.id.register_agree_agreement);
        e_user_del = (TextView) findViewById(R.id.e_user_del);
        et_user = (EditText) findViewById(R.id.reg_account_number);
        et_pass = (EditText) findViewById(R.id.reg_serial);
        hscrollView = (HorizontalScrollView) findViewById(R.id.hscrollView);
        cb_title_contre = (CheckBox) findViewById(R.id.cb_title_contre);
        e_pwd = (CheckBox) findViewById(R.id.e_pwd);
        level_t = (LinearLayout) findViewById(R.id.level_t);
        level_b = (LinearLayout) findViewById(R.id.level_b);
        qh_logo_img = (ImageView) findViewById(R.id.qh_logo_imgs);
        btn_register = (TextView) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        tvRegetPass = (TextView) findViewById(R.id.account_reget_pass);
//		tvRegetPass.setText(Html.fromHtml("<u>忘 记 密 码?</u>"));
        e_pwd.setOnClickListener(this);
        tvRegetPass.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        e_user_del.setOnClickListener(this);

//		AnimationUtil.RoteAnimation(this, qh_logo_img);

        et_user.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    e_user_del.setVisibility(View.VISIBLE);
                } else {
                    e_user_del.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

        et_user.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
                    if (et_user.getText().toString().length() > 0) {
                        e_user_del.setVisibility(View.VISIBLE);
                    }

                } else {
                    e_user_del.setVisibility(View.GONE);
                }
            }
        });
        et_pass.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
//					e_pwd.setVisibility(View.VISIBLE);
                } else {
                    e_pwd.setVisibility(View.GONE);
                }
            }
        });


        e_pwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

                CharSequence text = e_pwd.getText();
                //Debug.asserts(text instanceof Spannable);

                // TODO Auto-generated method stub
                if (arg1) {
                    et_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    et_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        cb_title_contre.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (hscrollView.getVisibility() == View.GONE) {
                    hscrollView.setVisibility(View.VISIBLE);
                    cb_title_contre.setChecked(true);
                } else {
                    cb_title_contre.setChecked(false);
                    hscrollView.setVisibility(View.GONE);

                }
            }
        });
        getView(R.id.btn_agent_sign_up).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), OnlineWeb.class);
                intent.putExtra("titleStr", "服务中心");
                intent.putExtra("urlStr", HttpUrls.SHARE_WEB_SERVICES_CENTER);
                startActivity(intent);
            }
        });
    }

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppContext.getInstance().addActivity(this);
        editor = MyCacheUtil.setshared(UserActivity.this);
        share = MyCacheUtil.getshared(UserActivity.this);
        context = this;
        appid = ((AppContext) getApplication()).getAppid();
        userid = ((AppContext) getApplication()).getUserid();
        mInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        plist = new ArrayList<HashMap<String, Object>>();
        lin_users = (LinearLayout) findViewById(R.id.lin_users);
        initView();
        for (int i = 1; i <= 4; i++) {
            String puser = MyCacheUtil.getshared(UserActivity.this).getString("suser" + i, "");
            String spwd = MyCacheUtil.getshared(UserActivity.this).getString("spwd" + i, "");
            String simg = MyCacheUtil.getshared(UserActivity.this).getString("simg" + i, "");
            if (puser != null && !puser.equals("") && !puser.equals("-1")) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("suser", puser);
                map.put("spwd", spwd);
                map.put("simg", simg);
                plist.add(map);


                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(240, LayoutParams.MATCH_PARENT);
                final LinearLayout layout = new LinearLayout(this);
                layout.setLayoutParams(lp);
                layout.setGravity(Gravity.CENTER);
                layout.setOrientation(LinearLayout.VERTICAL);
//			layout.setBackground(getResources().getDrawable(R.drawable.form_bg_wihte));
                LinearLayout.LayoutParams lpim = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                lpim.weight = 1.8f;
                LinearLayout.LayoutParams lptv = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                lptv.weight = 3.2f;
                ImageView img = new ImageView(this);
                img.setLayoutParams(lpim);
                TextView tv = new TextView(this);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_24));
                tv.setGravity(Gravity.CENTER);
                Log.e("", " = = = " + simg);
                if (simg != null && !simg.equals("")) {
                    new GetImageUtil(this, img, HttpUrls.HOST_POSM + simg);
                } else {
                    img.setImageResource(R.drawable.share_s_public_head_small_big);
                }
                final RelativeLayout relayout = new RelativeLayout(this);
                relayout.setLayoutParams(lp);
                tv.setLayoutParams(lptv);
                String setphone = puser.substring(0, 3);
                String getphone = puser.substring(puser.length() - 4);
                tv.setText(setphone + "****" + getphone);
                layout.addView(img);
                layout.addView(tv);
                layout.setId(i - 1);

                ImageView imgdel = new ImageView(UserActivity.this);
                RelativeLayout.LayoutParams lpdel = new RelativeLayout.LayoutParams(40, 40);
                lpdel.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lpdel.setMargins(0, 0, 30, 0);
                imgdel.setLayoutParams(lpdel);
                imgdel.setImageResource(R.drawable.del_img);
                imgdel.setId(i);
                imgdel.setVisibility(View.GONE);
                imgdel.setOnClickListener(UserActivity.this);

                relayout.addView(layout);
                relayout.addView(imgdel);
                lin_users.addView(relayout);
                imgdel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Log.e("", "点击了 = = =  " + "suser" + v.getId());
//					if(plist.size()<=1){
//						hscrollView.setVisibility(View.GONE);
//						return;
//					}
                        if (et_user.getText() != null && et_user.getText().toString().equals(MyCacheUtil.getshared(UserActivity.this).getString("suser" + v.getId(), ""))) {
                            et_user.setText("");
                            editor.putString("userp", "");
                            editor.commit();
                        }
                        editor.putString("suser" + v.getId(), "-1");
                        editor.putString("spwd" + v.getId(), "");
                        editor.putString("simg" + v.getId(), "");

                        editor.commit();
//					plist.remove(v.getId()-1);
                        relayout.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                hscrollView.setVisibility(View.GONE);
                            }
                        }, 1000);

                    }
                });


                layout.setOnLongClickListener(new OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {
                        // TODO Auto-generated method stub

                        AnimationUtil.BtnSpecialAnmations1(UserActivity.this, relayout, 500);

                        relayout.getChildAt(1).setVisibility(View.VISIBLE);
                        isshow = true;

                        return true;
                    }
                });
                layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
//						switch (layout.getId()) {
                        if (isshow) {

                            for (int j = 0; j < plist.size(); j++) {
                                RelativeLayout re = (RelativeLayout) lin_users.getChildAt(j);
                                re.getChildAt(1).setVisibility(View.GONE);
                            }
                            isshow = false;
                            return;
                        }
                        hscrollView.setVisibility(View.GONE);
                        String userphone = plist.get(layout.getId()).get("suser").toString();
                        String userpwd = plist.get(layout.getId()).get("spwd").toString();
                        if (userphone != null && !userphone.equals("") && userpwd != null && !userpwd.equals("")) {
                            userName = userphone;
                            passWord = userpwd;
                            login(userName, getEncriptPwd(passWord));
//                            LoginTask task = new LoginTask();
//                            String pcsim = "11111111";
//                            String aa = JPushInterface.getRegistrationID(UserActivity.this);
//                            String idd = MyCacheUtil.getshared(UserActivity.this).getString("IDD", "");
//                            task.execute("199002", userphone, userpwd, pcsim, "", "2", "", "", "1", HttpUrls.APPNUM, "", idd, aa + "");
                        }
                    }
                });

            }
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        String usermobile = share.getString("userp", "");
        if (usermobile != null && !usermobile.equals("")) {
            et_user.setText(usermobile);
            e_user_del.setVisibility(View.VISIBLE);
            et_pass.requestFocus();
        }

        String pass = share.getString("pass", "");
        if (pass != null && !pass.equals("")) {
            et_pass.setText(pass);
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_register:
                LicenseDialog dialog = new LicenseDialog();
                dialog.setUrl(HttpUrls.REGISTRATIONAGREEMENT);
                dialog.setListener(new LicenseDialog.LicenseAgreeListener() {
                    @Override
                    public void agreeLicense() {
                        Intent intent = new Intent();
                        intent.setClass(UserActivity.this, RegisterActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
                break;
            case R.id.btn_login:
                System.out.println();
                if (level_b.getVisibility() == View.GONE) {
                    level_t.setVisibility(View.GONE);
                    level_b.setVisibility(View.VISIBLE);
                } else {
                    userName = et_user.getText().toString();
                    if (userName == null || userName.equals("")) {
                        Toast.makeText(getApplicationContext(), "手机号码不能为空",
                                Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(UserActivity.this, "手机号码不能为空...");
                        return;
                    } else if (userName.length() != 11) {
                        Toast.makeText(getApplicationContext(), "手机号码长度有误",
                                Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(UserActivity.this, "手机号码长度有误...");
                        return;
                    }
                    passWord = et_pass.getText().toString();
                    if (passWord == null || passWord.equals("")) {
                        Toast.makeText(getApplicationContext(), "密码不能为空",
                                Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(UserActivity.this, "密码不能为空...");
                        return;
                    } else if (passWord.length() < 6) {
                        Toast.makeText(getApplicationContext(), "密码长度有误",
                                Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(UserActivity.this, "密码长度有误...");
                        return;
                    }

                    if (ChineseUtil.checkNameChese(passWord)) {
                        Toast.makeText(getApplicationContext(), "密码不能为中文",
                                Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(UserActivity.this, "密码不能为中文");
                        return;
                    }
                    login(userName, getEncriptPwd(passWord));
//                    oldLogin();
                }
                break;

            case R.id.account_reget_pass:
                intent = new Intent(UserActivity.this, RegetPwActivity.class);
                startActivity(intent);
                break;
            case R.id.e_user_del:
                if (et_user.getText().toString().length() > 0) {
                    et_user.requestFocus();
                    et_user.setText("");
                }

                break;
            default:
                break;
        }
    }

    /**
     * 获取加密后的密码
     *
     * @param pwd
     * @return
     */
    private String getEncriptPwd(String pwd) {
        String result = pwd;
        for (int i = 0; i < 3; i++) {
            result = MD5Utils.getMD5String(result);
        }
        return result;
    }

    private void login(String mobile, String password) {
        showLoadingDialog("正在努力加载...");
        Request.getSalesmanSalesmanLogin(mobile, password, new CallbackObject<SAlesmanLoginResponse>() {
            @Override
            public void onFailure(String msg) {
                loadingDialogWhole.dismiss();
                toast(msg);
            }

            @Override
            public void onSuccess(SAlesmanLoginResponse data) {
                loadingDialogWhole.dismiss();
                // TODO: 2017/7/24 保存用户相关信息
                saveLoginInfo(data);
                checkUserInfo(data);
            }

            @Override
            public void onNetError(int code, String msg) {
                loadingDialogWhole.dismiss();
                warnDialog = new OneButtonDialogWarn(UserActivity.this,
                        R.style.CustomDialog, "提示", "请求服务器失败！！！", "确定",
                        new OnMyDialogClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                warnDialog.dismiss();
                            }
                        });
                warnDialog.show();
            }
        });
    }

    /**
     * 保存登录信息
     */
    private void saveLoginInfo(SAlesmanLoginResponse data) {
        //保存头像
        String img = "";
        if (data.getPhoto() != null) {
            editor.putString(Login.PERSONPIC, data.getPhoto());
            img = data.getPhoto();
        }
        //保存登录密码
        if (ckAgreeAgreement.isChecked()) {
            editor.putString(Login.PASSWORD, passWord);
            editor.putString(Login.USERRP, data.getMobile());
            initphone(img);
        }
        editor.putString(Login.USERID, data.getId());
        editor.putString(Login.MOBILE, data.getMobile());
        editor.putString(Login.MERCNUM, data.getMercnum());
        editor.putString(Login.MERCNAM, data.getMercnam());
        editor.putString(Login.TYPE, data.getType());
        editor.putString(Login.LEVEL, data.getLevel());
        editor.putString(Login.PHOTO, data.getPhoto());
        editor.putString(Login.PAYPASSWORD, data.getPayPassword());
        editor.putString(Login.CERTIFICATIONSTEP, data.getCertificationStep());
        editor.putString(Login.CERTIFICATIONSTATUS, data.getCertificationStatus());
        editor.putString(Login.REALNAME, data.getRealName());
        editor.putString(Login.PERSONNO, data.getPersonNo());
        editor.putString(Login.AGENTTYPE, data.getAgentType());
        if (data.getAgentAreaList() == null || data.getAgentAreaList().isEmpty()) {
            editor.putString(Login.AGENTAREALIST, "");
        } else {
            editor.putString(Login.AGENTAREALIST, ParseUtil.toJson(data.getAgentAreaList()));
        }
        editor.putString(Login.MERSTS, data.getCertificationStatus().equals("2") ? "0" : "1");
        editor.commit();
    }

    private void checkUserInfo(SAlesmanLoginResponse data) {
        Intent intent;
        if (StringUtil.isEmpty(share.getString(Common.C_USERMOBILE, ""))) {
            editor.putString(Common.C_USERMOBILE,
                    data.getMobile());
            editor.putString(Common.C_USERPWD, passWord);

            editor.commit();
            intent = new Intent(UserActivity.this,
                    UnlockLoginActivity.class);
            intent.putExtra("refresh", "refresh");
            Log.e("", "11111111111");
        } else {
            if (share.getString(Common.C_USERMOBILE, "").equals(data.getMobile())) {
                Log.e("", "222222222222");
                intent = new Intent(UserActivity.this,
                        FmMainActivity.class);
            } else {
                editor.putString(Common.C_USERMOBILE, data.getMobile());
                editor.putString(Common.C_USERPWD, passWord);
                editor.commit();
                Log.e("", "3333333333333");
                intent = new Intent(UserActivity.this,
                        UnlockLoginActivity.class);
                intent.putExtra("refresh", "refresh");
            }
        }

//        if (!TextUtils.equals(data.getCertificationStatus(), "2") && TextUtils.equals(data.getCertificationStep(), "1")) {
//            //认证未通过，已验证身份证信息
//            editor.putString(Common.C_USERMOBILE, "");
//            editor.putString(Common.C_USERPWD, "");
//            editor.commit();
//            Intent it = new Intent(UserActivity.this,
//                    SetPayPassActivity.class);
//            startActivity(it);
//            Toast.makeText(getApplicationContext(), "请设置支付密码",
//                    Toast.LENGTH_SHORT).show();
//							ToastCustom.showMessage(UserActivity.this, "请设置支付密码!");
//            finish();

//        } else {

            startActivity(intent);
            finish();
//							ToastCustom.showMessage(UserActivity.this, "欢迎登录移动支付");
//        }
    }

    /**
     * 原登录功能。
     */
    private void oldLogin() {
        // 登录功能。
        LoginTask task = new LoginTask();
        // task.execute("199002", "13917830795","111111",
        // "18917114556");
        String pcsim = "11111111";
        String aa = JPushInterface.getRegistrationID(this);
        String idd = MyCacheUtil.getshared(this).getString("IDD", "");
//				PushManager.startWork(context, userName, "Q8htwUGPqpfpU5uFo4zGdp4C");
        task.execute("199002", userName, passWord, pcsim, "", "2", "", "", "1", HttpUrls.APPNUM, "", idd, aa + "");
    }

    /**
     * 登录异步类
     *
     * @author liangge
     */
    class LoginTask extends AsyncTask<String, Integer, HashMap<String, Object>> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            showLoadingDialog("正在努力加载...");
        }

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            String[] values = {params[0], params[1], params[2], params[3],
                    params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12]};
            return NetCommunicate.get(HttpUrls.LOGIN, values);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            loadingDialogWhole.dismiss();
            final HashMap<String, Object> results = result;
            Intent intent;
            if (result != null) {
                if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
                    //保存登录账号与密码
                    if (ckAgreeAgreement.isChecked()) {

                        editor.putString("pass", passWord);
                        editor.commit();
                    }
                    String img = "";
                    if (result.get("PERSONPIC") != null) {
                        editor.putString("PERSONPIC", result.get("PERSONPIC").toString());
                        img = result.get("PERSONPIC").toString();
                    }
                    if (ckAgreeAgreement.isChecked()) {
                        initphone(img);
                    }
                    //保存地区信息
                    if (result.get("PROVID") != null) {
                        editor.putString("PROVIDs", result.get("PROVID").toString());
                    }
                    if (result.get("CITYID") != null) {
                        editor.putString("CITYIDs", result.get("CITYID").toString());
                    }
                    if (result.get("AREAID") != null) {
                        editor.putString("AREAIDs", result.get("AREAID").toString());
                    }

                    if (result.get("NOTICEMESSAGE") != null) {
                        editor.putString("NOTICEMESSAGE", result.get("NOTICEMESSAGE").toString());
                    } else {

                    }
                    if (result.get("ISAREAAGENT") != null) {
                        editor.putString("ISAREAAGENT", result.get("ISAREAAGENT").toString());
                    }

                    if (result.get("NCONTENT") != null) {
                        editor.putString("NCONTENT", result.get("NCONTENT").toString());
                        editor.putString("NCREATETIM", result.get("NCREATETIM").toString());
                    } else {
                        editor.putString("NCONTENT", "");
                        editor.putString("NCREATETIM", "");
                    }

                    if (result.get("NOCARDFEERATE") != null) {
                        ((AppContext) UserActivity.this.getApplication())
                                .setNocein(result.get("NOCARDFEERATE").toString());

                        editor.putString("nocardfeerate", result.get("NOCARDFEERATE").toString());
                        editor.putString("oemfeerate", result.get("OEMFEERATE").toString());
                        editor.commit();
                    } else {
                        editor.putString("nocardfeerate", "0.69");

                        editor.commit();
                    }
                    if (result.get("IDCARDPICSTA") != null && result.get("CUSTPICSTA") != null && result.get("FRYHKIMGPATHSTA") != null) {
                        String a = result.get("IDCARDPICSTA").toString();
                        String b = result.get("CUSTPICSTA").toString();
                        String c = result.get("FRYHKIMGPATHSTA").toString();
                        ((AppContext) getApplication()).setStateaudit(a + b + c);
                    } else {

                    }
                    if (result.get("PERSONPIC") != null) {
                        editor.putString("PERSONPIC", result.get("PERSONPIC").toString());
                    }
                    editor.putString("pwd", passWord);

//					editor.putString("ISBRUSHOTHERSCARD", result.get("ISBRUSHOTHERSCARD").toString());.
                    editor.putString("OEMID", result.get("LOEMID").toString());
                    editor.putString("ISRETAILERS", result.get("ISRETAILERS").toString());
                    editor.putString("ISSALEAGT", result.get("ISSALEAGT").toString());
                    editor.putString("ISGENERALAGENT", result.get("ISGENERALAGENT").toString());
                    editor.putString("ISSENIORMEMBER", result.get("ISSENIORMEMBER").toString());
                    editor.commit();

                    editor.putString("userp", result
                            .get("PHONENUMBER").toString());
                    if (results.get("MERSTS").toString().equals("0")) {
                        if (results.get("CURROL") != null) {
                            ((AppContext) getApplicationContext()).setCurrol(results.get("CURROL").toString());
                            editor.putString("CURROL", results.get("CURROL").toString());

                            editor.commit();
                            if (results.get("CURROL").toString().equals("0")) {

                            } else if (results.get("CURROL") != null) {
                                editor.putString("AGENTID", results.get("AGENTID").toString());

                                editor.commit();
                                ((AppContext) getApplicationContext()).setAgentid(results.get("AGENTID").toString());
                            }
                        }
                        editor.putString("CustId", result.get("PHONENUMBER").toString());
                        editor.putString("Txnsts", result.get("TXNSTS").toString());
                        editor.putString("Mobile", result.get("PHONENUMBER").toString());
                        editor.putString("MercNum", result.get("MERCNUM").toString());
                        editor.putString("STS", result.get("STS").toString());
                        editor.putString("MERSTS", result.get("MERSTS").toString());
                        editor.commit();
                        ((AppContext) getApplicationContext()).setCustId(result
                                .get("PHONENUMBER").toString());
                        ((AppContext) getApplicationContext()).setMobile(result
                                .get("PHONENUMBER").toString());
                        ((AppContext) getApplicationContext()).setMerSts(result
                                .get("MERSTS").toString());
                        ((AppContext) getApplicationContext())
                                .setCustPass(passWord);
                        ((AppContext) getApplicationContext()).setTxnsts(result
                                .get("TXNSTS").toString());
                        ((AppContext) getApplicationContext()).setSts(result
                                .get("STS").toString());
                        ((AppContext) UserActivity.this.getApplication())
                                .setMercNum(result.get("MERCNUM").toString());


//						((AppContext) UserActivity.this.getApplication())
//						.setNocein(result.get("NOCARDFEERATE").toString());
//						
//						Log.e("", " = = = =  ="+((AppContext) UserActivity.this.getApplication()).getNocein());


                        if (StringUtil.isEmpty(share.getString(Common.C_USERMOBILE, ""))) {
                            editor.putString(Common.C_USERMOBILE,
                                    result.get("PHONENUMBER").toString());
                            editor.putString(Common.C_USERPWD, passWord);

                            editor.commit();
                            intent = new Intent(UserActivity.this,
                                    UnlockLoginActivity.class);
                            intent.putExtra("refresh", "refresh");
                            Log.e("", "11111111111");
                        } else {
                            if (share.getString(Common.C_USERMOBILE, "").equals(result
                                    .get("PHONENUMBER").toString())) {
                                Log.e("", "222222222222");
                                intent = new Intent(UserActivity.this,
                                        FmMainActivity.class);
                            } else {
                                editor.putString(Common.C_USERMOBILE,
                                        result.get("PHONENUMBER").toString());
                                editor.putString(Common.C_USERPWD, passWord);
                                editor.commit();
                                Log.e("", "3333333333333");
                                intent = new Intent(UserActivity.this,
                                        UnlockLoginActivity.class);
                                intent.putExtra("refresh", "refresh");
                            }
                        }

                        lognum = result.get("LOGNUM").toString();

                        if (lognum != null && lognum.equals("0")) {
                            editor.putString(Common.C_USERMOBILE, "");
                            editor.putString(Common.C_USERPWD, "");
                            editor.commit();
                            Intent it = new Intent(UserActivity.this,
                                    SetPayPassActivity.class);
                            startActivity(it);
                            Toast.makeText(getApplicationContext(), "请设置支付密码",
                                    Toast.LENGTH_SHORT).show();
//							ToastCustom.showMessage(UserActivity.this, "请设置支付密码!");
                            finish();

                        } else {

                            startActivity(intent);
                            finish();
//							ToastCustom.showMessage(UserActivity.this, "欢迎登录移动支付");
                        }
                    } else {
                        if (results != null) {
                            ((AppContext) getApplicationContext()).setSts(result
                                    .get("STS").toString());
                            ((AppContext) getApplicationContext()).setTxnsts(result
                                    .get("TXNSTS").toString());
                            if (result.get("NOCARDFEERATE") != null) {
                                ((AppContext) UserActivity.this.getApplication())
                                        .setNocein(result.get("NOCARDFEERATE").toString());
                            }

                            if (results.get("PHONENUMBER") != null) {
                                editor.putString("oemfeerate", result.get("OEMFEERATE").toString());
                                editor.putString("Txnsts", result.get("TXNSTS").toString());
                                editor.putString("CustId", result.get("PHONENUMBER").toString());
                                editor.putString("Mobile", result.get("PHONENUMBER").toString());
                                editor.putString("STS", result.get("STS").toString());
                                editor.putString("MERSTS", result.get("MERSTS").toString());
                                if (results.get("CURROL") != null) {
                                    editor.putString("CURROL", results.get("CURROL").toString());
                                }
                                editor.commit();
                                ((AppContext) getApplicationContext())
                                        .setCustId(results.get("PHONENUMBER").toString());
                                ((AppContext) getApplicationContext())
                                        .setMobile(results.get("PHONENUMBER").toString());
                            }
                            ((AppContext) getApplicationContext())
                                    .setCustPass(passWord);
                            if (results.get("MERCNUM") != null) {
                                editor.putString("MercNum", result.get("MERCNUM").toString());
                                editor.commit();
                                ((AppContext) UserActivity.this.getApplication())
                                        .setMercNum(results.get("MERCNUM").toString());
                            }


                            if (results.get("MERSTS") != null) {
                                ((AppContext) getApplicationContext())
                                        .setMerSts(results.get("MERSTS").toString());

                                Intent intent1 = new Intent(UserActivity.this,
                                        FmMainActivity.class);
                                editor.putString("MERSTS", result.get("MERSTS").toString());
                                editor.putString(Common.C_USERMOBILE,
                                        "");
                                editor.putString(Common.C_USERPWD, "");
                                editor.commit();
                                startActivity(intent1);
                                finish();
                            } else {
                                warnDialog = new OneButtonDialogWarn(UserActivity.this,
                                        R.style.CustomDialog, "提示", result.get(
                                        Entity.RSPMSG).toString(), "确定",
                                        new OnMyDialogClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // TODO Auto-generated method stub

                                                warnDialog.dismiss();
                                            }
                                        });

                                warnDialog.show();
                            }
                        }
                    }


                } else {
                    warnDialog = new OneButtonDialogWarn(UserActivity.this,
                            R.style.CustomDialog, "提示", result.get(
                            Entity.RSPMSG).toString(), "确定",
                            new OnMyDialogClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub

                                    warnDialog.dismiss();
                                }
                            });

                    warnDialog.show();
                }

            } else {
                warnDialog = new OneButtonDialogWarn(UserActivity.this,
                        R.style.CustomDialog, "提示", "请求服务器失败！！！", "确定",
                        new OnMyDialogClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                warnDialog.dismiss();
                            }
                        });
                warnDialog.show();
            }

            // Intent intent = new Intent(UserActivity.this,
            // MenuActivity.class);
            // startActivity(intent);
            // finish();
            super.onPostExecute(result);

        }

    }


    private void initphone(String img) {
        // TODO Auto-generated method stub
        List<String> l = new ArrayList<String>();
        for (int i = 1; i <= 4; i++) {
            String a = MyCacheUtil.getshared(UserActivity.this).getString("suser" + i, "");
            Log.e("", "手机号码 = = = = " + "suser" + i + "  = = =  " + a);
            if (a != null && !a.equals("")) {
                if (a.equals("")) {
                    l.add("-1");
                    Log.e("", "a = = = " + -1);
                } else {
                    l.add(a);
                    Log.e("", "a = = = " + a);
                }


            }

        }
        if (l.contains(userName)) {
            Log.e("", "替换图片" + l.indexOf(userName));
            editor.putString("simg" + (l.indexOf(userName) + 1), img);
            editor.putString("suser1" + (l.indexOf(userName) + 1), userName);
            editor.putString("spwd1" + (l.indexOf(userName) + 1), passWord);
            editor.commit();
        }
        if (l.size() <= 0) {
            editor.putString("suser1", userName);
            editor.putString("spwd1", passWord);
            editor.putString("simg1", img);
            editor.commit();
        } else {
            Log.e("", "还没存  2= = = " + userName);
            for (int i = 1; i <= l.size(); i++) {

                if (l.contains("-1") && !l.contains(userName)) {

                    Log.e("", "存了新未知 = = " + "suser" + l.indexOf("") + 1 + " = = = = " + userName);
                    editor.putString("suser" + (l.indexOf("-1") + 1), userName);
                    editor.putString("spwd" + (l.indexOf("-1") + 1), passWord);
                    editor.putString("simg" + (l.indexOf("-1") + 1), img);
                    editor.commit();
                    break;
                }
                if (l.size() == 4) {

                    if (!l.contains(userName)) {
                        Log.e("", "替换号码 = = " + "suser1" + " = = = = " + userName);
                        editor.putString("suser1", userName);
                        editor.putString("spwd1", passWord);
                        editor.putString("simg1", img);
                        editor.commit();
                        break;
                    }

                }
                if (!l.contains(userName)) {
                    Log.e("", "存了新号码 = = " + "suser" + (l.size() + 1) + " = = = = " + userName);
                    editor.putString("suser" + (l.size() + 1), userName);
                    editor.putString("spwd" + (l.size() + 1), passWord);
                    editor.putString("simg" + (l.size() + 1), img);
                    editor.commit();
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            SpannableString msp = new SpannableString("您确定要退出应用?");
            showDoubleWarnDialog(msp);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void doubleWarnOnClick(View v) {
        // TODO Auto-generated method stub
        super.doubleWarnOnClick(v);
        switch (v.getId()) {
            case R.id.btn_left:

                doubleWarnDialog.dismiss();

                break;
            case R.id.btn_right:
                ((AppContext) getApplication()).setCustId(null); // 商户ID赋为空
                ((AppContext) getApplication()).setPsamId(null);
                ((AppContext) getApplication()).setMacKey(null);
                ((AppContext) getApplication()).setPinKey(null);
                ((AppContext) getApplication()).setMerSts(null);
                ((AppContext) getApplication()).setMobile(null);
                ((AppContext) getApplication()).setEncryPtkey(null);
                ((AppContext) getApplication()).setStatus(null);
                ((AppContext) getApplication()).setCustPass(null);
                ((AppContext) getApplication()).setVersionSerial(null);
                AppContext.getInstance().setStateaudit(null);
                AppManager.getAppManager().AppExit(UserActivity.this);
                doubleWarnDialog.dismiss();
                break;

            default:
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return plist.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return plist.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.user_login_gvitem, null);
                holder = new ViewHolder();
                holder.textview1 = (TextView) convertView.findViewById(R.id.textview1);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView1);
                convertView.setTag(holder);


            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            HashMap<String, Object> map = plist.get(position);
            if (map.get("puser") != null) {
                holder.textview1.setText(map.get("suser").toString());
            }


            return convertView;
        }

        class ViewHolder {
            public TextView textview1;
            public ImageView image;
        }
    }


//	@Override
//	public boolean onLongClick(View v) {
//		// TODO Auto-generated method stub
//		AnimationUtil.BtnSpecialAnmations1(UserActivity.this, lin_users, 500);
//		
//		return false;
//	}
}
