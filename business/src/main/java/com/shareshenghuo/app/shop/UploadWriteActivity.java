package com.shareshenghuo.app.shop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.UserInfo;
import com.shareshenghuo.app.shop.network.request.PayChannelsResquest;
import com.shareshenghuo.app.shop.network.response.FileUploadResponse;
import com.shareshenghuo.app.shop.network.response.ImageResultResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.PictureUtil;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;


/***
 * 电子签名
 * 
 * @author liangge
 * 
 */
public class UploadWriteActivity extends BaseTopActivity {
	private LayoutParams p;
	// private DialogListener dialogListener;
	private TextView tv_content,tv_bala;
	static final int BACKGROUND_COLOR = Color.TRANSPARENT;
	static final int BRUSH_COLOR = Color.BLACK;
	PaintView mView;
	private TextView tv_pro;
	private File cardPicFile;
	private String idcardpic,mobile,balance,pcsimId,lognos;
	private RelativeLayout title;
	

	/** The index of the current color to use. */
	int mColorIndex;
	
	public String getStringDateMerge() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		String imgpash = "1"+mobile+dateString+getsixnumb();
		return imgpash;
	}
	
	private String getsixnumb(){
		Random random = new Random();
		String result="";
		for(int i=0;i<6;i++){
		result+=random.nextInt(10);
		}
		return result;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 3) {
//				Bitmap bitmap = mView.getCachebBitmap();
				
				Bitmap bitmap = shot(UploadWriteActivity.this);
				
				File dir = new File(Environment.getExternalStorageDirectory()+"/share/");
				if (!dir.exists()){
					dir.mkdirs();
				}
				
				String pathname = getStringDateMerge()+".jpg";
				
				String filepath = dir+pathname;
				
				FileOutputStream m_fileOutPutStream = null;
				
				Log.e("", "123456");
				cardPicFile = new File(filepath);
				idcardpic =pathname;
				Log.e("", ""+filepath);
				try {
					m_fileOutPutStream = new FileOutputStream(filepath);
				}catch (FileNotFoundException e) {
					e.printStackTrace();
				}
//				obmp = BitmapFactory.decodeFile(filepath, newOpts);
				
				bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
				bitmap.compress(CompressFormat.PNG, 100, m_fileOutPutStream);
				
				
				upPhotos(cardPicFile);
				
				try {
					m_fileOutPutStream.flush();
					m_fileOutPutStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	};
	
	
	public void upPhotos(File file) {
		try {
            // 压缩图片
            String compressPath = PictureUtil.compressImage(UploadWriteActivity.this, file.getPath(), file.getName(), 65);
            file = new File(compressPath);
        } catch(Exception e) {
            e.printStackTrace();
        }
		ProgressDialogUtil.showProgressDlg(UploadWriteActivity.this, "请稍候..");
		RequestParams params = new RequestParams();
		params.addBodyParameter("business_type", "posp");
		params.addBodyParameter("1", file);
		Log.e("", ""+UserInfoManager.getUserId(UploadWriteActivity.this)+"");
		Log.e("", ""+Api.URL_UPLOAD_FILE);
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPLOAD_FILE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String msg) {
				T.showNetworkError(UploadWriteActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				if(resp.statusCode==200 && resp.result!=null) {
					FileUploadResponse bean = new Gson().fromJson(resp.result, FileUploadResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						String URL= bean.data.get(0);
						rechange(URL);
					} else {  
						T.showShort(UploadWriteActivity.this, bean.result_desc);
					}
				}
			}

		});
	}
	

	private void rechange(String uRL) {
		// TODO Auto-generated method stub
		PayChannelsResquest req = new PayChannelsResquest();
		req.ACCOUNT = UserInfoManager.getUserInfo(this).shop_id+"";
		req.TERMTYPE = "1";
		req.ABSIMGPATH  = uRL;
		req.LOGNO = lognos;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.ELESIGN, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(UploadWriteActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				ImageResultResponse bean = new Gson().fromJson(resp.result, ImageResultResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					if(bean.data.RSPCOD.equals("000000")){
						Intent it = new Intent(UploadWriteActivity.this,QrcodeAct.class);
						it.putExtra("title","小票");
						it.putExtra("tag","0");
						it.putExtra("url",bean.data.IMGURL);
						startActivity(it);
						finish();
					}else{
						T.showShort(getApplicationContext(), bean.data.RSPMSG);
					}
				}else{
					T.showShort(getApplicationContext(), "请求失败");
				}
			}
		});
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
	         int statusBarHeight = frame.top+title.getHeight()+tv_pro.getHeight()+tv_bala.getHeight()+10;  
	         // 获取屏幕长和高  
	         int width = activity.getWindowManager().getDefaultDisplay().getWidth();  
	         int height = activity.getWindowManager().getDefaultDisplay().getHeight();  
	        // 去掉标题栏  
	            Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);  
	         Bitmap bs = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);  
	        view.destroyDrawingCache();  
	         return bs;  
	    }  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				  WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		requestWindowFeature(Window.FEATURE_PROGRESS);
		
		setContentView(R.layout.upload_sign_dialog);
		
		UserInfo userInfo = UserInfoManager.getUserInfo(this);
		mobile = userInfo.band_mobile;
		
