package com.td.qianhai.epay.oem.views.city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.SelectListNameActivity;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.city.ScrollerNumberPicker.OnSelectListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


/**
 * 城市Picker
 * 
 * @author zd
 * 
 */
public class CityPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker provincePicker;
	private ScrollerNumberPicker cityPicker;
	private ScrollerNumberPicker counyPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时日期 */
	private int tempProvinceIndex = -1;
	private int temCityIndex = -1;
	private int tempCounyIndex = -1;
	private Context context;
//	private List<Cityinfo> province_list = new ArrayList<Cityinfo>();
//	private HashMap<String, List<Cityinfo>> city_map = new HashMap<String, List<Cityinfo>>();
//	private HashMap<String, List<Cityinfo>> couny_map = new HashMap<String, List<Cityinfo>>();
	private static ArrayList<String> province_list_code = new ArrayList<String>();
	private static ArrayList<String> city_list_code = new ArrayList<String>();
	private static ArrayList<String> couny_list_code = new ArrayList<String>();
	
	private ArrayList<HashMap<String, Object>> mList;
	private ArrayList<HashMap<String, Object>> mList1;
	private ArrayList<HashMap<String, Object>> mList2;

	private CitycodeUtil citycodeUtil;
	private String city_code_string;
	private String city_string;
	
	private String provinceid;
	
	private String cityid;
	
	private String regionid;
	
	private String a,b,c;
	
	private onChoiceCytyChilListener Listener;
	
	private int tag = -1;
	
	

	public CityPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		getaddressinfo();
		// TODO Auto-generated constructor stub
	}

	public CityPicker(Context context) {
		super(context);
		this.context = context;
		getaddressinfo();
		// TODO Auto-generated constructor stub
	}
	
	public void getcity(onChoiceCytyChilListener Listener){
		this.Listener = Listener;
		
	};
	
	public void setfirstdata(){
		Listener.onClick(provinceid,cityid,regionid,a,b,c);
		tag = 1;
	}

	// 获取城市信息
	private void getaddressinfo() {
		LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
		citycodeUtil = CitycodeUtil.getSingleton();
		// 获取控件引用
		provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);

		cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
		counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny);
		
		provincePicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				
				// TODO Auto-generated method stub
				System.out.println("id-->" + id + "text----->" + text);
				if (text.equals("") || text == null)
					return;
				if (tempProvinceIndex != id) {
					System.out.println("endselect");
					String selectDay = cityPicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					String selectMonth = counyPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals(""))
						return;
					// 城市数组
//					cityPicker.setData(citycodeUtil.getCity(city_map,
//							citycodeUtil.getProvince_list_code().get(id)));
//					cityPicker.setDefault(1);
//					counyPicker.setData(citycodeUtil.getCouny(couny_map,
//							citycodeUtil.getCity_list_code().get(1)));
//					counyPicker.setDefault(1);
					try {
						provinceid = mList.get(id).get("AREACODE").toString();
					} catch (Exception e) {
						// TODO: handle exception
						provinceid = "";
					}
					try {
						a = mList.get(id).get("AREANAME").toString();
					} catch (Exception e) {
						// TODO: handle exception
						a = "";
					}
					mList1.clear();
					 new Thread(run1).start();	
					
					int lastDay = Integer.valueOf(provincePicker.getListSize());
					if (id > lastDay) {
						provincePicker.setDefault(lastDay - 1);
					}
				}
				tempProvinceIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub
			}
		});
		cityPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub
				if (text.equals("") || text == null)
					return;
				if (temCityIndex != id) {
					String selectDay = provincePicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					String selectMonth = counyPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals(""))
						return;
