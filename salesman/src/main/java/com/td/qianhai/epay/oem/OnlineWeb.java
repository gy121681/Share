package com.td.qianhai.epay.oem;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.mpay.utils.DateUtil;

public class OnlineWeb extends BaseActivity {

	private WebView wb_epos;
	private Intent intent;
	private Bundle bundle;
	private String urlStr,title,tag;
	private LinearLayout ll_web_loading;
	private RelativeLayout re;

	private void initView() {
		wb_epos = (WebView) findViewById(R.id.wv_epos);
		ll_web_loading = (LinearLayout) findViewById(R.id.ll_web_loading);
		((TextView) findViewById(R.id.tv_title_contre)).setText(title);
		((TextView) findViewById(R.id.bt_title_left))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (DateUtil.isFastDoubleClick()) {
							return;
						} else {
							finish();
						}
					}
				});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.web);
		intent = getIntent();
		urlStr = intent.getStringExtra("urlStr");
		title = intent.getStringExtra("titleStr");
		tag = intent.getStringExtra("urlStr");
		Log.e("", ""+urlStr);
		initView();
		wb_epos.getSettings().setJavaScriptEnabled(true);
		wb_epos.getSettings().setSupportMultipleWindows(true);
		wb_epos.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		wb_epos.loadUrl(urlStr);
		// 监听有需要再修改
		OnlineWebViewClient viewClient = new OnlineWebViewClient();
		wb_epos.setWebViewClient(viewClient);
		
		
		
//		WebViewClient  mWebviewclient = new WebViewClient(){  
//		     public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){  
//		          handler.proceed();  
//		     }  
//		};  
//		wb_epos.setWebViewClient(mWebviewclient);  
	}


	public class OnlineWebViewClient extends WebViewClient {
//		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
//
//		@Override
//		public void doUpdateVisitedHistory(WebView view, String url,
//				boolean isReload) {
//			// TODO Auto-generated method stub
//			super.doUpdateVisitedHistory(view, url, isReload);
//		}
//
//		@Override
//		public void onFormResubmission(WebView view, Message dontResend,
//				Message resend) {
//			// TODO Auto-generated method stub
//			super.onFormResubmission(view, dontResend, resend);
//		}
//
//		@Override
//		public void onLoadResource(WebView view, String url) {
//			// TODO Auto-generated method stub
//			super.onLoadResource(view, url);
//		}
//
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			ll_web_loading.setVisibility(View.GONE);
		}
//
//		@Override
//		public void onPageStarted(WebView view, String url, Bitmap favicon) {
//			// TODO Auto-generated method stub
//			super.onPageStarted(view, url, favicon);
//		}
//
//		@Override
//		public void onReceivedError(WebView view, int errorCode,
//				String description, String failingUrl) {
//			// TODO Auto-generated method stub
//			super.onReceivedError(view, errorCode, description, failingUrl);
//		}
//
//		@Override
//		public void onReceivedHttpAuthRequest(WebView view,
//				HttpAuthHandler handler, String host, String realm) {
//			// TODO Auto-generated method stub
//			super.onReceivedHttpAuthRequest(view, handler, host, realm);
//		}
//
		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			// TODO Auto-generated method stub
			  handler.proceed();  
		}
//
//		@Override
//		public void onScaleChanged(WebView view, float oldScale, float newScale) {
//			// TODO Auto-generated method stub
//			super.onScaleChanged(view, oldScale, newScale);
//		}
//
//		@Override
//		public void onTooManyRedirects(WebView view, Message cancelMsg,
//				Message continueMsg) {
//			// TODO Auto-generated method stub
//			super.onTooManyRedirects(view, cancelMsg, continueMsg);
//		}
//
//		@Override
//		public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
//			// TODO Auto-generated method stub
//			super.onUnhandledKeyEvent(view, event);
//		}
//
//		@Override
//		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
//			// TODO Auto-generated method stub
//			return super.shouldOverrideKeyEvent(view, event);
//		}
	}

}
