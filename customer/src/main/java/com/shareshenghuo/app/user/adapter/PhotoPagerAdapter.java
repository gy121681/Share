package com.shareshenghuo.app.user.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.networkapi.Api;


public class PhotoPagerAdapter extends PagerAdapter {

	private List<String> mArrayUrl;

    private ImageLoader mImageLoader = ImageLoader.getInstance();

    private DisplayImageOptions mOptions = null;

    private boolean dataSetChanged;

    private ImageLoadingListener mAnimateFirstListener = new AnimateFirstDisplayListener();

    public PhotoPagerAdapter(ArrayList<String> dataList) {
        init(dataList);
    }

    private void init(ArrayList<String> urlList) {
        mArrayUrl = urlList;

        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.share_c_business_details_photo_default)
                .showImageForEmptyUri(R.drawable.share_c_business_details_photo_default)
                .showImageOnFail(R.drawable.share_c_business_details_photo_default)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                        // .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }

    @Override
    public int getCount() {
    	return mArrayUrl.size();
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
    	ImageView photoView= new ImageView(container.getContext());
//        PhotoView photoView = new PhotoView(container.getContext());
        photoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        String photoUrl = (String) getItem(position);
        mImageLoader.displayImage(Api.IMG_HOST+photoUrl, photoView);
        container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        return photoView;
    }

    public Object getItem(int position) {
        return mArrayUrl.get(position);
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
