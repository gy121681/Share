package com.shareshenghuo.app.shop;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.adapter.AddPhotoGridAdapter;
import com.shareshenghuo.app.shop.adapter.MyGridAdapter;
import com.shareshenghuo.app.shop.adapter.PoiListAdapter;
import com.shareshenghuo.app.shop.manager.ImageLoadManager;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.PhotoBean;
import com.shareshenghuo.app.shop.network.bean.ShopInfoBean;
import com.shareshenghuo.app.shop.network.bean.ShopPhone;
import com.shareshenghuo.app.shop.network.bean.ShopPhoneBean;
import com.shareshenghuo.app.shop.network.request.SaveShopPhotoRequest;
import com.shareshenghuo.app.shop.network.request.ShopInfoRequest;
import com.shareshenghuo.app.shop.network.request.ShopRequest;
import com.shareshenghuo.app.shop.network.response.AutResponse;
import com.shareshenghuo.app.shop.network.response.FileUploadResponse;
import com.shareshenghuo.app.shop.network.response.ShopInfoRes;
import com.shareshenghuo.app.shop.network.response.ShopInfoResponse;
import com.shareshenghuo.app.shop.network.response.ShopPhoneResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.networkapi.Callback;
import com.shareshenghuo.app.shop.util.BitmapTool;
import com.shareshenghuo.app.shop.util.FileUtil;
import com.shareshenghuo.app.shop.util.PictureUtil;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;
import com.shareshenghuo.app.shop.widget.MapLayout;
import com.shareshenghuo.app.shop.widget.MyGridView;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.PickPhotoWindow;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;

/**
 * @author hang
 * 纠错商家信息
 */
public class EditShopActivity extends BaseTopActivity implements LocationSource, AMapLocationListener, OnPoiSearchListener, OnMapClickListener, OnItemClickListener, OnClickListener{//, PickTypeCallback {
	
	private static final int REQ_SET_ADDR = 0x101;
	
	private EditText edName;
	private EditText edMobile,contents;
	private EditText edDesc;
//	private LinearLayout llPickType;
	
//	private ShopInfo shopInfo; // 为null新增  非null修改
	
	private AddPhotoGridAdapter adapter;
	
//	private ShopTypeWindow shopTypeWindow;
	
	private int parentTypeId = -1;
	private int childTypeId = -1;
	private String addr;
	private double lat = -1, lng = -1;
	private MapLayout maplayout;
	private ScrollView scro;
	//地图
	private static final int REQ_SEARCH_POI = 0x101;
	private TextView tvShopType,tvShopAddr;
	private ListView lvPOI;
	
	private MapView mapView;
	private AMap aMap;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private Marker locationMarker; // 选择的点
	private List<PhotoBean> data;
	private List<HashMap<String, Object>> data_list ;
	private MyGridAdapter sim_adapter;
	private ImageView logo_img,img_shopzxf;
	private Uri photoUri;
	private static final int SCALE = 5;//照片缩小比例
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static final int REQUEST_PICK_LOCAL = 2;
	private View parentView;
	private RelativeLayout readdlogo;
	private String photologo;
	private ShopInfoBean beans;
	private TwoButtonDialog downloadDialog;
	public boolean ismap = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parentView = getLayoutInflater().inflate(R.layout.activity_edit_shop, null);
		setContentView(parentView);
		init();
		initmap(savedInstanceState);
	}
	
	public void initmap(Bundle savedInstanceState) {
		lvPOI = (ListView) findViewById(R.id.lvPOIS);
		lvPOI.setOnItemClickListener(this);
//		findViewById(R.id.btnTopSearch).setOnClickListener(this);
		
		mapView = getView(R.id.mapView);
		mapView.onCreate(savedInstanceState);
		
		
		loaddata();
		initpop();
//		loadData();
//		loadPoto();
	}
		
	
	public void init() {
//		shopInfo = (ShopInfo) getIntent().getSerializableExtra("shopInfo");
//		
//		if(shopInfo != null)
//			initTopBar("纠错商家信息");
//		else
			initTopBar("店铺管理");
		maplayout = getView(R.id.llmap);
		scro = getView(R.id.scro);
		logo_img = getView(R.id.logo_img);
		maplayout.setScrollView(scro);
		readdlogo = getView(R.id.readdlogo);
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("保存");
		btnTopRight1.setOnClickListener(this);
		contents = getView(R.id.contentss);
//		llPickType = getView(R.id.llShopPickType);
		edName = getView(R.id.edShopName);
		edMobile = getView(R.id.edShopMobile);
		edDesc = getView(R.id.edShopDesc);
		tvShopType = getView(R.id.tvShopType);
		tvShopAddr = getView(R.id.tvShopAddr);
		img_shopzxf = getView(R.id.img_shopzxf);
		logo_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(photologo)){
					ArrayList<String> arr = new ArrayList<String>();
					arr.add(photologo);
					Intent it = new Intent(EditShopActivity.this, ImagePagerActivity.class);
					it.putExtra("title", "浏览");
					it.putExtra("position", 0);
					it.putStringArrayListExtra("urls", arr);
					startActivity(it);	
				}
			}
		});
		llTopBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(getext()){
					initDialog("退出本次编辑?", "否", "是");
				}else{
					finish();
				}
			}
		});
		readdlogo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ll_popup.startAnimation(AnimationUtils.loadAnimation(EditShopActivity.this,R.anim.activity_translate_in));
				pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
