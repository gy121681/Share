package com.shareshenghuo.app.user.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shareshenghuo.app.user.BuildConfig;
import com.shareshenghuo.app.user.LoginActivity;
import com.shareshenghuo.app.user.MainActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.ShopDetailActivity;
import com.shareshenghuo.app.user.adapter.ShopListAdapter;
import com.shareshenghuo.app.user.adapter.ShopListHotAdapter;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.AreaInfo;
import com.shareshenghuo.app.user.network.bean.AreasBean;
import com.shareshenghuo.app.user.network.bean.CategoryInfo;
import com.shareshenghuo.app.user.network.bean.ScreenBean;
import com.shareshenghuo.app.user.network.bean.SearchBean;
import com.shareshenghuo.app.user.network.bean.ShopInfo;
import com.shareshenghuo.app.user.network.request.AreaListRequest;
import com.shareshenghuo.app.user.network.request.BannerRequest;
import com.shareshenghuo.app.user.network.request.CategoryRequest;
import com.shareshenghuo.app.user.network.request.ShopListRequest;
import com.shareshenghuo.app.user.network.response.AreaListResponse;
import com.shareshenghuo.app.user.network.response.BannerListResponse;
import com.shareshenghuo.app.user.network.response.CategoryResponse;
import com.shareshenghuo.app.user.network.response.ScreenResponse;
import com.shareshenghuo.app.user.network.response.ShopListResponse1;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.Util;
import com.shareshenghuo.app.user.widget.BannerViewPager;
import com.shareshenghuo.app.user.widget.ListViewForScrollView;
import com.shareshenghuo.app.user.widget.MyGridView;
import com.shareshenghuo.app.user.widget.dialog.CustomMultiChoiceDialog;
import com.shareshenghuo.app.user.widget.dialog.GridDialog;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener1;
import com.shareshenghuo.app.user.widget.dialog.ShopTypeWindow;
import com.shareshenghuo.app.user.widget.dialog.ShopTypeWindow.PickTypeCallback;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView.OnScrollListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView.OnScrollChangedListeners;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author hang
 * 商户列表
 */
@SuppressLint("NewApi") 
public class ShopListFragment extends Fragment
	implements OnScrollListener,OnRefreshListener2<ScrollView>, OnItemClickListener, OnClickListener, PickTypeCallback,OnKeyListener,OnScrollChangedListeners{
	
	private RelativeLayout rlBannerContainer,resreach;
	private BannerViewPager banner;
	private Button btnShopType,btnArea,btnSort,btnArea1,btnSort1,btnShopType1;
	private ShopTypeWindow shopTypeWindow;
	
	private ListViewForScrollView lvData;
	
	private ShopListAdapter adapter;
	
	private CityManager cityManager;
	
	private CustomMultiChoiceDialog.Builder multiChoiceDialogBuilder;
	private List<ShopInfo> data ;
	public 	List<ScreenBean> data3;
	private int pageNo = 1;
	private int pageSize = 20;
	
	public int shopTypeId;				//商户分类id
	public int order_type = 1;			//排序 1距离 2好评 3人气 4关注
//	public int is_authentication = 0;	//是否认证 （可选）
	public int is_integrity = 0;			//是否诚信商家 （可选
	public int is_muslim = 0;			//是否清真 （可选
	public int is_push = 0;				//是否派送（可选
	public int is_recommend = 0;			//是否推荐（可选
	public int is_have_coupon = 0;		//是否有优惠劵（可选
	public int is_have_active = 0;		//是否有活动（可选
	public int is_consumption;
	
	public int is_discount_type_one = 0;
	public int is_discount_type_two = 0;
	public int is_discount_type_three= 0;
	
	public int area_id = -1;				//区域（可选
	private boolean[] boos;
	
	private View rootView;
	
	private Activity activity;
	
	public  MainActivity mactivity ;
	
	private EditText btnSearch;
	private TextView tv_ceash,tv_search;
	private String search_name = "";
	private int positions = -1,positions2 = 0;
	private int childposition =  -1;
	private CustomMultiChoiceDialog multiChoiceDialog;
	private PullToRefreshScrollView refreshscrollview;
	private MyGridView gvHot;
	private ShopListHotAdapter gridadapter;
	private List<CategoryInfo> gridlist;
	private LinearLayout llayout;
	
	private LinearLayout mBuyLayout,llayout1;
	private WindowManager mWindowManager;
	public List<AreaInfo> data2;
	
	private int statusBarHeight1 = -1;  
	/**
	 * 手机屏幕宽度
	 */
	private int screenWidth;
	/**
	 * 悬浮框View
	 */
	private  View suspendView;
	/**
	 * 悬浮框的参数
	 */
	private  WindowManager.LayoutParams suspendLayoutParams;
	/**
	 * 购买布局的高度
	 */
	private int buyLayoutHeight;
	/**
	 * myScrollView与其父类布局的顶部距离
	 */
	private int myScrollViewTop;

	/**
	 * 购买布局与其父类布局的顶部距离
	 */
	private int buyLayoutTop;
	private boolean istag = true;
	private int height;
	
	
//	@Override
//	protected int getLayoutId() {
//		return R.layout.fragment_shop_list;
//	}
//
//	@Override
//	protected void init(View rootView) {
//		shopTypeId = activity.getIntent().getIntExtra("shop_type_id", 0);
//		shopTypeWindow = null;
//		initView(rootView);
//		onPullDownToRefresh(lvData);
//		getBannerList();
//	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_shop_list, null);
		this.activity = getActivity();
		mactivity =(MainActivity) ShopListFragment.this.getActivity();
//		shopTypeId = activity.getIntent().getIntExtra("shop_type_id", 0);
		
		shopTypeWindow = null;
		initView(rootView);
		
		if(mactivity.positions<=0){
			onPullDownToRefresh(refreshscrollview);
		}
		refreshscrollview.setOnScrollchangeListeners(this);
		getBannerList();
//		refreshscrollview.setOnScrollChangeListener(new OnScrollChangeListener() {
//
//				@Override
//				public void onScrollChange(View arg0, int l, int t,int oldl, int oldt) {
//						// TODO Auto-generated method stub
//						if (oldt > t && oldt - t > 0.1) {// 向下
//							llayout.setVisibility(View.VISIBLE);
//							removeSuspend();
//						} else if (oldt < t && t - oldt > 0.1) {// 向上
//						}
//					}
//				});
		
	}
	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
