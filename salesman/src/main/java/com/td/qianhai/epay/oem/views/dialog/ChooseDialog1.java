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
import com.td.qianhai.epay.oem.adapter.ClearingAdapter1;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnBackDialogClickListener;

/**
 * 结算方式选择专用dialog
 * 
 * @author liangge
 * 
 */
public class ChooseDialog1 extends Dialog {

	private TextView tvTitle;

	private ListView listView;

	private ClearingAdapter1 clearingAdapter;

	private Context context;

	private String[] tempArr;

	private ArrayList<HashMap<String, Object>> list;

	private HashMap<String, String> itemMap;

	private String title;

	private OnBackDialogClickListener backDialogClickListener;

	public ChooseDialog1(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public ChooseDialog1(Context context, int theme,
			OnBackDialogClickListener backDialogClickListener, String title,
			ArrayList<HashMap<String, Object>> list) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.backDialogClickListener = backDialogClickListener;
		this.title = title;
		this.list = new ArrayList<HashMap<String, Object>>();
		this.list.addAll(list);
//		this.tempArr = tempArr;
	}

	public ChooseDialog1(Context context) {
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
		tvTitle.setText(title);
		listView = (ListView) findViewById(R.id.lv_filter);
		// tempArr = context.getResources().getStringArray(R.array.clearing);
		
		
		if(list!=null){
//			for (int i = 0; i < tempArr.length; i++) {
//				itemMap = new HashMap<String, String>();
//				itemMap.put("itemName", tempArr[i]);
//				list.add(itemMap);
				clearingAdapter = new ClearingAdapter1(context, list);
				listView.setAdapter(clearingAdapter);
//			}

		}

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v,
					final int position, long arg3) {
				// TODO Auto-generated method stub
				backDialogClickListener.OnBackClick(v, list.get(position).get("FEERATE").toString(),
						position);
			}
		});
	}
}
