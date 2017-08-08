package com.hentica.tools;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * 删除9png源文件
 */
public class Delete9PngFiles {

    /**
     * 删除多余的9png文件，删除目标文件夹下与9png同名的文件
     *
     * @param copyDir    文件夹，表示复制目标
     * @param ninePngDir 9png输出文件夹
     */
    public static void delete9Png(String copyDir, String ninePngDir) {

        // 所有9png文件名转为普通文件名
        Collection<String> ninePngNames = findAll9PngName(ninePngDir);
        Collection<String> normalNames = convertAll9PngToNormalNames(ninePngNames);

        // 在目标文件夹下删除有普通文件名的文件
        for (String normalName : normalNames) {

            File willDeleteFile = new File(copyDir + "/" + normalName);
            if (willDeleteFile.exists()) {

                System.out.println("删除9png源文件: " + willDeleteFile.getAbsolutePath());
                //noinspection ResultOfMethodCallIgnored
                willDeleteFile.delete();
            } else {
                System.err.println("9png源文件不存在: " + willDeleteFile.getAbsolutePath());
            }
        }
    }

    // 取得所有9png文件名，不包括路径
    private static Collection<String> findAll9PngName(String ninePngDir) {

        //noinspection Convert2Lambda
        Collection<File> files = FileHelper.findFiles(ninePngDir, new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".9.png");
            }
        });

        Collection<String> result = new HashSet<>();
        if (files != null) {

            //noinspection Convert2streamapi
            for (File file : files) {

                result.add(file.getName());
            }
        }
        return result;
    }

    // 把一系列9png文件名转换为普通文件名
    private static Collection<String> convertAll9PngToNormalNames(Collection<String> ninePngNames) {

        Collection<String> result = new ArrayList<>();
        //noinspection Convert2streamapi
        for (String ninePngName : ninePngNames) {

            result.add(convert9PngToNormalName(ninePngName));
        }
        return result;
    }

    // 把9png文件名转换为普通文件名
    private static String convert9PngToNormalName(String ninePngName) {

        String endSuffix = ".9.png";
        if (ninePngName.endsWith(endSuffix)) {

            // 普通文件名
            return ninePngName.substring(0, ninePngName.length() - endSuffix.length()) + ".png";
        }
        return null;
    }
}