//		mactivity =(MainActivity) ShopListFragment.this.getActivity();
//		shopTypeId = activity.getIntent().getIntExtra("shop_type_id", 0);
//		shopTypeWindow = null;
//		initView(rootView);
		if(!TextUtils.isEmpty(mactivity.shop_type_name)){
			btnShopType.setText(mactivity.shop_type_name);
		}
//		if(CityManager.getInstance(activity)!=null&&CityManager.getInstance(activity).cityInfo!=null&&CityManager.getInstance(activity).cityInfo.city_name!=null){
//			String aa = CityManager.getInstance(activity).cityInfo.city_name;
//			if(TextUtils.isEmpty(aa)){
//				btnArea.setText("全城");
//			}else{
//				btnArea.setText(aa);
//			}
//		}else{
			btnArea.setText("附近");
//		}

		search_name = MainActivity.name;
		if(!TextUtils.isEmpty(search_name)){
			btnSearch.setText(search_name);
			pageNo = 1;
			loadData();
		}
		
		if(gridlist==null){
			gridlist =  new ArrayList<CategoryInfo>();
			gridadapter = new ShopListHotAdapter(activity, gridlist);
			gvHot.setAdapter(gridadapter);
		}
		
		if(mactivity.positions>0){
			if(mactivity.tag){
			shopTypeId = 0;
			gvHot.setVisibility(View.VISIBLE);
			onPullDownToRefresh(refreshscrollview);
			if(mactivity.item!=null){
//				if(mactivity.item.size()>=8){
					List<CategoryInfo> list = new ArrayList<CategoryInfo>();
					for (int i = 0; i < mactivity.item.size(); i++) {
						if(mactivity.item.get(i).is_hot!=null&&mactivity.item.get(i).is_hot.equals("1")){
							list.add(mactivity.item.get(i));
						}
						
					}
					gridlist.clear();
					gridlist.addAll(list);
					gridadapter.notifyDataSetChanged();
//				}else{
//					
//					gridlist.clear();
//					gridlist.addAll(mactivity.item);
//					gridadapter.notifyDataSetChanged();
//					}
					if(gridlist.size()<=0){
						gvHot.setVisibility(View.GONE);
					}
				}
			mactivity.tag = false;
			}
		}else {
			gvHot.setVisibility(View.GONE);
		}
		
		onscroll();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		
		if (rootView == null) {
			rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_shop_list, null);
		}

		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	public void initView(View rootView) {
		resreach = (RelativeLayout) rootView.findViewById(R.id.resreach);
		rlBannerContainer = (RelativeLayout) rootView.findViewById(R.id.rlBannerContainer);
		banner = (BannerViewPager) rootView.findViewById(R.id.bannerShop);
		btnShopType = (Button) rootView.findViewById(R.id.btnShopType);
		gvHot = (MyGridView) rootView.findViewById(R.id.gvHomeHot);
		llayout = (LinearLayout) rootView.findViewById(R.id.llayout);
		lvData = (ListViewForScrollView) rootView.findViewById(R.id.lvShop);
		refreshscrollview = (PullToRefreshScrollView) rootView.findViewById(R.id.refreshscrollview);
		refreshscrollview.setOnRefreshListener(this);
		refreshscrollview.setMode(Mode.BOTH);
		lvData.setOnItemClickListener(this);
		refreshscrollview.setOnScrollListener(this);
		
		cityManager = CityManager.getInstance(getActivity());
		
		btnSearch = (EditText) rootView.findViewById(R.id.btnSearch);

		
		btnSearch.setOnKeyListener(this);
		tv_ceash = (TextView) rootView.findViewById(R.id.tv_ceash);
		tv_ceash.setOnClickListener(this);
		tv_search = (TextView) rootView.findViewById(R.id.tv_search);
		tv_search.setOnClickListener(this);
		rootView.findViewById(R.id.ivBack).setOnClickListener(this);
		rootView.findViewById(R.id.ivCloseBanner).setOnClickListener(this);
		rootView.findViewById(R.id.btnArea).setOnClickListener(this);
		rootView.findViewById(R.id.btnSort).setOnClickListener(this);
		rootView.findViewById(R.id.btnFilter).setOnClickListener(this);
		btnSort = (Button) rootView.findViewById(R.id.btnSort);
		btnArea = (Button) rootView.findViewById(R.id.btnArea);
		btnShopType.setOnClickListener(this);
		
        mWindowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);  
        mBuyLayout = (LinearLayout) rootView.findViewById(R.id.buy);
        llayout1 = (LinearLayout) rootView.findViewById(R.id.llayout1);

		if(shopTypeId == 0) {
			rootView.findViewById(R.id.ivBack).setVisibility(View.GONE);
			rootView.findViewById(R.id.statusBar).setVisibility(View.VISIBLE);
		}
