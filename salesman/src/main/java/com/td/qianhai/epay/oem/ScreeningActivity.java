package com.td.qianhai.epay.oem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class ScreeningActivity extends BaseActivity{
	
	private String mobile,shopName;
	private LinearLayout lin_edit;
	private EditText edit;
	private TextView btn_edit,btn_scree;
	private RelativeLayout re_qrcode;
	private ImageView myqrcode_imgs,imgs_codes;
	private HashMap<String, Object> result = null; 
	private HashMap<String, Object> result1 = null; 
	private OneButtonDialogWarn warnDialog;
	private Bitmap bitmap;
	private ArrayAdapter<String> popadapter;
	private List<String> list ;
	private View popview;
	private PopupWindow pop;  
    float scaleWidth;  
    float scaleHeight; 
    private boolean ishowin = false;
    private RelativeLayout re_codes,re_imgs;
    private WebView wb_epos;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.activity_screening);
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		initview();
		webview();
		load();
		
	}

	private void load() {
		// TODO Auto-generated method stub
		showLoadingDialog("请稍候...");
		new Thread(run).start();
		
	}
	
	private void webview() {
		wb_epos = (WebView) findViewById(R.id.wv_epos);
		wb_epos.getSettings().setSupportMultipleWindows(true);
		wb_epos.getSettings().setJavaScriptEnabled(true);
		wb_epos.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		wb_epos.getSettings().setDomStorageEnabled(true);
		wb_epos.getSettings().setSupportZoom(true);
		wb_epos.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		wb_epos.getSettings().setBuiltInZoomControls(true);//support zoom
		wb_epos.getSettings().setUseWideViewPort(true);// 这个很关键
		wb_epos.getSettings().setLoadWithOverviewMode(true);
		wb_epos.loadUrl(HttpUrls.VADIOURL);
		// 监听有需要再修改
		OnlineWebViewClient viewClient = new OnlineWebViewClient();
		
		wb_epos.setWebChromeClient(new WebChromeClient());
		wb_epos.setWebViewClient(viewClient);
		
	}
	@Override
	protected void onPause ()
	{
		wb_epos.reload ();
	 
	    super.onPause ();
	}

	
	private void initview() {
		// TODO Auto-generated method stub
		initpop();
		
		((TextView) findViewById(R.id.tv_title_contre)).setText("商家收款码");
		((TextView) findViewById(R.id.bt_title_right2)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.bt_title_right)).setVisibility(View.GONE);
//		((TextView) findViewById(R.id.bt_title_right1)).setText("查询");
		re_codes = (RelativeLayout) findViewById(R.id.re_codes);
		imgs_codes = (ImageView) findViewById(R.id.imgs_codes);
		re_imgs = (RelativeLayout) findViewById(R.id.re_imgs);
		findViewById(R.id.bt_title_right2).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
//				if(!ishowin){
//					ishowin = true;
//					  pop.showAsDropDown(v);  
//
////					 pop.setFocusable(false);
//				}else{
//					ishowin = false;
//					 pop.dismiss();  
////					  pop.setFocusable(true);
//				}
	            if (pop.isShowing()) {  
	                pop.dismiss();  
	                
	            } else {  
	                pop.showAsDropDown(v);  
	            }
			}
		});
		
		

//		findViewById(R.id.bt_title_right1).setOnClickListener(
//				new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						String subMchId = "";
////						if(result.get("subMchId")!=null){
////							 subMchId = result.get("subMchId").toString();
////						}
//						if(result.get("is_subbranch")!=null){
//							 subMchId = result.get("is_subbranch").toString();
//						}
//						Intent intent = new Intent();
//						intent.putExtra("subMchId", subMchId);
//						intent.setClass(ScreeningActivity.this, WechatCollectionRecordActivity.class);//
//						startActivity(intent);
//						
//					}
//				});
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		lin_edit = (LinearLayout) findViewById(R.id.lin_edit);
		edit = (EditText) findViewById(R.id.edit);
		btn_edit = (TextView) findViewById(R.id.btn_edit);
		btn_scree = (TextView) findViewById(R.id.btn_scree);
		re_qrcode = (RelativeLayout) findViewById(R.id.re_qrcode);
		myqrcode_imgs = (ImageView) findViewById(R.id.myqrcode_imgs);
		
		
		
		myqrcode_imgs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					re_codes.setVisibility(View.VISIBLE);
					imgs_codes.setImageBitmap(bitmap);
			}
		});
		re_codes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				re_codes.setVisibility(View.GONE);
			}
		});
		
		edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()>=1){
					btn_edit.setEnabled(true);
				}else{
					btn_edit.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btn_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SpannableString msp = new SpannableString("您的商户名是:"+edit.getText().toString());
				showDoubleWarnDialog(msp);
				
			}
		});
		btn_scree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			
				Bitmap obmp  = getViewBitmap(re_imgs);
