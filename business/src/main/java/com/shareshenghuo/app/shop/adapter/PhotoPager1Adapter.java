package com.shareshenghuo.app.shop.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shareshenghuo.app.shop.ImagePager1Activity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.photo.Bimp;
import com.shareshenghuo.app.shop.photo.ImageItem;
import com.shareshenghuo.app.shop.photo.PhotoView;


public class PhotoPager1Adapter extends PagerAdapter {


//    private ImageLoader mImageLoader = ImageLoader.getInstance();

//    private DisplayImageOptions mOptions = null;

    private boolean dataSetChanged;
    
    private Context context;
    
    private ArrayList<ImageItem> tempSelectBitmap;

    private ImageLoadingListener mAnimateFirstListener = new AnimateFirstDisplayListener();


    public PhotoPager1Adapter(ArrayList<ImageItem> tempSelectBitmap,
			ImagePager1Activity context2) {
		// TODO Auto-generated constructor stub
    	this.tempSelectBitmap = tempSelectBitmap;
    	this.context = context2;
    	
	}


    @Override
    public int getCount() {
    	return tempSelectBitmap.size();
    }

    @Override
    public int getItemPosition(Object object) {
//        if (dataSetChanged) {
//            dataSetChanged = false;
            return POSITION_NONE;
//        }
//        return super.getItemPosition(object);
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        photoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
//        String photoUrl = (String) getItem(position);
        
        if(tempSelectBitmap.get(position).url!=null){
    		FinalBitmap.create(context.getApplicationContext()).display(photoView,
    				Api.HOSTERMA+tempSelectBitmap.get(position).url,
    				photoView.getWidth(),
    				photoView.getHeight(), null, null);
        }else if((tempSelectBitmap.get(position).imagePath!=null)){
        	photoView.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
        }
        

		
		
		
//        mImageLoader.displayImage(Api.HOSTERMA+photoUrl, photoView);
        container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        return photoView;
    }

    public Object getItem(int position) {
        return tempSelectBitmap.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        dataSetChanged = true;
        super.notifyDataSetChanged();
    }
    
}
