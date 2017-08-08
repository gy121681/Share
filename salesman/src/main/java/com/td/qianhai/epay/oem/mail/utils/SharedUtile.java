package com.td.qianhai.epay.oem.mail.utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Set;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;

/**
 * 
 * FUNCTION：我的缓存类
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SharedUtile{

	// 缓存的名字
	private static final String CACHE = "woka";
	// 缓存对象
	private SharedPreferences cache;
	// 编辑对象
	private Editor editor;
	// 本类对象
	private static SharedUtile my;

	/**
	 * 构造方法
	 * 
	 * @param 上下文
	 */
	private SharedUtile(Context context) {

		// 取得缓存的实例化对象
		cache = context.getSharedPreferences(CACHE, Context.MODE_PRIVATE);
		// 取得编辑对象
		editor = cache.edit();
	}

	/**
	 * 取得本类的实例化
	 * 
	 * @param 上下文
	 */
	public static synchronized SharedUtile getMySharedPreferencesObejct(
			Context context) {

		if (my == null) {
			my = new SharedUtile(context);
		}

		// 返回本类对象
		return my;

	}

	/**
	 * 1、储存字符串
	 * 
	 * @param 取值的名字
	 * @param 存放的值
	 */
	public void putString(String key, String values) {
		// 存储字符类型
		editor.putString(key, values);
	}

	/**
	 * 2、存储整型
	 * 
	 * @param 存储值的名字
	 * @param 存储的值
	 */
	public void putInt(String key, int values) {
		// 存储整数类型
		editor.putInt(key, values);
	}

	/**
	 * 3、储存布尔型
	 * 
	 * @param 存储值的名字
	 * @param 存储的值
	 */
	public void putBoolean(String key, boolean values) {
		// 存储布尔类型
		editor.putBoolean(key, values);
	}

	/**
	 * 4、储存浮点型
	 * 
	 * @param 存储值的名字
	 * @param 存储的值
	 */
	public void putFloat(String key, float values) {
		// 存储浮点型
		editor.putFloat(key, values);
	}

	/**
	 * 5、储存长整型
	 * 
	 * @param 存储值的名字
	 * @param 存储的值
	 */
	public void putLong(String key, long values) {
		// 储存长整型
		editor.putLong(key, values);
	}

	/**
	 * 6、储存Set容器
	 * 
	 * @param 存储值的名字
	 * @param 存储的值
	 */
	public void putStringSet(String key, Set<String> values) {
		// 储存set容器
		editor.putStringSet(key, values);
	}

	/**
	 * 提交
	 */
	public void commit() {
		// 提交
		editor.commit();
	}

	/**
	 * 0、删除缓存中的值，通过名字删除
	 * 
	 * @param 存储值的名字
	 */
	public void remove(String key) {
		// 删除指定名字的值
		editor.remove(key);
	}

	/**
	 * 1、取得字符串
	 * 
	 * @param 存储值的名字
	 */
	public String getString(String key) {
		// 取得字符串
		return cache.getString(key, "");
	}

	/**
	 * 2、取得整型
	 * 
	 * @param 存储值的名字
	 */
	public int getInt(String key) {
		// 取得整型
		return cache.getInt(key, 0);
	}

	/**
	 * 3、取得浮点型
	 * 
	 * @param 存储值的名字
	 */
	public float getFloat(String key) {
		// 取得浮点型
		return cache.getFloat(key, 0f);
	}

	/**
	 * 4、取得长整型
	 * 
	 * @param 存储值的名字
	 */
	public long getLong(String key) {
		// 取得长整型
		return cache.getLong(key, 0);
	}

	/**
	 * 5、取得布尔类型
	 * 
	 * @param 存储值的名字
	 */
	public boolean getBoolean(String key) {
		// 取得布尔类型
		return cache.getBoolean(key, false);
	}

	/**
	 * 6、取得Set容器
	 * 
	 * @param 存储值的名字
	 */
	public Set<String> getStringSet(String key) {
		// 取得Set容器
		return cache.getStringSet(key, null);
	}

	/**
	 * 存储对象
	 * 
	 * @param 对象转换后的字符串内容
	 */
	public void putObject(Object obj, String key) {

		// 对象信息
		String objBase64 = null;

		// 处理异常
		try {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			ObjectOutputStream oos = new ObjectOutputStream(baos);

			oos.writeObject(obj);

			objBase64 = Base64.encodeToString(baos.toByteArray(),
					Base64.DEFAULT);

			editor.putString(key, objBase64);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 取得对象
	 * 
	 * @param 字符串转换后的实例化
	 */
	public Object getObject(String key) {

		// 返回的对象
		Object obj = null;

		// 取得字符串
		String objBase64 = cache.getString(key, "");

		// 进行解码
		byte[] base64Bytes = Base64
				.decode(objBase64.getBytes(), Base64.DEFAULT);

		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			obj = ois.readObject();

		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// 返回的对象
		return obj;
	}

	/**
	 * 存储图片
	 * 
	 * @param 图片
	 * @param 存储值的名字
	 */
	public void putBitmap(Bitmap bitmap, String key) {

		// 字节数组输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 图片压缩
		bitmap.compress(CompressFormat.JPEG, 100, baos);
		// 取得压缩的字节编码
		String imageBase64 = new String(Base64.encode(baos.toByteArray(),
				Base64.DEFAULT));
		// 通过指定的值保存
		editor.putString(key, imageBase64);
		try {
			// 关闭流
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取得图片
	 * 
	 * @param 存储值的名字
	 */
	public Bitmap getBitmap(String key) {

		// 返回的图片
		Bitmap bitmap = null;

		// 取得对应名字的值
		String imageBase = cache.getString(key, "");
		// 取得解码后的字节数组
		byte[] iamgeBytes = Base64.decode(imageBase.getBytes(), Base64.DEFAULT); // 解码
		// 字节数组输入流
		ByteArrayInputStream bais = new ByteArrayInputStream(iamgeBytes);
		// 保存取得的图片
		bitmap = BitmapFactory.decodeStream(bais);
		try {
			// 关闭输入流
			bais.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 返回取得的图片
		return bitmap;
	}

}

