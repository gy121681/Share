package com.td.qianhai.epay.oem;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.interfaces.JavaScriptinterface;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;


public class WebViewActivity extends BaseActivity{
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
		wb_epos.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		wb_epos.getSettings().setJavaScriptEnabled(true);
		wb_epos.getSettings().setSupportMultipleWindows(true);
		wb_epos.loadUrl(URLs);
		// 监听有需要再修改
		OnlineWebViewClient viewClient = new OnlineWebViewClient();
		wb_epos.setWebViewClient(viewClient);
//		wb_epos.addJavascriptInterface(this, "wst");
		
		wb_epos.addJavascriptInterface(new JsInteration(), "wst");
		
	}
	
	
	 public class JsInteration {

	      @JavascriptInterface
	      public void call(String phone) {
	    	  Log.e("", "onSumResult result=" + phone);
	            Uri uri = Uri.parse("tel:" + phone);  
	            Intent intent = new Intent(Intent.ACTION_DIAL, uri);  
	            startActivity(intent);  
	    	  
	      }

	      @JavascriptInterface
	      public void share(String bb) {
	    	  
				UMSocialService  mController = UMServiceFactory.getUMSocialService("com.umeng.share");
				mController.getConfig().setPlatforms(SHARE_MEDIA.QQ,
						SHARE_MEDIA.WEIXIN,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN_CIRCLE);
				mController = UMServiceFactory.getUMSocialService("com.umeng.share");
				mController
				.setShareContent("前海商品分享");
				String appID = "wx00b3216e58e87b78";
				String appSecret = "fd98183918ba0581c731a67c61a5f5ae";
				
				
				UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(WebViewActivity.this,
						"1105007624", "peu6n4ucUC0PLHCg");
				qqSsoHandler.addToSocialSDK();
				
				// 添加微信平台
				UMWXHandler wxHandler = new UMWXHandler(WebViewActivity.this, appID, appSecret);
				// WeiXinShareContent weixinContent = new WeiXinShareContent();
				// weixinContent.setTargetUrl("http://180.166.124.95:8092/posm/upload/QH_W_V1.2.apk");
				wxHandler.addToSocialSDK();
				
				UMImage aa = new UMImage(WebViewActivity.this, R.drawable.ico);

				WeiXinShareContent weixinContent = new WeiXinShareContent();
				
				UMWXHandler wxCircleHandler = new UMWXHandler(WebViewActivity.this, appID,
						appSecret);
				wxCircleHandler.setToCircle(true);
				wxCircleHandler.addToSocialSDK();
				// 设置分享文字
				weixinContent.setShareContent("前海商品分享");
				// 设置title
				weixinContent.setTitle("前海商品分享");
				// 设置分享内容跳转URL
				weixinContent
						.setTargetUrl(bb);
				// 设置分享图片m
				weixinContent.setShareImage(aa);
				
				QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(WebViewActivity.this,
						"1105007624", "peu6n4ucUC0PLHCg");
				qZoneSsoHandler.addToSocialSDK();
				
				QQShareContent qqShareContent = new QQShareContent();
				// 设置分享文字
				qqShareContent.setShareContent("前海商品分享");
				// 设置分享title
				qqShareContent.setTitle("前海商品分享");
				// 设置分享图片
				qqShareContent.setShareImage(aa);
				// 设置点击分享内容的跳转链接
				qqShareContent
						.setTargetUrl(bb);
				mController.setShareMedia(qqShareContent);

				mController.setShareMedia(weixinContent);

				mController.openShare(WebViewActivity.this, false);
				
	      }
	  }

	
	public class OnlineWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
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
