package com.shareshenghuo.app.shop.photo;

import java.util.ArrayList;
import java.util.List;

import com.shareshenghuo.app.shop.BaseTopActivity;
import com.shareshenghuo.app.shop.CommodityInfoActivity;
import com.shareshenghuo.app.shop.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


/**
 * 这个是进入相册显示所有图片的界面
 * 
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:47:15
 */
public class AlbumActivity extends BaseTopActivity {
	//显示手机里的所有图片的列表控件
	private GridView gridView;
	//当手机里没有图片时，提示用户没有图片的控件
	private TextView tv;
	//gridView的adapter
	private AlbumGridViewAdapter gridImageAdapter;
	//完成按钮
	private Button okButton;
	// 返回按钮
	private Button back,content;
	// 取消按钮
	private Button cancel;
	private Intent intent;
	// 预览按钮
	private Button preview;
	private Context mContext;
	private ArrayList<ImageItem> dataList;
	private AlbumHelper helper;
	public static List<ImageBucket> contentList;
	public static Bitmap bitmap;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_album);
//		setContentView(Res.getLayoutID("plugin_camera_album"));
		PublicWay.activityList.add(this);
		mContext = this;
		//注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
		IntentFilter filter = new IntentFilter("data.broadcast.action");  
		registerReceiver(broadcastReceiver, filter);  
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.plugin_camera_no_pictures);//Res.getDrawableID("plugin_camera_no_pictures"));
        init();
		initListener();
		//这个函数主要用来控制预览和完成按钮的状态
		isShowOkBt();
	}
	
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {  
		  
        @Override  
        public void onReceive(Context context, Intent intent) {  
        	//mContext.unregisterReceiver(this);
            // TODO Auto-generated method stub  
        	gridImageAdapter.notifyDataSetChanged();
        }  
    };  
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

	// 预览按钮的监听
	private class PreviewListener implements OnClickListener {
		public void onClick(View v) {
			if (Bimp.tempSelectBitmap.size() > 0) {
				intent.putExtra("tag", "0");
				intent.putExtra("position", "1");
				intent.setClass(AlbumActivity.this, GalleryActivity.class);
				startActivity(intent);
			}
		}

	}

	// 完成按钮的监听
	private class AlbumSendListener implements OnClickListener {
		public void onClick(View v) {
//			overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
//			intent.setClass(mContext, CommodityInfoActivity.class);
//			startActivityForResult(intent,CommodityInfoActivity.TAKE_PICTURE);
//			setResult(RESULT_OK, intent);
			finish();
		}
	}

	// 返回按钮监听
	private class BackListener implements OnClickListener {
		public void onClick(View v) {
//			Bimp.tempSelectBitmap.clear();
			finish();
		}
	}


	

	// 初始化，给一些对象赋值
	private void init() {
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		initTop();
		contentList = helper.getImagesBucketList(false);
		dataList = new ArrayList<ImageItem>();
		for(int i = 0; i<contentList.size(); i++){
			dataList.addAll( contentList.get(i).imageList );
		}
		
		back = (Button) findViewById(R.id.back);//Res.getWidgetID("back"));
		back.setOnClickListener(new BackListener());
		preview = (Button) findViewById(R.id.preview);//Res.getWidgetID("preview"));
		preview.setOnClickListener(new PreviewListener());
		
		content = (Button) findViewById(R.id.content);
		content.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				intent.setClass(AlbumActivity.this, ImageFile.class);
				startActivity(intent);
				finish();
			}
		});
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		gridView = (GridView) findViewById(R.id.myGrid);//(Res.getWidgetID("myGrid"));
		gridImageAdapter = new AlbumGridViewAdapter(this,dataList,
				Bimp.tempSelectBitmap);
		gridView.setAdapter(gridImageAdapter);
		tv = (TextView) findViewById(R.id.myText);//(Res.getWidgetID("myText"));
		gridView.setEmptyView(tv);
		okButton = (Button) findViewById(R.id.ok_button);//(Res.getWidgetID("ok_button"));
		okButton.setText("完成"+"(" + Bimp.tempSelectBitmap.size()
				+ "/"+PublicWay.num+")");
	}

	private void initListener() {

		gridImageAdapter
				.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(final ToggleButton toggleButton,
							int position, boolean isChecked,Button chooseBt) {
						if (Bimp.tempSelectBitmap.size() >= PublicWay.num) {
							toggleButton.setChecked(false);
							chooseBt.setVisibility(View.GONE);
							if (!removeOneData(dataList.get(position))) {
								Toast.makeText(getApplicationContext(), "照片超限",
										200).show();
							}
							return;
						}
						if (isChecked) {
							chooseBt.setVisibility(View.VISIBLE);
							Bimp.tempSelectBitmap.remove(Bimp.tempSelectBitmap.size()-1);
							Bimp.tempSelectBitmap.add(dataList.get(position));
							if(Bimp.tempSelectBitmap.size()<PublicWay.num){
								Bimp.tempSelectBitmap.add(new ImageItem());
							}
							okButton.setText("完成"+"(" + (Bimp.tempSelectBitmap.size()-1 )
									+ "/"+PublicWay.num+")");
						} else {
							Bimp.tempSelectBitmap.remove(dataList.get(position));
							chooseBt.setVisibility(View.GONE);
							okButton.setText("完成"+"(" + (Bimp.tempSelectBitmap.size()-1 ) + "/"+(PublicWay.num-1)+")");
						}
						isShowOkBt();
					}
				});

		okButton.setOnClickListener(new AlbumSendListener());

	}

	private boolean removeOneData(ImageItem imageItem) {
			if (Bimp.tempSelectBitmap.contains(imageItem)) {
				Bimp.tempSelectBitmap.remove(imageItem);
				okButton.setText("完成"+"(" +(Bimp.tempSelectBitmap.size()-1 ) + "/"+(PublicWay.num-1)+")");
				return true;
			}
		return false;
	}
	
	public void isShowOkBt() {
		if (Bimp.tempSelectBitmap.size() > 0) {
			okButton.setText("完成"+"(" + (Bimp.tempSelectBitmap.size()-1 ) + "/"+(PublicWay.num-1)+")");
			preview.setEnabled(true);
			okButton.setEnabled(true);
//			okButton.setTextColor(Color.WHITE);
			preview.setTextColor(Color.BLACK);
		} else {
			okButton.setText("完成"+"(" + (Bimp.tempSelectBitmap.size()-1 ) + "/"+(PublicWay.num-1)+")");
			preview.setEnabled(false);
			okButton.setEnabled(false);
			preview.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			intent.setClass(AlbumActivity.this, ImageFile.class);
//			startActivity(intent);
//		}
//		return false;
//
//	}
@Override
protected void onRestart() {
	isShowOkBt();
	super.onRestart();
}
}
