package com.shareshenghuo.app.shop.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.ImageLoadManager;

public abstract class BaseFragment extends Fragment {
	
	protected FragmentActivity activity;
	protected View rootView;
	protected LinearLayout llTopBack;
	protected RadioButton btn1,btn2;

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
	
	protected void initTopBar(String title,String title1) {
		
        
	llTopBack = (LinearLayout) rootView.findViewById(R.id.llTopBack);
	btn1 = (RadioButton)rootView. findViewById(R.id.btn_1);
	btn2 = (RadioButton) rootView.findViewById(R.id.btn_2);
	btn1.setText(title);
	btn2.setText(title1);
	llTopBack.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			getActivity().finish();
		}
	});
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
