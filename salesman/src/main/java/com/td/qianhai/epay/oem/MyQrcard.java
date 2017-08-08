package com.td.qianhai.epay.oem;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.AES;
import com.td.qianhai.epay.oem.mail.utils.EncodingHandler;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;

public class MyQrcard extends BaseActivity{
	
	private TextView back,title,my_name;
	
	private ImageView qr_img;
	
	private String mobile,username;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myqrcode_activity);
		AppContext.getInstance().addActivity(this);
		initview();
		
		
	}

	private void initview() {
		
//		mobile = ((AppContext)getApplication()).getMobile();
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		
		username = ((AppContext)getApplication()).getUsername();
		
		qr_img = (ImageView) findViewById(R.id.myqrcode_img);
		
		
		my_name = (TextView) findViewById(R.id.my_name);
		
		title = (TextView) findViewById(R.id.tv_title_contre);
		title.setText("付款码");
		
		back = (TextView) findViewById(R.id.bt_title_left);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		initqr();

	}

	private void initqr() {
		
		
		if(username!=null&&!username.equals("")){
			//根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
			
			my_name.setText(username);
			try {
				String translation = mobile+username;
				

				Bitmap qrCodeBitmap = null;
//				String aa = DESKey.encrypt("bgg", translation);
				String bb = ((AppContext)getApplication()).getAecuserinfo();
				if(bb!=null&&!bb.equals("")){
					 qrCodeBitmap = EncodingHandler.createQRCode("1"+bb, 500);
				}else{
					String aa = new AES().encrypt(translation.getBytes());
					
					((AppContext)getApplication()).setAecuserinfo(aa);
					 qrCodeBitmap = EncodingHandler.createQRCode("1"+aa, 500);
				}
				
				
				
				
//				Bitmap logoBmp = BitmapFactory.decodeResource(getResources(), R.drawable.transaction);
//				//二维码和logo合并
//				Bitmap bitmap = Bitmap.createBitmap(qrCodeBitmap.getWidth(), qrCodeBitmap
//		                .getHeight(), qrCodeBitmap.getConfig());
//		        Canvas canvas = new Canvas(bitmap);
//		        //二维码
//		        canvas.drawBitmap(qrCodeBitmap, 0,0, null);
//		        //logo绘制在二维码中央
//				canvas.drawBitmap(logoBmp, qrCodeBitmap.getWidth() / 2
//						- logoBmp.getWidth() / 2, qrCodeBitmap.getHeight()
//						/ 2 - logoBmp.getHeight() / 2, null);
				
				qr_img.setImageBitmap(qrCodeBitmap);
				
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			
			final BussinessInfoTask task = new BussinessInfoTask();
			task.execute(HttpUrls.BUSSINESS_INFO + "", mobile, "4","0");
			
//			try {
//				Bitmap qrCodeBitmap = EncodingHandler.createQRCode("0", 500);
//				qr_img.setImageBitmap(qrCodeBitmap);
//			} catch (WriterException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
	
	/**
	 * 查看商户资料Task
	 * 
	 * @author Administrator
	 * 
	 */
	class BussinessInfoTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog("加载中...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2],params[3] };
			return NetCommunicate.get(HttpUrls.BUSSINESS_INFO, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					if(result.get("ACTNAM")!=null){
						my_name.setText(result.get("ACTNAM").toString());
						AppContext.getInstance().setUsername(result.get("ACTNAM").toString());
						try {
							String translation = mobile+result.get("ACTNAM").toString();
							
							
							String aa = new AES().encrypt(translation.getBytes());
//							String aa = DESKey.encrypt("bgg", translation);
							((AppContext)getApplication()).setAecuserinfo(aa);
							Bitmap qrCodeBitmap = EncodingHandler.createQRCode("1"+aa, 500);
							
							
//							Bitmap logoBmp = BitmapFactory.decodeResource(getResources(), R.drawable.transaction);
//							//二维码和logo合并
//							Bitmap bitmap = Bitmap.createBitmap(qrCodeBitmap.getWidth(), qrCodeBitmap
//					                .getHeight(), qrCodeBitmap.getConfig());
//					        Canvas canvas = new Canvas(bitmap);
//					        //二维码
//					        canvas.drawBitmap(qrCodeBitmap, 0,0, null);
//					        //logo绘制在二维码中央
//							canvas.drawBitmap(logoBmp, qrCodeBitmap.getWidth() / 2
//									- logoBmp.getWidth() / 2, qrCodeBitmap.getHeight()
//									/ 2 - logoBmp.getHeight() / 2, null);
							
							qr_img.setImageBitmap(qrCodeBitmap);
							
							
//							qr_img.setImageBitmap(qrCodeBitmap);
						} catch (WriterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
//					((AppContext)con.getApplication()).setUsername(result.get("ACTNAM"));
					
//					showView(result);
				} else {
					Toast.makeText(getApplicationContext(),"商户信息获取失败",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(MyQrcard.this, "商户信息获取失败");
					finish();
				}
			}
			super.onPostExecute(result);
		}
	}
}
