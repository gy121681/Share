package com.hentica.allmain;

import com.hentica.tools.FileHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 数据结构文件
 *
 * @author mili
 * @createTime 2016-7-1 下午12:30:03
 */
public class CopyDataModelFiles {

    public static void main(String[] args) {

        String srcPath = "../../doc/car_peccancy_data/src";
        String outPath = "../../client/android/datalib/src/main/java";

        // 从svn更新
        updateFromSVN(srcPath);
        System.out.println("svn更新完成");
        System.out.println();

        // 删除临时文件夹
        FileHelper.deletePath(outPath);
        // 复制文件并重命名
        FileHelper.copyDirToDir(srcPath, outPath);
        // 修改文件内容
        replacePackageNumberStr(outPath);

        System.out.println("复制完成");
    }

    /**
     * 从svn更新
     */
    private static void updateFromSVN(String srcPath) {

        try {
            Runtime run = Runtime.getRuntime();
            Process process = run.exec("svn up " + srcPath);

            // 将调用结果打印到控制台上
            InputStream in = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            reader.close();
            process.waitFor();

        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("未添加svn命令行，请手动更新");
        }
    }

    // 替换某文件夹下所有文件的大字数字类型
    private static void replacePackageNumberStr(String srcPath) {

        // 找到java源文件
        List<File> fileList = FileHelper.findFiles(srcPath, new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".java");
            }
        });

        // 替换文件内容
        if (fileList != null) {

            for (File file : fileList) {

                String oldSrcString = FileHelper.readString(file.getPath());
                String newSrcString = replaceNumberStr(oldSrcString);
                FileHelper.writeString(file.getPath(), newSrcString);
            }
        }
    }

    // 替换大写的数字类型
    private static String replaceNumberStr(String oldStr) {

        if (oldStr != null) {

            return FieldReplacer.create().withString(oldStr)
                    .replaceType("Long", "long")
                    .replaceType("Integer", "int")
                    .replaceType("Double", "double")
                    .replaceType("Float", "float")
                    .replaceType("Short", "short")
                    .finalString();
        }
        return "";
    }

    // 用于替换Java类型
    private static class FieldReplacer {

        // 创建一个实例
        public static FieldReplacer create() {
            return new FieldReplacer();
        }

        // 最终字符串
        private String mFinalString;

        // 初始字符串
        public FieldReplacer withString(String str) {

            mFinalString = str;
            return this;
        }

        // 替换类型
        public FieldReplacer replaceType(String oldType, String newType) {

            if (mFinalString != null) {
                {
                    String oldMatcher = String.format("\\s+%s\\s+", oldType);
                    String newStr = String.format(" %s ", newType);
                    mFinalString = mFinalString.replaceAll(oldMatcher, newStr);
                }
                {
                    String oldMatcher = String.format("\\(\\s*%s", oldType);
                    String newStr = String.format("(%s", newType);
                    mFinalString = mFinalString.replaceAll(oldMatcher, newStr);
                }
            }
            return this;
        }

        // 最终字符串
        public String finalString() {

            return mFinalString;
        }
    }
}