//		btnSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View arg0, boolean arg1) {
//				// TODO Auto-generated method stub
//				if(arg1){
//					if(tv_ceash.getVisibility()==View.GONE){
//						tv_ceash.setVisibility(View.VISIBLE);
//					}
//					if(tv_search.getVisibility()==View.GONE){
//						tv_search.setVisibility(View.VISIBLE);
//					}
//				}
//			}
//		});
		btnSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					tv_search.setVisibility(View.VISIBLE);
					tv_ceash.setVisibility(View.VISIBLE);
				}else{
					if(TextUtils.isEmpty(search_name)){
						tv_ceash.setVisibility(View.GONE);
					}
					tv_search.setVisibility(View.GONE);
					
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
		
		
//		btnSearch.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
////				if(tv_ceash.getVisibility()==View.GONE){
////					tv_ceash.setVisibility(View.VISIBLE);
////				}
//				if(tv_search.getVisibility()==View.GONE){
//					tv_search.setVisibility(View.VISIBLE);
//				}
//			}
//		});
		
		gvHot.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				shopTypeId = gridlist.get((int)arg3).id;
				onPullDownToRefresh(null);	
			}
		});
		
		 /** 
		 * 获取状态栏高度——方法1 
		 * */  

		//获取status_bar_height资源的ID  
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");  
		if (resourceId > 0) {  
		    //根据资源ID获取响应的尺寸值  
		    statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);  
		}  
		
	}
	
	public void loadData() {
		String TypeId = "";
		if(shopTypeId==0){
			TypeId = "";
		}else{
			TypeId = shopTypeId+"";
		}
//		ProgressDialogUtil.showProgressDlg(activity, "请稍候");
		ShopListRequest req = new ShopListRequest();
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		if (BuildConfig.isApply) {
			req.city_id = "38";
		} else {
			req.city_id = cityManager.getCityId()+"";
        }
		req.latitude = cityManager.latitude+"";
		req.longitude = cityManager.longitude+"";
		req.shop_type_id = mactivity.shop_type_id;
		req.search_name = search_name;
		req.shop_child_type_id = TypeId;//
		req.order_type = order_type+"";
		
		if(is_consumption!=-1){
			req.is_consumption = is_consumption+"";//再消费
		}
//		if(is_authentication != -1)
//			req.is_authentication = is_authentication+"";
		if(is_integrity != -1)
			req.is_integrity = is_integrity+"";
		if(is_muslim != -1)
			req.is_muslim = is_muslim+"";
		if(is_push != -1)
			req.is_push = is_push+"";
		if(is_recommend != -1)
			req.is_recommend = is_recommend+"";
		if(is_have_coupon != -1)
			req.is_have_coupon = is_have_coupon+"";
		if(is_have_active != -1)
			req.is_have_active = is_have_active+"";
		if(area_id != -1)
			req.area_id = area_id+"";
		
		req.is_discount_type_one = is_discount_type_one+"";
		req.is_discount_type_two = is_discount_type_two+"";
		req.is_discount_type_three = is_discount_type_three+"";
		
		Log.e("", " = = =   "+req.toString());
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETSHOPLISTNEW, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
				refreshscrollview.onRefreshComplete();
				if(getActivity() != null)
					T.showNetworkError(getActivity());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
				refreshscrollview.onRefreshComplete();
				if(getActivity() != null) {
					ShopListResponse1 bean = new Gson().fromJson(resp.result, ShopListResponse1.class);
					if(Api.SUCCEED == bean.result_code) {
//						data = bean.data.shop_list;
						Log.e("", ""+resp.result);
						updateView(bean.data.shop_list,bean.data.search);
//						Utility.setListViewHeightBasedOnChildren(lvData);
					} else {
						T.showShort(getActivity(), bean.result_desc);
					}
				}
			}
		});
	}
	
	public void updateView(List<ShopInfo> data, SearchBean search) {
		
		if(search.type!=null&&search.type.equals("2")){
			if(!TextUtils.isEmpty(search.name)){
//				btnArea.setText(search.name);
				
			}else{
//				btnArea.setText("全城");
			}
		}else{
			if(positions==-1){
//				if(CityManager.getInstance(activity)!=null&&CityManager.getInstance(activity).cityInfo!=null&&CityManager.getInstance(activity).cityInfo.city_name!=null){
//				String aa = CityManager.getInstance(activity).cityInfo.city_name;
//				if(TextUtils.isEmpty(aa)){
//					btnArea.setText("全城");
//				}else{
//					btnArea.setText(aa);
//				}
//				}else{
					btnArea.setText("附近");
//				}
			}
			
		}
		
		
		if(pageNo==1 || adapter==null) {
//			this.data.clear();
//			this.data.addAll(data);
//			adapter.getmData().clear();
//			adapter.getmData().addAll(data);
//			adapter.notifyDataSetChanged();
			adapter = new ShopListAdapter(getActivity(), data);
			lvData.setAdapter(adapter);
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		
		pageNo++;
	
		
	}
	
	@Override
	public void onPause() {
		banner.stopRoll();
		super.onPause();
//		removeSuspend();
	}

	@Override
	public void onResume() {
		super.onResume();
		banner.startRoll();
	}
	

	@Override
	public void onClick(View v) {
	       ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
	       .hideSoftInputFromWindow(activity.getCurrentFocus()
	       .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	       if(multiChoiceDialog!=null&&multiChoiceDialog.isShowing()){
	    	   multiChoiceDialog .dismiss();
	       }
	       if(dlg!=null&&dlg.isShowing()){
	    	   dlg .dismiss();
	       }
	       if(shopTypeWindow!=null){
	    	   shopTypeWindow.odismiss(); 
	       }
		switch(v.getId()) {
		case R.id.ivBack:
			activity.finish();
			break;
			
		case R.id.tv_ceash:
			tv_ceash.setVisibility(View.GONE);
			tv_search.setVisibility(View.GONE);
			MainActivity.name = "";
			btnSearch.setText("");
			if(TextUtils.isEmpty(search_name)){
				return;
			}
			pageNo = 1;
			search_name = "";
			
			onPullDownToRefresh(refreshscrollview);
//			startActivity(new Intent(activity, SearchShopActivity.class));
			break;
			
		case R.id.ivCloseBanner:
			rlBannerContainer.setVisibility(View.GONE);
			banner.stopRoll();
			break;
			
		case R.id.tv_search:
			tv_search.setVisibility(View.GONE);
		 	search_name = btnSearch.getText().toString();
		 	if(TextUtils.isEmpty(search_name)){
		 		tv_ceash.setVisibility(View.GONE);
		 		return;
		 	}
		 	pageNo = 1;
		 	loadData();
			break;
			
		case R.id.btnArea:
			moveview();
			showAreaDlg(positions);
			break;
			
		case R.id.btnSort:
			moveview();
			showSortDlg(positions2);
			break;
			
		case R.id.btnFilter:
			moveview();
			showFilterDlg();
			break;
			
		case R.id.btnShopType:
			moveview();
			if(Util.isFastDoubleClick()){
				 return;
			}
			if(shopTypeWindow == null){
				getShopTypeList();
			}else{
				
//				if(suspendView!=null){
				shopTypeWindow.show(resreach.getHeight()+buyLayoutHeight+statusBarHeight1,btnShopType);
//					shopTypeWindow.showAsDropDown(suspendView);
//				}else{
//					shopTypeWindow.showAsDropDown(btnShopType);
//				}
				shopTypeWindow.showChildTypeList(mactivity.positions,childposition);
			}

			break;
		}
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int arg2, long id) {
		if(UserInfoManager.isLogin(getActivity())) {
			ShopInfo item = adapter.getItem((int) id);
			Intent it = new Intent(getActivity(), ShopDetailActivity.class);
			it.putExtra("shopId", item.id);
			startActivity(new Intent(it));
		} else {
			startActivity(new Intent(getActivity(), LoginActivity.class));
		}
	}
	
private int areaIndex = -1;
	
	public void showAreaDlg(final int postion) {
//		if(data2!=null&&data2.size()>0){
//			showaredlg(data2,postion);
//			return;
//		}
		ProgressDialogUtil.showProgressDlg(activity, "加载数据");
		AreaListRequest req = new AreaListRequest();
		req.city_id = cityManager.getCityId()+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_AREA_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				final AreaListResponse bean = new Gson().fromJson(resp.result, AreaListResponse.class);
				Log.e("", ""+resp.result);
				if(Api.SUCCEED == bean.result_code) {
					if(bean.data==null || bean.data.size()<=0) {
						T.showShort(activity, "暂无可选区域");
						return;
					}
					data2 = bean.data;
					showaredlg(bean.data,postion);
					
					
					
					
//					new AlertDialog.Builder(activity).setTitle("区域选择")
//						.setSingleChoiceItems(areas, areaIndex, new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface arg0, int which) {
//								areaIndex = which;
//							}
//						})
//						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface arg0, int arg1) {
//								if(areaIndex > -1) {
//									area_id = bean.data.get(areaIndex).id;
//									onPullDownToRefresh(null);
//								}
//							}
//						})
//						.setNegativeButton("不限", new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface arg0, int arg1) {
//								area_id = -1;
//								onPullDownToRefresh(null);
//							}
//						})
//						.show();
				} else {
					removeSuspend();
					T.showShort(activity, bean.result_desc);
				}
			}
		});
	}
	
	
	public void showaredlg(final List<AreaInfo> data2, int postion){
		final List<AreasBean> list = new ArrayList<AreasBean>();
		AreasBean beans1 = new AreasBean();
//		String aa = CityManager.getInstance(activity).cityInfo.city_name;
//		if(TextUtils.isEmpty(aa)){
			beans1.name = "附近";
//		}else{
//			beans1.name = aa;
//		}
		
		if(postion==-1){
			beans1.bos = true;
		}else{
			beans1.bos = false;
		}
		list.add(beans1);
		for(int i=0; i<data2.size(); i++){
			AreasBean beans = new AreasBean();
			beans.name = data2.get(i).area_name;
//			if(postion>=0){
//				beans.bos = true;
//				list.get(postion).bos = true;
//			}else{
//				if(i==0){
//					beans.bos = true;
//				}else{
					beans.bos = false;
//				}
//			}
			
			
			list.add(beans);

		}
		if(postion>=0){
			list.get(postion).bos = true;
		}
			
			
		
		showMultiChoiceDialog("选择区域",true,list,null, boos,new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(arg2==0){
					positions = -1;
//					btnArea.setText("全城");
					area_id = -1;
					onPullDownToRefresh(null);
				}else{
					if(arg2 > -1) {
						positions = arg2;
						area_id = data2.get(arg2-1).id;
						onPullDownToRefresh(null);
						btnArea.setText(list.get(arg2).name);
					}
				}
				multiChoiceDialog.dismiss();
				
			}
		},new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
			
				areaIndex = arg1;
				
			}
		}, new OnMyDialogClickListener1() {
			
			@Override
			public void onClick(View v,DialogInterface arg0,int with) {
				// TODO Auto-generated method stub
				int num  = multiChoiceDialogBuilder.getCheckednum();
				switch (v.getId()) {
				case R.id.positiveButton:
					if(num > -1) {
						positions = num;
						area_id = data2.get(num).id;
						onPullDownToRefresh(null);
						btnArea.setText(list.get(num).name);
						}
					break;
				case R.id.negativeButton:
						positions = -1;
						btnArea.setText("商圈");
						area_id = -1;
						onPullDownToRefresh(null);
					break;
					
				default:
					break;
				}
			}
		});
	}
	
	
	
	
	public String showMultiChoiceDialog(String title,boolean tag, List<AreasBean> list, String[] item,boolean [] boos,OnItemClickListener onitem,DialogInterface.OnClickListener lisen,OnMyDialogClickListener1 onclic) {
		multiChoiceDialogBuilder = new CustomMultiChoiceDialog.Builder(getActivity());
		multiChoiceDialog = multiChoiceDialogBuilder.setTitle(title)
				.setMultiChoiceItems(list,tag, onitem, true)
				.setPositiveButton("确定", lisen,onclic).setNegativeButton("不限", lisen,onclic).create();
		
//		Window dialogWindow = multiChoiceDialog.getWindow();
//		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//		dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//		dialogWindow.setGravity(Gravity.TOP);
//		//显示的坐标
//		lp.y = resreach.getHeight()+50;
//		dialogWindow.setAttributes(lp);
		
        Window window = multiChoiceDialog.getWindow();
        window.setGravity(Gravity.TOP);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.Transparentdialog);  //添加动画
        
        
        
		WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
		lp.alpha = 0.7f;
