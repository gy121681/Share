package com.shareshenghuo.app.shop.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.FindPaypassActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.request.FilialobeRequest;
import com.shareshenghuo.app.shop.network.request.NumRequest;
import com.shareshenghuo.app.shop.network.request.getUserOrShopRequest;
import com.shareshenghuo.app.shop.network.response.AutResponse;
import com.shareshenghuo.app.shop.network.response.GetUserOrShopResponse;
import com.shareshenghuo.app.shop.network.response.InvestmentConfigResponse;
import com.shareshenghuo.app.shop.network.response.NumResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.MD5Utils;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.widget.dialog.MyEditDialog;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;
import com.shareshenghuo.app.shop.widget.dialog.onMyaddTextListener;

public class TransferActivity2 extends BaseFragment {

	private EditText ed_phone, ed_balance;
	private TextView username, nickname, tv_xinnum, tv_pro;
	private String userType, userOrShopId;
	private boolean istran = false;
	private Button llWalletRecharge;
	private MyEditDialog doubleWarnDialog1;
	private String num = "";
	private TwoButtonDialog downloadDialog;
	private String count = "";
	// _________________新增
	private Spinner spinner;// 转给商家或者消费者的下拉框
	private LinearLayout shangjia_layout;
	private LinearLayout xiaofeizhe_layout;
	private TextView title_xiaoxinzhuanrang;// 轉給誰的提示
	private int cfz_sj = 2;// 1消費者，2商家
	private TextView nickname_xiaofeizhe;// 消費者姓名
	private EditText ed_phone_xiaofeizhe;// 消费者账号

	@Override
	protected int getLayoutId() {
		return R.layout.transfer_fmlayout;
	}

