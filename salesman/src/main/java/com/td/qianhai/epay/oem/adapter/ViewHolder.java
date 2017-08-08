package com.td.qianhai.epay.oem.adapter;

import com.td.qianhai.epay.oem.mail.utils.ImageLoadManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ViewHolder {
	
	private Context mContext;
	private final SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	
	private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		this.mContext = context;
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}

	/**
	 * 获得一个ViewHolder对象
	 */
	public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
		return convertView==null? new ViewHolder(context, parent, layoutId, position) : (ViewHolder)convertView.getTag();
	}
	
	public View getConvertView() {
		return mConvertView;
	}
	
	/**
	 * 通过控件ID获取对应控件
	 */
	public <T extends View>T getView(int viewId) {
		View view  = mViews.get(viewId);
		if(view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T)view;
	}
	
	public ViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}
	
	public ViewHolder setImageResource(int viewId, int drawableId) {
		ImageView iv = getView(viewId);
		iv.setImageResource(drawableId);
		return this;
	}
	
	public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView iv = getView(viewId);
		iv.setImageBitmap(bm);
		return this;
	}
	
	public ViewHolder setImageByURL(int viewId, final String url) {
		ImageView iv = getView(viewId);
		ImageLoadManager.getInstance(mContext).displayImage(url, iv);
		return this;
	}
}
