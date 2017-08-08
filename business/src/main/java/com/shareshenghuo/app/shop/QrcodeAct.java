package com.shareshenghuo.app.shop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.tsz.afinal.FinalBitmap;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.BitmapTool;
import com.shareshenghuo.app.shop.util.FileUtil;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class QrcodeAct extends BaseTopActivity {

	private TextView tv, tv_save;
	private ImageView ivQR;
	private TwoButtonDialog downloadDialog;
	private String url,title,tag;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.qrcode_layout);
		
		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		tag = getIntent().getStringExtra("tag");
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar(title);
		tv = getView(R.id.tv);
		ivQR = getView(R.id.ivQR);
		tv_save = getView(R.id.tv_save);
		
	
		tv_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				savebitmap(ivQR);

				downloadDialog = new TwoButtonDialog(QrcodeAct.this,
						R.style.CustomDialog, "提示", "保存成功,可在相册查看", "确定", "",
						true, new OnMyDialogClickListener() {

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
		
		if(tag!=null){
			FinalBitmap.create(QrcodeAct.this).display(ivQR,
					url,ivQR.getWidth(),ivQR.getHeight(), null, null);
			Log.e("", ""+url);
			return;
		}
		
		if(url!=null){
			Bitmap logoBmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_launcher);

			try {
				ivQR.setImageBitmap(BitmapTool.createCode(url, logoBmp,
						BarcodeFormat.QR_CODE, 450, 30));
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void savebitmap(ImageView img) {

		File dir = new File(Environment.getExternalStorageDirectory()
				+ "/share/");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		Bitmap bitmap = null;

		bitmap = convertViewToBitmap(img);

		FileOutputStream m_fileOutPutStream = null;
		String filepath = Environment.getExternalStorageDirectory() + "/share/"// +File.separator
				+ getStringDateMerge() + "mypayqrcode.png";
		try {
			m_fileOutPutStream = new FileOutputStream(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// obmp = BitmapFactory.decodeFile(filepath, newOpts);
		bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
				bitmap.getConfig());
		bitmap.compress(CompressFormat.PNG, 100, m_fileOutPutStream);

		try {
			m_fileOutPutStream.flush();
			m_fileOutPutStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileUtil.setphotopath(QrcodeAct.this, filepath);
		// MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
		// bitmap, "title", "description");
	}

	public Bitmap convertViewToBitmap(View view) {
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
