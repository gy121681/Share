package com.shareshenghuo.app.user.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.util.UrlUtils;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageLoadManager {

	private static ImageLoadManager instance;
	
	private Context context;
	private DisplayImageOptions options;
	private DisplayImageOptions options1;
	 
	
	private ImageLoadManager(Context context) {
		this.context = context;
		initImageLoader();
	}
	private ImageLoadManager(Context context,boolean tag) {
		this.context = context;
		initImageLoader1();
	}
	
	private void initImageLoader1() {
	       //ImageLoaderConfiguration是针对图片缓存的全局配置，主要有线程类、缓存大小、磁盘大小、图片下载与解析、日志方面的配置。
	        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
	                context)
	                .memoryCacheExtraOptions(480, 800)
	                // max width, max height，即保存的每个缓存文件的最大长宽
	                .threadPoolSize(5)
	                // 线程池内加载的数量
	                .threadPriority(Thread.NORM_PRIORITY - 2)
	                .denyCacheImageMultipleSizesInMemory()
	                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
	                // You can pass your own memory cache
	                // 将保存的时候的URI名称用MD5 加密
	                .tasksProcessingOrder(QueueProcessingType.LIFO)
	                // 缓存的文件数量
	                // 自定义缓存路径
	                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
	                .imageDownloader(
	                        new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
	                .writeDebugLogs() // Remove for release app
	                .build();// 开始构建
	        ImageLoader.getInstance().init(config);
	        
			options = new DisplayImageOptions.Builder()  
	        .cacheInMemory(true)
	        .cacheOnDisc(true)
	        .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
	        .showImageOnFail(R.drawable.share_c_business_details_photo_default)
	        .showImageOnLoading(R.drawable.share_c_business_details_photo_default)
	        .showImageForEmptyUri(R.drawable.share_c_business_details_photo_default)
	        .build();  
		
	}
	
	
	
//	 public void loadImgByVolley(String imgUrl, final ImageView imageView) {
//		 if(mQueue==null){
//			 mQueue = Volley.newRequestQueue(context);
//		 }
//		 ImageRequest imgRequest = new ImageRequest(imgUrl,  new Response.Listener<Bitmap>(){
//
//			@Override
//			public void onResponse(Bitmap response) {
//				// TODO Auto-generated method stub
//				 imageView.setImageBitmap(response);
//			}
//			 
//		 }, 200, 200, Config.RGB_565, null);
//	       
//		 
//		 
//		 
////		 ImageRequest imgRequest = new ImageRequest(imgUrl,
////	                new Response.Listener<Bitmap>() {
////	            @Override
////	            public void onResponse(Bitmap arg0) {
////	                imageView.setImageBitmap(arg0);
////	            }
////	        }, 300, 200, Config.ARGB_8888, 
////	        new ErrorListener() {
////	            //加载失败
////	            @Override
////	            public void onErrorResponse(VolleyError arg0) {
////	                imageView.setImageResource(R.drawable.ic_launcher);
////	            }
////	        });
////	        //将图片加载放入请求队列中去
//	        mQueue.add(imgRequest);
//	    }
	
	public synchronized static ImageLoadManager getInstance(Context context) {
		if(instance == null)
			instance = new ImageLoadManager(context);
		return instance;
	}
//	public synchronized static ImageLoadManager getInstance(Context context,boolean tag) {
//		if(instance == null)
//			instance = new ImageLoadManager(context);
//		return instance;
//	}
	
	private void initImageLoader() {
		
//		   ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//	                context)
		   
//		    .memoryCacheExtraOptions(480, 800)          // default = device screen dimensions  
//		    .threadPoolSize(10)                          // default  
//		    .threadPriority(Thread.NORM_PRIORITY - 2)   // default  
//		    .tasksProcessingOrder(QueueProcessingType.LIFO) // default  
//		    .denyCacheImageMultipleSizesInMemory()  
//		    .memoryCache(new LruMemoryCache(2 * 1024 * 1024))  
//		    .memoryCacheSize(2 * 1024 * 1024)  
//		    .memoryCacheSizePercentage(13)              // default  
//		    .discCacheSize(50 * 1024 * 1024)        // 缓冲大小  
//		    .discCacheFileCount(100)                // 缓冲文件数目  
//		    .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default  
//		    .imageDownloader(new BaseImageDownloader(context)) // default  
//		    .imageDecoder(new BaseImageDecoder(false)) // default  
//		    .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default  
//		    .writeDebugLogs()  
//		    .build();// 开始构建
//	                .memoryCacheExtraOptions(480, 800)
	                // max width, max height，即保存的每个缓存文件的最大长宽
//	                .threadPoolSize(5)
//	                // 线程池内加载的数量
//	                .threadPriority(Thread.NORM_PRIORITY - 2)
//	                .denyCacheImageMultipleSizesInMemory()
//	                .memoryCache(new WeakMemoryCache()) 
////	                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
//	                // You can pass your own memory cache
//	                .memoryCacheSize(2 * 1024 * 1024) //缓存到内存的最大数据
//	                .discCacheSize(50 * 1024 * 1024)  //缓存到文件的最大数据
//	                .discCacheFileCount(50)            //文件数量
//	                // 将保存的时候的URI名称用MD5 加密
//	                .tasksProcessingOrder(QueueProcessingType.LIFO)
//	                // 缓存的文件数量
//	                // 自定义缓存路径
//	                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
//	                .imageDownloader(
//	                        new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
//	                .writeDebugLogs() // Remove for release app
//	                .build();// 开始构建
		ImageLoaderConfiguration config = new ImageLoaderConfiguration  
			    .Builder(context.getApplicationContext())  
			    .memoryCacheExtraOptions(480, 800) // 
			    .threadPoolSize(5)
			    .threadPriority(Thread.NORM_PRIORITY - 2)  
			    .denyCacheImageMultipleSizesInMemory()  
			    .memoryCache(new WeakMemoryCache()) 
//			    .memoryCache(new UsingFreqLimitedMemoryCache(20 * 1024 * 1024))
			    .memoryCacheSize(2 * 1024 * 1024)    
			    .discCacheSize(50 * 1024 * 1024)    
			    .tasksProcessingOrder(QueueProcessingType.LIFO)  
			    .discCacheFileCount(300) 
			    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
			    .writeDebugLogs() // Remove for release app  
			    .build();
		   ImageLoader.getInstance().init(config);
		
		options = new DisplayImageOptions.Builder()  
	        .cacheInMemory(true)
	        .cacheOnDisc(true)
	        .considerExifParams(true)    
	        .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//	        .showImageOnFail(R.drawable.share_c_business_details_photo_default)
//	        .showImageOnLoading(R.drawable.share_c_business_details_photo_default)
//	        .showImageForEmptyUri(R.drawable.share_c_business_details_photo_default)
	        .build();  
		
		options1 = new DisplayImageOptions.Builder()  
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//        .showImageOnFail(R.drawable.icon_53)
//        .showImageOnLoading(R.drawable.icon_53)
//        .showImageForEmptyUri(R.drawable.icon_53)
        .build();  
//		 ImageLoader.getInstance().init(config);
	}
	
	
	
	public void displayImage(String url, ImageView view) {
		ImageLoader.getInstance().displayImage(UrlUtils.getUrl(url), view, options);
//		if(url!=null&&url.contains("http")){
//			ImageLoader.getInstance().displayImage(url, view, options);
//		}else{
//			ImageLoader.getInstance().displayImage(Api.IMG_HOST+url, view, options);
//		}
		
	}
	
	public void displayfi(String url, ImageView view) {
		ImageLoader.getInstance().displayImage(UrlUtils.getUrl(url), view, options);
//		if(url!=null&&url.contains("http")){
////			loadImgByVolley(url, view);
//			ImageLoader.getInstance().displayImage(url, view, options);
//		}else{
////			loadImgByVolley(Api.IMG_HOST+url, view);
//			ImageLoader.getInstance().displayImage(Api.IMG_HOST+url, view, options);
//		}

	}

	
	
	public void userphoto(String url, ImageView view) {
		ImageLoader.getInstance().displayImage(UrlUtils.getUrl(url), view, options);
//		if(url!=null&&url.contains("http")){
//			ImageLoader.getInstance().displayImage(url, view, options1);
//		}else{
//			ImageLoader.getInstance().displayImage(Api.IMG_HOST+url, view, options1);
//		}
		
	}
	
	public void displayImageByNet(String url, ImageView view) {
		ImageLoader.getInstance().displayImage(url, view, options);
	}
	
	public void loadImage(String uri, ImageLoadingListener listener) {
		ImageLoader.getInstance().loadImage(UrlUtils.getUrl(uri), options, listener);
//		if(uri!=null&&uri.contains("http")){
//			ImageLoader.getInstance().loadImage(uri, options, listener);
//		}else{
//			ImageLoader.getInstance().loadImage(Api.IMG_HOST+uri, options, listener);
//		}
//		ImageLoader.getInstance().loadImage(Api.IMG_HOST+uri, options, listener);
	}
	
	public Bitmap loadImageSyn(String uri) {
		return ImageLoader.getInstance().loadImageSync(UrlUtils.getUrl(uri), options);
	}
}
