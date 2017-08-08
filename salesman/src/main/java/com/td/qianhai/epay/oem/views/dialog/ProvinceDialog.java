package com.td.qianhai.epay.oem.views.dialog;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.td.qianhai.epay.oem.R;

public class ProvinceDialog extends Dialog {

	// 定义回调事件，用于dialog的点击事件
	public interface OnProvinceDialogListener {
		public void back(HashMap<String, Object> map);
	}

	private ArrayList<HashMap<String, Object>> list;
	private OnProvinceDialogListener provinceDialogListener;
	private Context context;
	private int state;

	public ProvinceDialog(int theme, int state, Context context,
			ArrayList<HashMap<String, Object>> list,
			OnProvinceDialogListener listener) {
		super(context, theme);
		this.context = context;
		this.list = list;
		this.provinceDialogListener = listener;
		this.state = state;
	}

	public ProvinceDialog(int state, Context context,
			ArrayList<HashMap<String, Object>> list,
			OnProvinceDialogListener listener) {
		super(context, android.R.style.Theme_DeviceDefault_Light_Dialog);
		this.context = context;
		this.list = list;
		this.provinceDialogListener = listener;
		this.state = state;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bank_listview);
		ListView listView = (ListView) findViewById(R.id.lv_bank_list_province);
		Button button = (Button) findViewById(R.id.bank_listview_load_more);
		LinearLayout relativeLayout = (LinearLayout) findViewById(R.id.bank_relativelayout);
		relativeLayout.setVisibility(View.GONE);
		String key = "";
		if (state == 1) {
			key = "PROVNAM";
		} else if (state == 2) {
			key = "CITYNAM";
		} else if (state == 3) {
			key = "BENELX";
		} else if (state == 4) {
			key = "MACE";
		}
		listView.setAdapter(new SimpleAdapter(context, list,
				R.layout.bank_listview_item, new String[] { key },
				new int[] { R.id.tv_bank_listview_item }));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				provinceDialogListener.back(list.get(arg2));
				ProvinceDialog.this.dismiss();
			}
		});
	}

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
	 * if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){ return
	 * false; } return super.onKeyDown(keyCode, event); }
	 */

}
