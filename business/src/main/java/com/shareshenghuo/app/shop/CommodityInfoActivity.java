package com.shareshenghuo.app.shop;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.adapter.AddPhotoGridAdapter;
import com.shareshenghuo.app.shop.adapter.EtcAdapter;
import com.shareshenghuo.app.shop.adapter.GridAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.PhotoBean;
import com.shareshenghuo.app.shop.network.bean.ShopCategoryBean;
import com.shareshenghuo.app.shop.network.bean.ShopInfoByIdBean;
import com.shareshenghuo.app.shop.network.request.ShopCategoryRequest;
import com.shareshenghuo.app.shop.network.response.AutResponse;
import com.shareshenghuo.app.shop.network.response.FileUploadResponse;
import com.shareshenghuo.app.shop.network.response.ShopCategoryResponse;
import com.shareshenghuo.app.shop.network.response.ShopinfoByIdResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.photo.AlbumActivity;
import com.shareshenghuo.app.shop.photo.Bimp;
import com.shareshenghuo.app.shop.photo.GalleryActivity;
import com.shareshenghuo.app.shop.photo.ImageItem;
import com.shareshenghuo.app.shop.photo.PublicWay;
import com.shareshenghuo.app.shop.util.CheckEnji;
import com.shareshenghuo.app.shop.util.FileUtil;
import com.shareshenghuo.app.shop.util.ImageTools;
import com.shareshenghuo.app.shop.util.PictureUtil;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;
import com.shareshenghuo.app.shop.widget.MyGridView;
import com.shareshenghuo.app.shop.widget.dialog.MyEditDialog1;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;
import com.shareshenghuo.app.shop.widget.dialog.onMyaddTextListener;

public class CommodityInfoActivity extends BaseTopActivity {
	
	private TwoButtonDialog downloadDialog;
	private List<ShopCategoryBean> datas;
	private RelativeLayout re_type;
	private TextView tv_type;
	private AddPhotoGridAdapter adapter;
	private EtcAdapter typeadapter;
	private ListView listivew;
	private View parentView;
	private List<PhotoBean> data;
	private MyEditDialog1 doubleWarnDialog1;
	public static Bitmap bimap ;
	private LinearLayout ll_popup;
	private PopupWindow pop = null;
	private  GridAdapter Gridadapter;
	private  StringBuilder strshopsype =new StringBuilder();
	private EditText edShopName,content,edprice,edmodel;
	private   PopupWindow popupWindow;
	private String id,typeid;
	private StringBuffer photostr = new StringBuffer();
	private boolean ischoose = false;
	private  String[] type ;
	private Uri photoUri;
	private static final int SCALE = 5;//照片缩小比例
	private ShopInfoByIdBean datainfo;
	private String[] ptlist ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		PublicWay.num = 7;
		parentView = getLayoutInflater().inflate(R.layout.commodity_info_activity, null);
		setContentView(parentView);
		id = getIntent().getStringExtra("id");
		typeid= getIntent().getStringExtra("typeid");
		initpopwind();
		initView();
		
