package com.share.app.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

/**
 * Created by Snow on 2017/7/26.
 */

public class WaringDialogUtils {

    private static OneButtonDialogWarn waringDialog;

    public static void showWaringDialog(Context context, String msg, @Nullable final OnMyDialogClickListener l) {
        waringDialog = new OneButtonDialogWarn(context,
                R.style.CustomDialog, "提示", msg, "确定",
                new OnMyDialogClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (l != null) {
                            l.onClick(v);
                        }
                        waringDialog.dismiss();
                    }
                });
        waringDialog.show();
    }

}
