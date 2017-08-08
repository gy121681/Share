package com.shareshenghuo.app.shop;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shareshenghuo.app.shop.adapter.GridAdapter;
import com.shareshenghuo.app.shop.adapter.ShopPhotoGridAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.ShopPhoneBean;
import com.shareshenghuo.app.shop.network.request.SaveShopPhotoRequest;
import com.shareshenghuo.app.shop.network.request.ShopCategoryRequest;
import com.shareshenghuo.app.shop.network.response.AutResponse;
import com.shareshenghuo.app.shop.network.response.FileUploadResponse;
import com.shareshenghuo.app.shop.network.response.ShopTypePhotoResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.photo.AlbumActivity;
import com.shareshenghuo.app.shop.photo.Bimp;
import com.shareshenghuo.app.shop.photo.GalleryActivity;
import com.shareshenghuo.app.shop.photo.ImageItem;
import com.shareshenghuo.app.shop.photo.PublicWay;
import com.shareshenghuo.app.shop.util.BitmapTool;
import com.shareshenghuo.app.shop.util.ImageTools;
import com.shareshenghuo.app.shop.util.PictureUtil;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;


public class ShopPhotoAcitivty extends BaseTopActivity{
	
	private GridView gvShopPhoto;
	private String phototype,title;
	private ShopPhotoGridAdapter sim_adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private Uri photoUri;
	private StringBuffer photostr = new StringBuffer();
	private static final int SCALE = 5;//照片缩小比例
	private boolean ischoose = false;
	private TwoButtonDialog downloadDialog;
	public  boolean istag = false;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		parentView = getLayoutInflater().inflate(R.layout.shop_photo_acitivty, null);
		setContentView(parentView);
//		PublicWay.num = 7;
		phototype = getIntent().getStringExtra("type");
		title =getIntent().getStringExtra("title");
	
		initpop();
		initdata();
		initview();
	}
	

	private void initdata() {
		// TODO Auto-generated method stub
		initTopBar(title);
		gvShopPhoto  = getView(R.id.gvShopPhoto);
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("上传");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(istag){
					savephoto();
					istag = false;
				}else{
					finish();
				}
			}
		});
		llTopBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(istag){
					initDialog("图片已修改，是否上传?", "否", "是");
					istag = false;
				}else{
					finish();
				}
			}
		});
		
//		if(Bimp.tempSelectBitmap.size()<=0){
//		Bimp.tempSelectBitmap.add(new ImageItem());
//		}else{
//			
//		}
		sim_adapter = new ShopPhotoGridAdapter(ShopPhotoAcitivty.this);
		gvShopPhoto.setAdapter(sim_adapter);
		gvShopPhoto.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ShopPhotoAcitivty.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				
				if (Bimp.tempSelectBitmap.get(arg2).getBitmap() == null&&Bimp.tempSelectBitmap.get(arg2).url==null) {
					
					ll_popup.startAnimation(AnimationUtils.loadAnimation(ShopPhotoAcitivty.this,R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
//					istag = true;
//					Intent intent = new Intent(ShopPhotoAcitivty.this,
//							GalleryActivity.class);
//					intent.putExtra("position", "1");
//					intent.putExtra("ID", arg2);
//					startActivity(intent);
					
					ArrayList<String> arr = new ArrayList<String>();
					for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
						if(Bimp.tempSelectBitmap.get(i).url!=null){
							arr.add(Bimp.tempSelectBitmap.get(i).url);
						}
					}
					
					Intent it = new Intent(ShopPhotoAcitivty.this, ImagePager1Activity.class);
					it.putExtra("title", "浏览");
					it.putExtra("position", arg2);
					it.putStringArrayListExtra("urls", arr);
					startActivity(it);	
				}
			}
		});
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
    	Bimp.tempSelectBitmap.clear();
		Bimp.max = 0;

	}
	
