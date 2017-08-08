/**
 * 
 */
package com.hentica.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模板工具，简单替换模板内容
 * 
 * @author Limi
 * @createTime 2016-2-29 上午9:30:13
 */
public class TemplateUtil {

	/** 缓存的文件内容 */
	private static Map<String, String> mCachedContent = new HashMap<String, String>();

	/** 键值对 */
	public static class KeyValue {

		/** 键 */
		private String mKey;

		/** 值 */
		private String mValue;

		/** 构造函数 */
		public KeyValue() {
		}

		/** 构造函数 */
		public KeyValue(String key, String value) {

			mKey = key;
			mValue = value;
		}

		/** 键 */
		public String getKey() {
			return mKey;
		}

		/** 键 */
		public void setKey(String key) {
			mKey = key;
		}

		/** 值 */
		public String getValue() {
			return mValue;
		}

		/** 值 */
		public void setValue(String value) {
			mValue = value;
		}

		@Override
		public String toString() {

			return mKey + " : " + mValue + " ";
		}
	}

	/**
	 * 根据模板生成代码
	 * 
	 * @param templeteFile
	 *            模板文件
	 * @param replaces
	 *            要替换的字符串
	 * @param outFile
	 *            生成文件
	 */
	public static void genByFile(String templeteFile, List<KeyValue> replaces, String outFile) {

		// 读取模板文件，生成代码字符串，写入文件
		FileHelper.writeString(outFile, genByFile(templeteFile, replaces));
	}

	/**
	 * 
	 * 根据模板生成代码，返回生成代码
	 * 
	 * @param templeteFile
	 *            模板文件
	 * @param replaces
	 *            要替换的字符串
	 * @return
	 */
	public static String genByFile(String templeteFile, List<KeyValue> replaces) {

		// 读取模板文件，生成代码字符串，写入文件
		String templeteContent = getTemplateContent(templeteFile);
		String result = genCode(templeteContent, replaces);
		return result;
	}

	/**
	 * 生成代码
	 * 
	 * @param templeteContent
	 *            源字符串
	 * @param replaces
	 *            要替换的字符串
	 * @return
	 */
	public static String genCode(String templeteContent, List<KeyValue> replaces) {

		if (templeteContent == null) {
			templeteContent = "";
		}

		// 替换所有文字
		for (KeyValue keyValue : replaces) {

			String oldString = keyValue.getKey();
			String newString = keyValue.getValue();

			if (newString == null) {
				newString = "";
			}
			templeteContent = templeteContent.replace(oldString, newString);
		}

		return templeteContent;
	}

	/** 取得文件内容，有缓存 */
	private static String getTemplateContent(String templeteFile) {

		String result = mCachedContent.get(templeteFile);

		if (result == null) {

			result = FileHelper.readString(templeteFile);
			mCachedContent.put(templeteFile, result);
		}
		return result;
	}
}
