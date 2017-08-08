package com.hentica.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "ConstantConditions", "ResultOfMethodCallIgnored", "SimplifiableIfStatement"})
public class FileHelper {

	/**
	 * 把某文件、文件夹拷贝到另一路径下面，两个参数：from to
	 */
	public static void main(String[] args) {

		// args = new String[2];
		// args[0] = "E:\\Tencent\\812659179\\FileRecv\\格斗\\shanggukuilei\\1";
		// args[1] = "E:\\Tencent\\812659179\\FileRecv\\格斗\\shanggukuilei\\2";

		if (args != null && args.length > 1) {

			String from = args[0];

			String to = args[1];

			copyInDir(from, to);
		}

		System.exit(0);
	}

	/** 拷贝文件，仅针对单个文件 */
	public static boolean copyFile(String from, String to) {

		return copyFile(new File(from), new File(to));
	}

	/** 拷贝文件，仅针对单个文件 */
	public static boolean copyFile(File from, File to) {

		boolean isCopy = false;

		FileInputStream fi = null;

		FileOutputStream fo = null;

		FileChannel in = null;

		FileChannel out = null;

		try {
			// 若是同一路径，则不拷贝
			if (from.getPath().equals(to.getPath())) {

				return isCopy;
			}

			// 创建文件夹
			File parent = to.getParentFile();
			if (parent != null && !parent.exists()) {

				parent.mkdirs();
			}

			fi = new FileInputStream(from);

			fo = new FileOutputStream(to);

			in = fi.getChannel();// 得到对应的文件通道

			out = fo.getChannel();// 得到对应的文件通道

			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道

			isCopy = true;

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (fi != null)
					fi.close();

				if (in != null)
					in.close();

				if (fo != null)
					fo.close();

				if (out != null)
					out.close();

			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		return isCopy;
	}

	/** 拷贝文件，仅针对单个文件 */
	public static boolean copyFile(InputStream iStream, OutputStream oStream) {

		boolean isCopy = false;

		if (iStream != null && oStream != null) {

			try {

				byte[] buffer = new byte[1024 * 8 * 10]; // 10KB
				int byteread = 0;
				while ((byteread = iStream.read(buffer)) != -1) {

					oStream.write(buffer, 0, byteread);
				}

				isCopy = true;

			} catch (Exception e1) {

				e1.printStackTrace();

			} finally {

				if (iStream != null) {

					try {
						iStream.close();

					} catch (IOException e1) {

						e1.printStackTrace();
					}
				}

				if (oStream != null) {

					try {
						oStream.close();

					} catch (IOException e1) {

						e1.printStackTrace();
					}
				}
			}
		}
		return isCopy;
	}

	/**
	 * 拷贝某文件或文件夹的内容到指定目录下，兼容文件和目录
	 * 
	 * @param from
	 *            某文件或文件夹
	 * @param toDir
	 *            指定目录
	 */
	public static boolean copyInDir(String from, String toDir) {

		if (from != null && toDir != null) {

			File fromFile = new File(from);

			if (fromFile.isFile()) { // 若源文件是文件

				return copyFileInDir(from, toDir);

			} else if (fromFile.isDirectory()) { // 若源文件是文件夹

				return copyDirToDir(from, toDir + "/" + new File(from).getName());
			}
		}
		return false;
	}

	/** 拷贝某文件到指定目录下 */
	public static boolean copyFileInDir(String from, String toDir) {

		if (from != null && toDir != null) {

			return copyFile(from, toDir + "/" + new File(from).getName());
		}
		return false;
	}

	/**
	 * 拷贝文件夹到某路径
	 */
	public static boolean copyDirToDir(String from, String toDir) {

		System.out.println("copy " + from + "\n  to " + toDir);

		boolean isCopy = false;

		if (from != null && toDir != null) {

			File fromFile = new File(from);
			File toFile = new File(toDir);

			// 若是同一路径，则不拷贝
			if (fromFile.getPath().equals(toFile.getPath())) {

				return isCopy;
			}

			if (!toFile.exists()) { // 若目录路径不存在

				toFile.mkdirs(); // 创建目标目录

			} else if (toFile.isFile()) { // 若目录路径是个文件

				return isCopy; // 则不能继续拷贝了
			}

			isCopy = true; // 只要有一个文件拷贝失败了，那么返回失败

			// 把from下面的内容全部拷贝到to下面
			for (File fromChild : fromFile.listFiles())// 遍历from下面的每个
			{
				boolean isChildCopy = false; // 子文件/目录是否拷贝成功

				if (fromChild.isFile()) { // 若from子文件是文件

					// 拷贝此文件
					isChildCopy = copyFileInDir(fromChild.getPath(), toDir);

				} else if (fromChild.isDirectory()) {// 若from子文件是目录

					isChildCopy = copyDirToDir(fromChild.getPath(),
							toDir + "/" + fromChild.getName());
				}

				if (!isChildCopy) { // 只要有一个文件拷贝失败了，那么返回失败

					isCopy = false;
				}
			}
		}

		return isCopy;
	}

	/**
	 * 删除某路径，文件或文件夹都可以
	 * 
	 * @param path
	 *            文件或文件夹
	 */
	public static void deletePath(String path) {

		// 若路径有效
		if (path != null && !path.isEmpty()) {

			File file = new File(path);

			if (file.isFile()) { // 若是文件，则删除文件

				file.delete();

			} else if (file.isDirectory()) { // 若是文件夹，则删除文件夹

				deleteDir(path);
			}
		}
	}

	/** 删除某文件 */
	public static void deleteFile(String path) {

		if (path != null && !path.isEmpty()) {

			File file = new File(path);

			if (file.exists()) {

				file.delete();
			}
		}
	}

	/** 删除某文件夹 */
	public static void deleteDir(String path) {

		if (path != null && !path.isEmpty()) {

			File dir = new File(path); // 待删除的文件夹对象

			deleteDir(dir); // 删除
		}
	}

	/** 删除某文件夹 */
	public static void deleteDir(File dir) {

		if (dir.exists()) { // 若此文件夹存在

			File[] children = dir.listFiles();// 子文件列表

			if (children.length == 0) { // 若没有子文件

				dir.delete(); // 删除

			} else { // 若有子文件

				for (File child : dir.listFiles()) {

					if (child.isFile()) { // 若子文件是文件

						child.delete(); // 删除

					} else if (child.isDirectory()) { // 若子文件是目录

						deleteDir(child); // 递归删除
					}
				}
			}
		}
	}

	/** 查找符合条件的文件，仅文件 */
	public static List<File> findFiles(String dirPath, FileFilter filter) {

		if (dirPath != null) {

			List<File> files = new ArrayList<>();
			findFiles(new File(dirPath), filter, files);
			return files;
		}
		return null;
	}

	/** 查找符合条件的文件，仅文件 */
	private static void findFiles(File dir, final FileFilter filter, List<File> files) {

		if (dir != null) {

			File[] children = dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {

					if (file.isDirectory()){
						return true;
					}
					if (filter == null){
						return true;
					}
					return filter.accept(file);
				}
			});

			if (children != null) {

				for (File file : children) {

					if (file.isFile()) {

						files.add(file);

					} else {

						findFiles(file, filter, files);
					}
				}
			}
		}
	}

	/** 取得短文件名 */
	public static String getSortFileName(String path) {

		File file = new File(path);
		return file.getName();
	}

	/** 读取字符串，会关闭流 */
	public static String readString(InputStream iStream) {

		StringBuilder builder = new StringBuilder();

		try {

			byte[] buffer = new byte[1024 * 8 * 10]; // 10KB
			int byteread;
			while ((byteread = iStream.read(buffer)) != -1) {

				builder.append(new String(buffer, 0, byteread));
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {
				iStream.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return builder.toString();
	}

	/** 读取字符串，默认编码 */
	public static String readString(String path) {

		FileInputStream in = null;
		try {
			StringBuilder builder = new StringBuilder();
			// 一次读多个字节
			char[] tempchars = new char[100];
			in = new FileInputStream(path);
			InputStreamReader reader = new InputStreamReader(in);

			int byteread;
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			while ((byteread = reader.read(tempchars)) != -1) {

				builder.append(tempchars, 0, byteread);
			}

			return builder.toString();

		} catch (Exception e1) {

			e1.printStackTrace();

		} finally {

			if (in != null) {
				try {
					in.close();

				} catch (IOException e1) {

					e1.printStackTrace();
				}
			}
		}
		return "";
	}

	/** 读取字符串，文件以指定格式编码 */
	public static String readString(String path, String charsetName) {

		FileInputStream in = null;
		try {
			StringBuilder builder = new StringBuilder();
			// 一次读多个字节
			char[] tempchars = new char[100];
			in = new FileInputStream(path);
			InputStreamReader reader = new InputStreamReader(in, charsetName);

			int byteread;
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			while ((byteread = reader.read(tempchars)) != -1) {

				builder.append(tempchars, 0, byteread);
			}

			return builder.toString();

		} catch (Exception e1) {

			e1.printStackTrace();

		} finally {

			if (in != null) {

				try {

					in.close();

				} catch (IOException e1) {

					e1.printStackTrace();
				}
			}
		}
		return "";
	}

	/** 读取行 */
	public static List<String> readLines(String path) {

		List<String> result = new ArrayList<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
			String tempString ;
			// 一次读一行，读入null时文件结束
			while ((tempString = reader.readLine()) != null) {

				result.add(tempString);
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

		return result;
	}

	/** 是否是utf8文本 */
	public static boolean isUtf8(String path) {

		boolean isUtf8 = false;
		RandomAccessFile ramdomReader = null;

		try {

			byte[] header = new byte[3];
			ramdomReader = new RandomAccessFile(path, "r");

			// 若文件头读取成功
			if (ramdomReader.read(header) != -1) {

				// 若是utf8头
				if (header[0] == 0xEF && header[1] == 0xBB && header[2] == 0xBF) {

					return isUtf8;
				}
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			if (ramdomReader != null) {

				try {

					ramdomReader.close();

				} catch (Exception e2) {

					e2.printStackTrace();
				}
			}
		}

		return isUtf8;
	}

	/** 读取utf8文本 */
	public static String readUtf8String(String path) {

		FileInputStream in = null;
		try {
			StringBuilder builder = new StringBuilder();
			// 一次读多个字节
			char[] tempchars = new char[100];
			in = new FileInputStream(path);

			// 若是utf8文本，则忽略前3个字节
			if (isUtf8(path)) {

				in.read();
				in.read();
				in.read();
			}

			InputStreamReader reader = new InputStreamReader(in);

			int byteread;
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			while ((byteread = reader.read(tempchars)) != -1) {

				builder.append(tempchars, 0, byteread);
			}

			return builder.toString();

		} catch (Exception e1) {

			e1.printStackTrace();

		} finally {

			if (in != null) {

				try {

					in.close();

				} catch (IOException e1) {

					e1.printStackTrace();
				}
			}
		}
		return "";
	}

	/**
	 * 写入字符串，返回是否写入成功
	 * 
	 * @param path
	 *            保存路径
	 * @param str
	 *            要保存的字符串
	 */
	public static boolean writeString(String path, String str) {

		boolean isWrite = false;
		FileWriter writer = null;
		try {
			// 先创建目录
			File file = new File(path);
			File parent = file.getParentFile();
			if (parent != null && !parent.exists()) {
				//noinspection ResultOfMethodCallIgnored
				parent.mkdirs();
			}

			// 再写文件
			writer = new FileWriter(file);
			writer.append(str);
			writer.flush();

			isWrite = true;

		} catch (Exception e1) {

			e1.printStackTrace();

		} finally {

			if (writer != null) {
				try {
					writer.close();

				} catch (IOException e1) {

					e1.printStackTrace();
				}
			}
		}
		return isWrite;
	}

	/** 取得完整路径 */
	public static String getFullPath(String dir, String path) {

		try {
			return new File(dir, path).getCanonicalPath();

		} catch (Exception e) {

			e.printStackTrace();
		}
		return "";
	}

	/** 取得完整路径 */
	public static String getFullPath(String path) {

		try {
			return new File(path).getCanonicalPath();

		} catch (Exception e) {

			e.printStackTrace();
		}
		return "";
	}

	/** 取得父目录路径 */
	public static String getParentPath(String path) {

		try {
			return new File(getFullPath(path)).getParentFile().getCanonicalPath();

		} catch (Exception e) {

			e.printStackTrace();
		}
		return "";
	}
}
