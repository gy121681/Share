package com.td.qianhai.epay.oem;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.td.qianhai.epay.oem.beans.AppContext;


public class NewWebViewActivity extends BaseActivity{
	private String URLs;
	private String datas;  
	private WebView wb_epos;
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_activity);
		AppContext.getInstance().addActivity(this);
		Intent it = getIntent();
		URLs = it.getStringExtra("URLs");
		wb_epos = (WebView) findViewById(R.id.wv_epos);
		
//		wb_epos.getSettings().setJavaScriptEnabled(true);
//		wb_epos.getSettings().setSupportMultipleWindows(true);
//		wb_epos.getSettings().setSupportZoom(true);
//		wb_epos.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//		wb_epos.getSettings().setBuiltInZoomControls(true);// support zoom
//		wb_epos.getSettings().setUseWideViewPort(true);// 这个很关键
//		wb_epos.getSettings().setLoadWithOverviewMode(true);
//		
//		wb_epos.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//		wb_epos.getSettings().setJavaScriptEnabled(true);
//		wb_epos.getSettings().setSupportMultipleWindows(true);
//		wb_epos.loadUrl(URLs);
//		wb_epos.addJavascriptInterface(new JsInteration(), "GobackToHomepage");
		
		
		wb_epos = (WebView) findViewById(R.id.wv_epos);
		wb_epos.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		wb_epos.getSettings().setJavaScriptEnabled(true);
		wb_epos.getSettings().setSupportMultipleWindows(true);
		wb_epos.getSettings().setSupportZoom(true);
		wb_epos.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		wb_epos.getSettings().setBuiltInZoomControls(true);// support zoom
		wb_epos.getSettings().setUseWideViewPort(true);// 这个很关键
		wb_epos.getSettings().setLoadWithOverviewMode(true);

		wb_epos.addJavascriptInterface(new GobackToHomepage(), "GobackToHomepage");
		wb_epos.loadUrl(URLs);
		// 监听有需要再修改
		OnlineWebViewClient viewClient = new OnlineWebViewClient();
		wb_epos.setWebViewClient(viewClient);
//		wb_epos.addJavascriptInterface(this, "wst");
		
		
		
		
	}
	
	
	 public class GobackToHomepage {

	      @JavascriptInterface
	      public void GobackHome() {
	    	  
	    	  finish();
//	            Uri uri = Uri.parse("tel:" + phone);  
//	            Intent intent = new Intent(Intent.ACTION_DIAL, uri);  
//	            startActivity(intent);  
	    	  
	      }

//	      @JavascriptInterface
//	      public void share(String bb) {
//	    	  
//				UMSocialService  mController = UMServiceFactory.getUMSocialService("com.umeng.share");
//				mController.getConfig().setPlatforms(SHARE_MEDIA.QQ,
//						SHARE_MEDIA.WEIXIN,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN_CIRCLE);
//				mController = UMServiceFactory.getUMSocialService("com.umeng.share");
//				mController
//				.setShareContent("前海商品分享");
//				String appID = "wx00b3216e58e87b78";
//				String appSecret = "fd98183918ba0581c731a67c61a5f5ae";
//				
//				
//				UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(NewWebViewActivity.this,
//						"1105007624", "peu6n4ucUC0PLHCg");
//				qqSsoHandler.addToSocialSDK();
//				
//				// 添加微信平台
//				UMWXHandler wxHandler = new UMWXHandler(NewWebViewActivity.this, appID, appSecret);
//				// WeiXinShareContent weixinContent = new WeiXinShareContent();
//				// weixinContent.setTargetUrl("http://180.166.124.95:8092/posm/upload/QH_W_V1.2.apk");
//				wxHandler.addToSocialSDK();
//				
//				UMImage aa = new UMImage(NewWebViewActivity.this, R.drawable.ico);
//
//				WeiXinShareContent weixinContent = new WeiXinShareContent();
//				
//				UMWXHandler wxCircleHandler = new UMWXHandler(NewWebViewActivity.this, appID,
//						appSecret);
//				wxCircleHandler.setToCircle(true);
//				wxCircleHandler.addToSocialSDK();
//				// 设置分享文字
//				weixinContent.setShareContent("前海商品分享");
//				// 设置title
//				weixinContent.setTitle("前海商品分享");
//				// 设置分享内容跳转URL
//				weixinContent
//						.setTargetUrl(bb);
//				// 设置分享图片m
//				weixinContent.setShareImage(aa);
//				
//				QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(NewWebViewActivity.this,
//						"1105007624", "peu6n4ucUC0PLHCg");
//				qZoneSsoHandler.addToSocialSDK();
//				
//				QQShareContent qqShareContent = new QQShareContent();
//				// 设置分享文字
//				qqShareContent.setShareContent("前海商品分享");
//				// 设置分享title
//				qqShareContent.setTitle("前海商品分享");
//				// 设置分享图片
//				qqShareContent.setShareImage(aa);
//				// 设置点击分享内容的跳转链接
//				qqShareContent
//						.setTargetUrl(bb);
//				mController.setShareMedia(qqShareContent);
//
//				mController.setShareMedia(weixinContent);
//
//				mController.openShare(NewWebViewActivity.this, false);
//				
//	      }
	  }
	 
	 

	
	public class OnlineWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			view.addJavascriptInterface(new GobackToHomepage(), "GobackToHomepage");
			view.loadUrl(url);
//			view.addJavascriptInterface(new GobackToHomepage(), "GobackToHomepage");
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			// TODO Auto-generated method stub
			handler.proceed();
		}
		
	}

}
