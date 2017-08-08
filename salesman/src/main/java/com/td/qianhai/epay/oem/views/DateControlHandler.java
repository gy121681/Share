package com.td.qianhai.epay.oem.views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class DateControlHandler extends Handler {

	public int year;
	public int month;
	public int day;
	public int viewId;

	/**
	 * 重写此方法 变量为:year,month,day,viewid(均为int)
	 */
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);

		Bundle bundle = msg.getData();
		year = bundle.getInt("year");
		month = bundle.getInt("month");
		day = bundle.getInt("day");
		viewId = bundle.getInt("view");
	}
}
