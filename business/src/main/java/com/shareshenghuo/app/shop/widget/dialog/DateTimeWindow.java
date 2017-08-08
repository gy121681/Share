package com.shareshenghuo.app.shop.widget.dialog;

import java.util.Calendar;
import java.util.Date;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.util.DateUtil;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class DateTimeWindow extends CommonDialog implements OnClickListener {
	
	private DatePicker datePicker;
	private TimePicker timePicker;
	
	private PickTimeCallback callback;

	public DateTimeWindow(Context context) {
		super(context, R.layout.dlg_date_time_picker);
	}

	@Override
	public void initDlgView() {
		datePicker = getView(R.id.datePicker);
		timePicker = getView(R.id.timePicker);
		
		Calendar c = Calendar.getInstance();
		datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), null);
		timePicker.setIs24HourView(true);
		
		getView(R.id.btnCancel).setOnClickListener(this);
		getView(R.id.btnOK).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnCancel:
			dismiss();
			break;
			
		case R.id.btnOK:
			if(callback != null) {
				Calendar c = Calendar.getInstance();
				c.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), 
						timePicker.getCurrentHour(), timePicker.getCurrentMinute());
				callback.timePicked(DateUtil.getDateStr(c.getTime(), "yyyy-MM-dd HH:mm"));
			}
			dismiss();
			break;
		}
	}
	
	public void setPickTimeCallback(PickTimeCallback callback) {
		this.callback = callback;
	}
	
	public interface PickTimeCallback {
		public void timePicked(String dateTime);
	}
}
