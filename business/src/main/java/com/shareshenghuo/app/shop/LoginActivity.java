package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.jpush.android.api.JPushInterface;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.function.mallSettled.MallStatus;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.request.LoginRequest;
import com.shareshenghuo.app.shop.network.response.LoginResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.MD5Utils;
import com.shareshenghuo.app.shop.util.MallStringUtils;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.PwdUtils;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;

public class LoginActivity extends BaseTopActivity implements OnClickListener {

	private EditText edAccount;
	private EditText edPwd;
	private Button btnLogin;
	private ImageView del_img;
	private LinearLayout llTopBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!getIntent().getBooleanExtra("logout", false))
			startActivity(new Intent(this, LoadingActivity.class));

		setContentView(R.layout.activity_login);
		initView();

		if (UserInfoManager.isLogin(this))
			login(UserInfoManager.getUserName(this),
					UserInfoManager.getUserPwd(this));
	}

	public void initView() {
		initTopBar("秀儿商户");
		llTopBack = getView(R.id.llTopBack);
		llTopBack.setVisibility(View.GONE);
		edAccount = (EditText) findViewById(R.id.edLoginAccount);
		edPwd = (EditText) findViewById(R.id.edLoginPwd);
		btnLogin = (Button) findViewById(R.id.btnLogin);

		if (edAccount != null && UserInfoManager.getUserName(this) != null) {
			edAccount.setText(UserInfoManager.getUserName(this));
		}
		if (btnLogin != null) {
			btnLogin.setOnClickListener(this);
		}

		findViewById(R.id.tvRetrievePwd).setOnClickListener(this);

		del_img = (ImageView) findViewById(R.id.del_img);
		edAccount.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				if (s.length() > 0) {
					del_img.setVisibility(View.VISIBLE);
				} else {
					del_img.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		del_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				edAccount.setText("");
				del_img.setVisibility(View.GONE);
			}
		});

		edAccount.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (edAccount.getText().length() > 0) {
					del_img.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			if (ViewUtil.checkEditEmpty(edAccount, "请填写手机号"))
				return;
			if (ViewUtil.checkEditEmpty(edPwd, "请填写密码"))
				return;

			String pwd = edPwd.getText().toString();
			for (int i = 0; i < 3; i++)
				pwd = MD5Utils.getMD5String(pwd);
			login(edAccount.getText().toString(), pwd);
			break;

		case R.id.tvRetrievePwd:
			startActivity(new Intent(this, RetrievePwdActivity.class));
			break;
		}
	}

	public void login(final String account, final String pwd) {

		ProgressDialogUtil.showProgressDlg(this, "登录中");
		btnLogin.setEnabled(false);

		final LoginRequest req = new LoginRequest();
		req.account = account;
		req.password = pwd;
		req.registration_id = JPushInterface.getRegistrationID(this);
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		new HttpUtils().send(HttpMethod.POST, Api.URL_LOGIN, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						ProgressDialogUtil.dismissProgressDlg();
						btnLogin.setEnabled(true);
						if (resp.statusCode == 200 && resp.result != null) {
							final LoginResponse bean = new Gson().fromJson(
									resp.result, LoginResponse.class);
							System.out.println("===登录返回的值:" + resp.result);
							if (Api.SUCCEED == bean.result_code) {
								// T.showShort(LoginActivity.this,
								// bean.result_desc);
								UserInfoManager.setUserPwd(LoginActivity.this,
										pwd);
								UserInfoManager.setUserName(LoginActivity.this,
										account);

								// 保存商家登录信息
								System.out.println("===登录返回的值:"
										+ bean.data.mall_login_name + ",appid:"
										+ bean.data.app_id);

								UserInfoManager.setMallLoginName(
										LoginActivity.this,
										bean.data.mall_login_name.toString());// 保存商家登录账号
								UserInfoManager.setMallLoginPassword(
										LoginActivity.this,
										bean.data.mall_login_password);// 保存密码
								UserInfoManager.setMallUrl(LoginActivity.this,
										bean.data.mall_url.toString());// 保存商家地址
								UserInfoManager.setMallShopId(
										LoginActivity.this,
										bean.data.mall_shop_id.toString());// 保存商家店铺id
								UserInfoManager.setAppId(LoginActivity.this,
										bean.data.app_id.toString());// 保存appid
								UserInfoManager.setAccount(LoginActivity.this,
										bean.data.account);// 保存账号

								

								/**RequestParams params = new RequestParams();
								params.addBodyParameter("appid",
										bean.data.app_id.toString());// appid
								params.addBodyParameter("account",
										bean.data.mall_shop_id);// 用户标识
								params.addBodyParameter("username",
										bean.data.mall_login_name.toString());// 商家登录的账户
								params.addBodyParameter("password",
										bean.data.mall_login_password);// 商家登录的密码
								// http://119.23.146.39/mobile/supplier/privilege.php
								// 登录之后,拿到商户信息,进行商家登录
								new HttpUtils()
										.send(HttpMethod.POST,
												"http://119.23.146.39/mobile/supplier/privilege.php",
												params,
												new RequestCallBack<String>() {

													@Override
													public void onFailure(
															HttpException arg0,
															String arg1) {
														MallStringUtils.mallState = -2;// 请求失败后,设置为异常状态
													}

													@Override
													public void onSuccess(
															ResponseInfo<String> arg0) {
														System.out
																.println("======登录商家返回:"
																		+ arg0.result);
														if (arg0.statusCode == 200
																&& arg0.result != null) {
															MallStatus ms = new Gson()
																	.fromJson(
																			arg0.result,
																			MallStatus.class);
															MallStringUtils.mallState = ms
																	.getStatus();
															T.showShort(
																	LoginActivity.this,
																	"登录成功!"
																			+ bean.data.mall_url
																			+ ","
																			+ ms.toString());
															System.out
																	.println("======登录商家返回:"
																			+ MallStringUtils.mallState);
														}
													}
												});*/

								if (TextUtils.isEmpty(bean.data.band_mobile)) {
									// 绑定手机号
									Intent it = new Intent(LoginActivity.this,
											BindMobileActivity.class);
									it.putExtra("userInfo", bean.data);
									startActivity(it);
								} else {
									// 登录环信
									try {
										ProgressDialogUtil.showProgressDlg(
												LoginActivity.this, "登录中");
									} catch (Exception e) {
										// TODO: handle exception
									}

									EMChatManager.getInstance().login(
											"s" + bean.data.shop_id, "123456",
											new EMCallBack() {

												@Override
												public void onSuccess() {
													ProgressDialogUtil
															.dismissProgressDlg();
													Log.e("", "环信login succeed");
													EMChatManager
															.getInstance()
															.updateCurrentUserNick(
																	bean.data.nick_name);
													UserInfoManager
															.saveUserInfo(
																	LoginActivity.this,
																	bean.data);
													startActivity(new Intent(
															LoginActivity.this,
															MainActivity.class));
													finish();
													if (LoadingActivity.instance != null)
														LoadingActivity.instance
																.finish();
												}

												@Override
												public void onProgress(
														int arg0, String arg1) {
													ProgressDialogUtil
															.dismissProgressDlg();
													Log.e("", arg1);
												}

												@Override
												public void onError(int arg0,
														String arg1) {
													ProgressDialogUtil
															.dismissProgressDlg();
													Log.e("", "环信login error "
															+ arg1);
													EMChatManager
															.getInstance()
															.updateCurrentUserNick(
																	bean.data.nick_name);
													UserInfoManager
															.saveUserInfo(
																	LoginActivity.this,
																	bean.data);
													startActivity(new Intent(
															LoginActivity.this,
															MainActivity.class));
													finish();
													if (LoadingActivity.instance != null)
														LoadingActivity.instance
																.finish();
													//
													// UserInfoManager.saveUserInfo(LoginActivity.this,
													// bean.data);
													// startActivity(new
													// Intent(LoginActivity.this,
													// MainActivity.class));
													// finish();
													// if(LoadingActivity.instance
													// != null)
													// LoadingActivity.instance.finish();
												}
											});
								}
							} else {

								T.showShort(LoginActivity.this,
										bean.result_desc);
							}
						}
					}

					@Override
					public void onFailure(HttpException e, String msg) {
						ProgressDialogUtil.dismissProgressDlg();
						btnLogin.setEnabled(true);
						T.showNetworkError(LoginActivity.this);
					}
				});
	}
}
