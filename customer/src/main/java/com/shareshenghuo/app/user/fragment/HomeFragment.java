package com.shareshenghuo.app.user.fragment;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;

import com.baozi.Zxing.CaptureActivity;
import com.shareshenghuo.app.user.BuildConfig;
import com.shareshenghuo.app.user.CityListActivity;
import com.shareshenghuo.app.user.FlashSaleActivity;
import com.shareshenghuo.app.user.LoginActivity;
import com.shareshenghuo.app.user.MainActivity;
import com.shareshenghuo.app.user.MessageActivity;
import com.shareshenghuo.app.user.ProdDetailActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.SearchShopActivity;
import com.shareshenghuo.app.user.ShopDetailActivity;
import com.shareshenghuo.app.user.ShopListActivity;
import com.shareshenghuo.app.user.activity.realname.RealnameStepIDCardActivity;
import com.shareshenghuo.app.user.adapter.CategoryGridAdapter;
import com.shareshenghuo.app.user.adapter.HomeHotAdapter;
import com.shareshenghuo.app.user.adapter.MyPagerAdapter;
import com.shareshenghuo.app.user.adapter.ShopListAdapter;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.manager.CityManager.LocationCallback;
import com.shareshenghuo.app.user.manager.MessageManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.Banner;
import com.shareshenghuo.app.user.network.bean.CategoryInfo;
import com.shareshenghuo.app.user.network.bean.CityInfo;
import com.shareshenghuo.app.user.network.bean.NoticeBean;
import com.shareshenghuo.app.user.network.bean.ProdInfo;
import com.shareshenghuo.app.user.network.bean.ShopInfo;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.network.request.BannerRequest;
import com.shareshenghuo.app.user.network.request.BindBean;
import com.shareshenghuo.app.user.network.request.CategoryRequest;
import com.shareshenghuo.app.user.network.request.HomeDataRequest;
import com.shareshenghuo.app.user.network.request.NumRequest;
import com.shareshenghuo.app.user.network.request.VersionRequest;
import com.shareshenghuo.app.user.network.response.BannerListResponse;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.network.response.CategoryResponse;
import com.shareshenghuo.app.user.network.response.HomeDataResponse;
import com.shareshenghuo.app.user.network.response.NoticeResponse;
import com.shareshenghuo.app.user.network.response.ProdListResponse;
import com.shareshenghuo.app.user.network.response.VersionRespone;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.receiver.NewChatMsgWorker;
import com.shareshenghuo.app.user.receiver.NewChatMsgWorker.NewMessageCallback;
import com.shareshenghuo.app.user.util.ApkUtil;
import com.shareshenghuo.app.user.util.BitmapTool;
import com.shareshenghuo.app.user.util.CommonUtil;
import com.shareshenghuo.app.user.util.PreferenceUtils;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.UpdateService;
import com.shareshenghuo.app.user.util.Utility;
import com.shareshenghuo.app.user.util.ViewUtil;
import com.shareshenghuo.app.user.widget.AdGallery;
import com.shareshenghuo.app.user.widget.AdGallery.OnAdItemClickListener;
import com.shareshenghuo.app.user.widget.AdGalleryHelper;
import com.shareshenghuo.app.user.widget.BannerViewPager;
import com.shareshenghuo.app.user.widget.CountDownTextView;
import com.shareshenghuo.app.user.widget.MyGridView;
import com.shareshenghuo.app.user.widget.SystemBarTintManager;
import com.shareshenghuo.app.user.widget.dialog.ButtonDialogStyleNotice;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.user.widget.dialog.TwoButtonDialog;
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

/**
 * @author hang 首页
 */
