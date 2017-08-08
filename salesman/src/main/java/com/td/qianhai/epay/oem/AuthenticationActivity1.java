package com.td.qianhai.epay.oem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import net.tsz.afinal.FinalBitmap;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.NewAuthenticationBean;
import com.td.qianhai.epay.oem.mail.utils.IdCard;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.mail.utils.PictureUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.AnimationUtil;
import com.td.qianhai.epay.oem.views.SelectPicPopupWindow;
import com.td.qianhai.epay.utils.ChineseUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AuthenticationActivity1 extends BaseActivity implements
		OnClickListener {

	private TextView tv_au_button,tv_pro1,tv_pro2;
	private EditText re_name, re_id;
	public static Activity con;
	private String custId;
	private ImageView ivFront;
	private SelectPicPopupWindow menuWindow;
	private String idFrontPic, scenePic, bancardPic, idbackPic;
	// 身份证照片Code
	private final int FRONT_CODE = 100;
	// 情景照片Code
	private final int SCENS_CODE = 101;
	public static boolean iscameras = false;

	private ImageView idcard_back_img;

	private Button button1, button2;

	private String custpic, idcardpic, mercnum, bankpic;

	private File custPicFile, idcardPicFile;
	private Editor editor;
	// 文件名
	private String localTempImgFileName = "";
	
	private Uri photoUri;
	
	public static NewAuthenticationBean newauthenticationbean;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authentication_activity1);
		custId = MyCacheUtil.getshared(this).getString("Mobile", "");
		mercnum = MyCacheUtil.getshared(this).getString("MercNum", "");
		AppContext.getInstance().addActivity(this);
		editor = MyCacheUtil.setshared(this);
		con = this;
		initview();

		
		AuthenticationTask task2 = new AuthenticationTask();
		task2.execute(HttpUrls.CERTIFICATIONBEFORE + "", mercnum);
		
//		BussinessInfoTask task = new BussinessInfoTask();
//		task.execute(HttpUrls.BUSSINESS_INFO + "", custId, "4", "0");
	}

	private void initview() {
		// TODO Auto-generated method stub
		
		File fil = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/DCIM/");
		//判断文件夹是否存在,如果不存在则创建文件夹
		if (!fil.exists()) {
			fil.mkdir();
		}
		
		((TextView) findViewById(R.id.tv_title_contre)).setText("实名认证");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		tv_pro1 = (TextView) findViewById(R.id.tv_pro1);
		tv_pro2 = (TextView) findViewById(R.id.tv_pro2);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		idcard_back_img = (ImageView) findViewById(R.id.idcard_back_img);
		idcard_back_img.setOnClickListener(this);
		ivFront = (ImageView) findViewById(R.id.iv_name_auth_front);
		ivFront.setOnClickListener(this);
		re_name = (EditText) findViewById(R.id.re_name);
		re_id = (EditText) findViewById(R.id.re_id);
		tv_au_button = (TextView) findViewById(R.id.tv_au_button);
		tv_au_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				 if (!ChineseUtil.checkNameChese(re_name.getText().toString())) {
						Toast.makeText(getApplicationContext(), "用户姓名必须全为中文",
								Toast.LENGTH_SHORT).show();
						return;
					}
				
				if (new IdCard().isValidatedAllIdcard(re_id.getText()
						.toString())) {
				} else {
					if (re_id.getText().toString().length() < 15) {
						if (new IdCard().iscard(re_id.getText().toString())) {

						} else {
							Toast.makeText(getApplicationContext(), "身份证有误",
									Toast.LENGTH_SHORT).show();
							return;
						}

					} else {
						Toast.makeText(getApplicationContext(), "身份证有误",
								Toast.LENGTH_SHORT).show();
						return;
					}
				}
				completedata();

			}
		});

		re_id.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (s.length() >= 1) {
					if (re_name.getText().toString().length() >= 1) {
						tv_au_button.setEnabled(true);
					} else {
						tv_au_button.setEnabled(false);
					}
				} else {
					tv_au_button.setEnabled(false);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
		re_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (s.length() >= 1) {
					if (re_id.getText().toString().length() >= 1) {
						tv_au_button.setEnabled(true);
					} else {
						tv_au_button.setEnabled(false);
					}
				} else {
					tv_au_button.setEnabled(false);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		custpic = custId + "_"
				+ getStringDateMerge() + "_" + "CU.jpg";
		idcardpic = custId + "_"
				+ getStringDateMerge() + "_" + "ID.jpg";
	}

	/**
	 * 补全资料(全部信息上传)
	 */
	private void completedata() {
		if (idFrontPic == null) {
			AnimationUtil.BtnSpecialAnmations1(this, ivFront, 500);
			Toast.makeText(getApplicationContext(), "请拍摄身份证正面照片",
					Toast.LENGTH_SHORT).show();
			// ToastCustom.showMessage(this, "请拍摄身份证正面照片");
			return;
		}

		if (scenePic == null) {
			AnimationUtil.BtnSpecialAnmations1(this, idcard_back_img, 500);
			Toast.makeText(getApplicationContext(), "请拍摄身份证反面照",
					Toast.LENGTH_SHORT).show();
			// ToastCustom.showMessage(this, "请拍摄情景照片");
			return;
		}

		Intent it = new Intent(AuthenticationActivity1.this,
				Authenticationactivity2.class);
		it.putExtra("name", re_name.getText().toString());
		it.putExtra("id", re_id.getText().toString());
		startActivity(it);
	}

	public String getStringDateMerge() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString+".png";
	}

	class BussinessInfoTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate.getVerificationMidatc(
					HttpUrls.BUSSINESS_INFO, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					if (result.get("CARDID") != null) {
						re_id.setText(result.get("CARDID").toString());
					}
					if (result.get("ACTNAM") != null) {
						re_name.setText(result.get("ACTNAM").toString());
					}
				} else {
					// Toast.makeText(getApplicationContext(),
					// result.get(Entity.RSPMSG).toString(),
					// Toast.LENGTH_SHORT);
				}
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_name_auth_front:
//			takePicture(FRONT_CODE);
			photo(FRONT_CODE);
		
			// editpic();
			break;
		case R.id.idcard_back_img:
