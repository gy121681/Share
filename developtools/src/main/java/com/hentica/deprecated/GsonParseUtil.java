package com.hentica.deprecated;

import com.hentica.tools.TemplateUtil;
import com.hentica.tools.TemplateUtil.KeyValue;

import java.util.ArrayList;
import java.util.List;

/** Gson工具，创建方法，以解析指定类与json的转换 */
public class GsonParseUtil {

	public static void main(String[] args) {

		// 要解析的类
		String[][] allClasses = {
		//
		{ "HouseMoreData", "测试数据——更多房源列表数据结构" },//
		};

		// 输出结果到控制台
		System.out.println(parse(allClasses));
	}

	/** 生成解析代码 allClasses[类名][注释] */
	public static String parse(String[][] allClasses) {

		StringBuilder builder = new StringBuilder();

		// 为每个类生成解析代码
		for (String[] strings : allClasses) {

			String result = parse(strings[0], strings[1]);
			builder.append(result).append("\r\n");
		}

		return builder.toString();
	}

	/** 生成解析代码 */
	public static String parse(String className, String des) {

		String templeteFile = "res/templates/json_parse_template.java";

		List<KeyValue> replaces = new ArrayList<TemplateUtil.KeyValue>();
		replaces.add(new KeyValue("{$ClassName}", className));
		replaces.add(new KeyValue("{$CommentDes}", des));
		String result = TemplateUtil.genByFile(templeteFile, replaces);

		return result;
	}
}
