package com.shareshenghuo.app.user.network.bean;
import java.io.File;

import com.shareshenghuo.app.user.BaseTopActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.util.FileUtils;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class DffWebView extends BaseTopActivity{
	
	private String title = "";
	private String url = "";
	private WebView mWebView;
	ValueCallback<Uri> mUploadMessage;
	ValueCallback<Uri[]> mUploadMessage1;
	private boolean isdown = false;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.dff_web);
		
		title = getIntent().getStringExtra("title");
		url = getIntent().getStringExtra("url");
		initTopBar(title);
		initView();
		
	}
	
	private void initView() {
		mWebView = (WebView) findViewById(R.id.webView);
		mWebView.setWebChromeClient(new MyWebChromeClient());
		
//		webView.loadUrl("file:///android_asset/upload_image.html");
	
//		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.getSettings().setSupportMultipleWindows(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.getSettings().setBuiltInZoomControls(true);//support zoom
		mWebView.getSettings().setUseWideViewPort(true);// 这个很关键
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.loadUrl(url);
	}
	
//    @Override
//    public void onResume() {
//        super.onResume();
//        }


	
	   public class MyWebViewClient extends WebViewClient {

	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        	if (url.startsWith("mqqwpa")) {
					Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(in);
					return false;
				}
	    		// 支持拨打电话
	    		if (url.startsWith("tel:")) {
	    			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	    			startActivity(intent);
	    			return false;
	    		}
	        	
	            return super.shouldOverrideUrlLoading(view, url);
	        }

	        @Override
	        public void onPageStarted(WebView view, String url, Bitmap favicon) {
	            super.onPageStarted(view, url, favicon);
	        }

	        @Override
	        public void onPageFinished(WebView view, String url) {
	            super.onPageFinished(view, url);
	        }

	        @Override
	        public void onReceivedError(WebView view, int errorCode,
	                                    String description, String failingUrl) {
	            super.onReceivedError(view, errorCode, description, failingUrl);
//	            T.showShort(mActivity, "加载失败，请稍候再试");
	        }

//	    	@Override
//			public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//				// url:tencent://message/?uin=156753296&Site=www.adsofts.cn&Menu=no
//				Log.d("--------->", "这里是shouldInterceptRequest url:" + url);
	//
//				if (url.startsWith("tencent")) {
//					Log.d("--------->", "执行跳转到QQ url:" + url);
//					Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//					startActivity(in);
//					return null;
//				}
//				if (url.startsWith("http") || url.startsWith("https")) {
//					return super.shouldInterceptRequest(view, url);
//				} else {
//					// Log.d("--------->", "执行跳转到QQ url:" + url);
//					// Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//					// startActivity(in);
//					return null;
//				}
//			}

	        
	        @Override
	        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
	        	handler.proceed();//接受所有网站的证书
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
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            i.setType("*/*");
//                startActivityForResult( Intent.createChooser( i, "File Chooser" ), FILECHOOSER_RESULTCODE );
        }
         // For Android < 3.0
	        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
	               openFileChooser( uploadMsg, "" );
	        }
	        // For Android  > 4.1.1
	      public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
	              openFileChooser(uploadMsg, acceptType);
	              Log.e("", " - - - - ");
	      }
	      
          @Override
		public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
              if (mUploadMessage1 != null) {
                  mUploadMessage1.onReceiveValue(null);
              }
