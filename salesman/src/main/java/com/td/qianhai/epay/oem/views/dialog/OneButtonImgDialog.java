package com.td.qianhai.epay.oem.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

/**
 * 单按钮带titel(中间textView无限制下拉)Dialog
 * 
 * @author liangge
 * 
 */
public class OneButtonImgDialog extends Dialog implements
		android.view.View.OnClickListener {
	/** 标题、内容 */
	private TextView tv_title, tv_content;
	/** 确认、取消 */
	private Button bt_affirm;
	/** 标题、内容、按钮确认的名字、按钮取消的名字(按钮默认为：确认更新、取消) */
	private String title, content, affirmStr;
	/** dialog监听器 */
	public OnMyDialogClickListener onMyDialogClickListener;
	/** 图片标识 */
	private ImageView iv_identify;

	private int imgRes;

	public OneButtonImgDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public OneButtonImgDialog(Context context, int theme, String title,
			String content, String affirmStr,
			OnMyDialogClickListener onMyDialogClickListener, int imgRes) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.title = title;
		this.content = content;
		this.affirmStr = affirmStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
		this.imgRes = imgRes;
	}

	public OneButtonImgDialog(Context context, String title, String content,
			String affirmStr, OnMyDialogClickListener onMyDialogClickListener,
			int imgRes) {
		super(context);
		// TODO Auto-generated constructor stub
		this.title = title;
		this.content = content;
		this.affirmStr = affirmStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
		this.imgRes = imgRes;
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.add_title);
		tv_content = (TextView) findViewById(R.id.alert_message);
		bt_affirm = (Button) findViewById(R.id.Button_OK);
		iv_identify = (ImageView) findViewById(R.id.d_img);
		tv_title.setText(title);
		tv_content.setText(content);
		bt_affirm.setText(affirmStr);
		bt_affirm.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_template_band_img_one);
		initView();
		iv_identify.setImageResource(imgRes);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		onMyDialogClickListener.onClick(v);
	}
}
