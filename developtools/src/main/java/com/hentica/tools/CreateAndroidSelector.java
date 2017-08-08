package com.hentica.tools;

import java.io.BufferedReader;
import java.io.StringReader;

/** 创建安卓的selector文件 */
public class CreateAndroidSelector {

	public static void main(String[] args) {

		String configFile = "selector_config.txt";

		if (args != null && args.length > 0) {

			configFile = args[0];
		}

		createSelectorFiles(configFile);
	}

	/** 根据配置文件，创建selector文件 */
	public static void createSelectorFiles(String configFile) {

		String dir = FileHelper.getParentPath(configFile);

		String content = FileHelper.readString(configFile);

		if (content != null && !content.isEmpty()) {

			try {
				// 读文件
				BufferedReader reader = new BufferedReader(new StringReader(content));

				// 行数
				int lineNumber = 0;

				// selector创建器
				SelectorBuilder builder = new SelectorBuilder(dir);
				String line = null;
				while ((line = reader.readLine()) != null) {

					lineNumber++;
					line = line.trim();

					// 忽略空行
					if (line.isEmpty()) {

						continue;
					}

					// 若是注释行
					if (line.startsWith("#")) {

						continue;
					}

					// 至少5个，表示有新selector
					boolean isNewSelector = line.startsWith("------");

					// 若开始新文件
					if (isNewSelector) {
						if (builder.hasFile()) {
							builder.saveToFile();
						}
						// 新建对象
						builder = new SelectorBuilder(dir);
					}
					// 若没有开始文件
					else {
						// 以冒号分隔
						String[] args = line.split(":");

						// 至少有两列
						if (args != null && args.length >= 2) {

							// 去空格
							args[0] = args[0].trim();
							args[1] = args[1].trim();

							// 若是配置的保存文件
							if (args[0].equals("to")) {

								builder.file(args[1]);
							}
							// 若是配置的item
							else if (args[0].equals("tag")) {

								String[] tags = args[1].split(",");
								if (tags != null && tags.length > 0) {

									// 执行各个命令
									for (String tag : tags) {

										// 去空格
										tag = tag.trim();

										// 分隔符
										int divIndex = tag.indexOf('(');

										// 至少1个字符之后
										if (divIndex <= 0) {

											continue;
										}

										// tag格式: 命令(参数)
										String commond = tag.substring(0, divIndex).trim();
										String param = tag
												.substring(divIndex + 1, tag.length() - 1).trim();

										// 参数对应的布尔值
										boolean paramBooleanVal = "true".equals(param);

										if (commond.equals("image")) {
											builder.image(param);

										} else if (commond.equals("color")) {
											builder.color(param);

										} else if (commond.equals("focused")) {
											builder.focused(paramBooleanVal);

										} else if (commond.equals("window_focused")) {
											builder.window_focused(paramBooleanVal);

										} else if (commond.equals("enabled")) {
											builder.enabled(paramBooleanVal);

										} else if (commond.equals("checkable")) {
											builder.checkable(paramBooleanVal);

										} else if (commond.equals("checked")) {
											builder.checked(paramBooleanVal);

										} else if (commond.equals("selected")) {
											builder.selected(paramBooleanVal);

										} else if (commond.equals("pressed")) {
											builder.pressed(paramBooleanVal);

										} else if (commond.equals("activated")) {
											builder.activated(paramBooleanVal);

										} else if (commond.equals("active")) {
											builder.active(paramBooleanVal);
										} else {
											System.out.println("未知命令(" + lineNumber + "): " + tag);
										}
									}// end for

									// 一行结束，保存item
									builder.saveItem();
								}
							}

						} else {
							System.out.println("无效行(" + lineNumber + "): " + line);
						}
					}
				}
				reader.close();

				// 保存最后一个文件
				if (builder.hasFile()) {
					builder.saveToFile();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	/** 用于创建selector */
	protected static class SelectorBuilder {

		/** 当前item的可画对象 */
		String mCurrItemDrawable;

		/** 当前item的所有状态 */
		String mCurrItemStates = "";

		/** 所有item */
		String mAllItemString = "";

		/** 注释 */
		String mComment;

		/** 文件名 */
		String mFile;

		/** 相对路径 */
		String mDir;

		public SelectorBuilder() {
		}

		public SelectorBuilder(String dir) {

			mDir = dir;
		}

		/** 保存当前item */
		public SelectorBuilder saveItem() {

			String item = "";
			if (mCurrItemDrawable != null) {

				item = "\r\n<item ";
				item += mCurrItemDrawable;
				item += mCurrItemStates;
				item += "></item>";
			}

			// 记录所有的item
			mAllItemString += item;

			// 重置
			mCurrItemDrawable = null;
			mCurrItemStates = "";

			return this;
		}

		/** 文件 */
		public SelectorBuilder file(String file) {
			mFile = file;
			return this;
		}

		/** 注释 */
		public SelectorBuilder comment(String comment) {
			mComment = comment;
			return this;
		}

		/** 图像 */
		public SelectorBuilder image(String image) {
			mCurrItemDrawable = " android:drawable=\"@drawable/" + image + "\"";
			return this;
		}

		/** 颜色 */
		public SelectorBuilder color(String color) {
			mCurrItemDrawable = " android:color=\"@color/" + color + "\"";
			return this;
		}

		/** 状态，焦点 */
		public SelectorBuilder focused(boolean tag) {

			mCurrItemStates += " android:state_focused=\"" + tag + "\"";
			return this;
		}

		/** 状态，窗口焦点？ */
		public SelectorBuilder window_focused(boolean tag) {

			mCurrItemStates += " android:state_window_focused=\"" + tag + "\"";
			return this;
		}

		/** 状态，可用 */
		public SelectorBuilder enabled(boolean tag) {

			mCurrItemStates += " android:state_enabled=\"" + tag + "\"";
			return this;
		}

		/** 状态，可选 */
		public SelectorBuilder checkable(boolean tag) {

			mCurrItemStates += " android:state_checkable=\"" + tag + "\"";
			return this;
		}

		/** 状态，选中 */
		public SelectorBuilder checked(boolean tag) {

			mCurrItemStates += " android:state_checked=\"" + tag + "\"";
			return this;
		}

		/** 状态，选中 */
		public SelectorBuilder selected(boolean tag) {

			mCurrItemStates += " android:state_selected=\"" + tag + "\"";
			return this;
		}

		/** 状态，按下 */
		public SelectorBuilder pressed(boolean tag) {

			mCurrItemStates += " android:state_pressed=\"" + tag + "\"";
			return this;
		}

		/** 状态，可激活？ */
		public SelectorBuilder activated(boolean tag) {

			mCurrItemStates += " android:state_activated=\"" + tag + "\"";
			return this;
		}

		/** 状态，激活？ */
		public SelectorBuilder active(boolean tag) {

			mCurrItemStates += " android:state_active=\"" + tag + "\"";
			return this;
		}

		/** 是否指定了保存文件 */
		public boolean hasFile() {

			return mFile != null && !mFile.isEmpty();
		}

		/** 保存到文件 */
		public SelectorBuilder saveToFile() {

			// 是否写入成功
			boolean isWriteSuccess = false;

			if (mFile != null) {

				String fullPath = mDir != null ? mDir + "/" + mFile : mFile;
				isWriteSuccess = FileHelper.writeString(fullPath, this.toString());
			}

			if (isWriteSuccess) {

				System.out.println("保存成功: " + mFile);
			} else {
				System.out.println("保存失败: " + mFile);
			}
			return this;
		}

		@Override
		public String toString() {

			this.saveItem();

			String result = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n";
			result += "<selector xmlns:android=\"http://schemas.android.com/apk/res/android\">\r\n";
			if (mComment != null) {

				result += "\r\n<!-- " + mComment + " -->";
			}
			result += mAllItemString;
			result += "\r\n\r\n</selector>";

			return result;
		}
	}
}