		if(id!=null){
			loaddata1();
		}else{
		if(Bimp.tempSelectBitmap.size()<=0){
		Bimp.tempSelectBitmap.add(new ImageItem());
		}
		}
		if(typeid!=null){
//			re_type.setVisibility(View.GONE);
//			strshopsype.append(typeid);
		}
//		initd();
	}

	public void initView() {
		initTopBar("编辑商品");
		btnTopRight1.setText("完成");
		tv_type = getView(R.id.tv_type);
		re_type = getView(R.id.re_type);
		edShopName = getView(R.id.edShopName);
		content = getView(R.id.content);
		edprice = getView(R.id.edprice);
		edmodel = getView(R.id.edmodel);
		llTopBack = (LinearLayout) findViewById(R.id.llTopBack);
		
		btnTopRight1.setVisibility(View.VISIBLE);
		
		btnTopRight1.setText("完成");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if(ViewUtil.checkEditEmpty(edShopName, "请填写商品名称")){
					T.showShort(getApplicationContext(), "请填写商品名称");
					return;
				}
				
				
				if(xiaolian(edShopName.getText().toString())||CheckEnji.containsEmoji(edShopName.getText().toString())){
					T.showShort(getApplicationContext(), "非法字符");
					return;
				}
				
				if(id==null){
					if(Bimp.tempSelectBitmap.size()<=1){
						T.showShort(getApplicationContext(), "请选择照片");
						return;
					}
				}
				if(ViewUtil.checkEditEmpty(content, "请填写商品描述")){
					T.showShort(getApplicationContext(), "请填写商品描述");
					return;
				}
//				if(judgetext(content.getText().toString())){
//					T.showShort(getApplicationContext(), "非法字符");
//					return;
//				}
				if(ViewUtil.checkEditEmpty(edprice, "请填写商品价格")){
					T.showShort(getApplicationContext(), "请填写商品价格");
					return;
				}
				
				if(judgetext(edmodel.getText().toString())){
					T.showShort(getApplicationContext(), "非法字符");
					ViewUtil.showError(edmodel, "非法字符");
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
						if(Bimp.tempSelectBitmap.size()<=1){
							T.showShort(getApplicationContext(), "请选择照片");
							return;
						}
					}
				}
				if(!ischoose&&id!=null){
					rechange();
				}
			}

		});
		llTopBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isout()){
					initDialog("退出本次编辑?","否","是");
				}else{
					finish();
				}
			}
		});
		
				
		re_type.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					initd(false);
					showPopupWindow(tv_type);
			}
		});
		initpop();
		MyGridView gvPhoto = getView(R.id.gvShopPhoto);
//		data = new ArrayList<PhotoBean>();
//		data.add(new PhotoBean());
//		adapter = new AddPhotoGridAdapter(this, data, gvPhoto);
//		gvPhoto.setAdapter(adapter);
//		if(Bimp.tempSelectBitmap.size()<=0){
//			Bimp.tempSelectBitmap.add(new ImageItem());
//		}
		gvPhoto.setSelector(new ColorDrawable(Color.TRANSPARENT));
		Gridadapter = new GridAdapter(this);