//				new PickPhotoWindowNew(EditShopActivity.this).showAtBottom();
//				photoUri= Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
			}
		});
//		if(shopInfo != null) {
//			edName.setText(shopInfo.shop_name);
//			edMobile.setText(shopInfo.mobile);
//			setText(R.id.tvShopType, shopInfo.shop_child_type_name);
//			setText(R.id.tvShopAddr, shopInfo.address);
//			edDesc.setText(shopInfo.introduction);
//			
//			parentTypeId = shopInfo.shop_type_id;
//			childTypeId = shopInfo.shop_child_type_id;
//			addr = shopInfo.address;
//			lat = shopInfo.latitude;
//			lng = shopInfo.longitude;
//		}
		initpohoto();

	}

	private void initpohoto() {
		// TODO Auto-generated method stub
		MyGridView gvPhoto = getView(R.id.gvShopPhoto);
		data_list = new ArrayList<HashMap<String, Object>>();
	    for (int i = 1; i < 5; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("img", "");
			switch (i ) {
			case 1:
				map.put("type", "4");
				map.put("name", "店铺照片");
				break;
			case 2:
				map.put("type", "1");
				map.put("name", "店内环境");
				break;
			case 3:
				map.put("type", "2");
				map.put("name", "服务照片");
				break;
			case 4:
				map.put("type", "5");
				map.put("name", "其他照片");
				break;
			default:
				break;
			}
			map.put("num", "0");
			data_list.add(map);
		}
		sim_adapter = new MyGridAdapter(this, data_list);
		gvPhoto.setAdapter(sim_adapter);
		getView(R.id.llShopPickAddr).setOnClickListener(this);
		gvPhoto.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent it = new Intent(EditShopActivity.this,ShopPhotoAcitivty.class);
				it.putExtra("type", data_list.get((int)arg3).get("type").toString());
				it.putExtra("title",data_list.get((int)arg3).get("name").toString());
				startActivity(it);
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnTopRight1:
//			if(ViewUtil.checkEditEmpty(edName, "请输入名称"))
//				return;
			if(ViewUtil.checkEditEmpty(edMobile, "请输入联系电话"))
				return;
//			if(TextUtils.isEmpty(addr)) {
//				T.showShort(this, "请选择地址");
//				return;
//			}
			
//			loadphoto(null,edMobile.getText().toString(),contents.getText().toString());
			loadphoto(null, edMobile.getText().toString(), contents.getText().toString(), new Callback() {
				@Override
				public void onSuccess(Object data) {
					// 保存成功结算界面
					finish();
				}

				@Override
				public void onFailure(String msg) {

				}

				@Override
				public void onNetError(int code, String msg) {

				}
			});
			break;
			
//		case R.id.llShopPickType:
//			if(shopTypeWindow == null)
//				getShopTypeList();
//			else
//				shopTypeWindow.showAsDropDown(llPickType);
//			break;
			
		case R.id.llShopPickAddr:
//			startActivityForResult(new Inten  t(this, PoiMapActivity.class), REQ_SET_ADDR);
			break;
		}
	}
	
