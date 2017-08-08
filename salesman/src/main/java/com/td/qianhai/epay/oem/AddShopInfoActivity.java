package com.td.qianhai.epay.oem;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.td.qianhai.epay.oem.beans.AddShopBean;
import com.td.qianhai.epay.oem.beans.AddShopInfoBean;
import com.td.qianhai.epay.oem.beans.AddShopInfoResponse;
import com.td.qianhai.epay.oem.beans.AddShopRequest;
import com.td.qianhai.epay.oem.beans.AddShopResponce;
import com.td.qianhai.epay.oem.beans.AddShopRsp;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.UpShopInfoRequst;
import com.td.qianhai.epay.oem.dateutil.ScreenInfo;
import com.td.qianhai.epay.oem.dateutil.WheelMain;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddShopInfoActivity extends BaseActivity implements OnClickListener{
	
	
	private LinearLayout lin_1;
	private EditText shopname ,shoplicense,shopcontent,shopturnover,shopsize,shopnumber,shopmobile,peopleturnover;
	private TextView starttime,lasttime,shoptype,tv_go,tv_textadvertising,tv_starttime,tv_lasttime;
	private CheckBox radio1,radio2,radio3;
	private LayoutInflater inflater;
	private WheelMain wheelMain;
	private PopupWindow mPopupWindowDialog;
	private Button determine,cacel;
	private View view ;
	private Time startTime, endTime;
	private String industry_type = "",collect_id = "",tag = "";
	private String type = "",type1 = "",type2 = "",type3 = "";
	private  LinearLayout.LayoutParams lp;
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addahopinfo_activity);
		AppContext.getInstance().addActivity(this);
		AppContext.getInstance().addActivity1(this);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		collect_id = getIntent().getStringExtra("collect_id");
		tag = getIntent().getStringExtra("tag");
		