//					counyPicker.setData(citycodeUtil.getCouny(couny_map,
//							citycodeUtil.getCity_list_code().get(id)));
//					counyPicker.setDefault(1);
					try {
						cityid = mList1.get(id).get("AREACODE").toString();
					} catch (Exception e) {
						// TODO: handle exception
						cityid = "";
					}
					try {
						b = mList1.get(id).get("AREANAME").toString();
					} catch (Exception e) {
						// TODO: handle exception
						b = "";
					}
					mList2.clear();
					 new Thread(run2).start();	

					
					int lastDay = Integer.valueOf(cityPicker.getListSize());
					if (id > lastDay) {
						cityPicker.setDefault(lastDay - 1);
					}
				}
				temCityIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub

			}
		});
		counyPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub

				if (text.equals("") || text == null)
					return;
				if (tempCounyIndex != id) {
					String selectDay = provincePicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					String selectMonth = cityPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals(""))
						return;
					
					try {
						regionid = mList2.get(id).get("AREACODE").toString();
					} catch (Exception e) {
						// TODO: handle exception
						regionid = "";
					}
					try {
						c = mList2.get(id).get("AREANAME").toString();
					} catch (Exception e) {
						// TODO: handle exception
						c = "";
					}
					// 城市数组
					city_code_string = citycodeUtil.getCouny_list_code()
							.get(id);
					Listener.onClick(provinceid,cityid,regionid,a,b,c);
					
					int lastDay = Integer.valueOf(counyPicker.getListSize());
					if (id > lastDay) {
						counyPicker.setDefault(lastDay - 1);
					}
				}
				tempCounyIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}
			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub

			}
		});
		// TODO Auto-generated method stub
		// 读取城市信息string
//		JSONParser parser = new JSONParser();
//		String area_str = FileUtil.readAssets(context, "area.json");
//		province_list = parser.getJSONParserResult(area_str, "area0");
//		// citycodeUtil.setProvince_list_code(parser.province_list_code);
//		city_map = parser.getJSONParserResultArray(area_str, "area1");
//		// System.out.println("city_mapsize" +
//		// parser.city_list_code.toString());
//		// citycodeUtil.setCity_list_code(parser.city_list_code);
//		couny_map = parser.getJSONParserResultArray(area_str, "area2");
//		// citycodeUtil.setCouny_list_code(parser.city_list_code);
//		// System.out.println("couny_mapsize" +
//		// parser.city_list_code.toString());
		mList = new ArrayList<HashMap<String,Object>>();
		new Thread(run).start();	
		
	}
	Runnable run = new Runnable() {

		@Override
		public void run() {
			String[] values = { HttpUrls.INTHEQUERY+"","1"};
			ArrayList<HashMap<String, Object>> list = NetCommunicate.getList(HttpUrls.INTHEQUERY, values,HttpKeys.INTHEQUERY_BACK);
			Message msg = new Message();
			if (list != null) {
						mList.addAll(list);
					if(mList.size()<=0||mList==null){
						
						msg.what = 2;
					}else{
						msg.what = 1;
					}
					} else {
				msg.what = 3;
			}
			handlers.sendMessage(msg);
		}
	};

	private Handler handlers = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:

				List<Cityinfo> list = new ArrayList<Cityinfo>();
				for (int i = 0; i < mList.size(); i++) {
					Cityinfo city = new Cityinfo();
					city.setCity_name(mList.get(i).get("AREANAME").toString());
					city.setId(mList.get(i).get("AREACODE").toString());
					list.add(city);
				}
//				province_list = list;
				try {
					provinceid = mList.get(0).get("AREACODE").toString();
				} catch (Exception e) {
					// TODO: handle exception
					provinceid = "";
				}
				try {
					a = mList.get(0).get("AREANAME").toString();
				} catch (Exception e) {
					// TODO: handle exception
					a = "";
				}
				mList1 = new ArrayList<HashMap<String,Object>>();
				provincePicker.setData(citycodeUtil.getProvince(list));
				provincePicker.setDefault(0);
				
			
				
				 new Thread(run1).start();	
				break;
			case 2:
				