//		lp.x = 0;
//		lp.y = myScrollViewTop+30;
		getActivity().getWindow().setAttributes(lp);
		
		multiChoiceDialog.setCanceledOnTouchOutside(true);
		multiChoiceDialog.show();
	
		multiChoiceDialog.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
				lp.alpha = 1f;
				getActivity().getWindow().setAttributes(lp);
				removeSuspend();
//				refreshscrollview.scrollBy(0, -llayout.getHeight());
//				inititem();
			}
		});
//		multiChoiceDialog.setOnDismissListener(new OnDismissListener() {
//
//			@Override
//			public void onDismiss() {
//				WindowManager.LayoutParams lp = getWindow().getAttributes();
//				lp.alpha = 1f;
//				getWindow().setAttributes(lp);
//			}
//		});
		
		return "";
	}

	
	private int sortIndex = 1;
	
	public void showSortDlg(int positions) {
		Log.e("", " - - - -  "+positions);
		List<AreasBean> areas1 = new ArrayList<AreasBean>();
		final String[] items = {"距离优先", "好评优先", "人气优先", "最受关注"};
		boolean[] boos = new boolean[items.length];
		for (int i = 0; i < items.length; i++) {
			
			AreasBean beans  = new AreasBean();
			beans.name = items[i];
//			beans.bos = false;
			beans.bos = false;
			areas1.add(beans);

		
		}
		
		if(positions>=0){
			areas1.get(positions).bos = true;
		}else{
//			boos[i] = false;
		}
		showMultiChoiceDialog("智能排序",true,areas1,items, boos,new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(arg2 == 0){
					btnSort.setText(items[arg2]);
					
					positions2 = 0;
					order_type = 1;
					onPullDownToRefresh(null);
				}else{
					btnSort.setText(items[arg2]);
					positions2 = arg2;
					order_type = arg2;
					onPullDownToRefresh(null);
				}
				multiChoiceDialog.dismiss();
			}
		},new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				areaIndex = arg1;
			}
		}, new OnMyDialogClickListener1() {
			
			@Override
			public void onClick(View v,DialogInterface arg0,int with) {
				// TODO Auto-generated method stub
				int num  = multiChoiceDialogBuilder.getCheckednum();
				switch (v.getId()) {
				case R.id.positiveButton:
					if(num > -1) {
						positions2 = num;
						order_type = num;
						onPullDownToRefresh(null);
						btnSort.setText(items[num]);
						}
					break;
				case R.id.negativeButton:
						order_type = 1;
						onPullDownToRefresh(null);
					break;
					
				default:
					break;
				}
			}
		});
		
		
		
