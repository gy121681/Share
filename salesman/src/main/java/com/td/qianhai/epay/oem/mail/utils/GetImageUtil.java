package com.td.qianhai.epay.oem.mail.utils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.td.qianhai.epay.oem.advertising.Image3DView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;


public class GetImageUtil {
	 private int mBorderColor = Color.parseColor("#f2f2f2"); 
	 static final int MEM_CACHE_DEFAULT_SIZE = 5 * 1024 * 1024;
	private int mBorderWidth = 2;  
	private ImageView headimg;
	private Context context;
	private String url;
	private Image3DView images;
	private Bitmap bitmap = null;
	private static LruCache<String, Bitmap> memCache;
	
	
	public GetImageUtil(Context context,ImageView imageview, String url) {
		// TODO Auto-generated constructor stub
		headimg = imageview;
		this.context  = context;
		this.url = url;
		 initMemCache();
		 new ImageLoadTask(context).execute(url);
		
	}
	
	
	   /**
     * 初始化内存缓存
     */
    private void initMemCache() {
        memCache = new LruCache<String, Bitmap>(MEM_CACHE_DEFAULT_SIZE) {
            @SuppressLint("NewApi") @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }
	public GetImageUtil(Context context,Image3DView image,String url,String nu) {
		// TODO Auto-generated constructor stub
		images = image;
		this.context  = context;
		this.url = url;
		new ImageLoadTask1(context).execute(url);
	}
	
	public GetImageUtil(Context context,ImageView image,String url,int nu) {
		// TODO Auto-generated constructor stub
		headimg = image;
		this.context  = context;
		this.url = url;
		new ImageLoadTask2(context).execute(url);
	}
	
	
	  public void loadImage(ImageView imageView, String imageUrl) {
	        // 先从内存中拿
	        Bitmap bitmap = getBitmapFromMem(imageUrl);

	        if (bitmap != null) {
	        	imageView.setImageBitmap(bitmap);
	        	Log.e("", "缓存");
	        }else{
	        	new ImageLoadTask(context).execute(url);
	        }
	  }
	  
	  public static Bitmap iscace(ImageView imageView, String imageUrl){
		  Bitmap bitmap = getBitmapFromMem(imageUrl);
	        if (bitmap != null) {
	        	return bitmap;
	        }else{
	        	return null;
	        }
		  
	  }
	  
	    public static Bitmap getBitmapFromMem(String url) {
	        return memCache.get(url);
	    }
	    
	    /**
	     * 加入到内存缓存中
	     * 
	     * @param url
	     * @param bitmap
	     */
	    public void putBitmapToMem(String url, Bitmap bitmap) {
	        memCache.put(url, bitmap);
	    }
	
	public class ImageLoadTask extends AsyncTask<String, Void, Void> {
		 
        public ImageLoadTask(Context context) {
        }

        @Override
        protected Void doInBackground(String... params) {
           String url = params[0];// 获取传来的参数，图片uri地址
                 bitmap = BitmapFactory.decodeStream(
                        HandlerData(url));
                Message msg = new Message();
                if(bitmap!=null){
//            		if (getbitmap(Imageurl)) {
//       			 bitmap = BitmapFactory.decodeFile(Imageurl);
                	msg.what = 1;
       			
                }else{
                    bitmap = BitmapFactory.decodeStream(
                            HandlerData(url));
                	msg.what = 1;
//	        			Resources res = context.getResources();
//	        			bitmap = BitmapFactory.decodeResource(res, R.drawable.head_portrait);
//	        			headimg.setImageBitmap(getRoundedCornerBitmap(bitmap));
                }
//                publishProgress(); // 通知去更新UI
                hander.sendMessage(msg);
            return null;
    }
        
        

        public void onProgressUpdate(Void... voids) {
                return;
        }
    }

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
    
    private Handler hander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           // button.setText(msg.getData().getString("data"));
            if(msg.what==1){
            	try {
           		 headimg.setImageBitmap(getRoundedCornerBitmap(bitmap));
           		putBitmapToMem(url, getRoundedCornerBitmap(bitmap));
				} catch (Exception e) {
					// TODO: handle exception
				}
            }
        }
    };
    
    
    public class ImageLoadTask1 extends AsyncTask<String, Void, Void> {
		 
        public ImageLoadTask1(Context context) {
        }

        @Override
        protected Void doInBackground(String... params) {
           String url = params[0];// 获取传来的参数，图片uri地址
                 bitmap = BitmapFactory.decodeStream(
                        HandlerData(url));
                Message msg = new Message();
                if(bitmap!=null){
//            		if (getbitmap(Imageurl)) {
//       			 bitmap = BitmapFactory.decodeFile(Imageurl);
                	msg.what = 1;
       			
                }else{
                	msg.what = 1;
//	        			Resources res = context.getResources();
//	        			bitmap = BitmapFactory.decodeResource(res, R.drawable.head_portrait);
//	        			headimg.setImageBitmap(getRoundedCornerBitmap(bitmap));
                }
//                publishProgress(); // 通知去更新UI
                hander1.sendMessage(msg);
            return null;
    }
        
        private Handler hander1 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
               // button.setText(msg.getData().getString("data"));
                if(msg.what==1){
                	try {
               		 images.setBackgroundResource1(bitmap);
    				} catch (Exception e) {
    					// TODO: handle exception
    				}
                }
            }
        };
        
        

        public void onProgressUpdate(Void... voids) {
                return;
        }
    }
  
    
    /**
	 * 圆形图片裁剪
	 * @param bitmap
	 * @return
	 */
	private  Bitmap getRoundedCornerBitmap(Bitmap bitmap1) {
		Bitmap bitmap = centerSquareScaleBitmap(bitmap1,125);
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
	
	
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength)
	  {
	   if(null == bitmap || edgeLength <= 0)
	   {
	    return  null;
	   }
	                                                                                  
	   Bitmap result = bitmap;
	   int widthOrg = bitmap.getWidth();
	   int heightOrg = bitmap.getHeight();
	                                                                                  
	   if(widthOrg > edgeLength && heightOrg > edgeLength)
	   {
	    //压缩到一个最小长度是edgeLength的bitmap
	    int longerEdge = (int)(edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
	    int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
	    int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
	    Bitmap scaledBitmap;
	                                                                                   
	          try{
	           scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
	          }
	          catch(Exception e){
	           return null;
	          }
	                                                                                        
	       //从图中截取正中间的正方形部分。
	       int xTopLeft = (scaledWidth - edgeLength) / 2;
	       int yTopLeft = (scaledHeight - edgeLength) / 2;
	                                                                                      
	       try{
	        result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
	        scaledBitmap.recycle();
	       }
	       catch(Exception e){
	        return null;
	       }       
	   }
	                                                                                       
	   return result;
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
//        canvas.drawCircle(width >> 1, height >> 1, (width - mBorderWidth) >> 1, mBorderPaint);  
        canvas = null;  
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
//            		if (getbitmap(Imageurl)) {
//                	headimg.setImageBitmap(bitmap);
//       			 bitmap = BitmapFactory.decodeFile(Imageurl);
                	msg.what = 1;
       			
                }else{
                    bitmap = BitmapFactory.decodeStream(
                            HandlerData(url));
                	msg.what = 1;
//	        			Resources res = context.getResources();
//	        			bitmap = BitmapFactory.decodeResource(res, R.drawable.head_portrait);
//	        			headimg.setImageBitmap(bitmap);
                }
//                publishProgress(); // 通知去更新UI
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
           		 headimg.setImageBitmap(bitmap);
				} catch (Exception e) {
					// TODO: handle exception
				}
            }
        }
    };
}
