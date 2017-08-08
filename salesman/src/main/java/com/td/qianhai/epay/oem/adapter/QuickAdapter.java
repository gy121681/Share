package com.td.qianhai.epay.oem.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snow on 2017/7/15.
 */

public abstract class QuickAdapter<T> extends BaseAdapter {

    private OnItemClickListener<T> mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.mItemClickListener = listener;
    }

    public OnItemClickListener<T> getItemClickListener() {
        return mItemClickListener;
    }

    public interface OnItemClickListener<D> {
        void onItemClick(D data);
    }

    private List<T> mDatas = new ArrayList<>();

    public void setDatas(List<T> datas) {
        mDatas.clear();
        addDatas(datas);
        notifyDataSetChanged();
    }

    public void addDatas(List<T> datas) {
        if (datas != null && !datas.isEmpty()) {
            mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public List<T> getDatas(){
        return new ArrayList<>(mDatas);
    }

    public void removeData(T data) {
        mDatas.remove(data);
        notifyDataSetChanged();
    }

    public void removeData(int pos) {
        mDatas.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        int layoutId = getLayoutId(type);
        QuickAdapterHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), layoutId, null);
            holder = new QuickAdapterHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (QuickAdapterHolder) convertView.getTag();
        }
        fillView(holder, position, getItem(position));
        return convertView;
    }

    protected abstract void fillView(QuickAdapterHolder holder, int pos, T data);

    protected abstract @LayoutRes int getLayoutId(int type);
}
