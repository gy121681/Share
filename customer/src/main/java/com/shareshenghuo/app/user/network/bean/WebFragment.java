package com.shareshenghuo.app.user.network.bean;

import java.io.File;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.FileUtils;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.user.widget.dialog.TwoButtonDialog;

/**
 * @author hang
 */
public class WebFragment extends Fragment {
	
	private static final int REQ_ADD_ARTICLE = 0x100;

    private final static String PARAMS_TITLE = "params_title";
    private final static String PARAMS_URL = "params_url";
    private final static String PARAMS_BACK = "params_back";
    private final static String UA = "android_citylife";
    private final static String JS_NAME = "yicheng";
    private  TwoButtonDialog downloadDialog;
    
    private WebView webView = null;
    private String url;
//    private ValueCallback<Uri> mUploadMessage;
    private Activity mActivity;
    
	ValueCallback<Uri> mUploadMessage;
	ValueCallback<Uri[]> mUploadMessage1;
	private boolean isdown = false;
	public static final int FILECHOOSER_RESULTCODE = 1;
	private static final int REQ_CAMERA = FILECHOOSER_RESULTCODE+1;
	private static final int REQ_CHOOSE = REQ_CAMERA+1;
	private View rootView;
	
	private Activity activity;
	
//	@Override
//	protected int getLayoutId() {
//		return R.layout.fragment_life;
//	}
//
//	@Override
//	protected void init(View rootView) {
//		initView();
//		loadData();
//		getCircleList();
//	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = getActivity();
		rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_web_view, null);
		initView();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		
		if (rootView == null) {
			rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_web_view, null);
		}

		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}
	
	public void initView() {
		
		 webView = (WebView) rootView.findViewById(R.id.webView);
//       webView.getSettings().setUserAgentString(UA);
       webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //禁止缓存
       webView.getSettings().setJavaScriptEnabled(true);
       webView.addJavascriptInterface(new JSClient(mActivity), "GobackToHomepage");//JS_NAME);
       // 保证可滑动
       webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
       String appCachePath = getActivity().getApplicationContext().getCacheDir().getAbsolutePath();
       webView.getSettings().setAppCachePath(appCachePath);
       webView.getSettings().setAllowFileAccess(true);
       webView.getSettings().setAppCacheEnabled(true);
       webView.getSettings().setSupportZoom(true);
       webView.getSettings().setBuiltInZoomControls(true);// 支持缩放
       webView.getSettings().setLoadsImagesAutomatically(true);
//       // 启用数据库
       webView.getSettings().setDatabaseEnabled(true);
       // 启用地理定位
       webView.getSettings().setGeolocationEnabled(true);
       // 设置定位的数据库路径
       String dir = getActivity().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
       webView.getSettings().setGeolocationDatabasePath(dir);
       webView.getSettings().setDomStorageEnabled(true);
       webView.getSettings().setUseWideViewPort(true);
       webView.getSettings().setLoadWithOverviewMode(true);
       webView.setDownloadListener(new MyWebViewDownLoadListener());
//       webView.requestFocus();
       webView.setWebViewClient(new MyWebViewClient());
//       // 各种内容的渲染需要使用webviewChromClient去实现
       webView.setWebChromeClient(new WebChromeClient() {
       	@Override
   		public boolean onJsAlert(WebView view, String url, final String message, final JsResult result) {
       		initDialog(message, "确定", "",result);
       		
   			return true;
   		}
       	
       	// For Android 3.0+
		   public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {  
            if (mUploadMessage != null) return;
            mUploadMessage = uploadMsg;   
            selectImage();
        }
         // For Android < 3.0
	        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
	               openFileChooser( uploadMsg, "" );
	        }
	        // For Android  > 4.1.1
	      public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
	              openFileChooser(uploadMsg, acceptType);
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

       });
       
       UserInfo userInfo = UserInfoManager.getUserInfo(getActivity());
       String mall_url  = "";
       String mall_user_id = "";
       String mall_user_password = "";
       String face = "";
       String nickname = "";
       String appid = "";
       if(userInfo.mall_url!=null){
    	   mall_url = userInfo.mall_url;
       }
       if(userInfo.mall_user_id!=null){
    	   mall_user_id = userInfo.mall_user_id;
       }
       if(userInfo.mall_user_password!=null){
    	   mall_user_password = userInfo.mall_user_password;
       }
       if(userInfo.user_photo!=null){
    	   face = Api.HOSTERMA+userInfo.user_photo;
       }
       if(userInfo.nick_name!=null){
    	   nickname = userInfo.nick_name;
       }
       if(userInfo.app_id!=null){
    	   appid = userInfo.app_id;
       }
       
//       String mall_user_id = userInfo.mall_user_id;
//       String mall_user_password = userInfo.mall_user_password;
//       String face = Api.HOSTERMA+userInfo.user_photo;
//       String nickname = userInfo.nick_name;
//       String appid = userInfo.app_id;
       
       
     //需要访问的网址  
       String url = Api.SHOP;  
       //post访问需要提交的参数  
       String postDate = "mall_url="+mall_url+"&account="+mall_user_id+"&password="+mall_user_password+"&face="+face+"&nickname="+nickname+"&appid="+appid;  
       Log.e("", "postDate = = = "+postDate);
       webView.postUrl(url, EncodingUtils.getBytes(postDate, "BASE64"));  
