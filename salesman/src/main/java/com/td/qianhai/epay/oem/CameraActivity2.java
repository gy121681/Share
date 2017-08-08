package com.td.qianhai.epay.oem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.views.CameraPreview;

public class CameraActivity2 extends Activity implements
		CameraPreview.OnCameraStatusListener, OnClickListener {

	public static final Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	public static final String PATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/DCIM/";
	private CameraPreview mCameraPreview;
	private ImageView focusView;
	private boolean isTaking = false; // 拍照中
//	private RelativeLayout layout; // 底部菜单
	@SuppressWarnings("unused")
	private Button position, btn_finish, btn_restart;// 返回和切换前后置摄像头、确定和重新拍摄
	private int cameraPosition = 0;// 0代表前置摄像头，1代表后置摄像头
	private TextView textView;
	private byte[] data;
	private long dateTaken;
	private Bitmap bitmap;
	private Animation animation;
	private String fileName;
	private TextView back,title,btn_cameras;
	private String tag,tag1;
	private LinearLayout camera_bg,camera_bg1;
//	public static  boolean isdown = false;
	

	@Override
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.cut_window3);
		// 设置横屏
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// 设置全屏
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		((AppContext)getApplication()).setCameraCount(1);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		fileName = bundle.getString("fileName");
		tag = bundle.getString("tag");
		tag1 = bundle.getString("tag1");
		title = (TextView) findViewById(R.id.tv_title_contre);
		textView = (TextView) findViewById(R.id.cut_window_textView2);
		Animation mAnimationRight = AnimationUtils.loadAnimation(this, R.anim.text_heng);  
		mAnimationRight.setFillAfter(true);   
		focusView = (ImageView) findViewById(R.id.focusView);
		focusView.bringToFront();
		camera_bg = (LinearLayout) findViewById(R.id.camera_bg);
		camera_bg1 = (LinearLayout) findViewById(R.id.camera_bg1);
		if(tag.equals("100")){
			if(tag1!=null){
				title.setText("拍银行卡照");
				textView.setText("请拍摄借记卡正面照片");
			}else{
				title.setText("拍摄证件照");
				textView.setText("请拍摄身份证正面照片");
			}

			textView.setAnimation(mAnimationRight); 
		}else if(tag.equals("101")){
			focusView.setVisibility(View.GONE);
			title.setText("拍摄情景照");
			camera_bg.setVisibility(View.GONE);
			camera_bg1.setVisibility(View.GONE);
			if(tag1!=null){
				textView.setText("请将身份证+借记卡置于胸前拍摄情景照片");
			}else{
				textView.setText("请将身份证置于胸前拍摄情景照片");
			}
			
			
		}else if(tag.equals("102")){
			title.setText("拍摄证件反面照");
			textView.setText("请拍摄身份证反面照片");
			textView.setAnimation(mAnimationRight); 
		}
		else{
			title.setText("拍信用卡照");
			textView.setText("请拍摄信用卡正面照片");
			textView.setAnimation(mAnimationRight); 
		}
		

		
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 照相预览界面
		mCameraPreview = (CameraPreview) findViewById(R.id.camera_preview);
		mCameraPreview.setOnCameraStatusListener(this);
		// 焦点图片

//		layout = (RelativeLayout) findViewById(R.id.cup_window_linearLayout1);
		findViewById(R.id.bt_title_left).setOnClickListener(this);
		
		position = (Button) findViewById(R.id.btn_change_camera);
		
		position.setOnClickListener(this);
		btn_finish = (Button) findViewById(R.id.btn_finish);
		btn_finish.setOnClickListener(this);
		btn_restart = (Button) findViewById(R.id.btn_restarts);
		btn_restart.setOnClickListener(this);
		btn_cameras = (TextView) findViewById(R.id.btn_cameras);
		
		btn_cameras.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCameraPreview.camera.autoFocus(new AutoFocusCallback() {
					@Override
					public void onAutoFocus(boolean success, Camera camera) {
						
						if(tag.equals("101")||tag.equals("102")){
							isTaking = true;
							mCameraPreview.takePicture();
							btn_cameras.setEnabled(false);
							// 隐藏前后切换
							position.setVisibility(View.GONE);
						}else{
						if(success){
							isTaking = true;
							mCameraPreview.takePicture();
							btn_cameras.setEnabled(false);
							// 隐藏前后切换
							position.setVisibility(View.GONE);
						}else{
							isTaking = true;
							mCameraPreview.takePicture();
							btn_cameras.setEnabled(false);
							// 隐藏前后切换
							position.setVisibility(View.GONE);
						}
						}
					}
				});
			}
		});
		
		if(!NewRealNameAuthenticationActivity.iscameras){
			position.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 触屏事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN && !isTaking) {
			if (mCameraPreview.camera != null) {
				// 自动对焦
				mCameraPreview.camera.autoFocus(new AutoFocusCallback() {
					@Override
					public void onAutoFocus(boolean success, Camera camera) {

					}
				});
			}
			
		}
		return super.onTouchEvent(event);
	}
	
	


