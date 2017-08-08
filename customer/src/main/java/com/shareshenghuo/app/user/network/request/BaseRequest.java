package com.shareshenghuo.app.user.network.request;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.shareshenghuo.app.user.util.EncryptionUtil;
import com.google.gson.Gson;

public class BaseRequest {
	
	public String isEncrypt;
	public String info;
	public String token;
	
	public String toJson() {
//		try {
//			testReflect(this);
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return new Gson().toJson(this);
	}
	
	
	public  void testReflect(Object model) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			InstantiationException {
		  	Field[] field = model.getClass().getDeclaredFields();        //获取实体类的所有属性，返回Field数组  
		  	
		  	List<String> list = new ArrayList<String>();
	        for(int j=0 ; j<field.length ; j++){     //遍历所有属性
	                String name = field[j].getName();    //获取属性的名字
	                Object obj = field[j].get(model);
	                if(obj!=null){
	            		list.add(name+"="+obj);
	                }
	               
	                field[j].set(model, null);

	        }
	        
	        isEncrypt = "1";
	        info = EncryptionUtil.getEncryptionstring(list);
	        Log.e("", ""+info);
	}
}
