package com.td.qianhai.epay.oem.views;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.td.qianhai.epay.oem.R;

/**
 * Created by Snow on 2017/7/17.
 */

public class SearchDateView extends FrameLayout {
    public SearchDateView(@NonNull Context context) {
        this(context, null);
    }

    public SearchDateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchDateView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        View.inflate(getContext(), R.layout.lucre_search_view, this);
    }

}
