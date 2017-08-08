package com.td.qianhai.epay.oem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.DESKey;
import com.td.qianhai.epay.oem.mail.utils.EncodingHandler;
import com.td.qianhai.epay.oem.mail.utils.GetImageUtil;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class MyPromotionActivity extends BaseActivity {

	private String mobile, username, getusername,personpic;
	private ImageView qr_img,user_headimg;
	private TextView my_name, my_phone;
	private String Imageurl = Environment.getExternalStorageDirectory().getAbsolutePath() +"/myImage/1.jpg";
	private int mBorderColor = Color.parseColor("#f2f2f2"); 
	private int mBorderWidth = 2;  
	private OneButtonDialogWarn warnDialog;
	private RelativeLayout title;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mypromotion_activity);
		AppContext.getInstance().addActivity(this);
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		username = ((AppContext) getApplication()).getUsername();
		personpic = MyCacheUtil.getshared(this).getString("PERSONPIC", "");
		initview();
		setfirstimg();
	}

	private void initview() {
		// TODO Auto-generated method stub
		title = (RelativeLayout) findViewById(R.id.title_s);
		((TextView) findViewById(R.id.bt_title_right)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.bt_title_right1)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.bt_title_right1)).setText("保存");
		((TextView) findViewById(R.id.tv_title_contre)).setText("我的推广二维码");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		user_headimg = (ImageView) findViewById(R.id.user_headimg);
		qr_img = (ImageView) findViewById(R.id.myqrcode_img);
		my_phone = (TextView) findViewById(R.id.my_phone);
		my_name = (TextView) findViewById(R.id.my_name);
		
//		if (username != null && !username.equals("")) {
//			my_name.setText(username);
//		} else {
		final BussinessInfoTask task = new BussinessInfoTask();
		task.execute(HttpUrls.BUSSINESS_INFO + "", mobile, "4", "0");