//		new AlertDialog.Builder(activity).setTitle("智能排序")
//		.setSingleChoiceItems(items, sortIndex-1, new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int which) {
//				sortIndex = which+1;
//			}
//		})
//		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				order_type = sortIndex;
//				onPullDownToRefresh(null);
//			}
//		})
//		.setNegativeButton("默认", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				order_type = 1;
//				onPullDownToRefresh(null);
//			}
//		})
//		.show();
	}
	
	private boolean[] filterChecked = {false, false, false, false, false, false};
	
	public void showFilterDlg() {
		if(dlg !=null&&dlg.isShowing()){
			return;
		}
		if(data3!=null&&data3.size()>0){
		
			showtypedlg(data3, 0);
			return;
		}else{
			showTypeDlg(0);
		}
		
//		List<AreasBean> areas1 = new ArrayList<AreasBean>();
//	
//		String[] items = {"优惠券", "特惠活动", "派送", "清真", "诚信商家", "认证商家","100%积分商家","50%积分商家","25%积分商家"};
//		boolean[] boos = new boolean[items.length];
//		for (int i = 0; i < items.length; i++) {
//			AreasBean beans  = new AreasBean();
//			beans.name = items[i];
//			beans.bos = false;
//			areas1.add(beans);
////			if(positions22>=0){
////				boos[i] = true;
////			}else{
////				boos[i] = false;
////			}
//		}
//		
//		showMultiChoiceDialog("筛选(勾选为是/未勾选为否)",false,areas1,items, boos,null,new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				// TODO Auto-generated method stub
//				areaIndex = arg1;
//			}
//			}, new OnMyDialogClickListener1() {
//			
//			@Override
//			public void onClick(View v,DialogInterface arg0,int with) {
//				// TODO Auto-generated method stub
//				List<AreasBean> areas1   = multiChoiceDialogBuilder.getCheckedItems();
//				switch (v.getId()) {
//				case R.id.positiveButton:
//						is_have_coupon = areas1.get(0).bos? 1 : 0;
//						is_have_active = areas1.get(1).bos? 1 : 0;
//						is_push = areas1.get(2).bos? 1 : 0;
//						is_muslim = areas1.get(3).bos? 1 : 0;
//						is_integrity =areas1.get(4).bos? 1 : 0;
//						is_authentication = areas1.get(5).bos? 1 : 0;
//						is_discount_type_three = areas1.get(6).bos? 1 : 0;
//						is_discount_type_two = areas1.get(7).bos? 1 : 0;
//						is_discount_type_one = areas1.get(8).bos? 1 : 0;
//						onPullDownToRefresh(null);
//					break;
//				case R.id.negativeButton:
//						is_have_coupon = -1;
//						is_have_active = -1;
//						is_push = -1;
//						is_muslim = -1;
//						is_integrity = -1;
//						is_authentication = -1;
//						is_discount_type_one = 0;
//						is_discount_type_two = 0;
//						is_discount_type_three = 0;
//						onPullDownToRefresh(null);
//					break;
//					
//				default:
//					break;
//				}
//			}
//		});
 //- - - - - - - - - -- - - -  		
//		new AlertDialog.Builder(activity).setTitle("筛选(勾选为是/未勾选为否)")
//		.setMultiChoiceItems(items, filterChecked, new DialogInterface.OnMultiChoiceClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//				filterChecked[which] = isChecked;
//			}
//		})
//		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				is_have_coupon = filterChecked[0]? 1 : 0;
//				is_have_active = filterChecked[1]? 1 : 0;
//				is_push = filterChecked[2]? 1 : 0;
//				is_muslim = filterChecked[3]? 1 : 0;
//				is_integrity = filterChecked[4]? 1 : 0;
//				is_authentication = filterChecked[5]? 1 : 0;
//				onPullDownToRefresh(null);
//			}
//		})
//		.setNegativeButton("不限", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				is_have_coupon = -1;
//				is_have_active = -1;
//				is_push = -1;
//				is_muslim = -1;
//				is_integrity = -1;
//				is_authentication = -1;
//				onPullDownToRefresh(null);	
//			}
//		})
//		.show();
	}
	
	public void getBannerList() {
		BannerRequest req = new BannerRequest();
		req.city_id = cityManager.getCityId()+"";
		req.banner_place = "4";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_BANNER_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				if(resp.result!=null) {
					BannerListResponse bean = new Gson().fromJson(resp.result, BannerListResponse.class);
					if(bean.data!=null && bean.data.size()>0) {
						rlBannerContainer.setVisibility(View.VISIBLE);
						banner.createView(bean.data);
						banner.startRoll();
						banner.setRelativeLayoutWidthHeight(0.5);
					}
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}
		});
	}
	
	public void getShopTypeList() {
		ProgressDialogUtil.showProgressDlg(activity, "加载数据");
		CategoryRequest req = new CategoryRequest();
		req.parent_id = "0";
		req.city_id = CityManager.getInstance(activity).getCityId()+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_CATEGORY_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				CategoryResponse bean = new Gson().fromJson(resp.result, CategoryResponse.class);
				if(Api.SUCCEED == bean.result_code) {
//					Log.e("", " = = = "+mactivity.positions);
//					Log.e("", ""+bean.data.toString());
//					if(mactivity.positions!=0){
//						mactivity.positions = mactivity.positions+1;
//					}
					
					shopTypeWindow = new ShopTypeWindow(activity, bean.data,mactivity.positions);
					shopTypeWindow.setPickTypeCallback(ShopListFragment.this);
					
					shopTypeWindow.dlg.setOnDismissListener(new OnDismissListener() {
						
						@Override
						public void onDismiss() {
							// TODO Auto-generated method stub
							removeSuspend();
							shopTypeWindow.odismiss();
						}
					});
					
//					if(suspendView!=null){
//					shopTypeWindow.showAsDropDown(suspendView);
//				}else{
					shopTypeWindow.show(resreach.getHeight()+buyLayoutHeight+statusBarHeight1,btnShopType);
//					shopTypeWindow.showAsDropDown(btnShopType);
//				}
				}else{
					removeSuspend();
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
			}
		});
	}
	
