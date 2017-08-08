package com.shareshenghuo.app.shop.widget.dialog;

import com.shareshenghuo.app.shop.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MyEditDialog1 extends Dialog implements OnClickListener {
	/** 标题、内容 */
	private TextView tv_title ;
	/** 确认、取消 */
	private Button  bt_affirm, bt_cancel;

	private EditText paypwd;//, paypwd1, paypwd2, paypwd3, paypwd4, paypwd5;
	/** 标题、内容、按钮确认的名字、按钮取消的名字(按钮默认为：确认更新、取消) */
	private String title, affirmStr, cancelStr;
	private String content;
	/** dialog监听器 */
	public OnMyDialogClickListener onMyDialogClickListener;
	
	public onMyaddTextListener onMyListener;

	private Context context;
	private String moneys;

	private String a, b, c, d, e, f;

	public MyEditDialog1(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public MyEditDialog1(Context context, int theme, String title,
			String content, String affirmStr, String cancelStr,
			String ratemoney, OnMyDialogClickListener onMyDialogClickListener,onMyaddTextListener onMyListener) {
		super(context, theme);
		this.context = context;
		this.title = title;
		this.content = content;
		this.cancelStr = cancelStr;
		this.moneys = ratemoney;
		this.affirmStr = affirmStr;
		this.onMyDialogClickListener = onMyDialogClickListener;
		this.onMyListener = onMyListener;

	}

	private void initview() {
		tv_title = (TextView) findViewById(R.id.tv_title);
//		tv_money = (TextView) findViewById(R.id.tv_money);
		bt_affirm = (Button) findViewById(R.id.btn_left);
		bt_cancel = (Button) findViewById(R.id.btn_right);
		paypwd = (EditText) findViewById(R.id.searchCa);
		paypwd.requestFocus();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		
//		if (moneys != null && !moneys.equals("")) {
//
//			try {
//				double money = Double.parseDouble(moneys);
//				tv_money.setText((money / 100) + "元");
//			} catch (Exception e) {
//				// TODO: handle exception
//				tv_money.setText(moneys);
//			}
//
//		}else{
//			tv_money.setText("");
//		}
		tv_title.setText(title);
		paypwd.setText(moneys);
		// paypwd.setHint(content);
		bt_affirm.setText(affirmStr);
		bt_cancel.setText(cancelStr);
		bt_affirm.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
		

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.edit_dialog1);

		initview();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		
		switch (v.getId()) {
		case R.id.btn_left:
			onMyListener.refreshActivity(getpaypwd());
			
			break;
		case R.id.btn_right:
			onMyDialogClickListener.onClick(v);
			break;
		default:
			break;
		}
		
	}
	
	
	public String getpaypwd() {

		return paypwd.getText().toString();

	}


//	/** 标题、内容 */
//	private TextView tv_title, tv_money;
//	/** 确认、取消 */
//	private TextView bt_affirm, bt_cancel;
//
//	private EditText paypwd, paypwd1, paypwd2, paypwd3, paypwd4, paypwd5;
//	/** 标题、内容、按钮确认的名字、按钮取消的名字(按钮默认为：确认更新、取消) */
//	private String title, affirmStr, cancelStr;
//	private String content;
//	/** dialog监听器 */
//	public OnMyDialogClickListener onMyDialogClickListener;
//	
//	public onMyaddTextListener onMyListener;
//
//	private Context context;
//	private String moneys;
//
//	private String a, b, c, d, e, f;
//
//	public MyEditDialog(Context context) {
//		super(context);
//		// TODO Auto-generated constructor stub
//		this.context = context;
//	}
//
//	public MyEditDialog(Context context, int theme, String title,
//			String content, String affirmStr, String cancelStr,
//			String ratemoney, OnMyDialogClickListener onMyDialogClickListener,onMyaddTextListener onMyListener) {
//		super(context, theme);
//		this.context = context;
//		this.title = title;
//		this.content = content;
//		this.cancelStr = cancelStr;
//		this.moneys = ratemoney;
//		this.affirmStr = affirmStr;
//		this.onMyDialogClickListener = onMyDialogClickListener;
//		this.onMyListener = onMyListener;
//
//	}
//
//	private void initview() {
//		tv_title = (TextView) findViewById(R.id.tv_title);
//		tv_money = (TextView) findViewById(R.id.tv_money);
//		bt_affirm = (TextView) findViewById(R.id.btn_left);
//		bt_cancel = (TextView) findViewById(R.id.btn_right);
//		paypwd = (EditText) findViewById(R.id.searchCa);
//		paypwd1 = (EditText) findViewById(R.id.searchCb);
//		paypwd2 = (EditText) findViewById(R.id.searchCc);
//		paypwd3 = (EditText) findViewById(R.id.searchCd);
//		paypwd4 = (EditText) findViewById(R.id.searchCe);
//		paypwd5 = (EditText) findViewById(R.id.searchCf);
//		paypwd.requestFocus();
////		paypwd.setFocusable(true);
////		paypwd.setFocusableInTouchMode(true);
////		InputMethodUtils.show(context, paypwd);
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
////		paypwd1.setEnabled(false);
////		paypwd2.setEnabled(false);
////		paypwd3.setEnabled(false);
////		paypwd4.setEnabled(false);
////		paypwd5.setEnabled(false);
//		// a = paypwd.getText().toString();
//		// b = paypwd1.getText().toString();
//		// c = paypwd2.getText().toString();
//		// d = paypwd3.getText().toString();
//		// e = paypwd4.getText().toString();
//		// f = paypwd5.getText().toString();
//		
////		paypwd.setOnKeyListener(new View.OnKeyListener() { 
////		    
////		    @Override 
////		    public boolean onKey(View v, int keyCode, KeyEvent event) { 
////		        if(event.getAction() == KeyEvent.ACTION_DOWN) { 
////		            switch(keyCode) { 
////		            case KeyEvent.KEYCODE_DEL: 
////		            	Log.e("", "点了我啊啊啊啊啊");
////		                break; 
////		            } 
////		        } 
////		        return true; 
////		    } 
////		});
//		
//
//		paypwd.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//				if (s.length() > 0) {
//					a = paypwd.getText().toString();
//					paypwd.clearFocus();
////					paypwd.setEnabled(false);
////					paypwd1.setEnabled(true);
//					paypwd1.requestFocus();
//					
//
//				}
//
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//
//		paypwd1.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//
//				b = paypwd1.getText().toString();
//				if (s.length() == 1) {
//					
//					paypwd1.clearFocus();
////					paypwd2.setEnabled(true);
//					paypwd2.requestFocus();
////					paypwd1.setEnabled(false);
//				} else {
//					
//				}
//
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//
//		paypwd2.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//				if (s.length() == 1) {
//					c = paypwd2.getText().toString();
//					paypwd2.clearFocus();
////					paypwd3.setEnabled(true);
//					paypwd3.requestFocus();
////					paypwd2.setEnabled(false);
//				}
//
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//
//		paypwd3.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//				if (s.length() == 1) {
//					d = paypwd3.getText().toString();
//					paypwd3.clearFocus();
////					paypwd4.setEnabled(true);
//					paypwd4.requestFocus();
////					paypwd3.setEnabled(false);
//				}
//
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//
//		paypwd4.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//				if (s.length() == 1) {
//					e = paypwd4.getText().toString();
//					paypwd4.clearFocus();
////					paypwd5.setEnabled(true);
//					paypwd5.requestFocus();
////					paypwd4.setEnabled(false);
//				}
//
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//		paypwd5.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//				if (s.length() > 0) {
//					f = paypwd5.getText().toString();
////					paypwd5.setEnabled(false);
//					onMyListener.refreshActivity(getpaypwd());
//				}
//
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//		
//
//		paypwd.setTransformationMethod(new AsteriskPasswordTransformationMethod());
//		paypwd1.setTransformationMethod(new AsteriskPasswordTransformationMethod());
//		paypwd2.setTransformationMethod(new AsteriskPasswordTransformationMethod());
//		paypwd3.setTransformationMethod(new AsteriskPasswordTransformationMethod());
//		paypwd4.setTransformationMethod(new AsteriskPasswordTransformationMethod());
//		paypwd5.setTransformationMethod(new AsteriskPasswordTransformationMethod());
//
//		if (moneys != null && !moneys.equals("")) {
//
//			try {
//				double money = Double.parseDouble(moneys);
//				tv_money.setText((money / 100) + "元");
//			} catch (Exception e) {
//				// TODO: handle exception
//				tv_money.setText(moneys);
//			}
//
//		}else{
//			tv_money.setText("");
//		}
//		tv_title.setText("请输入支付密码");
//		// paypwd.setHint(content);
//		bt_affirm.setText(affirmStr);
//		bt_cancel.setText(cancelStr);
//		bt_affirm.setOnClickListener(this);
//		bt_cancel.setOnClickListener(this);
//
//	}
//	
//	public void setpaypwd(){
////		paypwd.setEnabled(true);
//		paypwd.requestFocus();
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//		paypwd.setText("");
//		paypwd1.setText("");
//		paypwd2.setText("");
//		paypwd3.setText("");
//		paypwd4.setText("");
//		paypwd5.setText("");
////		paypwd1.setEnabled(false);
////		paypwd2.setEnabled(false);
////		paypwd3.setEnabled(false);
////		paypwd4.setEnabled(false);
////		paypwd5.setEnabled(false);
//		
//		
//	}
//	
//
//	public String getpaypwd() {
////		String a = paypwd.getText().toString();
////		String b = paypwd1.getText().toString();
////		String c = paypwd2.getText().toString();
////		String d = paypwd3.getText().toString();
////		String e = paypwd4.getText().toString();
////		String f = paypwd5.getText().toString();
//
//		return a + b + c + d + e + f;
//
//	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		this.setContentView(R.layout.edit_dialog);
//
//		initview();
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		onMyDialogClickListener.onClick(v);
//	}
//
//	public class AsteriskPasswordTransformationMethod extends
//			PasswordTransformationMethod {
//		@Override
//		public CharSequence getTransformation(CharSequence source, View view) {
//			Log.e("", "mSource1111 =- = = " + source.toString());
//			return new PasswordCharSequence(source);
//		}
//
//		private class PasswordCharSequence implements CharSequence {
//			private CharSequence mSource;
//
//			public PasswordCharSequence(CharSequence source) {
//				mSource = source; // Store char sequence
//
//				Log.e("", "mSource =- = = " + source.toString());
//			}
//
//			public char charAt(int index) {
//				return '*'; // This is the important part
//			}
//
//			public int length() {
//				return mSource.length(); // Return default
//			}
//
//			public CharSequence subSequence(int start, int end) {
//				return mSource.subSequence(start, end); // Return default
//			}
//		}
//
//		@Override
//		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//				int arg3) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void afterTextChanged(Editable arg0) {
//			// TODO Auto-generated method stub
//		}
//
//	}
//	
//	
//	
//	@Override  
//    public boolean dispatchKeyEvent(KeyEvent event) {  
//		if(event.getKeyCode() == KeyEvent.KEYCODE_DEL && event.getAction() != KeyEvent.ACTION_UP){
//			Log.e("", "dsdsdsdsd");
//        	if(paypwd.isFocused()){
//        		paypwd.setText("");
//        	}else if(paypwd1.isFocused()){
////        		paypwd.setEnabled(true);
//        		paypwd1.clearFocus();
//        		paypwd.requestFocus();
////        		paypwd1.setEnabled(false);
//        		paypwd.setText("");
//        	}else if(paypwd2.isFocused()){
////        		paypwd1.setEnabled(true);
//        		paypwd2.clearFocus();
//        		paypwd1.requestFocus();
////        		paypwd2.setEnabled(false);
//        		paypwd1.setText("");
//        	}
//        	else if(paypwd3.isFocused()){
////        		paypwd2.setEnabled(true);
//        		paypwd3.clearFocus();
//        		paypwd2.requestFocus();
////        		paypwd3.setEnabled(false);
//        		paypwd2.setText("");
//        	}
//        	else if(paypwd4.isFocused()){
////        		paypwd3.setEnabled(true);
//        		paypwd4.clearFocus();
//        		paypwd3.requestFocus();
////        		paypwd4.setEnabled(false);
//        		paypwd3.setText("");
//        	}
//        	else if(paypwd5.isFocused()){
//        		if(paypwd5.getText().toString()!=null&&!paypwd5.getText().toString().equals("")){
//        			paypwd5.setText("");
//        		}else{
////            		paypwd4.setEnabled(true);
//            		paypwd5.clearFocus();
//            		paypwd4.requestFocus();
////            		paypwd5.setEnabled(false);
//            		paypwd4.setText("");
//        		}
//
//        		
//        	}else{
////        		paypwd5.setText("");
////        		paypwd5.requestFocus();
//        	}
//            return false;  
//        }
//        return super.dispatchKeyEvent(event);  
//    } 
}
