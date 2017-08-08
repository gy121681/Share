package com.td.qianhai.epay.oem;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONException;
import org.json.JSONObject;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.interfaces.JavaScriptinterface;
import com.td.qianhai.epay.oem.mail.utils.DESKey;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("JavascriptInterface") public class ShouKuanBAvtivity extends BaseActivity {

	private WebView wb_epos;
	private RelativeLayout ll_web_loading;
	private String pwd, mobile, username;
	private OneButtonDialogWarn warnDialog;
	private ValueCallback<Uri> mUploadMessage;
	private String URLs = "";
	private String lognum,oemid;
	private HashMap<String, Object> result = null; 
	@Override
	@SuppressLint({ "NewApi", "JavascriptInterface", "SetJavaScriptEnabled" })
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.activity_haigou_layout);
		Intent it = getIntent();
		URLs = it.getStringExtra("url");
		String tags = it.getStringExtra("tag");
		pwd = MyCacheUtil.getshared(this).getString("pwd", "");
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		oemid = MyCacheUtil.getshared(this).getString("OEMID", "");
		// lognum = MyCacheUtil.getshared(this).getString("LOGNUM", "");
		if (tags.equals("0")) {
			initview();
		} else {
			webview();
		}
		
	}

	private void initview() {
		// TODO Auto-generated method stub
		
		showLoadingDialog("请稍候...");
		new Thread(run).start();
		
//		ShouKuanTask walletinfo = new ShouKuanTask();
//
//		walletinfo.execute(HttpUrls.COLLECTIONTREASURE + "",oemid,mobile);

	}

	private void webview() {
		ll_web_loading = (RelativeLayout) findViewById(R.id.ll_web_loading);
		wb_epos = (WebView) findViewById(R.id.wv_epos);
		wb_epos.getSettings().setSupportMultipleWindows(true);
		wb_epos.getSettings().setJavaScriptEnabled(true);
		wb_epos.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		wb_epos.loadUrl(URLs);
		
//		wb_epos.getSettings().setSupportZoom(true);
//		wb_epos.getSettings().setJavaScriptEnabled(true);
//		wb_epos.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//		wb_epos.getSettings().setBuiltInZoomControls(true);//support zoom
//		wb_epos.getSettings().setUseWideViewPort(true);// 这个很关键
//		wb_epos.getSettings().setLoadWithOverviewMode(true);
		wb_epos.addJavascriptInterface(new JavaScriptinterface(this),
				"android");
		// 监听有需要再修改
		OnlineWebViewClient viewClient = new OnlineWebViewClient();
		wb_epos.setWebViewClient(viewClient);
		
	}
	

	
	private void inithtml() {
		// TODO Auto-generated method stub

		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("PHONENUMBER", mobile);
			jsonObj.put("PASSWORD", pwd);
			jsonObj.put("USERNAME", username);
			jsonObj.put("ISSETPAYPWD", lognum);
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
//		if (Build.VERSION.SDK_INT >= 19) {
//			wb_epos.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//	    }
		wb_epos.loadUrl(URLs + aa);
		wb_epos.addJavascriptInterface(new JavaScriptinterface(this),
				"android");
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
		Log.e("", "f  == "+f);
		Log.e("", "compressPath  == "+compressPath);
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
//		FileUtils.delFile(compressPath);
//		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
//		String IMAGE_UNSPECIFIED = "image/*";
//		innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
//		Intent wrapperIntent = Intent.createChooser(innerIntent, null);
//		startActivityForResult(wrapperIntent, REQ_CHOOSE);
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
//			File newFile = FileUtils.compressFile(path, compressPath);
//			return Uri.fromFile(newFile);
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
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
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
								ShouKuanBAvtivity.this, R.style.CustomDialog,
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
								ShouKuanBAvtivity.this, R.style.CustomDialog,
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
	
	Runnable run = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = null;
			 
			try {
				
				String[] values = {"1",mobile};
				 result = NetCommunicate.executeHttpPostnulls(HttpUrls.SKBDIRECTLOGIN,
						HttpKeys.WECHATRECEIVE1_BACK,HttpKeys.WECHATRECEIVE_ASK,values);
			} catch (HttpHostConnectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Message msg = new Message();
			if(result!=null){
				if(result.get("RSPCOD")!=null&&result.get("RSPCOD").toString().equals("000000")){
					msg.what = 0;
				}else if(result.get("RSPCOD")!=null&&result.get("RSPCOD").toString().equals("100001")){
					msg.what = 1;
				}else if(result.get("RSPCOD")!=null&&result.get("RSPCOD").toString().equals("100002")){
					msg.what = 2;
				}else if(result.get("RSPCOD")!=null&&result.get("RSPCOD").toString().equals("100003")){
					msg.what = 3;
				}else if(result.get("RSPCOD")!=null&&result.get("RSPCOD").toString().equals("100004")){
					msg.what = 4;
				}else if(result.get("RSPCOD")!=null&&result.get("RSPCOD").toString().equals("100005")){
					msg.what = 5;
				}else if(result.get("RSPCOD")!=null&&result.get("RSPCOD").toString().equals("100006")){
					msg.what = 6;
				}
			}else{
				msg.what = -1;
			}
			Log.e("", " - - - "+	msg.what);
			loadingDialogWhole.dismiss();
			handler.sendMessage(msg);
			
		}
	};
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Log.e("", " - - - "+	msg.what);
			switch (msg.what) {
			case 0:
				if(result.get("login_url")!=null){
				URLs = result.get("login_url").toString();
				}
				webview();
				
				break;
			case 1:
				warnDialog = new OneButtonDialogWarn(
						ShouKuanBAvtivity.this, R.style.CustomDialog,
						"提示", result.get(Entity.RSPMSG).toString(),
						"确定", new OnMyDialogClickListener() {
							@Override
							public void onClick(View v) {
								Intent it = new Intent(ShouKuanBAvtivity.this,AuthenticationActivity1.class);
								startActivity(it);
								finish();
								warnDialog.dismiss();
							}
						});
				warnDialog.show();
				warnDialog.setCancelable(false);
				warnDialog.setCanceledOnTouchOutside(false);

				break;
			case 2:
				
				warnDialog = new OneButtonDialogWarn(
						ShouKuanBAvtivity.this, R.style.CustomDialog,
						"提示", result.get(Entity.RSPMSG).toString(),
						"确定", new OnMyDialogClickListener() {
							@Override
							public void onClick(View v) {
								Intent it = new Intent(ShouKuanBAvtivity.this,NewRealNameAuthenticationActivity.class);
								it.putExtra("intentObj","Skactivity");
								startActivity(it);
								finish();
								warnDialog.dismiss();
							}
						});
				warnDialog.show();
				warnDialog.setCancelable(false);
				warnDialog.setCanceledOnTouchOutside(false);
				break;
			case 3:
				warnDialog = new OneButtonDialogWarn(
						ShouKuanBAvtivity.this, R.style.CustomDialog,
						"提示", result.get(Entity.RSPMSG).toString(),
						"确定", new OnMyDialogClickListener() {
							@Override
							public void onClick(View v) {
								finish();
								warnDialog.dismiss();
							}
						});
				warnDialog.show();
				warnDialog.setCancelable(false);
				warnDialog.setCanceledOnTouchOutside(false);
				break;
			case 4:
				warnDialog = new OneButtonDialogWarn(
						ShouKuanBAvtivity.this, R.style.CustomDialog,
						"提示", result.get(Entity.RSPMSG).toString(),
						"确定", new OnMyDialogClickListener() {
							@Override
							public void onClick(View v) {
								Intent it = new Intent(ShouKuanBAvtivity.this,NewRealNameAuthenticationActivity.class);
								it.putExtra("intentObj","Skactivity");
								startActivity(it);
								finish();
								warnDialog.dismiss();
							}
						});
				warnDialog.show();
				warnDialog.setCancelable(false);
				warnDialog.setCanceledOnTouchOutside(false);
				break;
			case 5:
				warnDialog = new OneButtonDialogWarn(
						ShouKuanBAvtivity.this, R.style.CustomDialog,
						"提示", result.get(Entity.RSPMSG).toString(),
						"确定", new OnMyDialogClickListener() {
							@Override
							public void onClick(View v) {
								finish();
								warnDialog.dismiss();
							}
						});
				warnDialog.show();
				warnDialog.setCancelable(false);
				warnDialog.setCanceledOnTouchOutside(false);
				break;
			case 6:
				
				break;
			default:
				break;
			}
		};
	};
	
	
	
	class ShouKuanTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在加载...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {

			String[] values = { params[0], params[1] ,params[2] };
			return NetCommunicate.getAgentMidatc(HttpUrls.COLLECTIONTREASURE,
					values);

		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();

			if (result != null) {
				if (result.get(Entity.RSPCOD) != null
						&& result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					
					if(result.get("LOGINURL")!=null){
						String url ="" ;
						String mainCustomerNumber ="";
						String customerNumber = "";
						String target = "";
						String hmac  = "";
						if(result.get("LOGINURL")!=null){
							url = result.get("LOGINURL").toString();
						}
						if(result.get("MAINCUSTOMERNUMBER")!=null){
							 mainCustomerNumber = result.get("MAINCUSTOMERNUMBER").toString();
						}
						 if(result.get("CUSTOMERNUMBER")!=null){
							 customerNumber = result.get("CUSTOMERNUMBER").toString();
						 }
						 if(result.get("TARGET")!=null){
							 target = result.get("TARGET").toString();
						 }
						 if(result.get("HMAC")!=null){
							 hmac = result.get("HMAC").toString();
						 }
						
						https://skb.yeepay.com/skb-app/loginFromApp.action?mainCustomerNumber=10012593531&customerNumber=10012963667&target=all&hmac=8850d4ada11a6ae2bdcd43503c6341da
						URLs = url+"?mainCustomerNumber="+mainCustomerNumber+"&customerNumber="+customerNumber+"&target="+target+"&hmac="+hmac;
						Log.e("", ""+URLs);
					}
					webview();
				} else if(result.get(Entity.RSPCOD).equals("00444444")){
					warnDialog = new OneButtonDialogWarn(
							ShouKuanBAvtivity.this, R.style.CustomDialog,
							"提示", result.get(Entity.RSPMSG).toString(),
							"确定", new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									Intent it = new Intent(ShouKuanBAvtivity.this,NewRealNameAuthenticationActivity.class);
									it.putExtra("intentObj","Skactivity");
									startActivity(it);
									finish();
									warnDialog.dismiss();
								}
							});
					warnDialog.show();
					warnDialog.setCancelable(false);
					warnDialog.setCanceledOnTouchOutside(false);

				}else if(result.get(Entity.RSPCOD).equals("00555555")){
					warnDialog = new OneButtonDialogWarn(
							ShouKuanBAvtivity.this, R.style.CustomDialog,
							"提示", result.get(Entity.RSPMSG).toString(),
							"确定", new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									finish();
									warnDialog.dismiss();
								}
							});
					warnDialog.show();
					warnDialog.setCancelable(false);
					warnDialog.setCanceledOnTouchOutside(false);
				}else if(result.get(Entity.RSPCOD).equals("00666666")){
					warnDialog = new OneButtonDialogWarn(
							ShouKuanBAvtivity.this, R.style.CustomDialog,
							"提示", result.get(Entity.RSPMSG).toString(),
							"确定", new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									Intent it = new Intent(ShouKuanBAvtivity.this,NewRealNameAuthenticationActivity.class);
									it.putExtra("intentObj","Skactivity");
									startActivity(it);
									finish();
									warnDialog.dismiss();
								}
							});
					warnDialog.show();
					warnDialog.setCancelable(false);
					warnDialog.setCanceledOnTouchOutside(false);

				}else{
					warnDialog = new OneButtonDialogWarn(
							ShouKuanBAvtivity.this, R.style.CustomDialog,
							"提示", result.get(Entity.RSPMSG).toString(),
							"确定", new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									finish();
									warnDialog.dismiss();
								}
							});
					warnDialog.show();
					warnDialog.setCancelable(false);
					warnDialog.setCanceledOnTouchOutside(false);
				}
			} else {
				Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络连接",
						Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);

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
