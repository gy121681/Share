package com.td.qianhai.epay.oem;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.share.app.entity.response.Constans;
import com.share.app.network.Api;
import com.share.app.network.CallbackObject;
import com.share.app.network.Request;
import com.share.app.utils.ProgressDialogUtil;
import com.td.qianhai.epay.oem.adapter.HotCityAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.AreaInfoBean;
import com.td.qianhai.epay.oem.beans.CityResponse;
import com.td.qianhai.epay.oem.beans.GetAgentInfoBean;
import com.td.qianhai.epay.oem.beans.GetAgentInfoResponse;
import com.td.qianhai.epay.oem.beans.GetcityRequst;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.TurnoverRequest;
import com.td.qianhai.epay.oem.beans.TurnoverResponse;
import com.td.qianhai.epay.oem.dateutil.ScreenInfo;
import com.td.qianhai.epay.oem.dateutil.WheelMain;
import com.td.qianhai.epay.oem.fragment.BusinessFragment;
import com.td.qianhai.epay.oem.fragment.ConsumerFragment;
import com.td.qianhai.epay.oem.fragment.FriendFragment;
import com.td.qianhai.epay.oem.fragment.TurnoverFragment;
import com.td.qianhai.epay.oem.mail.utils.ImageLoadManager;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.mail.utils.NumUtil;
import com.td.qianhai.epay.oem.views.CircleImageView;
import com.td.qianhai.epay.oem.views.MyTabView;
import com.td.qianhai.epay.oem.views.MyTabView.PageChangeListener;
import com.td.qianhai.epay.oem.views.StickyLayout;
import com.td.qianhai.epay.oem.views.dialog.LoadingDialogWhole;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.mpay.utils.DateUtil;

public class BusinessCenterActivity extends FragmentActivity implements
		OnClickListener {

	private MyTabView tabView;
	private StickyLayout stickyLayout;
	private String phone;
	private CircleImageView img;
	private TextView tv_phone, tv_totamt, tv_toinfo, tv_name,
			tv1_province_came, tv2, tv3, tv, tvDateEnd, tv_rr, tv_close,
			tv_data_title, trade_amount_type3, trade_amount_type2,
			trade_amount_type1;
	private List<AreaInfoBean> areaInfo;
	public OnArticleSelectedListener mListener;
	public String area_type = "", province_id = "", city_id = "", area_id = "";
	public String provinceid = "", cityid = "", areaid = "",
			provinceidname = "";
	private int pages = 0;
	protected LoadingDialogWhole loadingDialogWhole;
	private PopupWindow mPopupWindowDialog;
	private Button determine, cacel;
	private LayoutInflater inflater;
	private WheelMain wheelMain;
	private TextView tvStartDate, tvEndDate;
	private LinearLayout ll_showdata, ll_title;
	private String startDate = "";
	private String endDate = "", personpic = "";
	private String is_search = "1";
	private ImageView img_head;
	private String ftiem = "";
	private String odate1 = "", odate2 = "";

    private OneButtonDialogWarn  warnDialog;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business_center_activity);
		AppContext.getInstance().addActivity(this);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		phone = MyCacheUtil.getshared(this).getString("Mobile", "");
		personpic = MyCacheUtil.getshared(this).getString("PERSONPIC", "");

		Date dt = new Date();
		SimpleDateFormat matter1 = new SimpleDateFormat("yyyy年MM月dd日");
		Date std = DateUtil.getDateBefore(dt, 7);
		SimpleDateFormat matter2 = new SimpleDateFormat("yyyyMMdd");
		endDate = matter2.format(dt);
		startDate = matter2.format(std);
		ftiem = endDate;

		initview();
        requestAgentInfo();
