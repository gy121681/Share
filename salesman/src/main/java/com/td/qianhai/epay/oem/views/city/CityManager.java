package com.td.qianhai.epay.oem.views.city;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.td.qianhai.epay.oem.beans.CityInfo;

public class CityManager {

	private static CityManager instance;

	private Context context;

	// 声明AMapLocationClient类对象
	public AMapLocationClient mLocationClient;
	// 声明定位回调监听器
	public AMapLocationListener mLocationListener;
	// 声明mLocationOption对象
	public AMapLocationClientOption mLocationOption;
	
	private LocationCallback callback;
	
	public double latitude;
	public double longitude;
	public String locCity;			//定位城市名称
	public CityInfo locCityInfo;	//定位城市信息（已开通）
	public CityInfo cityInfo;		//用户选择城市
	
	private CityManager(Context context) {
		this.context = context;
		initAMap();
	}

	public static synchronized CityManager getInstance(Context context) {
		if (instance == null)
			instance = new CityManager(context);
		return instance;
	}

	/**
	 * 设置高德地图SDK
	 */
	public void initAMap() {
		mLocationListener = new AMapLocationListener() {
			@Override
			public void onLocationChanged(AMapLocation amapLocation) {
				if (amapLocation != null) {
			        if (amapLocation.getErrorCode() == 0) {
				        //定位成功回调信息，设置相关消息
//				        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
				        latitude = amapLocation.getLatitude();//获取纬度
				        longitude = amapLocation.getLongitude();//获取经度
				        
				        Log.e("", ""+latitude);
				        Log.e("", ""+longitude);
//				        amapLocation.getAccuracy();//获取精度信息
//				        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				        Date date = new Date(amapLocation.getTime());
//				        df.format(date);//定位时间
//				        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果
//				        amapLocation.getCountry();//国家信息
//				        amapLocation.getProvince();//省信息
				        locCity = amapLocation.getCity();//城市信息
//				        amapLocation.getDistrict();//城区信息
//				        amapLocation.getRoad();//街道信息
//				        amapLocation.getCityCode();//城市编码
//				        amapLocation.getAdCode();//地区编码
				        
				        getCityByLatLng(latitude, longitude);
			    } else {
			    	
			        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
			        Log.e("AmapError","location Error, ErrCode:"
			            + amapLocation.getErrorCode() + ", errInfo:"
			            + amapLocation.getErrorInfo());
			        stopLocation();
//			        callback.getCity(null);
			        return;
			        }
			    }else{
			    	   Log.e("AmapError","location Error, ErrCode:");
			    }
			}
		};
		
		// 初始化定位
		mLocationClient = new AMapLocationClient(context);
		// 设置定位回调监听
		mLocationClient.setLocationListener(mLocationListener);
		
		//初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		//设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		//设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(false);
		//设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		//设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		//设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(2000);
		mLocationOption.setOnceLocation(true);
		//给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
	}
	
	public void requestLocation() {
		if(mLocationClient != null)
			mLocationClient.startLocation();
	}
	
	public void stopLocation() {
		if(mLocationClient != null)
			mLocationClient.stopLocation();
	}
	
	/**
	 * 通过经纬度查询城市信息
	 */
	public void getCityByLatLng(double lat, double lng) {
//		CityInfoRequest req = new CityInfoRequest();
//		req.latitude = lat;
//		req.longitude = lng;
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_CITY_BY_LATLNG, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				if(callback != null)
//					callback.getCity(null);
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				CityInfoResponse bean = new Gson().fromJson(resp.result, CityInfoResponse.class);
//				cityInfo = bean.data;
//				locCityInfo = cityInfo;
//
//				if(callback != null)
//					callback.getCity(cityInfo);
//			}
//		});
	}
	
	/**
	 * 获取城市信息
	 */
	public void getCityInfo() {
		if(callback != null) {
			if(cityInfo != null)
				callback.getCity(cityInfo);
			else if(latitude==0 && longitude==0)
				requestLocation();
			else
				getCityByLatLng(latitude, longitude);
		}
	}
	
	public int getCityId() {
		if(cityInfo != null)
			return cityInfo.id;
		return 0;
	}
	
	public void setLocationCallback(LocationCallback callback) {
		this.callback = callback;
	}
	
	public interface LocationCallback {
		public void getCity(CityInfo cityInfo);
	}
}
