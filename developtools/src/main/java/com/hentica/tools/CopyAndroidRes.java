package com.hentica.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 批量拷贝，只拷贝指定的文件 */
public class CopyAndroidRes {

	/** 拷贝文件 */
	public static void copyRes(String configPath) {

		// 旧完整文件名、配置文件名映射
		Map<String, String> configFileMap = new LinkedHashMap<String, String>();
		// 旧完整文件名、新文件名映射
		Map<String, String> configFullPathFileMap = new LinkedHashMap<String, String>();
		// 忽略文件名
		Set<String> ignoreFileSet = new HashSet<String>();
		// 空格太多的行
		List<String> moreSpaceLines = new ArrayList<String>();
		// 未知错误的行
		List<String> errorLines = new ArrayList<String>();

		// 读取配置文件
		// 取得资源文件列表
		String mapConifg = FileHelper.readString(configPath, "utf-8");
		String configDir = FileHelper.getParentPath(configPath);
		try {
			BufferedReader reader = new BufferedReader(new StringReader(mapConifg));

			String line = null;
			while ((line = reader.readLine()) != null) {

				line = line.trim();
				// 忽略注释
				if (line.startsWith("#")) {
					continue;
				}

				// 忽略文件
				boolean isIgnoreFile = line.startsWith("*");
				if (isIgnoreFile) {
					line = line.substring(1).trim();
				}

				String[] pathArr = line.split("\\s+");

				// 若文件路径配置是两个
				if (pathArr.length == 2) {

					String oldPath = pathArr[0];
					String newPath = pathArr[1];

					// 完整路径
					String fullPath = FileHelper.getFullPath(configDir, oldPath);

					// 若是正常文件
					if (!isIgnoreFile) {

						// 把资源路径加入map
						configFullPathFileMap.put(fullPath, newPath);
						configFileMap.put(fullPath, oldPath);
					}
					// 若是要忽略的文件
					else {
						ignoreFileSet.add(fullPath);
					}

				}
				// 若文件路径配置不是两个
				else {

					// 若不是忽略文件
					if (!isIgnoreFile) {

						// 若多于2个
						if (pathArr.length > 2) {

							moreSpaceLines.add(line);

						} else if (!line.isEmpty()) {
							errorLines.add(line);
						}
					}
				}

				// 加入忽略列表
				if (isIgnoreFile) {

					if (pathArr.length > 0) {

						String ignorePath = FileHelper.getFullPath(configDir, pathArr[0]);
						ignoreFileSet.add(ignorePath);
					}
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		// 取得所有文件夹
		Set<String> allFromDir = new LinkedHashSet<String>();
		for (String oldFilePath : configFullPathFileMap.keySet()) {

			File oldFile = new File(oldFilePath);
			if (oldFile.isFile()) {

				try {

					allFromDir.add(oldFile.getParentFile().getCanonicalPath());
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}

		// 取得所有文件夹下的文件
		Set<String> allFromFiles = new LinkedHashSet<String>();
		for (String dirPath : allFromDir) {

			File dir = new File(dirPath);
			if (dir.isDirectory()) {

				File[] childFiles = dir.listFiles();
				Arrays.sort(childFiles, new Comparator<File>() {
					@Override
					public int compare(File o1, File o2) {

						return o1.getName().compareTo(o2.getName());
					}
				});

				// 遍历所有文件
				for (File file : childFiles) {

					if (file.isFile()) {

						try {

							String filePath = file.getCanonicalPath();
							allFromFiles.add(filePath);

						} catch (Exception e) {

							e.printStackTrace();
						}
					}
				}
			}
		}

		// 取得共同都有文件
		Set<String> commonFiles = getCommonSet(configFullPathFileMap.keySet(), allFromFiles);
		// 取得配置文件有，但是实际文件没有的文件名
		Set<String> notInFiles = getNotExistSet(configFullPathFileMap.keySet(), allFromFiles);
		// 取得配置文件没有，但是实际文件有的文件名
		Set<String> notInConfigFile = getNotExistSet(allFromFiles, configFullPathFileMap.keySet());
		// 去掉忽略文件
		notInConfigFile.removeAll(ignoreFileSet);

		// 拷贝共同的文件
		for (String fromFile : commonFiles) {

			String toFile = configFullPathFileMap.get(fromFile);

			System.out.println("拷贝 " + configFileMap.get(fromFile) + " -> " + toFile);
			FileHelper.copyFile(fromFile, toFile);
		}
		// 打印不是共同的文件
		System.out.println();
		for (String file : notInConfigFile) {

			System.out.println("缺少配置: " + file);
		}
		System.out.println();
		for (String file : notInFiles) {

			System.out.println("无效配置: " + configFileMap.get(file));
		}

		for (String line : moreSpaceLines) {

			System.out.println("空格太多： " + line);
		}
		for (String line : errorLines) {

			System.out.println("未知错误： " + line);
		}

		if (notInConfigFile.isEmpty() && notInFiles.isEmpty()) {

			System.out.println("拷贝完成");
		}
	}

	/** 遍历set1，取得set1比set2多余的值 */
	private static Set<String> getNotExistSet(Set<String> set1, Set<String> set2) {

		Set<String> notInSet2 = new LinkedHashSet<String>();

		// 找到set2中没有的值
		for (String key : set1) {

			if (!set2.contains(key)) {

				notInSet2.add(key);
			}
		}

		return notInSet2;
	}

	/** 取得两个set共同的部分 */
	private static Set<String> getCommonSet(Set<String> set1, Set<String> set2) {

		Set<String> commonSet = new LinkedHashSet<String>();

		for (String key : set1) {

			if (set2.contains(key)) {

				commonSet.add(key);
			}
		}

		return commonSet;
	}

}
