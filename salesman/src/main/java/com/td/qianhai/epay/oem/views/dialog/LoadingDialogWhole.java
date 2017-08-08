package com.td.qianhai.epay.oem.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
/**
 * 旋转dialog加载
 * @author liangge
 *
 */
public class LoadingDialogWhole extends Dialog {

	private Context context;
	/** 内容 */
	private TextView tv_text;
	/** 加载图 */
	private ImageView iv_progress;
	/** 内容 */
	private String content;

	public LoadingDialogWhole(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public LoadingDialogWhole(Context context, int theme, String content) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.content = content;
	}

	public LoadingDialogWhole(Context context, String content) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.content = content;
	}

	private void initView() {
		tv_text = (TextView) findViewById(R.id.text);
		iv_progress = (ImageView) findViewById(R.id.progress);
		tv_text.setText(content);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_container_whole);
		initView();
		Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.loading_img);  
		LinearInterpolator lin = new LinearInterpolator();  
		operatingAnim.setInterpolator(lin); 
        iv_progress.setAnimation(operatingAnim);
        
	}
}
