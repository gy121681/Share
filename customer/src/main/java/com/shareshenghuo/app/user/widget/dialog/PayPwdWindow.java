package com.shareshenghuo.app.user.widget.dialog;

import android.content.Context;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.util.T;

public class PayPwdWindow extends CommonDialog implements OnClickListener {
	
	private TextView[] tvPwd;
	private TextView[] tvNum;
	
	public String tips;
	private int index;
	
	private PayPwdCallback callback;

	public PayPwdWindow(Context context) {
		super(context, R.layout.dlg_pay_pwd, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	@Override
	public void initDlgView() {
		index = 0;
		
		setText(R.id.tvPayPwdTips, tips);
		
		tvPwd = new TextView[6];
		tvPwd[0] = getView(R.id.tvPayPwd1);
		tvPwd[1] = getView(R.id.tvPayPwd2);
		tvPwd[2] = getView(R.id.tvPayPwd3);
		tvPwd[3] = getView(R.id.tvPayPwd4);
		tvPwd[4] = getView(R.id.tvPayPwd5);
		tvPwd[5] = getView(R.id.tvPayPwd6);
		
		tvNum = new TextView[10];
		tvNum[0] = getView(R.id.tvNumKey0);
		tvNum[1] = getView(R.id.tvNumKey1);
		tvNum[2] = getView(R.id.tvNumKey2);
		tvNum[3] = getView(R.id.tvNumKey3);
		tvNum[4] = getView(R.id.tvNumKey4);
		tvNum[5] = getView(R.id.tvNumKey5);
		tvNum[6] = getView(R.id.tvNumKey6);
		tvNum[7] = getView(R.id.tvNumKey7);
		tvNum[8] = getView(R.id.tvNumKey8);
		tvNum[9] = getView(R.id.tvNumKey9);
		for(int i=0; i<6; i++)
			tvPwd[i].setTransformationMethod(new AsteriskPasswordTransformationMethod());
		
		for(int i=0; i<10; i++)
			tvNum[i].setOnClickListener(new OnNumKeyClickListener(i));
		
		getView(R.id.tvNumKeyCancel).setOnClickListener(this);
		getView(R.id.tvNumKeyDel).setOnClickListener(this);
		getView(R.id.btnPayPwdOK).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.tvNumKeyCancel:
			odismiss();
			break;
			
		case R.id.tvNumKeyDel:
			if(index > 0) {
				index--;
				tvPwd[index].setText("");
			}
			break;
			
		case R.id.btnPayPwdOK:
			StringBuilder sb = new StringBuilder();
			for (TextView tv : tvPwd)
				sb.append(tv.getText().toString());
			if(sb.length() < 6) {
				T.showShort(context, "密码不足6位");
				return;
			}
			
			if (callback != null)
				callback.inputPayPwd(sb.toString());
			odismiss();
			break;
		}
	}
	
	private class OnNumKeyClickListener implements OnClickListener {
		private int num;
		
		OnNumKeyClickListener(int num) {
			this.num = num;
		}

		@Override
		public void onClick(View arg0) {
			if(index>=0 && index<6) {
				tvPwd[index].setText(num+"");
				index++;
			}
		}
	}
	
	public void setPayPwdCallback(PayPwdCallback callback) {
		this.callback = callback;
	}
	
	public interface PayPwdCallback {
		public void inputPayPwd(String payPwd);
	}
	
	/** 
     * 更改密码默认替代字符,系统默认的字符太小了 
    */  
   class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {  
 
       @Override  
       public CharSequence getTransformation(CharSequence source, View view) {  
           return new PasswordCharSequence(source);  
       }   
       private class PasswordCharSequence implements CharSequence {  
           private CharSequence mSource;    
           public PasswordCharSequence(CharSequence source) {    
               mSource = source;    
           }    
           @Override  
           public int length() {  
               return mSource.length();  
           }  
 
           @Override  
           public char charAt(int index) {  
               return '●';  
           }  
 
           @Override  
           public CharSequence subSequence(int start, int end) {  
               return mSource.subSequence(start, end);  
           }  
       }  
    }

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		
	}

}