//				Bitmap obmp = convertViewToBitmap(re_imgs);
				
//				BitmapFactory.Options newOpts = new BitmapFactory.Options();    
//				newOpts.outWidth = 400;
//				newOpts.outHeight = 600;
//				  newOpts.inSampleSize = 1;
			
				
				FileOutputStream m_fileOutPutStream = null;
				String filepath = Environment.getExternalStorageDirectory() +File.separator
						+ "mywechatqrcode.png";
				Log.e("", ""+filepath);
				try {
					m_fileOutPutStream = new FileOutputStream(filepath);
				}catch (FileNotFoundException e) {
					e.printStackTrace();
				}
//				bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
//				bitmap.compress(CompressFormat.PNG, 100, m_fileOutPutStream);
				obmp.createBitmap(obmp.getWidth(), obmp.getHeight(), obmp.getConfig());
				obmp.compress(CompressFormat.PNG, 100, m_fileOutPutStream);
				
				try {
					m_fileOutPutStream.flush();
					m_fileOutPutStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				MediaStore.Images.Media.insertImage(getContentResolver(), obmp, "title", "description");
				warnDialog = new OneButtonDialogWarn(ScreeningActivity.this,
						R.style.CustomDialog, "提示", "保存成功", "确定",
						new OnMyDialogClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								warnDialog.dismiss();
								myqrcode_imgs.setDrawingCacheEnabled(false);
							}
						});
				warnDialog.show();

			}
		});
	}
	
	
	private void initpop() {
		// TODO Auto-generated method stub
		list = new ArrayList<String>();

		popadapter = new ArrayAdapter<String>(this, R.layout.pop_name,list);
		
		popview = this.getLayoutInflater().inflate(R.layout.currency_pop,null);  
		
		ListView listview = (ListView) popview.findViewById(R.id.poplist);
		listview.setAdapter(popadapter);
		
        pop = new PopupWindow(popview, ViewGroup.LayoutParams.FILL_PARENT,  
                ViewGroup.LayoutParams.WRAP_CONTENT,true);  
//		pop.setTouchable(true);
//		pop.setOutsideTouchable(true);
//		pop.update();
//		pop.setTouchable(true);
//		pop.setOutsideTouchable(true);
//		pop.setFocusable(true);
//		pop.update();
        
        listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long position) {
				// TODO Auto-generated method stub
				if((int)position==0){
					String subMchId = "";
					if (result.get("is_subbranch") != null) {
						subMchId = result.get("is_subbranch").toString();
					}
					Intent intent = new Intent();
					intent.putExtra("subMchId", subMchId);
					intent.setClass(ScreeningActivity.this,WechatCollectionRecordActivity.class);//
					startActivity(intent);
				}else if((int)position==1){
					String subMchId = "";
					if (result.get("is_subbranch") != null) {
						subMchId = result.get("is_subbranch").toString();
					}
						Intent intent = new Intent();
						intent.putExtra("subMchId", subMchId);
						intent.setClass(ScreeningActivity.this,AlipayCollectionRecordActivity.class);//
						startActivity(intent);
					
				}else if((int)position==2){
					Intent intent = new Intent();
					intent.putExtra("shopName", shopName);
					intent.setClass(ScreeningActivity.this,BranchManagementActivity.class);//
					startActivity(intent);
				}
				ishowin = false;
				pop.dismiss();
			}
		});
	}

	public Bitmap convertViewToBitmap(View view)  
	{  
//	    view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));  
//	    view.layout(view.getPaddingLeft(), view.getPaddingLeft(), view.getMeasuredWidth(), view.getMeasuredHeight());  
//	    view.buildDrawingCache();  
//	    Bitmap bitmap = view.getDrawingCache();  
	  
	    view.setDrawingCacheEnabled(true);  
	    Bitmap bitmap = view.getDrawingCache();  
	    view.setDrawingCacheEnabled(false);  
	    
	    return bitmap;  
	} 
	
	Runnable run = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = null;
			
			try {
				
				String[] values = {mobile};
				result = NetCommunicate.executeHttpPostnulls(HttpUrls.SCREENINGURL,
						HttpKeys.SCREENINGURL_BACK,HttpKeys.SCREENINGURL_ASK,values);
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
				if(result.get("flag")!=null&&result.get("flag").toString().equals("00")){
					msg.what = 1;
				}else if(result.get("flag")!=null&&result.get("flag").toString().equals("03")){
					msg.what = 2;
				}else{
					msg.what = 5;
				}
			}else{
				msg.what = 5;
			}
			loadingDialogWhole.dismiss();
			handler.sendMessage(msg);
			
		}
	};
	
	private Handler handler = new Handler() {
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case 1:
			re_qrcode.setVisibility(View.VISIBLE);
			lin_edit.setVisibility(View.GONE);
			if(result.get("qrcode_url")!=null){
				setimg(result.get("qrcode_url").toString());
			}
			String subMchId = result.get("is_subbranch").toString();
			shopName = result.get("shopName").toString();
			if(subMchId.equals("01")){
				list.add("微信流水");
				list.add("支付宝流水");
				list.add("分店管理");
			}else{
				list.add("微信流水");
				list.add("支付宝流水");
			}
			break;
		case 2:
			lin_edit.setVisibility(View.VISIBLE);
			
			break;
		case 5:
			((TextView) findViewById(R.id.bt_title_right2)).setVisibility(View.GONE);
			wb_epos.setVisibility(View.VISIBLE);
			
//			if(result!=null){
//				warnDialog = new OneButtonDialogWarn(ScreeningActivity.this,
//						R.style.CustomDialog, "提示", result.get("message").toString(), "确定",
//						new OnMyDialogClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//								finish();
//								warnDialog.dismiss();
//							}
//						});
//				warnDialog.show();
//			}

			break;
		default:
			break;
		}
	};
};

	@Override
	protected void doubleWarnOnClick(View v) {
		// TODO Auto-generated method stub
		super.doubleWarnOnClick(v);
		switch (v.getId()) {
		case R.id.btn_left:
			doubleWarnDialog.dismiss();
			break;
		case R.id.btn_right:
			doubleWarnDialog.dismiss();
			load1();
			break;

		default:
			break;
		}
	}
	
	
	private void load1() {
		// TODO Auto-generated method stub
		showLoadingDialog("请稍候...");
		new Thread(run1).start();
	}


	Runnable run1 = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = null;
			
			try {
				
				String[] values = {mobile,edit.getText().toString()};
				result1 = NetCommunicate.executeHttpPostnulls(HttpUrls.SCREENINGURLFA,
						HttpKeys.SCREENINGURLFA_BACK,HttpKeys.SCREENINGURLFA_ASK,values);
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
			if(result1!=null){
				if(result1.get("flag")!=null&&result1.get("flag").toString().equals("00")){
					msg.what = 1;
				}else if(result1.get("flag")!=null&&result1.get("flag").toString().equals("03")){
					msg.what = 2;
				}else{
					msg.what = 5;
				}
			}else{
				msg.what = 5;
			}
			loadingDialogWhole.dismiss();
			handler1.sendMessage(msg);
			
		}
	};
	
	private Handler handler1 = new Handler() {
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case 1:

			
			warnDialog = new OneButtonDialogWarn(ScreeningActivity.this,
					R.style.CustomDialog, "提示", result1.get("message").toString(), "确定",
					new OnMyDialogClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							warnDialog.dismiss();
							lin_edit.setVisibility(View.GONE);
							re_qrcode.setVisibility(View.VISIBLE);
							result1 = null;
							load();
						}
					});
			warnDialog.show();
			
			break;
		case 2:
			warnDialog = new OneButtonDialogWarn(ScreeningActivity.this,
					R.style.CustomDialog, "提示", result1.get("message").toString(), "确定",
					new OnMyDialogClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							warnDialog.dismiss();
						}
					});
			warnDialog.show();
			break;
		case 5:
			warnDialog = new OneButtonDialogWarn(ScreeningActivity.this,
					R.style.CustomDialog, "提示", result1.get("message").toString(), "确定",
					new OnMyDialogClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							finish();
							warnDialog.dismiss();
						}
					});
			warnDialog.show();
			break;
		default:
			break;
		}
	}
};

