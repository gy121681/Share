package com.share.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * Created by Snow on 2017/7/30.
 */

public class CusCheckBox extends CheckBox {
    public CusCheckBox(Context context) {
        super(context);
    }

    public CusCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CusCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void toggle() {

    }
}
