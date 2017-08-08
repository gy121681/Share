package com.td.qianhai.epay.oem.views.dialog;

import java.io.File;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.td.qianhai.epay.oem.AddShopPhotoActivity;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.adapter.AddPhotoGridAdapter;
import com.td.qianhai.epay.oem.beans.FileUploadResponse;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.BitmapTool;
import com.td.qianhai.epay.oem.mail.utils.FileUtil;
import com.td.qianhai.epay.oem.mail.utils.FileUtils;
import com.td.qianhai.epay.oem.mail.utils.ImageUtil;
import com.td.qianhai.epay.oem.mail.utils.PictureUtil;

public class PickPhotoWindow extends CommonDialog implements OnClickListener {
	
	public static final int REQUEST_TAKE_CAMERA = 1;
	public static final int REQUEST_PICK_LOCAL = 2;
	
	public static final int FILECHOOSER_RESULTCODE = 1;
	private static final int REQ_CAMERA = FILECHOOSER_RESULTCODE+1;
	private static final int REQ_CHOOSE = REQ_CAMERA+1;

	private Button btnPhoto, btnAlbum, btnCancel;
	String compressPath = "";
	private Context context;
	private Fragment fragment;
	private int tag = 0;
	 private ImageView img_model;
	private PhotoUploadCallback callback;
	private TextView tv_pro;

	public PickPhotoWindow(Context context, int i) {
		super(context, R.layout.window_modify_avatar, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.context = context;
		this.tag = i;
	}

	@Override
	public void initDlgView() {
		tv_pro = (TextView) dlgView.findViewById(R.id.tv_pro);
		img_model = (ImageView) dlgView.findViewById(R.id.img_model);
		btnPhoto = (Button) dlgView.findViewById(R.id.btnAvatarPhoto);
		btnAlbum = (Button) dlgView.findViewById(R.id.btnAvatarAlbum);
		btnCancel = (Button) dlgView.findViewById(R.id.btnAvatarCancel);
		
		btnPhoto.setOnClickListener(this);
		btnAlbum.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		switch (tag) {
		case 1:
			img_model.setImageResource(R.drawable.share_s_mine_1);
			break;
		case 2:
			img_model.setImageResource(R.drawable.share_s_mine_2);
			btnAlbum.setVisibility(View.GONE);
			break;
		case 3:
			img_model.setImageResource(R.drawable.model_2); // TODO: 2017/7/26 商家承诺书照片
			break;
		case 4:
			img_model.setImageResource(R.drawable.share_s_mine_3);
			break;
		case 5:
			img_model.setImageResource(R.drawable.share_s_mine_4);
			break;
		case 6:
			img_model.setVisibility(View.GONE);
			tv_pro.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
//		if(tag==2){
//			
//		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnAvatarPhoto:
//			takePhoto();
			openCarcme();
			dismiss();
			break;
			
		case R.id.btnAvatarAlbum:
//			pickAlbum();
			chosePic();
			dismiss();
			break;
			
		case R.id.btnAvatarCancel:
			dismiss();
			break;
		}
	}
	
	public void setFragmentContext(Fragment fragment) {
		this.fragment = fragment;
	}
	
	public void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		if(fragment != null)
//			fragment.startActivityForResult(intent, REQUEST_TAKE_CAMERA);
//		else
			((Activity)context).startActivityForResult(intent, REQUEST_TAKE_CAMERA);
	}
	
	Uri  cameraUri,phoneUri;
	