public class HomeFragment extends Fragment implements OnClickListener,
		PlatformActionListener, OnItemClickListener, LocationCallback,
		OnAdItemClickListener, NewMessageCallback,
		OnRefreshListener2<ScrollView> {

	private static final int REQ_CHOOSE_CITY = 0x101;
	private static final int REQ_LOGIN = 0x102;

	private Button btnCity;
	private ViewPager vpCategory;
	private BannerViewPager banner;
	private CountDownTextView tvRemainingTime;
	private MyGridView gvHot;
	private ListView lvRecomm;
	private Button btnMsg;
	private CityInfo cityInfos;
	private LinearLayout llSlideDot;
	private List<ImageView> dots;
	private int currentIndex;

	private RelativeLayout adContainer;
	private AdGalleryHelper adGalleryHelper;
	private AdGallery adGallery;

	private CityManager cityManager;

	private NewChatMsgWorker newMsgWatcher;

	private PullToRefreshScrollView refreshscrollview;

	private TwoButtonDialog downloadDialog;

	private View rootView;

	private Activity activity;

	private ButtonDialogStyleNotice noticedialog;

	// @Override
	// protected int getLayoutId() {
	// return R.layout.fragment_home;
	// }
	//
	// @Override
	// protected void init(View rootView) {
	// initWxBind();
	//
	// initView();
	// initData();
	// initListener();
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// if(UserInfoManager.isLogin(getActivity())){
		// getnotice();
		// }

		rootView = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_home, null);
		this.activity = getActivity();

		checkVersion();
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getActivity().getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}

		SystemBarTintManager tintManager = new SystemBarTintManager(
				getActivity());
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.bg_white);
		// if(!PreferenceUtils.getPrefBoolean(getActivity(), "first", false))
		// startActivity(new Intent(getActivity(), GuideActivity.class));
		initView();
		initListener();
		init();
	}

	private void init() {
		if (!PreferenceUtils.getPrefBoolean(getActivity(), "mfirst0", false)) {
			showMask();
		} else {
			getnotice();
		}
		initData();
	}

	public void getnotice() {
		NumRequest req = new NumRequest();
		req.notice_type = "1";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETANNOUNCEMENT, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ProgressDialogUtil.dismissProgressDlg();
						// T.showNetworkError(activity);
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						ProgressDialogUtil.dismissProgressDlg();
						Log.e("", "" + resp.result);
						NoticeResponse bean = new Gson().fromJson(resp.result,
								NoticeResponse.class);
						if (Api.SUCCEED == bean.result_code) {
							if (bean.data != null
									&& bean.data.is_announcement.equals("1")) {
								upnotice(bean.data);
							}
						}
					}
				});
	}

	private void upnotice(NoticeBean data) {
		SpannableString msps = new SpannableString(data.content);
		showDoubleWarnDialogs(msps, data.title, data.create_time);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = LayoutInflater.from(getActivity()).inflate(
					R.layout.fragment_home, null);
		}

		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	public void initView() {
		refreshscrollview = (PullToRefreshScrollView) rootView
				.findViewById(R.id.refreshscrollview);
		refreshscrollview.setOnRefreshListener(this);
		llSlideDot = (LinearLayout) rootView
				.findViewById(R.id.llHomeCategoryDot);
		btnCity = (Button) rootView.findViewById(R.id.btnHomeCity);
		vpCategory = (ViewPager) rootView.findViewById(R.id.vpHomeCategory);
		banner = (BannerViewPager) rootView.findViewById(R.id.bannerHome);
		tvRemainingTime = (CountDownTextView) rootView
				.findViewById(R.id.tvRemainingTime);
		gvHot = (MyGridView) rootView.findViewById(R.id.gvHomeHot);
		lvRecomm = (ListView) rootView.findViewById(R.id.lvHomeRecomm);
		btnMsg = (Button) rootView.findViewById(R.id.btnMessage);
		adContainer = (RelativeLayout) rootView.findViewById(R.id.ad_container);
		// if(UserInfoManager.getUserInfo(getActivity())!=null){
		// Log.e("", ""+UserInfoManager.getUserInfo(getActivity())._id);
		// Log.e("", ""+UserInfoManager.getUserInfo(getActivity()).id);
		// }

	}

	public void realizeFunc(List<Banner> data) {
		if (adGalleryHelper != null) {
			adGalleryHelper = null;
			adContainer.removeAllViews();
			adGallery = null;
		}
		if (activity != null) {
			try {
				adGalleryHelper = new AdGalleryHelper(activity, data, 5000,
						true);
				adContainer.addView(adGalleryHelper.getLayout());
				adGallery = adGalleryHelper.getAdGallery();
				adGallery.setAdOnItemClickListener(this);
			} catch (Exception e) {
			}
		}
	}

	public void initData() {
		if (CommonUtil.checkNetworkConnection(activity)) {
			// 网络可用
			rootView.findViewById(R.id.llNetworkException).setVisibility(
					View.GONE);

			if (cityInfos != null) {
				btnCity.setText(cityInfos.city_name);
				getBannerList();
				getCategoryData(0);
				// getLimitProdList();
				getProdAndShopList();
				return;
			}

			// if(cityInfos==null){
			cityManager = CityManager.getInstance(activity);
			cityManager.setLocationCallback(this);
			ProgressDialogUtil.showProgressDlg(activity, "获取城市数据");
			cityManager.getCityInfo();
			// }

			//

		}

		newMsgWatcher = new NewChatMsgWorker(activity, this);
		newMsgWatcher.startWork();
	}

	// public void initWxBind(){
	// if(!UserInfoManager.isLogin(activity)){
	// return;
	// }
	// // if(UserInfoManager.getWxType(activity).equals("1")){
	// // return;
	// // }
	// if(UserInfoManager.getUserInfo(getActivity()).band_type==0&&downloadDialog==null){
	//
	//
	// downloadDialog = new TwoButtonDialog(getActivity(), R.style.CustomDialog,
	// "温馨提示", "尊敬的会员您好:\n\n     您是第一次注册,建议您绑定您常用的微信号.绑定后,消费可获得积分激励.", "忽略",
	// "去绑定",false,new OnMyDialogClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// switch (v.getId()) {
	// case R.id.Button_OK:
	// UserInfoManager.setWxType(getActivity(), "1");
	// downloadDialog.dismiss();
	// downloadDialog = null;
	// break;
	// case R.id.Button_cancel:
	//
	// auth(ShareSDK.getPlatform(activity, Wechat.NAME));
	// downloadDialog.dismiss();
	// downloadDialog = null;
	// default:
	// break;
	// }
	// }
	// });
	// downloadDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
	// @Override
	// public boolean onKey(DialogInterface dialog, int keyCode,
	// KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_SEARCH) {
	// return true;
	// } else {
	// return true; // 默认返回 false
	// }
	// }
	// });
	// // downloadDialog.setCanceledOnTouchOutside(false);
	//
	// downloadDialog.show();
	// }
	//
	//
	// }

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		if (UserInfoManager.getUserInfo(getActivity()) != null
				&& UserInfoManager.getUserInfo(getActivity()).certification_status != null) {

			if (UserInfoManager.getUserInfo(getActivity()).certification_status
					.equals("0")) {

				if (UserInfoManager.getWxType(activity).equals("0")) {
					initrename("为保证您的资金安全\n在交易前需进行实名认证");
					// return;
				}

			} else if (UserInfoManager.getUserInfo(getActivity()).certification_status
					.equals("1")) {
				// initrename("您的实名认证未通过\n重新认证?");
			}

		}

		// initWxBind();

		if (downloadDialog != null && downloadDialog.isShowing()) {
			ProgressDialogUtil.dismissProgressDlg();
		}

	}

	public void auth(Platform platform) {
		ProgressDialogUtil.showProgressDlg(activity, "请稍后");
		platform.setPlatformActionListener(this);
		platform.showUser(null);
	}

	public void initListener() {
		btnCity.setOnClickListener(this);
		rootView.findViewById(R.id.btnSearch).setOnClickListener(this);
		btnMsg.setOnClickListener(this);
		rootView.findViewById(R.id.llHomeFlashSale).setOnClickListener(this);
		gvHot.setOnItemClickListener(this);
		lvRecomm.setOnItemClickListener(this);
		rootView.findViewById(R.id.btnQRcode).setOnClickListener(this);
		rootView.findViewById(R.id.llHomeActiv).setOnClickListener(this);
		rootView.findViewById(R.id.llHomePointShop).setOnClickListener(this);
		rootView.findViewById(R.id.re_masking).setOnClickListener(this);

	}

	@Override
	public void onPause() {
		// banner.stopRoll();
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		// banner.startRoll();

		btnMsg.setBackgroundResource(MessageManager.getUnreadCount() > 0 ? R.drawable.btn_msg_new
				: R.drawable.btn_msg);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tvRemainingTime.stopCountDown();
		newMsgWatcher.stopWork();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnMessage:
			btnMsg.setBackgroundResource(R.drawable.btn_msg);
			startActivity(new Intent(activity, MessageActivity.class));
			break;

		case R.id.llHomeFlashSale:
			startActivity(new Intent(activity, FlashSaleActivity.class));
			break;
		case R.id.re_masking:
			if (!UserInfoManager.isLogin(activity)) {
				T.showShort(activity, "您当前未登录，请先登录");
				startActivityForResult(
						new Intent(activity, LoginActivity.class), REQ_LOGIN);
				return;
			}
			activity.startActivityForResult(new Intent(activity,
					CaptureActivity.class), CaptureActivity.REQ_SCAN_QR);
			break;

		case R.id.btnHomeCity:

			// 由于webView.postUrl(url, postData)中 postData类型为byte[] ，
			// 通过EncodingUtils.getBytes(data, charset)方法进行转换

			// Intent it = new Intent(activity, WebLoadActivity.class);
			// it.putExtra("title", "商城");
			// it.putExtra("url", createWebForm(new HashMap<String, Object>()));
			// startActivity(it);

			startActivityForResult(
					new Intent(activity, CityListActivity.class),
					REQ_CHOOSE_CITY);
			break;

		case R.id.btnSearch:
			startActivity(new Intent(activity, SearchShopActivity.class));
			MainActivity m = (MainActivity) HomeFragment.this.getActivity();
			m.fTabHost.setCurrentTab(1);
			break;

		case R.id.btnQRcode:
			if (!UserInfoManager.isLogin(activity)) {
				T.showShort(activity, "您当前未登录，请先登录");
				startActivityForResult(
						new Intent(activity, LoginActivity.class), REQ_LOGIN);
				return;
			}

			activity.startActivityForResult(new Intent(activity,
					CaptureActivity.class), CaptureActivity.REQ_SCAN_QR);
			// activity.startActivity(new Intent(activity,
			// CaptureActivity1.class));
			break;

		case R.id.llHomeActiv:
			// startActivity(new Intent(activity, ActivListActivity.class));
			break;

		case R.id.llHomePointShop:
			// StringBuilder sb = new StringBuilder(Api.URL_POINT_SHOP);
			// sb.append("?city_id=").append(CityManager.getInstance(activity).getCityId())
			// .append("&user_id=").append(UserInfoManager.getUserId(activity));
			// Intent it = new Intent(activity, WebLoadActivity.class);
			// it.putExtra("title", "积分商城");
			// it.putExtra("url", sb.toString());
			// startActivity(it);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1,
			int position, long id) {
		if (adapterView == gvHot) {
			ProdInfo item = (ProdInfo) adapterView.getItemAtPosition(position);
			Intent it = new Intent(activity, ProdDetailActivity.class);
			it.putExtra("prodInfo", item);
			startActivity(it);
		} else if (adapterView == lvRecomm) {
			if (UserInfoManager.isLogin(activity)) {
				ShopInfo item = (ShopInfo) adapterView
						.getItemAtPosition(position);
				Intent it = new Intent(activity, ShopDetailActivity.class);
				it.putExtra("shopId", item.id);
				startActivity(new Intent(it));
			} else {
				startActivity(new Intent(activity, LoginActivity.class));
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case REQ_CHOOSE_CITY:
			case REQ_LOGIN:
				MainActivity activity = (MainActivity) HomeFragment.this
						.getActivity();
				activity.positions = 0;
				cityInfos = null;
				initData();
				// initWxBind();
				break;
			}
		}
	}

	@Override
	public void getCity(CityInfo cityInfo) {

		ProgressDialogUtil.dismissProgressDlg();
		if (cityInfo == null && activity != null) {

			T.showLong(activity, "获取城市数据失败,请检查网络！");
			return;
		}
		cityInfos = cityInfo;
		// if(cityInfo = null && activity != null){
		getCategoryData(0);
		// getLimitProdList();
		getProdAndShopList();
		getBannerList();

		// }

		btnCity.setText(cityInfo.city_name);

	}

	public void getBannerList() {
		BannerRequest req = new BannerRequest();
        if (BuildConfig.isApply) {
            req.city_id = "38";
        } else {
		    req.city_id = cityManager.getCityId() + "";
        }
		req.banner_place = "1";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_BANNER_LIST, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						if (resp.result != null) {
							BannerListResponse bean = new Gson().fromJson(
									resp.result, BannerListResponse.class);

							if (bean.data != null && bean.data.size() > 0) {
								// banner.createView(bean.data);
								realizeFunc(bean.data);
								// banner.startRoll();
								// banner.setLayoutWidthHeight(0.5);
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
				});
	}

	public void getCategoryData(final int parentId) {
		// ProgressDialogUtil.showProgressDlg(activity, "加载数据");
		CategoryRequest req = new CategoryRequest();
		req.parent_id = parentId + "";
        if (BuildConfig.isApply) {
            req.city_id = "38";
        } else {
            req.city_id = cityManager.getCityId() + "";
        }
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_CATEGORY_LIST, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> resp) {

						ProgressDialogUtil.dismissProgressDlg();
						CategoryResponse bean = new Gson().fromJson(
								resp.result, CategoryResponse.class);
						if (Api.SUCCEED == bean.result_code) {
							updateParentCategory(bean.data);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ProgressDialogUtil.dismissProgressDlg();
					}
				});
	}

	private int viewCount;

	public void updateParentCategory(List<CategoryInfo> data) {
		if (data == null || activity == null)
			return;

		List<View> views = new ArrayList<View>();
		View v = null;
		List<CategoryInfo> list = null;
		for (int i = 0; i < data.size(); i++) {
			if (i % 8 == 0) {
				list = new ArrayList<CategoryInfo>();
				v = LayoutInflater.from(activity).inflate(
						R.layout.gridview_category, null);
				views.add(v);
				((MyGridView) v).setAdapter(new CategoryGridAdapter(activity,
						list));
				((MyGridView) v)
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View arg1, int position, long arg3) {
								CategoryInfo item = ((CategoryGridAdapter) parent
										.getAdapter()).getItem(position);
								if (item.child_shop_type_list == null
										|| item.child_shop_type_list.size() <= 0) {
									// 直接打开商家列表
									Intent it = new Intent(activity,
											ShopListActivity.class);
									it.putExtra("shop_type_id", item.id);
									startActivity(it);
								} else {
									// 显示二级分类
									MainActivity activity = (MainActivity) HomeFragment.this
											.getActivity();
									activity.shop_type_id = item.id + "";
									activity.shop_type_name = item.type_name;
									activity.positions = (int) arg3 + 1;
									activity.tag = true;
									if (activity.item != null) {
										activity.item.clear();
										activity.item
												.addAll(item.child_shop_type_list);
									} else {
										activity.item = new ArrayList<CategoryInfo>();
										activity.item
												.addAll(item.child_shop_type_list);
									}
									activity.fTabHost.setCurrentTab(1);

									// new CategoryWindow(activity,
									// item).show();
								}
							}
						});
			}
			list.add(data.get(i));
		}

		viewCount = views.size();
		// 初始化小圆点
		llSlideDot.removeAllViews();
		if (viewCount > 0) {
			dots = new ArrayList<ImageView>();
			for (int i = 0; i < viewCount; i++) {
				ImageView dot = new ImageView(activity);
				dot.setBackgroundResource(R.drawable.gray_circle);
				int side = BitmapTool.dp2px(activity, 5);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						side, side);
				params.setMargins(5, 5, 5, 5);
				dot.setLayoutParams(params);
				dots.add(dot);
				llSlideDot.addView(dot);
			}
			dots.get(0).setBackgroundResource(R.drawable.gree_circle);
		}

		vpCategory.setAdapter(new MyPagerAdapter(views));
		vpCategory.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int selectedIndex) {
				dots.get(selectedIndex).setBackgroundResource(
						R.drawable.gree_circle);
				dots.get(currentIndex).setBackgroundResource(
						R.drawable.gray_circle);
				currentIndex = selectedIndex;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	/**
	 * 获取限购商品列表
	 */
	public void getLimitProdList() {
		HomeDataRequest req = new HomeDataRequest();
		req.city_id = cityManager.getCityId() + "";
		req.latitude = cityManager.latitude + "";
		req.longitude = cityManager.longitude + "";
		req.page_no = "1";
		req.page_size = "1";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_FLASH_SALE_PROD, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						ProdListResponse bean = new Gson().fromJson(
								resp.result, ProdListResponse.class);
						if (bean.data != null && bean.data.size() > 0) {
							ProdInfo prodInfo = bean.data.get(0);
							tvRemainingTime
									.startCountDown(prodInfo.current_time);
							rootView.findViewById(R.id.llHomeFlashSale)
									.setVisibility(View.VISIBLE);

							((TextView) rootView
									.findViewById(R.id.tvLimitProdTitle))
									.setText(prodInfo.product_name);
							((TextView) rootView
									.findViewById(R.id.tvLimitProdNewPrice))
									.setText("¥" + prodInfo.default_new_price);
							// setText(R.id.tvLimitProdTitle,
							// prodInfo.product_name);
							// setText(R.id.tvLimitProdNewPrice,
							// "¥"+prodInfo.default_new_price);
							TextView tvOldPrice = (TextView) rootView
									.findViewById(R.id.tvLimitProdOldPrice);
							tvOldPrice
									.setText("¥" + prodInfo.default_old_price);
							tvOldPrice
									.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG
											| Paint.ANTI_ALIAS_FLAG);
						} else {
							// setText(R.id.tvLimitProdTitle, "未开始");
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
				});
	}

	/**
	 * 获取热门商品和人气好店数据
	 */
	public void getProdAndShopList() {
		HomeDataRequest req = new HomeDataRequest();
		if (BuildConfig.isApply) {
			req.city_id = "38";
		} else {
			req.city_id = cityManager.getCityId() + "";
		}
		req.latitude = cityManager.latitude + "";
		req.longitude = cityManager.longitude + "";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_HOME_PROD_SHOP, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						refreshscrollview.onRefreshComplete();
						HomeDataResponse bean = new Gson().fromJson(
								resp.result, HomeDataResponse.class);
						if (bean.data != null) {
							if (bean.data.product_list_by_hot != null
									&& bean.data.product_list_by_hot.size() > 0) {
								rootView.findViewById(R.id.llHomeHot)
										.setVisibility(View.VISIBLE);
								gvHot.setAdapter(new HomeHotAdapter(activity,
										bean.data.product_list_by_hot));
							} else {
								rootView.findViewById(R.id.llHomeHot)
										.setVisibility(View.GONE);
							}
						}
						if (bean.data != null
								&& bean.data.shop_list_by_recomment != null
								&& bean.data.shop_list_by_recomment.size() > 0) {
							rootView.findViewById(R.id.llHomeRecomm)
									.setVisibility(View.VISIBLE);
							lvRecomm.setAdapter(new ShopListAdapter(activity,
									bean.data.shop_list_by_recomment));
							Utility.setListViewHeightBasedOnChildren(lvRecomm);
						} else {
							rootView.findViewById(R.id.llHomeRecomm)
									.setVisibility(View.GONE);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						refreshscrollview.onRefreshComplete();
					}
				});
	}
	@Override
	public void newMessage(int which) {
		btnMsg.setBackgroundResource(R.drawable.btn_msg_new);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		cityInfos = null;
		initData();

		// new Handler().postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// refreshscrollview.onRefreshComplete();
		// }
		// }, 2000);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub

	}

	/**
	 * 下面是授权登录
	 */
	@Override
	public void onCancel(Platform platform, int arg1) {
		// TODO Auto-generated method stub
		T.showShort(activity, "授权失败");
		ProgressDialogUtil.dismissProgressDlg();
		platform.removeAccount();
	}

	@Override
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		if (action == Platform.ACTION_AUTHORIZING
				|| action == Platform.ACTION_USER_INFOR) {
			if (platform.isValid()) {
				PlatformDb db = platform.getDb();
				String name = "";
				String id = db.getUserId();
				name = db.getUserName();
				String avatar = db.getUserIcon();
				String unionid = db.get("unionid");
				if (name == null || name.equals("")) {
					name = db.get("nickname");
				}
				Message msg = new Message();
				Bundle b = new Bundle();

				b.putString("id", id);
				b.putString("name", name);
				b.putString("avatar", avatar);
				b.putString("unionid", unionid);
				msg.setData(b);
				handler.sendMessage(msg);
			}
		}

		ProgressDialogUtil.dismissProgressDlg();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();

			BInd(b.getString("id"), b.getString("name"), b.getString("avatar"),
					b.getString("unionid"));
		}
	};

	/**
	 * 第三方登录
	 */
	public void BInd(String id, String name, String avatar, String unionid) {
		ProgressDialogUtil.showProgressDlg(activity, "登录中");
		BindBean req = new BindBean();
		req.band_type = "2";
		req.band_id = id;
		req.band_unionid = unionid;
		req.nick_name = name;
		if (avatar == null || avatar.equals("")) {
			req.user_photo = "";
		} else {
			req.user_photo = avatar;
		}
		req.user_id = UserInfoManager.getUserInfo(getActivity()).id;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(req.toJson());
		new HttpUtils().send(HttpMethod.POST, Api.USERBAND, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						ProgressDialogUtil.dismissProgressDlg();
						BaseResponse bean = new Gson().fromJson(resp.result,
								BaseResponse.class);
						if (Api.SUCCEED == bean.result_code) {
							// UserInfoManager.setLoginType(activity,
							// loginType);
							// ((LoginActivity)activity).login(bean.data);
							UserInfo u = UserInfoManager
									.getUserInfo(getActivity());
							u.band_type = 2;
							T.showShort(activity, "绑定成功");
							ProgressDialogUtil.dismissProgressDlg();
						} else {
							T.showShort(activity, bean.result_desc);
							ProgressDialogUtil.dismissProgressDlg();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ProgressDialogUtil.dismissProgressDlg();
						// T.showNetworkError(activity);
					}
				});
	}

	@Override
	public void onError(Platform platform, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		T.showShort(activity, "授权失败");
		platform.removeAccount();
	}

	@Override
	public void setItemClick(int position) {
		// TODO Auto-generated method stub

	}

	public void initrename(String content) {

		if (downloadDialog != null && downloadDialog.isShowing()) {
			return;
		}
		if (UserInfoManager.getWxType(activity).equals("1")) {
			return;
		}
		Log.e("", "getWxType = = = " + UserInfoManager.getWxType(activity));
		downloadDialog = new TwoButtonDialog(getActivity(),
				R.style.CustomDialog, " 尊敬的会员", content, " 取消", "开始认证", true,
				new OnMyDialogClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							downloadDialog = null;
							UserInfoManager.setWxType(activity, "1");
							break;
						case R.id.Button_cancel:
							startActivity(new Intent(activity,
                                    RealnameStepIDCardActivity.class));
							downloadDialog.dismiss();
							downloadDialog = null;
							UserInfoManager.setWxType(activity, "1");
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

	protected void showDoubleWarnDialogs(SpannableString msg, String content,
			String info) {
		noticedialog = new ButtonDialogStyleNotice(activity, R.style.mydialog,
				"提示", msg, content, info, "我知道了", "我知道了",
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doubleWarnOnClicks(v);
					}
				});
		noticedialog.setCancelable(false);
		noticedialog.setCanceledOnTouchOutside(false);

		// LayoutParams lay = noticedialog.getWindow().getAttributes();
		// DisplayMetrics dm = new DisplayMetrics();
		// activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		// Rect rect = new Rect();
		// View view = activity.getWindow().getDecorView();
		// view.getWindowVisibleDisplayFrame(rect);
		// lay.height = dm.heightPixels - rect.top;
		// lay.width = dm.widthPixels;

		noticedialog.show();

	}

	protected void doubleWarnOnClicks(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
			noticedialog.dismiss();
			break;
		case R.id.btn_right:
			noticedialog.dismiss();
			break;
		default:
			break;
		}
	}

	private void showMask() {
		int statusBarHeight1 = 0;
		// 获取status_bar_height资源的ID
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			// 根据资源ID获取响应的尺寸值
			statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
		}

		ViewUtil.showtip(activity, rootView.findViewById(R.id.re_masking), 0,
				statusBarHeight1, "mfirst0", R.drawable.tip1, null);

		// mBtMasking = (LinearLayout) rootView.findViewById(R.id.re_masking);
		// mBtMasking.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// // runOnUiThread(new Runnable() {
		// // @Override
		// // public void run() {
		// mHeight = statusBarHeight1;
		// int hight = mBtMasking.getHeight()+mHeight;
		// int left = mBtMasking.getLeft()-15;
		// int right = mBtMasking.getRight()+15;
		// int top = mBtMasking.getTop()+mHeight;
		// int bottom = mBtMasking.getBottom()+mHeight;
		// int loc[] = {left,top,right,bottom,hight,R.drawable.tip1,0};
		// Intent intent = new Intent(activity,TipsActivity.class);
		// intent.putExtra("loc",loc);
		// startActivity(intent);
		// // }
		// // });
		// }
		// },500);
	}

	public void checkVersion() {
		VersionRequest req = new VersionRequest();
		req.client = "1";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_VERSION_INFO, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						final VersionRespone bean = new Gson().fromJson(
								resp.result, VersionRespone.class);
						if (Api.SUCCEED == bean.result_code) {
							try {
								if (bean.data.version_code > ApkUtil
										.getVersionCode(getActivity())) {
									if (bean.data.status == 1) {
										System.out.println("=====版本升级地址："
												+ bean.data.version_url);
										// 强制升级
										upgrade(bean.data.version_url,
												getActivity().getPackageName()
														+ bean.data.version_no);

									} else {
										// 建议升级

										downloadDialog = new TwoButtonDialog(
												getActivity(),
												R.style.CustomDialog,
												"版本升级",
												"发现新版本：" + bean.data.version_no,
												"取消", "升级", true,
												new OnMyDialogClickListener() {

													@Override
													public void onClick(View v) {
														// TODO Auto-generated
														// method stub
														switch (v.getId()) {
														case R.id.Button_OK:
															downloadDialog
																	.dismiss();
															break;
														case R.id.Button_cancel:

															/***** update service *******/
															Intent intent = new Intent(
																	getActivity(),
																	UpdateService.class);
															intent.putExtra(
																	"Key_App_Name",
																	"ermagy"
																			+ bean.data.version_no);
															intent.putExtra(
																	"Key_Down_Url",
																	bean.data.version_url);
															getActivity()
																	.startService(
																			intent);
															// new
															// UpDate(MainActivity.this,
															// bean.data.version_url,bean.data.version_no);
															// upgrade(bean.data.version_url,
															// MainActivity.this.getPackageName()+bean.data.version_no);
															downloadDialog
																	.dismiss();
														default:
															break;
														}
													}
												});
										downloadDialog
												.setOnKeyListener(new DialogInterface.OnKeyListener() {
													@Override
													public boolean onKey(
															DialogInterface dialog,
															int keyCode,
															KeyEvent event) {
														if (keyCode == KeyEvent.KEYCODE_SEARCH) {
															return true;
														} else {
															return true; // 默认返回
																			// false
														}
													}
												});
										downloadDialog
												.setCanceledOnTouchOutside(false);

										downloadDialog.show();
									}

									// new
									// AlertDialog.Builder(MainActivity.this).setTitle("发现新版本："+bean.data.version_no)
									// .setMessage(bean.data.update_content)
									// .setPositiveButton("取消", null)
									// .setNegativeButton("升级", new
									// DialogInterface.OnClickListener() {
									// @Override
									// public void onClick(DialogInterface
									// dialog, int which) {
									// upgrade(bean.data.version_url,
									// getPackageName()+bean.data.version_no);
									// }
									// }).show();
								} else {
									// init();
								}
							} catch (Exception e) {
								e.printStackTrace();
								init();
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						init();
					}
				});
	}

	public void upgrade(String url, String name) {
		ProgressDialogUtil.showProgressDlg1(getActivity(), "升级中,请勿退出");
		new HttpUtils().download(url, Environment.getExternalStorageDirectory()
				+ "/" + name, new RequestCallBack<File>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg1();
				T.showShort(getActivity(), "下载失败");
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
//				T.showShort(getActivity(), "===升级状态："+current + "," + total);
//				System.out.println("====升级状态："+current + "," + total);
				super.onLoading(total, current, isUploading);

			}

			@Override
			public void onSuccess(ResponseInfo<File> resp) {
				ProgressDialogUtil.dismissProgressDlg1();
				if (resp.statusCode == 200 && resp.result != null) {
					ApkUtil.installAPK(getActivity(), resp.result);
					getActivity().finish();
				}
			}
		});
	}
}
