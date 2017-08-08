package com.shareshenghuo.app.shop.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.widget.CountDownButton;


public class EidtDialog extends Dialog implements OnClickListener {
	
	/** 标题、内容 */
	private TextView tv_title, tv_money;
	/** 确认、取消 */
	private TextView bt_affirm, bt_cancel,tv_setnum;

	/** 标题、内容、按钮确认的名字、按钮取消的名字(按钮默认为：确认更新、取消) */
	private String title, affirmStr, cancelStr;
	private String content;
	/** dialog监听器 */
	public OnEditDialogChlicListener onMyDialogClickListener;
	
	private Context context;
	private String moneys;
	
	private EditText getnum;
	private int a = 0;
	private CountDownButton btnGetVCode;


	public EidtDialog(Context context, int theme, String title, String content,
			String affirmStr, String cancelStr, String ratemoney,
			OnEditDialogChlicListener onMyDialogClickListener,int a) {
		super(context,theme);
		this.context = context;
		this.title = title;
		this.content = content;
		this.a = a;
		this.cancelStr = cancelStr;
		this.moneys = ratemoney;
		this.affirmStr = affirmStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.btnGetVCode){
			btnGetVCode.getVCode(null, null);
		}
		onMyDialogClickListener.onClick(v, getnum.getText().toString());
		
	}
	
	

	@Override
	protected void onStart() {
		super.onStart();
		btnGetVCode.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		btnGetVCode.onStop();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.edit_dialog2);

		initview();
	}

	private void initview() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		bt_affirm = (TextView) findViewById(R.id.btn_right);
		bt_cancel = (TextView) findViewById(R.id.llWalletRecharge);
		getnum = (EditText) findViewById(R.id.edVCode);
		btnGetVCode = (CountDownButton) findViewById(R.id.btnGetVCode);
		btnGetVCode.getVCode(null, null);
		if(a==1){
			getnum.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
		}
		
//		tv_setnum = (TextView) findViewById(R.id.tv_setnum);
		
		tv_title.setText(title);
		// paypwd.setHint(content);
//		bt_affirm.setText(affirmStr);
		bt_cancel.setText(cancelStr);
		
		btnGetVCode.setOnClickListener(this);
		bt_affirm.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
//		tv_setnum.setText(content);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		
	}

}