	@Override
	protected void init(View rootView) {
		shangjia_layout = getView(R.id.shangjia_layout);// 商家布局
		xiaofeizhe_layout = getView(R.id.xiaofiezhe_layout);// 消费者布局
		List<String> list = new ArrayList<String>();
		list.add("商家");
		list.add("消费者");
		spinner = getView(R.id.zhuanrang_xx_cyl_spinner);
		// new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
		// list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(0, true);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {// 0是商家。
					xiaofeizhe_layout.setVisibility(View.GONE);// 隐藏消费者
					shangjia_layout.setVisibility(View.VISIBLE);// 显示商家
					cfz_sj = 2;// 設置是商家
				} else {// 1是消費者
					xiaofeizhe_layout.setVisibility(View.VISIBLE);// 显示消费者
					shangjia_layout.setVisibility(View.GONE);// 隐藏商家
					cfz_sj = 1;// 設置是消費者
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		nickname_xiaofeizhe = getView(R.id.nickname_xiaofeizhe);// 消費者姓名
		ed_phone_xiaofeizhe = getView(R.id.ed_phone_xiaofeizhe);// 消费者账号

		ed_phone_xiaofeizhe.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {

				if (s.length() >= 11) {
					initname(ed_phone_xiaofeizhe.getText().toString(), cfz_sj
							+ "");
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
		// -------------------------------------------------

		ed_phone = getView(R.id.ed_phone);
		username = getView(R.id.username);
		nickname = getView(R.id.nickname);
		tv_xinnum = getView(R.id.tv_xinnum);
		ed_balance = getView(R.id.ed_balance);
		tv_pro = getView(R.id.tv_pro);
		llWalletRecharge = getView(R.id.llWalletRecharge);
		init();
		ed_balance.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				if (s.length() > 0) {
					llWalletRecharge.setEnabled(true);
				} else {
					llWalletRecharge.setEnabled(false);
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
		llWalletRecharge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Submit();
			}
		});

		ed_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {

				if (s.length() >= 11) {
					initname(ed_phone.getText().toString(), cfz_sj + "");
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

	}

	private void Submit() {

		// if(TextUtils.isEmpty(union_id)){
		// initDialog("抱歉，当前消费者帐号暂不可进行录单", "确定", "");
		// return;
		// }
		if (cfz_sj == 1) {// 消费者

		} else {// 转给商家
			if (TextUtils.isEmpty(ed_phone.getText())) {
				T.showShort(activity, "请填写要转让的商家帐号");
				return;
			}
		}

		if (!istran) {
			T.showShort(activity, "当前对象不可转让");
			return;
		}

		if (TextUtils.isEmpty(ed_balance.getText())) {
			T.showShort(activity, "请输入转让秀心");
			return;
		}

		if (Double.parseDouble(ed_balance.getText().toString()) < 1) {
			T.showShort(activity, "请输入转让秀心");
			return;
		}
		if (ed_balance.getText() != null
				&& num != null
				&& Double.parseDouble(ed_balance.getText().toString()) > Double
						.parseDouble(num)) {
			T.showShort(activity, "剩余秀心不足");
			return;
		}

		count = ed_balance.getText().toString();
		shwpaypwd(ed_balance.getText().toString());

	}

	private void initname(String string, final String types2) {

		// ProgressDialogUtil.showProgressDlg(activity, "");
		getUserOrShopRequest req = new getUserOrShopRequest();
		req.account = string;
		req.userType = types2;

		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETUSERORSHOPNAME, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ProgressDialogUtil.dismissProgressDlg();
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						ProgressDialogUtil.dismissProgressDlg();
						Log.e("", "" + resp.result.toString());
						GetUserOrShopResponse bean = new Gson().fromJson(
								resp.result, GetUserOrShopResponse.class);
						if (Api.SUCCEED == bean.result_code) {
							if (bean.data.RSPCOD.equals("000000")) {
								if (cfz_sj == 2) {// 如果是商家
									istran = true;
									// tv_name.setVisibility(View.VISIBLE);
									username.setText(bean.data.RSPDATA.shop_name);
									String name = bean.data.RSPDATA.user_shop_name;
									if (name != null && name.length() >= 2) {
										nickname.setText("*"
												+ name.substring(1,
														name.length()));
									} else {
										nickname.setText(name);
									}
									userType = bean.data.RSPDATA.user_type;
									userOrShopId = bean.data.RSPDATA.user_shop_id;
								} else {
									// 消費者
									istran = true;
									String name = bean.data.RSPDATA.user_shop_name;
									if (name != null && name.length() >= 2) {
										nickname_xiaofeizhe.setText("*"
												+ name.substring(1,
														name.length()));
									} else {
										nickname_xiaofeizhe.setText(name);
									}
									userType = bean.data.RSPDATA.user_type;
									userOrShopId = bean.data.RSPDATA.user_shop_id;
								}
							} else if (bean.data.RSPCOD.equals("000002")) {
								if (cfz_sj == 2) {// 如果是商家
									istran = false;
									username.setText("未实名认证,不可进行操作");
									userType = "";
									userOrShopId = "";
									T.showShort(activity, bean.data.RSPMSG);
									username.setText("");
									nickname.setText("");
								} else {
									// 消費者
									istran = false;
									T.showShort(activity, "实名认证未通过");
									userType = "";
									userOrShopId = "";
									nickname_xiaofeizhe.setText("");
								}
							} else {
								if (cfz_sj == 2) {// 如果是商家
									istran = false;
									username.setText(bean.data.RSPMSG);
									T.showShort(activity, bean.data.RSPMSG);
									username.setText("");
									nickname.setText("");
								} else {
									istran = false;
									T.showShort(activity, bean.data.RSPMSG);
									nickname_xiaofeizhe.setText("");

								}
							}
						}
					}
				});
	}

	private void init() {
		// ProgressDialogUtil.showProgressDlg(activity, "");
		FilialobeRequest req = new FilialobeRequest();
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.INVESTMENTCONTROLLER, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// ProgressDialogUtil.dismissProgressDlg();
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						// ProgressDialogUtil.dismissProgressDlg();
						InvestmentConfigResponse bean = new Gson().fromJson(
								resp.result, InvestmentConfigResponse.class);
						if (Api.SUCCEED == bean.result_code) {
							if (bean.data.investmentIsOpen.equals("0")) {
								initDialog1("抱歉,该功能暂停使用", " 确定", "");
								return;
							}
							initnum();
							// initname(UserInfoManager.getUserInfo(activity).mobile,
							// "1");
							tv_pro.setText(bean.data.investmentExplain);
						}
					}
				});
	}

	private void initnum() {

		NumRequest req = new NumRequest();
		req.userId = UserInfoManager.getUserInfo(activity).shop_id + "";
		req.userType = "2";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETBUNBER, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						T.showNetworkError(activity);
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						NumResponse bean = new Gson().fromJson(resp.result,
								NumResponse.class);
						Log.e("", " = = = = = =" + resp.result);
						if (Api.SUCCEED == bean.result_code) {
							tv_xinnum.setText(bean.data.shareFilialPiety + " ");
							num = bean.data.shareFilialPiety;
						}
					}
				});
	}

	private void initDialog1(String content, String left, String right) {

		downloadDialog = new TwoButtonDialog(activity, R.style.CustomDialog,
				"", content, left, right, true, new OnMyDialogClickListener() {
					@Override
					public void onClick(View v) {

						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							activity.finish();
							break;
						case R.id.Button_cancel:
							downloadDialog.dismiss();
							activity.finish();
						default:
							break;
						}
					}
				});
		downloadDialog.show();
		downloadDialog.setCancelable(false);
	}

	protected void shwpaypwd(String num) {
		doubleWarnDialog1 = new MyEditDialog(activity, R.style.loading_dialog,
				"兑换", "请输入支付密码", "确认", "取消", num,
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {

						switch (v.getId()) {
						case R.id.btn_right:
							doubleWarnDialog1.dismiss();
							InputMethodManager m = (InputMethodManager) activity
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							m.toggleSoftInput(0,
									InputMethodManager.HIDE_NOT_ALWAYS);
							break;
						case R.id.btn_left:
							String paypwd = doubleWarnDialog1.getpaypwd();

							if (paypwd == null || paypwd.equals("")) {
								Toast.makeText(activity, "请输入支付密码",
										Toast.LENGTH_SHORT).show();
								return;
							}
							if (paypwd.length() < 6 || paypwd.length() > 15) {
								return;
							}
							break;
						default:
							break;
						}
					}

				}, new onMyaddTextListener() {

					@Override
					public void refreshActivity(String paypwd) {

						if (paypwd == null || paypwd.equals("")) {
							Toast.makeText(activity, "请输入支付密码",
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (paypwd.length() < 6 || paypwd.length() > 15) {
							return;
						}

						Transfer(paypwd);

					}
				});
		doubleWarnDialog1.setCancelable(false);
		doubleWarnDialog1.setCanceledOnTouchOutside(false);
		doubleWarnDialog1.show();

	}

	private void Transfer(String paypwd) {

		ProgressDialogUtil.showProgressDlg(activity, "");
		getUserOrShopRequest req = new getUserOrShopRequest();
		req.investmentUserType = "2";
		req.investmentUserId = UserInfoManager.getUserInfo(activity).shop_id
				+ "";
		req.investmentAccount = UserInfoManager.getUserInfo(activity).id + "";
		req.filialPietyNum = ed_balance.getText().toString();
		// if(isinvestmentTypes){
		req.investmentType = "1";
		// }else{
		// req.investmentType = "1";
		// }
		req.userOrShopId = userOrShopId;
		req.investmentProjectId = "";
		req.userType = userType;

		for (int i = 0; i < 3; i++)
			paypwd = MD5Utils.getMD5String(paypwd);
		req.payPassword = paypwd;
		Log.e("", "" + req.toString());
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GENERATECONTROLLER, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ProgressDialogUtil.dismissProgressDlg();
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						ProgressDialogUtil.dismissProgressDlg();
						Log.e("", "" + resp.result.toString());
						AutResponse bean = new Gson().fromJson(resp.result,
								AutResponse.class);
						if (Api.SUCCEED == bean.result_code) {
							if (bean.data.RSPCOD.equals("000000")) {
								// initDialog1(bean.data.RSPMSG, "确定", "");
								T.showShort(activity, bean.data.RSPMSG);
								initnum();
								// initname(UserInfoManager.getUserInfo(activity).mobile,
								// "1");
								if (doubleWarnDialog1 != null) {
									doubleWarnDialog1.dismiss();
								}
								activity.finish();
							} else {
								// T.showShort(activity, bean.data.RSPMSG);
								initDialog2(bean.data.RSPMSG, "忘记密码", "重试");
							}
						} else {
							// T.showShort(activity, bean.result_desc);
							initDialog2(bean.data.RSPMSG, "忘记密码", "重试");
						}
					}
				});
	}

	private void initDialog2(String content, String left, String right) {
		downloadDialog = new TwoButtonDialog(activity, R.style.CustomDialog,
				"", content, left, right, true, new OnMyDialogClickListener() {
					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.Button_OK:
							if (doubleWarnDialog1 != null) {
								doubleWarnDialog1.dismiss();
							}
							downloadDialog.dismiss();
							//
							Intent intent = new Intent(activity,
									FindPaypassActivity.class);
							intent.putExtra("tag", "1");
							startActivity(intent);
							break;

						case R.id.Button_cancel:
							if (doubleWarnDialog1 != null) {
								doubleWarnDialog1.dismiss();
							}
							downloadDialog.dismiss();
							shwpaypwd(count);
							break;
						}
					}
				});
		downloadDialog.show();
		downloadDialog.setCancelable(false);
	}

}