private void setimg(String url) {
	// TODO Auto-generated method stub
	
//    Bitmap bitmap = BitmapFactory.decodeStream(
//            HandlerData(url));
//    myqrcode_imgs.setImageBitmap(bitmap);
	new ImageLoadTask2(this).execute(url);
//	FinalBitmap.create(this).display(myqrcode_imgs,
//			url,
//			myqrcode_imgs.getWidth(),
//			myqrcode_imgs.getHeight(), null, null);
	
	}

public class ImageLoadTask2 extends AsyncTask<String, Void, Void> {
	 
    public ImageLoadTask2(Context context) {
    }

    @Override
    protected Void doInBackground(String... params) {
       String url = params[0];// 获取传来的参数，图片uri地址
             bitmap = BitmapFactory.decodeStream(
                    HandlerData(url));
            Message msg = new Message();
            if(bitmap!=null){
//        		if (getbitmap(Imageurl)) {
//            	headimg.setImageBitmap(bitmap);
//   			 bitmap = BitmapFactory.decodeFile(Imageurl);
            	msg.what = 1;
   			
            }else{
                bitmap = BitmapFactory.decodeStream(
                        HandlerData(url));
            	msg.what = 1;
//        			Resources res = context.getResources();
//        			bitmap = BitmapFactory.decodeResource(res, R.drawable.head_portrait);
//        			headimg.setImageBitmap(bitmap);
            }
//            publishProgress(); // 通知去更新UI
            hander2.sendMessage(msg);
        return null;
}
    
    

