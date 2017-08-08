package com.td.qianhai.epay.oem.mail.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.beans.HttpUrls;

public class ImageLoadManager {

	private static ImageLoadManager instance;
	
	private Context context;
	private DisplayImageOptions options;
	
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
			    .threadPoolSize(15)
			    .threadPriority(Thread.NORM_PRIORITY - 2)  
			    .denyCacheImageMultipleSizesInMemory()  
			    .memoryCache(new UsingFreqLimitedMemoryCache(20 * 1024 * 1024))
			    .memoryCacheSize(20 * 1024 * 1024)    
			    .discCacheSize(100 * 1024 * 1024)    
			    .tasksProcessingOrder(QueueProcessingType.LIFO)  
			    .discCacheFileCount(300) 
			    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
			    .writeDebugLogs() // Remove for release app  
			    .build();
		ImageLoader.getInstance().init(config);
		
		options = new DisplayImageOptions.Builder()  
	        .cacheInMemory(true)
	        .cacheOnDisc(true)
	        .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
	        .showImageOnFail(R.drawable.p_load_faileds)
	        .showImageOnLoading(R.drawable.p_load_faileds)
	        .showImageForEmptyUri(R.drawable.p_load_faileds)
	        .build();  
	}
	
	public void displayImage(String url, ImageView view) {
		if(url!=null&&url.contains("http")){
			ImageLoader.getInstance().displayImage(url, view, options);
		}else{
			ImageLoader.getInstance().displayImage(HttpUrls.HOSTERMA+url, view, options);
		}
		
	}
	
	public void displayImageByNet(String url, ImageView view) {
		ImageLoader.getInstance().displayImage(url, view, options);
	}
	
	public void loadImage(String uri, ImageLoadingListener listener) {
		ImageLoader.getInstance().loadImage(HttpUrls.HOSTERMA+uri, options, listener);
	}
	
	public Bitmap loadImageSyn(String uri) {
		return ImageLoader.getInstance().loadImageSync(HttpUrls.HOSTERMA+uri, options);
	}
}
