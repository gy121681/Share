package com.hentica.tools.image;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hentica.tools.FileHelper;

/** 9png配置解析 */
public class NinePngConfigPaser {

	/**
	 * 解析出配置文件内容
	 * 
	 * @param path
	 *            配置文件路径
	 * @return
	 */
	public List<NinePngInfo> parse(String path) {

		String content = FileHelper.readString(path);
		File file = new File(path);
		String dirPath = "";
		try {
			dirPath = new File(file.getCanonicalPath()).getParentFile().getAbsolutePath();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return this.parse(content, dirPath);
	}

	/**
	 * 解析出配置文件内容
	 * 
	 * @param content
	 *            配置文件内容
	 * @param dirPath
	 *            配置文件文件夹路径
	 * @return
	 */
	public List<NinePngInfo> parse(String content, String dirPath) {

		// 结果
		List<NinePngInfo> result = new ArrayList<NinePngInfo>();

		if (content == null) {
			return result;
		}

		// 读取字符串
		BufferedReader reader = new BufferedReader(new StringReader(content));
		try {
			NinePngInfo currInfo = null;

			String tempLine = null;
			while ((tempLine = reader.readLine()) != null) {

				// 去空格
				tempLine = tempLine.trim();

				// 注释，跳过
				if (tempLine.startsWith("#")) {
					continue;
				}

				// 分割线
				if (tempLine.startsWith("-----")) {

					// 添加新记录
					if (currInfo != null) {

						result.add(currInfo);
						currInfo = null;
					}
				}

				// 若是功能行
				if (isFunctionLine(tempLine)) {

					// 创建一条新配置
					if (currInfo == null) {

						currInfo = new NinePngInfo();
						currInfo.mBasePath = dirPath;
					}

					// 当前行对应的距离段
					List<Range> tempRanges = this.parseRanges(tempLine);
					// 当前行第一个距离段
					Range firstRange = (tempRanges != null && !tempRanges.isEmpty()) ? tempRanges
							.get(0) : null;

					// 解析各个段
					if (tempLine.startsWith("left")) {
						currInfo.mLeft = tempRanges;

					} else if (tempLine.startsWith("top")) {
						currInfo.mTop = tempRanges;

					} else if (tempLine.startsWith("right")) {
						currInfo.mRight = tempRanges;

					} else if (tempLine.startsWith("bottom")) {
						currInfo.mBottom = tempRanges;

					} else if (tempLine.startsWith("deleteLeft")) {
						currInfo.mDeleteLeft = firstRange;

					} else if (tempLine.startsWith("deleteTop")) {
						currInfo.mDeleteTop = firstRange;

					} else if (tempLine.startsWith("src")) {
						currInfo.mSrcImage = this.parsePath(tempLine);

					} else if (tempLine.startsWith("target")) {
						currInfo.mTargetImage = this.parsePath(tempLine);
					}
				}
			}

			// 添加新记录
			if (currInfo != null) {

				result.add(currInfo);
				currInfo = null;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		// 去除无效配置
		result = this.clipVaildRange(result);

		// 合并距离
		result = this.mergeAllRanges(result);

		return result;
	}

	/** 解析出一行对应的路径 */
	private String parsePath(String line) {

		if (line != null) {

			int divIdx = line.indexOf("=") + 1;

			try {
				if (divIdx >= 0) {

					return line.substring(divIdx, line.length());
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		return null;
	}

	/** 解析出一行的配置 */
	private List<Range> parseRanges(String line) {

		List<Range> result = new ArrayList<Range>();

		if (line != null) {

			int start = line.indexOf('[') + 1;
			int end = line.indexOf(']');

			// 若没有内容，则返回空
			if (start < 0 || end < 0 || start >= end) {

				return result;
			}

			String allRangeStr = line.substring(start, end).trim();
			String[] rangeStrings = allRangeStr.split(",");

			// 每条距离
			for (String rangeStr : rangeStrings) {

				// 纯数字、数字-数字
				int splitIdx = rangeStr.indexOf('-');

				try {

					// 数字-数字
					if (splitIdx > 0) {

						String firstStr = rangeStr.substring(0, splitIdx).trim();
						String secondStr = rangeStr.substring(splitIdx + 1, rangeStr.length())
								.trim();

						// 要不为空才能转换为数字
						if (firstStr.isEmpty() || secondStr.isEmpty()) {

							continue;
						}
						// 解析为数字
						int firstValue = Integer.valueOf(firstStr);
						int secondValue = Integer.valueOf(secondStr);

						// 加入结果中
						result.add(new Range(firstValue, secondValue));
					}
					// 纯数字
					else {

						String valueStr = rangeStr.trim();

						// 要不为空才能转换为数字
						if (valueStr.isEmpty()) {
							continue;
						}

						int rangeValue = Integer.valueOf(valueStr);

						// 加入结果中
						result.add(new Range(rangeValue, rangeValue));
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}

		return result;
	}

	/** 一行文字是否是功能行 */
	private boolean isFunctionLine(String line) {

		// 关键字
		String[] keyWords = { "src", "target", "left", "bottom", "right", "top", "deleteLeft",
				"deleteTop", };

		for (String string : keyWords) {

			if (line.startsWith(string)) {

				return true;
			}
		}

		return false;
	}

	/** 筛选出有效区间 */
	private List<NinePngInfo> clipVaildRange(List<NinePngInfo> from) {

		// 开始值不能比结束值小
		for (NinePngInfo ninePngInfo : from) {

			ninePngInfo.mLeft = this.clipRange(ninePngInfo.mLeft);
			ninePngInfo.mTop = this.clipRange(ninePngInfo.mTop);
			ninePngInfo.mRight = this.clipRange(ninePngInfo.mRight);
			ninePngInfo.mBottom = this.clipRange(ninePngInfo.mBottom);

			if (ninePngInfo.mDeleteLeft != null
					&& ninePngInfo.mDeleteLeft.start > ninePngInfo.mDeleteLeft.end) {

				ninePngInfo.mDeleteLeft = null;
			}
			if (ninePngInfo.mDeleteTop != null
					&& ninePngInfo.mDeleteTop.start > ninePngInfo.mDeleteTop.end) {
				ninePngInfo.mDeleteTop = null;
			}
		}

		return from;
	}

	/** 筛选出有效区间 */
	private List<Range> clipRange(List<Range> from) {

		List<Range> result = new ArrayList<Range>();

		if (from != null) {

			for (Range range : from) {

				if (range.start <= range.end) {

					result.add(new Range(range.start, range.end));
				}
			}
		}

		return result;
	}

	/** 合并相同范围 */
	private List<NinePngInfo> mergeAllRanges(List<NinePngInfo> from) {

		for (NinePngInfo ninePngInfo : from) {

			ninePngInfo.mLeft = this.merge(ninePngInfo.mLeft);
			ninePngInfo.mTop = this.merge(ninePngInfo.mTop);
			ninePngInfo.mRight = this.merge(ninePngInfo.mRight);
			ninePngInfo.mBottom = this.merge(ninePngInfo.mBottom);
		}

		return from;
	}

	/**
	 * 合并一系列区间
	 * 
	 * @param from
	 * @return
	 */
	private List<Range> merge(List<Range> from) {
		// 思路：
		// 新建一个列表，用于保存结果
		// 新建一个数组，用于保存所有的起始和末点；
		// 对所有的点排序；
		// 新建一个标识数，遍历所有的点，碰到起点则+1,碰到末点则-1，若一点既是起点又是末点，则不计数；
		// 当标识数为0时，碰到起点了，则一个新段开始，记录起点位置；
		// 当碰到末点后，标识数减为0了，则一个段结束了，记录结束位置，并把整段记录到结果中；

		// 结果
		List<Range> result = new ArrayList<Range>();
		// 同时保存所有的开始点，结束点
		List<Integer> allPointList = new ArrayList<Integer>();

		// 所有的开始点、结束点，不重复
		Map<Integer, Integer> startSet = new HashMap<Integer, Integer>();
		Map<Integer, Integer> endSet = new HashMap<Integer, Integer>();
		Set<Integer> allPoints = new HashSet<Integer>();
		// 记录所有的开始点、结束点
		for (Range range : from) {

			// 添加计数
			this.addCount(startSet, range.start);
			this.addCount(endSet, range.end);
			// 记录所有点
			allPoints.add(range.start);
			allPoints.add(range.end);
		}

		// 添加所有的点到数组中，排序用
		allPointList.addAll(allPoints);

		// 从小到大排序
		Collections.sort(allPointList, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {

				return o1 - o2;
			}
		});

		// 依次遍历所有点
		int rangFlag = 0; // 区间标记，遇到开始则+1,遇到结束则-1，同时遇到则不变
		int tempStart = -1; // 当前开始标记
		for (Integer val : allPointList) {

			// 若遇到开始段了，记录
			if (rangFlag == 0) {

				tempStart = val;
			}

			// 计算当前值被多少个起点、末点所占有
			int startCount = this.getCount(startSet, val);
			int endCount = this.getCount(endSet, val);
			rangFlag += startCount - endCount;

			// 若遇到结束段
			if (rangFlag == 0) {

				// 新建段，并加入结果
				Range range = new Range();
				range.start = tempStart;
				range.end = val;

				result.add(range);
			}
		}

		return result;
	}

	/** 添加一个计数 */
	private void addCount(Map<Integer, Integer> map, int key) {

		if (map != null) {

			int count = getCount(map, key);
			map.put(key, count + 1);
		}
	}

	/** 取得计数 */
	private int getCount(Map<Integer, Integer> map, int key) {

		if (map != null) {

			Integer count = map.get(key);
			return count != null ? count : 0;
		}
		return 0;
	}

	/** 9png信息 */
	public static class NinePngInfo {

		/** 当前路径 */
		public String mBasePath = null;
		/** 源文件 */
		public String mSrcImage = null;
		/** 输出文件 */
		public String mTargetImage = null;

		/** 缩放区域，左方 */
		public List<Range> mLeft = null;
		/** 缩放区域，上方 */
		public List<Range> mTop = null;

		/** 文字区域，右方 */
		public List<Range> mRight = null;
		/** 文字区域，下方 */
		public List<Range> mBottom = null;

		/** 删除区域，左方 */
		public Range mDeleteLeft = null;
		/** 删除区域，上方 */
		public Range mDeleteTop = null;

		@Override
		public String toString() {

			StringBuilder builder = new StringBuilder();

			builder.append("\n");
			builder.append("path:" + mBasePath + "\n");
			builder.append("src:" + mSrcImage + "\n");
			builder.append("target:" + mTargetImage + "\n");
			builder.append("left:" + mLeft + "\n");
			builder.append("top:" + mTop + "\n");
			builder.append("right:" + mRight + "\n");
			builder.append("bottom:" + mBottom + "\n");
			builder.append("deleteLeft:" + mDeleteLeft + "\n");
			builder.append("deleteTop:" + mDeleteTop + "\n");

			return builder.toString();
		}
	}

	/** 范围类 */
	public static class Range {

		/** 开始位置 */
		public int start;
		/** 结束位置 */
		public int end;

		public Range() {
			this(-1, -1);
		}

		public Range(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public String toString() {
			return "(" + start + ", " + end + ")";
		}
	}
}
