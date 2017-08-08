package com.shareshenghuo.app.user.widget;


import java.util.List;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.Banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;


/**
 * 对自定义组件AdGallery进行了一次封装 包含对图片标题和当前位置指示器(RadioGroup)的操作
 */
public class AdGalleryHelper {
//	private MyAdGallery mAdGallery; // 无限滚动Gallery
	private AdGallery mAdGallery; // 无限滚动Gallery
	private TextView mPicTitle; // 广告图片标题
	private RadioGroup mRadioGroup; // 滚动标记组件
	
	private Context mContext;
	private LayoutInflater mInflater;

	private RelativeLayout galleryLayout;
	
	public AdGalleryHelper(Context context, final List<Banner> dataids,
			long switchTime,boolean isAutoSwitch) {

		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		galleryLayout = (RelativeLayout) mInflater.inflate(
				R.layout.adgallery_hellper, null);
		mRadioGroup = (RadioGroup) galleryLayout
				.findViewById(R.id.home_pop_gallery_mark);

		// 添加RadioButton
		Bitmap b = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.point_1);
		LayoutParams params = new LayoutParams(dpToPx(mContext, b.getWidth()),
				dpToPx(mContext, b.getHeight()));
		for (int i = 0; i < dataids.size(); i++) {
			RadioButton _rb = new RadioButton(mContext);
			_rb.setId(0x1234 + i);
			_rb.setButtonDrawable(mContext.getResources().getDrawable(
					R.drawable.gallery_selector));
			mRadioGroup.addView(_rb, params);
		}

		mPicTitle = (TextView) galleryLayout
				.findViewById(R.id.news_gallery_text);
		mAdGallery = (AdGallery) galleryLayout.findViewById(R.id.gallerypop);
		mAdGallery.isAutoSwitch=isAutoSwitch;
		mAdGallery.init(dataids, switchTime, new OnGallerySwitchListener() {

			@Override
			public void onGallerySwitch(int position) {
				if (mRadioGroup != null) {
					mRadioGroup.check(mRadioGroup.getChildAt(position).getId());
				}
				if (mPicTitle != null) {
//					mPicTitle.setText(ads[position].getTitle());
				}
			}
		}, mAdGallery.isAutoSwitch);
	}

	/**
	 * 向外开放布局对象，使得可以将布局对象添加到外部的布局中去
	 * 
	 * @return
	 */
	public RelativeLayout getLayout() {
		return galleryLayout;
	}
	/**
	 * 获取当前使用的AdGllery对象
	 * @return
	 */
	public AdGallery getAdGallery(){
		return mAdGallery;
	}
	
	/**
	 * 开始自动循环切换
	 */
	public void startAutoSwitch() {
		mAdGallery.setRunFlag(true);
		mAdGallery.startAutoScroll();
	}
	
	/**
	 * 停止自动循环切换
	 */
	public void stopAutoSwitch() {
		mAdGallery.setRunFlag(true);
	}

	/**
	 * 图片切换回调接口
	 * 
	 */
	public interface OnGallerySwitchListener {
		public void onGallerySwitch(int position);
	}

	private int dpToPx(Context context, int dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	private int pxToDp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}

