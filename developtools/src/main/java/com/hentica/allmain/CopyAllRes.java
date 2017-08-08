package com.hentica.allmain;

import com.hentica.tools.CopyRes;
import com.hentica.tools.CreateAndroidSelector;
import com.hentica.tools.Delete9PngFiles;
import com.hentica.tools.image.NinePngConfigPaser.NinePngInfo;
import com.hentica.tools.image.NinePngUtil;

import java.util.List;

/**
 * 生成所有资源
 *
 * @author Limi
 * @createTime 2016-2-26 下午2:40:18
 */
public class CopyAllRes {

    public static void main(String[] args) {

//         复制文件
//        copyFile("./developtools/res/res_config/copy_customer_res_config.txt");
//        copyFile("./developtools/res/res_config/copy_business_res_config.txt");
        copyFile("./developtools/res/res_config/copy_salesman_res_config.txt");

        // 生成9png
//        create9Png("./developtools/res/res_config/nine_png_config.txt");

        // 生成selector文件
//        createSelector("./developtools/res/res_config/selector_config.txt");

        // 删除多余的9png文件
//        String copyConfig = "./developtools/res/res_config/copy_res_config.txt";
//        String ninePngConfig = "./developtools/res/res_config/nine_png_config.txt";
//        delete9Png("./developtools/temp_out/drawable-xhdpi/", "./developtools/temp_out/res_9_png/");

        System.out.println("所有资源生成完成");
    }

    // 1.把直接可用的图片复制到res目录下
    private static void copyFile(String configPath) {
        CopyRes.copyRes(configPath);
        System.out.println();
    }

    // 2.生成需要的9png到res目录下
    private static void create9Png(String configPath) {
        List<NinePngInfo> result = NinePngUtil.parseNinePngInfo(configPath);
        //noinspection Convert2streamapi
        for (NinePngInfo ninePngInfo : result) {

            NinePngUtil.create9Png(ninePngInfo);
        }
        System.out.println();
    }

    // 3.生成必要的selector文件
    private static void createSelector(String configPath) {
        CreateAndroidSelector.createSelectorFiles(configPath);
        System.out.println();
    }

    // 4.删除多余的9png文件
    private static void delete9Png(String copyDir, String ninePngDir) {

        Delete9PngFiles.delete9Png(copyDir, ninePngDir);
        System.out.println();
    }
}
