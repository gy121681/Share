package com.td.qianhai.epay.oem.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.td.qianhai.epay.oem.R;

public class DateDialogBlack_haveday extends Dialog {

	String[] yearString;
	String[][] dayString = {
			new String[] { "01", "02", "03", "04", "05", "06", "07", "08",
					"09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
					"19", "20", "21", "22", "23", "24", "25", "26", "27", "28" },
			new String[] { "01", "02", "03", "04", "05", "06", "07", "08",
					"09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
					"19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
					"29" },
			new String[] { "01", "02", "03", "04", "05", "06", "07", "08",
					"09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
					"19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
					"29", "30" },
			new String[] { "01", "02", "03", "04", "05", "06", "07", "08",
					"09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
					"19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
					"29", "30", "31" }

	};
	String[] monthString = new String[] { "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12" };
	int[] dayNumSelect = { 1, 3, 0, 3, 2, 3, 2, 3, 3, 2, 3, 2, 3 };
	int year;
	int month;
	int day;
	Handler handler;
	Time defaultDate;
	int yearSelect;

	Context context;
	WheelView yearView;
	WheelView monthView;
	WheelView dayView;
	View date;
	Button back;
	Button sure;
	View onClickView;

	public DateDialogBlack_haveday(Context context, int theme,
			Handler calculatorDateControlHandler, View v, Time defaultDate) {
		super(context, theme);
		this.context = context;

		onClickView = v;
		handler = calculatorDateControlHandler;
		this.defaultDate = defaultDate;

		initDateStrs();
		initDateView();

		this.setContentView(date);

	}

	private void initDateView() {

		date = LayoutInflater.from(context).inflate(
				R.layout.datedialog_black_haveday, null);
		yearView = (WheelView) date.findViewById(R.id.dateDialoghd_year);
		monthView = (WheelView) date.findViewById(R.id.dateDialoghd_mouth);
		dayView = (WheelView) date.findViewById(R.id.dateDialoghd_day);
		sure = (Button) date.findViewById(R.id.dateblackhd_surebutton);
		back = (Button) date.findViewById(R.id.dateblackhd_backbutton);

		sure.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Bundle bundle = new Bundle();
				bundle.putInt("year", year);
				bundle.putInt("month", month);
				bundle.putInt("day", day);
				bundle.putInt("view", onClickView.getId());

				Message msg = new Message();
				msg.setData(bundle);
				handler.sendMessage(msg);
				dismiss();
			}
		});

		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});

		yearView.setVisibleItems(5);
		monthView.setVisibleItems(5);
		dayView.setVisibleItems(5);

		yearView.setAdapter(new ArrayWheelAdapter<String>(yearString));
		monthView.setAdapter(new ArrayWheelAdapter<String>(monthString));
		setDayAdapter(defaultDate.year, defaultDate.month);

		yearView.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				year = 1900 + newValue;
				if (month == 1
						&& isLeapYear(oldValue + 1900) != isLeapYear(year))
					setDayAdapter(year, month);
			}
		});

		monthView.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				month = newValue;
				setDayAdapter(year, month);
			}
		});

		dayView.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				day = newValue + 1;

			}
		});

		yearView.setCurrentItem(year - 1900);
		monthView.setCurrentItem(month);
		dayView.setCurrentItem(day - 1);

	}

	private void setDayAdapter(int yearselect, int monthselect) {
		monthselect++;
		if (monthselect == 2 && isLeapYear(yearselect)) {
			dayView.setAdapter(new ArrayWheelAdapter<String>(
					dayString[dayNumSelect[0]]));
			if ((dayString[dayNumSelect[0]]).length < day) {
				dayView.setCurrentItem((dayString[dayNumSelect[0]]).length - 1);
				day = (dayString[dayNumSelect[0]]).length;
			}

		} else {
			dayView.setAdapter(new ArrayWheelAdapter<String>(
					dayString[dayNumSelect[monthselect]]));
			if ((dayString[dayNumSelect[monthselect]]).length < day) {
				dayView.setCurrentItem((dayString[dayNumSelect[monthselect]]).length - 1);
				day = (dayString[dayNumSelect[monthselect]]).length;
			}
		}

	}

	private void initDateStrs() {

		yearString = new String[300];
		for (int i = 1900; i < 2199; i++) {
			yearString[i - 1900] = "" + i;
		}

		year = defaultDate.year;
		month = defaultDate.month;
		day = defaultDate.monthDay;

	}

	boolean isLeapYear(int year) {
		if (year == 1900 || year == 2000 || year == 2100) {
			if (year % 400 == 0) {
				return true;
			}
		} else {
			if (year % 4 == 0 && year % 100 != 0) {
				return true;
			}
		}

		return false;
	}

}
