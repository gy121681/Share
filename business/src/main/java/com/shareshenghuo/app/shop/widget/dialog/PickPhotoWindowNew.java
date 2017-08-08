package com.shareshenghuo.app.shop.widget.dialog;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

import com.shareshenghuo.app.shop.R;

public class PickPhotoWindowNew extends CommonDialog implements OnClickListener {
	
	public static final int REQUEST_TAKE_CAMERA = 1;
	public static final int REQUEST_PICK_LOCAL = 2;

	private Button btnPhoto, btnAlbum, btnCancel;
	
	private Fragment fragment;

	public PickPhotoWindowNew(Context context) {
		super(context, R.layout.window_modify_avatar, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	public void initDlgView() {
		btnPhoto = (Button) dlgView.findViewById(R.id.btnAvatarPhoto);
		btnAlbum = (Button) dlgView.findViewById(R.id.btnAvatarAlbum);
		btnCancel = (Button) dlgView.findViewById(R.id.btnAvatarCancel);
		
		btnPhoto.setOnClickListener(this);
		btnAlbum.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnAvatarPhoto:
			takePhoto();
			dismiss();
			break;
			
		case R.id.btnAvatarAlbum:
			pickAlbum();
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
		 Uri photoUri = null;
		File dir = new File(Environment.getExternalStorageDirectory()+"/share/");
		if (!dir.exists()){
			dir.mkdirs();
		}
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		try {
//			photoUri= Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/share/",String.valueOf(System.currentTimeMillis())+"image.png"));
//		} catch (Exception e) {
//			// TODO: handle exception
			photoUri= Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
//		}
		
		//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		if(fragment != null)
			fragment.startActivityForResult(openCameraIntent, REQUEST_TAKE_CAMERA);
		else
			((Activity)context).startActivityForResult(openCameraIntent, REQUEST_TAKE_CAMERA);
		
//		
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		if(fragment != null)
//			fragment.startActivityForResult(intent, REQUEST_TAKE_CAMERA);
//		else
//			((Activity)context).startActivityForResult(intent, REQUEST_TAKE_CAMERA);
	}
	
	public void pickAlbum() {

		
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		if(fragment != null)
			fragment.startActivityForResult(intent, REQUEST_PICK_LOCAL);
		else
			((Activity)context).startActivityForResult(intent, REQUEST_PICK_LOCAL);
	}
}
