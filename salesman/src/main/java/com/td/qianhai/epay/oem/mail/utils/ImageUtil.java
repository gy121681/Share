package com.td.qianhai.epay.oem.mail.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

public class ImageUtil {
	
	public static  Bitmap compressBySize(String pathName, int targetWidth,  
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
	public  static boolean saveBitmap2file(String filename, int targetWidth,   int targetHeight) {
		Log.e("", ""+FileSizeUtil.getFileOrFilesSize(filename,2 )+"kb");
		if(FileSizeUtil.getFileOrFilesSize(filename,2 )<400){
		
			return false;
		}
		   ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		

		int options = 90;
		Bitmap bmp = compressBySize(filename, targetWidth, targetHeight);
		 bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);  
	        while (baos.toByteArray().length / 1024 > 200) {   
	            baos.reset();  
	            options -= 10;  
	            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);  
	        }  
		
		
		CompressFormat format = Bitmap.CompressFormat.JPEG;
		int quality = 100;
		OutputStream stream = null;
		
		File vFile = new File(filename);
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		} else {
			if (vFile.exists()) {
				vFile.delete();
			}
		}
		
//		String name= "test.jpeg";
//		File picFileDir = new File(Environment.getExternalStorageDirectory().toString()+
//		File.separator +"testCamera");//仅创建路径的File对象
//		  if(!picFileDir.exists()){
//		   picFileDir.mkdir();//如果路径不存在就先创建路径
//		  }
//		File picFile = new File(picFileDir,name);//然后再创建路径和文件的File对象
//		try {
//		//FileOutputStream方法会在指定文件不存在的情况下自动创建
//		FileOutputStream fos = new FileOutputStream(picFile);
//		fos.write(data);
//		} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		}
		
		try {
			stream = new FileOutputStream(filename);
			
            try {
            	stream.write(baos.toByteArray());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
//			bmp.compress(format, quality, stream);
			try {
				stream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return false;
		}
		

		return true;
	}
	
	
	public static void compressBmpToFile(Bitmap bmp,File file){  
		
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        int options = 80;//个人喜欢从80开始,  
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);  
        while (baos.toByteArray().length / 1024 > 100) {   
            baos.reset();  
            options -= 10;  
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);  
        }  
        try {  
            FileOutputStream fos = new FileOutputStream(file);  
            fos.write(baos.toByteArray());  
            fos.flush();  
            fos.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
	
	public static  Bitmap compressBmpFromBmp(Bitmap image) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        int options = 100;  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
        while (baos.toByteArray().length / 1024 > 100) {   
            baos.reset();  
            options -= 10;  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);  
        return bitmap;  
    }  
	
}
