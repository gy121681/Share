package com.shareshenghuo.app.shop.fragment;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.CommodityManageActivity;
import com.shareshenghuo.app.shop.ConsumptionFragmentActivity;
import com.shareshenghuo.app.shop.EditShopActivity;
import com.shareshenghuo.app.shop.EkOrderPay;
import com.shareshenghuo.app.shop.IncentivePointsActivity;
import com.shareshenghuo.app.shop.MemberManageActivity;
import com.shareshenghuo.app.shop.MessageActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.MessageManager;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.NoticeBean;
import com.shareshenghuo.app.shop.network.bean.NumberBean;
import com.shareshenghuo.app.shop.network.request.BannerRequest;
import com.shareshenghuo.app.shop.network.request.NumRequest;
import com.shareshenghuo.app.shop.network.request.VersionRequest;
import com.shareshenghuo.app.shop.network.response.BannerListResp;
import com.shareshenghuo.app.shop.network.response.NoticeResponse;
import com.shareshenghuo.app.shop.network.response.NumResponse;
import com.shareshenghuo.app.shop.network.response.ShopShowDataResponse;
import com.shareshenghuo.app.shop.network.response.VersionRespone;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker.NewMessageCallback;
import com.shareshenghuo.app.shop.util.ApkUtil;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.UpdateService;
import com.shareshenghuo.app.shop.util.Util;
import com.shareshenghuo.app.shop.widget.BannerViewPager;
import com.shareshenghuo.app.shop.widget.JazzyViewPager;
import com.shareshenghuo.app.shop.widget.MyGridView;
import com.shareshenghuo.app.shop.widget.JazzyViewPager.TransitionEffect;
import com.shareshenghuo.app.shop.widget.dialog.ButtonDialogStyleNotice;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.QRCodeWindow;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;