	/**
	 * 打开照相机
	 */
	private void openCarcme() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		AddPhotoGridAdapter.imagePaths = Environment.getExternalStorageDirectory().getPath()
				+ "/fuiou_wmp/temp/"
				+ (System.currentTimeMillis() + ".jpg");
		
		
		// 必须确保文件夹路径存在，否则拍照后无法完成回调
		File vFile = new File(AddPhotoGridAdapter.imagePaths);
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		} else {
			if (vFile.exists()) {
				vFile.delete();
			}
		}
		
		
		
		
		cameraUri = Uri.fromFile(vFile);
		
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		((Activity)context).startActivityForResult(intent, REQ_CAMERA);
	}
	
	/**
	 * 本地相册选择图片
	 */
	private void chosePic() {
		
//		compressPath = Environment
//				.getExternalStorageDirectory()
//				.getPath()
//				+ "/fuiou_wmp/temp";
//		new File(compressPath).mkdirs();
//		compressPath = compressPath + File.separator
//				+ "compress.jpg";
//		
//		FileUtils.delFile(compressPath);
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
//		String IMAGE_UNSPECIFIED = "image/*";
//		innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
//		Intent wrapperIntent = Intent.createChooser(innerIntent, null);
//		startActivityForResult(wrapperIntent, REQ_CHOOSE);
		
		innerIntent.addCategory(Intent.CATEGORY_OPENABLE);  
		innerIntent.setType("image/jpeg");  
		if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){                  
			((Activity)context). startActivityForResult(innerIntent, REQ_CHOOSE);    
		}else{                
			((Activity)context). startActivityForResult(innerIntent, REQ_CHOOSE);   
		}
		
	}
	
	
	public void pickAlbum() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
//		if(fragment != null)
//			fragment.startActivityForResult(intent, REQUEST_PICK_LOCAL);
//		else
		Log.e("", " = = = = 1");
			((Activity)context).startActivityForResult(intent, REQUEST_PICK_LOCAL);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.e("", " - - - -  ");
		if(resultCode == Activity.RESULT_OK) {
			Uri uri = null;
			switch(requestCode) {
			case REQ_CAMERA:
					uri = cameraUri;
					
//					ImageUtil.saveBitmap2file(imagePaths,768,1280);
//					File f = new File(imagePaths);
//					upPhoto(f);
					break;
			case REQ_CHOOSE:
					uri = intent == null || resultCode != Activity.RESULT_OK ? null
					: intent.getData();
					
					ImageUtil.saveBitmap2file(getPath(context, uri),768,1280);
					
					File f1 = new File(getPath(context, uri));
					upPhoto(f1);
//					phoneUri = getPath(context, uri);
//					cameraUri = Uri.fromFile(compressPath);
					break;
			
				}
			}
			
		
		
		
		
//		if (resultCode == Activity.RESULT_OK && data != null) {
//			if (requestCode == PickPhotoWindow.REQUEST_PICK_LOCAL || requestCode == PickPhotoWindow.REQUEST_TAKE_CAMERA) {
//				// 修改头像
//				String path = FileUtil.getPath((Activity) context, data.getData());
//				if (path == null) {
//					Bundle extras = data.getExtras();
//					if (extras != null) {
//						Bitmap bmp = extras.getParcelable("data");
//						if (bmp != null) {
//							upPhoto(BitmapTool.Bitmap2File(context, bmp));
//						}
//					}
//				} else {
//				Log.e("", " = = = = 3");
//					upPhoto(new File(path));
//				}
//			}
//		}
	}
	
	public void upPhoto(File f) {
		try {
            // 压缩图片
            String compressPath = PictureUtil.compressImage(context, f.getPath(), f.getName(), 75);
            f = new File(compressPath);
        } catch(Exception e) {
            e.printStackTrace();
        }
		
		
//		ProgressDialogUtil.showProgressDlg(context, "图片上传中");
		RequestParams params =  new RequestParams();
		params.addBodyParameter("business_type", "user_photo");
		params.addBodyParameter("file", f);
		new HttpUtils().send(HttpMethod.POST, HttpUrls.URL_UPLOAD_FILE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(context);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
				if(resp.statusCode==200 && resp.result!=null) {
					FileUploadResponse bean = new Gson().fromJson(resp.result, FileUploadResponse.class);
					Log.e("", " = = =  = =- - -  = "+resp.result);
//					T.showShort(context, bean.result_desc);
					if(0 == bean.result_code) {
						if(callback != null)
							callback.uploadSucceed(bean.data.get(0));
					}
				}
			}
		});
	}
	
	public void setPhotoUploadCallback(PhotoUploadCallback callback) {
		this.callback = callback;
	}
	
	public interface PhotoUploadCallback {
		public void uploadSucceed(String fileUrl);
	}
	
	public static String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {
	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}
	
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int column_index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(column_index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
}
