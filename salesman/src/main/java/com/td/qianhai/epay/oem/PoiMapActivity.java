package com.td.qianhai.epay.oem;

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
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.td.qianhai.epay.oem.adapter.PoiListAdapter;
import com.td.qianhai.epay.oem.views.city.CityManager;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class PoiMapActivity extends BaseActivity
	implements LocationSource, AMapLocationListener, OnPoiSearchListener,OnMapClickListener, OnItemClickListener, OnClickListener {
	
	private static final int REQ_SEARCH_POI = 0x101;
	
	private ListView lvPOI;
	
	private MapView mapView;
	private AMap aMap;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private Marker locationMarker; // 选择的点
	private Button btnSearch;
	private EditText edSearchKeyWord;
	private boolean tag = true;
	private double latitude = 0.0;
	private double longitude = 0.0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_map);
		latitude = getIntent().getDoubleExtra("latitude", 0);
		longitude = getIntent().getDoubleExtra("longitude", 0);
		initView(savedInstanceState);

	}
	
	public void initView(Bundle savedInstanceState) {
//		((TextView) findViewById(R.id.tv_title_contre)).setText("选择地址");
		lvPOI = (ListView) findViewById(R.id.lvPOIS);
		btnSearch  = (Button) findViewById(R.id.btnSearch);
		edSearchKeyWord   =  (EditText) findViewById(R.id.edSearchKeyWord);
		lvPOI.setOnItemClickListener(this);
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.onCreate(savedInstanceState);
		findViewById(R.id.bt_title_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(edSearchKeyWord.getText().toString()!=null){
					queryPOIs(edSearchKeyWord.getText().toString());
				}
			}
		});
		
		
		setUpMap();
		if(latitude>0&&longitude>0){
			initPoiParams(latitude, longitude);
		}else{
			initPoiParams(CityManager.getInstance(this).latitude, CityManager.getInstance(this).longitude);
		}
		
		queryAroundPOI();
	}

	
	public void queryPOIs(String keyword) {
		tag = true;
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
	
	public void initPoiParams(double latitude,double longitude) {
		
		locationMarker = aMap.addMarker(new MarkerOptions()
			.anchor(0.5f, 1)
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_60))
			.position(new LatLng(latitude, longitude)));
		locationMarker.showInfoWindow();
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
				
				if(tag){
					LatLng latng= new LatLng(result.getPois().get(0).getLatLonPoint().getLatitude(), result.getPois().get(0).getLatLonPoint().getLongitude());
					aMap.clear();
					locationMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_60))
							.position(latng));
					locationMarker.showInfoWindow();
//					queryAroundPOI();
					
//					setUpMap();
					
					initPoiParams(result.getPois().get(0).getLatLonPoint().getLatitude(), 
							result.getPois().get(0).getLatLonPoint().getLongitude());
					tag = false;
					
					
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
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
//		case R.id.btnTopSearch:
//			startActivityForResult(new Intent(this, SearchPOIActivity.class), REQ_SEARCH_POI);
//			break;
		}
	}

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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			switch(requestCode) {
			case REQ_SEARCH_POI:
				setResult(RESULT_OK, data);
				finish();
				break;
			}
		}
	}
}