//		loaddata();
	}

	public interface OnArticleSelectedListener {
		public void onArticleSelected(int tag);
	}

	public void loadData() {

		TurnoverRequest req = new TurnoverRequest();
		req.page_no = "";
		req.page_size = "";
		req.is_search = is_search;
		req.trade_date_begin = startDate;
		req.trade_date_end = endDate;
		req.area_type = area_type;
		req.area_code = area_id;
		req.city_code = city_id;
		req.province_code = province_id;
		Log.e("", "" + req.toString());
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.FINDAGENTTRADE, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						TurnoverResponse bean = new Gson().fromJson(
								resp.result, TurnoverResponse.class);
						if (0 == bean.result_code) {
							tv_data_title.setText(NumUtil
									.getStrTime1(startDate)
									+ " - "
									+ NumUtil.getStrTime1(endDate));
							ll_showdata.setVisibility(View.VISIBLE);
							if (bean.data != null && bean.data.size() > 0) {
								trade_amount_type3.setText("¥"
										+ NumUtil.getfotmatnum(
												bean.data.get(0).trade_amount_type3,
												true, 1));
								trade_amount_type2.setText("¥"
										+ NumUtil.getfotmatnum(
												bean.data.get(0).trade_amount_type2,
												true, 1));
								trade_amount_type1.setText("¥"
										+ NumUtil.getfotmatnum(
												bean.data.get(0).trade_amount_type1,
												true, 1));
								tv_totamt.setText("¥"
										+ NumUtil.getfotmatnum(
												bean.data.get(0).total_trade_amount,
												true, 1));

							} else {
								tv_totamt.setText("暂无数据");
							}
						} else {
							tv_data_title.setText("");
							trade_amount_type3.setText("");
							trade_amount_type3.setText("");
							trade_amount_type3.setText("");
							ll_showdata.setVisibility(View.GONE);
						}
					}
				});

	}

    private void requestAgentInfo(){
        String userId = MyCacheUtil.getshared(this).getString(Constans.Login.USERID, "");
        ProgressDialogUtil.showProgressDlg(this, "数据加载中....");
        Request.getServiceGetAgentInfo(userId, new CallbackObject<GetAgentInfoBean>() {
            @Override
            public void onFailure(String msg) {
                ProgressDialogUtil.dismissProgressDlg();
                showWarningDialog(msg, null);
            }

            @Override
            public void onSuccess(GetAgentInfoBean data) {
                ProgressDialogUtil.dismissProgressDlg();
                if (!Constans.Common.C_RSPCOD_SUCCESS.equals(data.RSPCOD)) {
                    showWarningDialog(data.RSPMSG, new OnMyDialogClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    return;
                } else {
                    init(data);
                }
            }

            @Override
            public void onNetError(int code, String msg) {
                ProgressDialogUtil.dismissProgressDlg();
                showWarningDialog("网络异常", null);
            }
        });
    }


	private void loaddata() {
		TurnoverRequest req = new TurnoverRequest();
		req.phone = phone;
		// req.pageSize = pageSize + "";

		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("=====请求参数：204：" + HttpUrls.GETAGENTINFO + "--"
				+ req.toJson());
		System.out.println("=====请求参数204：" + params.getEntity().toString());
		new HttpUtils().send(HttpMethod.POST, HttpUrls.GETAGENTINFO, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(getApplicationContext(), "请求失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						GetAgentInfoResponse bean = new Gson().fromJson(
								resp.result, GetAgentInfoResponse.class);
						System.out.println("=====初始化营业中心,省市区:"+resp.result);
						try {
							cityid=bean.data.areaInfo.get(0).city_code;
							provinceid=bean.data.areaInfo.get(0).province_code;
						} catch (Exception e) {
						}
						if (0 == bean.result_code) {
							if (bean.data.RSPCOD != null
									&& bean.data.RSPCOD.equals("000000")) {
								init(bean.data);
								areaInfo = bean.data.areaInfo;
							} else {
								Toast.makeText(getApplicationContext(),
										bean.data.RSPMSG, Toast.LENGTH_SHORT)
										.show();
							}

						} else {
							Toast.makeText(getApplicationContext(),
									bean.result_desc, Toast.LENGTH_SHORT)
									.show();
						}
					}

				});
	}


    private void showWarningDialog(String msg, @Nullable final OnMyDialogClickListener l) {
        warnDialog = new OneButtonDialogWarn(this,
                R.style.CustomDialog, "提示", msg , "确定",
                new OnMyDialogClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (l != null) {
                            l.onClick(v);
                        }
                        warnDialog.dismiss();
                    }
                });
        warnDialog.show();
    }

	private void init(GetAgentInfoBean data) {
		if (data == null || data.areaInfo == null || data.areaInfo.size() <= 0) {
			return;
		}
		if (data.RSPCOD != null && data.RSPCOD.equals("000000")) {
            areaInfo = data.areaInfo;
        }
        cityid=data.areaInfo.get(0).city_code;
        provinceid=data.areaInfo.get(0).province_code;
		for (int i = 0; i < data.areaInfo.size(); i++) {
			if (data.areaInfo.get(i).area_type.equals("1")) {
				area_type = data.areaInfo.get(i).area_type;
				province_id = data.areaInfo.get(i).province_code;
				tv1_province_came.setText(data.areaInfo.get(i).province_came);
				provinceidname = data.areaInfo.get(i).province_came;
				
				img_head.setImageResource(R.drawable.share_s_mine_agent_province);
				break;
			}
			if (data.areaInfo.get(i).area_type.equals("2")) {
				area_type = data.areaInfo.get(i).area_type;
				city_id = data.areaInfo.get(i).city_code;
				tv1_province_came.setText(data.areaInfo.get(i).province_came);
				tv2.setText(data.areaInfo.get(i).city_name);				
				img_head.setImageResource(R.drawable.share_s_mine_agent_city);
				tv2.setTextColor(getResources().getColor(R.color.gray));
				break;
			}
			if (data.areaInfo.get(i).area_type.equals("3")) {
				area_type = data.areaInfo.get(i).area_type;
				area_id = data.areaInfo.get(i).area_code;
				tv1_province_came.setText(data.areaInfo.get(i).province_came);
				tv2.setText(data.areaInfo.get(i).city_name);
				tv3.setText(data.areaInfo.get(i).area_name);
				img_head.setImageResource(R.drawable.share_s_mine_agent_area);

				tv2.setTextColor(getResources().getColor(R.color.gray));
				tv3.setTextColor(getResources().getColor(R.color.gray));
				break;
			}
		}

		if (!TextUtils.isEmpty(data.photo)) {
			ImageLoadManager.getInstance(this).displayImage(HttpUrls.HOST_POSM +data.photo, img);
			//原来 上边被注释 ,else 是自己加的
		}else{//自己修改的
			ImageLoadManager.getInstance(this).displayImage(
			HttpUrls.HOST_POSM + personpic, img);//自己修改
		}
		if (!TextUtils.isEmpty(data.phone)) {
			tv_phone.setText(data.phone);
		}
		if (!TextUtils.isEmpty(data.name)) {
			tv_name.setText(data.name);
		}

		List<Map<String, Integer>> titles = new ArrayList<Map<String, Integer>>();
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("交易额", null);
		titles.add(map);
		map = new HashMap<String, Integer>();
		map.put("商家总数", null);
		titles.add(map);
		map = new HashMap<String, Integer>();
		map.put("消费者总数", null);
		titles.add(map);
		map = new HashMap<String, Integer>();
		map.put("业务员总数", null);
		titles.add(map);

		List<Fragment> fragments = new ArrayList<Fragment>();

		fragments.add(new TurnoverFragment());
		fragments.add(new BusinessFragment());
		fragments.add(new ConsumerFragment());
		fragments.add(new FriendFragment());
		// fragments.add(new IncentivePoints2Fragment());

		stickyLayout = (StickyLayout) findViewById(R.id.sticky_layout);

		tabView.createView(titles, fragments, getSupportFragmentManager());
		mListener = TurnoverFragment.mListener;

	}

	public boolean DateCompare(String s1) throws Exception {

		Date dt = new Date();

		// 设定时间的模板
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// 得到指定模范的时间
		String thisdate = sdf.format(dt);
		Date d1 = sdf.parse(s1);
		Date d2 = sdf.parse(thisdate);
		// 比较
		if ((d1.getTime() - d2.getTime()) / (24 * 3600 * 1000) >= 1) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public void onClick(View v) {
		List<AreaInfoBean> list1 = new ArrayList<AreaInfoBean>();
		System.out.println("======城市provinceid:"+provinceid+",区县cityid:"+cityid);
		switch (v.getId()) {
		
		case R.id.tv1s:
			showPopupWindow(ll_title, areaInfo, 1);
			break;
		case R.id.tv2:
			if (!TextUtils.isEmpty(provinceid)) {
				querycity(tv2, "");
			}
			break;
		case R.id.tv3:

			if (!TextUtils.isEmpty(cityid)) {
				queryarea(tv3, "");
			}

			// showPopupWindow(tv3, areaInfo, 3);
			break;

		case R.id.tv_toinfo:
			Intent it = new Intent(BusinessCenterActivity.this,
					CotyledonInfoActivity.class);
			it.putExtra("trade_date_begin", startDate);
			it.putExtra("trade_date_end", endDate);
			it.putExtra("area_type", area_type);
			it.putExtra("area_id", area_id);
			it.putExtra("city_id", city_id);
			it.putExtra("province_id", province_id);

			startActivity(it);
			break;
		default:
			break;
		}

	}

	@SuppressLint("NewApi")
	private void showPopupWindow(View view, final List<AreaInfoBean> listbean,
			final int tag) {

		if (listbean == null || listbean.size() <= 0) {
			return;
		}

		// final List<String> list = new ArrayList<String>();
		// final List<AreaInfoBean> list1 = new ArrayList<AreaInfoBean>();

		//
		// if(tag==1){
		// for (int i = 0; i < listbean.size(); i++) {
		// if(listbean.get(i).area_type.equals("1")){
		// list.add(listbean.get(i).province_came);
		// list1.add(listbean.get(i));
		// }
		// }
		// }else if(tag==2){
		// for (int i = 0; i < listbean.size(); i++) {
		// if(listbean.get(i).area_type.equals("2")){
		// list.add(listbean.get(i).city_name);
		// list1.add(listbean.get(i));
		// }
		// }
		// }else if(tag==3){
		// for (int i = 0; i < listbean.size(); i++) {
		// if(listbean.get(i).area_type.equals("3")){
		// list.add(listbean.get(i).area_name);
		// list1.add(listbean.get(i));
		// }
		// }
		// }

		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(BusinessCenterActivity.this)
				.inflate(R.layout.currency_pop1, null);
		// 设置按钮的点击事件

		// ListView listview = (ListView)
		// contentView.findViewById(R.id.poplist);
		// ArrayAdapter popadapter = new ArrayAdapter<String>(this,
		// R.layout.pop_name_w,list);
		// listview.setAdapter(popadapter);

		GridView gvlishi = (GridView) contentView.findViewById(R.id.gvlishi);
		gvlishi.setAdapter(new HotCityAdapter(BusinessCenterActivity.this,
				listbean));

		final PopupWindow popupWindow = new PopupWindow(contentView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setTouchable(true);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return false;
			}
		});

		ColorDrawable dw = new ColorDrawable(00000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		popupWindow.setBackgroundDrawable(dw);
		// WindowManager.LayoutParams lp = getWindow().getAttributes();
		// lp.alpha = 0.7f;
		// getWindow().setAttributes(lp);
		// 设置好参数之后再show
		// Log.e("", ""+(int)view2.getY());
		// Log.e("", ""+view2.getHeight());
		// Log.e("", ""+view2.getTop());
		// Log.e("", ""+view2.getBottom());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			popupWindow.showAsDropDown(view, Gravity.NO_GRAVITY, 0, 0);
		} else {
			popupWindow.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
		}
		// popupWindow.showAtLocation(view,Gravity.BOTTOM,(int) 100,0);
		// popupWindow.showAsDropDown(view,Gravity.NO_GRAVITY,0,0);
		gvlishi.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int id = (int) arg3;
				if (tag == 1) {
					cityid = "";
					if (listbean.get(id).area_type.equals("1")) {
						area_type = listbean.get(id).area_type;
						province_id = listbean.get(id).province_code;
						provinceid = listbean.get(id).province_code;
						provinceidname = listbean.get(id).province_came;
						city_id = "";
						area_id = "";
						tv1_province_came.setText(listbean.get(id).province_came);
						tv2.setText("城市");
						tv3.setText("区/县");
						tv1_province_came.setTextColor(getResources().getColor(
								R.color.black));
						tv2.setTextColor(getResources().getColor(R.color.black));
						tv3.setTextColor(getResources().getColor(R.color.black));
					} else if (listbean.get(id).area_type.equals("2")) {
						provinceid = "";
						province_id = "";
						area_id = "";
						area_type = listbean.get(id).area_type;
						city_id = listbean.get(id).city_code;
						cityid = listbean.get(id).city_code;
						tv1_province_came.setText(listbean.get(id).province_came);
						tv2.setText(listbean.get(id).city_name);
						tv3.setText("区/县");
						tv2.setTextColor(getResources().getColor(R.color.gray));
						// mListener.onArticleSelected(2);
					} else if (listbean.get(id).area_type.equals("3")) {
						provinceid = "";
						cityid = "";
						province_id = "";
						city_id = "";
						area_type = listbean.get(id).area_type;
						area_id = listbean.get(id).area_code;
						tv1_province_came.setText(listbean.get(id).province_came);
						tv2.setText(listbean.get(id).city_name);
						tv3.setText(listbean.get(id).area_name);
						tv2.setTextColor(getResources().getColor(R.color.gray));
						tv3.setTextColor(getResources().getColor(R.color.gray));
						// mListener.onArticleSelected(3);
					}

				} else if (tag == 2) {
					province_id = "";
					area_id = "";
					area_type = listbean.get(id).area_type;
					city_id = listbean.get(id).city_code;
					cityid = listbean.get(id).city_code;
					tv2.setText(listbean.get(id).city_name);
					tv3.setText("区/县");
					// mListener.onArticleSelected(2);
				} else if (tag == 3) {
					// province_id = "";
					city_id = "";
					area_type = listbean.get(id).area_type;
					area_id = listbean.get(id).area_code;
					tv3.setText(listbean.get(id).area_name);
					// mListener.onArticleSelected(3);
				}

				if (pages == 0) {
					if (ll_showdata.getVisibility() == View.VISIBLE) {
						loadData();
					} else {
						if (TurnoverFragment.contxt != null) {
							TurnoverFragment.contxt.pageNo = 1;
							TurnoverFragment.contxt.loadData();
						}
						// mListener.onArticleSelected(1);
					}

				} else if (pages == 1) {
					if (BusinessFragment.contxt != null) {
						BusinessFragment.contxt.pageNo = 1;
						BusinessFragment.contxt.loadData();
					}
				}
				popupWindow.dismiss();
			}
		});
	}

	private void initview() {
		tv_toinfo = (TextView) findViewById(R.id.tv_toinfo);
		img_head = (ImageView) findViewById(R.id.img_head);
		tv_rr = (TextView) findViewById(R.id.tv_rr);
		img = (CircleImageView) findViewById(R.id.img);
		tv_close = (TextView) findViewById(R.id.tv_close);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv1_province_came = (TextView) findViewById(R.id.tv1s);
		tv2 = (TextView) findViewById(R.id.tv2);
		ll_title = (LinearLayout) findViewById(R.id.ll_title);
		tv_data_title = (TextView) findViewById(R.id.tv_data_title);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv_totamt = (TextView) findViewById(R.id.tv_totamt);
		trade_amount_type3 = (TextView) findViewById(R.id.trade_amount_type3);
		trade_amount_type2 = (TextView) findViewById(R.id.trade_amount_type2);
		trade_amount_type1 = (TextView) findViewById(R.id.trade_amount_type1);
		ll_showdata = (LinearLayout) findViewById(R.id.ll_showdata);
		ll_showdata.setOnClickListener(this);
		tv1_province_came.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv_toinfo.setOnClickListener(this);
		tv3.setOnClickListener(this);

		((TextView) findViewById(R.id.tv_title_contre)).setText("营业中心");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		tv_rr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showPopupWindow(findViewById(R.id.title));
			}
		});

		tv_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ll_showdata.setVisibility(View.GONE);
				tv_data_title.setText("");
				trade_amount_type3.setText("");
				trade_amount_type3.setText("");
				trade_amount_type3.setText("");
			}
		});
		tabView = (MyTabView) findViewById(R.id.tabFavorites);

		tabView.setPageChangeListener(new PageChangeListener() {

			@Override
			public void onPageChanged(int index, String tabTitle) {
				// TODO Auto-generated method stub
				pages = index;
				if (pages == 0) {
					tv_rr.setVisibility(View.VISIBLE);
					// mListener.onArticleSelected(1);
					if (TurnoverFragment.contxt != null) {
						TurnoverFragment.contxt.pageNo = 1;
						TurnoverFragment.contxt.loadData();
					}
				} else if (pages == 1) {

					tv_rr.setVisibility(View.GONE);
					if (BusinessFragment.contxt != null) {
						Log.e("", "");
						BusinessFragment.contxt.pageNo = 1;
						BusinessFragment.contxt.loadData();
					}
				} else {
					tv_rr.setVisibility(View.GONE);
				}
			}
		});
	}

	private void querycity(final TextView v, final String city_id) {
		// TODO Auto-generated method stub
		showLoadingDialog("请稍候...");
		GetcityRequst req = new GetcityRequst();
		req.parentCode = provinceid;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("=====请求参数638：" + params);
		new HttpUtils().send(HttpMethod.POST, HttpUrls.AREAQUERY, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						try {
							loadingDialogWhole.dismiss();
						} catch (Exception e) {
							// TODO: handle exception
						}
						CityResponse bean = new Gson().fromJson(resp.result,
								CityResponse.class);
						if (0 == bean.result_code) {
							List<AreaInfoBean> list1 = new ArrayList<AreaInfoBean>();
							for (int i = 0; i < bean.data.size(); i++) {
								AreaInfoBean b = new AreaInfoBean();
								b.city_code = bean.data.get(i).areacode;
								b.city_name = bean.data.get(i).areaname;
								b.area_type = "2";
								list1.add(b);
							}
							showPopupWindow(v, list1, 2);
							// }
						} else {
							Toast.makeText(getApplicationContext(),
									bean.result_desc, Toast.LENGTH_SHORT)
									.show();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(getApplicationContext(), "请求失败",
								Toast.LENGTH_SHORT).show();
						try {
							loadingDialogWhole.dismiss();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});

	}

	private void queryarea(final TextView v, final String area_id) {
		// TODO Auto-generated method stub
		showLoadingDialog("请稍候...");
		GetcityRequst req = new GetcityRequst();
		req.parentCode = cityid;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("=====请求参数688：" + params);
		new HttpUtils().send(HttpMethod.POST, HttpUrls.AREAQUERY, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						try {
							loadingDialogWhole.dismiss();
						} catch (Exception e) {
							// TODO: handle exception
						}
						CityResponse bean = new Gson().fromJson(resp.result,
								CityResponse.class);
						if (0 == bean.result_code) {

							List<AreaInfoBean> list1 = new ArrayList<AreaInfoBean>();
							for (int i = 0; i < bean.data.size(); i++) {
								AreaInfoBean b = new AreaInfoBean();
								b.area_code = bean.data.get(i).areacode;
								b.area_name = bean.data.get(i).areaname;
								b.area_type = "3";
								list1.add(b);
							}
							showPopupWindow(v, list1, 3);

						} else {
							Toast.makeText(getApplicationContext(),
									bean.result_desc, Toast.LENGTH_SHORT)
									.show();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(getApplicationContext(), "请求失败",
								Toast.LENGTH_SHORT).show();
						try {
							loadingDialogWhole.dismiss();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});
	}

	protected void showLoadingDialog(String msg) {
		loadingDialogWhole = new LoadingDialogWhole(this, R.style.CustomDialog,
				msg);
		loadingDialogWhole.setCancelable(false);
		loadingDialogWhole
				.setOnKeyListener(new DialogInterface.OnKeyListener() {
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
		loadingDialogWhole.setCanceledOnTouchOutside(false);
		loadingDialogWhole.show();
	}

	private void showPopupWindow(View view) {

		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.remember_pop, null);
		// 设置按钮的点击事件
		Button button = (Button) contentView.findViewById(R.id.btnOK);
		Button button1 = (Button) contentView.findViewById(R.id.btnNo);
		final EditText edquery = (EditText) contentView
				.findViewById(R.id.edquery);
		tv = (TextView) contentView.findViewById(R.id.tvDateStart);

		// if(odate1!=null){
		// tv.setText(odate1);
		// }

		tvDateEnd = (TextView) contentView.findViewById(R.id.tvDateEnd);
		// if(odate2!=null){
		// tvDateEnd.setText(odate2);
		// }

		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showdate(tv, 0, odate1);
			}
		});
		tvDateEnd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showdate(tvDateEnd, 1, odate2);
			}
		});

		final PopupWindow popupWindow = new PopupWindow(contentView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setTouchable(true);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		popupWindow.setBackgroundDrawable(dw);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.7f;
		getWindow().setAttributes(lp);
		// 设置好参数之后再show
		popupWindow.showAsDropDown(view);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (TextUtils.isEmpty(tv.getText())) {
					Toast.makeText(getApplicationContext(), "请选择开始时间",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (TextUtils.isEmpty(tvDateEnd.getText())) {
					Toast.makeText(getApplicationContext(), "请选择结束时间",
							Toast.LENGTH_SHORT).show();
					return;
				}

				loadData();
				popupWindow.dismiss();

			}
		});
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// loadData();
				popupWindow.dismiss();
			}
		});

		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
	}

	private void showdate(TextView v, int tag, String date) {
		// setLayoutY(lin_1, 0);

		// Calendar calendar = Calendar.getInstance();
		//
		// int year = calendar.get(Calendar.YEAR);
		// int month = calendar.get(Calendar.MONTH);
		// int day = 1;
		// int hour = calendar.get(Calendar.HOUR_OF_DAY);
		// int min = calendar.get(Calendar.MINUTE);

		Calendar calendar = Calendar.getInstance();
		// if(!TextUtils.isEmpty(date)){
		// SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
		// Date pushDate = null;
		// try {
		// pushDate = matter1.parse(date);
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// calendar=Calendar.getInstance();
		// calendar.setTime(pushDate);
		// }

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);

		View view = inflater.inflate(R.layout.choose_dialog, null);
		setPopupWindowDialog(view);

		ScreenInfo screenInfo = new ScreenInfo(BusinessCenterActivity.this);
		wheelMain = new WheelMain(view, 1);
		wheelMain.screenheight = screenInfo.getHeight();
		wheelMain.initDateTimePicker(year, month, day, hour, min);

		if (mPopupWindowDialog != null) {
			mPopupWindowDialog.showAtLocation(tv_rr, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
		}

		bottomBtn(v, tag);

	}

	protected void setPopupWindowDialog(View view) {
		// TODO Auto-generated method stub
		determine = (Button) view.findViewById(R.id.textview_dialog_album);
		cacel = (Button) view.findViewById(R.id.textview_dialog_cancel);

		mPopupWindowDialog = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindowDialog.setAnimationStyle(R.style.popwin_anim_style);
		mPopupWindowDialog.setFocusable(true);
		mPopupWindowDialog.update();
		mPopupWindowDialog.setBackgroundDrawable(new BitmapDrawable(
				getResources(), (Bitmap) null));
		// mPopupWindowDialog.setBackgroundDrawable(getResources().getDrawable(R.drawable.air_city_button));
		mPopupWindowDialog.setOutsideTouchable(true);
	}

	protected void bottomBtn(final TextView v, final int tag) {
		// TODO Auto-generated method stub
		determine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (mPopupWindowDialog != null
						&& mPopupWindowDialog.isShowing()) {
					mPopupWindowDialog.dismiss();
					// setLayoutY(lin_1, 0);
				}
			}
		});

		cacel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String dates = wheelMain.getTime();
				if (tag == 0) {
					try {
						if (DateCompare(dates)) {
							startDate = ftiem;
							if (ftiem.length() >= 8) {
								String a = startDate.substring(0, 4);
								String b = startDate.substring(4, 6);
								String c = startDate.substring(startDate
										.length() - 2);
								v.setText(a + "年" + b + "月" + c + "日");
								odate1 = a + "年" + b + "月" + c + "日";
							}
						} else {
							startDate = dates;
							if (wheelMain.getTime().length() >= 8) {
								String a = dates.substring(0, 4);
								String b = dates.substring(4, 6);
								String c = dates.substring(dates.length() - 2);
								v.setText(a + "年" + b + "月" + c + "日");
								odate1 = a + "年" + b + "月" + c + "日";
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// startDate = dates;
					// if(wheelMain.getTime().length()>=8){
					// String a = dates.substring(0, 4);
					// String b = dates.substring(4, 6);
					// String c = dates.substring(dates.length()-2);
					// v.setText(a+"年"+b+"月"+c+"日");
					// }
				} else if (tag == 1) {
					// endDate = dates;
					// setdata(dates);
					try {
						if (DateCompare(dates)) {
							endDate = ftiem;
							if (endDate.length() >= 8) {
								String a = endDate.substring(0, 4);
								String b = endDate.substring(4, 6);
								String c = endDate.substring(endDate.length() - 2);
								v.setText(a + "年" + b + "月" + c + "日");
								odate2 = a + "年" + b + "月" + c + "日";
							}
						} else {
							endDate = dates;
							if (wheelMain.getTime().length() >= 8) {
								String a = dates.substring(0, 4);
								String b = dates.substring(4, 6);
								String c = dates.substring(dates.length() - 2);
								v.setText(a + "年" + b + "月" + c + "日");
								odate2 = a + "年" + b + "月" + c + "日";
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (mPopupWindowDialog != null
						&& mPopupWindowDialog.isShowing()) {
					mPopupWindowDialog.dismiss();
					// setLayoutY(lin_1, 0);
				}
			}
		});
	}

	public static void setLayoutY(View view, int y) {
		MarginLayoutParams margin = new MarginLayoutParams(
				view.getLayoutParams());
		margin.setMargins(margin.leftMargin, y, margin.rightMargin, y
				+ margin.height);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				margin);
		view.setLayoutParams(layoutParams);
	}

	private String getthismoth() {

		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.getTime();
		// start_time.setText(dateFormater.format(cal.getTime()) + "");

		// cal.set(Calendar.DAY_OF_MONTH,
		// cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		// end_time.setText(dateFormater.format(cal.getTime()));
		return dateFormater.format(cal.getTime()) + "";

	}
}