//		Gridadapter.update();
		gvPhoto.setAdapter(Gridadapter);
		gvPhoto.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(CommodityInfoActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				
				if (Bimp.tempSelectBitmap.get(arg2).getBitmap() == null&&Bimp.tempSelectBitmap.get(arg2).url==null) {
					ll_popup.startAnimation(AnimationUtils.loadAnimation(CommodityInfoActivity.this,R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(CommodityInfoActivity.this,
							GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
		
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
    	Bimp.tempSelectBitmap.clear();
		Bimp.max = 0;
    }
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Gridadapter.update();
//		Gridadapter.update();
	}
	
	public void initpop(){
		pop = new PopupWindow(CommodityInfoActivity.this);
		
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
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(CommodityInfoActivity.this,
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
		try {
			photoUri= Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/share/",String.valueOf(System.currentTimeMillis())+"image.png"));
		} catch (Exception e) {
			// TODO: handle exception
			photoUri= Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
		}
		
		//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
	
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < PublicWay.num && resultCode == RESULT_OK) {
				String path =  photoUri.getPath();
//				String path = FileUtil.getPath((Activity) CommodityInfoActivity.this, data.getData());
				if (path == null) {
//				Bitmap bm = (Bitmap) data.getExtras().getParcelable("data");
//				File file = BitmapTool.saveBitmap(bm);
//				FileUtils.saveBitmap(bm, fileName);
//				path = upPhoto(BitmapTool.Bitmap2File(CommodityInfoActivity.this, bm));
//					Bundle extras = data.getExtras();
//					if (extras != null) {
//						Bitmap bmp = extras.getParcelable("data");
//						if (bmp != null) {
//							upPhoto(BitmapTool.Bitmap2File(CommodityInfoActivity.this, bmp));
//						}
//					}
//				
				Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				//由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				bitmap.recycle();
				
				File file =ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath()+ "/share/", String.valueOf(System.currentTimeMillis()));
				
					Bimp.tempSelectBitmap.get(Bimp.tempSelectBitmap.size()-1).setImagePath(file.getPath()) ;
					if(Bimp.tempSelectBitmap.size()<PublicWay.num){
					 Bimp.tempSelectBitmap.add(new ImageItem());
					}
					Gridadapter.update();
				} else {
					Bimp.tempSelectBitmap.get(Bimp.tempSelectBitmap.size()-1).setImagePath(path);
					 if(Bimp.tempSelectBitmap.size()<PublicWay.num){
						 Bimp.tempSelectBitmap.add(new ImageItem());
					 }
					 Gridadapter.update();
					 
				}
			}
		}
	}
	
	public String upPhoto(File file) {
		 String compressPath = "";
		try {
            // 压缩图片
			compressPath = PictureUtil.compressImage(CommodityInfoActivity.this, file.getPath(), file.getName(), 65);
        } catch(Exception e) {
            e.printStackTrace();
        }
		return compressPath;
	}
	
	
	public void upPhotos(File file, final int i, final int j) {
		try {
            // 压缩图片
            String compressPath = PictureUtil.compressImage(CommodityInfoActivity.this, file.getPath(), file.getName(), 65);
            file = new File(compressPath);
        } catch(Exception e) {
            e.printStackTrace();
        }
		if(i==0){
			ProgressDialogUtil.showProgressDlg(CommodityInfoActivity.this, "请稍候..");
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("business_type", UserInfoManager.getUserId(CommodityInfoActivity.this)+"");
		params.addBodyParameter("1", file);
		Log.e("", ""+UserInfoManager.getUserId(CommodityInfoActivity.this)+"");
		Log.e("", ""+Api.URL_UPLOAD_FILE);
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPLOAD_FILE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String msg) {
				T.showNetworkError(CommodityInfoActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				if(resp.statusCode==200 && resp.result!=null) {
					FileUploadResponse bean = new Gson().fromJson(resp.result, FileUploadResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						photostr.append(bean.data.get(0)+",");
//						rechange();
								if (i >= (j - 1)) {
									if (id != null) {
										rechange();
									} else {
										loaddata();
									}
									
								}
					} else {  
						T.showShort(CommodityInfoActivity.this, bean.result_desc);
					}
				}
			}
		});
	}
	
	
	
	private void loaddata() {
//		if(ViewUtil.checkEditEmpty(edShopName, "请填写商品名称")){
//			return;
//		}
//		if(ViewUtil.checkEditEmpty(edprice, "请填写商品价格")){
//			return;
//		}
//		
//		if(judgetext(edmodel.getText().toString())){
//			ViewUtil.showEditError(edmodel, "非法字符");
//			return;
//		}
		if(Bimp.tempSelectBitmap.size()<=1){
			T.showShort(getApplicationContext(), "请选择照片");
			return;
		}


		
		
		ProgressDialogUtil.showProgressDlg(CommodityInfoActivity.this, "请稍候");
		ShopCategoryRequest req = new ShopCategoryRequest();
		req.shopId = UserInfoManager.getUserInfo(this).shop_id+"";
		req.name = edShopName.getText().toString();
		req.photo = photostr.toString();
		req.description = content.getText().toString();
		req.price = edprice.getText().toString();
		req.model = edmodel.getText().toString();
		req.typeId = strshopsype.toString();
		Log.e("", ""+ req.toString());
		RequestParams params = new RequestParams();
	
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.ADDGOODS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
					T.showNetworkError(getApplicationContext());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
//				AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
//					if(Api.SUCCEED == bean.result_code) {
//						if(bean.data!=null&&bean.data.RSPCOD.equals("000000")){
//							T.showShort(getApplicationContext(), bean.data.RSPMSG);
//						}
//					} else {
//						T.showShort(getApplicationContext(), bean.result_desc);
//					}
				AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
				Log.e("", " = = =   "+resp.result);
					if(Api.SUCCEED == bean.result_code) {
						T.showShort(getApplicationContext(),bean.data.RSPMSG);
						finish();
//						initDialog(bean.data.RSPMSG,"","确定");
					} else {
						T.showShort(getApplicationContext(), bean.result_desc);
					}
			}
		});
	}
	
	
