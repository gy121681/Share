package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.td.qianhai.epay.oem.beans.PhotoBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter {
	
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mData;
	protected final int mItemLayoutId;

	public CommonAdapter(Context context, List<T> data, int itemLayoutId) {
		this.mContext = context;
		this.mData = data;
		this.mItemLayoutId = itemLayoutId;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mData==null? 0:mData.size();
	}

	@Override
	public T getItem(int position) {
		return mData==null? null:mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public List<T> getmData() {
//		Collections.reverse(mData);
		return mData;
	}
	

	public void setmData(List<T> data) {
		this.mData = data;
	}
	
	public void remove(T item) {
		if(mData != null) {
			mData.remove(item);
		}
	}
	
	public void insert(T item, int position) {
		if(mData != null) {
			mData.add(position, item);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder = getViewHodler(position, convertView, parent);
		conver(holder, getItem(position), position);
		return holder.getConvertView();
	}

	public abstract void conver(ViewHolder holder, T item, int position);
	
	private  ViewHolder getViewHodler(int position, View convertView, ViewGroup parent) {
		return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
	}
}