//	public void submit() {
//		ProgressDialogUtil.showProgressDlg(this, "提交审核");
//		EditShopRequest req = new EditShopRequest();
//		req.user_id = UserInfoManager.getUserId(this)+"";
//		req.city_id = CityManager.getInstance(this).cityInfo.id+"";
//		req.shop_type_id = parentTypeId+"";
//		req.shop_child_type_id = childTypeId+"";
//		if(shopInfo != null)
//			req.shop_id = shopInfo.id+"";
//		req.shop_name = edName.getText().toString();
//		req.mobile = edMobile.getText().toString();
//		req.introduction = edDesc.getText().toString();
//		req.address = addr;
//		req.latitude = lat+"";
//		req.longitude = lng+"";
//		req.shop_photo = adapter.getPhotoUrls();
//		RequestParams params = new RequestParams("utf-8");
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		
//		String api = "";
//		if(shopInfo != null)
//			api = Api.URL_ADD_SHOP_WRONG;
//		else
//			api = Api.URL_FIND_SHOP;
//		new HttpUtils().send(HttpMethod.POST, api, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(EditShopActivity.this);
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					T.showLong(EditShopActivity.this, "提交成功，请等待审核");
//					setResult(RESULT_OK);
//					finish();
//				}
//			}
//		});
//	}
	
//	public void getShopTypeList() {
//		ProgressDialogUtil.showProgressDlg(this, "加载数据");
//		CategoryRequest req = new CategoryRequest();
//		req.parent_id = "0";
//		req.city_id = CityManager.getInstance(this).getCityId()+"";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_CATEGORY_LIST, params, new RequestCallBack<String>() {
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				CategoryResponse bean = new Gson().fromJson(resp.result, CategoryResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					shopTypeWindow = new ShopTypeWindow(EditShopActivity.this, bean.data);
//					shopTypeWindow.setPickTypeCallback(EditShopActivity.this);
//					shopTypeWindow.showAsDropDown(llPickType);
//				}
//			}
//			
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//			}
//		});
//	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		adapter.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK&&requestCode == PickPhotoWindow.REQUEST_PICK_LOCAL ){
			String path = FileUtil.getPath(EditShopActivity.this, data.getData());
			if (path == null) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap bmp = extras.getParcelable("data");
					if (bmp != null) {
						upPhoto(BitmapTool.Bitmap2File(EditShopActivity.this, bmp));
					}
				}
			} else {
				upPhoto(new File(path));
			}
		}else if(resultCode == RESULT_OK&&requestCode == PickPhotoWindow.REQUEST_TAKE_CAMERA){
			String path =  photoUri.getPath();
//			if (path == null) {
//				Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
//				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
//				//由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
//				bitmap.recycle();
//				
//				File file =ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath()+ "/share/", String.valueOf(System.currentTimeMillis()));
//			upPhoto(file);
//			}
			upPhoto(new File(path));
		}else{
			if(resultCode == RESULT_OK) {
				switch(requestCode) {
				case REQ_SET_ADDR:
					addr = data.getStringExtra("addr");
					lat = data.getDoubleExtra("lat", lat);
					lng = data.getDoubleExtra("lng", lng);
					setText(R.id.tvShopAddr, addr);
					break;
				}
			}
		}

	}
	

	/* 
	 * 商户类型选择回调
	 */
