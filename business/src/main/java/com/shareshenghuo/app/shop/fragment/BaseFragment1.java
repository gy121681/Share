package com.shareshenghuo.app.shop.fragment;

import com.shareshenghuo.app.shop.BaseTopActivity;
import com.shareshenghuo.app.shop.manager.ImageLoadManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseFragment1 extends  Fragment {
	
	protected FragmentActivity activity;
	protected View rootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = getActivity();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(rootView == null){
			rootView = inflater.inflate(getLayoutId(), container, false);
			init(rootView);
		}else{
			init(rootView);
		}
		
        ViewGroup parent = (ViewGroup) rootView.getParent();  
        if (parent != null) {  
            parent.removeView(rootView);  
        }   
		
		return rootView;
	}
	
	protected abstract int getLayoutId();
	
	protected abstract void init(View rootView);
	
	public void setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
	}

	public void setText(int viewId, int textId) {
		TextView tv = getView(viewId);
		tv.setText(textId);
	}

	public void setImageResource(int viewId, int drawableId) {
		ImageView iv = getView(viewId);
		iv.setImageResource(drawableId);
	}

	public void setImageBitmap(int viewId, Bitmap bm) {
		ImageView iv = getView(viewId);
		iv.setImageBitmap(bm);
	}

	public void setImageByURL(int viewId, final String url) {
		if(getActivity()==null || TextUtils.isEmpty(url))
			return;
		
		ImageView iv = getView(viewId);
		ImageLoadManager.getInstance(getActivity()).displayImage(url, iv);
	}

	public <T extends View>T getView(int viewId) {
		View view  = rootView.findViewById(viewId);
		return (T)view;
	}
}