//	public static String StringFilter(String str){ 
//		String regEx = "[/\\:*?<>|\"\n\t]"; //要过滤掉的字符 
//		Pattern p = Pattern.compile(regEx);  
//		Matcher m = p.matcher(str); 
//		return m.replaceAll("").trim();  
//	}
	
	private boolean judgetext(String text) {
		// TODO Auto-generated method stub
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？，。、；‘’,./;'☺�]"; 
        Pattern p = Pattern.compile(regEx); 
        Matcher m = p.matcher(text);                 
        if( m.find()){
        	return true;
//            Toast.makeText(CommodityInfoActivity.this, "不允许输入特殊符号！", Toast.LENGTH_LONG).show();
        }
		return m.find();
	}
	
	private boolean xiaolian(String text) {
		// TODO Auto-generated method stub
		
		String regEx="[☺�]"; 
        Pattern p = Pattern.compile(regEx); 
        Matcher m = p.matcher(text);                 
        if( m.find()){
        	return true;
//            Toast.makeText(CommodityInfoActivity.this, "不允许输入特殊符号！", Toast.LENGTH_LONG).show();
        }
		return m.find();
	}
	private void rechange() {
		// TODO Auto-generated method stub
		

		if(Bimp.tempSelectBitmap.size()<=1){
			T.showShort(getApplicationContext(), "请选择照片");
			return;
		}

		
		ProgressDialogUtil.showProgressDlg(CommodityInfoActivity.this, "请稍候");
		ShopCategoryRequest req = new ShopCategoryRequest();
		req.shopId = UserInfoManager.getUserInfo(this).shop_id+"";
		req.id = id;
		req.name = edShopName.getText().toString();
		req.photo = photostr.toString();
		req.description = content.getText().toString();
		req.price = edprice.getText().toString();
		req.model = edmodel.getText().toString();
		req.typeId = strshopsype.toString();
		
		
		Log.e("", " - - --  "+req.toString());
		RequestParams params = new RequestParams();
	
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.UPDATEGOODS, params, new RequestCallBack<String>() {
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
						
						
						if(isout()){
							T.showShort(getApplicationContext(), bean.data.RSPMSG);
//							initDialog(bean.data.RSPMSG,"","确定");
							finish();
						}else{
							finish();
						}
//						
					} else {
						T.showShort(getApplicationContext(), bean.result_desc);
					}
			}
		});
	}
	


	private void loaddata1() {
		// TODO Auto-generated method stub
		ProgressDialogUtil.showProgressDlg(CommodityInfoActivity.this, "请稍候");
		ShopCategoryRequest req = new ShopCategoryRequest();
		req.id = id;
		RequestParams params = new RequestParams();
	
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETGOODSINFOBYID, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
					T.showNetworkError(getApplicationContext());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				ShopinfoByIdResponse bean = new Gson().fromJson(resp.result, ShopinfoByIdResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						init(bean.data);
						Log.e("", ""+resp.result);
					} else {
						T.showShort(getApplicationContext(), bean.result_desc);
					}
			}

		});
	}
	private void init(ShopInfoByIdBean data) {
		
		
		 datainfo = data;
		// TODO Auto-generated method stub
		edShopName.setText(data.name);
		content.setText(data.description);
		edprice.setText(data.price);
		edmodel.setText(data.model);
		tv_type.setText(data.type_name);
		
		
		type = data.type_id.split(",");
		 
		if(data.type_id!=null){
			try {
				strshopsype.append(data.type_id);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		if(!TextUtils.isEmpty(data.photo)){
			 String[] sourceStrArray = data.photo.split(",");
			 ptlist = sourceStrArray;
			 for (int i = 0; i < sourceStrArray.length; i++) {
				 ImageItem item = new ImageItem();
				 item.url = sourceStrArray[i];
				 Bimp.tempSelectBitmap.add(item);
			}
		}

		 

		 if(Bimp.tempSelectBitmap.size()<PublicWay.num){
			 Bimp.tempSelectBitmap.add(new ImageItem());
		 }
		 
//		photostr = photostr.toString();
		 if(Bimp.tempSelectBitmap.size()<=0){
			 Bimp.tempSelectBitmap.add(new ImageItem());
		 }
		 Gridadapter.update();
	}
	
	private void initDialog(String content,String left,String right) {
		// TODO Auto-generated method stub
		downloadDialog = new TwoButtonDialog(CommodityInfoActivity.this, R.style.CustomDialog,
				"", content, left, right,true,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							finish();
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
	}
	
	 private void initpopwind() {
		// TODO Auto-generated method stub
	        // 一个自定义的布局，作为显示的内容
		 datas = new ArrayList<ShopCategoryBean>();
	        View contentView = LayoutInflater.from(this).inflate(
	                R.layout.comm_pop, null);
	        // 设置按钮的点击事件
	        TextView button = (TextView) contentView.findViewById(R.id.tv_conmmit);
	        TextView newtype = (TextView) contentView.findViewById(R.id.newtype);
	        TextView tv_dimss = (TextView) contentView.findViewById(R.id.tv_dimss);
	        final TextView content = (TextView) contentView.findViewById(R.id.content);
	        listivew = (ListView) contentView.findViewById(R.id.listview);
	      
	        popupWindow = new PopupWindow(contentView,
		                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
	       	popupWindow.setTouchable(true);
		       
		   	popupWindow.setTouchInterceptor(new OnTouchListener() {
			@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
						return false;
					}
		     });
		      
		   	ColorDrawable dw = new ColorDrawable(0xb0000000);  
		     //设置SelectPicPopupWindow弹出窗体的背景  
		    popupWindow.setBackgroundDrawable(dw);  
		    popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
	        
	        typeadapter = new EtcAdapter(CommodityInfoActivity.this,datas);
	        listivew.setAdapter(typeadapter);
	        content.setText("分类至(按商分类展示商品,方便买家筛选)");
//	        button.setText("");
	        
	        button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					StringBuffer typename = new StringBuffer();
					strshopsype = null;
					strshopsype =new StringBuilder();
					 List<ShopCategoryBean> dt = typeadapter.getchoose();
					 for (int i = 0; i < dt.size(); i++) {
						if(dt.get(i).ischeck){
							strshopsype.append(datas.get(i).id+",");
							typename.append(datas.get(i).name+",");
						}
					}
					if(strshopsype.length()>0){
						tv_type.setText("已选:"+typename);
					}else{
						T.showShort(getApplicationContext(), "未选择分类");
//						Toast.makeText(getApplicationContext(), "未选择类型", Toast.LENGTH_SHORT).show();
					}
					
					popupWindow.dismiss();
				}
			});
	        tv_dimss.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					popupWindow.dismiss();
				}
			});
	        
	        listivew.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					typeadapter.setcheck(arg2);
				}
			});
	        
	        newtype.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showeditdailog();
				}
			});
	        popupWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					WindowManager.LayoutParams lp = getWindow().getAttributes();
					lp.alpha = 1f;
					getWindow().setAttributes(lp);
				}
			});
		
	}
	 

	private void showPopupWindow(final TextView  view) {

		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.7f;
		getWindow().setAttributes(lp);
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
//	        popupWindow.showAsDropDown(view);
	       
	    }
	 
	 public void showeditdailog(){
			doubleWarnDialog1 = new MyEditDialog1(CommodityInfoActivity.this,
					R.style.CustomDialog, "新建分类名称", "", "确认", "取消", "",
					new OnMyDialogClickListener() {

						@Override
						public void onClick(View v) {

							switch (v.getId()) {
							case R.id.btn_right:
								doubleWarnDialog1.dismiss();
//								InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//								m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
								break;
							case R.id.btn_left:
								String paypwd = doubleWarnDialog1.getpaypwd();

								if (paypwd == null || paypwd.equals("")) {
									Toast.makeText(CommodityInfoActivity.this,"不能为空",
											Toast.LENGTH_SHORT).show();
									return;
								}
								break;
							default:
								break;
							}
						}
					},
			
					new onMyaddTextListener() {
						
						@Override
						public void refreshActivity(String paypwd) {
							if (paypwd == null || paypwd.equals("")) {
								Toast.makeText(CommodityInfoActivity.this.getApplicationContext(),"不能为空",
										Toast.LENGTH_SHORT).show();
//								ToastCustom.showMessage(
//										TransferAccountsActivity.this,
//										"");
								return;
							}
							
							
							InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
							
							if(judgetext(paypwd)){
								T.showShort(getApplicationContext(), "非法字符");
								return;
							}
							
							doubleWarnDialog1.dismiss();
							addshoptype(paypwd);
//							Toast.makeText(CommodityInfoActivity.this.getApplicationContext(), paypwd, Toast.LENGTH_SHORT).show();
						}


					});

			doubleWarnDialog1.setCancelable(false);
			doubleWarnDialog1.setCanceledOnTouchOutside(false);
			doubleWarnDialog1.show();
			
		}
	 
		private void addshoptype(String name) {
			// TODO Auto-generated method stub
			ShopCategoryRequest req = new ShopCategoryRequest();
			req.shopId = UserInfoManager.getUserInfo(this).shop_id+"";
			req.name = name;
			RequestParams params = new RequestParams();
			try {
				params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			new HttpUtils().send(HttpMethod.POST, Api.ADDSHOPCATEGORY, params, new RequestCallBack<String>() {
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
							if(bean.data!=null&&bean.data.RSPCOD.equals("000000")){
								T.showShort(getApplicationContext(), bean.data.RSPMSG);
								initd(true);
							}else{
								T.showShort(getApplicationContext(), bean.data.RSPMSG);
							}
						
						} else {
							T.showShort(getApplicationContext(), bean.result_desc);
						}
				}
			});
		}
	 
	 private void initd(final boolean tag) {
			// TODO Auto-generated method stub
//			 datas = new ArrayList<ShopCategoryBean>();
//			for (int i = 0; i < 10; i++) {
//				ShopCategoryBean	bean = new ShopCategoryBean();
//				bean.ischeck = false;
//				bean.name = "分类123";
//				datas.add(bean);
//			}
			
			ShopCategoryRequest req = new ShopCategoryRequest();
			req.shopId = UserInfoManager.getUserInfo(this).shop_id+"";
			RequestParams params = new RequestParams();
			try {
				params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			new HttpUtils().send(HttpMethod.POST, Api.FINDALLGOODSTYPELIST, params, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					ProgressDialogUtil.dismissProgressDlg();
						T.showNetworkError(getApplicationContext());
				}

				@Override
				public void onSuccess(ResponseInfo<String> resp) {
					ProgressDialogUtil.dismissProgressDlg();
					ShopCategoryResponse bean = new Gson().fromJson(resp.result, ShopCategoryResponse.class);
						if(Api.SUCCEED == bean.result_code) {
							datas.clear();
							datas.addAll(bean.data);

							if(type!=null){
								for (int i = 0; i < type.length; i++) {
//								
									Log.e("", " - - t--  "+type[i]);
									for (int j = 0; j < datas.size(); j++) {
										Log.e("", " d- - --  "+datas.get(i).id);
										if(datas.get(j).id.equals(type[i])){
											
											datas.get(j).ischeck = true;
										}
									}


								}
							}
							if(typeadapter!=null){
								typeadapter.notifyDataSetChanged();
								listivew.setAdapter(typeadapter);
							}
							if(tag){
								listivew.setSelection(typeadapter.getCount()-1);  
							}
						} else {
							T.showShort(getApplicationContext(), bean.result_desc);
						}
				}
			});
		}
	 
		/**
		 * 监听返回按钮
		 */