//	/**
//	 * 触屏事件
//	 */
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (event.getAction() == MotionEvent.ACTION_DOWN && !isTaking) {
//
//		}
////		setFlickerAnimation(focusView);
//		return super.onTouchEvent(event);
//	}

	/**
	 * 存储图像并将信息添加入媒体数据库
	 */
	private Uri insertImage(ContentResolver cr, String name, long dateTaken,
			String directory, String filename, Bitmap source, byte[] jpegData) {

		OutputStream outputStream = null;
		String filePath = directory + filename;
		try {
			File dir = new File(directory);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(directory, filename);
			if (file.createNewFile()) {
				outputStream = new FileOutputStream(file);
//                FileOutputStream fos = new FileOutputStream(file);
//                fos.write(buffer);
//                fos.close();
				
				if (source != null) {
					source.compress(CompressFormat.JPEG,30, outputStream);
				} else {
					outputStream.write(jpegData);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Throwable t) {
				}
			}

			if (source != null) {
				source.recycle();
			}
		}
		ContentValues values = new ContentValues(7);
		values.put(MediaStore.Images.Media.TITLE, name);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
		values.put(MediaStore.Images.Media.DATE_TAKEN, dateTaken);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		values.put(MediaStore.Images.Media.DATA, filePath);
		
		
		Log.e("", "  返回地址 = ==  "+ cr.insert(IMAGE_URI, values));
		return cr.insert(IMAGE_URI, values);
	}

	/**
	 * 相机拍照结束事件
	 */
	@Override
	public void onCameraStopped(byte[] data) {
		Log.e("onCameraStopped", "==onCameraStopped==");
		File file = new File(PATH, fileName);
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				
				Log.e("","删除文件");
				file.delete(); // delete()方法 你应该知道 是删除的意思;
				// } else if (file.isDirectory()) { // 否则如果它是一个目录
				// File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				// for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
				// this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				// }
				// }
//				file.delete();
			}
		}
		
//		Matrix matrix = new Matrix(); 
//		// resize the bit map 
//		matrix.postScale(800, 400); 
		// 创建图像
		
		
		
//			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		
		
//		
//		Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), 
//				matrix, true); 
//		
//		bitmap.recycle();
		// 旋转图片 动作
		// Matrix matrix = new Matrix();
		// matrix.postRotate(90);
		// System.out.println("angle2=" + angle);
		// 创建新的图片
		// bitmap = Bitmap.createBitmap(bitmap, 0, 0,
		// bitmap.getWidth(), bitmap.getHeight(), matrix, true);

		// bitmap=BitmapUtil.rotaingImageView(90, bitmap);
		// 系统时间
		long dateTaken = System.currentTimeMillis();
		// // 图像名称
		// String filename = DateFormat.format("yyyy-MM-dd kk.mm.ss", dateTaken)
		// .toString() + ".jpg";
		// // 存储图像（PATH目录）
		// Uri uri = insertImage(getContentResolver(), "temp", dateTaken, PATH,
		// "temp", bitmap, data);
		this.data = data;
		this.dateTaken = dateTaken;
		// 显示和隐藏布局
		btn_cameras.setVisibility(View.GONE);