//		LayoutInflater inflater = LayoutInflater.from(AddShopInfoActivity.this);
//		 lp = new LinearLayout.LayoutParams( 
//				 500, 45); 
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		((TextView) findViewById(R.id.tv_title_contre)).setText("提交商户");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
//		tv_starttime = (TextView) findViewById(R.id.tv_starttime);
//		tv_lasttime = (TextView) findViewById(R.id.tv_lasttime);
//		tv_starttime.requestFocus();
//		tv_starttime.setFocusable(true);
//		tv_starttime.setFocusableInTouchMode(true);
//		tv_lasttime.requestFocus();
		tv_textadvertising = (TextView) findViewById(R.id.tv_textadvertising);
		tv_textadvertising.requestFocus();
		tv_textadvertising.setFocusable(true);
		tv_textadvertising.setFocusableInTouchMode(true);
		peopleturnover = (EditText) findViewById(R.id.peopleturnover);
		lin_1 = (LinearLayout) findViewById(R.id.lin_1);  
		shopname = (EditText) findViewById(R.id.shopname);
		shoplicense = (EditText) findViewById(R.id.shoplicense);
		shopcontent = (EditText) findViewById(R.id.shopcontent);
		shopturnover = (EditText) findViewById(R.id.shopturnover);
		shopsize = (EditText) findViewById(R.id.shopsize);
		shopnumber = (EditText) findViewById(R.id.shopnumber);
		shopmobile = (EditText) findViewById(R.id.shopmobile);
		starttime = (TextView) findViewById(R.id.starttime);
		lasttime = (TextView) findViewById(R.id.lasttime);
		shoptype = (TextView) findViewById(R.id.shoptype);
		starttime.setOnClickListener(this);
		lasttime.setOnClickListener(this);
		shoptype.setOnClickListener(this);
		radio1 = (CheckBox) findViewById(R.id.radio1);
		radio2 = (CheckBox) findViewById(R.id.radio2);
		radio3 = (CheckBox) findViewById(R.id.radio3);
		tv_go = (TextView) findViewById(R.id.tv_go);
		tv_go.setOnClickListener(this);
		radio1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					type1 = "1,";
				}else{
					type1 = "";
				}
				
			}
		});
		radio2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					type2 = "2,";
				}else{
					type2 = "";
				}
			}
		});
		radio3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					type3 = "3";
				}else{
					type3 = "";
				}
			}
		});
		
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
				Log.e("", ""+resp.result);
				if(0== bean.result_code) {
//				Toast.makeText(getApplicationContext(), "提交", Toast.LENGTH_SHORT).show();
				initinfo(bean.data);
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
		shopname.setText(data.shop_name);
		shoplicense.setText(data.cert_no);
		if(!TextUtils.isEmpty(data.industry_type)){
			gettype(shoptype,data.industry_type);
			industry_type = data.industry_type;
		}
		
		
		if(data.bili!=null){
			 String[] sourceStrArray = data.bili.split(",");
			 for (int i = 0; i < sourceStrArray.length; i++) {
				 if(sourceStrArray[i].equals("1")){
					 radio1.setChecked(true);
				 }else if(sourceStrArray[i].equals("2")){
					 radio2.setChecked(true);
				 }else if(sourceStrArray[i].equals("3")){
					 radio3.setChecked(true);
				 }
			}
		}

		 shopcontent.setText(data.industry_content);
		 shopturnover.setText(data.day_fee);
		 shopsize.setText(data.shop_area);
		 shopnumber.setText(data.shop_person_num);
		 peopleturnover.setText(data.consumption_per_person);
//		 shopmobile.setText(data.mobile);
		 starttime.setText(data.shop_open_time);
		 lasttime.setText(data.shop_end_time);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.starttime:
			showdate(starttime,9);
			break;
		case R.id.lasttime:
			showdate(lasttime,20);
			break;
		case R.id.shoptype:
			gettype(shoptype,"");
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
		if(checkEditEmpty(shopname, "请填写商家名称")){
			return;
		}
		
		if(TextUtils.isEmpty(shoptype.getText())){
			Texterro(shoptype, "请选择行业类别");
			return;
		}
		if(checkEditEmpty(shoplicense, "请填写统一社会行用代码(即营业执照号)")){
			return;
		}
		if(shoplicense.getText().length()<11){
			Texterro(shoplicense, "11~18位");
			return;
		}
		
		
		type = type1+type2+type3;
		if(TextUtils.isEmpty(type)){
			Toast.makeText(getApplicationContext(), "请选择激励模式", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(checkEditEmpty(shopcontent, "请填写经营内容摘要")){
			return;
		}
		if(checkEditEmpty(shopturnover, "请填写日均交易额")){
			return;
		}
		if(checkEditEmpty(peopleturnover, "请填写人均消费")){
			return;
		}
		String balances = peopleturnover.getText().toString();
		
		try {
			Double.parseDouble(balances);
		} catch (Exception e) {
			// TODO: handle exception
			Editerro(peopleturnover, "日均交易金额有误");
//			Toast.makeText(getApplicationContext(), "日均交易金额有误", Toast.LENGTH_SHORT).show();
			return;
		}
		if(balances.equals(".")){
			Editerro(peopleturnover, "日均交易金额有误");
//			Toast.makeText(getApplicationContext(), "日均交易金额有误", Toast.LENGTH_SHORT).show();
			return;
		}
		if(!balances.equals(".")&&Double.parseDouble(balances)<=0){
			Editerro(peopleturnover, "日均交易金额有误");
//			Toast.makeText(getApplicationContext(), "日均交易金额有误", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(checkEditEmpty(shopsize, "请填写门店面积")){
			return;
		}
		if(checkEditEmpty(shopnumber, "请填写门店规模")){
			return;
		}
//		if(checkEditEmpty(shopmobile, "请填写门店联系电话")){
//			return;
//		}
		if(TextUtils.isEmpty(starttime.getText())){
			Texterro(starttime, "请选择营业开始时间");
			return;
		}
		if(TextUtils.isEmpty(lasttime.getText())){
			Texterro(lasttime, "请选择营业结束时间");
			return;
		}
		
		UpShopInfoRequst req = new UpShopInfoRequst();
		req.collect_id = collect_id;
		req.shop_name = shopname.getText().toString();
		req.cert_no = shoplicense.getText().toString();
		req.industry_type = industry_type;
		req.bili = type;
		req.consumption_per_person = peopleturnover.getText().toString();
		req.industry_content = shopcontent.getText().toString();
		req.day_fee = shopturnover.getText().toString();
		req.shop_area = shopsize.getText().toString();
		req.shop_person_num = shopnumber.getText().toString();
//		req.mobile = shopmobile.getText().toString();
		req.shop_open_time = starttime.getText().toString();
		req.shop_end_time = lasttime.getText().toString();
		
		showLoadingDialog("请稍候...");
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
					
					Intent it = new Intent(AddShopInfoActivity.this,AddShopCityActivity.class);
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

	private void gettype(final TextView v, final String i) {
		// TODO Auto-generated method stub
		
		AddShopRequest req = new AddShopRequest();
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, HttpUrls.TYPELIST, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				AddShopInfoResponse bean = new Gson().fromJson(resp.result, AddShopInfoResponse.class);
				if(0== bean.result_code) {
					if(i!=null&&!i.equals("")){
						for (int j = 0; j < bean.data.size(); j++) {
							if(i.equals(bean.data.get(j).id)){
								v.setText(bean.data.get(j).industry_name);
							}
						}
					}else{
						initdialog(bean.data,v);
					}
					
				} else {
					Toast.makeText(getApplicationContext(), bean.result_desc, Toast.LENGTH_SHORT).show();
//					T.showShort(AddrEditActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(getApplicationContext(),"请求失败", Toast.LENGTH_SHORT).show();
//				T.showNetworkError(AddrEditActivity.this);
			}
		});
	}
	
	
	private void initdialog(final List<AddShopInfoBean> data, final TextView tv) {
		// TODO Auto-generated method stub

		final Dialog dialog = new Dialog(this,R.style.MyEditDialog1);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog.setTitle("选择");
		ListView lv = new ListView(this); 
		ProvinceAdapter pAdapter = new ProvinceAdapter(data);
		lv.setAdapter(pAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View initbank, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				industry_type = data.get((int)arg2).id;
				tv.setText(data.get((int)arg2).industry_name);
				dialog.dismiss();
			}
		});
		
		dialog.setContentView(lv);
		dialog.show();
	}
	
	
	class ProvinceAdapter extends BaseAdapter{
		public List<AddShopInfoBean>  adapter_list;
		public ProvinceAdapter(List<AddShopInfoBean> b){
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
			
//			View view = inflater.inflate(R.layout.dailog_item, null);
//			TextView v = (TextView) view.findViewById(R.id.tvs);
//			v.setText(adapter_list.get(position).industry_name);
			
			TextView tv = new TextView(AddShopInfoActivity.this);
			tv.setWidth(800);
			tv.setTextColor(getResources().getColor(R.color.black));
			tv.setPadding(20, 20, 20, 20);
			tv.setTextSize(18);
			tv.setText(adapter_list.get(position).industry_name);
			return tv;
		}
		
	}
	
	

	private void showdate(TextView v,int a) {
		setLayoutY(lin_1, 0);
		Calendar calendar = Calendar.getInstance();

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		
		view = inflater.inflate(R.layout.choose_dialog, null);
		setPopupWindowDialog();

		ScreenInfo screenInfo = new ScreenInfo(AddShopInfoActivity.this);
		wheelMain = new WheelMain(view, -1);
		wheelMain.screenheight = screenInfo.getHeight();
		wheelMain.initDateTimePicker(year, month, day, a, 0);

		if (mPopupWindowDialog != null) {
			mPopupWindowDialog.showAtLocation(
					findViewById(R.id.lin_1), Gravity.BOTTOM
							| Gravity.CENTER_HORIZONTAL, 0, 0);
		}

		bottomBtn(v);
		
	}
	
	protected void setPopupWindowDialog() {
		// TODO Auto-generated method stub
		determine = (Button) view.findViewById(R.id.textview_dialog_album);
		cacel = (Button) view.findViewById(R.id.textview_dialog_cancel);

		mPopupWindowDialog = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindowDialog.setAnimationStyle(R.style.popwin_anim_style);  
		mPopupWindowDialog.setFocusable(true);
		mPopupWindowDialog.update();
		mPopupWindowDialog.setBackgroundDrawable(new BitmapDrawable(
				getResources(), (Bitmap) null));
//		mPopupWindowDialog.setBackgroundDrawable(getResources().getDrawable(R.drawable.air_city_button));
		mPopupWindowDialog.setOutsideTouchable(true);
	}
	
	
	protected void bottomBtn(final TextView v) {
		// TODO Auto-generated method stub
		determine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if (mPopupWindowDialog != null
						&& mPopupWindowDialog.isShowing()) {
					mPopupWindowDialog.dismiss();
					setLayoutY(lin_1, 0);
				}
				
			}
		});

		cacel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String dates = wheelMain.getTime();
				
				v.setText(dates);
				
				if (mPopupWindowDialog != null
						&& mPopupWindowDialog.isShowing()) {
					mPopupWindowDialog.dismiss();
					setLayoutY(lin_1, 0);
				}
			}
		});
	}
	
	public static void setLayoutY(View view,int y) 
	{ 
	MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams()); 
	margin.setMargins(margin.leftMargin,y, margin.rightMargin, y+margin.height); 
	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(margin); 
	view.setLayoutParams(layoutParams); 
	}
}
