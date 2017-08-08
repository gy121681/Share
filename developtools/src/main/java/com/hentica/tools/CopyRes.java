package com.hentica.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 复制文件工具，可指定文件夹
 * 
 * @author mili
 * @createTime 2016-6-7 下午5:21:18
 */
public class CopyRes {

	/** 拷贝文件 */
	public static void copyRes(String configPath) {

		// 错误列表
		List<String> errorMsg = new ArrayList<String>();

		// 解析出配置数据(ignore、must copy)
		AllConfig allConfig = parseConfig(configPath, errorMsg);

		// 创建初始复制列表 (fromDir, toDir, preName)
		List<CopyInfo> copyList = createCopyInfos(allConfig.mFromDir, allConfig.mToDir,
				allConfig.mPreName);

		// 从ignore减去 must copy.from，得到新的ignore
		allConfig.mIgnoreFullPathes.removeAll(getFromFullPathes(allConfig.mMustCopyFiles));

		// 从复制列表中删除ignore
		removeIgnore(copyList, allConfig.mIgnoreFullPathes);

		// 从复制列表中删除must copy
		remove(copyList, allConfig.mMustCopyFiles);

		// 添加must copy到复制列表中
		copyList.addAll(allConfig.mMustCopyFiles);

		// 执行复制
		doCopy(copyList, errorMsg);

		// 显示缺失文件
		showErrors(errorMsg);

		System.out.println();
		System.out.println("所有文件复制完成");
	}

	/** 解析文件 */
	public static AllConfig parseConfig(String configPath, List<String> errorMsg) {

		AllConfig allConfig = new AllConfig();

		int lineNum = 0;
		List<String> allLines = FileHelper.readLines(configPath);
		String configDir = FileHelper.getParentPath(configPath);
		for (String line : allLines) {

			lineNum++;

			line = line.trim();
			// 忽略无效行
			if (line.isEmpty() || line.startsWith("#")) {
				continue;
			}

			// 读取全局配置
			if (line.startsWith("--")) {

				allConfig.mFromDir = getOptionValue(line, "--fromDir", allConfig.mFromDir);
				allConfig.mToDir = getOptionValue(line, "--toDir", allConfig.mToDir);
				allConfig.mPreName = getOptionValue(line, "--preName", allConfig.mPreName);
			}
			// 读取忽略文件
			else if (line.startsWith("*")) {

				line = line.substring(1).trim();
				String[] pathArr = line.split("\\s+");

				if (pathArr != null && pathArr.length > 0) {

					String ignore = FileHelper.getFullPath(configDir, pathArr[0].trim());
					allConfig.mIgnoreFullPathes.add(ignore);
				}
				// 若格式错误
				else {

					errorMsg.add(String.format("无效配置，行%d: %s", lineNum, line));
				}
			}
			// 读取必须复制文件
			else {

				String[] pathArr = line.split("\\s+");

				// 若格式正确
				if (pathArr.length == 2) {

					String oldPath = pathArr[0];
					String newPath = pathArr[1];

					CopyInfo copyInfo = new CopyInfo();
					copyInfo.mFrom = FileHelper.getFullPath(configDir, oldPath);
					copyInfo.mTo = FileHelper.getFullPath(configDir, newPath);
					copyInfo.mLineNum = lineNum;

					allConfig.mMustCopyFiles.add(copyInfo);
				}
				// 若格式错误
				else {
					errorMsg.add(String.format("无效配置，行%d: %s", lineNum, line));
				}
			}
		}

		// 把全局配置转换为绝对路径
		allConfig.mFromDir = getConfigFullPath(configDir, allConfig.mFromDir);
		allConfig.mToDir = getConfigFullPath(configDir, allConfig.mToDir);

		return allConfig;
	}

	/** 取得配置的绝对路径 */
	private static String getConfigFullPath(String configDir, String configPath) {

		if (!isEmpty(configPath)) {
			File testFile = new File(configPath);

			// 若已经是绝对路径，则直接返回
			if (testFile.isAbsolute()) {

				return configPath;
			}
			// 若不是绝对路径，则计算后返回
			else {
				return FileHelper.getFullPath(configDir, configPath);
			}
		}

		return configDir;
	}

	/** 某字符串是否为空 */
	private static boolean isEmpty(String text) {

		return text == null || text.isEmpty();
	}

