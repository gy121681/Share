package com.td.qianhai.epay.oem.views.dialog;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.adapter.DealRecordsAdapter;

public class DealTesultDialog extends Dialog{
	private ArrayList<HashMap<String, Object>> list;
	private Context context;
	public DealTesultDialog(Context context, ArrayList<HashMap<String, Object>> list) {
		super(context);
		this.list = list;
		this.context = context;
	
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bank_listview);
		findViewById(R.id.bank_listview_load_more);
		ListView listView = (ListView) findViewById(R.id.lv_bank_list_province);
		DealRecordsAdapter adapter = new DealRecordsAdapter(context, list);
		listView.setAdapter(adapter);
	}

}
