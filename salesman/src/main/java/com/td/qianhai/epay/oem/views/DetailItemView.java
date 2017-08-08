package com.td.qianhai.epay.oem.views;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;

/**
 * Created by Snow on 2017/7/17.
 */

public class DetailItemView extends FrameLayout {
    private String mLabel;
    private String mValue;

    public DetailItemView(@NonNull Context context, String label, String value) {
        this(context);
        this.mLabel = label;
        this.mValue = value;
        showInfo();
    }

    public DetailItemView(@NonNull Context context) {
        this(context, null);
    }

    public DetailItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        View.inflate(getContext(), R.layout.exchange_history_detail_item, this);
    }

    private void showInfo(){
        TextView tvLabel = (TextView) findViewById(R.id.tv_label);
        tvLabel.setText(mLabel);
        TextView tvValue = (TextView) findViewById(R.id.tv_value);
        tvValue.setText(mValue);
    }
}