//		layout.setVisibility(View.VISIBLE);
		btn_finish.setVisibility(View.VISIBLE);
		btn_restart.setVisibility(View.VISIBLE);
		focusView.setVisibility(View.GONE);
		textView.setVisibility(View.GONE);
	}

	/**
	 * 拍摄时自动对焦事件
	 */
	@SuppressLint("NewApi") @Override
	public void onAutoFocus(boolean success) {
		// 改变对焦状态图像
		if (success) {
//			focusView.setImageResource(R.drawable.focus2);
		} else {
			focusView.setImageResource(R.drawable.focus1);
			Toast.makeText(getApplicationContext(), "焦距不准，请重拍！",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(this, "焦距不准，请重拍！");
			isTaking = false;
		}
		animation.cancel();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_title_left:
			// 返回上一个Activity
			// AppManager.getAppManager().finishActivity();
			if (bitmap != null) {
				bitmap.recycle();
			}
			finish();
			break;
		case R.id.btn_change_camera:
			switchLens();
			
			break;
		case R.id.btn_finish:
			// 返回结果
			// 存储图像（PATH目录）
			
			Uri uri = insertImage(getContentResolver(), fileName, dateTaken,
					PATH, fileName, bitmap, data);
			
			Intent intent = getIntent();
//			intent.putExtra("uriStr", uri.toString());
			intent.putExtra("dateTaken", dateTaken);
			// intent.putExtra("filePath", PATH + filename);
			// intent.putExtra("orientation", orientation); // 拍摄方向
			setResult(RESULT_OK, intent);
			if (bitmap != null) {
				bitmap.recycle();
			}
			// 关闭当前Activity
			finish();
			break;

		case R.id.btn_restarts:
			btn_cameras.setEnabled(true);
			if(NewRealNameAuthenticationActivity.iscameras){
				position.setVisibility(View.VISIBLE);
				
			}
			btn_cameras.setVisibility(View.VISIBLE);
			textView.setVisibility(View.VISIBLE);
			focusView.setVisibility(View.VISIBLE);
			
			btn_finish.setVisibility(View.GONE);
			btn_restart.setVisibility(View.GONE);
//			layout.setVisibility(View.INVISIBLE);
			/* 重新设定Camera */
			// bmp = null;
//			if (!bitmap.isRecycled())
//				bitmap.recycle();
			stopCamera();
			initCamera();
			break;
		default:
			break;
		}
	}

	@SuppressLint("NewApi") @SuppressWarnings("static-access")
	private void switchLens() {
		// 切换前后摄像头
		int cameraCount = 0;
		CameraInfo cameraInfo = new CameraInfo();
		cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数

		for (int i = 0; i < cameraCount; i++) {
			Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
			if (cameraPosition == 1) {
				// 现在是后置，变更为前置
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
//					if(mCameraPreview.camera!=null){
						mCameraPreview.camera.stopPreview();// 停掉原来摄像头的预览
						mCameraPreview.camera.release();// 释放资源
						mCameraPreview.camera = null;// 取消原来摄像头
//					}	
					mCameraPreview.camera = Camera.open(i);// 打开当前选中的摄像头
//					mCameraPreview.MaxHEIGHT = 2560;
//					mCameraPreview.MaxWIDTH = 1920;
					mCameraPreview.camera.setDisplayOrientation(90);
					try {
						mCameraPreview.camera
								.setPreviewDisplay(mCameraPreview.holder);// 通过surfaceview显示取景画面
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					 获取照相机参数
					 Camera.Parameters parameters = mCameraPreview.camera
					 .getParameters();
					 // 设置照片分辨率
					 parameters.setPictureSize(parameters.getSupportedPictureSizes().get(parameters.getSupportedPictureSizes().size()/2).width,
								parameters.getSupportedPictureSizes().get(parameters.getSupportedPictureSizes().size()/2).height);
					 // 设置照相机参数
					 mCameraPreview.camera.setParameters(parameters);
					mCameraPreview.camera.startPreview();// 开始预览
					((AppContext) this.getApplication()).setCameraCount(0);
					cameraPosition = 0;
					break;
				}
			} else {
				Log.e("", "zheli");
				// 现在是前置， 变更为后置
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
																				// CAMERA_FACING_BACK后置
//					if(mCameraPreview.camera!=null){
						mCameraPreview.camera.stopPreview();// 停掉原来摄像头的预览
						mCameraPreview.camera.release();// 释放资源
						mCameraPreview.camera = null;// 取消原来摄像头
//					}

					mCameraPreview.camera = Camera.open(i);// 打开当前选中的摄像头
					// 设置旋转90度
					mCameraPreview.camera.setDisplayOrientation(90);
//					 获取照相机参数
					 Camera.Parameters parameters = mCameraPreview.camera
					 .getParameters();
					 // 设置照片分辨率
					 parameters.setPictureSize(parameters.getSupportedPictureSizes().get(parameters.getSupportedPictureSizes().size()/2).width,
								parameters.getSupportedPictureSizes().get(parameters.getSupportedPictureSizes().size()/2).height);
					 // 设置照相机参数
					 mCameraPreview.camera.setParameters(parameters);
					try {
						mCameraPreview.camera
								.setPreviewDisplay(mCameraPreview.holder);// 通过surfaceview显示取景画面
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mCameraPreview.camera.startPreview();// 开始预览
					((AppContext) this.getApplication()).setCameraCount(1);
					cameraPosition = 1;
					break;
				}
			}
		}
	}

	/* 相机初始化的method */
	@SuppressLint("NewApi") @SuppressWarnings({ "static-access", "deprecation" })
	private void initCamera() {
		Log.e("", "初始化相机");
		if (mCameraPreview.camera != null) {
			try {
				// 取消拍摄中
				isTaking = false;
				if (((AppContext) this.getApplication()).getCameraCount() == 0) {
					System.out.println("摄像头1");
					mCameraPreview.camera.stopPreview();// 停掉原来摄像头的预览
					mCameraPreview.camera.release();// 释放资源
					mCameraPreview.camera = null;// 取消原来摄像头
					mCameraPreview.camera = Camera.open(1);// 打开当前选中的摄像头
					mCameraPreview.camera.setDisplayOrientation(90);
//					mCameraPreview.MaxHEIGHT = 2560;
//					mCameraPreview.MaxWIDTH = 1920;
					
					 Camera.Parameters parameters = mCameraPreview.camera
					 .getParameters();
					 // 设置照片分辨率
					 parameters.setPictureSize(parameters.getSupportedPictureSizes().get(parameters.getSupportedPictureSizes().size()/2).width,
								parameters.getSupportedPictureSizes().get(parameters.getSupportedPictureSizes().size()/2).height);
					 // 设置照相机参数
					 mCameraPreview.camera.setParameters(parameters);
					try {
						mCameraPreview.camera
								.setPreviewDisplay(mCameraPreview.holder);// 通过surfaceview显示取景画面
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mCameraPreview.camera.startPreview();// 开始预览
				} else {
					System.out.println("摄像头2");
					// 获取照相机参数
					Camera.Parameters parameters = mCameraPreview.camera
							.getParameters();
//					// 设置照片格式
//					parameters.setPictureFormat(PixelFormat.JPEG);
					// 设置预浏尺寸
//					parameters.setPreviewSize(mCameraPreview.WIDTH,
//							mCameraPreview.HEIGHT);
//					// 设置照片分辨率
					 parameters.setPictureSize(parameters.getSupportedPictureSizes().get(parameters.getSupportedPictureSizes().size()/2).width,
								parameters.getSupportedPictureSizes().get(parameters.getSupportedPictureSizes().size()/2).height);
					 // 设置照相机参数
					 mCameraPreview.camera.setParameters(parameters);
					// 开始拍照
					mCameraPreview.camera.startPreview();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* 停止相机的method */
	private void stopCamera() {
		if (mCameraPreview.camera != null) {
			try {
				/* 停止预览 */
				mCameraPreview.camera.stopPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setFlickerAnimation(ImageView iv_chat_head) {
		animation = new AlphaAnimation(1, 0); // Change alpha from fully visible
												// to invisible
		animation.setDuration(500); // duration - half a second
		animation.setInterpolator(new LinearInterpolator()); // do not alter
																// animation
																// rate
		animation.setRepeatCount(Animation.INFINITE); // Repeat animation
														// infinitely
		animation.setRepeatMode(Animation.REVERSE); //
		iv_chat_head.setAnimation(animation);
	}
}