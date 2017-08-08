package com.shareshenghuo.app.shop.fragment;

import java.io.File;
import java.io.UnsupportedEncodingException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.BankCardListActivity;
import com.shareshenghuo.app.shop.ExchangeFmAcyivity;
import com.shareshenghuo.app.shop.ExcitationMainActivity;
import com.shareshenghuo.app.shop.FeedbackActivity;
import com.shareshenghuo.app.shop.FindPaypassActivity;
import com.shareshenghuo.app.shop.IntegralFragmentActivity;
import com.shareshenghuo.app.shop.LoginActivity;
import com.shareshenghuo.app.shop.MentionAmountActivity;
import com.shareshenghuo.app.shop.MerchantInfoActivity;
import com.shareshenghuo.app.shop.MessageActivity;
import com.shareshenghuo.app.shop.MineActivity;
import com.shareshenghuo.app.shop.QrCodeActivityFm;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.RealnameActivity;
import com.shareshenghuo.app.shop.RecommendActivity;
import com.shareshenghuo.app.shop.RecommendedmemberActivity;
import com.shareshenghuo.app.shop.RecordedSingleFmActivity;
import com.shareshenghuo.app.shop.ShuntMainFMActivity;
import com.shareshenghuo.app.shop.function.MallSettledActivity;
import com.shareshenghuo.app.shop.function.MallSettled_kt_Activity;
import com.shareshenghuo.app.shop.function.MallSettled_sh_Activity;
import com.shareshenghuo.app.shop.function.mallSettled.MallStatus;
import com.shareshenghuo.app.shop.function.myfhq.MyFhqMainActivity;
import com.shareshenghuo.app.shop.manager.ImageLoadManager;
import com.shareshenghuo.app.shop.manager.MessageManager;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.UserInfo;
import com.shareshenghuo.app.shop.network.bean.WebLoadActivity;
import com.shareshenghuo.app.shop.network.request.AdminRequest;
import com.shareshenghuo.app.shop.network.request.SysMsgRequest;
import com.shareshenghuo.app.shop.network.request.UpdateShopRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.network.response.FeedBackMsgResponse;
import com.shareshenghuo.app.shop.network.response.FileUploadResponse;
import com.shareshenghuo.app.shop.network.response.LoginResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker.NewMessageCallback;
import com.shareshenghuo.app.shop.util.AnimationUtil;
import com.shareshenghuo.app.shop.util.BitmapTool;
import com.shareshenghuo.app.shop.util.FileUtil;
import com.shareshenghuo.app.shop.util.MallStringUtils;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.widget.CircleImageView;
import com.shareshenghuo.app.shop.widget.dialog.PickPhotoWindow;

/**
 * 
 * @author Lenovo 我的界面
 */
