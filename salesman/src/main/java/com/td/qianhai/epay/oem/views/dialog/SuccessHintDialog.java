package com.td.qianhai.epay.oem.views.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class SuccessHintDialog extends Dialog implements
		android.view.View.OnClickListener {

	/** 标题、内容 */
	private TextView tv_title, tv_content;
	/** 确认、取消 */
	private TextView bt_content2;
	/** 标题、内容、按钮确认的名字、按钮取消的名字(按钮默认为：确认更新、取消) */
	private String title, content, affirmStr;
	private Button bt_ok;
	/** dialog监听器 */
	public OnMyDialogClickListener onMyDialogClickListener;

	public SuccessHintDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public SuccessHintDialog(Context context, int theme, String title,
			String content, String affirmStr,
			OnMyDialogClickListener onMyDialogClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.title = title;
		this.content = content;
		this.affirmStr = affirmStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	public SuccessHintDialog(Context context, String title, String content,
			String affirmStr, OnMyDialogClickListener onMyDialogClickListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.title = title;
		this.content = content;
		this.affirmStr = affirmStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.common_dialog_title);
		tv_content = (TextView) findViewById(R.id.common_dialog_msg_line_bold);
		// bt_content2 = (TextView)
		// findViewById(R.id.layout_common_dialog_msg_line_bold);
		bt_ok = (Button) findViewById(R.id.buttonOk);
		tv_title.setText(title);
		tv_content.setText(content);
		// bt_content2.setVisibility(View.GONE);
		bt_ok.setText(affirmStr);
		bt_ok.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.withdrawal_ok);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		onMyDialogClickListener.onClick(v);
	}

}
