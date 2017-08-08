package com.shareshenghuo.app.user.fragment;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baozi.Zxing.CaptureActivity;
import com.shareshenghuo.app.user.fragment.BaseFragment;
import com.shareshenghuo.app.user.BankCardListActivity;
import com.shareshenghuo.app.user.ClubCardActivity;
import com.shareshenghuo.app.user.ExchangeFmAcyivity;
import com.shareshenghuo.app.user.ExcitationMainActivity;
import com.shareshenghuo.app.user.FavoritesActivity;
import com.shareshenghuo.app.user.FeedbackActivity;
import com.shareshenghuo.app.user.FindShopListActivity;
import com.shareshenghuo.app.user.IntegralFragmentActivity;
import com.shareshenghuo.app.user.LoginActivity;
import com.shareshenghuo.app.user.MessageActivity;
import com.shareshenghuo.app.user.MyArticleActivity;
import com.shareshenghuo.app.user.MyConsumptionFmActivity;
import com.shareshenghuo.app.user.PaymentnewActivity;
import com.shareshenghuo.app.user.PersonalInfoActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.RecommendActivity;
import com.shareshenghuo.app.user.SettingActivity;
import com.shareshenghuo.app.user.ShopDetailActivity;
import com.shareshenghuo.app.user.ShuntMainActivity;
import com.shareshenghuo.app.user.ShuntMainFMActivity;
import com.shareshenghuo.app.user.SignActivity;
import com.shareshenghuo.app.user.TipsActivity;
import com.shareshenghuo.app.user.activity.realname.RealnameStepIDCardActivity;
import com.shareshenghuo.app.user.function.myfhq.MyFhqMainActivity;
import com.shareshenghuo.app.user.manager.ImageLoadManager;
import com.shareshenghuo.app.user.manager.MessageManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.NumberBean;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.network.bean.WebLoadActivity;
import com.shareshenghuo.app.user.network.request.NumRequest;
import com.shareshenghuo.app.user.network.request.SysMsgRequest;
import com.shareshenghuo.app.user.network.request.UpdateUserInfoRequest;
import com.shareshenghuo.app.user.network.response.FeedBackMsgResponse;
import com.shareshenghuo.app.user.network.response.LoginResponse;
import com.shareshenghuo.app.user.network.response.NumResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.receiver.NewChatMsgWorker;
import com.shareshenghuo.app.user.receiver.NewChatMsgWorker.NewMessageCallback;
import com.shareshenghuo.app.user.util.AnimationUtil;
import com.shareshenghuo.app.user.util.PreferenceUtils;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.Util;
import com.shareshenghuo.app.user.util.Utility;
import com.shareshenghuo.app.user.widget.CircleImageView;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.user.widget.dialog.TwoButtonDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author hang 我的
 */