//	@Override
//	public void onPickedType(CategoryInfo parentInfo, CategoryInfo info) {
//		setText(R.id.tvShopType, parentInfo.type_name+" "+info.type_name);
//		parentTypeId = info.parent_id;
//		childTypeId = info.id;
//	}
	
	
	public void upPhoto(File f) {
		
		try {
            // 压缩图片
            String compressPath = PictureUtil.compressImage(EditShopActivity.this, f.getPath(), f.getName(), 65);
            f = new File(compressPath);
        } catch(Exception e) {
            e.printStackTrace();
        }
		
		ProgressDialogUtil.showProgressDlg(EditShopActivity.this, "图片上传中");
		RequestParams params =  new RequestParams();
		params.addBodyParameter("business_type", UserInfoManager.getUserId(EditShopActivity.this)+"");
		params.addBodyParameter("file", f);
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPLOAD_FILE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(EditShopActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				if(resp.statusCode==200 && resp.result!=null) {
					FileUploadResponse bean = new Gson().fromJson(resp.result, FileUploadResponse.class);
					T.showShort(EditShopActivity.this, bean.result_desc);
					if(Api.SUCCEED == bean.result_code)
						loadphoto(bean.data.get(0), null,null);
				}
			}
		});
	}
	
	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		aMap = mapView.getMap();
		// 自定义系统定位小蓝点
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		aMap.setOnMapClickListener(this);
	}
	
	public void initPoiParams(double d, double e) {
		locationMarker = aMap.addMarker(new MarkerOptions()
			.anchor(0.5f, 1)
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_60))
			.position(new LatLng(d, e)));
		locationMarker.showInfoWindow();
		
		
