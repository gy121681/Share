package com.shareshenghuo.app.shop;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.amap.api.maps2d.model.Text;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.shareshenghuo.app.shop.manager.ImageLoadManager;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.UserInfo;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.BitmapTool;
import com.shareshenghuo.app.shop.util.DESKey;
import com.shareshenghuo.app.shop.util.FileUtil;
import com.shareshenghuo.app.shop.widget.CircleImageView;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.ShareMenuWindow;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecommendActivity extends BaseTopActivity{
	
	private String mobile;
	private String url = "";
	private ImageView myqr_img;
	private TextView tv_share;
	private TextView tvNick,tvMobile;
	private TwoButtonDialog downloadDialog;
	private CircleImageView ivAvatar;
	private LinearLayout re_layout;
	private ViewGroup title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myrecommend_activity);
        mobile = UserInfoManager.getUserInfo(RecommendActivity.this).band_mobile;
        url =  Api.URL_SHARE_APP + "&sign=" +inithtml();
        initView();
        
    }
    
    public void upview(){
    	UserInfo user = UserInfoManager.getUserInfo(this);
    	if(TextUtils.isEmpty(user.nick_name)){
    		tvNick.setText("未设置");
    	}else{
    		tvNick.setText(user.nick_name);
    	}
    	if(TextUtils.isEmpty(user.band_mobile)){
    		tvMobile.setText("未设置");
    	}else{
    		tvMobile.setText(user.band_mobile);
    	}
    	
    	ImageLoadManager.getInstance(this).displayHeadIconImage(user.user_photo, ivAvatar);
    
    	
    }
    


	private void initView() {
		// TODO Auto-generated method stub
		initTopBar("推广二维码");
		myqr_img = getView(R.id.myqr_img);
		tv_share = getView(R.id.tv_share);
		title = getView(R.id.title);
		re_layout = getView(R.id.re_layout);
		ivAvatar = (CircleImageView)findViewById(R.id.ivUserAvatars);
		tvNick = (TextView) findViewById(R.id.tvUserNick);
		tvMobile = (TextView) findViewById(R.id.tvUserMobile);
		upview();
		myqr_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ShareMenuWindow window = new ShareMenuWindow(RecommendActivity.this);
				window.url = url;
				window.title = getResources().getString(R.string.app_name);
				window.content = "秀儿代代孝,公益辈辈传";
				window.showAtBottom();
			}
		});
		myqr_img.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				save();
				return false;
			}
		});
		re_layout.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				save();
				return false;
			}
		});
		
		 Bitmap logoBmp = BitmapFactory.decodeResource(getResources(),
		 R.drawable.ic_launcher);
		
		try {
			myqr_img.setImageBitmap(BitmapTool.createCode(url,logoBmp,BarcodeFormat.QR_CODE,450,30));
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("推荐记录");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(RecommendActivity.this, RecommendedmemberActivity.class));
			}
		});
	}
	
	public void save(){

		File dir = new File(Environment.getExternalStorageDirectory()+"/share/");
		if (!dir.exists()){
			dir.mkdirs();
		}
		
		downloadDialog = new TwoButtonDialog(RecommendActivity.this, R.style.CustomDialog,
				"提示", "是否保存图片?", "取消", "确定",true,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
						
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							FileOutputStream m_fileOutPutStream = null;
							String filepath = Environment.getExternalStorageDirectory() //+File.separator
									+"/share/"+ "ermamyqrcode.png";
							Log.e("", ""+filepath);
							try {
								m_fileOutPutStream = new FileOutputStream(filepath);
							}catch (FileNotFoundException e) {
								e.printStackTrace();
							}
							
							Bitmap mp = shot(RecommendActivity.this);
							mp.compress(CompressFormat.PNG, 100, m_fileOutPutStream);
							try {
								m_fileOutPutStream.flush();
								m_fileOutPutStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							FileUtil.setphotopath(RecommendActivity.this, filepath);
//							MediaStore.Images.Media.insertImage(getContentResolver(), mp, "title", "description");
							downloadDialog.dismiss();
							Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
		
	}
	
	private String  inithtml() {
		// TODO Auto-generated method stub

//		JSONObject jsonObj = new JSONObject();
//		try {
//			jsonObj.put("PHONENUMBER", mobile);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String aa = null;
		try {
			aa = DESKey.AES_Encode(mobile,
					"f15f1ede25a2471998ee06edba7d2e29");
			aa = URLEncoder.encode(aa);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aa;
	}
	
	public void savebitmap(ImageView img){
		Bitmap bitmap = null;
		
		 bitmap = convertViewToBitmap(img);
		
		FileOutputStream m_fileOutPutStream = null;
		String filepath = Environment.getExternalStorageDirectory() +File.separator
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
		MediaStore.Images.Media.insertImage(RecommendActivity.this.getContentResolver(), bitmap, "title", "description");
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
	
	private Bitmap shot(Activity activity) {   
	       //View是你需要截图的View   
	         View view = activity.getWindow().getDecorView();   
	         view.setDrawingCacheEnabled(true);   
	         view.buildDrawingCache();   
	         Bitmap b1 = view.getDrawingCache();   
	       // 获取状态栏高度 /  
	           Rect frame = new Rect();  
	         activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
	         int statusBarHeight = frame.top+title.getHeight();  
	         // 获取屏幕长和高  
	         int width = activity.getWindowManager().getDefaultDisplay().getWidth();  
	         int height = activity.getWindowManager().getDefaultDisplay().getHeight();  
	        // 去掉标题栏  
	            Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);  
	         Bitmap bs = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);  
	        view.destroyDrawingCache();  
	         return bs;  
	    }  
}
