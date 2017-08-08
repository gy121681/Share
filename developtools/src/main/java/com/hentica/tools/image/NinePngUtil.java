package com.hentica.tools.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.hentica.tools.image.NinePngConfigPaser.NinePngInfo;
import com.hentica.tools.image.NinePngConfigPaser.Range;

/** 9png工具 */
public class NinePngUtil {

	public static void main(String[] args) {

		// String configFile =
		// "E:\\Outsourcing\\dentistry\\art\\_013_9pngConfig.txt";
		String configFile = "9pngConfig.txt";

		if (args != null && args.length > 0) {

			configFile = args[0];
		}

		List<NinePngInfo> result = parseNinePngInfo(configFile);

		for (NinePngInfo ninePngInfo : result) {

			create9Png(ninePngInfo);
		}
	}

	/***
	 * 创建一个9png文件
	 * 
	 * @param config
	 *            配置
	 */
	public static void create9Png(NinePngInfo config) {

		try {
			// String imagePath =
			// "E:\\Outsourcing\\iDste\\self\\art\\元素\\temp9png\\common_second_title_checkedbg.png";
			// String toPath =
			// "E:\\Outsourcing\\iDste\\self\\art\\元素\\temp9png\\common_second_title_checkedbg_2.png";

			String imagePath = config.mBasePath + "/" + config.mSrcImage;
			String toPath = config.mBasePath + "/" + config.mTargetImage;

			InputStream in = new FileInputStream(imagePath);
			BufferedImage oldImg = ImageIO.read(in);
			in.close();

			// 复制图片
			BufferedImage newImg = new BufferedImage(oldImg.getWidth() + 2, oldImg.getHeight() + 2,
					BufferedImage.TYPE_INT_ARGB);
			newImg.getGraphics().drawImage(oldImg, 1, 1, oldImg.getWidth(), oldImg.getHeight(),
					null);

			// 取得所有黑点坐标
			List<Vec2> allFlagPoints = parseAllVec2(newImg.getWidth(), newImg.getHeight(), config);

			// 画黑点
			Color blackColor = new Color(0, 0, 0);
			for (Vec2 flgPoint : allFlagPoints) {

				newImg.setRGB(flgPoint.x, flgPoint.y, blackColor.getRGB());
			}

			// 写入图片
			Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("png");
			ImageWriter writer = it.next();
			File f = new File(toPath);
			if (f.getParentFile() != null && !f.getParentFile().exists()) {

				f.getParentFile().mkdirs();
			}
			ImageOutputStream ios = ImageIO.createImageOutputStream(f);
			writer.setOutput(ios);
			writer.write(newImg);
			oldImg.flush();
			ios.flush();
			ios.close();

			System.out.println("成功: " + config.mSrcImage + ", imgsize: " + oldImg.getWidth() + ", "
					+ oldImg.getHeight());

		} catch (Exception e) {

			System.out.println("成功: " + config.mSrcImage);
			e.printStackTrace();
		}
	}

	/**
	 * 解析出
	 * 
	 * @param configPath
	 *            配置文件位置
	 * @return
	 */
	public static List<NinePngInfo> parseNinePngInfo(String configPath) {

		NinePngConfigPaser paser = new NinePngConfigPaser();
		return paser.parse(configPath);
	}

	/** 解析出所有要打黑点的坐标 */
	private static List<Vec2> parseAllVec2(int width, int height, NinePngInfo config) {

		List<Vec2> result = new ArrayList<Vec2>();

		addVec2(result, config.mLeft, width, height, false, false);
		addVec2(result, config.mTop, height, width, true, false);
		addVec2(result, config.mRight, width, height, false, true);
		addVec2(result, config.mBottom, height, width, true, true);

		return result;
	}

	private static void addVec2(List<Vec2> result, List<Range> ranges, int lineVal,
			int deltaMaxVal, boolean isX, boolean isMax) {

		if (result != null && ranges != null) {

			for (Range range : ranges) {

				for (int i = range.start; i < range.end && i < deltaMaxVal - 2; i++) {

					if (i >= 0) {

						int realVal = isMax ? lineVal - 1 : 0;

						if (isX) {

							result.add(new Vec2(i + 1, realVal));

						} else {

							result.add(new Vec2(realVal, i + 1));
						}
					}
				}
			}
		}
	}

	/** 坐标 */
	static class Vec2 {
		int x, y;

		public Vec2() {
		}

		public Vec2(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "(" + x + ", " + y + ")";
		}
	}
}
