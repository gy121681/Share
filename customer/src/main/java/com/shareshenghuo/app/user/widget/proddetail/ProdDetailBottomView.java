package com.shareshenghuo.app.user.widget.proddetail;

import android.content.Context;
import android.graphics.Bitmap;


import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout.LayoutParams;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.util.BitmapTool;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.snapscrollview.*;

public class ProdDetailBottomView implements McoySnapPageLayout.McoySnapPage {

	private Context context;

	private View rootView = null;
	private WebView webView;
	
	private String url;

	public ProdDetailBottomView(Context context,View rootView, String url) {
		this.context = context;
		this.rootView = rootView;
		this.url = url;
		initView();
	}

	@Override
	public View getRootView() {
		return rootView;
	}

	@Override
	public boolean isAtTop() {
		return webView.getScrollY()<=0;
	}

	@Override
	public boolean isAtBottom() {
		return false;
	}
	
	public void initView() {
		webView = (WebView) rootView.findViewById(R.id.webView);
        webView.getSettings().setUserAgentString("android_citylife");
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
        webView.getSettings().setJavaScriptEnabled(true);
        // 保证可滑动
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = context.getApplicationContext().getCacheDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(appCachePath);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);// 支持缩放
        webView.getSettings().setLoadsImagesAutomatically(true);
        // 启用数据库
        webView.getSettings().setDatabaseEnabled(true);
        // 启用地理定位
        webView.getSettings().setGeolocationEnabled(true);
        // 设置定位的数据库路径
        webView.getSettings().setDomStorageEnabled(true);
        webView.requestFocus();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, BitmapTool.getScreenHeightPX(context)));
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                    T.showShort(context, "加载失败，请稍候再试");
            }
        });
        // 各种内容的渲染需要使用webviewChromClient去实现
        webView.setWebChromeClient(new WebChromeClient());
        
        webView.loadUrl(url);
	}
}
