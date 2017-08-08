package com.td.qianhai.epay.oem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.dateutil.ScreenInfo;
import com.td.qianhai.epay.oem.dateutil.WheelMain;
import com.td.qianhai.epay.oem.views.DateControlHandler;
import com.td.qianhai.epay.oem.views.dialog.ChooseDialog;
import com.td.qianhai.mpay.utils.DateUtil;

public class RichTreasureDealRecordsActivity extends BaseActivity {

	/** 父布局开始日期、结束日期 */
	private LinearLayout lStartDate, lEndDate, lDeal;
	/** 开始日期、结束日期 */
	private TextView tvStartDate, tvEndDate;
	private Spinner deal_typt_content;
	private DateControlHandler calculatorDateControlHandler;// 时间控件handle
//	private TimeDialog timeDialog;// 日期监听
	private Time startTime, endTime;
	private String startDate = "";
	private String endDate = "";
	private int tag = 0;
	
	private LinearLayout deal_type;
	private LinearLayout lin_1;
	private ChooseDialog chooseDialog;
	
	private View view;
	
	private WheelMain wheelMain;
	
	private LayoutInflater inflater;
	
	private PopupWindow mPopupWindowDialog;
	
	private Button determine,cacel;
	
	private String gettag = "1";

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.deal_records);
		AppContext.getInstance().addActivity(this);
		Intent it = getIntent();
		gettag = it.getStringExtra("tag");
//		initvariable();
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		((TextView) findViewById(R.id.tv_title_contre)).setText("查询交易记录");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
//		 ((TextView) findViewById(R.id.bt_title_right)).setText("小票查询");
//		TextView tv_content = (TextView) findViewById(R.id.tv_title_contre);
//		tv_content.setTextSize(15);
//		((TextView) findViewById(R.id.bt_title_right)).setVisibility(View.GONE);
		deal_typt_content = (Spinner) findViewById(R.id.deal_typt_content);
		deal_type = (LinearLayout) findViewById(R.id.deal_type);
		lStartDate = (LinearLayout) findViewById(R.id.layout2_start_date);
		lEndDate = (LinearLayout) findViewById(R.id.layout2_end_date);
		tvStartDate = (TextView) findViewById(R.id.tv_deal_records_start_date);
		tvEndDate = (TextView) findViewById(R.id.tv_deal_records_end_date);
		lin_1 = (LinearLayout) findViewById(R.id.lin_1);
//		lStartDate.setOnClickListener(timeDialog);
//		lEndDate.setOnClickListener(timeDialog);
		Date dt = new Date();
		SimpleDateFormat matter1 = new SimpleDateFormat("yyyy年MM月dd日");
		Date std = DateUtil.getDateBefore(dt, 7);
		tvStartDate.setText(matter1.format(std));
		tvEndDate.setText(matter1.format(dt));
		SimpleDateFormat matter2 = new SimpleDateFormat("yyyyMMdd");
		endDate = matter2.format(dt);
		startDate = matter2.format(std);
		
		lStartDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showdate(tvStartDate);
			}
		});
		lEndDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showdate(tvEndDate);
			}
		});
		
		ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_spinner_item, new String[]{"全部", "收款", "提现", "收益", "转入", "转出", "服务购买","手机充值","闪电提现","信用卡还款"});  
		  
	      adapter.setDropDownViewResource(R.layout.sp_item);  
//	</span>//只需在这里设置一句即可setDropDownViewResource  
	      deal_typt_content.setAdapter(adapter);  
	      
	      deal_typt_content.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long id) {
				// TODO Auto-generated method stub
				
				tag = arg2;
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	      
		
//		deal_type.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				
//				chooseDialog = new ChooseDialog(
//						RichTreasureDealRecordsActivity.this,
//						R.style.CustomDialog,
//						new OnBackDialogClickListener() {
//
//							@Override
//							public void OnBackClick(View v, String str,
//									int position) {
//								// TODO Auto-generated method stub
//								tag = position;
//								chooseDialog.dismiss();
//								if(position==0){
//									deal_typt_content.setText("全部");
//								}else if(position==1){
//									deal_typt_content.setText("充值");
//								}else if(position==2){
//									deal_typt_content.setText("提现");
//								}else if(position==3){
//									deal_typt_content.setText("奖励");
//								}
//							}
//						}, "请选择", RichTreasureDealRecordsActivity.this
//								.getResources().getStringArray(
//										R.array.queryorderitem1));
//				chooseDialog.show();
//			}
//		});
		
		
		
//		sp.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				
//				tag = arg2;
//			}
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				
//			}
//		});

		findViewById(R.id.btn_deal_records_confirm).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.e("", " = = "+startDate);
						Log.e("", " = = "+endDate);
						if (tvStartDate.getText().toString() == null
								|| tvStartDate.getText().toString().equals("")) {
							Toast.makeText(getApplicationContext(),"请选择交易开始日期",
									Toast.LENGTH_SHORT).show();
//							ToastCustom.showMessage(
//									RichTreasureDealRecordsActivity.this,
//									"请选择交易开始日期");
							return;
						}

						if (tvEndDate.getText().toString() == null
								|| tvEndDate.getText().toString().equals("")) {
							Toast.makeText(getApplicationContext(),"请选择交易结束日期",
									Toast.LENGTH_SHORT).show();
//							ToastCustom.showMessage(
//									RichTreasureDealRecordsActivity.this,
//									"请选择交易结束日期");
							return;
						}
//						if(gettag!=null&&gettag.equals("0")){
//							Intent it = new Intent(
//									RichTreasureDealRecordsActivity.this,
//									RegularListActivity.class);
//							Bundle bundle = new Bundle();
//							bundle.putString("startDate", startDate);
//							bundle.putString("endDate", endDate);
//							it.putExtras(bundle);
//							startActivity(it);
//						}else{
							Intent it = new Intent(
									RichTreasureDealRecordsActivity.this,
									RichTreasureDetailActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("startDate", startDate);
							bundle.putString("endDate", endDate);
							bundle.putInt("tag", tag);
							it.putExtras(bundle);
							startActivity(it);
//						}
					}
				});
	}