//	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		sim_adapter.update();
	}
	
	
	
	private void initview() {
		// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			ProgressDialogUtil.showProgressDlg(ShopPhotoAcitivty.this, "请稍候");
			ShopCategoryRequest req = new ShopCategoryRequest();
			req.shopId = UserInfoManager.getUserInfo(this).shop_id;
			req.photoType = phototype;
			RequestParams params = new RequestParams();
		
			try {
				params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			new HttpUtils().send(HttpMethod.POST, Api.GETSHOPDIFFERENTPHOTOTYPELIST, params, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					ProgressDialogUtil.dismissProgressDlg();
						T.showNetworkError(getApplicationContext());
				}

				@Override
				public void onSuccess(ResponseInfo<String> resp) {
					ProgressDialogUtil.dismissProgressDlg();
					ShopTypePhotoResponse bean = new Gson().fromJson(resp.result, ShopTypePhotoResponse.class);
						if(Api.SUCCEED == bean.result_code) {
							init(bean.data);
							Log.e("", ""+resp.result);
						} else {
							T.showShort(getApplicationContext(), bean.result_desc);
						}
				}


			});
	}
	

	private void init(List<ShopPhoneBean> data) {
		// TODO Auto-generated method stub
		if(data!=null&&data.size()>0){
			for (int i = 0; i < data.size(); i++) {
				 ImageItem item = new ImageItem();
				 item.url = data.get(i).photo_url;
				 Bimp.tempSelectBitmap.add(item);
			}
		}
		 if(Bimp.tempSelectBitmap.size()<PublicWay.num){
			 Bimp.tempSelectBitmap.add(new ImageItem());
		 }
//		 if(Bimp.tempSelectBitmap.size()<=0){
//			 Bimp.tempSelectBitmap.add(new ImageItem());
//		 }
		sim_adapter.update();
		
	}
	
	public void initpop(){
		pop = new PopupWindow(ShopPhotoAcitivty.this);
		
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view
				.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view
				.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view
				.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				istag = true;
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				istag = true;
				Intent intent = new Intent(ShopPhotoAcitivty.this,
						AlbumActivity.class);
				startActivityForResult(intent, TAKE_PICTURE);
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
	}
	
	public  static final int TAKE_PICTURE = 1;

	public void photo() {
		
		File dir = new File(Environment.getExternalStorageDirectory()+"/share/");
		if (!dir.exists()){
			dir.mkdirs();
		}
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		photoUri= Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/share/",String.valueOf(System.currentTimeMillis())+"image.png"));
		//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
	
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < PublicWay.num && resultCode == RESULT_OK) {
				String path =  photoUri.getPath();
				Log.e("", " - - -  "+path);
//				if (path == null) {
//				Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
//				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
//				//由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
//				bitmap.recycle();
//				File file =ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath()+ "/share/", String.valueOf(System.currentTimeMillis()));
//					Bimp.tempSelectBitmap.get(Bimp.tempSelectBitmap.size()-1).setImagePath(file.getPath()) ;
//					if(Bimp.tempSelectBitmap.size()<PublicWay.num){
//					 Bimp.tempSelectBitmap.add(new ImageItem());
//					}
//					sim_adapter.update();
//				} else {
				
//					
					Bimp.tempSelectBitmap.get(Bimp.tempSelectBitmap.size()-1).setImagePath(path);
					 if(Bimp.tempSelectBitmap.size()<PublicWay.num){
						 Bimp.tempSelectBitmap.add(new ImageItem());
					 }
			}
		}
	}
	
	public String upPhoto(File file) {
		 String compressPath = "";
		 
		try {
            // 压缩图片
			compressPath = PictureUtil.compressImage(ShopPhotoAcitivty.this, file.getPath(), file.getName(), 65);
        } catch(Exception e) {
            e.printStackTrace();
        }
		return compressPath;
	}

	private void savephoto() {
		// TODO Auto-generated method stub
		if(Bimp.tempSelectBitmap.size()<1){
			return;
		}
		photostr = null;
		photostr = new StringBuffer();
		for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
			if(Bimp.tempSelectBitmap.get(i).imagePath!=null){
				ischoose = true;
				upPhotos(new File(Bimp.tempSelectBitmap.get(i).imagePath),i,Bimp.tempSelectBitmap.size()-1);
			}else if(Bimp.tempSelectBitmap.get(i).url!=null){
				photostr.append(Bimp.tempSelectBitmap.get(i).url+",");
			}else{
//				if(Bimp.tempSelectBitmap.size()<=1){
//					T.showShort(getApplicationContext(), "请选择照片");
//					return;
//				}
			}
			if(!ischoose){
				loadphoto();
			}
		}
	}
	
	public void upPhotos(File file, final int i, final int j) {
		try {
            // 压缩图片

            String compressPath = PictureUtil.compressImage(ShopPhotoAcitivty.this, file.getPath(), file.getName(), 50);
            	
			file = new File(compressPath);
            
        } catch(Exception e) {
            e.printStackTrace();
        }
		if(i==0){
			ProgressDialogUtil.showProgressDlg(ShopPhotoAcitivty.this, "请稍候..");
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("business_type", UserInfoManager.getUserId(ShopPhotoAcitivty.this)+"");
		params.addBodyParameter("1", file);
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPLOAD_FILE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String msg) {
				T.showNetworkError(ShopPhotoAcitivty.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				if(resp.statusCode==200 && resp.result!=null) {
					FileUploadResponse bean = new Gson().fromJson(resp.result, FileUploadResponse.class);
					if(Api.SUCCEED == bean.result_code) {
//						Log.e("", ""+bean.data.get(0));
						photostr.append(bean.data.get(0)+",");
//						rechange();
								if (i >= (j-1)) {
										loadphoto();
									}
									
					} else {
						T.showShort(ShopPhotoAcitivty.this, bean.result_desc);
					}
				}
			}
		});
	}
	
	private void loadphoto() {
//		if(Bimp.tempSelectBitmap.size()<=1){
//			T.showShort(getApplicationContext(), "请选择照片");
//			return;
//		}
		ProgressDialogUtil.showProgressDlg(ShopPhotoAcitivty.this, "请稍候");
		SaveShopPhotoRequest req = new SaveShopPhotoRequest();
		req.shopId = UserInfoManager.getUserInfo(this).shop_id;
		req.photo_type = phototype;
		if(phototype.equals("4")){
			req.shopPhotoUrls =  photostr.toString();
		}else if(phototype.equals("1")){
			req.storeEnvironmentPhotoUrls =  photostr.toString();
		}else if(phototype.equals("2")){
			req.servicesPhotoUrls =  photostr.toString();
		}else if(phototype.equals("5")){
			req.otherPhotoUrl =  photostr.toString();
		}
		Log.e("", ""+req.toString());
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.UPDATESHOPINFO, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
					T.showNetworkError(getApplicationContext());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						T.showShort(getApplicationContext(),bean.data.RSPMSG);
						finish();
					} else {
						T.showShort(getApplicationContext(), bean.result_desc);
					}
			}
		});
	}
	
	private void initDialog(String content,String left,String right) {
		// TODO Auto-generated method stub
		downloadDialog = new TwoButtonDialog(ShopPhotoAcitivty.this, R.style.CustomDialog,
				"", content, left, right,true,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							finish();
							break;
						case R.id.Button_cancel:
							savephoto();
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
			
	}

	
}
