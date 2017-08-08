package com.hentica.tools;

import java.io.StringWriter;
import java.util.StringTokenizer;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * 命令行参数辅助工具
 * 
 * @author mili
 * @createTime 2016-6-2 上午10:27:05
 */
public class ArgsHelper {

	/** 把字符串解析为参数 */
	public static String[] parseArgs(String command) {

		StringTokenizer st = new StringTokenizer(command);
		String[] cmdarray = new String[st.countTokens()];
		for (int i = 0; st.hasMoreTokens(); i++)
			cmdarray[i] = st.nextToken();

		return cmdarray;
	}

	/** 解析器 */
	private OptionParser parser = new OptionParser();

	/** 解析后的命令行 */
	private OptionSet options;

	/** 构造函数 */
	public ArgsHelper() {
	}

	/** 添加参数 */
	public void addOption(String option, String des) {

		parser.accepts(option, des).withOptionalArg();
	}

	/** 添加参数 */
	public void addOption(String option, String defValue, String des) {

		parser.accepts(option, des).withOptionalArg().defaultsTo(defValue);
	}

	/** 设置命令行 */
	public void setCommand(String command) {

		try {

			options = parser.parse(parseArgs(command));

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/** 取得某选项对应的值 */
	public String getValue(String key) {

		if (options != null) {

			Object object = options.valueOf(key);
			if (object != null) {

				return object.toString();
			}
		}
		return "";
	}

	/** 取得帮助文字 */
	public String getHelpString() {

		try {
			StringWriter writer = new StringWriter();
			parser.printHelpOn(writer);
			return writer.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
}
