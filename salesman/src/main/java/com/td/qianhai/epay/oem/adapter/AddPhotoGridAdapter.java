package com.td.qianhai.epay.oem.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.td.qianhai.epay.oem.AddShopPhotoActivity;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.beans.FileUploadResponse;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.PhotoBean;
import com.td.qianhai.epay.oem.mail.utils.BitmapTool;
import com.td.qianhai.epay.oem.mail.utils.FileSizeUtil;
import com.td.qianhai.epay.oem.mail.utils.FileUtil;
import com.td.qianhai.epay.oem.mail.utils.ImageUtil;
import com.td.qianhai.epay.oem.views.dialog.PickPhotoWindow;

public class AddPhotoGridAdapter extends CommonAdapter<PhotoBean> {
	
	private GridView gridView;
	
	public int maxCount = 9;
	
	private OnClickAdapterListener listener;
	public static final int FILECHOOSER_RESULTCODE = 1;
	private static final int REQ_CAMERA = FILECHOOSER_RESULTCODE+1;
	private static final int REQ_CHOOSE = REQ_CAMERA+1;
	String compressPath = "";
	public static String imagePaths;
	Uri  cameraUri,phoneUri;
	private int tag = 0;
	public  int num = 0;
	
	
	

	public AddPhotoGridAdapter(Context context, List<PhotoBean> datas, GridView gridView, int i, int j) {
		super(context, datas, R.layout.item_add_photo);
		
		this.gridView = gridView;
		this.tag = i;
		num = j;
	}
	
	public int getnum(){
		
		return num;
		
	}

	@Override
	public void conver(ViewHolder holder, final PhotoBean item, final int position) {
		Button btnDel = holder.getView(R.id.btnDelete);
		
//		if(getCount() <tag){
//			mData.add(new PhotoBean());
//		}
//		gridView.setAdapter(new AddPhotoGridAdapter(mContext, mData, gridView, tag,num));
		ImageView ivPhoto = holder.getView(R.id.ivPhoto);
		
		ivPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(listener != null)
					listener.onClicked();
				
				if(TextUtils.isEmpty(item.url)&& position==getCount()-1)
					new PickPhotoWindow(mContext,num).showAtBottom();
					AddShopPhotoActivity.num = num;

			}
		});
		
		btnDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {


				
				mData.remove(item);
				notifyDataSetChanged();
//				if(getCount() !=tag){
				if(getCount()>=1&&getCount() <tag){
					Log.e("", "1");
						if(!TextUtils.isEmpty(mData.get(mData.size()-1).url)){
							mData.add(new PhotoBean());
						}
				}else if(getCount() >=tag){
					
				}else{
					Log.e("", "2");
					mData.add(new PhotoBean());
				}
//				}

				

			}
		});
		if(TextUtils.isEmpty(item.url) && position==getCount()-1) {
			
//			if(getCount()>=tag){
//				ivPhoto.setVisibility(View.GONE);
//			}
			holder.setImageResource(R.id.ivPhoto, R.drawable.bg_add);
			btnDel.setVisibility(View.GONE);
		} else {
				holder.setImageByURL(R.id.ivPhoto, item.url);
				btnDel.setVisibility(View.VISIBLE);	

		}
		notifyDataSetChanged();
	}
	
	public boolean isnumm(){
		if(mData.size()>=1&&TextUtils.isEmpty(mData.get(mData.size()-1).url)){
			
			return false;
		}
		return true;
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		if(resultCode == Activity.RESULT_OK) {
			Uri uri = null;
			switch(requestCode) {
			case REQ_CAMERA:
				

//				Bundle extras = intent.getExtras();
//					String path = FileUtil.getPath((Activity) mContext, intent.getData());
//					if (path == null) {
//						Bundle extras = intent.getExtras();
//						if (extras != null) {
//							Bitmap bmp = extras.getParcelable("data");
//							if (bmp != null) {
//								upPhoto(BitmapTool.Bitmap2File(mContext, bmp));
//							}
//						}
//					} else {
//						upPhoto(new File(path));
//					}
					
					ImageUtil.saveBitmap2file(imagePaths,768,1280);
					File f = new File(imagePaths);
					upPhoto(f);
					
					

					break;
			case REQ_CHOOSE:
					uri = intent == null || resultCode != Activity.RESULT_OK ? null
					: intent.getData();
					
					ImageUtil.saveBitmap2file(getPath(mContext, uri),768,1280);
					
					File f1 = new File(getPath(mContext, uri));
					upPhoto(f1);
//					phoneUri = getPath(context, uri);
//					cameraUri = Uri.fromFile(compressPath);
					break;
			
				}
			}
//		if (resultCode == Activity.RESULT_OK && data != null) {
//			if (requestCode == PickPhotoWindow.REQUEST_PICK_LOCAL || requestCode == PickPhotoWindow.REQUEST_TAKE_CAMERA) {
//				String path = FileUtil.getPath((Activity) mContext, data.getData());
//				if (path == null) {
//					Bundle extras = data.getExtras();
//					if (extras != null) {
//						Bitmap bmp = extras.getParcelable("data");
//						if (bmp != null) {
//							upPhoto(BitmapTool.Bitmap2File(mContext, bmp));
//						}
//					}
//				} else {
//					upPhoto(new File(path));
//				}
//			}
//		}
	}
	
	public void upPhoto(File file) {
//		try {
//            // 压缩图片
//            String compressPath = PictureUtil.compressImage(mContext, file.getPath(), file.getName(), 65);
//            file = new File(compressPath);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
		
//		ProgressDialogUtil.showProgressDlg(mContext, "上传图片");
		RequestParams params = new RequestParams();
		params.addBodyParameter("business_type", "user_photo");
		params.addBodyParameter("1", file);
		new HttpUtils().send(HttpMethod.POST, HttpUrls.URL_UPLOAD_FILE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String msg) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(mContext);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
				if(resp.statusCode==200 && resp.result!=null) {
					FileUploadResponse bean = new Gson().fromJson(resp.result, FileUploadResponse.class);
					if(0 == bean.result_code) {
						Toast.makeText(mContext.getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
						mData.get(getCount()-1).url = bean.data.get(0);
						if(getCount() != tag)
							mData.add(new PhotoBean());
//						notifyDataSetChanged();
						gridView.setAdapter(new AddPhotoGridAdapter(mContext, mData, gridView, tag,num));
					} else {
						Toast.makeText(mContext.getApplicationContext(), bean.result_desc, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	public String getPhotoUrls() {
		StringBuilder sb = new StringBuilder();
		for(PhotoBean item : mData) {
			if(TextUtils.isEmpty(item.url))
				continue;
			sb.append(item.url).append(",");
		}
		if(sb.length() > 0)
			return sb.substring(0, sb.length()-1);
		return sb.toString();
	}
	
	public void setOnClickAdapterListener(OnClickAdapterListener listener) {
		this.listener = listener;
	}
	
	public interface OnClickAdapterListener {
		public void onClicked();
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
