package com.shareshenghuo.app.shop.function.myfhq;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.myfhq.xiangmu_main;
import com.shareshenghuo.app.shop.network.request.NumRequest;
import com.shareshenghuo.app.shop.network.request.getUserOrShopRequest;
import com.shareshenghuo.app.shop.network.response.AutResponse;
import com.shareshenghuo.app.shop.network.response.NumResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.MD5Utils;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.Util;
import com.shareshenghuo.app.shop.widget.dialog.MyEditDialog;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;
import com.shareshenghuo.app.shop.widget.dialog.onMyaddTextListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.BaseTopActivity;
import com.shareshenghuo.app.shop.R;

/**
 * 
 * @author wyp
 * @version 创建时间：2017-5-25 上午9:48:06 类说明：项目投资.购买界面
 */
public class MyFhqXmtz_xfMainActivity extends BaseTopActivity implements
		OnClickListener {
	private Spinner spinner;// 产业链和公益的投资
	private String title;
	private TextView tv_xinnum_cyl, tv_xinnum_gy;// 产业链公益数量文本框

	private String cyl_xxnum, gy_xxnum;// 产业链秀点数量和公益秀点数量
	private Spinner cyl_project_name, gy_project_name;// 产业链和公益的项目名称

	private LinearLayout cyl_layout, gy_layout;// 产业链和公益 布局
	private TextView tv_pro_cyl, tv_pro_gy;// 产业链和公益 。投资说明

	private Button cyl_buy_btn, gy_buy_btn;// 产业链秀点购买按钮,公益秀点购买按钮

	private EditText cyl_buy_num, gy_buy_num;// 产业链购买数量,公益购买数量.编辑框

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.myfhq_xmtz_xf_layout);
		loginui();
		loaddata();
	}

	private void loaddata() {
		// init_cyl_num();
		initnum();// 获取可投资的产业链数量和公益数量
		init_xx_list();// 获取项目列表
	}

	// 初始化UI
	void loginui() {
		cyl_layout = getView(R.id.cyl_layout);// 产业链界面
		gy_layout = getView(R.id.gy_layout);// 公益界面

		title = getIntent().getStringExtra("title");
		initTopBar(title);

		tv_xinnum_cyl = getView(R.id.tv_xinnum_cyl);// 产业链秀点数字
		tv_xinnum_gy = getView(R.id.tv_xinnum_gy);// 公益秀点数字

		tv_pro_cyl = getView(R.id.tv_pro_cyl);// 投资说明，产业链
		tv_pro_gy = getView(R.id.tv_pro_gy);// 投资说明，公益

		// 暂时只写一个
		cyl_project_name = getView(R.id.cyl_project_name);// 产业链项目选择/
		gy_project_name = getView(R.id.gy_project_name);// 公益项目选择

		cyl_buy_btn = getView(R.id.cyl_buy_btn);// 产业链秀点购买按钮
		gy_buy_btn = getView(R.id.gy_buy_btn);// 公益秀点购买按钮

		cyl_buy_num = getView(R.id.cyl_buy_num);// 产业链购买数量编辑框
		cyl_buy_num.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_FLAG_DECIMAL);
		gy_buy_num = getView(R.id.gy_buy_num);// 公益购买数量编辑框
		gy_buy_num.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_FLAG_DECIMAL);

		cyl_buy_btn.setOnClickListener(this);
		gy_buy_btn.setOnClickListener(this);

		spinner = getView(R.id.xx_spinner);
		List<String> list = new ArrayList<String>();
		list.add("秀儿产业链");
		list.add("秀儿消费");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(1, true);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {// 产业链
					cyl_layout.setVisibility(View.VISIBLE);// 显示产业链
					gy_layout.setVisibility(View.GONE);// 隐藏公益
				} else {
					cyl_layout.setVisibility(View.GONE);// 隐藏产业链
					gy_layout.setVisibility(View.VISIBLE);// 显示公益
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	/**
	 * 获取产业链秀点和公益秀点数量
	 */
	private void initnum() {
		NumRequest req = new NumRequest();
		req.userId = UserInfoManager.getUserInfo(MyFhqXmtz_xfMainActivity.this).shop_id
				+ "";
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
						T.showNetworkError(MyFhqXmtz_xfMainActivity.this);
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						NumResponse bean = new Gson().fromJson(resp.result,
								NumResponse.class);
						Log.e("", " = = = = = =" + resp.result);
						if (Api.SUCCEED == bean.result_code) {
							// tv_xinnum.setText(bean.data.shareFilialPietyNew
							// + " ");

							cyl_xxnum = Util.getfotmatnum(
									bean.data.filialMoneyNew, true, 1);
							// 保留两位小数
							gy_xxnum = Util.getfotmatnum(bean.data.filialMoney,
									true, 1);

							tv_xinnum_cyl.setText(cyl_xxnum + "");// 给产业链数量赋值
							tv_xinnum_gy.setText(gy_xxnum + "");// 给公益数量赋值

							System.out.println("=====产业链秀点数量:" + cyl_xxnum
									+ ",公益秀点数量:" + gy_xxnum);
						}
					}
				});
	}

	double getDouble(double a, int b) {// 保留两个小数
		int x = 0;
		int y = 1;
		for (int i = 0; i < b; i++) {
			y = y * 10;
		}
		x = (int) (a * y);
		return (double) x / y;
	}

	private int cyl_xmid = 0;// 产业链项目id
	private int gy_xmid = 0;// 公益项目id

	/**
	 * 获取产业链和公益项目
	 */
	private void init_xx_list() {
		// 记得把Api.test_utl 改掉
		new HttpUtils().send(HttpMethod.GET, Api.test_utl + Api.GETXFRGXMLIST,
				null, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						T.showNetworkError(MyFhqXmtz_xfMainActivity.this);
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						System.out.println("====项目列表：" + resp.result);
						try {
							JSONObject j = new JSONObject(resp.result);
							JSONObject j2 = j.getJSONObject("data");
							String tzsm = j2
									.getString("userMoneyInvestmentExplan");// 投资说明
							tv_pro_cyl.setText(tzsm);// 投资说明，产业链
							tv_pro_gy.setText(tzsm);// 投资说明，公益

							JSONArray jsonArray = j2
									.getJSONArray("userMoneyInvestmentsList");
							Gson gson = new Gson();
							String result = jsonArray.toString();

							ArrayList<xiangmu_main> list = new ArrayList<xiangmu_main>();// 项目列表
							list = gson.fromJson(result,
									new TypeToken<List<xiangmu_main>>() {
									}.getType());
							xiangmu_main xm = list.get(0);
							System.out.println("====项目文字："
									+ xm.getProject_name());
							ArrayAdapter<xiangmu_main> adapter_cyl = new ArrayAdapter<xiangmu_main>(
									MyFhqXmtz_xfMainActivity.this,
									android.R.layout.simple_spinner_item, list);
							adapter_cyl
									.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							cyl_project_name.setAdapter(adapter_cyl);
							gy_project_name.setAdapter(adapter_cyl);
							final ArrayList<xiangmu_main> temp_list = list;// 临时创建一个项目列表
							cyl_project_name
									.setOnItemSelectedListener(new OnItemSelectedListener() {// 产业链项目选择

										@Override
										public void onItemSelected(
												AdapterView<?> parent,
												View view, int position, long id) {

											cyl_xmid = Integer
													.valueOf(temp_list.get(
															position).getId());
											System.out.println("===选择的项目id:"
													+ cyl_xmid);
										}

										@Override
										public void onNothingSelected(
												AdapterView<?> parent) {

										}
									});
							gy_project_name
									.setOnItemSelectedListener(new OnItemSelectedListener() {

										@Override
										public void onItemSelected(
												AdapterView<?> parent,
												View view, int position, long id) {
											gy_xmid = Integer.valueOf(temp_list
													.get(position).getId());
										}

										@Override
										public void onNothingSelected(
												AdapterView<?> parent) {

										}
									});

							// cyl_project_name.setText(xm.getProject_name());//产业链项目文字
							// gy_project_name.setText(xm.getProject_name());//公益项目文字

						} catch (JSONException e) {
							e.printStackTrace();
						}

						/**
						 * NumResponse bean = new Gson().fromJson(resp.result,
						 * NumResponse.class); Log.e("", " = = = = = =" +
						 * resp.result); if (Api.SUCCEED == bean.result_code) {
						 * // tv_xinnum.setText(bean.data.shareFilialPietyNew //
						 * + " "); cyl_xxnum = Integer
						 * .valueOf(bean.data.shareFilialPietyNew); gy_xxnum =
						 * Integer .valueOf(bean.data.shareFilialPiety);
						 * 
						 * tv_xinnum_cyl.setText(cyl_xxnum + "");// 给产业链数量赋值
						 * tv_xinnum_gy.setText(gy_xxnum + "");// 给公益数量赋值
						 * 
						 * System.out.println("=====产业链秀点数量:" + cyl_xxnum +
						 * ",公益秀点数量:" + gy_xxnum); // num =
						 * bean.data.shareFilialPiety; }
						 */
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cyl_buy_btn:// 产业链购买事件
			if (isnotnull(cyl_buy_num.getText().toString())) {
				if (cyl_buy_num.getText().toString().contains(".")) {// 如有小数点
					String a = cyl_buy_num.getText().toString();
					String temp = a.substring(a.indexOf(".") + 1, a.length());
					System.out.println("秀点投资的购买数量:" + temp);
					if (temp.length() > 2) {
						T.showShort(MyFhqXmtz_xfMainActivity.this, "小数点后只能输入两位");
						return;
					} else {
						String buy_cyl_num_ = cyl_buy_num.getText().toString();
						double d = Double.valueOf(buy_cyl_num_) * 100;
						String buy_cyl_num = String.valueOf(d).substring(0,
								String.valueOf(d).lastIndexOf("."));
						shwpaypwd(buy_cyl_num, "cyl", buy_cyl_num_);
					}
				} else {// 不带小数点,
					String buy_cyl_num = cyl_buy_num.getText().toString();
					long buy = Integer.valueOf(buy_cyl_num) * 100;
					shwpaypwd(String.valueOf(buy), "cyl", buy_cyl_num);
				}
			}
			break;
		case R.id.gy_buy_btn:// 公益购买事件
			if (isnotnull(gy_buy_num.getText().toString())) {
				// String buy_gy_num = gy_buy_num.getText().toString();
				// // try {
				// // BigDecimal a = new BigDecimal(buy_gy_num);
				// // BigDecimal b = BigDecimal.valueOf(100);
				// // buy_gy_num = String.valueOf(a.multiply(b).doubleValue());
				// // } catch (Exception e) {
				// // T.showShort(MyFhqXmtz_xfMainActivity.this, "输入错误,请重新填写");
				// // return;
				// // }
				// buy_gy_num = String.valueOf(Integer.valueOf(buy_gy_num) *
				// 100);
				// System.out.println("====项目id是:" + gy_xmid);
				// shwpaypwd(buy_gy_num, "gy");
				if (gy_buy_num.getText().toString().contains(".")) {// 如有小数点
					String a = gy_buy_num.getText().toString();
					String temp = a.substring(a.indexOf(".") + 1, a.length());
					System.out.println("秀点投资的购买数量:" + temp);
					if (temp.length() > 2) {
						T.showShort(MyFhqXmtz_xfMainActivity.this, "小数点后只能输入两位");
						return;
					} else {
						String buy_cyl_num_ = gy_buy_num.getText().toString();
						double d = Double.valueOf(buy_cyl_num_) * 100;
						String buy_cyl_num = String.valueOf(d).substring(0,
								String.valueOf(d).lastIndexOf("."));
						shwpaypwd(buy_cyl_num, "gy", buy_cyl_num_);
					}
				} else {// 不带小数点,
					String buy_cyl_num = gy_buy_num.getText().toString();
					long buy = Integer.valueOf(buy_cyl_num) * 100;
					shwpaypwd(String.valueOf(buy), "gy", buy_cyl_num);
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 判断购买的数量是否为空.或者小于1
	 * 
	 * @param str
	 * @return
	 */
	private boolean isnotnull(String str) {
		boolean b = false;
		try {
			if (Double.valueOf(str) >= 0) {
				b = true;
			}
		} catch (Exception e) {

		}
		if (!b) {
			T.showShort(MyFhqXmtz_xfMainActivity.this, "请确定购买数量是否正确!");
		}
		return b;
	}

	private MyEditDialog doubleWarnDialog1;

	protected void shwpaypwd(final String buynum, final String menuTypeStr,
			String buy_name) {
		MyEditDialog.teshu = "my_buy";// 使用前,暂时特殊标识一下
		doubleWarnDialog1 = new MyEditDialog(MyFhqXmtz_xfMainActivity.this,
				R.style.loading_dialog, "投资", "请输入支付密码", "确认", "取消", buy_name,
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {

						switch (v.getId()) {
						case R.id.btn_right:
							doubleWarnDialog1.dismiss();
							InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							m.toggleSoftInput(0,
									InputMethodManager.HIDE_NOT_ALWAYS);
							break;
						case R.id.btn_left:
							String paypwd = doubleWarnDialog1.getpaypwd();

							if (paypwd == null || paypwd.equals("")) {
								Toast.makeText(getApplicationContext(),
										"请输入支付密码", Toast.LENGTH_SHORT).show();
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
							Toast.makeText(getApplicationContext(), "请输入支付密码",
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (paypwd.length() < 6 || paypwd.length() > 15) {
							return;
						}

						buy_cyl(paypwd, buynum, menuTypeStr);

					}
				});
		doubleWarnDialog1.setCancelable(false);
		doubleWarnDialog1.setCanceledOnTouchOutside(false);
		doubleWarnDialog1.show();

	}

	void toxxMainActivity() {// 跳到主界面
		Intent i = new Intent();// 秀心项目
		i.setClass(MyFhqXmtz_xfMainActivity.this, MyFhqMainActivity.class);
		i.putExtra("title", "秀心项目投资");
		startActivity(i);
	}

	/**
	 * 购买产业链秀点 密码,购买数量,购买产业链还是公益."cyl""gy"
	 */
	void buy_cyl(String paypwd, String buynum, String menuTypeStr) {

		getUserOrShopRequest req = new getUserOrShopRequest();
		req.investmentUserType = "2";// 用户类型
		req.investmentUserId = UserInfoManager.getUserInfo(this).shop_id + "";
		req.filialPietyNum = buynum;// 购买产业链秀点数量
		req.investmentProjectId = String.valueOf(cyl_xmid);// 入股项目id
		System.out.println("=====购买的秀点是:" + menuTypeStr);
		if (menuTypeStr.equals("cyl")) {// 产业链
			req.channelType = "2";
		} else {// 公益
			req.channelType = "1";
		}
		System.out.println("======购买的秀点channelType:" + req.channelType);
		for (int i = 0; i < 3; i++)
			paypwd = MD5Utils.getMD5String(paypwd);
		req.payPassword = paypwd;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// T.showShort(MyFhqXmtz_xfMainActivity.this, "正在处理中,请稍后...");
		try {
			doubleWarnDialog1.dismiss();
		} catch (Exception e) {
		}

		new HttpUtils().send(HttpMethod.POST, Api.test_utl + Api.BUYXF, params,
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
							initDialog1(bean.data.RSPMSG, "确定", "");
							if (doubleWarnDialog1 != null) {
								doubleWarnDialog1.dismiss();
							}
							//finish();
							// if(bean.data.RSPCOD.equals("000000")){
							// }else{
							// T.showShort(getApplicationContext(),
							// bean.data.RSPMSG);
							// }
						}
						System.out.println("======购买返回的数据:"
								+ resp.result.toString());
					}
				});
	}

	private TwoButtonDialog downloadDialog;

	private void initDialog1(String content, String left, String right) {
		downloadDialog = new TwoButtonDialog(this, R.style.CustomDialog, "",
				content, left, right, true, new OnMyDialogClickListener() {
					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							finish();
							toxxMainActivity();
							break;
						case R.id.Button_cancel:
							downloadDialog.dismiss();
							finish();
							toxxMainActivity();
						default:
							break;
						}
					}
				});
		downloadDialog.show();
		downloadDialog.setCancelable(false);
	}

	/**
	 * 购买公益秀点
	 */
	void buy_gy() {

	}
}