//		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if(isout()){
					initDialog("退出本次编辑?","否","是");
				}else{
						finish();
				}
			}
			return true;
		}
		
		public boolean isout(){
			// TODO Auto-generated method stub
//			edShopName.setText(data.name);
//			content.setText(data.description);
//			edprice.setText(data.price);
//			edmodel.setText(data.model);
//			tv_type.setText(data.type_name);
			
			boolean isout = false;
			if(datainfo!=null){
			if(ptlist!=null&&ptlist.length!=Bimp.getnum()
			||datainfo.name!=null&&edShopName.getText()!=null&&datainfo.name.length()!=edShopName.getText().length()
			||datainfo.description!=null&&content.getText()!=null&&datainfo.description.length()!=content.getText().length()
			||datainfo.price!=null&&edprice.getText()!=null&&datainfo.price.length()!=edprice.getText().length()
			||datainfo.model!=null&&edmodel.getText()!=null&&datainfo.model.length()!=edmodel.getText().length()
			||datainfo.type_name!=null&&tv_type.getText()!=null&&datainfo.type_name.length()!=tv_type.getText().length()){
				isout = true;
			 }
			}else{
				if(!TextUtils.isEmpty(edShopName.getText().toString())
				||!TextUtils.isEmpty(content.getText().toString())
				||!TextUtils.isEmpty(edprice.getText().toString())
				||!TextUtils.isEmpty(edmodel.getText().toString())
				||Bimp.tempSelectBitmap.size()>1){
					isout = true;
				}
			}

			
			return isout;
		}
}
