package com.td.qianhai.fragmentmanager;

import java.io.File;
import java.net.URLEncoder;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.share.app.entity.response.Constans;
import com.share.app.event.MainPageChangeEvent;
import com.td.qianhai.epay.oem.AddShopActivity;
import com.td.qianhai.epay.oem.BusinessCenterActivity;
import com.td.qianhai.epay.oem.PoiMapActivity;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.beans.CityInfo;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.DESKey;
import com.td.qianhai.epay.oem.mail.utils.FileUtils;
import com.td.qianhai.epay.oem.mail.utils.ImageUtil;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.views.PullToRefreshLayout;
import com.td.qianhai.epay.oem.views.city.CityManager;
import com.td.qianhai.epay.oem.views.city.CityManager.LocationCallback;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 第二个tab页 （附近）
 * 
 * @author Administrator
 * 
 */
public class TabAFm extends Activity implements LocationCallback {
	/** 头部标题 */
	private TextView title_name;
	/** 视图 */
//	private View view;
	
	private View titleview;
	
	private PullToRefreshLayout refreshlaout;
	
	private WebView webView;
	
	private  double lat = 0,lng = 0; 
	 
	private String mobile= "",pwd= "",oemid= "",mercnum = "",url;
	
	private ValueCallback<Uri> mUploadMessage;
	
	private boolean ismiss = false;
	
	private CityManager cityManager;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(R.layout.tab_a);
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		pwd = MyCacheUtil.getshared(this).getString("pwd", "");
		oemid = MyCacheUtil.getshared(this).getString("OEMID", "");
		mercnum = MyCacheUtil.getshared(this).getString("MercNum", "");
//		url = HttpUrls.BUSINESS+inithtml();
		url = HttpUrls.SHARE_WEB_CONSUME + MyCacheUtil.getshared(this).getString(Constans.Login.USERID, "");
		Log.e("", ""+url);
		initview();
		
