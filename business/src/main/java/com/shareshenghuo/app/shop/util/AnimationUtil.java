package com.shareshenghuo.app.shop.util;


import com.shareshenghuo.app.shop.R;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;


public class AnimationUtil {

	public static void ScaleAnimations(View v) {

		final ScaleAnimation anima = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		anima.setDuration(1500);
		anima.setFillAfter(true);

		v.setAnimation(anima);
		anima.startNow();

	}
	
//	public static void leftToRightAnmation(Context context, View view){
//		
//		Animation animationtop = AnimationUtils.loadAnimation(context, R.anim.activity_left_rignt_anim);
//		
//		view.startAnimation(animationtop);
//		
//	}
	
	public static void TopTranslateAnimation(Context context ,View v){
		
		Animation a = AnimationUtils.loadAnimation(context, R.anim.activity_top_buttom_anim);
		v.startAnimation(a);
	}
	
//	public static void ButTranslateAnimation(Context context ,View v){
//		
//		Animation a = AnimationUtils.loadAnimation(context, R.anim.activity_goout_top_anim);
//		v.startAnimation(a);
//	}
	
//	public static void BtnSpecialAnmations(Context context,View view){
//		Animation shake = AnimationUtils.loadAnimation(context, R.anim.btn_special_effects);
//		shake.reset();
//		shake.setFillAfter(true);
//		view.startAnimation(shake);
//	}
	
	public static void GoTop(Context context ,View v){
		Animation a = AnimationUtils.loadAnimation(context, R.anim.push_bottom_in);
		v.startAnimation(a);
	}
	
	public static void GoButton(Context context ,View v){
		Animation a = AnimationUtils.loadAnimation(context, R.anim.push_bottom_out);
		v.startAnimation(a);
	}
	
	
	public static void Bankchoiceto(Context context ,View v){
		
		Animation a = AnimationUtils.loadAnimation(context, R.anim.anim_enter);
		v.startAnimation(a);
	}
	
	public static void Bankchoiceback(Context context ,View v){
		
		Animation a = AnimationUtils.loadAnimation(context, R.anim.anim_exit);
		v.startAnimation(a);
	}
	
	public static void RoteAnimation(Context context ,View v){
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate_anima); 
		 v.startAnimation(animation);  
	}
	
	
	@SuppressLint("NewApi") 
	public static void BtnSpecialAnmations1(Context context, View view,int time,int lasttimg) {

		int delta = 10;

		PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(
				View.TRANSLATION_X, Keyframe.ofFloat(0f, 0),
				Keyframe.ofFloat(.10f, -delta), Keyframe.ofFloat(.26f, delta),
				Keyframe.ofFloat(.42f, -delta), Keyframe.ofFloat(.58f, delta),
				Keyframe.ofFloat(.74f, -delta), Keyframe.ofFloat(.90f, delta),
				Keyframe.ofFloat(1f, 0f));

		final ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(
				view, pvhTranslateX).setDuration(time);
		animator.setRepeatCount(ValueAnimator.INFINITE);
		animator.start();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
//				if(animator.isStarted()){
//					animator.cancel();
//				}else{
//					animator.start();
//				}
//				animator.start();
			}
		}, 2000);



		Handler han = new Handler();
		
		han.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				animator.cancel();
			}
		}, lasttimg);

	}
	
	
	
	public static void TranslateAnima(Context context,View v){
		
		 TranslateAnimation animation = new TranslateAnimation(-20, 0, 0, 0);
	     animation.setInterpolator(new AccelerateInterpolator());
	     animation.setDuration(1000);
	     animation.setRepeatCount(2);
	     animation.setFillAfter(true);

	     animation.setAnimationListener(new Animation.AnimationListener() {
	         public void onAnimationStart(Animation animation) {}

	         public void onAnimationEnd(Animation animation) {
//	             btn.setTop(200);
	         }
	         public void onAnimationRepeat(Animation animation) {}
	     });
	    
	     v.startAnimation(animation);
		
	}
	
	public  static void JAnima(Context context,View v){
		Animation mAnimation = AnimationUtils.loadAnimation(context, R.anim.pro_anim); 
		mAnimation.setRepeatCount(2);
		mAnimation.setFillEnabled(true);
		mAnimation.setFillAfter(true);
		 v.startAnimation(mAnimation);
	}


}
