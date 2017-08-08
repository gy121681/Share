package com.shareshenghuo.app.shop.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.tsz.afinal.FinalBitmap;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.ImageLoadManager;
import com.shareshenghuo.app.shop.util.FileUtil;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;

public class QrcodeFm extends BaseFragment {
	private TextView tv,tv_save;
	private ImageView ivQR;
	private TwoButtonDialog downloadDialog;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.qrcode_layout1;
	}

	@Override
	protected void init(View rootView) {
		// TODO Auto-generated method stub
		Bundle bundle1 = getArguments();
		tv = getView(R.id.tv);
		ivQR = getView(R.id.ivQR);
		tv_save = getView(R.id.tv_save);
		FinalBitmap.create(this.getActivity()).display(ivQR,
				bundle1.getString("url"),
				ivQR.getWidth(),
				ivQR.getHeight(), null, null);
//		ImageLoadManager.getInstance(activity).displayImagenohost(bundle1.getString("url"), ivQR);
//		ivQR.setImageBitmap(BitmapTool.createQRCodeBitmap();
		
		tv_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				savebitmap(ivQR);
				
				
				downloadDialog = new TwoButtonDialog(getActivity(), R.style.CustomDialog,
						"提示", "保存成功,可在相册查看", "确定", "",true,new OnMyDialogClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								switch (v.getId()) {
								case R.id.Button_OK:
									downloadDialog.dismiss();
									break;
								case R.id.Button_cancel:
									downloadDialog.dismiss();
								default:
									break;
								}
							}
						});
					downloadDialog.show();
				}
		});
		
	}
	public void savebitmap(ImageView img){
		
		File dir = new File(Environment.getExternalStorageDirectory()+"/share/");
		if (!dir.exists()){
			dir.mkdirs();
		}
		
		Bitmap bitmap = null;
		
		 bitmap = convertViewToBitmap(img);
		
		FileOutputStream m_fileOutPutStream = null;
		String filepath = Environment.getExternalStorageDirectory() +"/share/"//+File.separator
				+ getStringDateMerge()+"mypayqrcode.png";
		try {
			m_fileOutPutStream = new FileOutputStream(filepath);
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
//		obmp = BitmapFactory.decodeFile(filepath, newOpts);
		bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
		bitmap.compress(CompressFormat.PNG, 100, m_fileOutPutStream);
		
		try {
			m_fileOutPutStream.flush();
			m_fileOutPutStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileUtil.setphotopath(activity, filepath);
//		MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "title", "description");
	}
	public  Bitmap convertViewToBitmap(View view)  
	{  
	    view.buildDrawingCache();  
	    Bitmap bitmap = view.getDrawingCache();  
	  
	    return bitmap;  
	}
	public String getStringDateMerge() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
}
