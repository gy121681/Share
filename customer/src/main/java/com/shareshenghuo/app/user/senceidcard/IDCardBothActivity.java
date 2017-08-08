package com.shareshenghuo.app.user.senceidcard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

import com.sensetime.card.Card;
import com.sensetime.card.CardActivity;
import com.sensetime.idcard.IDCard;
import com.sensetime.idcard.IDCardActivity;
import com.sensetime.idcard.IDCardRecognizer;

public class IDCardBothActivity extends IDCardActivity {

	private static final String TAG = IDCardActivity.class.getSimpleName();
	private byte[] idcardFrontImage = null;
	private byte[] idcardFrontImageRectified = null;

	// 重写这个方法来自定义检测到卡片后的处理方式
	@Override
	public void onCardDetected(Card card, Bitmap cardBitmap, Bitmap rectifiedBitmap, Bitmap faceBitmap) {
		// 处理过程中先暂停扫描
		pauseScanning();
		IDCard idcard = (IDCard) card;
		// 获取识别出的是哪一面
		IDCard.Side side = idcard.getSide();
		if (side == IDCard.Side.FRONT) {	

//			onTextUpdate("扫描成功", getResources().getColor(R.));

			TimerTask task = new TimerTask() {
	            @Override
	            public void run() {
	    			onTextUpdate("请将身份证反面放入扫描框内", Color.WHITE);
	    		}   
	        };
	        Timer timer = new Timer();
	        timer.schedule(task, 1000);//“扫描成功”显示1秒	
	        
			// 返回原始图像
			if (getIntent() != null && getIntent().getBooleanExtra(EXTRA_CARD_IMAGE_FRONT, false)) {
				ByteArrayOutputStream scaledCardBytes = new ByteArrayOutputStream();
				cardBitmap.compress(Bitmap.CompressFormat.JPEG, 80, scaledCardBytes);
				idcardFrontImage = scaledCardBytes.toByteArray();
				try {
					scaledCardBytes.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 返回裁剪图像
			if (getIntent() != null && getIntent().getBooleanExtra(EXTRA_CARD_IMAGE_FRONT_RECTIFIED, false)) {
				ByteArrayOutputStream scaledCardBytesRectified = new ByteArrayOutputStream();
				cardBitmap = resizeImage(cardBitmap, 1280, 800);
				rectifiedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, scaledCardBytesRectified);
				idcardFrontImageRectified = scaledCardBytesRectified.toByteArray();
				try {
					scaledCardBytesRectified.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// 设置仅识别背面
			setRecognizeMode(IDCardRecognizer.Mode.BACK);
			// 继续扫描
			resumeScanning();

		} else if (side == IDCard.Side.BACK) {
			// 扫描完背面，返回数据或进行下一步操作
			Intent dataIntent = new Intent();
			
			// 返回原始图像
			if (getIntent() != null && getIntent().getBooleanExtra(EXTRA_CARD_IMAGE, false)) {
				ByteArrayOutputStream scaledCardBytes = new ByteArrayOutputStream();
				cardBitmap = resizeImage(cardBitmap, 1280, 800);
				cardBitmap.compress(Bitmap.CompressFormat.JPEG, 80, scaledCardBytes);
				dataIntent.putExtra(EXTRA_CARD_IMAGE, scaledCardBytes.toByteArray());
				try {
					scaledCardBytes.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 返回裁剪图像
			if (getIntent() != null && getIntent().getBooleanExtra(EXTRA_CARD_IMAGE_RECTIFIED, false)) {
				ByteArrayOutputStream scaledCardBytesRectified = new ByteArrayOutputStream();
				rectifiedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, scaledCardBytesRectified);
				dataIntent.putExtra(EXTRA_CARD_IMAGE_RECTIFIED, scaledCardBytesRectified.toByteArray());
				try {
					scaledCardBytesRectified.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			dataIntent.putExtra(EXTRA_CARD_IMAGE_FRONT, idcardFrontImage);
			dataIntent.putExtra(EXTRA_CARD_IMAGE_FRONT_RECTIFIED, idcardFrontImageRectified);
			dataIntent.putExtra(CardActivity.EXTRA_SCAN_RESULT, idcard);

			setResult(RESULT_CARD_INFO, dataIntent);
			finish();
		}
	}
	
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) 
    {  
        Bitmap BitmapOrg = bitmap;  
        int width = BitmapOrg.getWidth();  
        int height = BitmapOrg.getHeight();  
        int newWidth = w;  
        int newHeight = h;  

        float scaleWidth = ((float) newWidth) / width;  
        float scaleHeight = ((float) newHeight) / height;  

        Matrix matrix = new Matrix();  
        matrix.postScale(scaleWidth, scaleHeight);  
        
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,  
                        height, matrix, true);  
        return resizedBitmap;  
    }

}

