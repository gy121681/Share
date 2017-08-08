package com.td.qianhai.epay.oem.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

/**
 * 一按钮带titel(中间textView无限制下拉)Dialog样式2
 * 
 * @author liangge
 * 
 */
public class OneButtonDialogWarn extends Dialog implements
		android.view.View.OnClickListener {
	/** 标题、内容 */
	private TextView tv_title, tv_content;
	/** 确认、取消 */
	private TextView bt_affirm;
	/** 标题、内容、按钮确认的名字、按钮取消的名字(按钮默认为：确认更新、取消) */
	private String title, content, affirmStr;
	/** dialog监听器 */
	public OnMyDialogClickListener onMyDialogClickListener;

	public OneButtonDialogWarn(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public OneButtonDialogWarn(Context context, int theme, String title,
			String content, String affirmStr,
			OnMyDialogClickListener onMyDialogClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.title = title;
		this.content = content;
		this.affirmStr = affirmStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}
	
	
	public OneButtonDialogWarn(Context context, String title, String content,
			String affirmStr, OnMyDialogClickListener onMyDialogClickListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.title = title;
		this.content = content;
		this.affirmStr = affirmStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_content = (TextView) findViewById(R.id.tv_prompt);
		bt_affirm = (TextView) findViewById(R.id.btn_left);
		tv_title.setText(title);
		tv_content.setText(content);
		bt_affirm.setText(affirmStr);
		bt_affirm.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_resultdetail_warn);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		onMyDialogClickListener.onClick(v);
	}
}
