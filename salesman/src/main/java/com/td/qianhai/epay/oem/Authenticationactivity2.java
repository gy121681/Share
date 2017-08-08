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

import net.tsz.afinal.FinalBitmap;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.GetImageUtil;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.mail.utils.PictureUtil;
import com.td.qianhai.epay.oem.views.AnimationUtil;
import com.td.qianhai.epay.oem.views.MyTextWatcher;
import com.td.qianhai.epay.oem.views.city.CityPicker;
import com.td.qianhai.epay.oem.views.city.onChoiceCytyChilListener;

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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Authenticationactivity2 extends BaseActivity implements OnClickListener{
	private TextView tv_au_button,tv_pro1;
	private String name = "",id = "",banknum = "";
	private TextView tv_bank,bank_come,tv_finishs,tv_conmmit;
	private String banks = "";
	private EditText tv_banknum;
	private  CityPicker citypicker;
	private LinearLayout tv_finish;
	public static Activity con;
	private ImageView im_bank;
	private String bankProvinceid,bankCityid,bankareid;
	private ImageView iv_name_auth_front;
	private Button button1;
	private final int FRONT_CODE = 100;
	private String localTempImgFileName = "",idcardpic;
	private File custPicFile;
	private Editor editor;
	private String idFrontPic,custId;
	private Uri photoUri;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authentication_activity2);
		AppContext.getInstance().addActivity(this);
		Intent intent = getIntent();
		con = this;
		
		editor = MyCacheUtil.setshared(this);
		custId = MyCacheUtil.getshared(this).getString("Mobile", "");
		name = intent.getStringExtra("name");
		id = intent.getStringExtra("id");
		initview();
	}

	@SuppressLint("ResourceAsColor") private void initview() {

		// TODO Auto-generated method stub
		button1 = (Button) findViewById(R.id.button1);
		tv_pro1 = (TextView) findViewById(R.id.tv_pro1);
		button1.setOnClickListener(this);
		iv_name_auth_front = (ImageView) findViewById(R.id.iv_name_auth_front);
		iv_name_auth_front.setOnClickListener(this);
		tv_bank = (TextView) findViewById(R.id.tv_bank);
		((TextView) findViewById(R.id.tv_title_contre)).setText("实名认证");
		citypicker = (CityPicker) findViewById(R.id.citypicker);
		if(AuthenticationActivity1.newauthenticationbean.getFRYHKIMGPATHSTA().equals("0")){
			String pic = MyCacheUtil.getshared(Authenticationactivity2.this).getString("photoname3", "");
			if(pic.length()>11){
				if(!pic.substring(0, 11).equals(custId)){
//					return;
				}
			}
			String photo = MyCacheUtil.getshared(this).getString("photo3", "");
			initphto(photo,iv_name_auth_front);
			iv_name_auth_front.setEnabled(false);
			button1.setEnabled(false);
			button1.setVisibility(View.GONE);
			tv_pro1.setText("银行卡照片已认证通过");
			tv_pro1.setTextColor(this.getResources().getColor(R.color.red));
			idFrontPic = "ok";
		}

		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		tv_au_button = (TextView) findViewById(R.id.tv_au_button);
		tv_banknum = (EditText) findViewById(R.id.tv_banknum);
		tv_banknum.addTextChangedListener(new MyTextWatcher(tv_banknum));
		bank_come = (TextView) findViewById(R.id.bank_come);
		tv_finish = (LinearLayout) findViewById(R.id.tv_finish);
		tv_conmmit = (TextView) findViewById(R.id.tv_comit);
		tv_finishs = (TextView) findViewById(R.id.tv_finishs);
		im_bank = (ImageView) findViewById(R.id.im_bank);
		
		if(!AuthenticationActivity1.newauthenticationbean.getBANKACCOUNT().equals("")){
			tv_banknum.setText(AuthenticationActivity1.newauthenticationbean.getBANKACCOUNT());
		}
		tv_bank.setText(AuthenticationActivity1.newauthenticationbean.getBANKNAME());
		if(!AuthenticationActivity1.newauthenticationbean.getPROVNAME().equals("")){
			bank_come.setText(AuthenticationActivity1.newauthenticationbean.getPROVNAME()+AuthenticationActivity1.newauthenticationbean.getCITYNAME()+AuthenticationActivity1.newauthenticationbean.getAREANAME());
			 bankProvinceid = AuthenticationActivity1.newauthenticationbean.getPROVID();
			 bankCityid = AuthenticationActivity1.newauthenticationbean.getCITYID();
			 bankareid = AuthenticationActivity1.newauthenticationbean.getAREAID();
				tv_au_button.setEnabled(true);
		}
		
		tv_conmmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AnimationUtil.GoButton(Authenticationactivity2.this, tv_finish);
				tv_finish.setVisibility(View.GONE);
				citypicker.setfirstdata();
				if(tv_banknum.getText().toString().length()>=1){
					if(tv_bank.getText().toString().length()>=1){
						if(bank_come.getText().toString().length()>=1){
							tv_au_button.setEnabled(true);
						}else{
							tv_au_button.setEnabled(false);
						}
					}else{
						tv_au_button.setEnabled(false);
					}
				}else{
					tv_au_button.setEnabled(false);
			}
			}
		});
		
		
		tv_finishs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AnimationUtil.GoButton(Authenticationactivity2.this, tv_finish);
				tv_finish.setVisibility(View.GONE);
				
			}
		});
		
		bank_come.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AnimationUtil.GoTop(Authenticationactivity2.this, tv_finish);
				tv_finish.setVisibility(View.VISIBLE);
			}
		});
		
		
		citypicker.getcity(new onChoiceCytyChilListener() {
			
			@Override
			public void onClick(String i, String v, String a, String ni, String vi,
					String ai) {
				// TODO Auto-generated method stub
				bankProvinceid = i;
				bankCityid = v;
				bankareid = a;
				try {
					bank_come.setText(ni+vi+ai);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
			}
		});
		
		tv_au_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if (idFrontPic == null) {
					AnimationUtil.BtnSpecialAnmations1(Authenticationactivity2.this, iv_name_auth_front, 500);
					Toast.makeText(getApplicationContext(), "请拍摄银行卡正面照片",
							Toast.LENGTH_SHORT).show();
					// ToastCustom.showMessage(this, "请拍摄身份证正面照片");
					return;
				}
				 banknum = tv_banknum.getText().toString().replace(" ", "");
				Intent it = new Intent(Authenticationactivity2.this,
						AuthenticationActivity3.class);
				it.putExtra("bankProvinceid", bankProvinceid);
				it.putExtra("bankCityid", bankCityid);
				it.putExtra("bankareid", bankareid);
				it.putExtra("banknum", banknum);
				it.putExtra("name", name);
				it.putExtra("id", id);
				startActivity(it);
			}
		});
		tv_bank.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(Authenticationactivity2.this,SelectBankActivity.class);
				it.putExtra("tag", "3");
				startActivityForResult(it, 2);
			}
		});
		
		tv_banknum.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				if(s.length()>=1){
					if(tv_bank.getText().toString().length()>=1){
						if(bank_come.getText().toString().length()>=1){
							tv_au_button.setEnabled(true);
						}else{
							tv_au_button.setEnabled(false);
						}
					}else{
						tv_au_button.setEnabled(false);
					}
				}else{
					tv_au_button.setEnabled(false);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		idcardpic = custId + "_" + getStringDateMerge() + "_"
				+ "BK.jpg";
	}
	
	public String getStringDateMerge() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString+".png";
	}
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int arg1, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, arg1, data);
		
		if (requestCode == FRONT_CODE && arg1 == RESULT_OK) {
			handler.sendEmptyMessage(FRONT_CODE);
		}
		if(arg1==2){
			  banks = data.getExtras().getString("result");
			  tv_bank.setText(banks);
			  String imgurl =data.getExtras().getString("img");
				Bitmap bit = null;
				try {
					 bit = GetImageUtil.iscace( im_bank,HttpUrls.HOST_POSM+imgurl);
				} catch (Exception e) {
					// TODO: handle exception\
					Log.e("", ""+e.toString());
				}
				if(bit!=null){
					im_bank.setImageBitmap(bit);
				}else{
				}
				if(tv_banknum.getText().toString().length()>=1){
					if(tv_bank.getText().toString().length()>=1){
						if(bank_come.getText().toString().length()>=1){
							tv_au_button.setEnabled(true);
						}else{
							tv_au_button.setEnabled(false);
						}
					}else{
						tv_au_button.setEnabled(false);
					}
				}else{
					tv_au_button.setEnabled(false);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			photo(FRONT_CODE);
//			takePicture(FRONT_CODE);
			break;
		case R.id.iv_name_auth_front:
			photo(FRONT_CODE);
//			takePicture(FRONT_CODE);
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
		} 
		File f = new File(dir, localTempImgFileName);
		Log.e("fileNam", dir.getAbsolutePath() + localTempImgFileName);
		Uri u = Uri.fromFile(f);
		 intent.putExtra(MediaStore.Images.Media.ORIENTATION, 10);
//		 intent.setDataAndType(u, "image/*"); // 格式
		 try {
//			 intent.putExtra("crop", "true"); // 发送裁剪信号

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
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {

			case FRONT_CODE:
//				Drawable drawable = new BitmapDrawable(getBitmap(FRONT_CODE));
//				iv_name_auth_front.setBackgroundDrawable(drawable);//(getBitmap(FRONT_CODE));
				
				String path1 =  photoUri.getPath();
				upPhoto(new File(path1),true,FRONT_CODE,iv_name_auth_front);
				break;

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
			
			
//			Bitmap bitmap = getimage(Environment.getExternalStorageDirectory()
//					.getAbsolutePath() + "/DCIM/" + localTempImgFileName);
			
			Bitmap bitmap = compressBySize(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/DCIM/" + localTempImgFileName,768,1280);
			saveBitmap2file(bitmap, localTempImgFileName);
		} else {
			Log.e("", "没有文件");
		}
//		BitmapFactory.Options opts = new BitmapFactory.Options();// 获取缩略图显示到屏幕
//		opts.inSampleSize = 4;
		/* 下面两个字段需要组合使用 */
		// opts.inJustDecodeBounds = false;
		// opts.inPurgeable = true;
		// opts.inInputShareable = true;
		Bitmap cbitmap = getbitmap(dir.getAbsolutePath());
				
				//BitmapFactory.decodeFile(dir.getAbsolutePath());
		if (code == FRONT_CODE) {
			editor.putString("photo3",dir.toString());
			editor.putString("photoname3",localTempImgFileName);
			editor.commit();
			custPicFile = dir;
			idFrontPic = "ok";
		}
		return cbitmap;
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
        //设置好缩放比例后，加载图片进内容；  
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
	
	private void initphto(String photo,ImageView img){
		String pic = MyCacheUtil.getshared(Authenticationactivity2.this).getString("photoname1", "");
//		if(pic.length()>11){
//			if(!pic.substring(0, 11).equals(custId)){
//				Log.e("", "pic.substring(0, 11) = = = "+pic.substring(0, 11));
////				return;
//			}else{
//				if(!AuthenticationActivity1.newauthenticationbean.getIDCARDBACKPHOTO().equals("")){
//					FinalBitmap.create(this).display(img,
//							HttpUrls.HOST_POSM + AuthenticationActivity1.newauthenticationbean.getIDCARDBACKPHOTO(),
//							img.getWidth(),
//							img.getHeight(), null, null);
//				}
//			}
//			
//		}
//		
		if(photo!=null&&!photo.equals("")&&pic.length()>11&&pic.substring(0, 11).equals(custId)){
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
//			File dir = new File(photo);
//			Bitmap cbitmap = BitmapFactory.decodeFile(dir.getAbsolutePath());	
//			Drawable drawable = new BitmapDrawable(cbitmap);
//			img.setBackgroundDrawable(drawable);
		}else if(!AuthenticationActivity1.newauthenticationbean.getIDCARDBACKPHOTO().equals("")){
			FinalBitmap.create(this).display(img,
					HttpUrls.HOST_POSM + AuthenticationActivity1.newauthenticationbean.getIDCARDBACKPHOTO(),
					img.getWidth(),
					img.getHeight(), null, null);
		}
	};
	
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
            String compressPath = PictureUtil.compressImage(Authenticationactivity2.this, f.getPath(), getPathname, 65);
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
			
			editor.putString("photo3",setfile.getPath());
			editor.putString("photoname3",setfile.getName());
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
