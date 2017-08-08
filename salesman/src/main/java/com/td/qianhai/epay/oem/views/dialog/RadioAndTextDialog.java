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
import android.widget.ListView;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.adapter.RadioAdapter;
import com.td.qianhai.epay.oem.adapter.TextAdapter;

/**
 * 单选dialog
 * 
 * @author liangge
 * 
 */
public class RadioAndTextDialog extends Dialog {
	private Context context;
	private TextView t_title; // 标题
	private ListView listView; // 选项
	private ArrayList<HashMap<String, String>> list;// item
	private String title;// 标题来源
	private OnItemDialogListener onItemDialogListener;
	private int cbPosition;
	private RadioAdapter radioAdapter;
	private TextAdapter textAdapter;
	private Button cancel;

	public RadioAndTextDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	/**
	 * RadioAdater
	 * 
	 * @param context
	 * @param theme
	 * @param list
	 * @param title
	 * @param onItemDialogListener
	 * @param cbPosition
	 * @param radioAdapter
	 */
	public RadioAndTextDialog(Context context, int theme,
			ArrayList<HashMap<String, String>> list, String title,
			OnItemDialogListener onItemDialogListener, int cbPosition,
			RadioAdapter radioAdapter) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.list = list;
		this.title = title;
		this.context = context;
		this.onItemDialogListener = onItemDialogListener;
		this.cbPosition = cbPosition;
		this.textAdapter=null;
		this.radioAdapter = radioAdapter;
	}

	/**
	 * TextAdapter
	 * 
	 * @param context
	 * @param theme
	 * @param list
	 * @param title
	 * @param onItemDialogListener
	 * @param cbPosition
	 * @param textAdapter
	 */
	public RadioAndTextDialog(Context context, int theme,
			ArrayList<HashMap<String, String>> list, String title,
			OnItemDialogListener onItemDialogListener, int cbPosition,
			TextAdapter textAdapter) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.list = list;
		this.title = title;
		this.context = context;
		this.onItemDialogListener = onItemDialogListener;
		this.cbPosition = cbPosition;
		this.radioAdapter=null;
		this.textAdapter = textAdapter;
	}

	/**
	 * textAdapter
	 * 
	 * @param context
	 * @param list
	 *            list集合item
	 * @param title
	 *            标题
	 */
	public RadioAndTextDialog(Context context,
			ArrayList<HashMap<String, String>> list, String title,
			OnItemDialogListener onItemDialogListener, int cbPosition,
			TextAdapter textAdapter) {
		super(context);
		// TODO Auto-generated constructor stub
		this.list = list;
		this.title = title;
		this.context = context;
		this.onItemDialogListener = onItemDialogListener;
		this.cbPosition = cbPosition;
		this.textAdapter = textAdapter;
	}

	/**
	 * RadioAdapter
	 * 
	 * @param context
	 * @param list
	 * @param title
	 * @param onItemDialogListener
	 * @param cbPosition
	 * @param radioAdapter
	 */
	public RadioAndTextDialog(Context context,
			ArrayList<HashMap<String, String>> list, String title,
			OnItemDialogListener onItemDialogListener, int cbPosition,
			RadioAdapter radioAdapter) {
		super(context);
		// TODO Auto-generated constructor stub
		this.list = list;
		this.title = title;
		this.context = context;
		this.onItemDialogListener = onItemDialogListener;
		this.cbPosition = cbPosition;
		this.textAdapter=null;
		this.radioAdapter = radioAdapter;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.air_dialog_list);
		t_title = (TextView) findViewById(R.id.dialog_title_txt1);
		t_title.setText(title);
		listView = (ListView) findViewById(R.id.dialog_listView);

		if (textAdapter == null && radioAdapter != null) {
			radioAdapter.setSelectorposition(cbPosition);
			listView.setAdapter(radioAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long arg3) {
					// TODO Auto-generated method stub
					onItemDialogListener.back(list.get(position)
							.get("itemName"), position);
					radioAdapter.setSelectorposition(position);
					radioAdapter.notifyDataSetChanged();
					// 取消dialog
					RadioAndTextDialog.this.dismiss();
				}
			});
		}else if(textAdapter!=null && radioAdapter==null){
			cancel=(Button)findViewById(R.id.dialog_button);
			cancel.setVisibility(View.VISIBLE);
			listView.setAdapter(textAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long arg3) {
					// TODO Auto-generated method stub
					onItemDialogListener.back(list.get(position)
							.get("itemName"), position);
					// 取消dialog
					RadioAndTextDialog.this.dismiss();
				}
			});
			cancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// 取消dialog
					RadioAndTextDialog.this.dismiss();
				}
			});
		}
	}

	// 定义回调事件，用于dialog的点击事件
	public interface OnItemDialogListener {
		public void back(String itemName, int cbPosition);
	}
}
