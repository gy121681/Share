/**
 * 
 */
package com.hentica.allmain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hentica.tools.ArgsHelper;
import com.hentica.tools.FileHelper;
import com.hentica.tools.TemplateUtil;
import com.hentica.tools.TemplateUtil.KeyValue;

/**
 * 生成初始安卓界面，包括java文件与xml布局，并把两者关联起来。不涉及具体UI <br />
 * 配置文件: <br />
 * res/res_config/init_fragments_config.txt <br />
 * 生成文件: <br />
 * temp_out/包名/ <br />
 * temp_out/res_layout/ <br />
 * 
 * @author Limi
 * @createTime 2016-2-29 上午9:25:44
 */
public class InitAndroidUI {

	public static void main(String[] args) {

		System.out.println("开始生成界面");

		// 用户版
		String configFileUser = "./developtools/res/res_config/" +
				"init_fragments_config.txt";
		String appPackage = "com.hentica.app.car.rebate";
		String uiBasePackage = "com.hentica.app.module";
		String outDir = "developtools/temp_out/";
		gen(configFileUser, appPackage, uiBasePackage, outDir);
		System.out.println("所有fragment生成完成");

		// 帮助
		// System.out.println();
		// System.out.println("参数帮助:");
		// System.out.println(argsHelper.getHelpString());
	}

	private static ArgsHelper createArgsHelper() {

		ArgsHelper argsHelper = new ArgsHelper();

		argsHelper.addOption("package", "类所在的包名");
		argsHelper.addOption("title", "标题");
		argsHelper.addOption("className", "类名");
		argsHelper.addOption("layoutName", "布局文件名");
		argsHelper.addOption("background", "@color/bg_app", "背景");

		// argsHelper.addOption("appPackage", appPackage, "应用包名");
		// argsHelper.addOption("classComment", "类注释");
		// argsHelper.addOption("layoutComment", "布局注释");
		// argsHelper.addOption("layoutName", "布局名");
		// argsHelper.addOption("javaOutDir", "java文件输出路径");
		// argsHelper.addOption("layoutOutDir", "布局文件输出路径");
		argsHelper.addOption("user", "作者");

		return argsHelper;
	}

	/**
	 * 生成初始文件
	 * 
	 * @param configFile
	 *            配置文件
	 * @param appPackage
	 *            app包名
	 * @param uiBasePackage
	 *            ui基本包名
	 * @param outDir
	 *            输出文件
	 */
	public static void gen(String configFile, String appPackage, String uiBasePackage, String outDir) {

		ArgsHelper argsHelper = createArgsHelper();

		// 读取配置文件每一行
		List<String> lines = FileHelper.readLines(configFile);
		for (String line : lines) {

			line = line.trim();
			// 若是有效行
			if (line.length() > 0 && !line.startsWith("#")) {

				argsHelper.setCommand(line);
				FragmentHelper helper = new FragmentHelper();

				helper.className = argsHelper.getValue("className");
				helper.layoutName = argsHelper.getValue("layoutName");
				helper.title = argsHelper.getValue("title");
				helper.background = argsHelper.getValue("background");
				helper.classPackage = parsePackage(argsHelper.getValue("package"), uiBasePackage);

				helper.appPackage = appPackage;
				helper.classComment = helper.title;
				helper.user = argsHelper.getValue("user");
				helper.layoutComment = helper.title + "，布局";
				helper.javaOutDir = parseJavaOutDir(outDir, helper.classPackage, uiBasePackage);
				helper.layoutOutDir = outDir + "/layout/" + argsHelper.getValue("layoutOutDir");

				if (helper != null && helper.className != null && helper.className.length() > 0) {

					System.out.println("  生成: " + helper.title);
					helper.genJavaFile();
					helper.genLayoutFile();
				}
			}
		}

		System.out.println();
		System.out.println("参数说明:");
//		System.out.println(argsHelper.getHelpString());
	}

	/**
	 * 计算包名
	 * 
	 * @param configPackage
	 *            配置的包名
	 * @param uiBasePackage
	 *            ui基本包名
	 * @return
	 */
	private static String parsePackage(String configPackage, String uiBasePackage) {

		if (configPackage.startsWith(uiBasePackage)) {

			return configPackage;
		} else {

			return uiBasePackage + "." + configPackage;
		}
	}

	/**
	 * 计算java文件输出路径
	 * 
	 * @param parentDir
	 *            父文件夹
	 * @param classPackage
	 *            所在包
	 * @param uiBasePackage
	 *            ui基本包名
	 * @return
	 */
	private static String parseJavaOutDir(String parentDir, String classPackage,
			String uiBasePackage) {

		String childDir = classPackage;

		if (childDir.startsWith(uiBasePackage)) {

			childDir = childDir.substring(uiBasePackage.length());

			if (childDir.startsWith(".")) {

				childDir = childDir.substring(1);
			}
		}
		childDir = childDir.replace('.', '/');

		return String.format("%s/%s/%s", parentDir, uiBasePackage, childDir);
	}

	/** 用于生成一个fragment */
	public static class FragmentHelper {

		// 基本信息
		public String className = "UnknownClass";
		public String layoutName = "unknown_layout";
		public String title = "标题";
		public String background = "#fff4f4f4";

		// 扩展信息
		public String classPackage;
		public String appPackage;
		public String classComment;
		public String user;
		public String leftBtnVisible = "query.id(R.id.common_title_left_btn_back).visibility(View.VISIBLE);";
		public String layoutComment;

		// 输出路径
		public String javaOutDir = "temp_out/" + classPackage;
		public String layoutOutDir = "temp_out/res_layout/";

		/** 生成java文件 */
		public void genJavaFile() {

			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd aHH:mm:ss");
			String createTime = format.format(date);

			List<KeyValue> replaces = new ArrayList<KeyValue>();
			replaces.add(new KeyValue("{$package}", classPackage));
			replaces.add(new KeyValue("{$appPackage}", appPackage));
			replaces.add(new KeyValue("{$user}", user));
			replaces.add(new KeyValue("{$createTime}", createTime));
			replaces.add(new KeyValue("{$classComment}", classComment));
			replaces.add(new KeyValue("{$className}", className));
			replaces.add(new KeyValue("{$layoutName}", layoutName));
			replaces.add(new KeyValue("{$leftBtnVisible}", leftBtnVisible));
			replaces.add(new KeyValue("{$title}", title));

			// 生成代码并写入文件
			String templeteFile = "developtools/res/templates/init_fragment_template.java";
			String outFile = javaOutDir + "/" + className + ".java";
			TemplateUtil.genByFile(templeteFile, replaces, outFile);
		}

		/** 生成布局文件 */
		public void genLayoutFile() {

			List<KeyValue> replaces = new ArrayList<KeyValue>();
			replaces.add(new KeyValue("{$layoutComment}", layoutComment));
			replaces.add(new KeyValue("{$title}", title));
			replaces.add(new KeyValue("{$background}", background));

			// 生成代码并写入文件
			String templeteFile = "developtools/res/templates/init_fragment_layout.xml";
			String outFile = layoutOutDir + "/" + layoutName + ".xml";
			TemplateUtil.genByFile(templeteFile, replaces, outFile);
		}
	}
}
