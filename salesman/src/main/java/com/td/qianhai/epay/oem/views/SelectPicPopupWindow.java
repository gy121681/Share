package com.td.qianhai.epay.oem.views;

import com.td.qianhai.epay.oem.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;


public class SelectPicPopupWindow  extends PopupWindow {  
  
  
    private Button btn_take_photo, btn_pick_photo, btn_cancel;  
    private View mMenuView;  
    private TextView tv1;
    
  
    public SelectPicPopupWindow(final Activity context,OnClickListener itemsOnClick) {  
        super(context);  
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        mMenuView = inflater.inflate(R.layout.bottom_frame, null);  
        btn_take_photo = (Button) mMenuView.findViewById(R.id.btn_take_photo);  
        btn_pick_photo = (Button) mMenuView.findViewById(R.id.btn_pick_photo);  
        tv1 = (TextView) mMenuView.findViewById(R.id.tv1);
        btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);  
        //取消按钮  
        btn_cancel.setOnClickListener(new OnClickListener() {  
  
            public void onClick(View v) {  
                //销毁弹出框  
                dismiss();  
            }  
        });  
        //设置按钮监听  
        btn_pick_photo.setOnClickListener(itemsOnClick);  
        btn_take_photo.setOnClickListener(itemsOnClick);  
        //设置SelectPicPopupWindow的View  
        this.setContentView(mMenuView);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.MATCH_PARENT);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.MATCH_PARENT);  
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.PopupAnimation);  
        //实例化一个ColorDrawable颜色为半透明  
//        ColorDrawable dw = new ColorDrawable(00000000);  
        //设置SelectPicPopupWindow弹出窗体的背景  
//        this.setBackgroundDrawable(dw);  
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
        mMenuView.setOnTouchListener(new OnTouchListener() {  
              
            public boolean onTouch(View v, MotionEvent event) {  
                  
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();  
                int y=(int) event.getY();  
                if(event.getAction()==MotionEvent.ACTION_UP){  
                    if(y<height){  
                        dismiss();  
                    }  
                }                 
                return true;  
            }  
        });  
        
        mMenuView.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				 if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
                     WindowManager.LayoutParams params=context.getWindow().getAttributes();  
                     params.alpha=1f;  
                     context.getWindow().setAttributes(params); 
	                 //do something...
	              }
				return false;
			}
		});
        

  
    }  
    public void setbutton(){
    	btn_pick_photo.setEnabled(false);
    }
    public void setbutton1(String a){
    	btn_take_photo.setText(a);
  }
    public void setbutton2(String a){
    	btn_pick_photo.setText(a);
  }
    public void eidt(String a){
    	btn_pick_photo.setText(a);
    	btn_pick_photo.setVisibility(View.GONE);
  }
    public void settitile(){
    	tv1.setVisibility(View.VISIBLE);
    }
}