//				Toast.makeText(context.getApplicationContext(),"获取列表失败",
//						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"没有获取到您的订单信息");
				break;
			case 3:
//				Toast.makeText(context.getApplicationContext(),"网络异常,请检查您的网络",
//						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"订单信息获取失败");
				break;
			default:
				break;
			}
		};
	};
	
	
	Runnable run1 = new Runnable() {

		@Override
		public void run() {
			String[] values = { HttpUrls.INTHEQUERY+"",provinceid};
			ArrayList<HashMap<String, Object>> list = NetCommunicate.getList(HttpUrls.INTHEQUERY, values,HttpKeys.INTHEQUERY_BACK);
			Message msg = new Message();
			if (list != null) {
						mList1.addAll(list);
					if(mList1.size()<=0||mList1==null){
						
						msg.what = 2;
					}else{
						msg.what = 1;
					}
					} else {
				msg.what = 3;
			}
			handlers1.sendMessage(msg);
		}
	};

	private Handler handlers1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				HashMap<String, List<Cityinfo>> hashMap = new HashMap<String, List<Cityinfo>>();
				List<Cityinfo> list = new ArrayList<Cityinfo>();
				for (int i = 0; i < mList1.size(); i++) {
					Cityinfo city = new Cityinfo();
					city.setCity_name(mList1.get(i).get("AREANAME").toString());
					city.setId(mList1.get(i).get("AREACODE").toString());
					list.add(city);
					hashMap.put(provinceid, list);
				}
				try {
					cityid = mList1.get(0).get("AREACODE").toString();
				} catch (Exception e) {
					// TODO: handle exception
					cityid = "";
				}
				try {
					b = mList1.get(0).get("AREANAME").toString();
				} catch (Exception e) {
					// TODO: handle exception
					b = "";
				}
				
//				city_map = hashMap;
				mList2 = new ArrayList<HashMap<String,Object>>();
				cityPicker.setData(citycodeUtil.getCity(hashMap,provinceid));
				cityPicker.setDefault(0);
				if(hashMap.size()>=1){
//					cityPicker.setDefault(1);
				}else{
					
				}
				
				 new Thread(run2).start();	
				 
				break;
			case 2:
				
//				Toast.makeText(context.getApplicationContext(),"获取列表失败",
//						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"没有获取到您的订单信息");
				break;
			case 3:
//				Toast.makeText(context.getApplicationContext(),"网络异常,请检查您的网络",
//						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"订单信息获取失败");
				break;
			default:
				break;
			}
		};
	};
	
	
	
	Runnable run2 = new Runnable() {

		@Override
		public void run() {
			String[] values = { HttpUrls.INTHEQUERY+"",cityid};
			ArrayList<HashMap<String, Object>> list = NetCommunicate.getList(HttpUrls.INTHEQUERY, values,HttpKeys.INTHEQUERY_BACK);
			Message msg = new Message();
			if (list != null) {
						mList2.addAll(list);
					if(mList2.size()<=0||mList2==null){
						
						msg.what = 2;
					}else{
						msg.what = 1;
					}
					} else {
				msg.what = 3;
			}
			handlers2.sendMessage(msg);
		}
	};

	private Handler handlers2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				 if(counyPicker.getVisibility()==View.INVISIBLE){
					 counyPicker.setVisibility(View.VISIBLE);
				 }
				HashMap<String, List<Cityinfo>> hashMap = new HashMap<String, List<Cityinfo>>();
				List<Cityinfo> list = new ArrayList<Cityinfo>();
				for (int i = 0; i < mList2.size(); i++) {
					Cityinfo city = new Cityinfo();
					city.setCity_name(mList2.get(i).get("AREANAME").toString());
					city.setId(mList2.get(i).get("AREACODE").toString());
					list.add(city);
					hashMap.put(cityid, list);
				}
				
				try {
					regionid = mList2.get(0).get("AREACODE").toString();
				} catch (Exception e) {
					// TODO: handle exception
					regionid = "";
				}
				try {
					c = mList2.get(0).get("AREANAME").toString();
				} catch (Exception e) {
					// TODO: handle exception
					c = "";
				}
				if(tag!=-1){
					Listener.onClick(provinceid,cityid,regionid,a,b,c);
				}
				counyPicker.setData(citycodeUtil.getCouny(hashMap, cityid));
				counyPicker.setDefault(0);