	/** 取得选项值 */
	private static String getOptionValue(String line, String option, String def) {

		if (line.length() > option.length()) {

			if (line.startsWith(option + " ") || line.startsWith(option + "\t")) {

				return line.substring(option.length()).trim();
			}
		}

		return def;
	}

	/** 取得源文件列表(真实路径) */
	private static List<String> getFromFullPathes(List<CopyInfo> copyInfos) {

		List<String> result = new ArrayList<String>();

		for (CopyInfo copyInfo : copyInfos) {

			result.add(copyInfo.mFrom);
		}

		return result;
	}

	/** 创建初始复制表，仅根据源文件夹和目标文件夹 */
	private static List<CopyInfo> createCopyInfos(String fromDir, String toDir, String preName) {

		List<CopyInfo> result = new ArrayList<CopyInfo>();

		List<File> allFiles = FileHelper.findFiles(fromDir, null);
		for (File fromFile : allFiles) {

			String toFile = toDir + "/" + preName + fromFile.getName();

			CopyInfo copyInfo = new CopyInfo();
			copyInfo.mFrom = FileHelper.getFullPath(fromFile.getAbsolutePath());
			copyInfo.mTo = FileHelper.getFullPath(toFile);

			result.add(copyInfo);
		}

		return result;
	}

	/** 从复制列表中移除指定路径，指定路径可能是文件夹 */
	private static void removeIgnore(List<CopyInfo> copyInfos, Set<String> willRemove) {

		// 过滤出目录
		List<String> willRemveDir = new ArrayList<String>();
		for (String path : willRemove) {

			File file = new File(path);
			if (file.exists() && file.isDirectory()) {

				willRemveDir.add(path);
			}
		}

		for (int i = copyInfos.size() - 1; i >= 0; i--) {

			String fromPath = copyInfos.get(i).mFrom;

			// 若直接在忽略列表中，则移除
			if (willRemove.contains(fromPath)) {

				copyInfos.remove(i);
			}
			// 若不直接在忽略列表中，则继续判断
			else {

				for (String dir : willRemveDir) {

					if (!dir.endsWith(File.separator)) {

						dir += File.separator;
					}
					// 若在忽略目录中，则移除
					if (fromPath.startsWith(dir)) {

						copyInfos.remove(i);
						break;
					}
				}
			}
		}
	}

	/** 从复制列表中移除指定路径 */
	private static void remove(List<CopyInfo> copyInfos, List<CopyInfo> willRemoves) {

		Set<String> willRemovePathes = new HashSet<String>();

		for (CopyInfo willRemoveInfo : willRemoves) {

			willRemovePathes.add(willRemoveInfo.mFrom);
		}

		for (int i = copyInfos.size() - 1; i >= 0; i--) {

			// 若直接在忽略列表中，则移除
			if (willRemovePathes.contains(copyInfos.get(i).mFrom)) {

				copyInfos.remove(i);
			}
		}
	}

	/** 执行复制列表 */
	private static void doCopy(List<CopyInfo> copyInfos, List<String> errorMsg) {

		System.out.println();
		for (CopyInfo copyInfo : copyInfos) {

			File file = new File(copyInfo.mFrom);
			if (file.exists()) {

				System.out.println("复制: " + copyInfo.mFrom + " -> " + copyInfo.mTo);
				FileHelper.copyFile(copyInfo.mFrom, copyInfo.mTo);
			} else {
				errorMsg.add(String.format("缺失文件 行%d: %s", copyInfo.mLineNum, copyInfo.mFrom));
			}
		}
	}

	/** 显示缺失文件 */
	private static void showErrors(List<String> errorMsg) {

		if (errorMsg != null && !errorMsg.isEmpty()) {

			System.out.println();
			for (String error : errorMsg) {

				System.out.println(error);
			}
		}
	}

	/** 所有配置 */
	public static class AllConfig {

		/** 源文件夹 */
		public String mFromDir;
		/** 目标文件夹 */
		public String mToDir;
		/** 统一前缀 */
		public String mPreName;

		/** 配置中忽略的文件 */
		public Set<String> mIgnoreFullPathes = new HashSet<String>();
		/** 配置中要复制的文件 */
		public List<CopyInfo> mMustCopyFiles = new ArrayList<CopyInfo>();
	}

	/** 要复制的文件 */
	private static class CopyInfo {

		public int mLineNum;
		public String mFrom;
		public String mTo;
	}
}