//		}
		my_phone.setText(mobile);
		initqr();
		
		findViewById(R.id.bt_title_right1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			
				
//				Bitmap obmp = convertViewToBitmap(myqrcode_imgs);
				
//				BitmapFactory.Options newOpts = new BitmapFactory.Options();    
//				newOpts.outWidth = 400;
//				newOpts.outHeight = 600;
//				  newOpts.inSampleSize = 1;
//				MediaStore.Images.Media.insertImage(getContentResolver(), obmp, "title", "description");
				
				FileOutputStream m_fileOutPutStream = null;
				String filepath = Environment.getExternalStorageDirectory() +File.separator
						+ "myqrcode.png";
				Log.e("", ""+filepath);
				try {
					m_fileOutPutStream = new FileOutputStream(filepath);
				}catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				
//				obmp = BitmapFactory.decodeFile(filepath, newOpts);
//				bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
				
				Bitmap mp = shot(MyPromotionActivity.this);
				mp.compress(CompressFormat.PNG, 100, m_fileOutPutStream);
				try {
					m_fileOutPutStream.flush();
					m_fileOutPutStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				MediaStore.Images.Media.insertImage(getContentResolver(), mp, "title", "description");
//				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ filepath))); 
				
				warnDialog = new OneButtonDialogWarn(MyPromotionActivity.this,
						R.style.CustomDialog, "提示", "保存成功,可在相册查看", "确定",
						new OnMyDialogClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								warnDialog.dismiss();
							}
						});
				warnDialog.show();

			}
		});
		
	}
	
	
	private void setfirstimg(){
		if(personpic!=null){
			new GetImageUtil(this, user_headimg,HttpUrls.HOST_POSM+personpic);
		}
		
//		Bitmap bitmap = null;
//		
//		if (getbitmap(Imageurl)) {
//			 bitmap = BitmapFactory.decodeFile(Imageurl);
//			 user_headimg.setImageBitmap(getRoundedCornerBitmap(bitmap));
//		}else{
//			
//		}
		
	}
	
	private Bitmap shot(Activity activity) {   
	       //View是你需要截图的View   
	         View view = activity.getWindow().getDecorView();   
	         view.setDrawingCacheEnabled(true);   
	         view.buildDrawingCache();   
	         Bitmap b1 = view.getDrawingCache();   
	       // 获取状态栏高度 /  
	           Rect frame = new Rect();  
	         activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
	         int statusBarHeight = frame.top+title.getHeight();  
	         // 获取屏幕长和高  
	         int width = activity.getWindowManager().getDefaultDisplay().getWidth();  
	         int height = activity.getWindowManager().getDefaultDisplay().getHeight();  
	        // 去掉标题栏  
	            Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);  
	         Bitmap bs = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);  
	        view.destroyDrawingCache();  
	         return bs;  
	    }  
	
	
	
	
	
	private  Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        //保证是方形，并且从中心画
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int w;
        int deltaX = 0;
        int deltaY = 0;
        if (width <= height) {
            w = width;
            deltaY = height - w;
        } else {
            w = height;
            deltaX = width - w;
        }
        final Rect rect = new Rect(deltaX, deltaY, w, w);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        //圆形，所有只用一个
        
        int radius = (int) (Math.sqrt(w * w * 2.0d) / 2);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        drawBorder(canvas, width, height); 
        return output;
    }
	
	private void drawBorder(Canvas canvas, final int width, final int height) {  
        if (mBorderWidth == 0) {  
            return;  
        }  
        final Paint mBorderPaint = new Paint();  
        mBorderPaint.setStyle(Paint.Style.STROKE);  
        mBorderPaint.setAntiAlias(true);  
        mBorderPaint.setColor(mBorderColor);  
        mBorderPaint.setStrokeWidth(mBorderWidth);  
        /** 
         * 坐标x：view宽度的一般 坐标y：view高度的一般 半径r：因为是view的宽度-border的一半 
         */  
        canvas.drawCircle(width >> 1, height >> 1, (width - mBorderWidth) >> 1, mBorderPaint);  
        canvas = null;  
    }  
	
	private boolean getbitmap(String path){
		File mFile=new File(path);
        //若该文件存在
        if (mFile.exists()) {
        	return true;
        }
        return false;
	}

	private void initqr() {
		// aa = DESKey.AES_Encode(jsonObj.toString(),
		// "f15f1ede25a2471998ee06edba7d2e29");
	
			// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
			String translation = null;
			try {

				Bitmap qrCodeBitmap = null;
				// String aa = DESKey.encrypt("bgg", translation);
				String bb = ((AppContext) getApplication()).getAecuserinfo();
				if (bb != null && !bb.equals("")) {
					qrCodeBitmap = EncodingHandler.createQRCode(
							HttpUrls.SHEARURL + bb, 500);
				} else {
					try {
						translation = DESKey.AES_Encode(mobile,
								"f15f1ede25a2471998ee06edba7d2e29");
					} catch (InvalidKeyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchPaddingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidAlgorithmParameterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalBlockSizeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BadPaddingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					String aa = new AES().encrypt(translation.getBytes());

					((AppContext) getApplication()).setAecuserinfo(translation);
					qrCodeBitmap = EncodingHandler.createQRCode(
							HttpUrls.SHEARURL + translation, 500);
				}

				// Bitmap logoBmp = BitmapFactory.decodeResource(getResources(),
				// R.drawable.transaction);
				// //二维码和logo合并
				// Bitmap bitmap = Bitmap.createBitmap(qrCodeBitmap.getWidth(),
				// qrCodeBitmap
				// .getHeight(), qrCodeBitmap.getConfig());
				// Canvas canvas = new Canvas(bitmap);
				// //二维码
				// canvas.drawBitmap(qrCodeBitmap, 0,0, null);
				// //logo绘制在二维码中央
				// canvas.drawBitmap(logoBmp, qrCodeBitmap.getWidth() / 2
				// - logoBmp.getWidth() / 2, qrCodeBitmap.getHeight()
				// / 2 - logoBmp.getHeight() / 2, null);

				qr_img.setImageBitmap(qrCodeBitmap);

			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



	}

	class BussinessInfoTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog("加载中...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate.get(HttpUrls.BUSSINESS_INFO, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					if (result.get("ACTNAM") != null) {
						getusername = result.get("ACTNAM").toString();

					} else {
						getusername = "未实名认证用户";
					}
					my_name.setText(getusername);
					AppContext.getInstance().setUsername(getusername);
					// ((AppContext)con.getApplication()).setUsername(result.get("ACTNAM"));

					// showView(result);
				} else {
					Toast.makeText(getApplicationContext(), "商户信息获取失败",
							Toast.LENGTH_SHORT).show();
					// ToastCustom.showMessage(MyQrcard.this, "商户信息获取失败");
					finish();
				}
			}
			super.onPostExecute(result);
		}
	}
}