//				couny_map = hashMap;
				
				break;
			case 2:
				regionid = "";
				c = "";
				counyPicker.setVisibility(View.INVISIBLE);
				Listener.onClick(provinceid,cityid,regionid,a,b,c);
//				Toast.makeText(context.getApplicationContext(),"无区/县级,可不填",
//						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"没有获取到您的订单信息");
				break;
			case 3:
//				Toast.makeText(context.getApplicationContext(),"网络异常,请检查您的网络",
//						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"订单信息获取失败");
				break;
			default:
				break;
			}
		};
	};
	

//	public static class JSONParser {
//		public ArrayList<String> province_list_code = new ArrayList<String>();
//		public ArrayList<String> city_list_code = new ArrayList<String>();
//
//		public List<Cityinfo> getJSONParserResult(String JSONString, String key) {
//			List<Cityinfo> list = new ArrayList<Cityinfo>();
//			JsonObject result = new JsonParser().parse(JSONString)
//					.getAsJsonObject().getAsJsonObject(key);
//
//			Iterator iterator = result.entrySet().iterator();
//			while (iterator.hasNext()) {
//				Map.Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
//						.next();
//				Cityinfo cityinfo = new Cityinfo();
//
//				cityinfo.setCity_name(entry.getValue().getAsString());
//				cityinfo.setId(entry.getKey());
//				province_list_code.add(entry.getKey());
//				list.add(cityinfo);
//			}
//			System.out.println(province_list_code.size());
//			return list;
//		}
//
//		public HashMap<String, List<Cityinfo>> getJSONParserResultArray(
//				String JSONString, String key) {
//			HashMap<String, List<Cityinfo>> hashMap = new HashMap<String, List<Cityinfo>>();
//			JsonObject result = new JsonParser().parse(JSONString)
//					.getAsJsonObject().getAsJsonObject(key);
//
//			Iterator iterator = result.entrySet().iterator();
//			while (iterator.hasNext()) {
//				Map.Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
//						.next();
//				List<Cityinfo> list = new ArrayList<Cityinfo>();
//				JsonArray array = entry.getValue().getAsJsonArray();
//				for (int i = 0; i < array.size(); i++) {
//					Cityinfo cityinfo = new Cityinfo();
//					cityinfo.setCity_name(array.get(i).getAsJsonArray().get(0)
//							.getAsString());
//					cityinfo.setId(array.get(i).getAsJsonArray().get(1)
//							.getAsString());
//					city_list_code.add(array.get(i).getAsJsonArray().get(1)
//							.getAsString());
//					list.add(cityinfo);
//				}
//				hashMap.put(entry.getKey(), list);
//			}
//			return hashMap;
//		}
//	}

	
	
