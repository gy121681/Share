package com.shareshenghuo.app.user.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.shareshenghuo.app.user.R;


public class MorePopWindow extends PopupWindow {
	private View conentView;
	private Activity context;
	private PopupWindow pwMyPopWindow;
	
	public MorePopWindow(final Activity context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.popo_view, null);
		
		 pwMyPopWindow = new PopupWindow(conentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		
		 
		
//		int h = context.getWindowManager().getDefaultDisplay().getHeight();
//		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		
		// 设置SelectPicPopupWindow的View
//		this.setContentView(conentView);
		
		// 设置SelectPicPopupWindow弹出窗体的宽
//		this.setWidth(LayoutParams.MATCH_PARENT);
//		// 设置SelectPicPopupWindow弹出窗体的高
//		this.setHeight(LayoutParams.WRAP_CONTENT);
		
		
		// 设置SelectPicPopupWindow弹出窗体可点击
		 pwMyPopWindow.setFocusable(true);
		 pwMyPopWindow.setOutsideTouchable(true);
		 
		 //------
		 pwMyPopWindow.setTouchable(true);
		 pwMyPopWindow.setBackgroundDrawable(new BitmapDrawable());
		
		// 刷新状态
//		this.update();
		
		// 实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0000000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
//		this.setBackgroundDrawable(dw);
		// mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		// // 设置SelectPicPopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.AnimationPreview);

		backgroundAlpha(0.6f);

		pwMyPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				// popupwindow消失的时候恢复成原来的透明度
				backgroundAlpha(1f);
			}
		});
		
//		pwMyPopWindow.showAsDropDown(anchor);
	}

	
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = context.getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		context.getWindow().setAttributes(lp);
	}

	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			pwMyPopWindow.showAsDropDown(parent);
			// this.showAsDropDown(parent, parent.getLayoutParams().width / 2,
			// 18);
		} else {
			pwMyPopWindow.dismiss();
		}
	}
}