//        	  if (mUploadMessage1 != null) return false;
              mUploadMessage1 = filePathCallback;   
              selectImage();
              
              return true;
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
		AlertDialog dialog = new AlertDialog.Builder(this)
				.setItems(selectPicTypeStr,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								// 相机拍摄
								case 0:
									isdown = true;
									openCarcme();
									break;
								// 手机相册
								case 1:
									isdown = true;
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
//		dialog.setCancelable(false);
//		dialog.setc
		dialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				if(!isdown&&mUploadMessage1!=null){
					mUploadMessage1.onReceiveValue(null);
					  mUploadMessage1 = null;
				}
				if(!isdown&&mUploadMessage!=null){
					mUploadMessage.onReceiveValue(null);
					  mUploadMessage= null;
				}
			}
		});
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
				+ (System.currentTimeMillis() + ".png");
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
		if(!f.exists()){
			  mUploadMessage1.onReceiveValue(null);
			  mUploadMessage1 = null;
			  return;
		}
		addImageGallery(f);
		File newFile = FileUtils.compressFile(f.getPath(), compressPath);
		
	}

	/** 解决拍照后在相册中找不到的问题 */
	private void addImageGallery(File file) {
		ContentValues values = new ContentValues();
		values.put(MediaColumns.DATA, file.getAbsolutePath());
		values.put(MediaColumns.MIME_TYPE, "image/jpeg");
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
		String[] proj = { MediaColumns.DATA };
		// 好像是android多媒体数据库的封装接口，具体的看Android文档
		Cursor cursor = managedQuery(data.getData(), proj, null, null, null);
		if(cursor == null ){
			Toast.makeText(this, "上传的图片仅支持png或jpg格式",Toast.LENGTH_SHORT).show();
			return null;
		}
		// 按我个人理解 这个是获得用户选择的图片的索引值
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
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
		
		
//		 if (requestCode == FILECHOOSER_RESULTCODE) {
			if (mUploadMessage1 != null) {
				Uri uri = null;
				if (requestCode == REQ_CAMERA) {
//					if(intent!=null){
						afterOpenCamera();
						uri = cameraUri;
						try {
							mUploadMessage1.onReceiveValue(new Uri[] {uri});
							mUploadMessage1 = null;
						} catch (Exception e) {
							// TODO: handle exception
							if(mUploadMessage1!=null){
								mUploadMessage1.onReceiveValue(null);
							}
							
							mUploadMessage1 = null;
						}

//					}else{
//						mUploadMessage1.onReceiveValue(null);
//						mUploadMessage1 = null;
//					}
				} else if (requestCode == REQ_CHOOSE) {
					Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
					if(result!=null){
//					uri = afterChosePic(intent);
//					uri = intent == null || resultCode != RESULT_OK ? null: intent.getData();
//					Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
					String path =  FileUtils.getPath(this, result);
				     if (TextUtils.isEmpty(path)) {
				        mUploadMessage1.onReceiveValue(null);
				        mUploadMessage1 = null;
				        return;
				    }
				    uri = Uri.fromFile(new File(path));
					mUploadMessage1.onReceiveValue(new Uri[] {uri});
					mUploadMessage1 = null;

					}else{
					mUploadMessage1.onReceiveValue(null);
					mUploadMessage1 = null;
					}
				}


			}
			 
			if (mUploadMessage != null) {
				Uri uri = null;
				if (requestCode == REQ_CAMERA) {
					if(intent!=null){
						afterOpenCamera();
						uri = cameraUri;
						mUploadMessage.onReceiveValue(uri);
						mUploadMessage = null;
					}else{
						mUploadMessage.onReceiveValue(null);
						mUploadMessage = null;
					}
				} else if (requestCode == REQ_CHOOSE) {
					Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
					if(result!=null){
//					uri = afterChosePic(intent);
//					uri = intent == null || resultCode != RESULT_OK ? null: intent.getData();
//					Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
					String path =  FileUtils.getPath(this, result);
				     if (TextUtils.isEmpty(path)) {
				        mUploadMessage.onReceiveValue(null);
				        mUploadMessage = null;
				        return;
				    }
				    uri = Uri.fromFile(new File(path));
					mUploadMessage.onReceiveValue(uri);
					mUploadMessage = null;

					}else{
					mUploadMessage.onReceiveValue(null);
					mUploadMessage = null;
					}
				}
				
	}
			isdown = false;
			super.onActivityResult(requestCode, resultCode, intent);
		
}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {  
			mWebView.goBack();  
			return true;  
		}else{
		        finish();
		}
		return super.onKeyDown(keyCode, event);  
		}
}