//	@Override
//	protected void onFinishInflate() {
//		super.onFinishInflate();
//		LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
//		citycodeUtil = CitycodeUtil.getSingleton();
//		// 获取控件引用
//		provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);
//
//		cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
//		counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny);
////		provincePicker.setData(citycodeUtil.getProvince(province_list));
////		provincePicker.setDefault(1);
////		cityPicker.setData(citycodeUtil.getCity(city_map, citycodeUtil
////				.getProvince_list_code().get(1)));
////		cityPicker.setDefault(1);
////		counyPicker.setData(citycodeUtil.getCouny(couny_map, citycodeUtil
////				.getCity_list_code().get(1)));
////		counyPicker.setDefault(1);
//		provincePicker.setOnSelectListener(new OnSelectListener() {
//
//			@Override
//			public void endSelect(int id, String text) {
//				// TODO Auto-generated method stub
//				System.out.println("id-->" + id + "text----->" + text);
//				if (text.equals("") || text == null)
//					return;
//				if (tempProvinceIndex != id) {
//					System.out.println("endselect");
//					String selectDay = cityPicker.getSelectedText();
//					if (selectDay == null || selectDay.equals(""))
//						return;
//					String selectMonth = counyPicker.getSelectedText();
//					if (selectMonth == null || selectMonth.equals(""))
//						return;
//					// 城市数组
//					cityPicker.setData(citycodeUtil.getCity(city_map,
//							citycodeUtil.getProvince_list_code().get(id)));
//					cityPicker.setDefault(1);
//					counyPicker.setData(citycodeUtil.getCouny(couny_map,
//							citycodeUtil.getCity_list_code().get(1)));
//					counyPicker.setDefault(1);
//					int lastDay = Integer.valueOf(provincePicker.getListSize());
//					if (id > lastDay) {
//						provincePicker.setDefault(lastDay - 1);
//					}
//				}
//				tempProvinceIndex = id;
//				Message message = new Message();
//				message.what = REFRESH_VIEW;
//				handler.sendMessage(message);
//			}
//
//			@Override
//			public void selecting(int id, String text) {
//				// TODO Auto-generated method stub
//			}
//		});
//		cityPicker.setOnSelectListener(new OnSelectListener() {
//
//			@Override
//			public void endSelect(int id, String text) {
//				// TODO Auto-generated method stub
//				if (text.equals("") || text == null)
//					return;
//				if (temCityIndex != id) {
//					String selectDay = provincePicker.getSelectedText();
//					if (selectDay == null || selectDay.equals(""))
//						return;
//					String selectMonth = counyPicker.getSelectedText();
//					if (selectMonth == null || selectMonth.equals(""))
//						return;
//					counyPicker.setData(citycodeUtil.getCouny(couny_map,
//							citycodeUtil.getCity_list_code().get(id)));
//					counyPicker.setDefault(1);
//					int lastDay = Integer.valueOf(cityPicker.getListSize());
//					if (id > lastDay) {
//						cityPicker.setDefault(lastDay - 1);
//					}
//				}
//				temCityIndex = id;
//				Message message = new Message();
//				message.what = REFRESH_VIEW;
//				handler.sendMessage(message);
//			}
//
//			@Override
//			public void selecting(int id, String text) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//		counyPicker.setOnSelectListener(new OnSelectListener() {
//
//			@Override
//			public void endSelect(int id, String text) {
//				// TODO Auto-generated method stub
//
//				if (text.equals("") || text == null)
//					return;
//				if (tempCounyIndex != id) {
//					String selectDay = provincePicker.getSelectedText();
//					if (selectDay == null || selectDay.equals(""))
//						return;
//					String selectMonth = cityPicker.getSelectedText();
//					if (selectMonth == null || selectMonth.equals(""))
//						return;
//					// 城市数组
//					city_code_string = citycodeUtil.getCouny_list_code()
//							.get(id);
//					int lastDay = Integer.valueOf(counyPicker.getListSize());
//					if (id > lastDay) {
//						counyPicker.setDefault(lastDay - 1);
//					}
//				}
//				tempCounyIndex = id;
//				Message message = new Message();
//				message.what = REFRESH_VIEW;
//				handler.sendMessage(message);
//			}
//
//			@Override
//			public void selecting(int id, String text) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null)
					onSelectingListener.selected(true);
				break;
			default:
				break;
			}
		}

	};

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	public String getCity_code_string() {
		return city_code_string;
	}

	public String getCity_string() {
		city_string = provincePicker.getSelectedText()
				+ cityPicker.getSelectedText() + counyPicker.getSelectedText();
		return city_string;
	}

	public interface OnSelectingListener {

		public void selected(boolean selected);
	}
}
