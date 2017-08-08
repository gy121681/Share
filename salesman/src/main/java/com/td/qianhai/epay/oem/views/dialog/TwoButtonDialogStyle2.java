package com.td.qianhai.epay.oem.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

/**
 * 双按钮带titel(中间textView无限制下拉)Dialog样式2
 * @author liangge
 * 
 */
public class TwoButtonDialogStyle2 extends Dialog implements
		android.view.View.OnClickListener {
	/** 标题、内容 */
	private TextView tv_title, tv_content;
	/** 确认、取消 */
	private TextView bt_affirm, bt_cancel;
	/** 标题、内容、按钮确认的名字、按钮取消的名字(按钮默认为：确认更新、取消) */
	private String title, affirmStr, cancelStr;
	private SpannableString content;
	/** dialog监听器 */
	public OnMyDialogClickListener onMyDialogClickListener;

	public TwoButtonDialogStyle2(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public TwoButtonDialogStyle2(Context context, int theme, String title, SpannableString content,
			String affirmStr, String cancelStr,
			OnMyDialogClickListener onMyDialogClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.title = title;
		this.content = content;
		this.cancelStr = cancelStr;
		this.affirmStr = affirmStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	public TwoButtonDialogStyle2(Context context, String title, SpannableString content,
			String affirmStr, String cancelStr,
			OnMyDialogClickListener onMyDialogClickListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.title = title;
		this.content = content;
		this.cancelStr = cancelStr;
		this.affirmStr = affirmStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_content = (TextView) findViewById(R.id.tv_prompt);
		bt_affirm = (TextView) findViewById(R.id.btn_left);
		bt_cancel = (TextView) findViewById(R.id.btn_right);
		tv_title.setText(title);
		tv_content.setText(content);
		bt_affirm.setText(cancelStr);
		bt_cancel.setText(affirmStr);
		bt_affirm.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_resultdetail_zhixiao);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		onMyDialogClickListener.onClick(v);
	}
}