public class HomeFragment extends BaseFragment 
	implements OnClickListener, NewMessageCallback, OnRefreshListener2<ScrollView> {
	
	private PullToRefreshScrollView svContent;
	private ImageView ivIM;
	private JazzyViewPager jazzy_pager;
	private ArrayList<View> views;
	private BannerViewPager banner;
	private MyGridView lvData;
	private LayoutInflater inflater;
	private NewChatMsgWorker newMsgWatcher;
	private  BannerListResp bean = null;
	public static NumberBean idata = null;
	private TwoButtonDialog downloadDialog;
	private ButtonDialogStyleNotice noticedialog;
	private View stopdata;
	private TextView tv_date;
	private LinearLayout lin_1,lin_2,lin_3;

	@Override
	protected int getLayoutId() {
		
		return R.layout.fragment_home;
	}

	@Override
	protected void init(View rootView) {
		
		inflater = getActivity().getLayoutInflater();
		checkVersion();
		initView();
		initListener();
		
//		if(bean==null){
//			if(bean.data==null){
				loadData();
//			}else{
//				initpager(bean);
//			}
//		}
		
//		newMsgWatcher = new NewChatMsgWorker(activity, this);
//		newMsgWatcher.startWork();
		
		
	}
	
	
	private void initpager(BannerListResp bean) {
		// TODO Auto-generated method stub

		
		views = new ArrayList<View>();
//		View view1 = inflater.inflate(R.layout.banner_layout_1, null);
//		View view2 = inflater.inflate(R.layout.banner_layout_2, null);
//		View view3 = inflater.inflate(R.layout.banner_layout_3, null);
//		if(bean != null&&bean.data!=null){
//		
//		TextView tv_threeSunTotalFee = (TextView) view1.findViewById(R.id.tv_threeSunTotalFee);
//		TextView tv_twoSunTotalFee = (TextView) view1.findViewById(R.id.tv_twoSunTotalFee);
//		TextView tv_oneSunTotalFee = (TextView) view1.findViewById(R.id.tv_oneSunTotalFee);
//		tv_threeSunTotalFee.setText(Util.getnum(bean.data.threeSunTotalFee,true));
//		tv_twoSunTotalFee.setText(Util.getnum(bean.data.twoSunTotalFee,true));
//		tv_oneSunTotalFee.setText(Util.getnum(bean.data.oneSunTotalFee,true));
//		
//		TextView tv_sumTotalConsumption = (TextView) view2.findViewById(R.id.tv_sumTotalConsumption);
//		TextView tv_countUser = (TextView) view2.findViewById(R.id.tv_countUser);
//		TextView tv_countShop = (TextView) view2.findViewById(R.id.tv_countShop);
//		tv_sumTotalConsumption.setText(Util.getnum(bean.data.sumTotalConsumption,true));
//		tv_countUser.setText(bean.data.countUser);
//		tv_countShop.setText(bean.data.countShop);
//		
//		
//		TextView tv_payDate = (TextView) view3.findViewById(R.id.tv_payDate);
//		TextView tv_userFilialPric = (TextView) view3.findViewById(R.id.tv_userFilialPric);
//		TextView tv_shopFilialPric = (TextView) view3.findViewById(R.id.tv_shopFilialPric);
//		
//		tv_payDate.setText( DateUtil.getStrTime(bean.data.payDate));
//		tv_userFilialPric.setText(Util.getnum(bean.data.userFilialPric,false));
//		tv_shopFilialPric.setText(Util.getnum(bean.data.shopFilialPric,false));
//		}
//
//		
//		
//		views.add(view1);
//		views.add(view2);
//		views.add(view3);
		
		
		try {
			jazzy_pager = getView(R.id.jazzy_pager);
			jazzy_pager.setupJazziness(TransitionEffect.CubeOut, jazzy_pager, activity, views,inflater,bean,0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
//		jazzy_pager.startpager();
		
	}

	public void initView() {
		svContent = getView(R.id.svHomeContent);
		ivIM = getView(R.id.ivHomeIM);
		banner = getView(R.id.bannerHome);
		lvData = getView(R.id.lvData);
		stopdata = getView(R.id.stopdata);
		tv_date = getView(R.id.tv_date);
		
	}
	
	
	
	public void initListener() {
		svContent.setOnRefreshListener(this);
		
		ivIM.setOnClickListener(this);
		getView(R.id.ivHomeCreateQR).setOnClickListener(this);
		getView(R.id.llHomeAccount).setOnClickListener(this);
//		getView(R.id.llViewRechargeTotal).setOnClickListener(this);
//		getView(R.id.llViewConsumeTotal).setOnClickListener(this);
//		getView(R.id.llViewVipData).setOnClickListener(this);
//		getView(R.id.llViewPointLog).setOnClickListener(this);
//		getView(R.id.llViewCouponUse).setOnClickListener(this);
		getView(R.id.rela_4).setOnClickListener(this);
		getView(R.id.rela_3).setOnClickListener(this);
		getView(R.id.rela_2).setOnClickListener(this);
		getView(R.id.rela_1).setOnClickListener(this);
		getView(R.id.llMineWallet).setOnClickListener(this);
		getView(R.id.llMinePoint).setOnClickListener(this);
		getView(R.id.llMineCoupon).setOnClickListener(this);
		getView(R.id.lin_1).setOnClickListener(this);
		getView(R.id.lin_2).setOnClickListener(this);
		getView(R.id.lin_3).setOnClickListener(this);
		
	}
	
	public void loadData() {
//		getstop();
		getBannerList();
//		getStatisticsData();
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
//		jazzy_pager.startpager();
//		UserInfo user = UserInfoManager.getUserInfo(activity);
//		setText(R.id.tvUnconfirmMoney, ""+user.with_drawals_ing_fee);
//		setText(R.id.tvCanWithdrawMoney, ""+user.money);
//		setText(R.id.tvHasWithdrawMoney, ""+user.with_drawals_ed_fee);
	}
	
	public void upNum(NumResponse bean){
		setText(R.id.tvUnconfirmMoney, ""+Util.getnum(bean.data.integral, false));
		setText(R.id.tvCanWithdrawMoney, ""+bean.data.filialPiety);
		setText(R.id.tvHasWithdrawMoney, ""+Util.getnum(bean.data.filialMoney,false));
	}
	
	@Override
	public void onPause() {
		super.onPause();
		banner.stopRoll();
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(jazzy_pager!=null){
			jazzy_pager.stop();
		}
	
	}

	@Override
	public void onResume() {
		super.onResume();
		banner.startRoll();
		ivIM.setBackgroundResource(MessageManager.getUnreadCount() > 0 ? 
				R.drawable.ic_im_notice : R.drawable.ic_im);
	}
	
//	public void getBannerList() {
//		BannerRequest req = new BannerRequest();
//		req.city_id = UserInfoManager.getUserInfo(activity).city_id + "";
//		req.banner_place = "3";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_BANNER_LIST, params,
//				new RequestCallBack<String>() {
//					@Override
//					public void onSuccess(ResponseInfo<String> resp) {
//						svContent.onRefreshComplete();
//						BannerListResponse bean = new Gson().fromJson(resp.result, BannerListResponse.class);
//						if (bean.data != null && bean.data.size() > 0) {
//							banner.createView(bean.data);
//							banner.startRoll();
//							banner.setLayoutWidthHeight(0.5);
//						}
//					}
//
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//					}
//				});
//	}
	
	public void getBannerList() {
		BannerRequest req = new BannerRequest();
		req.city_id = UserInfoManager.getUserInfo(activity).city_id + "";
		req.banner_place = "3";
		RequestParams params = new RequestParams();
//		try {
////			params.setBodyEntity(new StringEntity());
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		new HttpUtils().send(HttpMethod.GET, Api.GETBANNER, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						svContent.onRefreshComplete();
						 bean = new Gson().fromJson(resp.result, BannerListResp.class);
						 Log.e("", "banner     "+resp.result);
						if(Api.SUCCEED == bean.result_code) {
							if(bean.data.isStop!=null&&bean.data.isStop.equals("1")){
								stopdata.setVisibility(View.VISIBLE);
								if(bean.data.openDate!=null){
									tv_date.setText(bean.data.openDate);
								}
							}else{
								stopdata.setVisibility(View.GONE);
								initpager(bean);
							}
							
						}else{
//							initpager(bean);
						}
					}
					@Override
					public void onFailure(HttpException arg0, String arg1) {
//						initpager(bean);
					}
				});
	}
	
	
//	public void getstop() {
//		BannerRequest req = new BannerRequest();
//		RequestParams params = new RequestParams();
//		new HttpUtils().send(HttpMethod.GET, Api.GETBANNERSTOP, params,
//				new RequestCallBack<String>() {
//					@Override
//					public void onSuccess(ResponseInfo<String> resp) {
//						svContent.onRefreshComplete();
//						ShopShowDataResponse bean = new Gson().fromJson(resp.result, ShopShowDataResponse.class);
//						if(Api.SUCCEED == bean.result_code) {
//							if(bean.data.isStop.equals("1")){
//								stopdata.setVisibility(View.VISIBLE);
//							}else{
//								getBannerList();
//								stopdata.setVisibility(View.GONE);
//							}
//						}
//					}
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//					}
//				});
//	}
	
	
//	public void getStatisticsData() {
//		ProgressDialogUtil.showProgressDlg(activity, "");
//		ShopRequest req = new ShopRequest();
//		req.shop_id = UserInfoManager.getUserInfo(activity).shop_id+"";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_STATISTIC_LIST, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				svContent.onRefreshComplete();
//				T.showNetworkError(activity);
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				svContent.onRefreshComplete();
//				StatisticListResponse bean = new Gson().fromJson(resp.result, StatisticListResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					lvData.setAdapter(new DataReportAdapter(activity, bean.data));
//				}
//			}
//		});
//	}
	
	public void getStatisticsData() {
		ProgressDialogUtil.showProgressDlg(activity, "");
		NumRequest req = new NumRequest();
		req.userId = UserInfoManager.getUserInfo(activity).shop_id+"";
		req.userType = "2";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETBUNBER, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				svContent.onRefreshComplete();
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				svContent.onRefreshComplete();
				NumResponse bean = new Gson().fromJson(resp.result, NumResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					upNum(bean);
					idata = bean.data;
//					lvData.setAdapter(new DataReportAdapter(activity, bean.data));
				}
			}
		});
	}
	
	
	public void getnotice() {
		NumRequest req = new NumRequest();
		req.notice_type = "2";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETANNOUNCEMENT, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				NoticeResponse bean = new Gson().fromJson(resp.result, NoticeResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					upnotice(bean.data);
				}
			}

		});
	}
	

	private void upnotice(NoticeBean data) {
		// TODO Auto-generated method stub
		SpannableString msps = new SpannableString(data.content);
		showDoubleWarnDialogs(msps,data.title,data.create_time);
	}
	
	protected void showDoubleWarnDialogs(SpannableString msg,String content,String info) {
		noticedialog = new ButtonDialogStyleNotice(activity,
				R.style.mydialog, "提示", msg,content,info,"我知道了", "我知道了",
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
//						doubleWarnOnClicks(v);
						noticedialog.dismiss();
					}
				});
		noticedialog.setCancelable(false);
		noticedialog.setCanceledOnTouchOutside(false);
		noticedialog.show();
	}

	@Override
	public void onClick(View v) {
		
		Intent intent = new Intent();
		switch(v.getId()) {
		case R.id.ivHomeIM:
			ivIM.setBackgroundResource(R.drawable.ic_im);
			startActivity(new Intent(activity, MessageActivity.class));
			break;

		case R.id.ivHomeCreateQR:
//			startActivity(new Intent(activity, QrCodeActivity.class));
			new QRCodeWindow(activity, UserInfoManager.getUserInfo(activity).shop_id).show();
			break;
			
		case R.id.llMineWallet:
//			intent.setClass(activity, IntegralActivity.class);
//			String integral =Util.getnum(idata.integral, false);
//			String  totalIntegral= Util.getnum(idata.totalIntegral, false);
//			if(integral!=null){
//				intent.putExtra("integral", integral);
//				intent.putExtra("totalIntegral", totalIntegral);
//			}
//			startActivity(intent);
			
			break;
		case R.id.llMineCoupon:
			
			intent.setClass(activity, ExcitationFragment.class);
			String filialPiety =idata.filialPiety+"";
			String  totalFilialPiety= idata.totalFilialPiety+"";
			if(filialPiety!=null){
				intent.putExtra("filialPiety", filialPiety);
				intent.putExtra("totalFilialPiety", totalFilialPiety);
			}
			startActivity(intent);
//			startActivity(new Intent(activity, ExcitationFragment.class));
			break;
		case R.id.llMinePoint:
			
			intent.setClass(activity, IncentivePointsActivity.class);
			String filialMoney =Util.getnum(idata.filialMoney, false);
			if(filialMoney!=null){
				intent.putExtra("filialMoney", filialMoney);
			}
			startActivity(intent);
			
//			startActivity(new Intent(activity, IncentivePointsActivity.class));
			break;
//		case R.id.llHomeAccount:
//			startActivity(new Intent(activity, MyAccountActivity.class));
//			break;
			
//		case R.id.llViewRechargeTotal:
//			Intent it1 = new Intent(activity, ViewTotalReportActivity.class);
//			it1.putExtra("from", FROM.RECHARGE_TOTAL);
//			startActivity(it1);
//			break;
			
//		case R.id.llViewConsumeTotal:
//			Intent it2 = new Intent(activity, ViewTotalReportActivity.class);
//			it2.putExtra("from", FROM.CONSUME_TOTAL);
//			startActivity(it2);
//			break;
			
//		case R.id.llViewPointLog:
//			Intent it3 = new Intent(activity, ViewTotalReportActivity.class);
//			it3.putExtra("from", FROM.POINT_USE);
//			startActivity(it3);
//			break;
			
//		case R.id.llViewCouponUse:
//			Intent it4 = new Intent(activity, ViewTotalReportActivity.class);
//			it4.putExtra("from", FROM.COUPON_USE);
//			startActivity(it4);
//			break;
			
		case R.id.lin_1:
			break;
		case R.id.lin_2:
			startActivity(new Intent(activity, EkOrderPay.class));
			break;
		case R.id.lin_3:
			break;
			
		case R.id.rela_1:
			startActivity(new Intent(activity, MemberManageActivity.class));
			break;
		case R.id.rela_2:
			startActivity(new Intent(activity, EditShopActivity.class));
		
			break;
		case R.id.rela_3:
			startActivity(new Intent(activity, CommodityManageActivity.class));
//			T.showShort(activity, "暂未开通");
			break;
		case R.id.rela_4:
			
//			intent.setClass(activity, ConsumptionSeriesActivity.class);
//			if(bean!=null&&bean.data!=null){
//				String sumTotalConsumption =bean.data.sumTotalConsumption+"";
//				if(sumTotalConsumption!=null){
//					intent.putExtra("sumTotalConsumption", Util.getnum(sumTotalConsumption,true));
//				}
//			}
			intent.setClass(activity, ConsumptionFragmentActivity.class);
			intent.putExtra("tag", "0");
			startActivity(intent);
//			startActivity(new Intent(activity, ConsumptionSeriesActivity.class));
			break;
			
		}
	}

	@Override
	public void newMessage(int which) {
		ivIM.setBackgroundResource(R.drawable.ic_im_notice);		
	}
	
	public void checkVersion() {
		VersionRequest req = new VersionRequest();
		req.client = "2";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_VERSION_INFO, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				final VersionRespone bean = new Gson().fromJson(resp.result, VersionRespone.class);
				Log.e("", ""+resp.result);
				if(Api.SUCCEED == bean.result_code) {
					try {
						if(bean.data.version_code > ApkUtil.getVersionCode(activity)) {
							if(bean.data.status == 1) {
								// 强制升级
//								T.showShort(activity, "此版本为强制升级");
								upgrade(bean.data.version_url, activity.getPackageName()+bean.data.version_no);
							} else {
								// 建议升级
								downloadDialog = new TwoButtonDialog(getActivity(), R.style.CustomDialog,
										"版本升级", "发现新版本："+bean.data.version_no, "取消", "升级",false,new OnMyDialogClickListener() {
											
											@Override
											public void onClick(View v) {
												// TODO Auto-generated method stub
												switch (v.getId()) {
												case R.id.Button_OK:
													downloadDialog.dismiss();
													break;
												case R.id.Button_cancel:
													
													/*****update service*******/
													Intent intent = new Intent(getActivity(),UpdateService.class);
													intent.putExtra("Key_App_Name","ermash"+bean.data.version_no);
													intent.putExtra("Key_Down_Url", bean.data.version_url);						
													getActivity().startService(intent);
//													new UpDate(getActivity(), bean.data.version_url,bean.data.version_no);
//													upgrade(bean.data.version_url, activity.getPackageName()+bean.data.version_no);
													downloadDialog.dismiss();
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
								downloadDialog.setCanceledOnTouchOutside(false);
								
									downloadDialog.show();
								}
//							upgrade(bean.data.version_url, activity.getPackageName()+bean.data.version_no);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}
		});
	}
	
	public void upgrade(String url, String name) {
		
		ProgressDialogUtil.showProgressDlg1(activity, "升级中,请勿退出");
		new HttpUtils().download(url, Environment.getExternalStorageDirectory()+"/"+name,
				new RequestCallBack<File>(){
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ProgressDialogUtil.dismissProgressDlg1();
						T.showShort(activity, "下载失败");
					}

					@Override
					public void onSuccess(ResponseInfo<File> resp) {
						ProgressDialogUtil.dismissProgressDlg1();
						if(resp.statusCode==200 && resp.result!=null) {
							ApkUtil.installAPK(activity, resp.result);
							getActivity().finish();
						}
					}
		});
	}
	

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
	}
	
	
	
	
}
