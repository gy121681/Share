package com.td.qianhai.epay.oem.views;

import android.content.Context;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.R;

public class ToastCustom {
	private static Toast mToast = null;

	// private static Handler handler = new Handler(Looper.getMainLooper());
	//
	// private static Toast toast = null;
	//
	// private static Object synObj = new Object();

	/**
	 * 自定义的Toast
	 * 
	 * @param context
	 * @param title
	 * @param content
	 * @param time
	 */
	
	public static void getMyToast(Context context, String title,
			String content, int time) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.custom_toast, null);
		ImageView image = (ImageView) layout.findViewById(R.id.tvImageToast);
		image.setImageResource(R.drawable.redalert);
		TextView titleX = (TextView) layout.findViewById(R.id.tvTitleToast);
		titleX.setText(title);
		TextView contextX = (TextView) layout.findViewById(R.id.tvTextToast);
		contextX.setText(content);
		 mToast = new Toast(context);
		 mToast.setDuration(time);// 设置时间
		 mToast.setGravity(Gravity.CENTER, 0, 0); // 设置出现的位置
		 mToast.setView(layout);
		 mToast.show();
	}

	/**
	 * 自定义的Toast
	 * 
	 * @param context
	 * @param title
	 * @param content
	 * @param time
	 */
	public static void getMyToast(Context context, String title,
			SpannableString content, int time) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.custom_toast, null);
		ImageView image = (ImageView) layout.findViewById(R.id.tvImageToast);
		image.setImageResource(R.drawable.redalert);
		TextView titleX = (TextView) layout.findViewById(R.id.tvTitleToast);
		titleX.setText(title);
		TextView contextX = (TextView) layout.findViewById(R.id.tvTextToast);
		contextX.setText(content);
		mToast = new Toast(context);
		mToast.setDuration(time);// 设置时间
		mToast.setGravity(Gravity.CENTER, 0, 0); // 设置出现的位置
		mToast.setView(layout);
		mToast.show();
	}

	/**
	 * 自定义清除前Toast,自定义时间
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void showMessage(Context context, String text, int duration) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.custom_toast, null);
//		ImageView image = (ImageView) layout.findViewById(R.id.tvImageToast);
//		image.setImageResource(R.drawable.redalert);
//		TextView titleX = (TextView) layout.findViewById(R.id.tvTitleToast);
//		titleX.setText("");
		TextView contextX = (TextView) layout.findViewById(R.id.tvTextToast);
		contextX.setText(text);
		 mToast = new Toast(context);
//		 mToast.setDuration(500);// 设置时间
		 mToast.setGravity(Gravity.CENTER, 0, 0); // 设置出现的位置
		layout.setPadding(5, 0, 5, 5);
		 mToast.setView(layout);
		 mToast.show();
//		if (mToast == null) {
//			mToast = Toast.makeText(context, text, duration);
//		} else {
//			mToast.setText(text);
//			mToast.setDuration(duration);
//		}
//		mToast.show();
	}

	/**
	 * 自定义清除前Toast,短时间
	 * 
	 * @param context
	 * @param text
	 */
	public static void showMessage(Context context, String text) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.custom_toast, null);
//		ImageView image = (ImageView) layout.findViewById(R.id.tvImageToast);
//		image.setImageResource(R.drawable.redalert);
		TextView titleX = (TextView) layout.findViewById(R.id.tvTitleToast);
		titleX.setText("");
		TextView contextX = (TextView) layout.findViewById(R.id.tvTextToast);
		contextX.setText(text);
		 mToast = new Toast(context);
		 layout.setPadding(5, 0, 5, 5);
		 mToast.setDuration(500);// 设置时间
		 mToast.setGravity(Gravity.CENTER, 0, 0); // 设置出现的位置
		 mToast.setView(layout);
		 mToast.show();
//		if (mToast == null) {
//			mToast = Toast.makeText(context, text, 0);
//		} else {
//			mToast.setText(text);
//			mToast.setDuration(0);
//		}

//		mToast.show();
	}

}