//       if (getArguments() != null) {
//       	url = getArguments().getString(PARAMS_URL);
//           webView.loadUrl(url);
//       }
		
	}
	
	 class JSClient {

	        private Context mContext;

	        private static final int RESULT_RREFRUSH = 1;

	        public JSClient(Context context) {
	            this.mContext = context;
	        }

	        
		      @JavascriptInterface
		      public void GobackHome() {
//		    	  if(PaymentnewActivity.context!=null){
//		    		  PaymentnewActivity.context. finish();
//		    	  }
//		    	  mActivity.finish();
		      }
		      @JavascriptInterface
		      public void jsJumpToPayView() {
//		    	  mActivity.finish();
		      }
	        
	        @JavascriptInterface
	        public String getIMEI() {
	            TelephonyManager tm = (TelephonyManager) mContext.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
	            return tm.getDeviceId();
	        }

	        @JavascriptInterface
	        public String getIMSI() {
	            TelephonyManager tm = (TelephonyManager) mContext.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
	            return tm.getSubscriberId();
	        }

	        @JavascriptInterface
	        public void sendSMS(String phoneNum, String content) {
	            Uri uri = Uri.parse(String.format("smsto:%s", phoneNum));
	            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
	            it.putExtra("sms_body", content);
	            mContext.startActivity(it);
	        }
	        
	        @JavascriptInterface
	        public void payComplete() {
//	        	mActivity.setResult(Activity.RESULT_OK);
//	        	mActivity.finish();
	        }
	    }
	 
	  private void initDialog(String content,String left,String right, final JsResult result) {
			// TODO Auto-generated method stub
	    	  downloadDialog = new TwoButtonDialog(getActivity(), R.style.CustomDialog,
					"提示", content, left, right,true,new OnMyDialogClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							switch (v.getId()) {
							case R.id.Button_OK:
								result.confirm();
								downloadDialog.dismiss();
								break;
							case R.id.Button_cancel:
								result.confirm();
								downloadDialog.dismiss();
							default:
								break;
							}
						}
					});
				downloadDialog.show();
				downloadDialog.setCancelable(false);
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
				Toast.makeText(mActivity, "请插入手机存储卡再使用本功能",Toast.LENGTH_SHORT).show();
			}
			return flag;
		}
		String compressPath = "";
	  
	  protected final void selectImage() {
			if (!checkSDcard())
				return;
			String[] selectPicTypeStr = { "拍照","相册" };
			AlertDialog dialog = new AlertDialog.Builder(mActivity)
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
//			dialog.setCancelable(false);
//			dialog.setc
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
			mActivity.getContentResolver().insert(
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
			Cursor cursor = mActivity.managedQuery(data.getData(), proj, null, null, null);
			if(cursor == null ){
				Toast.makeText(mActivity, "上传的图片仅支持png或jpg格式",Toast.LENGTH_SHORT).show();
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
				Toast.makeText(mActivity, "上传的图片仅支持png或jpg格式",Toast.LENGTH_SHORT).show();
			}
			return null;
		}



		/**
		 * 返回文件选择
		 */
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent intent) {
			
			
//			 if (requestCode == FILECHOOSER_RESULTCODE) {
				if (mUploadMessage1 != null) {
					Uri uri = null;
					if (requestCode == REQ_CAMERA) {
//						if(intent!=null){
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

//						}else{
//							mUploadMessage1.onReceiveValue(null);
//							mUploadMessage1 = null;
//						}
					} else if (requestCode == REQ_CHOOSE) {
						Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
						if(result!=null){
//						uri = afterChosePic(intent);
//						uri = intent == null || resultCode != RESULT_OK ? null: intent.getData();
//						Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
						String path =  FileUtils.getPath(mActivity, result);
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
						Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
						if(result!=null){
//						uri = afterChosePic(intent);
//						uri = intent == null || resultCode != RESULT_OK ? null: intent.getData();
//						Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
						String path =  FileUtils.getPath(mActivity, result);
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
		
		 private class MyWebViewDownLoadListener implements DownloadListener {
		        @Override
		        public void onDownloadStart(String url, String userAgent,
		                                    String contentDisposition, String mimetype, long contentLength) {
		            Uri uri = Uri.parse(url);
		            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		            startActivity(intent);
		        }
		    }
		 
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
		    			return true;
		    		}
		    		view.loadUrl(url);
		    		
		    		return true;
//		            return super.shouldOverrideUrlLoading(view, url);
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
		            T.showShort(mActivity, "加载失败，请稍候再试");
		        }

		        @Override
		        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
		        	handler.proceed();//接受所有网站的证书
		        }
		    }
		 
			/**
			 * 监听返回按钮
			 */
//			@Override
			public boolean onKeyDown(int keyCode, KeyEvent event) {
				
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					
			        if (webView.canGoBack()) {
			            webView.goBack();
			        }
				}
				return true;
			}
}