//		tv_content = (TextView) findViewById(R.id.tv_title_contre);
//		tv_content.setText("电子签名");
		p = getWindow().getAttributes(); // ��ȡ�Ի���ǰ�Ĳ���ֵ
		// p.height = height;// (int) (d.getHeight() * 0.4); //�߶�����Ϊ��Ļ��0.4
		// p.width = width;// (int) (d.getWidth() * 0.6); //�������Ϊ��Ļ��0.6
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Rect rect = new Rect();
		View view = getWindow().getDecorView();
		view.getWindowVisibleDisplayFrame(rect);
		
		
		balance = getIntent().getStringExtra("balance");
		pcsimId  = getIntent().getStringExtra("pcsimId");
		lognos  = getIntent().getStringExtra("logno");
		
		p.width = metrics.widthPixels; // 得到屏幕的宽度
		p.height = metrics.heightPixels - rect.top; // 得到屏幕的长度
		getWindow().setAttributes(p); // ������Ч
		mView = new PaintView(this);
		final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.tablet_view);
		frameLayout.addView(mView);
		mView.requestFocus();
		Button btnClear = (Button) findViewById(R.id.btn_upload_dialog_clear);
		title = (RelativeLayout) findViewById(R.id.title);
		tv_pro = (TextView) findViewById(R.id.tv_pro);

		btnClear.setOnClickListener(new View.OnClickListener() {
					@Override
			public void onClick(View v) {
				// mView.clear();
				// frameLayout.setBackgroundResource(R.drawable.authentication_bg);
				frameLayout.removeAllViews();
				mView = new PaintView(UploadWriteActivity.this);
				frameLayout.addView(mView);
				mView.requestFocus();
			}
		});
		
		tv_bala = (TextView) findViewById(R.id.tv_bala);
		if(balance!=null){
			tv_bala.setText("支付金额: "+balance+"元");
		}
		
		Button btnOk = (Button) findViewById(R.id.btn_upload_dialog_ok);
		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(3);
			}
		});
	}

	/**
	 * This view implements the drawing canvas.
	 * 
	 * It handles all of the input events and drawing functions.
	 */
	class PaintView extends View {
		private Paint paint;
		private Canvas cacheCanvas;
		private Bitmap cachebBitmap;
		private Path path;

		public Bitmap getCachebBitmap() {
			return cachebBitmap;
		}

		public PaintView(Context context) {
			super(context);
			init();
		}

		private void init() {
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(3);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.BLACK);
			path = new Path();
			cachebBitmap = Bitmap.createBitmap(p.width, (int) (p.height),
					Config.ARGB_8888);
			cacheCanvas = new Canvas(cachebBitmap);
			cacheCanvas.drawColor(Color.TRANSPARENT);
		}

		public void clear() {
			if (cacheCanvas != null) {
				paint.setColor(BACKGROUND_COLOR);
				cacheCanvas.drawPaint(paint);
				paint.setColor(Color.BLACK);
				cacheCanvas.drawColor(Color.TRANSPARENT);
				invalidate();
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// canvas.drawColor(BRUSH_COLOR);
			canvas.drawBitmap(cachebBitmap, 0, 0, null);
			canvas.drawPath(path, paint);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {

			int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
			int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
			if (curW >= w && curH >= h) {
				return;
			}

			if (curW < w)
				curW = w;
			if (curH < h)
				curH = h;

			Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
					Bitmap.Config.ARGB_8888);
			Canvas newCanvas = new Canvas();
			newCanvas.setBitmap(newBitmap);
			if (cachebBitmap != null) {
				newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
			}
			cachebBitmap = newBitmap;
			cacheCanvas = newCanvas;
		}

		private float cur_x, cur_y;

		@Override
		public boolean onTouchEvent(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				cur_x = x;
				cur_y = y;
				path.moveTo(cur_x, cur_y);
				break;
			}

			case MotionEvent.ACTION_MOVE: {
				path.quadTo(cur_x, cur_y, x, y);
				cur_x = x;
				cur_y = y;
				break;
			}

			case MotionEvent.ACTION_UP: {
				cacheCanvas.drawPath(path, paint);
				path.reset();
				break;
			}
			}
			invalidate();
			return true;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			T.showShort(UploadWriteActivity.this, "必须要电子签名");
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
