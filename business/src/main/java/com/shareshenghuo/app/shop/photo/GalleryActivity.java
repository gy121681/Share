package com.shareshenghuo.app.shop.photo;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shareshenghuo.app.shop.BaseTopActivity;
import com.shareshenghuo.app.shop.CommodityInfoActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.ImageLoadManager;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 这个是用于进行图片浏览时的界面
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:47:53
 */
public class GalleryActivity extends BaseTopActivity {
	private Intent intent;
    // 返回按钮
    private Button back_bt;
	// 发送按钮
	private Button send_bt;
	//删除按钮
	private Button del_bt;
	//顶部显示预览图片位置的textview
	private TextView positionTextView;
	//获取前一个activity传过来的position
	private int position;
	//当前的位置
	private int location = 0;
	
	private ArrayList<View> listViews = null;
	private ViewPagerFixed pager;
	private MyPageAdapter adapter;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();
	
	private TwoButtonDialog downloadDialog;
	private String tag ;
	private Context mContext;

	  private ImageLoader mImageLoader = ImageLoader.getInstance();
	
	RelativeLayout photo_relativeLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_gallery);
//		setContentView(Res.getLayoutID("plugin_camera_gallery"));// 切屏到主界面
		PublicWay.activityList.add(this);
		initTop();
		mContext = this;
		back_bt = (Button) findViewById(R.id.gallery_back);//(Res.getWidgetID("gallery_back"));
		send_bt = (Button) findViewById(R.id.send_button);//(Res.getWidgetID("send_button"));
		del_bt = (Button)findViewById(R.id.gallery_del);//(Res.getWidgetID("gallery_del"));
		back_bt.setOnClickListener(new BackListener());
		send_bt.setOnClickListener(new GallerySendListener());
		del_bt.setOnClickListener(new DelListener());
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		tag = intent.getStringExtra("tag");
		position = Integer.parseInt(intent.getStringExtra("position"));
		isShowOkBt();
		// 为发送按钮设置文字
		pager = (ViewPagerFixed) findViewById(R.id.gallery01);//(Res.getWidgetID("gallery01"));
		if (listViews == null)
			listViews = new ArrayList<View>();
		pager.setOnPageChangeListener(pageChangeListener);
		for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
			if( Bimp.tempSelectBitmap.get(i).getBitmap()!=null){
				initListViews( Bimp.tempSelectBitmap.get(i).getBitmap() );
			}else if(Bimp.tempSelectBitmap.get(i).url!=null){
				if(tag!=null&&tag.equals("0")){
				
				}else{
					initListViews1( Bimp.tempSelectBitmap.get(i).url );
				}
			}
		
		}
		
		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);
		pager.setPageMargin(10);
		int id = intent.getIntExtra("ID", 0);
		pager.setCurrentItem(id);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	
	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			location = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};
	
	private void initListViews(Bitmap bm) {

		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		listViews.add(img);
	}
	private void initListViews1(String url) {
		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		
//		FinalBitmap.create(this).display(img,
//		Api.HOSTERMA+url,
//		img.getWidth(),
//		img.getHeight(), null, null);
//		ImageLoadManager.getInstance(this).userphoto(url, img);
		ImageLoadManager.getInstance(getApplicationContext()).displayImage(Api.HOSTERMA+url,  img);
//		 mImageLoader.displayImage(Api.HOSTERMA+url, img);
//		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		listViews.add(img);
	}
	
	// 返回按钮添加的监听器
	private class BackListener implements OnClickListener {

		public void onClick(View v) {
			finish();
//			intent.setClass(GalleryActivity.this, ImageFile.class);
//			startActivity(intent);
		}
	}
	
	// 删除按钮添加的监听器
	private class DelListener implements OnClickListener {

		public void onClick(View v) {
			
			initDialog("确定删除?","否","是");
		}
	}
	
	private void initDialog(String content,String left,String right) {
		// TODO Auto-generated method stub
		downloadDialog = new TwoButtonDialog(GalleryActivity.this, R.style.CustomDialog,
				"", content, left, right,true,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							
							if (listViews.size() == 1) {
								Bimp.tempSelectBitmap.clear();
								Bimp.max = 0;
								Bimp.tempSelectBitmap.add(new ImageItem());
								send_bt.setText("完成"+"(" + (Bimp.tempSelectBitmap.size()-1 )  + "/"+(PublicWay.num-1)+")");
								Intent intent = new Intent("data.broadcast.action");  
				                sendBroadcast(intent);  
								finish();
							} else {
								
								Bimp.tempSelectBitmap.remove(location);

								Bimp.max--;
								pager.removeAllViews();
								listViews.remove(location);
								adapter.setListViews(listViews);
								send_bt.setText("完成"+"(" + (Bimp.tempSelectBitmap.size()-1 ) + "/"+(PublicWay.num-1)+")");
								adapter.notifyDataSetChanged();
							}
							
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
			
	}

	// 完成按钮的监听
	private class GallerySendListener implements OnClickListener {
		public void onClick(View v) {
			finish();
//			intent.setClass(mContext,CommodityInfoActivity.class);
//			startActivity(intent);
		}

	}

	public void isShowOkBt() {
		if (Bimp.tempSelectBitmap.size() > 0) {
			send_bt.setText("完成"+"(" + (Bimp.tempSelectBitmap.size()-1 ) + "/"+(PublicWay.num-1)+")");
			send_bt.setEnabled(true);
//			send_bt.setClickable(true);
//			send_bt.setTextColoR.color.black);
		} else {
			send_bt.setEnabled(false);
//			send_bt.setClickable(false);
//			send_bt.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}

	/**
	 * 监听返回按钮
	 */
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if(position==1){
//				this.finish();
//				intent.setClass(GalleryActivity.this, AlbumActivity.class);
//				startActivity(intent);
//			}else if(position==2){
//				this.finish();
//				intent.setClass(GalleryActivity.this, ShowAllPhoto.class);
//				startActivity(intent);
//			}
//		}
//		return true;
//	}
	
	
	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;
		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
