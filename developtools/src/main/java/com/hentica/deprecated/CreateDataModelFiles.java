package com.hentica.deprecated;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hentica.tools.TemplateUtil;
import com.hentica.tools.TemplateUtil.KeyValue;
import com.hentica.tools.apiparser.ParseHtmlToContentData;
import com.hentica.tools.apiparser.data.ApiParseResult.OneApiInfo;

/**
 * 创建数据Class文件
 * 
 * @author mili
 * @createTime 2016-6-4 下午4:41:37
 */
public class CreateDataModelFiles {

	public static void main(String[] args) {

		String packageName = "com.hentica.app.data";
		String apiUrl = "http://192.167.1.12/hen_menli/mobile/api/";
		String outDir = "temp_out/" + packageName;
		String author = "mili"; // 作者

		// 取得所有类信息
		List<DataClassInfo> allClasses = parseClassInfo(apiUrl, packageName, author);

		// 生成类文件
		for (DataClassInfo classInfo : allClasses) {

			System.out.println("生成: " + classInfo.mClassName + ".java");
			// 保存为java文件
			writeClass(classInfo, outDir);
		}

		System.out.println("所有文件生成完成\n\n\n");

		// 生成解析信息
		for (DataClassInfo classInfo : allClasses) {

			String result = GsonParseUtil.parse(classInfo.mClassName, classInfo.mDes);
			System.out.println(result);
		}
	}

	/** 读取api数据并解析为类信息 */
	private static List<DataClassInfo> parseClassInfo(String apiUrl, String packageName,
			String author) {

		List<DataClassInfo> result = new ArrayList<DataClassInfo>();

		ParseHtmlToContentData paser = new ParseHtmlToContentData(apiUrl);
		List<OneApiInfo> infos = paser.parseToContent();

		for (OneApiInfo apiInfo : infos) {

			result.add(parseApiToClassInfo(apiInfo, packageName, author));
		}

		return result;
	}

	/** 把api解析为类信息 */
	private static DataClassInfo parseApiToClassInfo(OneApiInfo apiInfo, String packageName,
													 String author) {

		DataClassInfo result = new DataClassInfo();

		result.mPackage = packageName;
//		result.mDes = apiInfo.mDes;
		result.mDes = apiInfo.dataParamDesc;
		result.mAuthor = author;
//		result.mClassName = parseActionToClassName(apiInfo.mActionPath);
		result.mClassName = parseActionToClassName(apiInfo.action);

		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd aHH:mm:ss");
		result.mDate = format.format(date);

		return result;
	}

	/** 把api路径解析为类名 */
	private static String parseActionToClassName(String actionPath) {

		StringBuilder builder = new StringBuilder();

		actionPath = actionPath.trim();
		if (actionPath.length() > 0) {

			String[] words = actionPath.split("/");

			// 遍历所有单词
			for (String word : words) {

				word = word.trim();
				// 若是有效单词
				if (!word.isEmpty()) {

					// 首字母大写
					String first = word.substring(0, 1).toUpperCase();
					String rest = word.substring(1, word.length());

					// 把当前单词加入结果
					builder.append(first).append(rest);
				}
			}
		}

		return builder.toString();
	}

	/** 把类保存为.java文件 */
	private static void writeClass(DataClassInfo info, String outDir) {

		// 参数
		List<KeyValue> replaces = new ArrayList<KeyValue>();
		replaces.add(new KeyValue("{$package}", info.mPackage));
		replaces.add(new KeyValue("{$comment}", info.mDes));
		replaces.add(new KeyValue("{$author}", info.mAuthor));
		replaces.add(new KeyValue("{$date}", info.mDate));
		replaces.add(new KeyValue("{$className}", info.mClassName));

		// 输出文件
		String outFile = outDir + "/" + info.mClassName + ".java";

		// 生成
		TemplateUtil.genByFile("res/templates/data_template.java", replaces, outFile);
	}

	/** 数据类 */
	private static class DataClassInfo {

		/** 包名 */
		public String mPackage;
		/** 注释 */
		public String mDes;
		/** 作者 */
		public String mAuthor;
		/** 创建日期 */
		public String mDate;
		/** 类名 */
		public String mClassName;
	}
}