    public void onProgressUpdate(Void... voids) {
            return;
    }
}

private Handler hander2 = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
       // button.setText(msg.getData().getString("data"));
        if(msg.what==1){
        	try {
        		myqrcode_imgs.setImageBitmap(bitmap);
			} catch (Exception e) {
				// TODO: handle exception
			}
        }
    }
};

public static InputStream HandlerData(String url) {
    InputStream inStream = null;

    try {
        URL feedUrl = new URL(url);
        URLConnection conn = feedUrl.openConnection();
        conn.setConnectTimeout(10 * 1000);
        inStream = conn.getInputStream();
    } catch (Exception e) {
       e.printStackTrace();
   }

    return inStream;
}


public class OnlineWebViewClient extends WebViewClient {
	
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		
//        if( url!=null ) {  
//            return false;  
//        }  

        // Otherwise allow the OS to handle things like tel, mailto, etc.  
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));  
//        startActivity( intent );  
//        return true;  
        
        if (url.startsWith("tel:")) { 
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url)); 
            startActivity(intent); 
            } else{
            	 view.loadUrl(url);
            }
   
    return true;
//		view.loadUrl(url);
//		return true;
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		// TODO Auto-generated method stub
		super.onPageFinished(view, url);
		wb_epos.getSettings().setBlockNetworkImage(false);
	}
	
	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler,
			SslError error) {
		// TODO Auto-generated method stub
		handler.proceed();
		}
	}

	public static Bitmap getViewBitmap(View v) {

	 v.clearFocus(); //

	 v.setPressed(false); //

	 // 能画缓存就返回false

	 boolean willNotCache = v.willNotCacheDrawing();

	 v.setWillNotCacheDrawing(false);

	 int color = v.getDrawingCacheBackgroundColor();

	 v.setDrawingCacheBackgroundColor(0);

	 if (color != 0) {

	 v.destroyDrawingCache();

	 }

	 v.buildDrawingCache();

	 Bitmap cacheBitmap = v.getDrawingCache();

	 if (cacheBitmap == null) {

	 return null;

	 }

	 Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

	 // Restore the view

	 v.destroyDrawingCache();

	 v.setWillNotCacheDrawing(willNotCache);

	 v.setDrawingCacheBackgroundColor(color);

	 return bitmap;

	 }

}
