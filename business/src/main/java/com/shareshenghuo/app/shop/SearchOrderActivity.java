package com.shareshenghuo.app.shop;

import java.util.Calendar;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.fragment.OrderListFragment;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class SearchOrderActivity extends BaseTopActivity implements OnClickListener {
	
	private EditText edKeyword;
	private TextView tvStart, tvEnd;
	private OrderListFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_order);
		init();
	}
	
	public void init() {
		initTopBar("订单查找");
		
		edKeyword = getView(R.id.edKeyword);
		tvEnd = getView(R.id.tvDateEnd);
		tvStart = getView(R.id.tvDateStart);
		fragment = (OrderListFragment) getSupportFragmentManager().findFragmentById(R.id.fOrderList);
		
		getView(R.id.ivSearch).setOnClickListener(this);
		tvStart.setOnClickListener(this);
		tvEnd.setOnClickListener(this);
		getView(R.id.btnOK).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.ivSearch:
			if(ViewUtil.checkEditEmpty(edKeyword, "请输入关键字"))
				return;
			fragment.order_no = edKeyword.getText().toString();
			ProgressDialogUtil.showProgressDlg(this, "查找中");
			fragment.onPullDownToRefresh(null);
			break;
			
		case R.id.tvDateStart:
			showDatePicker(tvStart);
			break;
			
		case R.id.tvDateEnd:
			showDatePicker(tvEnd);
			break;
		
		case R.id.btnOK:
			String start = tvStart.getText().toString();
			String end = tvEnd.getText().toString();
			if(TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
				T.showShort(this, "请选择日期");
				return;
			}
			fragment.start_time = start;
			fragment.end_time = end;
			ProgressDialogUtil.showProgressDlg(this, "查找中");
			fragment.onPullDownToRefresh(null);
			break;
		}
	}
	
	public void showDatePicker(final TextView tvDate) {
		Calendar c = Calendar.getInstance();
		new DatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker arg0, int year, int month, int day) {
				tvDate.setText(year+"-"+(month+1)+"-"+day);
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
	}
}