//	private void initvariable() {
//		// 重写handle
//		calculatorDateControlHandler = new DateControlHandler() {
//			@Override
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//				String year;
//				String month;
//				String day;
//				System.out.println("进入到这里");
//				if (tvStartDate.getId() == calculatorDateControlHandler.viewId
//						|| lStartDate.getId() == calculatorDateControlHandler.viewId) {
//					startTime.year = calculatorDateControlHandler.year;
//					startTime.month = calculatorDateControlHandler.month;
//					System.out.println();
//					startTime.monthDay = calculatorDateControlHandler.day;
//					year = startTime.year + "";
//
//					if (startTime.month < 9) {
//						month = "0" + (startTime.month + 1);
//					} else {
//						month = "" + (startTime.month + 1);
//					}
//					if (startTime.monthDay < 10) {
//						day = "0" + startTime.monthDay;
//					} else {
//						day = "" + startTime.monthDay;
//					}
//
//					tvStartDate.setText(calculatorDateControlHandler.year + "年"
//							+ month + "月" + day + "日");
//					System.out.println(year + month + day);
//					startDate = year + month + day;
//				} else if (tvEndDate.getId() == calculatorDateControlHandler.viewId
//						|| lEndDate.getId() == calculatorDateControlHandler.viewId) {
//					endTime.year = calculatorDateControlHandler.year;
//					endTime.month = calculatorDateControlHandler.month;
//					endTime.monthDay = calculatorDateControlHandler.day;
//					year = endTime.year + "";
//					if (endTime.month < 9) {
//						month = "0" + (endTime.month + 1);
//					} else {
//						month = "" + (endTime.month + 1);
//					}
//					if (endTime.monthDay < 10) {
//						day = "0" + endTime.monthDay;
//					} else {
//						day = "" + endTime.monthDay;
//					}
//					tvEndDate.setText(calculatorDateControlHandler.year + "年"
//							+ month + "月" + day + "日");
//					endDate = year + month + day;
//
//				}
//			}
//		};
//		timeDialog = new TimeDialog();
//		startTime = new Time();
//		endTime = new Time();
//		startTime.setToNow();
//		endTime.setToNow();
//	}

//	class TimeDialog implements OnClickListener {
//
//		public void onClick(View v) {
//			Time defaultDate = new Time();
//			if (tvStartDate == v || lStartDate == v) {
//				defaultDate = startTime;
//			} else if (tvEndDate == v || lEndDate == v) {
//				defaultDate = endTime;
//			}
//			DateDialogBlack_haveday dateDialog = new DateDialogBlack_haveday(
//					RichTreasureDealRecordsActivity.this,
//					R.style.dateDialogTheme, calculatorDateControlHandler, v,
//					defaultDate);
//			// 设置dialog的位置
//			WindowManager.LayoutParams lp = dateDialog.getWindow()
//					.getAttributes();
////			lp.height
//			lp.gravity = Gravity.BOTTOM;
//			dateDialog.show();
//		}
//	}
	
		
	
	private void showdate(TextView v) {
		setLayoutY(lin_1, 0);
		
//		Calendar calendar = Calendar.getInstance();
//
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH);
//		int day = 1;
//		int hour = calendar.get(Calendar.HOUR_OF_DAY);
//		int min = calendar.get(Calendar.MINUTE);
		
		
		Calendar calendar = Calendar.getInstance();

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		
		view = inflater.inflate(R.layout.choose_dialog, null);
		setPopupWindowDialog();

		ScreenInfo screenInfo = new ScreenInfo(RichTreasureDealRecordsActivity.this);
		wheelMain = new WheelMain(view, 1);
		wheelMain.screenheight = screenInfo.getHeight();
		wheelMain.initDateTimePicker(year, month, day, hour, min);

		if (mPopupWindowDialog != null) {
			mPopupWindowDialog.showAtLocation(
					findViewById(R.id.btn_deal_records_confirm), Gravity.BOTTOM
							| Gravity.CENTER_HORIZONTAL, 0, 0);
		}

		bottomBtn(v);
		
	}
	
	protected void setPopupWindowDialog() {
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
				if(v==tvStartDate){
					startDate = dates;
				}else if(v==tvEndDate){
					endDate = dates;
				}
				if(wheelMain.getTime().length()>=8){
					String a = dates.substring(0, 4);
					String b = dates.substring(4, 6);
					String c = dates.substring(dates.length()-2);
					v.setText(a+"年"+b+"月"+c+"日");
				}
				
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
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
