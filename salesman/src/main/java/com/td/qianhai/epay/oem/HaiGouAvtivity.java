package com.td.qianhai.epay.oem;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.td.qianhai.epay.oem.WebViewActivity.JsInteration;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.interfaces.JavaScriptinterface;
import com.td.qianhai.epay.oem.mail.utils.DESKey;
import com.td.qianhai.epay.oem.mail.utils.FileUtils;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("JavascriptInterface") public class HaiGouAvtivity extends BaseActivity {

	private WebView wb_epos;
	private RelativeLayout ll_web_loading;
	private String pwd = "", mobile, username = "";
	private OneButtonDialogWarn warnDialog;
	private ValueCallback<Uri> mUploadMessage;
	private String URLs,title = "";
	private String lognum = "",oemid = "";

	@Override
	@SuppressLint({ "NewApi", "JavascriptInterface", "SetJavaScriptEnabled" })
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.activity_haigou_layout);
		Intent it = getIntent();
		oemid = MyCacheUtil.getshared(this).getString("OEMID", "");
		URLs = it.getStringExtra("url");
		String tags = it.getStringExtra("tag");
		pwd = MyCacheUtil.getshared(this).getString("pwd", "");
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		title = it.getStringExtra("title");
		// lognum = MyCacheUtil.getshared(this).getString("LOGNUM", "");
		init();
		
		if (tags.equals("0")) {
			initview();
		} else {
			webview();
		}
		
	}
	
	

	private void init() {
		((TextView) findViewById(R.id.tv_title_contre)).setText(title);
		// TODO Auto-generated method stub
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		
		
//		String aa = null;
//		try {
//			aa = DESKey.AES_Encode("123","erMa@)!^!QAZ0okm");
//			aa = URLEncoder.encode(aa);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	

	private void initview() {
		// TODO Auto-generated method stub
		GetWalletInfohai walletinfo = new GetWalletInfohai();

		walletinfo.execute(HttpUrls.RICH_TREASURE_INFO + "", mobile);

	}

	private void webview() {
		ll_web_loading = (RelativeLayout) findViewById(R.id.ll_web_loading);
		wb_epos = (WebView) findViewById(R.id.wv_epos);
		wb_epos.getSettings().setSupportMultipleWindows(true);
		wb_epos.getSettings().setJavaScriptEnabled(true);
		wb_epos.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

		wb_epos.getSettings().setSupportZoom(true);
		wb_epos.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		wb_epos.getSettings().setBuiltInZoomControls(true);//support zoom
		wb_epos.getSettings().setUseWideViewPort(true);// 这个很关键
		wb_epos.getSettings().setLoadWithOverviewMode(true);
		wb_epos.addJavascriptInterface(new JsInteration(), "wst");
		wb_epos.loadUrl(URLs);
		// 监听有需要再修改
		OnlineWebViewClient viewClient = new OnlineWebViewClient();
		wb_epos.setWebViewClient(viewClient);
		wb_epos.setWebChromeClient(new MyWebChromeClient());
		
	}
	

	
	private void inithtml() {
		// TODO Auto-generated method stub

		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("PHONENUMBER", mobile);
			jsonObj.put("PASSWORD", pwd);
			jsonObj.put("USERNAME", username);
			jsonObj.put("ISSETPAYPWD", lognum);
			jsonObj.put("OEMID", oemid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String aa = null;
		try {
			aa = DESKey.AES_Encode(jsonObj.toString(),
					"f15f1ede25a2471998ee06edba7d2e29");
			aa = URLEncoder.encode(aa);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ll_web_loading = (RelativeLayout) findViewById(R.id.ll_web_loading);
		wb_epos = (WebView) findViewById(R.id.wv_epos);
		wb_epos.getSettings().setJavaScriptEnabled(true);
		wb_epos.getSettings().setSupportMultipleWindows(true);
		wb_epos.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		wb_epos.getSettings().setSupportZoom(true);
		wb_epos.getSettings().setBlockNetworkImage(true);
		wb_epos.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		wb_epos.getSettings().setBuiltInZoomControls(true);//support zoom
		wb_epos.getSettings().setUseWideViewPort(true);// 这个很关键
		wb_epos.getSettings().setLoadWithOverviewMode(true);
//		if (Build.VERSION.SDK_INT >= 19) {
//			wb_epos.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//	    }
		
		String url = URLs + aa;
		wb_epos.loadUrl(url);
		OnlineWebViewClient viewClient = new OnlineWebViewClient();
		wb_epos.setWebViewClient(viewClient);
		// 监听有需要再修改
		wb_epos.setWebChromeClient(new MyWebChromeClient());

//		wb_epos.setWebChromeClient(new WebChromeClient() {
//
//	        public void openFileChooser(ValueCallback<Uri> uploadMsg) {    
//	        	  
//	            mUploadMessage = uploadMsg;    
//	            Intent i = new Intent(Intent.ACTION_GET_CONTENT);    
//	            i.addCategory(Intent.CATEGORY_OPENABLE);    
//	            i.setType("image/*");    
//	            HaiGouAvtivity.this.startActivityForResult(Intent.createChooser(i,"File Chooser"), 1);    
//	  
//	           }  
//	  
//	        // For Android 3.0+  
//	           public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {  
//	           mUploadMessage = uploadMsg;  
//	           Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
//	           i.addCategory(Intent.CATEGORY_OPENABLE);  
//	           i.setType("*/*");  
//	           HaiGouAvtivity.this.startActivityForResult(  
//	           Intent.createChooser(i, "File Browser"),  
//	           1);  
//	           }  
//	  
//	        //For Android 4.1  
//	           public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){  
//	               mUploadMessage = uploadMsg;    
//	               Intent i = new Intent(Intent.ACTION_GET_CONTENT);    
//	               i.addCategory(Intent.CATEGORY_OPENABLE);    
//	               i.setType("image/*");    
//	               HaiGouAvtivity.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), 1 );  
//	  
//	           }
//		});
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
				mController.getConfig().setPlatforms(
						SHARE_MEDIA.WEIXIN);
				mController = UMServiceFactory.getUMSocialService("com.umeng.share");
				mController
				.setShareContent("商品分享");
				String appID = "wx67b3a23ca6de80f8";
				String appSecret = "1a35ae5ac3d57848c743a5655d11cb4b";
				
				
				UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(HaiGouAvtivity.this,
						"1105007624", "peu6n4ucUC0PLHCg");
				qqSsoHandler.addToSocialSDK();
				
				// 添加微信平台
				UMWXHandler wxHandler = new UMWXHandler(HaiGouAvtivity.this, appID, appSecret);
				// WeiXinShareContent weixinContent = new WeiXinShareContent();
				// weixinContent.setTargetUrl("http://180.166.124.95:8092/posm/upload/QH_W_V1.2.apk");
				wxHandler.addToSocialSDK();
				
				UMImage aa = new UMImage(HaiGouAvtivity.this, R.drawable.ico);

				WeiXinShareContent weixinContent = new WeiXinShareContent();
				
				UMWXHandler wxCircleHandler = new UMWXHandler(HaiGouAvtivity.this, appID,
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
				
				QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(HaiGouAvtivity.this,
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

				mController.openShare(HaiGouAvtivity.this, false);
				
	      }
	 }
	
	public static final int FILECHOOSER_RESULTCODE = 1;
	private static final int REQ_CAMERA = FILECHOOSER_RESULTCODE+1;
	private static final int REQ_CHOOSE = REQ_CAMERA+1;

	private class MyWebChromeClient extends WebChromeClient {

		// For Android 3.0+
		   public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {  
               if (mUploadMessage != null) return;
               mUploadMessage = uploadMsg;   
               selectImage();
//               Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//               i.addCategory(Intent.CATEGORY_OPENABLE);
//               i.setType("*/*");
//                   startActivityForResult( Intent.createChooser( i, "File Chooser" ), FILECHOOSER_RESULTCODE );
           }
            // For Android < 3.0
	        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
	               openFileChooser( uploadMsg, "" );
	        }
	        // For Android  > 4.1.1
	      public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
	              openFileChooser(uploadMsg, acceptType);
	      }

	}
	
	/**
	 * 检查SD卡是否存在
	 * 
	 * @return
	 */
	public final boolean checkSDcard() {
		boolean flag = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (!flag) {
			Toast.makeText(this, "请插入手机存储卡再使用本功能",Toast.LENGTH_SHORT).show();
		}
		return flag;
	}
	String compressPath = "";
	
	protected final void selectImage() {
		if (!checkSDcard())
			return;
		String[] selectPicTypeStr = { "拍照","相册" };
		new AlertDialog.Builder(this)
				.setItems(selectPicTypeStr,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								// 相机拍摄
								case 0:
									openCarcme();
									break;
								// 手机相册
								case 1:
									chosePic();
									break;
								default:
									break;
								}
								compressPath = Environment
										.getExternalStorageDirectory()
										.getPath()
										+ "/fuiou_wmp/temp";
								new File(compressPath).mkdirs();
								compressPath = compressPath + File.separator
										+ "compress.jpg";
							}
						}).show();
	}
	
	String imagePaths;
	Uri  cameraUri;
	/**
	 * 打开照相机
	 */
	private void openCarcme() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		imagePaths = Environment.getExternalStorageDirectory().getPath()
				+ "/fuiou_wmp/temp/"
				+ (System.currentTimeMillis() + ".jpg");
		// 必须确保文件夹路径存在，否则拍照后无法完成回调
		File vFile = new File(imagePaths);
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		} else {
			if (vFile.exists()) {
				vFile.delete();
			}
		}
		cameraUri = Uri.fromFile(vFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		startActivityForResult(intent, REQ_CAMERA);
	}

	/**
	 * 拍照结束后
	 */
	private void afterOpenCamera() {
		File f = new File(imagePaths);
		addImageGallery(f);
//		File newFile = FileUtils.compressFile(f.getPath(), compressPath);
	}

	/** 解决拍照后在相册中找不到的问题 */
	private void addImageGallery(File file) {
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}

	/**
	 * 本地相册选择图片
	 */
	private void chosePic() {
		FileUtils.delFile(compressPath);
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
		String IMAGE_UNSPECIFIED = "image/*";
		innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
		Intent wrapperIntent = Intent.createChooser(innerIntent, null);
		startActivityForResult(wrapperIntent, REQ_CHOOSE);
	}

	/**
	 * 选择照片后结束
	 * 
	 * @param data
	 */
	private Uri afterChosePic(Intent data) {

		// 获取图片的路径：
		String[] proj = { MediaStore.Images.Media.DATA };
		// 好像是android多媒体数据库的封装接口，具体的看Android文档
		Cursor cursor = managedQuery(data.getData(), proj, null, null, null);
		if(cursor == null ){
			Toast.makeText(this, "上传的图片仅支持png或jpg格式",Toast.LENGTH_SHORT).show();
			return null;
		}
		// 按我个人理解 这个是获得用户选择的图片的索引值
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		// 将光标移至开头 ，这个很重要，不小心很容易引起越界
		cursor.moveToFirst();
		// 最后根据索引值获取图片路径
		String path = cursor.getString(column_index);
		if(path != null && (path.endsWith(".png")||path.endsWith(".PNG")||path.endsWith(".jpg")||path.endsWith(".JPG"))){
			File newFile = FileUtils.compressFile(path, compressPath);
			return Uri.fromFile(newFile);
		}else{
			Toast.makeText(this, "上传的图片仅支持png或jpg格式",Toast.LENGTH_SHORT).show();
		}
		return null;
	}



	/**
	 * 返回文件选择
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
//		if (requestCode == 1) {
//			if (null == mUploadMessage)
//				return;
//			Uri result = intent == null || resultCode != RESULT_OK ? null
//					: intent.getData();
//			mUploadMessage.onReceiveValue(result);
//			mUploadMessage = null;
//		}
		
		if (null == mUploadMessage)
			return;
		Uri uri = null;
		if(requestCode == REQ_CAMERA ){
			afterOpenCamera();
			uri = cameraUri;
		}else if(requestCode == REQ_CHOOSE){
//			uri = afterChosePic(intent);
		uri = intent == null || resultCode != RESULT_OK ? null
				: intent.getData();
		}
		mUploadMessage.onReceiveValue(uri);
		mUploadMessage = null;
		super.onActivityResult(requestCode, resultCode, intent);
	}
	

	public class OnlineWebViewClient extends WebViewClient {
		
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
            if( url!=null ) {  
                return false;  
            }  
  
            // Otherwise allow the OS to handle things like tel, mailto, etc.  
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));  
//            startActivity( intent );  
//            return true;  
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			wb_epos.getSettings().setBlockNetworkImage(false);
			ll_web_loading.setVisibility(View.GONE);
		}
		
		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			// TODO Auto-generated method stub
			handler.proceed();
		}
		
		
	}

	class GetWalletInfohai extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在加载...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {

			String[] values = { params[0], params[1] };
			return NetCommunicate
					.getMidatc(HttpUrls.RICH_TREASURE_INFO, values);

		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {

			if (result != null) {
				if (result.get(Entity.RSPCOD) != null
						&& result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					loadingDialogWhole.dismiss();

					if (result.get("MERNAM") != null
							&& !result.get("MERNAM").equals("")) {
						username = result.get("MERNAM").toString();
					} else {
						username = "未知";
					}
					if (result.get("ISSETPAYPWD") != null
							&& !result.get("ISSETPAYPWD").equals("")) {
						lognum = result.get("ISSETPAYPWD").toString();
					} else {
						lognum = "1";
					}

					inithtml();
				} else {

					if (result.get(Entity.RSPMSG) != null) {
						loadingDialogWhole.dismiss();

						warnDialog = new OneButtonDialogWarn(
								HaiGouAvtivity.this, R.style.CustomDialog,
								"提示", result.get(Entity.RSPMSG).toString(),
								"确定", new OnMyDialogClickListener() {
									@Override
									public void onClick(View v) {
										finish();
										warnDialog.dismiss();
									}
								});
						warnDialog.show();
					} else {
						loadingDialogWhole.dismiss();

						warnDialog = new OneButtonDialogWarn(
								HaiGouAvtivity.this, R.style.CustomDialog,
								"提示", "网络异常请重试", "确定",
								new OnMyDialogClickListener() {
									@Override
									public void onClick(View v) {
										finish();
										warnDialog.dismiss();
									}
								});
						warnDialog.show();
					}
				}
			} else {
				Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络连接",
						Toast.LENGTH_SHORT).show();
				loadingDialogWhole.dismiss();
			}
			super.onPostExecute(result);

		}

	}
	
	
	//下面是打电话的操作方法  
    final class Phone { 
    	
        public void call(String mobile) {  
            Uri uri = Uri.parse("tel:" + mobile);  
            Intent intent = new Intent(Intent.ACTION_CALL, uri);  
            startActivity(intent);  
        }  
    } 
	
	

	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if ((keyCode == KeyEvent.KEYCODE_BACK) && wb_epos.canGoBack()) {
	// wb_epos.goBack(); // goBack()表示返回WebView的上一页面
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }
}
