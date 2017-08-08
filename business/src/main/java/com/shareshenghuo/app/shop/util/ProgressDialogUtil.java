package com.shareshenghuo.app.shop.util;

import java.lang.reflect.Field;

import com.shareshenghuo.app.shop.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProgressDialogUtil {

	private static ProgressDialog progressDlg;
	
	private static   Dialog loadingDialog ;
	private static   Dialog loadingDialog1 ;
	
	public static void showProgressDlg(Context context, String message) {
		
	      LayoutInflater inflater = LayoutInflater.from(context);  
	        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view  
	        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局  
	        // main.xml中的ImageView  
	        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);  
	        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字  
	        // 加载动画  
	        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(  
	                context, R.anim.loading_animation);  
	        // 使用ImageView显示动画  
	        spaceshipImage.startAnimation(hyperspaceJumpAnimation);  
	        tipTextView.setText(message);// 设置加载信息  
	  
	        loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
	        
	  
//	        loadingDialog.setCancelable(true);// 不可以用“返回键”取消  
	        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(  
	                LinearLayout.LayoutParams.FILL_PARENT,  
	                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局  
		
	        loadingDialog.show();
		
//		progressDlg = new ProgressDialog(context);
////		message = TextUtils.isEmpty(message)? "请稍等" : message;
//		progressDlg.setMessage(message);
//		progressDlg.show();
	}
	
	public static void showProgressDlg1(Context context, String message) {
		
	      LayoutInflater inflater = LayoutInflater.from(context);  
	        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view  
	        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局  
	        // main.xml中的ImageView  
	        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);  
	        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字  
	        // 加载动画  
	        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(  
	                context, R.anim.loading_animation);  
	        // 使用ImageView显示动画  
	        spaceshipImage.startAnimation(hyperspaceJumpAnimation);  
	        tipTextView.setText(message);// 设置加载信息  
	  
	        loadingDialog1 = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
	        
	  
//	        loadingDialog.setCancelable(true);// 不可以用“返回键”取消  
	        loadingDialog1.setContentView(layout, new LinearLayout.LayoutParams(  
	                LinearLayout.LayoutParams.FILL_PARENT,  
	                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局  
		
	        loadingDialog1.show();
	        loadingDialog1.setCancelable(false);
//		progressDlg = new ProgressDialog(context);
////		message = TextUtils.isEmpty(message)? "请稍等" : message;
//		progressDlg.setMessage(message);
//		progressDlg.show();
	}
	
	public static void setCancelable(boolean flag) {
		if(loadingDialog != null)
			loadingDialog.setCancelable(flag);
//			loadingDialog.setCanceledOnTouchOutside(flag);
	}
	
	public static void dismissProgressDlg() {
		if(loadingDialog != null)
			loadingDialog.dismiss();
	}
	
	public static void dismissProgressDlg1() {
		if(loadingDialog1 != null)
			loadingDialog1.dismiss();
	}
	
	public static void setDialogCloseAbility(boolean isCloseAble) {
	        try {

	            Field field = loadingDialog.getClass().getSuperclass()
	                    .getDeclaredField("mShowing");

	            field.setAccessible(true);

	            field.set(loadingDialog, isCloseAble);
	            loadingDialog.setCancelable(isCloseAble);
	        } catch (Exception e) {

	            e.printStackTrace();

	        }
	    }
}
