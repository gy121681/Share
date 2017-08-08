package com.shareshenghuo.app.shop.photo;


import com.shareshenghuo.app.shop.BaseTopActivity;
import com.shareshenghuo.app.shop.CommodityInfoActivity;
import com.shareshenghuo.app.shop.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 这个类主要是用来进行显示包含图片的文件夹
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:48:06
 */
public class ImageFile extends BaseTopActivity {

	private FolderAdapter folderAdapter;
	private Button bt_cancel;
	private Context mContext;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_image_file);
//		setContentView(Res.getLayoutID("plugin_camera_image_file"));
		PublicWay.activityList.add(this);
		mContext = this;
		bt_cancel = (Button) findViewById(R.id.cancel);//(Res.getWidgetID("cancel"));
		bt_cancel.setOnClickListener(new CancelListener());
		GridView gridView = (GridView) findViewById(R.id.fileGridView);//(Res.getWidgetID("fileGridView"));
		TextView textView = (TextView) findViewById(R.id.headerTitle);//(Res.getWidgetID("headerTitle"));
		folderAdapter = new FolderAdapter(this, this);
		gridView.setAdapter(folderAdapter);
		initTop();
	}

	private class CancelListener implements OnClickListener {// 取消按钮的监听
		public void onClick(View v) {
			//清空选择的图片
//			Bimp.tempSelectBitmap.clear();
			finish();
//			Intent intent = new Intent();
//			intent.setClass(mContext, CommodityInfoActivity.class);
//			startActivity(intent);
		}
	}

//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			Intent intent = new Intent();
//			intent.setClass(mContext, CommodityInfoActivity.class);
//			startActivity(intent);
//		}
//		
//		return true;
//	}

}
