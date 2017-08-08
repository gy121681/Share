package com.td.qianhai.epay.oem.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

/**
 * 菜单式dialog
 * 
 * @author liangge
 * 
 */
public class MenuDialog extends Dialog implements
		android.view.View.OnClickListener {
	/** 标题 */
	private TextView tv_title;
	/** 分割线1、2、3、4、5 */
	private View view, view2, view3, view4, view5;
	/** 按钮1、2、3、4、5 */
	private Button button, button2, button3, button4, button5;
	/** 标记要显示的按钮的数量 */
	private int number;
	/** 按钮的显示 */
	private String btnStr, btnStr2, btnStr3, btnStr4, btnStr5;

	private OnMyDialogClickListener onMyDialogClickListener;

	public MenuDialog(Context context, boolean cancelable,
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
	public MenuDialog(Context context, int theme, String btnStr,
			OnMyDialogClickListener onMyDialogClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.number = 1;
		this.btnStr = btnStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	/**
	 * 两个按钮
	 * 
	 * @param context
	 * @param theme
	 * @param number
	 * @param btnStr
	 */
	public MenuDialog(Context context, int theme, String btnStr,
			String btnStr2, OnMyDialogClickListener onMyDialogClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.number = 2;
		this.btnStr = btnStr;
		this.btnStr2 = btnStr2;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	/**
	 * 三个按钮
	 * 
	 * @param context
	 * @param theme
	 * @param number
	 * @param btnStr
	 */
	public MenuDialog(Context context, int theme, String btnStr,
			String btnStr2, String btnStr3,
			OnMyDialogClickListener onMyDialogClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.number = 3;
		this.btnStr = btnStr;
		this.btnStr2 = btnStr2;
		this.btnStr3 = btnStr3;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	/**
	 * 四个按钮
	 * 
	 * @param context
	 * @param theme
	 * @param number
	 * @param btnStr
	 */
	public MenuDialog(Context context, int theme, String btnStr,
			String btnStr2, String btnStr3, String btnStr4,
			OnMyDialogClickListener onMyDialogClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.number = 4;
		this.btnStr = btnStr;
		this.btnStr2 = btnStr2;
		this.btnStr3 = btnStr3;
		this.btnStr4 = btnStr4;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	/**
	 * 五个按钮
	 * 
	 * @param context
	 * @param theme
	 * @param number
	 * @param btnStr
	 */
	public MenuDialog(Context context, int theme, String btnStr,
			String btnStr2, String btnStr3, String btnStr4, String btnStr5,
			OnMyDialogClickListener onMyDialogClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.number = 5;
		this.btnStr = btnStr;
		this.btnStr2 = btnStr2;
		this.btnStr3 = btnStr3;
		this.btnStr4 = btnStr4;
		this.btnStr5 = btnStr5;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	public MenuDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title);
		view = (View) findViewById(R.id.divider1);
		view2 = (View) findViewById(R.id.divider2);
		view3 = (View) findViewById(R.id.divider3);
		view4 = (View) findViewById(R.id.divider4);
		view5 = (View) findViewById(R.id.divider5);
		button = (Button) findViewById(R.id.btn1);
		button2 = (Button) findViewById(R.id.btn2);
		button3 = (Button) findViewById(R.id.btn3);
		button4 = (Button) findViewById(R.id.btn4);
		button5 = (Button) findViewById(R.id.btn5);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_menu_template);
		initView();
		switch (number) {
		case 1:
			button.setVisibility(View.VISIBLE);
			view.setVisibility(View.VISIBLE);
			button.setText(btnStr);
			button.setOnClickListener(this);
			break;
		case 2:
			button.setVisibility(View.VISIBLE);
			button.setText(btnStr);
			view.setVisibility(View.VISIBLE);
			button2.setVisibility(View.VISIBLE);
			button2.setText(btnStr2);
			view2.setVisibility(View.VISIBLE);
			button.setOnClickListener(this);
			button2.setOnClickListener(this);
			break;
		case 3:
			button.setVisibility(View.VISIBLE);
			view.setVisibility(View.VISIBLE);
			button.setText(btnStr);
			button2.setVisibility(View.VISIBLE);
			view2.setVisibility(View.VISIBLE);
			button2.setText(btnStr2);
			button3.setVisibility(View.VISIBLE);
			view3.setVisibility(View.VISIBLE);
			button3.setText(btnStr3);
			button.setOnClickListener(this);
			button2.setOnClickListener(this);
			button3.setOnClickListener(this);
			break;
		case 4:
			button.setVisibility(View.VISIBLE);
			button.setText(btnStr);
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
			button.setOnClickListener(this);
			button2.setOnClickListener(this);
			button3.setOnClickListener(this);
			button4.setOnClickListener(this);
			break;
		case 5:
			button.setVisibility(View.VISIBLE);
			button.setText(btnStr);
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
			button.setOnClickListener(this);
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

}