public class MineFragment extends BaseFragment implements OnClickListener,
		NewMessageCallback {

	public static final int REQ_PERSONAL_CENTER = 0x100;

	private TextView btnSign, tvs;
	private CircleImageView ivAvatar;
	private TextView tvNick;
	private TextView tvMobile;
	private TextView tvMoney;
	private ImageView ivMsg;
	private NumberBean idata = null;
	private NewChatMsgWorker newMsgWatcher;
	private boolean tag = true;
	private TwoButtonDialog downloadDialog;
	private UserInfo data;
	private View c_service;
	private ScrollView scroll;
	int statusBarHeight1;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_mine;
	}

	@Override
	protected void init(View rootView) {
		initView();
		initListener();
		// 获取status_bar_height资源的ID
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {// 根据资源ID获取响应的尺寸值
			statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
		}
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			statusBarHeight1 = 0;
		}

		if (!PreferenceUtils.getPrefBoolean(getActivity(), "mfirst1", false)) {
//			showMask(101, statusBarHeight1, R.drawable.tip4, "mfirst1",
//					rootView.findViewById(R.id.llMinebankcard));

			// ViewUtil.showtip(getActivity(),
			// rootView.findViewById(R.id.llMinebankcard), 1, statusBarHeight1,
			// "mfirst1", R.drawable.tip4);
			return;
		}

		// newMsgWatcher = new NewChatMsgWorker(activity, this);
		// newMsgWatcher.startWork();
	}

	public void initView() {
		scroll = (ScrollView) rootView.findViewById(R.id.scroll);
		c_service = rootView.findViewById(R.id.c_service);
		btnSign = (TextView) rootView.findViewById(R.id.btnMineSign);
		ivAvatar = (CircleImageView) rootView.findViewById(R.id.ivUserAvatar);
		tvNick = (TextView) rootView.findViewById(R.id.tvUserNick);
		tvMobile = (TextView) rootView.findViewById(R.id.tvUserMobile);
		tvMoney = (TextView) rootView.findViewById(R.id.tvUserMoney);
		ivMsg = (ImageView) rootView.findViewById(R.id.ivMessage);

		AnimationUtil.BtnSpecialAnmations1(activity, c_service, 800, 5000);
		c_service.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent about = new Intent(activity, WebLoadActivity.class);
				about.putExtra("title", "在线客服");
				about.putExtra("url", Api.CUSTOMERSERVICE);
				startActivity(about);
			}
		});

		// if(idata==null){
		// loadUserInfo();
		// getStatisticsData();
		// }else{
		// upNum(idata);
		// }
	}

	public void initListener() {
		ivMsg.setOnClickListener(this);
		rootView.findViewById(R.id.llMinePersonalInfo).setOnClickListener(this);
		tvs = (TextView) rootView.findViewById(R.id.tvs);
		btnSign.setOnClickListener(this);
		rootView.findViewById(R.id.llMineWallet).setOnClickListener(this);
		rootView.findViewById(R.id.llMineOrder).setOnClickListener(this);
		rootView.findViewById(R.id.llOrderAll).setOnClickListener(this);
		rootView.findViewById(R.id.llOrderUnDeliver).setOnClickListener(this);
		rootView.findViewById(R.id.llOrderUnReceive).setOnClickListener(this);
		rootView.findViewById(R.id.llOrderUnComment).setOnClickListener(this);
		rootView.findViewById(R.id.llOrderRefund).setOnClickListener(this);
		rootView.findViewById(R.id.llMineClubCard).setOnClickListener(this);
		rootView.findViewById(R.id.llMinePoint).setOnClickListener(this);
		rootView.findViewById(R.id.llMineCoupon).setOnClickListener(this);
		rootView.findViewById(R.id.llMineFavorites).setOnClickListener(this);
		rootView.findViewById(R.id.llMineArticle).setOnClickListener(this);
		rootView.findViewById(R.id.llMineFindShop).setOnClickListener(this);
		rootView.findViewById(R.id.llMineShare).setOnClickListener(this);
		rootView.findViewById(R.id.llMineFeedback).setOnClickListener(this);
		rootView.findViewById(R.id.llMineAbout).setOnClickListener(this);
		rootView.findViewById(R.id.ivScanQR).setOnClickListener(this);
		rootView.findViewById(R.id.ivSetting).setOnClickListener(this);
		rootView.findViewById(R.id.llMineoil).setOnClickListener(this);
		rootView.findViewById(R.id.llMinebankcard).setOnClickListener(this);
		rootView.findViewById(R.id.llnewpay).setOnClickListener(this);
		rootView.findViewById(R.id.llMinehelp).setOnClickListener(this);
		rootView.findViewById(R.id.llcar).setOnClickListener(this);
		rootView.findViewById(R.id.llcarpay).setOnClickListener(this);

		rootView.findViewById(R.id.my_menu_fhq).setOnClickListener(this);// 我的分红权，监听事件

		// rootView.findViewById(R.id.llMinerealname).setOnClickListener(this);

		if (!tag) {
			tvs.setText("转账");
		}
		// rootView.findViewById(R.id.llOrderRefund).setOnLongClickListener(new
		// OnLongClickListener() {
		//
		// @Override
		// public boolean onLongClick(View arg0) {
		// // TODO Auto-generated method stub
		// if(!tag){
		// tvs.setText("兑换");
		// tag =true;
		// }else{
		// tvs.setText("转账");
		// tag =false;
		// }
		//
		// return false;
		// }
		// });

	}

	private void initmsg() {
		SysMsgRequest req = new SysMsgRequest();
		req.user_id = UserInfoManager.getUserId(activity) + "";
		req.user_type = "1";
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
						FeedBackMsgResponse bean = new Gson().fromJson(
								resp.result, FeedBackMsgResponse.class);
						if (Api.SUCCEED == bean.result_code) {
							int count = Integer
									.parseInt(bean.data.feedBackMsgNum);
							int count1 = Integer
									.parseInt(bean.data.systemMsgNum);
							if (count > 0 || count1 > 0) {
								ivMsg.setImageResource(R.drawable.share_c_mine_icon2_liaotian);//带红点
							} else if (MessageManager.getUnreadCount() > 0) {
								ivMsg.setImageResource(R.drawable.share_c_mine_icon2_liaotian);//带红
							} else {
								ivMsg.setImageResource(R.drawable.share_c_mine_icon2_liaotian);
							}
						}
					}
				});
	}

	@Override
	public void onStart() {
		super.onStart();
		// UserInfoManager.updateUserInfo(activity);
		loadUserInfo();
		// getStatisticsData();
		initmsg();
	}

	@Override
	public void onResume() {
		super.onResume();
		ivMsg.setImageResource(MessageManager.getUnreadCount() > 0 ? R.drawable.share_c_mine_icon2_liaotian
				: R.drawable.share_c_mine_icon2_liaotian);//TODO 第1个是带红点的。
	}

	public void loadUserInfo() {
		// ProgressDialogUtil.showProgressDlg(activity, "");
		if (!UserInfoManager.isLogin(getActivity())) {
			tvNick.setText("点击登录");
			tvMobile.setText("");
			ivAvatar.setImageResource(R.drawable.share_c_mine_head_moren);
			return;
		}
		tvNick.setText("");

		UpdateUserInfoRequest req = new UpdateUserInfoRequest();
		try {
			req.user_id = UserInfoManager.getUserId(activity) + "";
		} catch (Exception e) {
		}

		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		new HttpUtils().send(HttpMethod.POST, Api.URL_GET_USERINFO, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ProgressDialogUtil.dismissProgressDlg();
						if (activity != null)
							T.showNetworkError(activity);
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						ProgressDialogUtil.dismissProgressDlg();
						LoginResponse bean = new Gson().fromJson(resp.result,
								LoginResponse.class);
						Log.e("", " - - -  " + resp.result);
						if (Api.SUCCEED == bean.result_code && activity != null) {

							UserInfoManager.saveUserInfo(activity, bean.data);
							// if(data==null){
							updateView(bean.data);
							// }
							// data = bean.data;
						}
					}
				});
	}

	public void updateView(UserInfo data) {
		btnSign.setText(data.is_sign == 1 ? "已签到" : "签到");
		ImageLoadManager.getInstance(activity).userphoto(data.user_photo,
				ivAvatar);

		if (!TextUtils.isEmpty(data.nick_name)) {
			tvNick.setText(data.nick_name);
		}
		// else{
		// tvNick.setText("未实名认证");
		// }
		if (!TextUtils.isEmpty(data.mobile)) {
			tvMobile.setText(data.mobile);
		} else {
			tvMobile.setText("未知");
		}

		// tvMoney.setText(data.money+"");
		// setText(R.id.tvCouponCount, data.coupon_count+"");
		// setText(R.id.tvUserPoint, data.point+"");
	}

	@Override
	public void onClick(View v) {

		if (!UserInfoManager.isLogin(activity)) {
			T.showShort(getActivity().getApplicationContext(), "您当前未登录，请先登录");
			startActivity(new Intent(getActivity(), LoginActivity.class));
			return;
		}

		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.ivMessage:
			ivMsg.setImageResource(R.drawable.share_c_mine_icon2_liaotian);
			startActivity(new Intent(activity, MessageActivity.class));
			break;

		case R.id.llMinePersonalInfo:
			activity.startActivityForResult(new Intent(activity,
					PersonalInfoActivity.class), REQ_PERSONAL_CENTER);
			break;

		case R.id.btnMineSign:
			startActivity(new Intent(activity, SignActivity.class));
			break;

		case R.id.llMineWallet:

			// intent.setClass(activity, IntegralDivisionActivity.class);//分流
			// startActivity(intent);

			// intent.setClass(activity, IntegralActivity.class);//第一版
			// if(idata!=null){
			// String integrals =Util.getnum(idata.integral+"", false);
			// String totalIntegrals= Util.getnum(idata.totalIntegral+"",
			// false);
			// if(integrals!=null){
			// intent.putExtra("integral", integrals);
			// intent.putExtra("totalIntegral", totalIntegrals);
			// }
			// }
			intent.setClass(activity, IntegralFragmentActivity.class);// 最新
			startActivity(intent);
			// startActivity(new Intent(activity, WalletActivity.class));
			break;

		case R.id.llMineOrder:
			// startActivity(new Intent(activity, OrderListActivity.class));
			break;

		case R.id.llnewpay:

			startActivityForResult(new Intent(activity, CaptureActivity.class),
					CaptureActivity.REQ_SCAN_QR);
			// startActivity(new Intent(activity, OrderListActivity.class));
			// startActivity(new Intent(activity, OrderListActivity.class));
			break;

		case R.id.llcar:

			Intent car = new Intent(activity, WebLoadActivity.class);
			car.putExtra("title", "网约车");
			UserInfo userInfo = UserInfoManager.getUserInfo(getActivity());
			car.putExtra("url", Api.car + userInfo.mobile);
			System.out.println("===网约车访问地址：" + Api.car + userInfo.mobile);
			startActivity(car);

			break;

		case R.id.llcarpay:
			Intent carpay = new Intent(activity, WebLoadActivity.class);
			carpay.putExtra("title", "网约车充值");

			carpay.putExtra("url",
					Api.carpay + UserInfoManager.getUserId(getActivity()));
			System.out.println("===网约车充值访问地址：" + Api.carpay
					+ UserInfoManager.getUserId(getActivity()));
			startActivity(carpay);
			break;

		case R.id.llOrderAll://我的秀心
			intent.setClass(activity, ExcitationMainActivity.class);//
			startActivity(intent);
			break;

		case R.id.llOrderUnDeliver://我的秀点
			intent.setClass(activity, ShuntMainFMActivity.class);// 产业链
			// intent.setClass(activity, ShuntMainActivity.class);
			// if(idata!=null){
			// String filialMoney =Util.getnum(idata.filialMoney+"", false);
			// intent.putExtra("filialMoney", filialMoney);
			// }

			// if(filialMoney!=null){
			//
			// }
			startActivity(intent);
			// startActivity(new Intent(activity,
			// IncentivePointsActivity.class));
			// Intent unDeliver = new Intent(activity, OrderListActivity.class);
			// unDeliver.putExtra("status", 1);
			// startActivity(unDeliver);
			break;
		case R.id.llOrderUnReceive://我的积分

			// intent.setClass(activity, IntegralActivity.class);老版
			// if(idata!=null){
			// String integral =Util.getnum(idata.integral+"", false);
			// String totalIntegral= Util.getnum(idata.totalIntegral+"", false);
			// if(integral!=null){
			// intent.putExtra("integral", integral);
			// intent.putExtra("totalIntegral", totalIntegral);
			// }
			// }

			intent.setClass(activity, IntegralFragmentActivity.class);// 最新

			// intent.setClass(activity, IntegralDivisionActivity.class);//分流
			startActivity(intent);

			// startActivity(new Intent(activity, PointActivity.class));
			// startActivity(new Intent(activity, PointActivity.class));
			// Intent unReceive = new Intent(activity, OrderListActivity.class);
			// unReceive.putExtra("status", 2);
			// startActivity(unReceive);
			break;

		case R.id.llOrderUnComment:
			startActivity(new Intent(activity, MyConsumptionFmActivity.class));// 产业链

			// startActivity(new Intent(activity, MyConsumptionActivity.class));
			// startActivity(new Intent(activity, ConsumptionActivity.class));
			// Intent unComment = new Intent(activity, OrderListActivity.class);
			// unComment.putExtra("status", 3);
			// startActivity(unComment);
			break;

		case R.id.llOrderRefund:

			if (UserInfoManager.getUserInfo(activity).certification_status == null
					|| !UserInfoManager.getUserInfo(activity).certification_status
							.equals("2")
					&& !UserInfoManager.getUserInfo(activity).is_old_user
							.equals("1")) {
				if (UserInfoManager.getUserInfo(activity).is_can_certification
						.equals("0")) {
					initDialog("实名认证失败,请联系客服", " 确定", "");
					return;
				}
				initrename();
				// intent.setClass(activity, RealnameStepIDCardActivity.class);
				// startActivity(intent);
				// T.show(getActivity(), "请先实名认证", Toast.LENGTH_SHORT);
				return;
			}

			// if(UserInfoManager.getUserInfo(activity).is_set_pay_passwrod==0){
			// intent.setClass(activity, FindPaypassActivity.class);
			// startActivity(intent);
			// T.show(getActivity(), "请设置支付密码", Toast.LENGTH_SHORT);
			// return;
			// }

			// if(tag==false){
			// intent.setClass(activity, TransferTextActivity.class);
			// intent.putExtra("filialMoney", idata.filialMoney);
			// startActivity(intent);
			// return;
			// }

			intent.setClass(activity, ExchangeFmAcyivity.class);
			// if(idata!=null){
			// intent.putExtra("filialMoney", idata.filialMoney);
			// }

			startActivity(intent);
			// startActivity(new Intent(activity, ExchangeAcyivity.class));
			// Intent refund = new Intent(activity, OrderListActivity.class);
			// refund.putExtra("status", 5);
			// startActivity(refund);
			break;

		case R.id.llMineClubCard:
			startActivity(new Intent(activity, ClubCardActivity.class));
			break;
		case R.id.llMineoil:
			// if(UserInfoManager.getUserInfo(activity).certification_status==null||
			// !UserInfoManager.getUserInfo(activity).certification_status.equals("2")){
			// if(UserInfoManager.getUserInfo(activity).is_can_certification.equals("0")){
			// initDialog("实名认证失败,请联系客服", " 确定", "");
			// return;
			// }
			// initrename();
			// return;
			// }
			// startActivity(new Intent(activity, OilCardActivity.class));
			break;

		case R.id.llMinePoint:
			// intent.setClass(activity, IncentivePointsActivity.class);
			// if(idata!=null){
			// String filialMoneys =Util.getnum(idata.filialMoney+"", false);
			// if(filialMoneys!=null){
			// intent.putExtra("filialMoney", filialMoneys);
			// }
			// }
			intent.setClass(activity, ShuntMainActivity.class);
			if (idata != null) {
				String filialMoney = Util.getnum(idata.filialMoney + "", false);
				intent.putExtra("filialMoney", filialMoney);
			}
			startActivity(intent);

			// startActivity(new Intent(activity, PointActivity.class));
			break;

		case R.id.llMineCoupon:
			// intent.setClass(activity, ShowFilialActivity.class);//分流
			// startActivity(intent);
			// intent.setClass(activity, ExcitationFragment.class);//第一版
			// if(idata!=null){
			// String filialPietys =idata.filialPiety+"";
			// String totalFilialPietys= idata.totalFilialPiety+"";
			// if(filialPietys!=null){
			// intent.putExtra("filialPiety", filialPietys);
			// intent.putExtra("totalFilialPiety", totalFilialPietys);
			// }
			// }
			intent.setClass(activity, ExcitationMainActivity.class);

			startActivity(intent);
			// startActivity(new Intent(activity, CouponListActivity.class));
			break;

		case R.id.llMineFavorites:
			startActivity(new Intent(activity, FavoritesActivity.class));
			break;
		case R.id.my_menu_fhq:// 我的分红权，点击事件
			startActivity(new Intent(activity, MyFhqMainActivity.class));
			break;
		case R.id.llMinebankcard:
			if (UserInfoManager.getUserInfo(activity).certification_status == null
					|| !UserInfoManager.getUserInfo(activity).certification_status
							.equals("2")) {

				if (UserInfoManager.getUserInfo(activity).is_can_certification
						.equals("0")) {
					initDialog("实名认证失败,请联系客服", " 确定", "");
					return;
				}
				initrename();
				// intent.setClass(activity, RealnameStepIDCardActivity.class);
				// startActivity(intent);
				// T.show(getActivity(), "请先实名认证", Toast.LENGTH_SHORT);
				return;
			}
			startActivity(new Intent(activity, BankCardListActivity.class));// MyBankCardListActivity
			break;

		// case R.id.llMinerealname:
		// startActivity(new Intent(activity, Realname1Activity.class));
		// break;

		case R.id.llMineArticle:
			startActivity(new Intent(activity, MyArticleActivity.class));
			break;

		case R.id.llMineFindShop:
			startActivity(new Intent(activity, FindShopListActivity.class));
			break;

		case R.id.llMinehelp://用户帮助
			Intent about = new Intent(activity, WebLoadActivity.class);
			about.putExtra("title", "用户帮助");
			about.putExtra("url", Api.URL_HELP);
			startActivity(about);
			break;

		case R.id.llMineShare:
			startActivity(new Intent(activity, RecommendActivity.class));
			// startActivity(new Intent(activity, ShareMenuActivity.class));
			// ShareMenuWindow window = new ShareMenuWindow(activity);
			// window.url = Api.URL_SHARE_APP + "?user_id=" +
			// UserInfoManager.getUserId(activity);
			// window.title = getResources().getString(R.string.app_name);
			// window.content = getResources().getString(R.string.app_name);
			// window.showAtBottom();
			break;

		case R.id.llMineFeedback:
			startActivity(new Intent(activity, FeedbackActivity.class));
			break;

		case R.id.llMineAbout:
			Intent help = new Intent(activity, WebLoadActivity.class);
			help.putExtra("title", "关于我们");
			help.putExtra("url", Api.URL_ABOUT);
			startActivity(help);
			break;

		case R.id.ivScanQR:
			activity.startActivityForResult(new Intent(activity,
					CaptureActivity.class), CaptureActivity.REQ_SCAN_QR);
			break;

		case R.id.ivSetting:
			startActivity(new Intent(activity, SettingActivity.class));
			break;
		}
	}

	@Override
	public void newMessage(int which) {
		// TODO: 2017/6/29 带红点
		ivMsg.setImageResource(R.drawable.share_c_mine_icon2_liaotian);
	}

	public void getStatisticsData() {

		if (!UserInfoManager.isLogin(getActivity())) {
			setText(R.id.tvUserMoney, "0");
			setText(R.id.tvCouponCount, "0");
			setText(R.id.tvUserPoint, "0");
			return;
		}

		// ProgressDialogUtil.showProgressDlg(activity, "");
		NumRequest req = new NumRequest();
		try {
			req.userId = UserInfoManager.getUserInfo(activity).id + "";

		} catch (Exception e) {
			// TODO: handle exception
		}
		req.userType = "1";

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
						ProgressDialogUtil.dismissProgressDlg();
						T.showNetworkError(activity);
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						ProgressDialogUtil.dismissProgressDlg();
						NumResponse bean = new Gson().fromJson(resp.result,
								NumResponse.class);
						if (Api.SUCCEED == bean.result_code) {

							idata = bean.data;
							upNum(idata);
							// lvData.setAdapter(new DataReportAdapter(activity,
							// bean.data));
						}
					}
				});

		// SysMsgRequest reqs = new SysMsgRequest();
		// reqs.user_id = UserInfoManager.getUserId(activity)+"";
		// reqs.user_type = "1";
		// RequestParams paramss = new RequestParams();
		// try {
		// paramss.setBodyEntity(new StringEntity(reqs.toJson()));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		// new HttpUtils().send(HttpMethod.POST,
		// Api.NOTICEANDFEEDCOUNTCONTROLLER, params, new
		// RequestCallBack<String>() {
		// @Override
		// public void onFailure(HttpException arg0, String arg1) {
		// }
		//
		// @Override
		// public void onSuccess(ResponseInfo<String> resp) {
		// FeedBackMsgResponse bean = new Gson().fromJson(resp.result,
		// FeedBackMsgResponse.class);
		// if(Api.SUCCEED == bean.result_code) {
		// int count = Integer.parseInt(bean.data.feedBackMsgNum);
		// int count1 = Integer.parseInt(bean.data.systemMsgNum);
		// if(count > 0||count1 > 0) {
		// }else{
		// }
		// }
		// }
		//
		// });
	}

	public void upNum(NumberBean bean) {
		setText(R.id.tvUserMoney, "" + Util.getnum(bean.integral + "", false));
		setText(R.id.tvCouponCount, "" + bean.filialPiety);
		if (bean.totalFilialMoney != null) {
			setText(R.id.tvUserPoint,
					"" + Util.getnum(bean.totalFilialMoney + "", false));
		}
	}

	public void initrename() {
		if (downloadDialog != null && downloadDialog.isShowing()) {
			return;
		}

		downloadDialog = new TwoButtonDialog(getActivity(),
				R.style.CustomDialog, "尊敬的会员", "为保证您的资金安全\n在交易前需进行实名认证", " 取消",
				"开始认证", true, new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							downloadDialog = null;
							break;
						case R.id.Button_cancel:
//							startActivity(new Intent(activity,
//									RealnameStepIDCardActivity.class));
							startActivity(new Intent(activity, RealnameStepIDCardActivity.class));
							downloadDialog.dismiss();
							downloadDialog = null;
						default:
							break;
						}
					}
				});
		downloadDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else {
					return true; // 默认返回 false
				}
			}
		});
		// downloadDialog.setCanceledOnTouchOutside(false);

		downloadDialog.show();
	}

	private void initDialog(String content, String left, String right) {
		// TODO Auto-generated method stub
		downloadDialog = new TwoButtonDialog(activity, R.style.CustomDialog,
				"", content, left, right, true, new OnMyDialogClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
		downloadDialog.show();
	}

	@Override
	public void onActivityResult(int reqCode, int resCode, Intent data) {
		super.onActivityResult(reqCode, resCode, data);
		if (resCode == Activity.RESULT_OK) {
			switch (reqCode) {
			case CaptureActivity.REQ_SCAN_QR:

				try {
					Intent it = new Intent(activity, ShopDetailActivity.class);
					it.putExtra("shopId",
							Integer.parseInt(data.getStringExtra("result")));
					startActivity(it);
				} catch (Exception e1) {
					// T.showShort(this, "鏃犳硶璇嗗埆闈炲晢瀹朵簩缁寸爜");
					// e.printStackTrace();

					String shopid = data.getStringExtra("result");
					String id = "";
					String discountType = "";
					String mchId = "";
					try {
						id = Utility.URLRequest(shopid).get("shopid")
								.toString();

					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						discountType = Utility.URLRequest(shopid)
								.get("discounttype").toString();

					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						mchId = Utility.URLRequest(shopid).get("mchid")
								.toString();

					} catch (Exception e) {
						// TODO: handle exception
					}

					if (TextUtils.isEmpty(id) && TextUtils.isEmpty(mchId)) {
						Log.e("", "" + shopid);
						Intent about = new Intent(activity,
								WebLoadActivity.class);
						about.putExtra("title", "");
						about.putExtra("url", shopid);
						startActivity(about);
						return;
					}
					Intent it = new Intent(activity, PaymentnewActivity.class);
					it.putExtra("shopId", id);
					it.putExtra("discountType", discountType);
					it.putExtra("mchId", mchId);
					startActivity(it);
					// try {
					// Intent it = new Intent(this, ShopDetailActivity.class);
					// it.putExtra("shopId",
					// Integer.parseInt(data.getStringExtra("result")));
					// startActivity(it);
					// } catch(Exception e) {
					// T.showShort(this, "无法识别非商家二维码");
					// e.printStackTrace();
					// }
				}

			case 101:

				// if(!PreferenceUtils.getPrefBoolean(getActivity(), "mfirst2",
				// false)){
				// ViewUtil.showtip(getActivity(), (LinearLayout)
				// rootView.findViewById(R.id.llMinehelp), 1, statusBarHeight1,
				// "mfirst2", R.drawable.tip2,null);
				// return;
				// }

//				if (!PreferenceUtils.getPrefBoolean(getActivity(), "mfirst2",
//						false)) {
//					showMask(102, statusBarHeight1, R.drawable.tip2, "mfirst2",
//							rootView.findViewById(R.id.llMinehelp));
//					return;
//				}

				break;

			case 102:

				// if(!PreferenceUtils.getPrefBoolean(getActivity(), "mfirst3",
				// false)){
				// ViewUtil.showtip(getActivity(), (LinearLayout)
				// rootView.findViewById(R.id.llOrderAll), 1, statusBarHeight1,
				// "mfirst3", R.drawable.tip5,null);
				// return;
				// }
				// if(!PreferenceUtils.getPrefBoolean(getActivity(), "mfirst3",
				// false)){
				// showMask(103, statusBarHeight1, R.drawable.tip5, "mfirst3",
				// rootView.findViewById(R.id.llone));
				// return;
				// }

				// if (!PreferenceUtils.getPrefBoolean(getActivity(), "mfirst3",
				// false)) {
				// showMask2();
				// }
				//
				
//				if (!PreferenceUtils.getPrefBoolean(getActivity(), "mfirst5",
//						false)) {
//					showMask(102, statusBarHeight1, R.drawable.tip5, "mfirst5",
//							rootView.findViewById(R.id.llOrderAll));
//					return;
//				}
//				MorePopWindow d = new MorePopWindow(activity);
//				d.showPopupWindow(rootView.findViewById(R.id.tv_xiaoxin));
				break;
			}
		}
	}

	private void showMask2() {
		// int statusBarHeight1 = 0;
		// //获取status_bar_height资源的ID
		// int resourceId = getResources().getIdentifier("status_bar_height",
		// "dimen", "android");
		// if (resourceId > 0) {
		// //根据资源ID获取响应的尺寸值
		// statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
		// }

//		ViewUtil.showtip(activity, rootView.findViewById(R.id.llOrderAll), 1,
//				statusBarHeight1, "mfirst3", R.drawable.tip5, null);
	}

	private void showMask(final int k, final int statusBarHeight1,
			final int tip, final String string, final View view) {
		// view = (LinearLayout) rootView.findViewById(R.id.llMinebankcard);
		view.postDelayed(new Runnable() {
			@Override
			public void run() {
				// runOnUiThread(new Runnable() {
				// @Override
				// public void run() {

				int right = view.getWidth() / 2;
				int left = view.getLeft();
				int top = view.getTop() + statusBarHeight1;
				int bottom = view.getBottom() + statusBarHeight1;
				int hight = top;
				int loc[] = { left, top, right, bottom, hight, tip, 1 };
				Intent intent = new Intent(activity, TipsActivity.class);
				intent.putExtra("loc", loc);
				startActivityForResult(intent, k);
				PreferenceUtils.setPrefBoolean(activity, string, true);
				// }
				// });
			}
		}, 500);
	}

}