		initmap();
	}
	
	
	private void initmap() {
		// TODO Auto-generated method stub
		cityManager = CityManager.getInstance(this);
		cityManager.setLocationCallback(this);
		cityManager.getCityInfo();
	}


	private String inithtml() {
		// TODO Auto-generated method stub

		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("PHONENUMBER", mobile);
			jsonObj.put("USERNAME", "");
			jsonObj.put("PASSWORD", pwd);
			jsonObj.put("MERCNUM", mercnum);
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
		return aa;
	}
	

	
	@SuppressLint("SetJavaScriptEnabled") 
	private void initview() {
		
//		refreshlaout =  (PullToRefreshLayout) findViewById(R.id.refresh_view);
//		refreshlaout.canPullUp();
//		refreshlaout.setOnRefreshListener(new OnRefreshListener() {
//			
//			@Override
//			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//				// TODO Auto-generated method stub
////				webView.loadUrl(url);  
//				webView.reload(); 
//				refreshlaout.refreshFinish(0);
//			}
//			
//			@Override
//			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//				// TODO Auto-generated method stub
//				refreshlaout.loadmoreFinish(0);
//			}
//		});
		
		webView = (WebView) findViewById(R.id.wv_epos);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setBuiltInZoomControls(true);//support zoom
//		webView.getSettings().setUseWideViewPort(true);// 这个很关键
//		webView.getSettings().setLoadWithOverviewMode(true);
//		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		System.out.println("====加载的网页地址:"+url);
		webView.loadUrl(url);//"http://erma.h5h5h5.cn/yewu-wap/page/client/updateShop2.html?id=1&collect_shop_id=43");
		// 监听有需要再修改
		OnlineWebViewClient viewClient = new OnlineWebViewClient();
		webView.setWebViewClient(viewClient);
		webView.setWebChromeClient(new MyWebChromeClient());
		webView.addJavascriptInterface(new Object() {
			
			@JavascriptInterface
        	public void getPlace() {  
               startActivityForResult(new Intent(TabAFm.this, PoiMapActivity.class), 8);
           }   
			
			@JavascriptInterface
        	public void shareAction(String url) {  
				share(url);
           }
			
			@JavascriptInterface
        	public void businessCenterAction() {  
				Intent it = new Intent();
				it.setClass(TabAFm.this, BusinessCenterActivity.class);
				startActivity(it);
           }
			
			@JavascriptInterface
        	public void fazhanShop(String id) {  
				Intent it = new Intent(TabAFm.this,AddShopActivity.class);
				it.putExtra("handle_id", id);
				it.putExtra("tag", "0");
				startActivity(it);
           }
			@JavascriptInterface
        	public void changeShop(String id) {  
				Intent it = new Intent(TabAFm.this,AddShopActivity.class);
				Log.e("", " = = = = =  "+id);
				it.putExtra("handle_id", id);
				it.putExtra("tag", "1");
				startActivity(it);
           }
		 }, "erma");  
	}
	
	
	
	public static final int FILECHOOSER_RESULTCODE = 1;
	private static final int REQ_CAMERA = FILECHOOSER_RESULTCODE+1;
	private static final int REQ_CHOOSE = REQ_CAMERA+1;
	private class MyWebChromeClient extends WebChromeClient {

		// For Android 3.0+
		   public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {  
			   Log.e("", "111111");
//               if (mUploadMessage != null) return;
               mUploadMessage = uploadMsg;   
               selectImage();
//               Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//               i.addCategory(Intent.CATEGORY_OPENABLE);
//               i.setType("*/*");
//                   startActivityForResult( Intent.createChooser( i, "File Chooser" ), FILECHOOSER_RESULTCODE );
           }
            // For Android < 3.0
	        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
	        	 Log.e("", "22222");
	               openFileChooser( uploadMsg, "" );
	        }
	        // For Android  > 4.1.1
	      public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
	    	  Log.e("", "3333333");
	              openFileChooser(uploadMsg, acceptType);
	      }

	}
	
	public class OnlineWebViewClient extends WebViewClient {
		
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
//            if( url!=null ) {  
//                return false;  
//            }  
			Log.e("", ""+url);
			if (url.endsWith("index")) {
				finish();
                EventBus.getDefault().post(new MainPageChangeEvent(0));
				return true;
			}
            if (url.endsWith("userMoneyLogNew")) {
                return true;
            }
			if (url.endsWith("myUser")) {
				finish();
                EventBus.getDefault().post(new MainPageChangeEvent(2));
				return true;
			}
			if (url.endsWith("help")) {
				finish();
                EventBus.getDefault().post(new MainPageChangeEvent(3));
				return true;
			}
	        if (url.startsWith("tel:")) { 
	            Intent intent = new Intent(Intent.ACTION_VIEW,
	                    Uri.parse(url)); 
	            startActivity(intent); 
	            } else{
	            	 view.loadUrl(url);
	            }
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//		adapter.onActivityResult(requestCode, resultCode, data);
		if(resultCode == this.RESULT_OK) {
			Uri uri = null;
			switch(requestCode) {
			case 8:
//				addr = data.getStringExtra("addr");
				 lat = intent.getDoubleExtra("lat", lat);
				 lng = intent.getDoubleExtra("lng", lng);
				 
	       			this.runOnUiThread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	
	                    	webView.loadUrl("javascript:setPlace('" +lng + "','"+lat+"')");  
	                    }
	                });
				break;
			case REQ_CAMERA:
				if (null == mUploadMessage)
					return;
					afterOpenCamera();
					uri = cameraUri;
					mUploadMessage.onReceiveValue(uri);
					mUploadMessage = null;
					ismiss = false;
					break;
			case REQ_CHOOSE:
				if (null == mUploadMessage)
					return;
					uri = intent == null || resultCode != this.RESULT_OK ? null
					: intent.getData();
					ImageUtil.saveBitmap2file(getPath(this, uri),768,1280);
//					cameraUri = Uri.fromFile(());
//					ImageUtil.saveBitmap2file(uri.getPath(),768,1280);
//					File f = new File(imagePaths);
//					addImageGallery(f);
					mUploadMessage.onReceiveValue(uri);
					mUploadMessage = null;
					ismiss = false;
					break;
			
			}
			
		}else{
			if(mUploadMessage!=null){
				mUploadMessage.onReceiveValue(null);
				mUploadMessage = null;
			}

		}

//		super.onActivityResult(requestCode, resultCode, intent);
	}
	
	
	
	
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {
	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}
	
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int column_index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(column_index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
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
			Toast.makeText(this.getApplicationContext(), "请插入手机存储卡再使用本功能",Toast.LENGTH_SHORT).show();
		}
		return flag;
	}
	String compressPath = "";
	@SuppressLint("NewApi") protected final void selectImage() {
		
	
		if (!checkSDcard())
			return;
		
		 Log.e("", "444444");
		String[] selectPicTypeStr = { "拍照","相册"};
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//		new AlertDialog.Builder(this)
		dialog.setItems(selectPicTypeStr,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								// 相机拍摄
								case 0:
									ismiss = true;
									openCarcme();
									break;
								// 手机相册
								case 1:
									ismiss = true;
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
							
							
						});
					dialog.setOnDismissListener(new OnDismissListener() {
			
						@Override
						public void onDismiss(DialogInterface arg0) {
							// TODO Auto-generated method stub
							if(ismiss==false){
								mUploadMessage.onReceiveValue(null);
								mUploadMessage = null;
							}
						}
					});

						
