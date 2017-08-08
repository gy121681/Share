package com.td.qianhai.epay.oem.adapter;

import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.View;

import com.td.qianhai.epay.oem.R;

/**
 * Created by Snow on 2017/7/15.
 */

public class QuickAdapterHolder {
    private View mItemView;
    private SparseArray<View> mViewCaches;

    public QuickAdapterHolder(View itemView) {
        if (itemView == null) {
            throw new IllegalStateException("itemView must not be null！");
        }
        this.mItemView = itemView;
        mViewCaches = new SparseArray<>();
    }

    public View getItemView(){
        return mItemView;
    }

    /**
     *
     * @param id
     * @param <V>
     * @return 未找到返回null
     */
    public <V extends View> V findViewById(@IdRes int id) {
        V result = null;
        View cacheView = mViewCaches.get(id);
        if (cacheView != null) {
            result = (V) cacheView;
        } else {
            cacheView = mItemView.findViewById(id);
            if (cacheView != null) {
                result = (V) cacheView;
                mViewCaches.put(id, result);
            }
        }
        return result;
    }

}
