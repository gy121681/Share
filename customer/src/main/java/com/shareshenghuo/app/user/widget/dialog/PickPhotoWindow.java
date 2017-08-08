package com.shareshenghuo.app.user.widget.dialog;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.response.FileUploadResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.BitmapTool;
import com.shareshenghuo.app.user.util.FileUtil;
import com.shareshenghuo.app.user.util.ImageUtil;
import com.shareshenghuo.app.user.util.PictureUtil;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.dialog.CommonDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class PickPhotoWindow extends CommonDialog implements OnClickListener {

	public static final int REQUEST_TAKE_CAMERA = 1;
	public static final int REQUEST_PICK_LOCAL = 2;

    // 图片保存路径
    public static String mImagePaths = null;

	private Button btnPhoto, btnAlbum, btnCancel;

	private Context context;
	private Fragment fragment;

	private PhotoUploadCallback callback;

	public PickPhotoWindow(Context context) {
		super(context, R.layout.window_modify_avatar, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		this.context = context;
	}

	@Override
	public void initDlgView() {
		btnPhoto = (Button) dlgView.findViewById(R.id.btnAvatarPhoto);
		btnAlbum = (Button) dlgView.findViewById(R.id.btnAvatarAlbum);
		btnCancel = (Button) dlgView.findViewById(R.id.btnAvatarCancel);
		dlgView.setOnClickListener(this);
		btnPhoto.setOnClickListener(this);
		btnAlbum.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnAvatarPhoto:
			takePhoto();
			odismiss();
			break;

		case R.id.btnAvatarAlbum:
			pickAlbum();
			odismiss();
			break;
		case R.id.btnAvatarCancel:
			odismiss();
			break;
		default:
			odismiss();
			break;
		}
	}

	public void setFragmentContext(Fragment fragment) {
		this.fragment = fragment;
	}

	public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 图片保存路径
        mImagePaths = Environment.getExternalStorageDirectory().getPath()
                + "/id_card/temp/"
                + (System.currentTimeMillis() + ".jpg");
        // 必须确保文件夹路径存在，否则拍照后无法完成回调
        File vFile = new File(mImagePaths);
        if (!vFile.exists()) {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        } else {
            if (vFile.exists()) {
                vFile.delete();
            }
        }
        Uri cameraUri = Uri.fromFile(vFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
//		if(fragment != null)
//			fragment.startActivityForResult(intent, REQUEST_TAKE_CAMERA);
//		else
			((Activity)context).startActivityForResult(intent, REQUEST_TAKE_CAMERA);
	}

	public void pickAlbum() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		if(fragment != null)
			fragment.startActivityForResult(intent, REQUEST_PICK_LOCAL);
		else
			((Activity)context).startActivityForResult(intent, REQUEST_PICK_LOCAL);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PickPhotoWindow.REQUEST_PICK_LOCAL) {
                String path = FileUtil.getPath((Activity) context, data.getData());
                if (path == null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap bmp = extras.getParcelable("data");
                        if (bmp != null) {
                            upPhoto(BitmapTool.Bitmap2File(context, bmp));
                        }
                    }
                } else {
                    upPhoto(new File(path));
                }
            } else if (requestCode == PickPhotoWindow.REQUEST_TAKE_CAMERA) {
                ImageUtil.saveBitmap2file(PickPhotoWindow.mImagePaths,768,1280);
                upPhoto(new File(PickPhotoWindow.mImagePaths));
            }
        }

		if (resultCode == Activity.RESULT_OK && data != null) {
			upPhoto(new File(PickPhotoWindow.mImagePaths));
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
//					upPhoto(new File(path));
//				}
//			}
		}
	}

	public void upPhoto(File f) {
		try {
            // 压缩图片
            String compressPath = PictureUtil.compressImage(context, f.getPath(), f.getName(), 75);
            f = new File(compressPath);
        } catch(Exception e) {
            e.printStackTrace();
        }

		ProgressDialogUtil.showProgressDlg(context, "图片上传中");
		RequestParams params =  new RequestParams();
		params.addBodyParameter("business_type", UserInfoManager.getUserId(context)+"");
		params.addBodyParameter("file", f);
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPLOAD_FILE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(context);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				if(resp.statusCode==200 && resp.result!=null) {
					FileUploadResponse bean = new Gson().fromJson(resp.result, FileUploadResponse.class);
					T.showShort(context, bean.result_desc);
					if(Api.SUCCEED == bean.result_code) {
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


	@Override
	public void onDismiss() {
		WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
		lp.alpha = 1;
		((Activity)context).getWindow().setAttributes(lp);
	}
}