//						dialog.setCancelable(false);
						dialog.show();
						
		
	}
	
	String imagePaths;
	Uri  cameraUri,phoneUri;
	
	/**
	 * 打开照相机
	 */
	
	public String getSDPath(){ 
	       File sdDir = null; 
	       boolean sdCardExist = Environment.getExternalStorageState()   
	       .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
	        if(sdCardExist)   
	       {                               
	         sdDir = Environment.getExternalStorageDirectory();//获取跟目录
	       }   
	       return sdDir.toString(); 
	}
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
		ImageUtil.saveBitmap2file(imagePaths,768,1280);
		
		
		File f = new File(imagePaths);
		addImageGallery(f);
//		File newFile = FileUtils.compressFile(f.getPath(), compressPath);
	}

	/** 解决拍照后在相册中找不到的问题 */
	private void addImageGallery(File file) {
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		this.getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}

	/**
	 * 本地相册选择图片
	 */
	private void chosePic() {
		FileUtils.delFile(compressPath);
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
//		String IMAGE_UNSPECIFIED = "image/*";
//		innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
//		Intent wrapperIntent = Intent.createChooser(innerIntent, null);
//		startActivityForResult(wrapperIntent, REQ_CHOOSE);
		
		innerIntent.addCategory(Intent.CATEGORY_OPENABLE);  
		innerIntent.setType("image/jpeg");  
		if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){                  
		        startActivityForResult(innerIntent, REQ_CHOOSE);    
		}else{                
		        startActivityForResult(innerIntent, REQ_CHOOSE);   
		}
		
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
		Cursor cursor = this.managedQuery(data.getData(), proj, null, null, null);
		if(cursor == null ){
			Toast.makeText(this.getApplicationContext(), "上传的图片仅支持png或jpg格式",Toast.LENGTH_SHORT).show();
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
			Toast.makeText(this.getApplicationContext(), "上传的图片仅支持png或jpg格式",Toast.LENGTH_SHORT).show();
		}
		return null;
	}


	@Override
	public void getCity(CityInfo cityInfo) {
		// TODO Auto-generated method stub
		
	}
	
	private void share(String url){
		UMSocialService  mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		mController.getConfig().setPlatforms(
				SHARE_MEDIA.WEIXIN,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QZONE);
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		mController
		.setShareContent("");
		String appID = "wx67b3a23ca6de80f8";
		String appSecret = "1a35ae5ac3d57848c743a5655d11cb4b";
		
		
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(TabAFm.this,
				"1104744982", "MCXWLlnpU2S4ULld");
		qqSsoHandler.addToSocialSDK();
		
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(TabAFm.this, appID, appSecret);
		// WeiXinShareContent weixinContent = new WeiXinShareContent();
		// weixinContent.setTargetUrl("http://180.166.124.95:8092/posm/upload/QH_W_V1.2.apk");
		wxHandler.addToSocialSDK();
		
		UMImage aa = new UMImage(TabAFm.this, R.drawable.ico);

		WeiXinShareContent weixinContent = new WeiXinShareContent();
		
		UMWXHandler wxCircleHandler = new UMWXHandler(TabAFm.this, appID,
				appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		// 设置分享文字
		weixinContent.setShareContent("秀儿公益");
		// 设置title
		weixinContent.setTitle("秀儿公益");
		// 设置分享内容跳转URL
		weixinContent
				.setTargetUrl(url);
		// 设置分享图片m
		weixinContent.setShareImage(aa);
		
		QZoneShareContent qzone = new QZoneShareContent();
		// 设置分享文字
		qzone.setShareContent("秀儿公益");
		// 设置点击消息的跳转URL
		qzone.setTargetUrl(url);
		// 设置分享内容的标题
		qzone.setTitle("秀儿公益");
		// 设置分享图片
		qzone.setShareImage(aa);
		
		
	      //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent("秀儿公益");
        circleMedia.setTitle("秀儿公益");
        circleMedia.setShareImage(aa);
        circleMedia.setTargetUrl(url);
        mController.setShareMedia(circleMedia); 
        
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(TabAFm.this,
				"1104744982", "MCXWLlnpU2S4ULld");
		qZoneSsoHandler.addToSocialSDK();
		
		QQShareContent qqShareContent = new QQShareContent();
		// 设置分享文字
		qqShareContent.setShareContent("秀儿公益");
		// 设置分享title
		qqShareContent.setTitle("秀儿公益");
		// 设置分享图片
		qqShareContent.setShareImage(aa);
		// 设置点击分享内容的跳转链接
		qqShareContent
				.setTargetUrl(url);
		mController.setShareMedia(qzone);
		
		mController.setShareMedia(qqShareContent);

		mController.setShareMedia(weixinContent);

		mController.openShare(TabAFm.this, false);
	}
	
}
