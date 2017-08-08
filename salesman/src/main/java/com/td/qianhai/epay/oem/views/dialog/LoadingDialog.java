package com.td.qianhai.epay.oem.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
/**
 * 旋转dialog加载
 * @author liangge
 *
 */
public class LoadingDialog extends Dialog implements
		android.view.View.OnClickListener {

	private Context context;
	/** 内容 */
	private TextView tv_text;
	/** 取消 */
	private Button bt_close;
	/** 加载图 */
	private ImageView iv_progress;
	/** 内容 */
	private String content;
	/** dialog监听器 */
	public OnMyDialogClickListener onMyDialogClickListener;

	public LoadingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public LoadingDialog(Context context, int theme, String content,
			OnMyDialogClickListener onMyDialogClickListener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.content = content;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	public LoadingDialog(Context context, String content,
			OnMyDialogClickListener onMyDialogClickListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.content = content;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	private void initView() {
		tv_text = (TextView) findViewById(R.id.text);
		bt_close = (Button) findViewById(R.id.close);
		iv_progress = (ImageView) findViewById(R.id.progress);
		tv_text.setText(content);
		bt_close.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_container);
		initView();
		Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.loading_img);  
		LinearInterpolator lin = new LinearInterpolator();  
		operatingAnim.setInterpolator(lin); 
        iv_progress.setAnimation(operatingAnim);
        
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		onMyDialogClickListener.onClick(v);
	}
}
