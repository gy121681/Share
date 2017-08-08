package com.td.qianhai.epay.oem.views.dialog;


import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.adapter.LDSearshAdapter;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnLDSearchListClickListener;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 菜单式dialog
 * 
 * @author liangge
 * 
 */
public class SearchDeviceDialog extends Dialog implements
		android.view.View.OnClickListener, OnItemClickListener {
	/** 标题 */
	// private TextView tv_title;
	/** 分割线1、2、3、4、5 */
	private View view, view2, view3, view4, view5;
	/** 按钮1、2、3、4、5 */
	private Button button2, button3, button4, button5;

	private ListView lv1;
	private LDSearshAdapter ldSearshAdapter;
	public OnLDSearchListClickListener onLDSearchListClickListener;
	/** 标记要显示的按钮的数量 */
	private int number;
	/** 按钮的显示 */
	private String btnStr2, btnStr3, btnStr4, btnStr5;

	private OnMyDialogClickListener onMyDialogClickListener;

	public SearchDeviceDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 一个按钮
	 * 
	 * @param context
	 * @param theme
	 * @param number
	 * @param btnStr
	 */
	public SearchDeviceDialog(Context context, int theme,
			OnMyDialogClickListener onMyDialogClickListener,
			LDSearshAdapter ldSearshAdapter,
			OnLDSearchListClickListener onLDSearchListClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.number = 1;
		this.onMyDialogClickListener = onMyDialogClickListener;
		this.ldSearshAdapter = ldSearshAdapter;
		this.onLDSearchListClickListener = onLDSearchListClickListener;
	}

	/**
	 * 两个按钮
	 * 
	 * @param context
	 * @param theme
	 * @param number
	 * @param btnStr
	 */
	public SearchDeviceDialog(Context context, int theme, String btnStr2,
			OnMyDialogClickListener onMyDialogClickListener,
			LDSearshAdapter ldSearshAdapter,
			OnLDSearchListClickListener onLDSearchListClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.number = 2;
		this.btnStr2 = btnStr2;
		this.onMyDialogClickListener = onMyDialogClickListener;
		this.ldSearshAdapter = ldSearshAdapter;
		this.onLDSearchListClickListener = onLDSearchListClickListener;
	}

	/**
	 * 三个按钮
	 * 
	 * @param context
	 * @param theme
	 * @param number
	 * @param btnStr
	 */
	public SearchDeviceDialog(Context context, int theme, String btnStr2,
			String btnStr3, OnMyDialogClickListener onMyDialogClickListener,
			LDSearshAdapter ldSearshAdapter,
			OnLDSearchListClickListener onLDSearchListClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.number = 3;
		this.btnStr2 = btnStr2;
		this.btnStr3 = btnStr3;
		this.ldSearshAdapter = ldSearshAdapter;
		this.onMyDialogClickListener = onMyDialogClickListener;
		this.onLDSearchListClickListener = onLDSearchListClickListener;
	}

	/**
	 * 四个按钮
	 * 
	 * @param context
	 * @param theme
	 * @param number
	 * @param btnStr
	 */
	public SearchDeviceDialog(Context context, int theme, String btnStr2,
			String btnStr3, String btnStr4,
			OnMyDialogClickListener onMyDialogClickListener,
			LDSearshAdapter ldSearshAdapter,
			OnLDSearchListClickListener onLDSearchListClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.number = 4;
		this.btnStr2 = btnStr2;
		this.btnStr3 = btnStr3;
		this.btnStr4 = btnStr4;
		this.onMyDialogClickListener = onMyDialogClickListener;
		this.ldSearshAdapter = ldSearshAdapter;
		this.onLDSearchListClickListener = onLDSearchListClickListener;
	}

	/**
	 * 五个按钮
	 * 
	 * @param context
	 * @param theme
	 * @param number
	 * @param btnStr
	 */
	public SearchDeviceDialog(Context context, int theme, String btnStr2,
			String btnStr3, String btnStr4, String btnStr5,
			OnMyDialogClickListener onMyDialogClickListener,
			LDSearshAdapter ldSearshAdapter,
			OnLDSearchListClickListener onLDSearchListClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.number = 5;
		this.btnStr2 = btnStr2;
		this.btnStr3 = btnStr3;
		this.btnStr4 = btnStr4;
		this.btnStr5 = btnStr5;
		this.onMyDialogClickListener = onMyDialogClickListener;
		this.ldSearshAdapter = ldSearshAdapter;
		this.onLDSearchListClickListener = onLDSearchListClickListener;
	}

	public SearchDeviceDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private void initView() {
		// tv_title = (TextView) findViewById(R.id.title);
		view = (View) findViewById(R.id.divider1);
		view2 = (View) findViewById(R.id.divider2);
		view3 = (View) findViewById(R.id.divider3);
		view4 = (View) findViewById(R.id.divider4);
		view5 = (View) findViewById(R.id.divider5);
		button2 = (Button) findViewById(R.id.btn2);
		button3 = (Button) findViewById(R.id.btn3);
		button4 = (Button) findViewById(R.id.btn4);
		button5 = (Button) findViewById(R.id.btn5);
		lv1 = (ListView) findViewById(R.id.lv1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_search_device);
		initView();
		switch (number) {
		case 2:
			view.setVisibility(View.VISIBLE);
			button2.setVisibility(View.VISIBLE);
			button2.setText(btnStr2);
			view2.setVisibility(View.VISIBLE);
			button2.setOnClickListener(this);
			break;
		case 3:
			view.setVisibility(View.VISIBLE);
			button2.setVisibility(View.VISIBLE);
			view2.setVisibility(View.VISIBLE);
			button2.setText(btnStr2);
			button3.setVisibility(View.VISIBLE);
			view3.setVisibility(View.VISIBLE);
			button3.setText(btnStr3);
			if(lv1==null){
				System.out.println("lv1==null");
			}
			if(ldSearshAdapter==null){
				System.out.println("ldSearshAdapter==null");
			}
			lv1.setAdapter(ldSearshAdapter);
			lv1.setOnItemClickListener(this);
			button2.setOnClickListener(this);
			button3.setOnClickListener(this);
			break;
		case 4:
			view.setVisibility(View.VISIBLE);
			button2.setVisibility(View.VISIBLE);
			button2.setText(btnStr2);
			view2.setVisibility(View.VISIBLE);
			button3.setVisibility(View.VISIBLE);
			button3.setText(btnStr3);
			view3.setVisibility(View.VISIBLE);
			button4.setVisibility(View.VISIBLE);
			button4.setText(btnStr4);
			view4.setVisibility(View.VISIBLE);
			button2.setOnClickListener(this);
			button3.setOnClickListener(this);
			button4.setOnClickListener(this);
			break;
		case 5:
			view.setVisibility(View.VISIBLE);
			button2.setVisibility(View.VISIBLE);
			button2.setText(btnStr2);
			view2.setVisibility(View.VISIBLE);
			button3.setVisibility(View.VISIBLE);
			button3.setText(btnStr3);
			view3.setVisibility(View.VISIBLE);
			button4.setVisibility(View.VISIBLE);
			button4.setText(btnStr4);
			view4.setVisibility(View.VISIBLE);
			button5.setVisibility(View.VISIBLE);
			button5.setText(btnStr5);
			view5.setVisibility(View.VISIBLE);
			button2.setOnClickListener(this);
			button3.setOnClickListener(this);
			button4.setOnClickListener(this);
			button5.setOnClickListener(this);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		onMyDialogClickListener.onClick(v);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		onLDSearchListClickListener.onItemClick(parent, view, position, id);
	}

}
