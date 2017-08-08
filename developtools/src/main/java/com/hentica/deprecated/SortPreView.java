package com.hentica.deprecated;

import com.hentica.tools.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 示意图排序，并拷贝到目标文件夹下
 * 
 * @author mili
 * @createTime 2016-5-31 上午11:32:24
 */
public class SortPreView {

	public static void main(String[] args) {

		String srcPath = "../../../../art/示意图";
		String outPath = "../../../../art/示意图_归类";

		// 删除临时文件夹
		FileHelper.deletePath(outPath);
		// 复制文件并重命名
		sortAllChildDirs(srcPath, outPath);

		System.out.println("所有示意图整理完成");
	}

	private static void sortAllChildDirs(String srcPath, String outPath) {

		// 先复制到目标文件夹
		FileHelper.copyDirToDir(srcPath, outPath);

		// 递归，逐一重命名
		eachAndRenameFiles(new File(outPath));
	}

	/** 重命名文件夹下的文件，且递归遍历子目录 */
	private static void eachAndRenameFiles(File dir) {

		// 应该是目录
		if (dir.isDirectory()) {

			// 子目录/文件
			File[] allChildren = dir.listFiles();
			if (allChildren != null) {

				// 分别记录文件和目录
				List<File> childOnlyFiles = new ArrayList<File>(allChildren.length);
				List<File> childDirs = new ArrayList<File>(allChildren.length);

				// 遍历子节点
				for (File child : allChildren) {

					// 若子节点是文件
					if (child.isFile()) {

						childOnlyFiles.add(child);
					}
					// 若子节点是文件夹
					else if (child.isDirectory()) {

						// 记录到结果列表中
						childDirs.add(child);
					}
				}

				// 对所有文件排序
				sortFiles(childOnlyFiles);
				// 重命名
				renameFiles(childOnlyFiles);

				// 对继续遍历子目录
				for (File file : childDirs) {

					eachAndRenameFiles(file);
				}
			}
		}
	}

	/** 文件排序 */
	private static void sortFiles(List<File> files) {

		final int maxNumCount = getMaxNumCount(files);

		Collections.sort(files, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {

				// 取文件名
				String fileName1 = o1.getName();
				String fileName2 = o2.getName();

				// 取前缀数字
				List<Integer> fileNums1 = parseNumList(fileName1);
				List<Integer> fileNums2 = parseNumList(fileName2);

				// 计算前缀值
				int fileValue1 = getScore(fileNums1, maxNumCount);
				int fileValue2 = getScore(fileNums2, maxNumCount);

				// 比较
				return fileValue1 - fileValue2;
			}
		});
	}

	/** 重命名目录下的文件 */
	private static void renameFiles(List<File> files) {

		for (int i = 0; i < files.size(); i++) {

			File file = files.get(i);
			file.renameTo(new File(file.getParent(), String.format("%03d_%s", i, file.getName())));
		}
	}

	/** 计算每组数字的值 */
	private static int getScore(List<Integer> nums, int numCount) {

		// 这里每组数字不能超过1000

		int value = 0;
		for (int i = 0; i < numCount; i++) {

			int listNum = 0;
			if (i < nums.size()) {

				listNum = nums.get(i);
			}

			value = value * 1000 + listNum;
		}

		return value;
	}

	/** 从文件名中提取数字列表 */
	private static List<Integer> parseNumList(String inputStr) {

		List<Integer> result = new ArrayList<Integer>();

		// 取出前面数字和下划线部分
		Pattern p = Pattern.compile("^[0-9|_]+");
		Matcher m = p.matcher(inputStr);

		if (m.find()) {
			// 这里只有前面部分的数字和下划线了
			inputStr = m.group();

			// 取出所有数字
			String[] numStrings = inputStr.split("_");
			for (String str : numStrings) {

				str = str.trim();

				try {
					result.add(Integer.valueOf(str));

				} catch (Exception e) {

					e.printStackTrace();
				}
			}
			// 以下划线为分割符，遍历到非数字就停止
		}
		return result;
	}

	/** 取得一组文件名中，数字最多有多少个 */
	private static int getMaxNumCount(List<File> files) {

		int maxNum = 0;

		for (File file : files) {

			List<Integer> fileNums = parseNumList(file.getName());
			maxNum = Math.max(maxNum, fileNums.size());
		}

		return maxNum;
	}
}
