package com.hentica.tools.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 * 图片工具
 */
public class ImageUtil {

    public static void main(String[] args) {

        String img = "E:/Outsourcing/weather/program/client/android/app/src/main/res/drawable-xhdpi/wea_background_thunder_lightning.png";
        String out = "E:/Outsourcing/weather/program/client/android/app/src/main/res/drawable-xhdpi/wea_thundersnow_8__out.png";
        rewriteImage(img, out);

        System.out.print("xxxxxxxxxxxxxx");
    }

    /**
     * 重新读写图片，起到压缩的作用
     *
     * @param img 图片源路径
     * @param out 输出路径
     */
    public static void rewriteImage(String img, String out) {

        try {
            InputStream in = new FileInputStream(img);
            BufferedImage oldImg = ImageIO.read(in);
            in.close();

            // 复制图片
            BufferedImage newImg = new BufferedImage(oldImg.getWidth(), oldImg.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
            newImg.getGraphics().drawImage(oldImg, 0, 0, oldImg.getWidth(), oldImg.getHeight(), null);

            Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("png");
            ImageWriter writer = it.next();
            File f = new File(out);
            if (f.getParentFile() != null && !f.getParentFile().exists()) {

                //noinspection ResultOfMethodCallIgnored
                f.getParentFile().mkdirs();
            }
            ImageOutputStream ios = ImageIO.createImageOutputStream(f);
            writer.setOutput(ios);
            writer.write(newImg);
            oldImg.flush();
            ios.flush();
            ios.close();

            System.out.println("重写成功: " + img + ", imgsize: " + oldImg.getWidth() + ", " + oldImg.getHeight());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
