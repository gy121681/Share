package com.shareshenghuo.app.user.senceidcard;

import com.handmark.pulltorefresh.library.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

public class MyDrawView extends View {  
	
	private Paint mBackgroundPaint;
    private Path mBackgroundPath;
    
    public Rect guideRect;
    public int screenWidth = 1080;
    public int screenHeight = 1920;
	  
    public MyDrawView(Context context) {  
        super(context);  
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {   
        super.onDraw(canvas);  
 
        Rect mGuide= guideRect;
        Rect mScreen = new Rect(0, 0, screenWidth, screenHeight);
        
        mBackgroundPath = new Path();
        mBackgroundPath.addRect(new RectF(mScreen), Path.Direction.CW);
        mBackgroundPath.addRect(new RectF(mGuide), Path.Direction.CCW);
        
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.clearShadowLayer();
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(Color.BLACK); // 75% black
        mBackgroundPaint.setAlpha(200);//set BackGround alpha, range of value 0~255

        canvas.drawPath(mBackgroundPath, mBackgroundPaint);
        Paint paint = new Paint();
		paint.setAntiAlias(true);//设置画笔为无锯齿  
        paint.setColor(Color.WHITE);//设置画笔颜色  
        paint.setStrokeWidth((float) 5.0);//线宽 
        paint.setStyle(Style.STROKE);//空心效果
        
        RectF guideLineRect = new RectF(guideRect.left-2.0f, guideRect.top-2.0f, guideRect.right+2.0f, guideRect.bottom+2.0f);//下边  
        canvas.drawRoundRect(guideLineRect, 15, 15, paint);//绘制圆角矩形 
        
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.idcard_front); 
        bitmap = reSizeBitmap(bitmap);
        canvas.drawBitmap(bitmap, guideRect.left + (guideRect.right-guideRect.left) *3/5,
        		guideRect.top + (guideRect.bottom-guideRect.top)/8, paint);
    } 
    
    private Bitmap reSizeBitmap(Bitmap bitmap) {
    	Matrix matrix = new Matrix(); 
    	float scale = (float)(guideRect.right - guideRect.left)/screenHeight;
    	matrix.postScale(scale, scale);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        
    	return resizeBmp;
    }

}