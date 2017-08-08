package com.shareshenghuo.app.shop;



import com.shareshenghuo.app.shop.widget.CircleBar;
import com.shareshenghuo.app.shop.widget.CircleProgressView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MentionAmountActivity extends BaseTopActivity{
	
	
//	private CircleProgressView circleProgressbar;
	private  int mCurPercent;
	private int mPercent;
	private TextView tv_ok;
	private Handler handler=null;  
	private CircleBar progress;
	private TextView lastTime,lastTag;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.mention_amount_activity);
		handler = new Handler();
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("我的额度");
        progress = (CircleBar) findViewById(R.id.circleProgressbar);
        lastTime = (TextView) findViewById(R.id.day);
        lastTag = (TextView) findViewById(R.id.tag);
//		circleProgressbar = (CircleProgressView) findViewById(R.id.circleProgressbar);
		tv_ok = getView(R.id.tv_ok);
		
		tv_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				setPercent(0);
				progress.update(9000,10000,3000,lastTime,lastTag);
			}
		});
	}
	
	
	
	
	
	
	// 构建Runnable对象，在runnable中更新界面  
    Runnable   runnableUi=new  Runnable(){  
        @Override  
        public void run() {  
            //更新界面  
        }  
          
    };  
	
	public void setPercent(int percent) {
        new Thread(){  
            public void run(){    
				while(mCurPercent <= 99){
					mCurPercent += 1;
					handler.post(runnableUi);   
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
            }                     
        }.start();   
	}

}
