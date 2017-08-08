package com.td.qianhai.epay.oem.views;

import java.io.IOException;

import com.td.qianhai.epay.oem.CameraActivity2;
import com.td.qianhai.epay.oem.NewRealNameAuthenticationActivity;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {

	/** LOG标识 */
	// private static final String TAG = "CameraPreview";

	/** 分辨率 */
	public static final int WIDTH = 1028;
	public static final int HEIGHT = 768;
	
	public static int MaxWIDTH = 2560;
	public static int MaxHEIGHT = 1920;

	/** 监听接口 */
	private OnCameraStatusListener listener;

	public SurfaceHolder holder;
	public Camera camera;
	
	private Context context;
	
	private OneButtonDialogWarn warnDialog;

	// 创建一个PictureCallback对象，并实现其中的onPictureTaken方法
	private PictureCallback pictureCallback = new PictureCallback() {

		// 该方法用于处理拍摄后的照片数据
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			// 停止照片拍摄
			if(camera!=null){
				camera.stopPreview();
				camera = null;
			}
			
			// 调用结束事件
			if (null != listener) {
				listener.onCameraStopped(data);
			}
		}
	};

	// Preview类的构造方法
	@SuppressWarnings("deprecation")
	public CameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获得SurfaceHolder对象
		this.context = context;
		holder = getHolder();
		// 指定用于捕捉拍照事件的SurfaceHolder.Callback对象
		holder.addCallback(this);
		// 设置SurfaceHolder对象的类型
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	// 在surface创建时激发
	@SuppressLint("NewApi") 
	public void surfaceCreated(SurfaceHolder holder) {
		// Log.e(TAG, "==surfaceCreated==");
		// 获得Camera对象
		try {
			if(NewRealNameAuthenticationActivity.iscameras){
				camera = Camera.open(1);
				
			}else{
				camera = Camera.open();
				
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
//			if(camera!=null){
////				camera.release();
//			}else{
				
				warnDialog = new OneButtonDialogWarn(context,
						R.style.CustomDialog, "提示", "相机权限被拒绝,请进手机安全设置打开相机权限", "确定",
						new OnMyDialogClickListener() {
							@Override
							public void onClick(View v) {
						           Intent intent =  new Intent(Settings.ACTION_SETTINGS);  
						           context.startActivity(intent);  
						           ((Activity) context).finish();
								warnDialog.dismiss();
							}
						});
				warnDialog.setCancelable(false);
				warnDialog.setCanceledOnTouchOutside(false);
				warnDialog.show();
				
//				ToastCustom.showMessage(context, "相机权限被拒绝,请进手机安全设置打开相机权限");
				return;
//			}
			
		}
		
		
		try {
			
			//设置旋转90度
			camera.setDisplayOrientation(90);
			// 设置用于显示拍照摄像的SurfaceHolder对象
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
			// 释放手机摄像头
			camera.release();
			camera = null;
		}
	}

	// 在surface销毁时激发
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Log.e(TAG, "==surfaceDestroyed==");
		// 释放手机摄像头
		if(camera!=null){
			camera.release();
		}
	}

	// 在surface的大小发生改变时激发
	@SuppressWarnings("deprecation")
	public void surfaceChanged(final SurfaceHolder holder, int format, int w,
			int h) {
		// Log.e(TAG, "==surfaceChanged==");
		try {
			// 获取照相机参数
			Camera.Parameters parameters = camera.getParameters();
			// 设置照片格式
			parameters.setPictureFormat(PixelFormat.JPEG);
//			// 设置预浏尺寸
//			parameters.setPreviewSize(WIDTH, HEIGHT);
//			// 设置照片分辨率
			
//			Log.e("", "suoyou = "+parameters.getSupportedPictureSizes().get(0).width);
//			Log.e("", "suoyou = "+parameters.getSupportedPictureSizes().get(0).height);
//			Log.e("", "分辨率0"+parameters.getSupportedPictureSizes().size());
//			Log.e("", "分辨率0"+parameters.getSupportedPictureSizes().size());
//			
//			
////			Log.e("", "分辨率1 = "+parameters.getSupportedPictureSizes().size()/2);
////			Log.e("", "分辨率1 = "+parameters.getSupportedPictureSizes().size()/2);
//			
//			
//			Log.e("", "分辨率1 = "+9/2);
//			Log.e("", "分辨率1 = "+9/2);
//			
//			
//			Log.e("", "分辨率"+parameters.getSupportedPictureSizes().get((parameters.getSupportedPictureSizes().size())/2).height);
//			Log.e("", "分辨率"+parameters.getSupportedPictureSizes().get((parameters.getSupportedPictureSizes().size())/2).width);
			parameters.setPictureSize(parameters.getSupportedPictureSizes().get(parameters.getSupportedPictureSizes().size()/2).width,
					parameters.getSupportedPictureSizes().get(parameters.getSupportedPictureSizes().size()/2).height);
//			// 设置照相机参数
			camera.setParameters(parameters);
			// 开始拍照
			camera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 停止拍照，并将拍摄的照片传入PictureCallback接口的onPictureTaken方法
	public void takePicture() {
		// Log.e(TAG, "==takePicture==");
//		CameraActivity2.isdown = true;
		if (camera != null) {
			// 自动对焦
//			camera.autoFocus(new AutoFocusCallback() {
//				@Override
//				public void onAutoFocus(boolean success, Camera camera) {
//					if (null != listener) {
////						listener.onAutoFocus(success);
//					}
//					
//					if(NewRealNameAuthenticationActivity.iscameras){
						camera.takePicture(null, null, pictureCallback);
						
//					}else{
//						// 自动对焦成功后才拍摄
//						if (success) {
//							camera.takePicture(null, null, pictureCallback);
//						}
//					}
//				}
//			});
		}
	}

	// 设置监听事件
	public void setOnCameraStatusListener(OnCameraStatusListener listener) {
		this.listener = listener;
	}

	/**
	 * 相机拍照监听接口
	 */
	public interface OnCameraStatusListener {

		// 相机拍照结束事件
		void onCameraStopped(byte[] data);

		// 拍摄时自动对焦事件
		void onAutoFocus(boolean success);
	}
	
}