//	OnDismissListener listener = new OnDismissListener() {
//
//		@Override
//		public void onDismiss() {
//			// TODO Auto-generated method stub
//			WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//			lp.alpha = alpha;
//			activity.getWindow().setAttributes(lp);
//			
//		}
//	};
	
	
	
	/* 
	 * 商户类型选择回调
	 */
	@Override
	public void onPickedType(CategoryInfo parentInfo, CategoryInfo info,int po,int po1) {
		childposition = po1;
		mactivity.positions = po;
		
		if(info!=null&&info.type_name!=null){
			mactivity.shop_type_name = info.type_name;
			btnShopType.setText(info.type_name);
		}else{
			mactivity.shop_type_name = parentInfo.type_name;
			btnShopType.setText(parentInfo.type_name);
		}
	
		if(po==0){
			gvHot.setVisibility(View.GONE);
			btnShopType.setText("分类");
			mactivity.shop_type_id = "";
			shopTypeId = 0;
			 order_type = 1;			//排序 1距离 2好评 3人气 4关注
//			 is_authentication = 0;	//是否认证 （可选）
			 is_consumption = 0;//再消费
			 is_integrity = 0;			//是否诚信商家 （可选
			  is_muslim = 0;			//是否清真 （可选
			  is_push = 0;				//是否派送（可选
			  is_recommend = 0;			//是否推荐（可选
			  is_have_coupon = 0;		//是否有优惠劵（可选
			  is_have_active = 0;		//是否有活动（可选
			  is_discount_type_one = 0;
			  is_discount_type_two = 0; 
			  is_discount_type_three= 0;
//			  area_id = -1;				//区域（可选
			  mactivity.positions = 0;
			  onPullDownToRefresh(null);	
//			onPullDownToRefresh(lvData);
			return;
		}else{
			gvHot.setVisibility(View.VISIBLE);
			if(parentInfo.child_shop_type_list!=null){
				List<CategoryInfo> list = new ArrayList<CategoryInfo>();
				for (int i = 0; i < parentInfo.child_shop_type_list.size(); i++) {
					if(parentInfo.child_shop_type_list.get(i).is_hot!=null&&parentInfo.child_shop_type_list.get(i).is_hot.equals("1")){
						list.add(parentInfo.child_shop_type_list.get(i));
					}
					
				}
				gridlist.clear();
				gridlist.addAll(list);
				gridadapter.notifyDataSetChanged();
				
//				if(parentInfo.child_shop_type_list.size()>=8){
//					List<CategoryInfo> list = new ArrayList<CategoryInfo>();
//					for (int i = 0; i < 8; i++) {
//						list.add(parentInfo.child_shop_type_list.get(i));
//					}
//					gridlist.clear();
//					gridlist.addAll(list);
//					gridadapter.notifyDataSetChanged();
//				}else{
//					gridlist.clear();
//					gridlist.addAll(parentInfo.child_shop_type_list);
//					gridadapter.notifyDataSetChanged();
//				}
			}
//			gvHot.setAdapter(new ShopListHotAdapter(activity, parentInfo.child_shop_type_list));
		}
		if(po1==0){
			mactivity.shop_type_id = parentInfo.id+"";
			shopTypeId = 0;
		}else{
			shopTypeId = info==null? parentInfo.id : info.id;
		}
		
		onPullDownToRefresh(null);	
	}
	
	
	
