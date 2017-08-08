
package com.hentica.tools;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageHelper {

    /** 取得图片像素大小 */
    public Size getImageInfo(String imgPath) {

        Size size = new Size();

        // 取图片宽高
        File file = new File(imgPath);
        BufferedImage bi = null;
        try {

            bi = ImageIO.read(file);
            size.width = bi.getWidth();
            size.height = bi.getHeight();

        } catch (Exception e) {

            // e.printStackTrace();
        }
        return size;
    }

    /** 图片像素大小 */
    public class Size {

        int width; // 宽

        int height; // 高
    }
}
