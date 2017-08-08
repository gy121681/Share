package com.shareshenghuo.app.shop.city;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.city.ScrollerNumberPicker.OnSelectListener;
import com.shareshenghuo.app.shop.network.request.CityRequest;
import com.shareshenghuo.app.shop.network.response.CityResponse;
import com.shareshenghuo.app.shop.networkapi.Api;

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
	private List<Cityinfo> province_list = new ArrayList<Cityinfo>();
	private HashMap<String, List<Cityinfo>> city_map = new HashMap<String, List<Cityinfo>>();
	private HashMap<String, List<Cityinfo>> couny_map = new HashMap<String, List<Cityinfo>>();
	private static ArrayList<String> province_list_code = new ArrayList<String>();
	private static ArrayList<String> city_list_code = new ArrayList<String>();
	private static ArrayList<String> couny_list_code = new ArrayList<String>();

	// private ArrayList<HashMap<String, Object>> mList;
	// private ArrayList<HashMap<String, Object>> mList1;
	// private ArrayList<HashMap<String, Object>> mList2;

	private CityResponse bean;
	private CityResponse bean1;
	private CityResponse bean2;

	private CitycodeUtil citycodeUtil;
	private String city_code_string;
	private String city_string;

	private String provinceid;

	private String cityid;

	private String regionid;

	private String a, b, c;

	private onChoiceCytyChilListener Listener;

	private int tag = -1;

	public CityPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		getaddressinfo();
	}

	public CityPicker(Context context) {
		super(context);
		this.context = context;
		getaddressinfo();
	}

	public void getcity(onChoiceCytyChilListener Listener) {
		this.Listener = Listener;

	};

	public void setfirstdata() {
		Listener.onClick(provinceid, cityid, regionid, a, b, c);
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
					// cityPicker.setData(citycodeUtil.getCity(city_map,
					// citycodeUtil.getProvince_list_code().get(id)));
					// cityPicker.setDefault(1);
					// counyPicker.setData(citycodeUtil.getCouny(couny_map,
					// citycodeUtil.getCity_list_code().get(1)));
					// counyPicker.setDefault(1);
					try {
						provinceid = bean.data.get(id).areacode;
					} catch (Exception e) {
						provinceid = "";
					}
					try {
						a = bean.data.get(id).areaname;
					} catch (Exception e) {
						a = "";
					}
					bean1 = null;
					// new Thread(run1).start();
					loadData1();

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
			}
		});
		cityPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				if (text.equals("") || text == null)
					return;
				if (temCityIndex != id) {
					String selectDay = provincePicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					String selectMonth = counyPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals(""))
						return;
					// counyPicker.setData(citycodeUtil.getCouny(couny_map,
					// citycodeUtil.getCity_list_code().get(id)));
					// counyPicker.setDefault(1);
					try {
						cityid = bean1.data.get(id).areacode;
					} catch (Exception e) {
						cityid = "";
					}
					try {
						b = bean1.data.get(id).areaname;
					} catch (Exception e) {
						b = "";
					}
					bean2 = null;
					loadData2();
					// new Thread(run2).start();

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

			}
		});
		counyPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
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
						regionid = bean2.data.get(id).areacode;
					} catch (Exception e) {
						// TODO: handle exception
						regionid = "";
					}
					try {
						c = bean2.data.get(id).areaname;
					} catch (Exception e) {
						// TODO: handle exception
						c = "";
					}
					// 城市数组
					city_code_string = citycodeUtil.getCouny_list_code()
							.get(id);
					Listener.onClick(provinceid, cityid, regionid, a, b, c);

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

			}
		});
		loadData();
	}

	public void loadData() {

		CityRequest req = new CityRequest();
		req.parentCode = "1";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.e("", " - - - - - - " + Api.AREAQUERY);
		new HttpUtils().send(HttpMethod.POST, Api.AREAQUERY, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						bean = new Gson().fromJson(resp.result,
								CityResponse.class);
						if (Api.SUCCEED == bean.result_code) {
							Log.e("", " - - - - - - " + resp.result);
							List<Cityinfo> list = new ArrayList<Cityinfo>();
							for (int i = 0; i < bean.data.size(); i++) {
								Cityinfo city = new Cityinfo();
								city.setCity_name(bean.data.get(i).areaname);
								city.setId(bean.data.get(i).areacode);
								list.add(city);
							}
							try {
								provinceid = bean.data.get(0).areacode;
							} catch (Exception e) {
								provinceid = "";
							}

							try {
								a = bean.data.get(0).areaname;
							} catch (Exception e) {
								a = "";
							}
							provincePicker.setData(citycodeUtil
									.getProvince(list));
							provincePicker.setDefault(0);
							loadData1();
						}
					}
				});

	}

	public void loadData1() {

		CityRequest req = new CityRequest();
		Log.e("", "provinceid = = = =  " + provinceid);
		req.parentCode = provinceid;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.e("", " - - 2222- - - - " + Api.AREAQUERY);
		new HttpUtils().send(HttpMethod.POST, Api.AREAQUERY, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						bean1 = new Gson().fromJson(resp.result,
								CityResponse.class);
						if (Api.SUCCEED == bean1.result_code) {
							Log.e("",
									" - - bean1- - - - "
											+ bean1.data.toString());
							HashMap<String, List<Cityinfo>> hashMap = new HashMap<String, List<Cityinfo>>();
							List<Cityinfo> list = new ArrayList<Cityinfo>();
							for (int i = 0; i < bean1.data.size(); i++) {
								Cityinfo city = new Cityinfo();
								city.setCity_name(bean1.data.get(i).areaname);
								city.setId(bean1.data.get(i).areacode);
								list.add(city);
								hashMap.put(provinceid, list);
							}

							try {
								cityid = bean1.data.get(0).areacode;
							} catch (Exception e) {
								cityid = "";
							}

							try {
								b = bean1.data.get(0).areaname;
							} catch (Exception e) {
								// TODO: handle exception
								b = "";
							}
							cityPicker.setData(citycodeUtil.getCity(hashMap,
									provinceid));
							cityPicker.setDefault(0);
							loadData2();
						}
					}
				});

	}

	public void loadData2() {

		CityRequest req = new CityRequest();
		Log.e("", "cityid = = = =  " + cityid);
		req.parentCode = cityid;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.e("", " - - 3333- - - - " + Api.AREAQUERY);
		new HttpUtils().send(HttpMethod.POST, Api.AREAQUERY, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						bean2 = new Gson().fromJson(resp.result,
								CityResponse.class);
						if (Api.SUCCEED == bean.result_code) {
							Log.e("", " - - - - - - " + resp.result);
							HashMap<String, List<Cityinfo>> hashMap = new HashMap<String, List<Cityinfo>>();
							List<Cityinfo> list = new ArrayList<Cityinfo>();
							for (int i = 0; i < bean2.data.size(); i++) {
								Cityinfo city = new Cityinfo();
								city.setCity_name(bean2.data.get(i).areaname);
								city.setId(bean2.data.get(i).areacode);
								list.add(city);
								hashMap.put(cityid, list);
							}

							try {
								regionid = bean2.data.get(0).areacode;
							} catch (Exception e) {
								// TODO: handle exception
								regionid = "";
							}
							try {
								c = bean2.data.get(0).areaname;
							} catch (Exception e) {
								// TODO: handle exception
								c = "";
							}
							if (tag != -1) {
								Listener.onClick(provinceid, cityid, regionid,
										a, b, c);
							}
							counyPicker.setData(citycodeUtil.getCouny(hashMap,
									cityid));
							counyPicker.setDefault(0);
							couny_map = hashMap;

							// mList2 = new ArrayList<HashMap<String,Object>>();
							// cityPicker.setData(citycodeUtil.getCity(bean.data));
							// cityPicker.setDefault(0);
							//
							// if(tag!=-1){
							// Listener.onClick(provinceid,cityid,regionid,a,b,c);
							// }
							// counyPicker.setData(citycodeUtil.getCouny(hashMap,
							// cityid));
							// counyPicker.setDefault(0);
							// couny_map = hashMap;

						}
					}
				});

	}

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
