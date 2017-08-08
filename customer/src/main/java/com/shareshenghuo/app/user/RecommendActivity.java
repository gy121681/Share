package com.shareshenghuo.app.user;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.entity.StringEntity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.ImageLoadManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.network.request.UpdateUserInfoRequest;
import com.shareshenghuo.app.user.network.response.LoginResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.BitmapTool;
import com.shareshenghuo.app.user.util.DESKey;
import com.shareshenghuo.app.user.util.FileUtil;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.CircleImageView;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.user.widget.dialog.ShareMenuWindow;
import com.shareshenghuo.app.user.widget.dialog.TwoButtonDialog;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class RecommendActivity extends BaseTopActivity{
	
	private String mobile;
	private String url = "";
	private ImageView myqr_img;
	private TextView tv_share;
	private CircleImageView ivAvatar;
	private TextView tvNick,tvMobile;
	private TwoButtonDialog downloadDialog;
	private LinearLayout re_layout;
	 private ViewGroup title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myrecommend_activity);
        mobile = UserInfoManager.getUserInfo(RecommendActivity.this).mobile;
        url =  Api.URL_SHARE_APP + "&sign=" +inithtml();
        Log.e("", ""+url);
        initView();
        loadUserInfo();
     
        
    }

	private void initView() {
		// TODO Auto-generated method stub
		initTopBar("推广二维码");
		myqr_img = getView(R.id.myqr_img);
		title = getView(R.id.title);
		ivAvatar = (CircleImageView)findViewById(R.id.ivUserAvatars);
		tvNick = (TextView) findViewById(R.id.tvUserNick);
		tvMobile = (TextView) findViewById(R.id.tvUserMobile);
		re_layout = getView(R.id.re_layout);
		tv_share = getView(R.id.tv_share);
		
		
		myqr_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ShareMenuWindow window = new ShareMenuWindow(RecommendActivity.this);
				window.url = url;
				window.title = getResources().getString(R.string.app_name);
				window.content = "秀儿代代孝,公益辈辈传";
				window.showAtBottom1();
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
				
//					savebitmap(myqr_img);
			
				save();
				return false;
			}
		});
		
		 Bitmap logoBmp = BitmapFactory.decodeResource(getResources(),
		 R.drawable.ic_launcher);
		
		try {
			myqr_img.setImageBitmap(BitmapTool.createCode(url,logoBmp,BarcodeFormat.QR_CODE,400,30));
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
	
	
	public void  save(){
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
									+"/share/"+ "myqrcode.png";
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
	
	public void loadUserInfo() {
//		ProgressDialogUtil.showProgressDlg(activity, "");
		UpdateUserInfoRequest req = new UpdateUserInfoRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		new HttpUtils().send(HttpMethod.POST, Api.URL_GET_USERINFO, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
					T.showNetworkError(RecommendActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				LoginResponse bean = new Gson().fromJson(resp.result, LoginResponse.class);
				if(Api.SUCCEED==bean.result_code) {
//					UserInfoManager.saveUserInfo(this, bean.data);
					updateView(bean.data);
				}
			}
		});
	}
	
	public void updateView(UserInfo data) {
		
		if(!TextUtils.isEmpty(data.real_name)){
			tvNick.setText(data.real_name);
		}else if(!TextUtils.isEmpty(data.nick_name)){
			tvNick.setText(data.nick_name);
		}else{
			tvNick.setText("未实名认证");
		}
		
//		if(TextUtils.isEmpty(data.nick_name)){
//			tvNick.setText("未实名认证");
//		}else{
//			tvNick.setText(data.nick_name);
//		}
		
		if(TextUtils.isEmpty(data.mobile)){
			tvMobile.setText("未知");
		}else{
			tvMobile.setText(data.mobile);
		}
		ImageLoadManager.getInstance(this).userphoto(data.user_photo, ivAvatar);
		
		
//		tvMoney.setText(data.money+"");
//		setText(R.id.tvCouponCount, data.coupon_count+"");
//		setText(R.id.tvUserPoint, data.point+"");
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
				+ getStringDateMerge()+"ermamypayqrcode1.png";
		try {
			m_fileOutPutStream = new FileOutputStream(filepath);
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
//		obmp = BitmapFactory.decodeFile(filepath, newOpts);
		Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
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
