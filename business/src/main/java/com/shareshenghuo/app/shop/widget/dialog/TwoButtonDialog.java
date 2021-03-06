package com.shareshenghuo.app.shop.widget.dialog;





import com.shareshenghuo.app.shop.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 双按钮带titel(中间textView无限制下拉)Dialog
 * 
 * @author liangge
 * 
 */
public class TwoButtonDialog extends Dialog implements
		android.view.View.OnClickListener {
	/** 标题、内容 */
	private TextView tv_title, tv_content;
	/** 确认、取消 */
	private Button bt_affirm, bt_cancel;
	/** 标题、内容、按钮确认的名字、按钮取消的名字(按钮默认为：确认更新、取消) */
	private String title, content, affirmStr, cancelStr;
	/** dialog监听器 */
	public OnMyDialogClickListener onMyDialogClickListener;
	private boolean  tag = false;
	private View tv_lin;

	public TwoButtonDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public TwoButtonDialog(Context context, int theme, String title, String content,
			String affirmStr, String cancelStr,
			boolean i, OnMyDialogClickListener onMyDialogClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.title = title;
		this.content = content;
		this.cancelStr = cancelStr;
		this.tag = i;
		this.affirmStr = affirmStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	public TwoButtonDialog(Context context, String title, String content,
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
		tv_lin = findViewById(R.id.tv_lin);
		tv_title = (TextView) findViewById(R.id.add_title);
		tv_content = (TextView) findViewById(R.id.alert_message);
		bt_affirm = (Button) findViewById(R.id.Button_OK);
		bt_cancel = (Button) findViewById(R.id.Button_cancel);
		
		if(tag){
			tv_content.setGravity(Gravity.CENTER);
		}
		tv_title.setText(title);
		tv_content.setText(content);
		bt_affirm.setText(affirmStr);
		bt_cancel.setText(cancelStr);
		bt_affirm.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
		if(TextUtils.isEmpty(affirmStr)){
			tv_lin.setVisibility(View.GONE);
			bt_affirm.setVisibility(View.GONE);
		}
		if(TextUtils.isEmpty(cancelStr)){
			tv_lin.setVisibility(View.GONE);
			bt_cancel.setVisibility(View.GONE);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.global_dialog_template);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		onMyDialogClickListener.onClick(v);
	}
}