//		 aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(d, e), 19));  
//         MarkerOptions markerOptions = new MarkerOptions();  
//         markerOptions.position(new LatLng(d, e));  
//         markerOptions.title("当前位置");  
//         markerOptions.visible(true);  
//         BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_60));  
//         markerOptions.icon(bitmapDescriptor);  
//         aMap.addMarker(markerOptions);  
	}
	
	/**
	 * 设置周围POI列表
	 */
	public void queryAroundPOI() {
		PoiSearch.Query query = new PoiSearch.Query("", "", "");
		query.setPageSize(10);
		query.setPageNum(0);
		PoiSearch poiSearch = new PoiSearch(this, query);
		poiSearch.setBound(new SearchBound(
				new LatLonPoint(locationMarker.getPosition().latitude, locationMarker.getPosition().longitude)
				, 1000));
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		loaddata();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
//		FinalBitmap.create(this).closeCache();
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null && amapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
			} else {
				deactivate();
				mapView.onDestroy();
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
			}
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			//设置定位监听
			mlocationClient.setLocationListener(this);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			//设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}
	
	@Override
	public void onMapClick(LatLng latng) {
		aMap.clear();
		locationMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_60))
				.position(latng));
		locationMarker.showInfoWindow();
		queryAroundPOI();
	}

	/**
	 * POI搜索回调方法
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				lvPOI.setAdapter(new PoiListAdapter(this, result.getPois()));
				
				LatLng latng= new LatLng(result.getPois().get(0).getLatLonPoint().getLatitude(), result.getPois().get(0).getLatLonPoint().getLongitude());
				aMap.clear();
				
				locationMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_60))
						.position(latng));
				locationMarker.showInfoWindow();
//				queryAroundPOI();
				
//				setUpMap();
				if(result.getPois()!=null&&result.getPois().get(0).getLatLonPoint()!=null&&result.getPois().get(0).getLatLonPoint().getLatitude()>0
						&&result.getPois().get(0).getLatLonPoint().getLongitude()>0){
					
					initPoiParams(result.getPois().get(0).getLatLonPoint().getLatitude(), 
							result.getPois().get(0).getLatLonPoint().getLongitude());
				}

				
				
				 aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(result.getPois().get(0).getLatLonPoint().getLatitude(), result.getPois().get(0).getLatLonPoint().getLongitude()), 19));  
                    MarkerOptions markerOptions = new MarkerOptions();  
                    markerOptions.position(new LatLng(result.getPois().get(0).getLatLonPoint().getLatitude(), result.getPois().get(0).getLatLonPoint().getLongitude()));  
                    markerOptions.title("当前位置");  
                    markerOptions.visible(true);  
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_60));  
                    markerOptions.icon(bitmapDescriptor);  
                    aMap.addMarker(markerOptions);  
				
			}
		}
	}
	
//	@Override
//	public void onClick(View v) {
//		switch(v.getId()) {
//		case R.id.btnTopSearch:
//			startActivityForResult(new Intent(this, SearchPOIActivity.class), REQ_SEARCH_POI);
//			break;
//		}
//	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
		PoiItem item = (PoiItem) parent.getItemAtPosition(position);
		Intent data = new Intent();
		data.putExtra("addr", item.getSnippet());
		data.putExtra("lat", item.getLatLonPoint().getLatitude());
		data.putExtra("lng", item.getLatLonPoint().getLongitude());
		setResult(RESULT_OK, data);
		finish();
	}
	
	
	public void loadData() {
		ShopInfoRequest req = new ShopInfoRequest();
		req.shop_id = UserInfoManager.getUserInfo(this).shop_id;
		req.latitude = "0";
		req.longitude="0";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETSHOPDETAILS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ShopInfoResponse bean = new Gson().fromJson(resp.result, ShopInfoResponse.class);
				
				if(Api.SUCCEED == bean.result_code) {
//					UserInfoManager.saveUserInfo(MerchantInfoActivity.this, bean.data.);
					updateView(bean.data.shop_info);
				}
			}



		});
	}
//	edName = getView(R.id.edShopName);
//	edMobile = getView(R.id.edShopMobile);
//	edDesc = getView(R.id.edShopDesc);
	private void updateView(ShopInfoBean shop_info) {
		// TODO Auto-generated method stub
		edName.setText(shop_info.shop_name);
		edMobile.setText(shop_info.mobile);
		tvShopType.setText(shop_info.shop_type_name);
		tvShopAddr.setText(shop_info.address);
		
	}
	
	
	public void loadPoto() {
		ShopRequest req = new ShopRequest();
		req.shop_id = UserInfoManager.getUserInfo(this).shop_id;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETSHOPPHOTOINFOLIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				
				ShopPhoneResponse bean = new Gson().fromJson(resp.result, ShopPhoneResponse.class);
				Log.e("", " - - - "+resp.result.toString());
				if(Api.SUCCEED == bean.result_code) {
//					UserInfoManager.saveUserInfo(MerchantInfoActivity.this, bean.data.);
					updateView(bean.data);
				}
			}
		});
	}
	
	public void loaddata() {
		ShopRequest req = new ShopRequest();
		req.shopId = UserInfoManager.getUserInfo(this).shop_id;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETSHOPINFO, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				
				ShopInfoRes bean = new Gson().fromJson(resp.result, ShopInfoRes.class);
				Log.e("", " - - - "+resp.result.toString());
				if(Api.SUCCEED == bean.result_code) {
					beans = bean.data;
					updateView1(bean.data);
				}
			}
		});
	}
	

	private void updateView(ShopPhone data) {

		
		if(data!=null){
			if(data.mendian_photo.size()>0){
				update(data.mendian_photo);
			}else if(data.diannei_photo.size()>0){
				update(data.diannei_photo);
			}
		}
	}
	
	private void updateView1(final ShopInfoBean shop_info) {
		if(shop_info.is_consumption!=null&&shop_info.is_consumption.equals("1")){
			img_shopzxf.setVisibility(View.VISIBLE);
		}
		
		if(data_list!=null&&data_list.size()>0){
			data_list.clear();
		}
		
		if(!TextUtils.isEmpty(shop_info.mobile)){
			edMobile.setText(shop_info.mobile);
		}
		if(!TextUtils.isEmpty(shop_info.shop_type)){
			String[] s = shop_info.shop_type.split(",");
			if(s.length==1){
				tvShopType.setText(s[0]);
			}else if(s.length==2){
				tvShopType.setText(s[0]+" > "+s[1]);
			}else if(s.length==3){
				tvShopType.setText(s[0]+" > "+s[1]+" > "+s[2]);
			}
		}
		if(!TextUtils.isEmpty(shop_info.shop_name)){
			edName.setText(shop_info.shop_name);
		}
		if(!TextUtils.isEmpty(shop_info.address)){
			tvShopAddr.setText(shop_info.address);
		}
		contents.setText(shop_info.content);
		
		FinalBitmap.create(EditShopActivity.this).display(logo_img,
		Api.HOSTERMA+shop_info.logo,logo_img.getWidth(),logo_img.getHeight(), null, null);
//		 ImageLoadManager.getInstance(EditShopActivity.this).displayImage(Api.HOSTERMA+shop_info.logo,  logo_img);
		photologo = shop_info.logo;
		if(!TextUtils.isEmpty(shop_info.shop_photos)){
			String[] shop_photos = shop_info.shop_photos.split(",");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("img", shop_photos[0]);
			map.put("name", "店铺照片");
			map.put("num", shop_photos[1]);
			map.put("type", shop_photos[2]);
			data_list.add(map);
		}
		if(!TextUtils.isEmpty(shop_info.store_environment_photos)){
			String[] shop_photos = shop_info.store_environment_photos.split(",");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("img", shop_photos[0]);
			map.put("name", "店内环境");
			map.put("num", shop_photos[1]);
			map.put("type", shop_photos[2]);
			data_list.add(map);
		}
		if(!TextUtils.isEmpty(shop_info.services_photos)){
			String[] shop_photos = shop_info.services_photos.split(",");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("img", shop_photos[0]);
			map.put("name", "服务照片");
			map.put("num", shop_photos[1]);
			map.put("type", shop_photos[2]);
			data_list.add(map);
		}
		if(!TextUtils.isEmpty(shop_info.other_photos)){
			String[] shop_photos = shop_info.other_photos.split(",");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("img", shop_photos[0]);
			map.put("name", "其他照片");
			map.put("num", shop_photos[1]);
			map.put("type", shop_photos[2]);
			data_list.add(map);
		}
		sim_adapter.notifyDataSetChanged();
		
		if(!ismap){
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setUpMap();
				if(shop_info.shop_address!=null){
					String[] add = shop_info.shop_address.split(",");
					if(add.length>0){
						ismap = true;
						try {
							initPoiParams(Double.parseDouble(add[0]),Double.parseDouble(add[1]));
						} catch (Exception e) {
							// TODO: handle exception
						}
						
					}else{
//						initPoiParams(CityManager.getInstance(this).latitude,CityManager.getInstance(this).longitude);
					}
				}else{
//					initPoiParams(CityManager.getInstance(this).latitude,CityManager.getInstance(this).longitude);
				}
				queryAroundPOI();
			}
		}, 1000);
	}
	
		
	}

	private void update(List<ShopPhoneBean> mendian_photo) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		for (int i = 0; i < mendian_photo.size(); i++) {
			PhotoBean bean = new PhotoBean();
			bean.url = mendian_photo.get(i).shop_photo;
			data.add(bean);
		}
		data.add(new PhotoBean());
		adapter.notifyDataSetChanged();
	}
	
	
	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if(resultCode == RESULT_OK) {
//			switch(requestCode) {
//			case REQ_SEARCH_POI:
//				setResult(RESULT_OK, data);
//				finish();
//				break;
//			}
//		}
//	}
	/**
	 * 修改资料
	 * @param string 
	 */
	private void loadphoto(final String string,String mobile,String content) {

		loadphoto(string, mobile, content, null);
//		ProgressDialogUtil.showProgressDlg(EditShopActivity.this, "请稍候");
//		SaveShopPhotoRequest req = new SaveShopPhotoRequest();
//		req.shopId = UserInfoManager.getUserInfo(this).shop_id;
//		if(!TextUtils.isEmpty(mobile)){
//			req.mobile = mobile;
//		}
//		if(!TextUtils.isEmpty(string)){
//			req.logo = string;
//		}
//		if(!TextUtils.isEmpty(content)){
//			req.content = content;
//		}
//		Log.e("", ""+req.toString());
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.UPDATESHOPINFO, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//					T.showNetworkError(getApplicationContext());
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
//					if(Api.SUCCEED == bean.result_code) {
//						if(!TextUtils.isEmpty(string)){
//							ImageLoadManager.getInstance(EditShopActivity.this).displayImage(string, logo_img);
//						}
//						loadData();
//						T.showShort(getApplicationContext(),bean.data.RSPMSG);
//					} else {
//						T.showShort(getApplicationContext(), bean.result_desc);
//					}
//			}
//		});
	}

	/**
	 * 修改资料
	 * @param string
	 */
	private void loadphoto(final String string, String mobile, String content, final Callback l) {

		ProgressDialogUtil.showProgressDlg(EditShopActivity.this, "请稍候");
		SaveShopPhotoRequest req = new SaveShopPhotoRequest();
		req.shopId = UserInfoManager.getUserInfo(this).shop_id;
		if(!TextUtils.isEmpty(mobile)){
			req.mobile = mobile;
		}
		if(!TextUtils.isEmpty(string)){
			req.logo = string;
		}
		if(!TextUtils.isEmpty(content)){
			req.content = content;
		}
		Log.e("", ""+req.toString());
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.UPDATESHOPINFO, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(getApplicationContext());
				if (l != null) {
					l.onNetError(arg0.getExceptionCode(), arg1);
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					if(!TextUtils.isEmpty(string)){
						ImageLoadManager.getInstance(EditShopActivity.this).displayImage(string, logo_img);
					}
					loadData();
					T.showShort(getApplicationContext(),bean.data.RSPMSG);
					if (l != null) {
						l.onSuccess(bean.data.RSPMSG);
					}
				} else {
					T.showShort(getApplicationContext(), bean.result_desc);
					if (l != null) {
						l.onFailure(bean.result_desc);
					}
				}
			}
		});
	}

	public void initpop(){
		pop = new PopupWindow(EditShopActivity.this);
		
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view
				.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view
				.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view
				.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, REQUEST_PICK_LOCAL);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
	}
	
	public  static final int TAKE_PICTURE = 1;

	public void photo() {
		
		File dir = new File(Environment.getExternalStorageDirectory()+"/share/");
		if (!dir.exists()){
			dir.mkdirs();
		}
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			photoUri= Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/share/",String.valueOf(System.currentTimeMillis())+"image.png"));
		} catch (Exception e) {
			// TODO: handle exception
//			photoUri= Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
		}
		
		//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
		
	}
	
	public boolean getext(){
		boolean tag = false;
		if(beans!=null){
			Log.e("", ""+beans.content);
			if(edMobile.getText()!=null&&contents.getText()!=null&&!TextUtils.isEmpty(beans.mobile)&&!TextUtils.isEmpty(beans.content)){
				if(!beans.mobile.equals(edMobile.getText().toString())||!beans.content.equals(contents.getText().toString())){
					tag = true;
				}
			}else{
				if(!TextUtils.isEmpty(contents.getText())||!TextUtils.isEmpty(edMobile.getText())){
					tag = true;
				}
			}
		}
		return tag;
	}
	
	private void initDialog(String content,String left,String right) {
		// TODO Auto-generated method stub
		downloadDialog = new TwoButtonDialog(EditShopActivity.this, R.style.CustomDialog,
				"", content, left, right,true,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							finish();
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
			
	}
	
	/**
	 * 监听返回按钮
	 */
//	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(getext()){
				initDialog("退出本次编辑?","否","是");
			}else{
					finish();
			}
		}
		return true;
	}
	
	public void queryPOIs(String keyword) {
		PoiSearch.Query query = new PoiSearch.Query(keyword, "", "");
		query.setPageSize(30);
		query.setPageNum(0);
		PoiSearch poiSearch = new PoiSearch(this, query);
//		poiSearch.setBound(new SearchBound(
//				new LatLonPoint(CityManager.getInstance(this).latitude, CityManager.getInstance(this).longitude)
//				, 10000));
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
		
		
//		CloudSearch mCloudSearch = new CloudSearch(this);// 初始化查询类
//		mCloudSearch.setOnCloudSearchListener(this);// 设置回调函数
//		SearchBound bound = new SearchBound("全国");// 输入city “全国”，为本表全部搜索。
//		mQuery = new CloudSearch.Query(mTableID, "公园", bound);
//		mCloudSearch.searchCloudAsyn(mQuery);
		
	}
	

}