public class MineFragment extends BaseFragment implements NewMessageCallback,
		OnClickListener {

	private ImageView ivIM;
	private CircleImageView civAvatar;
	private TextView tvNickname, tv_paypwd;
	private TextView tvMobile;
	private NewChatMsgWorker newMsgWatcher;
	private ImageView c_service;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_mine;
	}

	@Override
	protected void init(View rootView) {
		ivIM = getView(R.id.ivHomeIM);
		civAvatar = getView(R.id.civAvtar);
		tvNickname = getView(R.id.tvNickname);
		tvMobile = getView(R.id.tvMobile);

		c_service = (ImageView) rootView.findViewById(R.id.c_service);
		getView(R.id.llremember).setOnClickListener(this);
		getView(R.id.llModifyAvatar).setOnClickListener(this);
		getView(R.id.btnLogout).setOnClickListener(this);
		getView(R.id.llModifyNick).setOnClickListener(this);
		getView(R.id.llBindMobile).setOnClickListener(this);
		getView(R.id.llModifyPwd).setOnClickListener(this);
		getView(R.id.llSetting).setOnClickListener(this);
		getView(R.id.llAboutUs).setOnClickListener(this);
		getView(R.id.llContactUs).setOnClickListener(this);
		getView(R.id.llintegral).setOnClickListener(this);
		getView(R.id.civAvtar).setOnClickListener(this);
		getView(R.id.llMentionamount).setOnClickListener(this);
		getView(R.id.llMinehelp).setOnClickListener(this);
		getView(R.id.llMineFeedback).setOnClickListener(this);
		getView(R.id.llrecordedsingle).setOnClickListener(this);
		getView(R.id.llMinebankcard).setOnClickListener(this);
		getView(R.id.llModifySCRZ).setOnClickListener(this);// 商家入驻
		getView(R.id.my_menu_fhq).setOnClickListener(this);// 我的分红权，监听事件

		ivIM.setOnClickListener(this);

		newMsgWatcher = new NewChatMsgWorker(activity, this);
		newMsgWatcher.startWork();

		AnimationUtil.BtnSpecialAnmations1(activity, c_service, 800, 5000);
		c_service.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent about = new Intent(activity, WebLoadActivity.class);
				about.putExtra("title", "在线客服");
				about.putExtra("url", Api.CUSTOMERSERVICE);
				startActivity(about);
			}
		});
		updateView();
	}

	@Override
	public void onStart() {
		super.onStart();
		loadData();
		initmsg();
	}

	private void initmsg() {
		SysMsgRequest req = new SysMsgRequest();
		try {
			req.user_id = UserInfoManager.getUserInfo(activity).shop_id;
		} catch (Exception e) {
		}

		req.user_type = "2";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.NOTICEANDFEEDCOUNTCONTROLLER,
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						Log.e("", "" + resp.result);
						FeedBackMsgResponse bean = new Gson().fromJson(
								resp.result, FeedBackMsgResponse.class);
						if (Api.SUCCEED == bean.result_code) {
							int count = Integer
									.parseInt(bean.data.feedBackMsgNum);
							int count1 = Integer
									.parseInt(bean.data.systemMsgNum);
							if (count > 0 || count1 > 0) {
								ivIM.setImageResource(R.drawable.share_b_mine_service2);
							} else if (MessageManager.getUnreadCount() > 0) {
								ivIM.setImageResource(R.drawable.share_b_mine_service2);
							} else {
								ivIM.setImageResource(R.drawable.share_b_mine_service3);
							}
						}
					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();
		ivIM.setBackgroundResource(MessageManager.getUnreadCount() > 0 ? R.drawable.share_b_mine_service2
				: R.drawable.share_b_mine_service3);
	}

	public void loadData() {
		AdminRequest req = new AdminRequest();
		req.admin_id = UserInfoManager.getUserId(activity) + "";
		RequestParams params = new RequestParams();

		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.e("", "" + Api.URL_SHOP_DETAIL);
		new HttpUtils().send(HttpMethod.POST, Api.URL_SHOP_DETAIL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						LoginResponse bean = new Gson().fromJson(resp.result,
								LoginResponse.class);
						if (Api.SUCCEED == bean.result_code) {
							UserInfoManager.saveUserInfo(activity, bean.data);
							updateView();
						}
					}
				});
	}

	public void updateView() {
		UserInfo user = UserInfoManager.getUserInfo(activity);
		ImageLoadManager.getInstance(activity).displayHeadIconImage(user.user_photo,
				civAvatar);

		if (!TextUtils.isEmpty(user.nick_name)) {
			tvNickname.setText(user.nick_name);
		} else {
			tvNickname.setText("未设置昵称");
		}
		if (!TextUtils.isEmpty(user.band_mobile)) {
			tvMobile.setText(user.band_mobile);
		} else {
			tvMobile.setText("未知");
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		// if(!UserInfoManager.isLogin(activity) ) {
		// T.showShort(getActivity().getApplicationContext(), "您当前未登录，请先登录");
		// startActivity(new Intent(getActivity(), LoginActivity.class));
		// return;
		// }
		switch (v.getId()) {
		case R.id.llintegral:// 我的积分
			// intent.setClass(activity, IntegralActivity.class);
			// String integral =Util.getnum(HomeFragment.idata.integral, false);
			// String totalIntegral=
			// Util.getnum(HomeFragment.idata.totalIntegral, false);
			// if(integral!=null){
			// intent.putExtra("integral", integral);
			// intent.putExtra("totalIntegral", totalIntegral);
			// }
			intent.setClass(activity, IntegralFragmentActivity.class);
			startActivity(intent);

			// startActivity(new Intent(activity, IntegralActivity.class));
			break;

		case R.id.llModifyAvatar:// 我的秀点

			// intent.setClass(activity, ShuntMainActivity.class);
			// String filialMoney =Util.getnum(HomeFragment.idata.filialMoney,
			// false);
			// if(filialMoney!=null){
			// intent.putExtra("filialMoney", filialMoney);
			// }
			// startActivity(intent);

			// intent.setClass(activity, IncentivePointsActivity.class);
			// String filialMoney =Util.getnum(HomeFragment.idata.filialMoney,
			// false);
			// if(filialMoney!=null){
			// intent.putExtra("filialMoney", filialMoney);
			// }

			intent.setClass(activity, ShuntMainFMActivity.class);
			startActivity(intent);// 20170606去掉

			// startActivity(new Intent(activity,
			// IncentivePointsActivity.class));
			// new PickPhotoWindow(activity).showAtBottom();
			break;
		case R.id.civAvtar:
			startActivity(new Intent(activity, MerchantInfoActivity.class));
			break;

		case R.id.llMinehelp:
			Intent about = new Intent(activity, WebLoadActivity.class);
			about.putExtra("title", "用户帮助");
			about.putExtra("url", Api.URL_HELP);
			startActivity(about);
			break;

		case R.id.btnLogout:
			logout();
			break;
		case R.id.llMentionamount:
			startActivity(new Intent(activity, MentionAmountActivity.class));
			break;
		case R.id.llremember:
			startActivity(new Intent(activity, RecommendedmemberActivity.class));
			break;

		case R.id.llModifyNick:// 我的秀心

			// intent.setClass(activity, ExcitationFragment.class);
			// String filialPiety =HomeFragment.idata.filialPiety+"";
			// String totalFilialPiety= HomeFragment.idata.totalFilialPiety+"";
			// if(filialPiety!=null){
			// intent.putExtra("filialPiety", filialPiety);
			// intent.putExtra("totalFilialPiety", totalFilialPiety);
			// }
			intent.setClass(activity, ExcitationMainActivity.class);
			startActivity(intent);
			// startActivity(new Intent(activity, ExcitationFragment.class));
			// startActivity(new Intent(activity,
			// ModifyNicknameActivity.class));
			break;
		case R.id.my_menu_fhq:// 我的分红权，点击事件
			startActivity(new Intent(activity, MyFhqMainActivity.class));
			break;
		case R.id.llModifySCRZ:// 商家入驻
			// System.out.println("===用户信息getAppId:"
			// + UserInfoManager.getAppId(getActivity()));
			// 商家入驻,点击的时候,先查询一下,
			// intent.setClass(activity, MallSettledActivity.class);
			// startActivity(intent);
			// getSelectMallStatusByHttp();//检测状态
			// gotoFunction(MallStringUtils.mallState);// 根据状态码进入不同的界面
//			getzhuangtai();// 获取状态
			break;
		case R.id.llBindMobile:// 兑换
			if (TextUtils
					.isEmpty(UserInfoManager.getUserInfo(activity).card_no)) {
				intent.setClass(activity, RealnameActivity.class);
				startActivity(intent);
				T.show(getActivity(), "请先添加银行卡", Toast.LENGTH_SHORT);
				return;
			}
			if (UserInfoManager.getUserInfo(activity).is_set_pay_passwrod == 0) {
				intent.setClass(activity, FindPaypassActivity.class);
				startActivity(intent);
				T.show(getActivity(), "请设置支付密码", Toast.LENGTH_SHORT);
				return;
			}
			intent.setClass(activity, ExchangeFmAcyivity.class);
			// intent.putExtra("filialMoney", HomeFragment.idata.filialMoney);
			startActivity(intent);
			// startActivity(new Intent(activity, ExchangeAcyivity.class));
			// Intent bind = new Intent(activity, BindMobileActivity.class);
			// bind.putExtra("userInfo", UserInfoManager.getUserInfo(activity));
			// bind.putExtra("back", true);
			// startActivity(bind);
			break;

		case R.id.llModifyPwd:
			startActivity(new Intent(activity, QrCodeActivityFm.class));
			// startActivity(new Intent(activity, RetrievePwdActivity.class));
			break;

		case R.id.llSetting:

			// startActivity(new Intent(activity, SettingActivity.class));
			break;

		case R.id.llrecordedsingle:

			// startActivity(new Intent(activity, RehistoryListActivity.class));
			startActivity(new Intent(activity, RecordedSingleFmActivity.class));
			// startActivity(new Intent(activity,
			// RecordedSingleActivity.class));//第一版
			break;

		case R.id.llAboutUs:
			startActivity(new Intent(activity, RecommendActivity.class));
			// Intent about = new Intent(activity, WebLoadActivity.class);
			// about.putExtra("title", "关于我们");
			// about.putExtra("url", Api.URL_ABOUT);
			// startActivity(about);
			break;

		case R.id.llContactUs:
			startActivity(new Intent(activity, MineActivity.class));
			// startActivity(new Intent(activity, ContactUsActivity.class));
			break;

		case R.id.ivHomeIM:
			ivIM.setBackgroundResource(R.drawable.share_b_mine_service3);
			startActivity(new Intent(activity, MessageActivity.class));
			break;

		case R.id.llMineFeedback:
			startActivity(new Intent(activity, FeedbackActivity.class));
			break;

		case R.id.llMinebankcard:

			startActivity(new Intent(activity, BankCardListActivity.class));// MyBankCardListActivity
			break;
		}
	}

	/*
	private void getzhuangtai() {
		// AjaxParams params = new AjaxParams();
		// params.put("appid", UserInfoManager.getAppId(getActivity()));
		// params.put("account",
		// UserInfoManager.getMallShopId(getActivity()));// 用户标识
		// params.put("username",
		// UserInfoManager.getMallLoginName(getActivity()));// 商家登录的账户
		// params.put("password",
		// UserInfoManager.getMallLoginPassword(getActivity()));// 商家登录的密码
		// final String a = params.getParamString();
		// T.showShort(getActivity(), a);
		// FinalHttp fh = new FinalHttp();
		// fh.post("http://119.23.146.39/mobile/supplier/privilege.php", params,
		// new AjaxCallBack<Object>() {
		// public void onSuccess(Object t) {
		// T.showShort(getActivity(), a + t.toString());
		// System.out.println("====商家入驻结果:"+t.toString());
		// try {
		// JSONObject j = new JSONObject(t.toString());
		// gotoFunction(j.getInt("status"));
		// } catch (JSONException e) {
		// e.printStackTrace();
		// T.showShort(getActivity(),"入驻失败!,请稍后再试...");
		// }
		// };
		//
		// public void onStart() {
		// T.showShort(getActivity(), "开始请求网络");
		// };
		//
		// public void onFailure(Throwable t, int errorNo,
		// String strMsg) {
		// T.showShort(getActivity(), strMsg);
		// };
		// });
		RequestParams params2 = new RequestParams();
		params2.addBodyParameter("appid",
				UserInfoManager.getAppId(getActivity()));// appid
		params2.addBodyParameter("account",
				UserInfoManager.getMallShopId(getActivity()));// 用户标识
		params2.addBodyParameter("username",
				UserInfoManager.getMallLoginName(getActivity()));// 商家登录的账户
		params2.addBodyParameter("password",
				UserInfoManager.getMallLoginPassword(getActivity()));// 商家登录的密码
		// http://119.23.146.39/mobile/supplier/privilege.php
		// 登录之后,拿到商户信息,进行商家登录
		new HttpUtils().send(HttpMethod.POST,
				"http://119.23.146.39/mobile/supplier/privilege.php", params2,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						// T.showShort(getActivity(), "正在处理请稍后...");
						ProgressDialogUtil.showProgressDlg(activity,
								"正在查询入驻状态,请稍后");
						super.onStart();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ProgressDialogUtil.dismissProgressDlg();
						T.showShort(getActivity(), "查询失败，请稍后再试");
						// MallStringUtils.mallState = -2;// 请求失败后,设置为异常状态
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						ProgressDialogUtil.dismissProgressDlg();
						// T.showShort(getActivity(), "查询结果：" + arg0.result);
						
						try {
							JSONObject j = new JSONObject(arg0.result);
							String result_desc = j.optString("result_desc");// 返回的说明
							System.out.println("====商家入驻结果,result_code:"+j.optString("result_code")+ arg0.result+",状态:"+j.optInt("status"));
							String url=j.optString("url");// 返回的说明
							gotoFunction(j.optInt("status"), result_desc,url);

						} catch (JSONException e) {
							e.printStackTrace();
							T.showShort(getActivity(), "未获取到商家入驻状态,请稍后再试...");
						}

						// MallStatus ms = new
						// Gson().fromJson(arg0.result,MallStatus.class);
						// MallStringUtils.mallState = ms.getStatus();
						// gotoFunction(MallStringUtils.mallState);//
						// 根据状态码进入不同的界面
					}
				});
	}
	*/

	@Override
	public void newMessage(int which) {
		ivIM.setBackgroundResource(R.drawable.share_b_mine_service2);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PickPhotoWindow.REQUEST_PICK_LOCAL
					|| requestCode == PickPhotoWindow.REQUEST_TAKE_CAMERA) {
				// 修改头像
				String path = FileUtil.getPath(activity, data.getData());
				if (path == null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap bmp = extras.getParcelable("data");
						if (bmp != null) {
							upPhoto(BitmapTool.Bitmap2File(activity, bmp));
						}
					}
				} else {
					upPhoto(new File(path));
				}
			}
		}
	}

	public void upPhoto(File f) {
		ProgressDialogUtil.showProgressDlg(activity, "图片上传中");
		RequestParams params = new RequestParams();
		params.addBodyParameter("business_type",
				UserInfoManager.getUserId(activity) + "");
		params.addBodyParameter("file", f);
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPLOAD_FILE, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ProgressDialogUtil.dismissProgressDlg();
						T.showNetworkError(activity);
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						ProgressDialogUtil.dismissProgressDlg();
						if (resp.statusCode == 200 && resp.result != null) {
							FileUploadResponse bean = new Gson().fromJson(
									resp.result, FileUploadResponse.class);
							T.showShort(activity, bean.result_desc);
							if (Api.SUCCEED == bean.result_code)
								updateAvatar(bean.data.get(0));
						}
					}
				});
	}

	public void updateAvatar(final String value) {
		ProgressDialogUtil.showProgressDlg(activity, "修改头像");
		UpdateShopRequest req = new UpdateShopRequest();
		req.admin_id = UserInfoManager.getUserId(activity) + "";
		req.user_photo = value;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPDATE_SHOP, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						ProgressDialogUtil.dismissProgressDlg();
						if (resp.statusCode == 200 && resp.result != null) {
							BaseResponse bean = new Gson().fromJson(
									resp.result, BaseResponse.class);
							T.showShort(activity, bean.result_desc);
							if (Api.SUCCEED == bean.result_code) {
								UserInfo user = UserInfoManager
										.getUserInfo(activity);
								user.user_photo = value;
								UserInfoManager.saveUserInfo(activity, user);
								ImageLoadManager.getInstance(activity)
										.displayImage(value, civAvatar);
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ProgressDialogUtil.dismissProgressDlg();
						T.showNetworkError(activity);
					}
				});
	}

	public void logout() {
		EMChatManager.getInstance().logout();
		UserInfoManager.clearUserInfo(activity);
		Intent login = new Intent(activity, LoginActivity.class);
		login.putExtra("logout", true);
		startActivity(login);
		activity.finish();
	}

	private void getSelectMallStatusByHttp() {
//		AjaxParams params = new AjaxParams();
//		params.put("appid", UserInfoManager.getAppId(getActivity()));// appid
//		params.put("account", UserInfoManager.getMallShopId(getActivity()));// 用户标识
//		System.out.println("====商家申请状态查询：" + params.getParamString());
//		// http://119.23.146.39/mobile/checked.php
//		FinalHttp fh = new FinalHttp();
//		fh.post("http://119.23.146.39/mobile/checked.php", params,
//				new AjaxCallBack<Object>() {
//					@Override
//					public void onStart() {
//						super.onStart();
//						ProgressDialogUtil.showProgressDlg(getActivity(),
//								"正在加载中,请稍后...");
//					}
//
//					@Override
//					public void onSuccess(Object t) {
//						super.onSuccess(t);
//						System.out.println("====查询商家申请状态返回:" + t.toString());
//						MallStatus ms = new Gson().fromJson(t.toString(),
//								MallStatus.class);
//						// System.out.println("====查询商家申请状态返回:" +
//						// ms.getStatus());
//						ProgressDialogUtil.dismissProgressDlg();
//						gotoFunction(ms.getStatus(), ms.getResult_desc(),"");// 根据状态码进入不同的界面
//					}
//
//					@Override
//					public void onFailure(Throwable t, int errorNo,
//							String strMsg) {
//						super.onFailure(t, errorNo, strMsg);
//						ProgressDialogUtil.dismissProgressDlg();
//						Toast.makeText(getActivity(), "请求失败,请检查网络!",
//								Toast.LENGTH_SHORT).show();
//					}
//				});
	}

	/**
	 * 根据状态码进入不同的界面
	 * 
	 * @param status
	 */
	/*
	private void gotoFunction(int status, String result_desc,String url) {
		Intent intent = new Intent();
		T.showShort(activity, result_desc + "");
		//status=-1;//测试完毕 就删除掉此代码
		MallStringUtils.mallState=status;
		if (status == -2) {// 未申请
			intent.setClass(activity, MallSettledActivity.class);
			startActivity(intent);
		} else if (status == -1) {// 审核未通过
			intent.setClass(activity, MallSettled_sh_Activity.class);
			startActivity(intent);
		} else if (status == 0) {// 正在审核
			intent.setClass(activity, MallSettled_sh_Activity.class);
			startActivity(intent);
		} else if (status == 1) {// 审核通过
			intent.setClass(activity, MallSettled_kt_Activity.class);
			Bundle b=new Bundle();
			b.putString("url",url);
			intent.putExtras(b);
			startActivity(intent);
		} else {
			T.showShort(getActivity(), "商家信息异常,请尝试重新登录!");
		}
	}
	 */
}
