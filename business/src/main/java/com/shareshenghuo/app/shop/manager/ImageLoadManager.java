package com.shareshenghuo.app.shop.manager;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.networkapi.Api;

public class ImageLoadManager {

	private static ImageLoadManager instance;
	
	private static Context context;
	private DisplayImageOptions options;
	private DisplayImageOptions options1;
	private static BitmapUtils bitmapUtils;
	
	private ImageLoadManager(Context context) {
		this.context = context;
		initImageLoader();
	}
	
	public synchronized static ImageLoadManager getInstance(Context context) {
		if(instance == null)
			instance = new ImageLoadManager(context);
		return instance;
	}
	
	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration  
			    .Builder(context.getApplicationContext())  
			    .memoryCacheExtraOptions(480, 800) // 
			    .threadPoolSize(5)
			    .threadPriority(Thread.NORM_PRIORITY - 2)  
			    .denyCacheImageMultipleSizesInMemory()  
			    .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
			    .memoryCacheSize(2* 1024 * 1024)    
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
        .showImageOnFail(R.drawable.share_b_public_default_pic)
        .build();  
		
		
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)  
//        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions  
//        .diskCacheExtraOptions(480, 800, null)  
//        .threadPoolSize(1) // default  
//        .threadPriority(Thread.NORM_PRIORITY - 1) // default  
//        .tasksProcessingOrder(QueueProcessingType.FIFO) // default  
//        .denyCacheImageMultipleSizesInMemory()  
//        .memoryCache(new LruMemoryCache(30 * 1024 * 1024))  
//        .memoryCacheSize(2 * 1024 * 1024)  
//        .memoryCacheSizePercentage(13) // default  
//        .diskCacheSize(50 * 1024 * 1024)  
//        .diskCacheFileCount(100)  
//        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default  
//        .imageDownloader(new BaseImageDownloader(context)) // default  
//        .imageDecoder(new BaseImageDecoder(false)) // default  
//        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default  
//        .writeDebugLogs()  
//        .build(); 
//		ImageLoader.getInstance().init(config);
		
//		options = new DisplayImageOptions.Builder()  
//        .cacheInMemory(true)
//        .cacheOnDisc(true)
//        .bitmapConfig(Bitmap.Config.RGB_565)
//        .showImageOnFail(R.drawable.defult_bg)
//        .showImageOnLoading(R.drawable.defult_bg)
//        .showImageForEmptyUri(R.drawable.defult_bg)
//        .build();  
		

		options1 = new DisplayImageOptions.Builder()  
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .showImageOnFail(R.drawable.share_b_mine_head_moren)
        .build();  
	}

	public void displayImage(String url, ImageView view) {
		if(url!=null&&url.contains("http")){
			ImageLoader.getInstance().displayImage(url, view, options);
		}else{
			ImageLoader.getInstance().displayImage(Api.HOSTERMA+url, view, options);
		}
	}

	public void displayHeadIconImage(String url, ImageView view) {
		if(url!=null&&url.contains("http")){
			ImageLoader.getInstance().displayImage(url, view, options1);
		}else{
			ImageLoader.getInstance().displayImage(Api.HOSTERMA+url, view, options1);
		}
	}
	
	
//	public  void userphoto(String url, ImageView view) {
//		if(bitmapUtils==null){
//			 bitmapUtils = new BitmapUtils(context);
//		}
//		bitmapUtils.configDefaultLoadingImage(R.drawable.defult_bg);  
//		bitmapUtils.display(view, Api.HOSTERMA+url);
//		 bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);  
		
		
//		FinalBitmap.create(context).configBitmapLoadThreadSize(3);
//        FinalBitmap.create(context).configMemoryCachePercent(0.5f);
//		FinalBitmap.create(context).display(view,
//				Api.HOSTERMA+url,
//				view.getWidth(),
//				view.getHeight(), null, null);
//		if(TextUtils.isEmpty(url))
//			return;
//		try {
//			ImageLoader.getInstance().displayImage(Api.HOSTERMA+url, view, options);
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
//	}
	
	public void displayImagenohost(String url, ImageView view) {
		if(TextUtils.isEmpty(url))
			return;
		try {
			ImageLoader.getInstance().displayImage(url, view, options);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
//	public void displayImage(String url, ImageView view) {
//		if(TextUtils.isEmpty(url))
//			return;
//		ImageLoader.getInstance().displayImage(Api.HOST+url, view, options);
//	}
	
	public void loadImage(String uri, ImageLoadingListener listener) {
		if(TextUtils.isEmpty(uri))
			return;
		
		if(uri!=null&&uri.contains("http")){
			ImageLoader.getInstance().loadImage(uri, options, listener);
		}else{
			ImageLoader.getInstance().loadImage(Api.HOSTERMA+uri, options, listener);
		}
		
	}
	
	public Bitmap loadImageSyn(String uri) {
		if(TextUtils.isEmpty(uri))
			return null;
		return ImageLoader.getInstance().loadImageSync(Api.HOSTERMA+uri, options);
	}
}
