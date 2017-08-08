package com.td.qianhai.epay.oem.views.dialog;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.adapter.ClearingAdapter;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnBackDialogClickListener;

/**
 * 结算方式选择专用dialog
 * 
 * @author liangge
 * 
 */
public class RadioDialog extends Dialog {

	private TextView tvTitle;

	private ListView listView;

	private ClearingAdapter clearingAdapter;

	private Context context;

	private String[] tempArr;

	private ArrayList<HashMap<String, String>> list;

	private HashMap<String, String> itemMap;

	private OnBackDialogClickListener backDialogClickListener;

	public RadioDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public RadioDialog(Context context, int theme,
			OnBackDialogClickListener backDialogClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.backDialogClickListener = backDialogClickListener;
	}

	public RadioDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.radio_dialog);
		tvTitle = (TextView) findViewById(R.id.radio_dialog_title);
		tvTitle.setText("结算方式选择");
		listView = (ListView) findViewById(R.id.lv_filter);
		tempArr = context.getResources().getStringArray(R.array.clearing);
		list = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < tempArr.length; i++) {
			itemMap = new HashMap<String, String>();
			itemMap.put("itemName", tempArr[i]);
			list.add(itemMap);
		}
		clearingAdapter = new ClearingAdapter(context, list);
		listView.setAdapter(clearingAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v,
					final int position, long arg3) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				String str = null;
				if (position == 0) {
					str = "0";
				} else if (position == 1) {
					str = "1";
				} else if (position == 2) {
					str = "5";
				}
				backDialogClickListener.OnBackClick(v, tempArr[position],position);
			}
		});
	}
}
