//package com.sensetime.liveness.ui;
//
//import java.util.List;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.view.Window;
//import android.widget.ImageView;
//
//import com.shareshenghuo.app.user.R;
//import com.sensetime.liveness.adapter.RecyclerAdapter;
//import com.sensetime.liveness.util.Utils;
//
//public class ShowImageActivity extends Activity {
//	private ImageView zoomImage;
//	private List<String> mDatas;
//	private Bitmap[] mBitmaps;
//	private Bitmap bitmap;
//	private RecyclerView recyclerThumbnails;
//	private RecyclerAdapter recycleAdapter;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.activity_show_image);
//		zoomImage = (ImageView) findViewById(R.id.zoomImage);
//		mDatas = Utils.getImageListName(SenseMainActivity.storageFolder);
//		mBitmaps = new Bitmap[mDatas.size()];
//		getBitmapsFromSDCard(mBitmaps, mDatas);
//		zoomImage.setImageBitmap(mBitmaps[0]);
//
//		recyclerThumbnails = (RecyclerView) findViewById(R.id.recycler_thumbnails);
//		recycleAdapter = new RecyclerAdapter(ShowImageActivity.this, mBitmaps);
//		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//		recyclerThumbnails.setLayoutManager(layoutManager);
//		recyclerThumbnails.setAdapter(recycleAdapter);
//		recycleAdapter.setOnItemClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				zoomImage.setImageBitmap(mBitmaps[(Integer) v.getTag()]);
//			}
//		});
//
//	}
//
//	private void getBitmapsFromSDCard(Bitmap[] mBitmaps, List<String> mDatas) {
//		if (mDatas != null && mDatas.size() > 0) {
//			for (int i = 0; i < mDatas.size(); i++) {
//				mBitmaps[i] = Utils.getLoacalBitmap(Utils.storageFolder
//								+ Utils.getImageListName(Utils.storageFolder).get(i));
//			}
//		}
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		if (bitmap != null && !bitmap.isRecycled()) {
//			bitmap.recycle();
//		}
//	}
//}