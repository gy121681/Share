//package com.shareshenghuo.app.user;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import net.tsz.afinal.FinalBitmap;
//
//import org.apache.http.entity.StringEntity;
//
//import UserInfoManager;
//import MyOilRequest;
//import com.shareshenghuo.app.user.network.response.MyOilResponse;
//import QrcodeResponse;
//import Api;
//import com.shareshenghuo.app.user.util.ProgressDialogUtil;
//import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
//import TwoButtonDialog;
//import com.google.gson.Gson;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.CompressFormat;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.support.v4.widget.DrawerLayout.LayoutParams;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//public class OilReceivQrActivity extends BaseTopActivity{
//	
//	private RelativeLayout title;
//	private ImageView ivQR;
//	private TwoButtonDialog downloadDialog;
//	
//	@Override
//	protected void onCreate(Bundle arg0) {
//		// TODO Auto-generated method stub
//		super.onCreate(arg0);
//		setContentView(R.layout.oil_receivqr_activity);
//		initview();
//		loadData();
//	}
//
//	private void initview() {
//		// TODO Auto-generated method stub
//		initTopBar("收款码");
//		ivQR = getView(R.id.ivQR);
//		title = getView(R.id.title);
//		btnTopRight1.setVisibility(View.VISIBLE);
//		btnTopRight1.setText("保存图片");
//		btnTopRight1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				save();
//			}
//		});
////		btnTopRight4.setVisibility(View.VISIBLE);
////		btnTopRight4.setBackgroundResource(R.drawable.top_menu);
////		btnTopRight4.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View arg0) {
////				// TODO Auto-generated method stub
////				showPopupWindow(title);
////			}
////		});
//	}
//	
//	 private void showPopupWindow(View view) {
//		 
//			List<String> list = new ArrayList<String>();
//			list.add("销售流水");
//			list.add("保存图片");
//			
//
//	        // 一个自定义的布局，作为显示的内容
//	        View contentView = LayoutInflater.from(this).inflate(
//	                R.layout.currency_pop, null);
//	        // 设置按钮的点击事件
//	        
//			ListView listview = (ListView) contentView.findViewById(R.id.poplist);
//			ArrayAdapter popadapter = new ArrayAdapter<String>(this, R.layout.pop_name,list);
//			listview.setAdapter(popadapter);
//			
//
//	        final PopupWindow popupWindow = new PopupWindow(contentView,
//	                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
//	        popupWindow.setTouchable(true);
//	        
//	        popupWindow.setTouchInterceptor(new OnTouchListener() {
//
//				@Override
//				public boolean onTouch(View arg0, MotionEvent arg1) {
//					// TODO Auto-generated method stub
//					return false;
//				}
//	        });
//	        
//	        ColorDrawable dw = new ColorDrawable(00000000);  
//	        //设置SelectPicPopupWindow弹出窗体的背景  
//	        popupWindow.setBackgroundDrawable(dw);  
////			WindowManager.LayoutParams lp = getWindow().getAttributes();
////			lp.alpha = 0.7f;
////			getWindow().setAttributes(lp);
//	        // 设置好参数之后再show
//	        popupWindow.showAsDropDown(view);
//	        listview.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1,
//						int arg2, long arg3) {
//					// TODO Auto-generated method stub
//					if((int)arg3==0){
//						Intent it =new Intent(OilReceivQrActivity.this,OilRechargeActivity.class);
//						it.putExtra("tag", "1");
//						startActivity(it);
////						startActivity(new Intent(OilReceivQrActivity.this,OilRechargeActivity.class));
//					}else if((int)arg3==1){
//						savebitmap(ivQR);
//						
//						
//						downloadDialog = new TwoButtonDialog(OilReceivQrActivity.this, R.style.CustomDialog,
//								"提示", "保存成功,可在相册查看", "确定", "",true,new OnMyDialogClickListener() {
//									
//									@Override
//									public void onClick(View v) {
//										// TODO Auto-generated method stub
//										switch (v.getId()) {
//										case R.id.Button_OK:
//											downloadDialog.dismiss();
//											break;
//										case R.id.Button_cancel:
//											downloadDialog.dismiss();
//										default:
//											break;
//										}
//									}
//								});
//							downloadDialog.show();
//					}
//					popupWindow.dismiss();
//				}
//			});
//	    }
//	 
//	 public void save(){
//			savebitmap(ivQR);
//			
//			if(downloadDialog==null){
//				downloadDialog = new TwoButtonDialog(OilReceivQrActivity.this, R.style.CustomDialog,
//						"提示", "保存成功,可在相册查看", "确定", "",true,new OnMyDialogClickListener() {
//							
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//								switch (v.getId()) {
//								case R.id.Button_OK:
//									downloadDialog.dismiss();
//									break;
//								case R.id.Button_cancel:
//									downloadDialog.dismiss();
//								default:
//									break;
//								}
//							}
//						});
//					downloadDialog.show();
//			}
//
//	 }
//	 
//	 public void loadData() {
//		 	ProgressDialogUtil.showProgressDlg(this, "");
//			MyOilRequest req = new MyOilRequest();
//			req.userId = UserInfoManager.getUserInfo(this).id+"";
//			RequestParams params = new RequestParams();
//			try {
//				params.setBodyEntity(new StringEntity(req.toJson()));
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//			new HttpUtils().send(HttpMethod.POST, Api.GENOILCARDERCODE, params, new RequestCallBack<String>() {
//				@Override
//				public void onFailure(HttpException arg0, String arg1) {
//					ProgressDialogUtil.dismissProgressDlg();
//				}
//
//				@Override
//				public void onSuccess(ResponseInfo<String> resp) {
//					ProgressDialogUtil.dismissProgressDlg();
//					QrcodeResponse bean = new Gson().fromJson(resp.result, QrcodeResponse.class);
//					Log.e("", ""+resp.result);
//					if(Api.SUCCEED == bean.result_code){
//						
//						FinalBitmap.create(OilReceivQrActivity.this).display(ivQR,
//								bean.data.returnUrl,
//								ivQR.getWidth(),
//								ivQR.getHeight(), null, null);
//					}
//				}
//			});
//		}
//	 
//	 
//	 public void savebitmap(ImageView img){
//			File dir = new File(Environment.getExternalStorageDirectory()+"/share/");
//			if (!dir.exists()){
//				dir.mkdirs();
//			}
//		 
//			Bitmap bitmap = null;
//			
//			 bitmap = convertViewToBitmap(img);
//			
//			FileOutputStream m_fileOutPutStream = null;
//			String filepath = Environment.getExternalStorageDirectory()+"/share/"// +File.separator
//					+ getStringDateMerge()+"myOilpayqrcode.png";
//			try {
//				m_fileOutPutStream = new FileOutputStream(filepath);
//			}catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
////			obmp = BitmapFactory.decodeFile(filepath, newOpts);
//			bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
//			bitmap.compress(CompressFormat.PNG, 100, m_fileOutPutStream);
//			
//			try {
//				m_fileOutPutStream.flush();
//				m_fileOutPutStream.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			MediaStore.Images.Media.insertImage(OilReceivQrActivity.this.getContentResolver(), bitmap, "title", "description");
//		}
//		public  Bitmap convertViewToBitmap(View view)  
//		{  
//		    view.buildDrawingCache();  
//		    Bitmap bitmap = view.getDrawingCache();  
//		  
//		    return bitmap;  
//		}
//		public String getStringDateMerge() {
//			Date currentTime = new Date();
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//			String dateString = formatter.format(currentTime);
//			return dateString;
//		}
//}