//	class PositiveClickListener implements DialogInterface.OnClickListener{
//
//		@Override
//		public void onClick(DialogInterface arg0, int arg1) {
//			// TODO Auto-generated method stub
//			String s = "您选择了:";
//			boos = multiChoiceDialogBuilder.getCheckedItems();
//			for (int i = 0; i < boos.length; i++) {
//				if (boos[i]) {
//					s += i + ":" + arrs[i] + "  ";
//				} 
//			}
//		}
//		
//	}
	
//	class PositiveClickListener implements OnClickListener {
//
//		@Override
//		public void onClick(DialogInterface dialog, int which) {
//			// TODO Auto-generated method stub
//			String s = "您选择了:";
//			boos = multiChoiceDialogBuilder.getCheckedItems();
//			for (int i = 0; i < boos.length; i++) {
//				if (boos[i]) {
//					s += i + ":" + arrs[i] + "  ";
//				} 
//			}
////			alert(getActivity().getApplicationContext(), s);
//		}
//	}
	
	@Override
	public boolean onKey(View arg0, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if (keyCode == KeyEvent.KEYCODE_ENTER) {
		       // 先隐藏键盘
//		       ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
//		       .hideSoftInputFromWindow(CommodityManageActivity.this.getCurrentFocus()
//		       .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			 if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
			      //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
				 	search_name = btnSearch.getText().toString();
				 	pageNo = 1;
				 	loadData();
				 	return true;
			 }
		   }
		return false;
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		btnSearch.setText("");
		search_name = "";
		removeSuspend();
	}




	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		
		search_name = "";
		pageNo = 1;
		loadData();
	}
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		loadData();
	}
	
	
	public void moveview(){
		scrollToPosition();
	}
	
	private Handler shandler = new Handler();

	 /**
	  * 滑动到指定位置
	  */
	private void scrollToPosition() {
//		onScroll(rlBannerContainer.getHeight()+gvHot.getHeight());
//		refreshscrollview.scrollBy(0, llayout.getHeight());
//		refreshscrollview.clearFocus();
		llayout.setVisibility(View.GONE);

		onScroll(height);
//		Utility.setListViewHeightBasedOnChildren(lvData);
		
//		inititem();
		
	}
	
	
	
	
    private void inititem() {
		// TODO Auto-generated method stub
    	if(adapter!=null&&adapter.getCount()>0){
		 View listItem = adapter.getView(0, null, lvData);
	     listItem.measure(0, 0);  
        int  totalHeight = listItem.getMeasuredHeight()*(adapter.getCount());  
//        ViewGroup.LayoutParams linearParams = lvData.getLayoutParams();
////		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) lvData.getLayoutParams();
//		linearParams.height = totalHeight;
//		lvData.setLayoutParams(linearParams);
		
		LinearLayout.LayoutParams linearParams1 = (LinearLayout.LayoutParams) refreshscrollview.getLayoutParams();
//		linearParams1.height = height-resreach.getHeight()-50-buyLayoutHeight;
		linearParams1.height =refreshscrollview.getHeight();
		Log.e("", " - - -  "+totalHeight);
		refreshscrollview.setLayoutParams(linearParams1);
    	}
	}

