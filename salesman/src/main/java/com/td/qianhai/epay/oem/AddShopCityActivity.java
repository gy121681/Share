package com.td.qianhai.epay.oem;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.td.qianhai.epay.oem.beans.AddShopBean;
import com.td.qianhai.epay.oem.beans.AddShopRequest;
import com.td.qianhai.epay.oem.beans.AddShopResponce;
import com.td.qianhai.epay.oem.beans.AddShopRsp;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.CityInfo;
import com.td.qianhai.epay.oem.beans.GetcityRequst;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.ShopCityInfoResponce;
import com.td.qianhai.epay.oem.beans.UpShopInfoRequst;
import com.td.qianhai.epay.oem.beans.UpShopInfoRequst1;
import com.td.qianhai.epay.oem.mail.utils.ConnUtil;
import com.td.qianhai.epay.oem.views.city.CityManager;
import com.td.qianhai.epay.oem.views.city.CityManager.LocationCallback;

public class AddShopCityActivity extends BaseActivity implements OnClickListener , LocationCallback{
	
	private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv_go;
	private EditText ed7;
	private String tag = "",collect_id = "",pro_id = "",city_id = "",area_id = "",one_id = "",two_id = "",addr ="";
	private double lat,lng;
	private CityManager cityManager;
	private AddShopBean datas;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addshopcity_activity);
		AppContext.getInstance().addActivity1(this);
		AppContext.getInstance().addActivity(this);
		
		initview();
		initmap();
	}

	private void initview() {
		// TODO Auto-generated method stub
		collect_id = getIntent().getStringExtra("collect_id");
		tag =  getIntent().getStringExtra("tag");
		((TextView) findViewById(R.id.tv_title_contre)).setText("提交商户");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv4 = (TextView) findViewById(R.id.tv4);
		tv5 = (TextView) findViewById(R.id.tv5);
		tv6 = (TextView) findViewById(R.id.tv6);
		ed7 = (EditText) findViewById(R.id.ed7);
		tv_go = (TextView) findViewById(R.id.tv_go);
		tv_go.setOnClickListener(this);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		tv4.setOnClickListener(this);
		tv5.setOnClickListener(this);
		tv6.setOnClickListener(this);
		if(tag!=null&&tag.equals("1")){
			getinfo();
		}
	}
	
	private void getinfo() {
		// TODO Auto-generated method stub
		showLoadingDialog("请稍候...");
		AddShopRequest req = new AddShopRequest();
		req.collect_shop_id = collect_id;

		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.DETAILS, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				try {
					loadingDialogWhole.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				AddShopRsp bean = new Gson().fromJson(resp.result, AddShopRsp.class);
				if(0== bean.result_code) {
//				Toast.makeText(getApplicationContext(), "提交", Toast.LENGTH_SHORT).show();
				initinfo(bean.data);
				datas = bean.data ;
				} else {
					Toast.makeText(getApplicationContext(), bean.result_desc, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				try {
					loadingDialogWhole.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				Toast.makeText(getApplicationContext(),"请求失败", Toast.LENGTH_SHORT).show();
			}
		});
		
		
	}
	
	private void initinfo(AddShopBean data) {
		
		if(!TextUtils.isEmpty(data.province_id)){
			querypro(tv1,data.province_id);
			pro_id = data.province_id;
		}
		if(!TextUtils.isEmpty(data.city_id)){
			querycity(tv2,data.city_id);
			city_id = data.city_id;
		}
		if(!TextUtils.isEmpty(data.area_id)){
			queryarea(tv3,data.area_id);
			area_id = data.area_id;
		}
		if(!TextUtils.isEmpty(data.shop_type_id)){
			queryone(tv4,data.shop_type_id);
			one_id = data.shop_type_id;
		}
		if(!TextUtils.isEmpty(data.shop_child_type_id)){
			querytwo(tv5,data.shop_child_type_id);
			two_id = data.shop_child_type_id;
		}





		lat = data.latitude;
		lng = data.longitude;
		ed7.setText(data.address);
		if( data.longitude>0){
			tv6.setText( data.longitude+" - "+data.latitude);
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(ConnUtil.isFastClick()){
			return;
		}
		switch (v.getId()) {
		case R.id.tv1:
			showLoadingDialog("请稍候...");
			querypro(tv1,"");
			break;
		case R.id.tv2:
			if(TextUtils.isEmpty(pro_id)){
				Texterro(tv1, "请先选择省份");
				return;
			}

			showLoadingDialog("请稍候...");
			querycity(tv2,"");
			break;
		case R.id.tv3:
			if(TextUtils.isEmpty(city_id)){
				Texterro(tv2, "请先选择城市");
				return;
			}
			
			showLoadingDialog("请稍候...");
			queryarea(tv3,"");
			break;
		case R.id.tv4:
			if(TextUtils.isEmpty(city_id)){
				Texterro(tv4, "请先选择城市");
				return;
			}
			
			showLoadingDialog("请稍候...");
			queryone(tv4,"");
			break;
		case R.id.tv5:
			if(TextUtils.isEmpty(city_id)){
				Texterro(tv4, "请先选择城市");
				return;
			}
			showLoadingDialog("请稍候...");
			querytwo(tv5,"");
			break;
		case R.id.tv6:
				Intent it= new Intent(AddShopCityActivity.this, PoiMapActivity.class);
				if(datas!=null&&datas.latitude>0){
					it.putExtra("latitude", datas.latitude);
					it.putExtra("longitude", datas.longitude);
				}
				startActivityForResult(it, 8);
//			  startActivityForResult(new Intent(AddShopCityActivity.this, PoiMapActivity.class), 8);
			break;
		case R.id.tv_go:
			
			change();
			
			break;

		default:
			break;
		}
		
	}
	
	private void change() {
		// TODO Auto-generated method stub
		
		if(TextUtils.isEmpty(tv1.getText())){
			Texterro(tv1, "请选择省份");
			return;
		}
		if(TextUtils.isEmpty(tv2.getText())){
			Texterro(tv2, "请选择城市");
			return;
		}
		if(TextUtils.isEmpty(tv3.getText())){
			Texterro(tv3, "请选择区域");
			return;
		}
		if(TextUtils.isEmpty(tv4.getText())){
			Texterro(tv4, "请选择一级分类");
			return;
		}
		if(TextUtils.isEmpty(tv5.getText())){
			Texterro(tv5, "请选择二级分类");
			return;
		}
		if(TextUtils.isEmpty(tv6.getText())){
			Texterro(tv6, "请选择位置");
			return;
		}
		if(checkEditEmpty(ed7, "填写详细地址")){
			return;
		}
		showLoadingDialog("请稍候...");
		
		UpShopInfoRequst1 req = new UpShopInfoRequst1();
		req.collect_id = collect_id;
		req.province_id = pro_id;
		req.city_id = city_id;
		req.area_id = area_id;
		req.shop_type_id = one_id;
		req.shop_child_type_id = two_id;
		req.longitude = lng;
		req.latitude = lat;
		req.address = ed7.getText().toString();
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.UPDATESHOP, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				try {
					loadingDialogWhole.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				AddShopResponce bean = new Gson().fromJson(resp.result, AddShopResponce.class);
				if(0== bean.result_code) {
					
					Intent it = new Intent(AddShopCityActivity.this,AddShopPhotoActivity.class);
					it.putExtra("tag", tag);
					it.putExtra("collect_id", collect_id);
					startActivity(it);
					
				} else {
					Toast.makeText(getApplicationContext(), bean.result_desc, Toast.LENGTH_SHORT).show();
//					T.showShort(AddrEditActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				try {
					loadingDialogWhole.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				Toast.makeText(getApplicationContext(),"请求失败", Toast.LENGTH_SHORT).show();
//				T.showNetworkError(AddrEditActivity.this);
			}
		});
		
	}

	private void querytwo(final TextView v, final String shop_child_type_id) {
		// TODO Auto-generated method stub
		
		
		GetcityRequst req = new GetcityRequst();
		req.city_id = city_id;
		req.parent_id  = one_id;
		
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.GETSHOPTYPELIST, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				try {
					loadingDialogWhole.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				ShopCityInfoResponce bean = new Gson().fromJson(resp.result, ShopCityInfoResponce.class);
				if(0== bean.result_code) {
					if(shop_child_type_id!=null&&!shop_child_type_id.equals("")){
						for (int i = 0; i < bean.data.size(); i++) {
							if(shop_child_type_id.equals(bean.data.get(i).id)){
								 v.setText(bean.data.get(i).type_name);
							}
						}
					}else{
					List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
					for (int i = 0; i < bean.data.size(); i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("id", bean.data.get(i).id);
						map.put("name", bean.data.get(i).type_name);
						list.add(map);
					}
					initdialog(list,v,5);
					}
				} else {
					Toast.makeText(getApplicationContext(), bean.result_desc, Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(getApplicationContext(),"请求失败", Toast.LENGTH_SHORT).show();
			}
		});
		
	}

	private void queryone(final TextView v, final String shop_type_id) {
		// TODO Auto-generated method stub
		
		
		GetcityRequst req = new GetcityRequst();
		req.city_id = city_id;
		req.parent_id  = "0";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.GETSHOPTYPELIST, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				try {
					loadingDialogWhole.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				ShopCityInfoResponce bean = new Gson().fromJson(resp.result, ShopCityInfoResponce.class);
				if(0== bean.result_code) {
					if(shop_type_id!=null&&!shop_type_id.equals("")){
						for (int i = 0; i < bean.data.size(); i++) {
							if(shop_type_id.equals(bean.data.get(i).id)){
								 v.setText(bean.data.get(i).type_name);
							}
						}
					}else{
					List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
					for (int i = 0; i < bean.data.size(); i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("id", bean.data.get(i).id);
						map.put("name", bean.data.get(i).type_name);
						list.add(map);
					}
					initdialog(list,v,4);
					}
				} else {
					Toast.makeText(getApplicationContext(), bean.result_desc, Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(getApplicationContext(),"请求失败", Toast.LENGTH_SHORT).show();
			}
		});
		
	}

	private void queryarea(final TextView v, final String area_id) {
		// TODO Auto-generated method stub
		
		GetcityRequst req = new GetcityRequst();
		req.city_id = city_id;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.AREALIST, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				try {
					loadingDialogWhole.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				ShopCityInfoResponce bean = new Gson().fromJson(resp.result, ShopCityInfoResponce.class);
				if(0== bean.result_code) {
					if(area_id!=null&&!area_id.equals("")){
						for (int i = 0; i < bean.data.size(); i++) {
							if(area_id.equals(bean.data.get(i).id)){
								 v.setText(bean.data.get(i).area_name);
							}
						}
					}else{
					List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
					for (int i = 0; i < bean.data.size(); i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("id", bean.data.get(i).id);
						map.put("name", bean.data.get(i).area_name);
						list.add(map);
					}
					initdialog(list,v,3);
					}
				} else {
					Toast.makeText(getApplicationContext(), bean.result_desc, Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(getApplicationContext(),"请求失败", Toast.LENGTH_SHORT).show();
			}
		});
		
	}

	private void querycity(final TextView v, final String city_id) {
		// TODO Auto-generated method stub
		
		GetcityRequst req = new GetcityRequst();
		req.province_id = pro_id;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.CITYINFO, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				try {
					loadingDialogWhole.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				ShopCityInfoResponce bean = new Gson().fromJson(resp.result, ShopCityInfoResponce.class);
				if(0== bean.result_code) {
					if(city_id!=null&&!city_id.equals("")){
						for (int i = 0; i < bean.data.size(); i++) {
							if(city_id.equals(bean.data.get(i).id)){
								 v.setText(bean.data.get(i).city_name);
							}
						}
					}else{
					
					List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
					for (int i = 0; i < bean.data.size(); i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("id", bean.data.get(i).id);
						map.put("name", bean.data.get(i).city_name);
						list.add(map);
					}
					initdialog(list,v,2);
					}
				} else {
					Toast.makeText(getApplicationContext(), bean.result_desc, Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(getApplicationContext(),"请求失败", Toast.LENGTH_SHORT).show();
			}
		});
		
	}

	private void querypro(final TextView v, final String province_id) {
		// TODO Auto-generated method stub
		
		AddShopRequest req = new AddShopRequest();
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.PROVINCE, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				try {
					loadingDialogWhole.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				ShopCityInfoResponce bean = new Gson().fromJson(resp.result, ShopCityInfoResponce.class);
				if(0== bean.result_code) {
					
					if(province_id!=null&&!province_id.equals("")){
						for (int i = 0; i < bean.data.size(); i++) {
							if(province_id.equals(bean.data.get(i).id)){
								 v.setText(bean.data.get(i).province_name);
							}
						}
						
					}else{
						List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
						for (int i = 0; i < bean.data.size(); i++) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("id", bean.data.get(i).id);
							map.put("name", bean.data.get(i).province_name);
							list.add(map);
						}
						initdialog(list,v,1);
					}
					

				} else {
					Toast.makeText(getApplicationContext(), bean.result_desc, Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(getApplicationContext(),"请求失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void initdialog(final List<HashMap<String, Object>> data, final TextView tv ,final int a) {
		// TODO Auto-generated method stub

		
//		final AlertDialog.Builder dialog = new AlertDialog.Builder(this,R.style.MyEditDialog1);
		final Dialog dialog = new Dialog(this,R.style.MyEditDialog1);
//		dialog.setTitle("选择");
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		ListView lv = new ListView(this); 
		ProvinceAdapter pAdapter = new ProvinceAdapter(data);
		lv.setAdapter(pAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View initbank, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(a==1){
					pro_id = data.get((int)arg2).get("id").toString();
					tv2.setText("");
					tv3.setText("");
				}else if(a==2){
					city_id = data.get((int)arg2).get("id").toString();
					tv3.setText("");
				}else if(a==3){
					area_id = data.get((int)arg2).get("id").toString();
				}else if(a==4){
					one_id = data.get((int)arg2).get("id").toString();
				}else if(a==5){
					two_id = data.get((int)arg2).get("id").toString();
				}
				
				
				tv.setText(data.get((int)arg2).get("name").toString());
				dialog.dismiss();
			}
		});
		
	
		dialog.setContentView(lv);
		dialog.show();
	}
	
	
	class ProvinceAdapter extends BaseAdapter{
		public List<HashMap<String, Object>>  adapter_list;
		public ProvinceAdapter(List<HashMap<String, Object>> b){
			adapter_list = b;
		}
		
		@Override
		public int getCount() {
			return adapter_list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			
//			LayoutInflater inflater = LayoutInflater.from(AddShopCityActivity.this);
//			View view = inflater.inflate(R.layout.dailog_item, null);
//			TextView v = (TextView) view.findViewById(R.id.tvs);
//			v.setText(adapter_list.get(position).get("name").toString());
			
			TextView tv = new TextView(AddShopCityActivity.this);
//			 LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( 
//	                    500, 45); 
//			tv.setLayoutParams(lp);
			tv.setTextColor(getResources().getColor(R.color.black));
			tv.setWidth(800);
			tv.setPadding(20, 20, 20, 20);
			tv.setTextSize(18);
			tv.setText(adapter_list.get(position).get("name").toString());
			return tv;
		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(resultCode == this.RESULT_OK) {
			switch(requestCode) {
			case 8:
				 addr = intent.getStringExtra("addr");
				 lat = intent.getDoubleExtra("lat", lat);
				 lng = intent.getDoubleExtra("lng", lng);
				 
				 tv6.setText(addr);
			}
		}
	}
	
	private void initmap() {
		// TODO Auto-generated method stub
		cityManager = CityManager.getInstance(this);
		cityManager.setLocationCallback(this);
		cityManager.getCityInfo();
	}
	
	@Override
	public void getCity(CityInfo cityInfo) {
		// TODO Auto-generated method stub
		
	}
}
