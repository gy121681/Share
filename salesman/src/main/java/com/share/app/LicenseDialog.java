package com.share.app;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.td.qianhai.epay.oem.R;

/**
 * Created by Snow on 2017/7/29.
 */

public class LicenseDialog extends DialogFragment {

    private View mView;
    private WebView mWebView;

    private String mUrl;

    private LicenseAgreeListener mListener;

    public interface LicenseAgreeListener{
        void agreeLicense();
    }

    public void setListener(LicenseAgreeListener l) {
        mListener = l;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialog_license, container, false);
        initView(mView);
        return mView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.LicenseDialogStyle);
        return super.onCreateDialog(savedInstanceState);
    }

    private void initView(View view) {
        mWebView = (WebView) view.findViewById(R.id.webView);
        mWebView.loadUrl(mUrl);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.btn_agree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null) {
                    mListener.agreeLicense();
                }
            }
        });
    }
}