//	/** 
//     * 窗口有焦点的时候，即所有的布局绘制完毕的时候，我们来获取购买布局的高度和myScrollView距离父类布局的顶部位置 
//     */  
//    @Override    
//    public void onWindowFocusChanged(boolean hasFocus) {  
//        if(hasFocus){  
//            buyLayoutHeight = mBuyLayout.getHeight();  
//            buyLayoutTop = mBuyLayout.getTop();  
//            myScrollViewTop = rlBannerContainer.getTop();  
//        }  
//    }
    
    /** 
     * 滚动的回调方法，当滚动的Y距离大于或者等于 购买布局距离父类布局顶部的位置，就显示购买的悬浮框 
     * 当滚动的Y的距离小于 购买布局距离父类布局顶部的位置加上购买布局的高度就移除购买的悬浮框 
     *  
     */  
    @Override  
    public void onScroll(int scrollY) { 
    	
		if(llayout.getVisibility()==View.GONE){
			height = 0;
		}else{
			height = llayout.getHeight();
		}
    	
//        buyLayoutHeight = mBuyLayout.getHeight();  
//        buyLayoutTop = mBuyLayout.getTop();  
//        myScrollViewTop = rlBannerContainer.getTop();  
    	
    	if(istag){
    		onscroll();
    		istag = false;
    	}
    	
        if(scrollY >= height){ 
//        	llayout.setVisibility(View.GONE);
            if(suspendView == null){  
                showSuspend();  
            }  
        }else if(scrollY <= llayout.getHeight() + buyLayoutHeight){ 
        	
//        	llayout.setVisibility(View.VISIBLE);
            if(suspendView != null){  
            	llayout.setVisibility(View.VISIBLE);
                removeSuspend();  
            }  
        }  
    }  
    
    
	private void onscroll() {
		// TODO Auto-generated method stub
        buyLayoutHeight = mBuyLayout.getHeight();  
        buyLayoutTop = mBuyLayout.getTop();  
        screenWidth = mBuyLayout.getWidth();    
        myScrollViewTop = refreshscrollview.getTop();  
	}
    
    /** 
     * 显示购买的悬浮框 
     */  
    private void showSuspend(){  
        if(suspendView == null){  
            suspendView = LayoutInflater.from(activity).inflate(R.layout.buy_layout, null);  
            suspendView.findViewById(R.id.btnArea).setOnClickListener(this);
            suspendView.findViewById(R.id.btnSort).setOnClickListener(this);
            suspendView.findViewById(R.id.btnFilter).setOnClickListener(this);
            suspendView.findViewById(R.id.btnShopType).setOnClickListener(this);
        	btnShopType1 = (Button) suspendView.findViewById(R.id.btnShopType);
    		btnSort1 = (Button) suspendView.findViewById(R.id.btnSort);
    		btnArea1 = (Button) suspendView.findViewById(R.id.btnArea);
    		btnShopType1.setText(btnShopType.getText().toString());
    		btnArea1.setText(btnArea.getText().toString());
    		btnSort1.setText(btnSort.getText().toString());
            if(suspendLayoutParams == null){  
                suspendLayoutParams = new LayoutParams();  
//                suspendLayoutParams.type = LayoutParams.TYPE_PHONE; //悬浮窗的类型，一般设为2002，表示在所有应用程序之上，但在状态栏之下   
                suspendLayoutParams.format = PixelFormat.RGBA_8888;   
                suspendLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL    
                | LayoutParams.FLAG_NOT_FOCUSABLE;  //悬浮窗的行为，比如说不可聚焦，非模态对话框等等   
                suspendLayoutParams.gravity = Gravity.TOP;  //悬浮窗的对齐方式  
                suspendLayoutParams.width = screenWidth;  
                suspendLayoutParams.height = buyLayoutHeight;    
                suspendLayoutParams.x = 0;  //悬浮窗X的位置  
                suspendLayoutParams.y = myScrollViewTop;  ////悬浮窗Y的位置  
                

            }  
        }  
          
        mWindowManager.addView(suspendView, suspendLayoutParams);  
    }  
      
      
    /** 
     * 移除购买的悬浮框 
     */  
    private void removeSuspend(){  
        if(suspendView != null){  
        
            mWindowManager.removeView(suspendView);  
            suspendView = null;  
        }  
    }
    
    
    public void showTypeDlg(final int postion) {
//		if(data2!=null&&data2.size()>0){
//			showaredlg(data2,postion);
//			return;
//		}
		ProgressDialogUtil.showProgressDlg(activity, "请稍候");
//		ProgressDialogUtil.setCancelable(false);
		AreaListRequest req = new AreaListRequest();
		req.type = "1";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETFILTERCONFIGURELISTBYTYPE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				final ScreenResponse bean = new Gson().fromJson(resp.result, ScreenResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					if(bean.data==null || bean.data.size()<=0) {
						return;
					}
					data3 = bean.data;
					showtypedlg(bean.data,postion);
				} else {
					removeSuspend();
					T.showShort(activity, bean.result_desc);
				}
			}


		});
	}
     GridDialog dlg;
	private void showtypedlg(List<ScreenBean> data, int postion) {
		// TODO Auto-generated method stub
		 	dlg= new GridDialog(activity, R.style.Transparentdialog,"",data,Listener);
	        Window window = dlg.getWindow();
	        window.setGravity(Gravity.TOP);  //此处可以设置dialog显示的位置
	        window.setWindowAnimations(R.style.Transparentdialog);  //添加动画
	        
	        getActivity().setFinishOnTouchOutside(true) ;
	        dlg.setBackgroundAlpha(activity, 0.7f);
	        dlg.setCancelable(true);
	        dlg.setCanceledOnTouchOutside(true);
	        dlg.show();

	}
	
	
	OnClickListener  Listener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		
//			removeSuspend();
			switch (arg0.getId()) {
			case R.id.tv_config:
				dlg.ondess();
				List<ScreenBean> datas = dlg.getPosition();
				List<AreasBean> areas1   = new ArrayList<AreasBean>();
				for (int i = 0; i < datas.size(); i++) {
					for (int j = 0; j < datas.get(i).child_filter_configures_list.size(); j++) {
						AreasBean areasBean = new AreasBean();
						areasBean.bos = datas.get(i).child_filter_configures_list.get(j).ischex;
						areas1.add(areasBean);
//						Log.e("", ""+datas.get(i).child_filter_configures_list.get(j).ischex);
					}
				}
				if(areas1.size()>=9){
					is_integrity = areas1.get(0).bos? 1 : 0;
//					 is_authentication=areas1.get(1).bos? 1 : 0;
					 is_consumption =areas1.get(1).bos? 1 : 0;
					is_push = areas1.get(2).bos? 1 : 0;
					is_muslim = areas1.get(3).bos? 1 : 0;
					is_discount_type_three = areas1.get(4).bos? 1 : 0;
					is_discount_type_two = areas1.get(5).bos? 1 : 0;
					is_discount_type_one = areas1.get(6).bos? 1 : 0;
					is_have_coupon = areas1.get(7).bos? 1 : 0;
					is_have_active = areas1.get(8).bos? 1 : 0;
					onPullDownToRefresh(null);
				}
				break;
				
			case R.id.tv_cech:
				for (int i = 0; i < GridDialog.data.size(); i++) {
					for (int j = 0; j <  GridDialog.data.get(i).child_filter_configures_list
							.size(); j++) {
						 GridDialog.data.get(i).child_filter_configures_list.get(j).ischex = false;
					}
				}
				dlg.initdata(GridDialog.data, true);
//				is_have_coupon = -1;
//				is_have_active = -1;
//				is_push = -1;
//				is_muslim = -1;
//				is_integrity = -1;
//				is_authentication = -1;
//				is_discount_type_one = 0;
//				is_discount_type_two = 0;
//				is_discount_type_three = 0;
//				onPullDownToRefresh(null);
				break;

			default:
				break;
			}
		

		}
	};
	
	   @Override  
	   public void onScrollChanged(int l, int t, int oldl, int oldt){
		   
			if (oldt > t && oldt - t > 0.1) {// 向下
			llayout.setVisibility(View.VISIBLE);
			removeSuspend();
			} else if (oldt < t && t - oldt > 0.1) {// 向上
			}
		   
	   }
}