//			takePicture(SCENS_CODE);
			photo(SCENS_CODE);
			break;
		case R.id.button1:
			photo(FRONT_CODE);
//			takePicture(FRONT_CODE);
			break;
		case R.id.button2:
//			takePicture(SCENS_CODE);
			photo(SCENS_CODE);
			break;
		default:
			break;
		}

	}
	
	public void photo(int code) {
		
		File dir = new File(Environment.getExternalStorageDirectory()+"/share/");
		if (!dir.exists()){
			dir.mkdirs();
		}
		String name = getStringDateMerge();

		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			photoUri= Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/share/",name));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		startActivityForResult(openCameraIntent, code);
		
	}

	private void editpic() {

		menuWindow = new SelectPicPopupWindow(AuthenticationActivity1.this,
				itemsOnClick);
		menuWindow.showAtLocation(
				AuthenticationActivity1.this.findViewById(R.id.main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		if (idFrontPic == null) {
			menuWindow.setbutton();
		} else {
			menuWindow.setbutton1("重拍");
		}
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:
				iscameras = false;
				takePicture(FRONT_CODE);
				break;
			case R.id.btn_pick_photo:
				if (idFrontPic != null) {
					ivFront.setBackgroundColor(getResources().getColor(
							R.color.transparent));
					ivFront.setImageResource(R.drawable.newreal_idcard);
					idFrontPic = null;
				}
				break;
			default:
				break;
			}
		}
	};

	/*
	 * 拍照返回图片
	 */
	private void takePicture(int code) {

		File dir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/DCIM/");// 设置临时文件的存放目录
		if (!dir.exists()) {
			dir.mkdir();
		}
		// 系统相机
		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		if (code == FRONT_CODE) {
			localTempImgFileName = idcardpic;

			Log.e("", "localTempImgFileName1 = = =" + localTempImgFileName);
		} else if (code == SCENS_CODE) {

			localTempImgFileName = custpic;
			Log.e("", "localTempImgFileName2 = = =" + localTempImgFileName);
		} else {
			localTempImgFileName = bankpic;
			Log.e("", "localTempImgFileName2 = = =" + localTempImgFileName);
		}
		File f = new File(dir, localTempImgFileName);
		Log.e("fileNam", dir.getAbsolutePath() + localTempImgFileName);
		Uri u = Uri.fromFile(f);
		intent.putExtra(MediaStore.Images.Media.ORIENTATION, 10);
		// intent.setDataAndType(u, "image/*"); // 格式
		try {
			// intent.putExtra("crop", "true"); // 发送裁剪信号

		} catch (Exception e) {
			// TODO: handle exception
		}

		intent.putExtra("aspectX", 4); // X方向上的比例
		intent.putExtra("aspectY", 3); // Y方向上的比例
		intent.putExtra("outputX", 800f); // 裁剪区的宽
		intent.putExtra("outputY", 400f); // 裁剪区的高
		intent.putExtra("scale", false); // 是否保留比例

		intent.putExtra(MediaStore.EXTRA_OUTPUT, u); // 将URI指向相应的file:///
		intent.putExtra("return-data", false); // 是否将数据保留在Bitmap中返回
		startActivityForResult(intent, code);

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent data) {
		super.onActivityResult(arg0, arg1, data);

		if (arg1 != RESULT_OK) {
			return;
		} else if (arg0 == FRONT_CODE && arg1 == RESULT_OK) {
			handler.sendEmptyMessage(FRONT_CODE);
		} else if (arg0 == SCENS_CODE && arg1 == RESULT_OK) {
			handler.sendEmptyMessage(SCENS_CODE);
		} else {
			// handler.sendEmptyMessage(BANK_CODE);
		}

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {

			case FRONT_CODE:
//				Drawable drawable = new BitmapDrawable(getBitmap(FRONT_CODE));
//				// ivFront.setBackgroundDrawable(drawable);
//				ivFront.setBackgroundDrawable(drawable);// (getBitmap(FRONT_CODE));
				String path =  photoUri.getPath();
				upPhoto(new File(path),true,FRONT_CODE,ivFront);
				break;

			case SCENS_CODE:
//				Drawable drawabless = new BitmapDrawable(getBitmap(SCENS_CODE));
//				idcard_back_img.setBackgroundDrawable(drawabless);// (getBitmap
				String path1 =  photoUri.getPath();
				upPhoto(new File(path1),true,SCENS_CODE,idcard_back_img);
				break;
			// case BANK_CODE:
			//
			// break;

			default:
				break;
			}
		}
	};

	/*
	 * 得到拍摄的图片
	 */
	private Bitmap getBitmap(int code) {
		File dir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/DCIM/" + localTempImgFileName);// 设置存放目录
		Log.e("", "      = = = = = = =dir = = = = =" + dir);
		Log.e("", "      = = = = = = =filenames = = = = ="
				+ localTempImgFileName);
		// File f = new File(dir.getAbsoluteFile(), localTempImgFileName);
		if (dir.isFile()) {
			Log.e("", "有文件");

			// Bitmap bitmap =
			// getimage(Environment.getExternalStorageDirectory()
			// .getAbsolutePath() + "/DCIM/" + localTempImgFileName);
			
			Bitmap bitmap = compressBySize(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/DCIM/"
					+ localTempImgFileName, 768, 1280);
			saveBitmap2file(bitmap, localTempImgFileName);
		} else {
			Log.e("", "没有文件");
		}
		// BitmapFactory.Options opts = new BitmapFactory.Options();//
		// 获取缩略图显示到屏幕
		// opts.inSampleSize = 4;
		/* 下面两个字段需要组合使用 */
		// opts.inJustDecodeBounds = false;
		// opts.inPurgeable = true;
		// opts.inInputShareable = true;
		Bitmap cbitmap = getbitmap(dir.getAbsolutePath());
//				BitmapFactory.decodeFile(dir.getAbsolutePath());
		
		
		if (code == FRONT_CODE) {
			editor.putString("photo1",dir.toString());
			editor.putString("photoname1",localTempImgFileName);
			editor.commit();
			custPicFile = dir;
			idFrontPic = "ok";
		} else if (code == SCENS_CODE) {
			editor.putString("photo2",dir.toString());
			editor.putString("photoname2",localTempImgFileName);
			editor.commit();
			idcardPicFile = dir;
			scenePic = "ok";
		}
		return cbitmap;
	}

	public Bitmap compressBySize(String pathName, int targetWidth,
			int targetHeight) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
		// 得到图片的宽度、高度；
		float imgWidth = opts.outWidth;
		float imgHeight = opts.outHeight;
		// 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
		int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
		int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
		opts.inSampleSize = 1;
		if (widthRatio > 1 || widthRatio > 1) {
			if (widthRatio > heightRatio) {
				opts.inSampleSize = widthRatio;
			} else {
				opts.inSampleSize = heightRatio;
			}
		}
		// 设置好缩放比例后，加载图片进内容；
		opts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(pathName, opts);
		return bitmap;
	}

	private static boolean saveBitmap2file(Bitmap bmp, String filename) {
		CompressFormat format = Bitmap.CompressFormat.JPEG;
		int quality = 80;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/DCIM/"
					+ filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bmp.compress(format, quality, stream);
		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	
	private Bitmap getbitmap(String url){
		 InputStream is = null;
			try {
				is = new FileInputStream(url);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		BitmapFactory.Options opts=new BitmapFactory.Options();
		opts.inTempStorage = new byte[100 * 1024];
		//3.设置位图颜色显示优化方式
		//ALPHA_8：每个像素占用1byte内存（8位）
		//ARGB_4444:每个像素占用2byte内存（16位）
		//ARGB_8888:每个像素占用4byte内存（32位）
		//RGB_565:每个像素占用2byte内存（16位）
		//Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。但同样的，占用的内存//也最大。也就意味着一个像素点占用4个字节的内存。我们来做一个简单的计算题：3200*2400*4 bytes //=30M。如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		//4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
		opts.inPurgeable = true;
		//5.设置位图缩放比例
		//width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
		opts.inSampleSize = 4;
		//6.设置解码位图的尺寸信息
		opts.inInputShareable = true; 
		//7.解码位图
		Bitmap btp =BitmapFactory.decodeStream(is,null, opts);    
		Drawable drawable = new BitmapDrawable(btp);
		try {
			if(is!=null){
				is.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return btp;
	} 
	
	
	
	private void initphto(String photo,ImageView img,int tag){
		String pic = MyCacheUtil.getshared(AuthenticationActivity1.this).getString("photoname1", "");
//		Log.e("", "pic = = = "+pic);
//		if(pic.length()>11){
//			if(!pic.substring(0, 11).equals(custId)){
//				Log.e("", "pic.substring(0, 11) = = = "+pic.substring(0, 11));
//				return;
//			}
//		}
		if(photo!=null&&!photo.equals("")&&pic.length()>11&&pic.substring(0, 11).equals(custId)){
//			File dir = new File(photo);
//			Bitmap cbitmap = null;
//			try {
//				 cbitmap = BitmapFactory.decodeFile(dir.getAbsolutePath());
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//				if(cbitmap!=null){
//					Drawable drawable = new BitmapDrawable(cbitmap);
//					img.setBackgroundDrawable(drawable);
//				}
			 InputStream is = null;
				try {
					is = new FileInputStream(photo);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BitmapFactory.Options opts=new BitmapFactory.Options();
				opts.inTempStorage = new byte[100 * 1024];
				//3.设置位图颜色显示优化方式
				//ALPHA_8：每个像素占用1byte内存（8位）
				//ARGB_4444:每个像素占用2byte内存（16位）
				//ARGB_8888:每个像素占用4byte内存（32位）
				//RGB_565:每个像素占用2byte内存（16位）
				//Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。但同样的，占用的内存//也最大。也就意味着一个像素点占用4个字节的内存。我们来做一个简单的计算题：3200*2400*4 bytes //=30M。如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。
				opts.inPreferredConfig = Bitmap.Config.RGB_565;
				//4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
				opts.inPurgeable = true;
				//5.设置位图缩放比例
				//width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
				opts.inSampleSize = 4;
				//6.设置解码位图的尺寸信息
				opts.inInputShareable = true; 
				//7.解码位图
				Bitmap btp =BitmapFactory.decodeStream(is,null, opts);    
				Drawable drawable = new BitmapDrawable(btp);
				img.setBackgroundDrawable(drawable);
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}else if(tag==0&&newauthenticationbean.getIDCARDPIC()!=null&&!newauthenticationbean.getIDCARDPIC().equals("")){
			FinalBitmap.create(this).display(img,
					HttpUrls.HOST_POSM + newauthenticationbean.getIDCARDPIC(),
					img.getWidth(),
					img.getHeight(), null, null);
		}else if(tag==1&&newauthenticationbean.getIDCARDBACKPHOTO()!=null&&!newauthenticationbean.getIDCARDBACKPHOTO().equals("")){
			FinalBitmap.create(this).display(img,
					HttpUrls.HOST_POSM + newauthenticationbean.getIDCARDBACKPHOTO(),
					img.getWidth(),
					img.getHeight(), null, null);
		}
	};
	

	class AuthenticationTask extends
			AsyncTask<String, Integer, NewAuthenticationBean> {

		@Override
		protected void onPreExecute() {
			try {
				showLoadingDialog("正在查询中...");
			} catch (Exception e) {
				// TODO: handle exception
			}
			super.onPreExecute();
		}

		@Override
		protected NewAuthenticationBean doInBackground(String... params) {
			String[] values = { params[0], params[1]};
			return NetCommunicate.getauthentication(HttpUrls.CERTIFICATIONBEFORE,
					values);
		}

		@Override
		protected void onPostExecute(NewAuthenticationBean result) {
			try {
				loadingDialogWhole.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			if (result != null) {
				newauthenticationbean = result;
				if (Entity.STATE_OK.equals(result.getRspcod())) {
					if(result.getIDCARDPICSTA().equals("0")){
						String photo = MyCacheUtil.getshared(AuthenticationActivity1.this).getString("photo1", "");
						initphto(photo,ivFront,0);
						idFrontPic = "ok";
						tv_pro1.setText("身份证照片已认证通过");
						ivFront.setEnabled(false);
						button1.setEnabled(false);
						tv_pro1.setTextColor(getResources().getColor(R.color.red));
						button1.setVisibility(View.GONE);
					}
					if(result.getIDCARDBANKPICSTA().equals("0")){
						String photo = MyCacheUtil.getshared(AuthenticationActivity1.this).getString("photo2", "");
						initphto(photo,idcard_back_img,1);
						idcard_back_img.setEnabled(false);
						tv_pro2.setText("身份证背面照片已认证通过");
						tv_pro2.setTextColor(getResources().getColor(R.color.red));
						button2.setEnabled(false);
						button2.setVisibility(View.GONE);
						scenePic = "ok";
					}
						re_name.setText(result.getMERCNAM());
						re_id.setText(result.getCORPORATEIDENTITY());
						
				} else {
//					showSingleWarnDialog(result.getRspmsg().toString());
				}
			}
			super.onPostExecute(result);
		}

	}
	
	public void upPhoto(File f, boolean b,int fRONT_CODE2, ImageView img_photo)  {
		File setfile = null;
		
		String getPathname = "";
		if(!b){
			getPathname = getStringDateMerge();
		}else{
			getPathname = f.getName();
		}
		
	
		try {
            // 压缩图片
            String compressPath = PictureUtil.compressImage(AuthenticationActivity1.this, f.getPath(), getPathname, 65);
            setfile = new File(compressPath);
        } catch(Exception e) {
            e.printStackTrace();
        }
		
		Bitmap bitmap = BitmapFactory.decodeFile(f.getPath(),getBitmapOption(2)); //将图片的长和宽缩小味原来的1/2
//		custPicFile,idcardpic,ivFront,idFrontPic
		switch (fRONT_CODE2) {
		case FRONT_CODE:
			custPicFile = setfile;
			idFrontPic = "ok";
			idcardpic = setfile.getName();
			editor.putString("photo1",setfile.getPath());
			editor.putString("photoname1",setfile.getName());
			editor.commit();
			break;
		case SCENS_CODE:
			idcardPicFile = setfile;
			scenePic = "ok";
			custpic = setfile.getName();
			editor.putString("photo2",setfile.getPath());
			editor.putString("photoname2",setfile.getName());
			editor.commit();
			break;
		default:
			break;
		}
		img_photo.setImageBitmap(bitmap);
	}
	
	private Options getBitmapOption(int inSampleSize){
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
	}

}
