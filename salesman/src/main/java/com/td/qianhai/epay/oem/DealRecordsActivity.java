package com.td.qianhai.epay.oem;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.views.DateControlHandler;
import com.td.qianhai.epay.oem.views.DateDialogBlack_haveday;
import com.td.qianhai.mpay.utils.DateUtil;

public class DealRecordsActivity extends BaseActivity {

	/** 父布局开始日期、结束日期 */
	private LinearLayout lStartDate, lEndDate;
	/** 开始日期、结束日期 */
	private TextView tvStartDate, tvEndDate;
	private DateControlHandler calculatorDateControlHandler;// 时间控件handle
	private TimeDialog timeDialog;// 日期监听
	private Time startTime, endTime;
	private String startDate = "";
	private String endDate = "";
	private int width,height;
	private LinearLayout deal_type;
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.deal_records);
		AppContext.getInstance().addActivity(this);
		 DisplayMetrics dm = new DisplayMetrics();
		    getWindowManager().getDefaultDisplay().getMetrics(dm);
		    width = dm.widthPixels;    //得到宽度
		    height = dm.heightPixels;  //得到高度

		initvariable();
		((TextView) findViewById(R.id.tv_title_contre)).setText("查询充值记录");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		lStartDate = (LinearLayout) findViewById(R.id.layout2_start_date);
		lEndDate = (LinearLayout) findViewById(R.id.layout2_end_date);
		tvStartDate = (TextView) findViewById(R.id.tv_deal_records_start_date);
		tvEndDate = (TextView) findViewById(R.id.tv_deal_records_end_date);
		lStartDate.setOnClickListener(timeDialog);
		lEndDate.setOnClickListener(timeDialog);
		deal_type = (LinearLayout) findViewById(R.id.deal_type);
		deal_type.setVisibility(View.GONE);
		/*
		 * Calendar cal=Calendar.getInstance(); String currentDate =
		 * cal.get(Calendar
		 * .YEAR)+"年"+cal.get(Calendar.MONTH)+"月"+cal.get(Calendar
		 * .DAY_OF_MONTH)+"日"; startDate = endDate =
		 * cal.get(Calendar.YEAR)+cal.get
		 * (Calendar.MONTH)+cal.get(Calendar.DAY_OF_MONTH)+"";
		 */
		Date dt = new Date();
		 SimpleDateFormat matter1 = new SimpleDateFormat("yyyy年MM月dd日");
		 Date std = DateUtil.getDateBefore(dt, 7);
		 tvStartDate.setText(matter1.format(std));
		 tvEndDate.setText(matter1.format(dt));
		SimpleDateFormat matter2 = new SimpleDateFormat("yyyyMMdd");
		startDate =matter2.format(std);
		endDate = matter2.format(dt);

		findViewById(R.id.btn_deal_records_confirm).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// Toast.makeText(DealRecordsActivity.this,
						// startDate+"-"+endDate, Toast.LENGTH_SHORT).show();

						if (tvStartDate.getText().toString() == null
								|| tvStartDate.getText().toString().equals("")) {
							Toast.makeText(getApplicationContext(),"请选择交易开始日期",
									Toast.LENGTH_SHORT).show();
//							ToastCustom.showMessage(DealRecordsActivity.this,
//									"请选择交易开始日期");
							return;
						}

						if (tvEndDate.getText().toString() == null
								|| tvEndDate.getText().toString().equals("")) {
							Toast.makeText(getApplicationContext(),"请选择交易结束日期",
									Toast.LENGTH_SHORT).show();
//							ToastCustom.showMessage(DealRecordsActivity.this,
//									"请选择交易结束日期");
							return;
						}

						Intent it = new Intent(DealRecordsActivity.this,
								DealRecordresultActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("startDate", startDate);
						bundle.putString("endDate", endDate);
						it.putExtras(bundle);
						startActivity(it);

					}
				});
	}

	private void initvariable() {
		// 重写handle
		calculatorDateControlHandler = new DateControlHandler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				String year;
				String month;
				String day;
				System.out.println("进入到这里");
				if (tvStartDate.getId() == calculatorDateControlHandler.viewId
						|| lStartDate.getId() == calculatorDateControlHandler.viewId) {
					startTime.year = calculatorDateControlHandler.year;
					startTime.month = calculatorDateControlHandler.month;
					startTime.monthDay = calculatorDateControlHandler.day;
					year = startTime.year + "";

					if (startTime.month < 10) {
						month = "0" + (startTime.month + 1);
					} else {
						month = "" + (startTime.month + 1);
					}
					if (startTime.monthDay < 10) {
						day = "0" + startTime.monthDay;
					} else {
						day = "" + startTime.monthDay;
					}

					tvStartDate.setText(calculatorDateControlHandler.year + "年"
							+ month + "月" + day + "日");
					startDate = year + month + day;
				} else if (tvEndDate.getId() == calculatorDateControlHandler.viewId
						|| lEndDate.getId() == calculatorDateControlHandler.viewId) {
					endTime.year = calculatorDateControlHandler.year;
					endTime.month = calculatorDateControlHandler.month;
					endTime.monthDay = calculatorDateControlHandler.day;
					year = endTime.year + "";
					if (endTime.month < 10) {
						month = "0" + (endTime.month + 1);
					} else {
						month = "" + (endTime.month + 1);
					}
					if (endTime.monthDay < 10) {
						day = "0" + endTime.monthDay;
					} else {
						day = "" + endTime.monthDay;
					}
					tvEndDate.setText(calculatorDateControlHandler.year + "年"
							+ month + "月" + day + "日");
					endDate = year + month + day;

				}

			}
		};
		timeDialog = new TimeDialog();
		startTime = new Time();
		endTime = new Time();
		// startTime.year=1990;
		// startTime.month=0;
		// startTime.monthDay=1;
		startTime.setToNow();
		endTime.setToNow();

	}

	class TimeDialog implements OnClickListener {

		public void onClick(View v) {
			Time defaultDate = new Time();
			if (tvStartDate == v || lStartDate == v) {
				defaultDate = startTime;
			} else if (tvEndDate == v || lEndDate == v) {
				defaultDate = endTime;
			}
			DateDialogBlack_haveday dateDialog = new DateDialogBlack_haveday(
					DealRecordsActivity.this, R.style.dateDialogTheme,
					calculatorDateControlHandler, v, defaultDate);
			// 设置dialog的位置
			WindowManager.LayoutParams lp = dateDialog.getWindow()
					.getAttributes();
			lp.height = height/3;
			lp.gravity = Gravity.BOTTOM;

			dateDialog.show();
		}
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
