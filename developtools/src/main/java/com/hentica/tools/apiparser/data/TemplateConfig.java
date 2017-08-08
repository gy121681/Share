package com.hentica.tools.apiparser.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * api模板配置
 *
 * @author mili
 * @createTime 2016/10/12
 */
@SuppressWarnings("ALL")
public class TemplateConfig {

    /**
     * api配置信息，包含有一系列api
     */
    public static class ApiGroupConfig {

        /**
         * 包名
         */
        public String mPackageName = "com.hentica.app.util.request";

        /**
         * url方法名
         */
        public String mUrlGetterName = "ApplicationData.getInstance().getServerUrl()";

        /**
         * 所有api信息
         */
        public List<ApiParseResult.OneApiInfo> mApiInfos;

        /**
         * Post类名
         */
        public String mPostClassName = "Post";

        /**
         * 要上传的文件信息 key:action, value:文件参数
         */
        public Map<String, List<ApiWithFile>> mFileInfos;

        /**
         * 合并为一个参数的urls
         */
        public Set<String> mMergedApis;
    }

    /**
     * 文件信息
     */
    public static class ApiWithFile {

        /**
         * 数组名
         */
        public String mArrayName;

        /**
         * 说明
         */
        public String mDes;

        /**
         * 是否是数组
         */
        public boolean mIsArray;

        /**
         * 构造函数
         */
        public ApiWithFile() {
        }

        /**
         * 构造函数
         *
         * @param arrayName 上传时，文件数组名
         * @param des       说明
         * @param isArray   上传的文件是否是数组
         */
        public ApiWithFile(String arrayName, String des, boolean isArray) {

            this.mArrayName = arrayName;
            this.mDes = des;
            this.mIsArray = isArray;
        }

        /**
         * 创建一个对象
         */
        public static ApiWithFile create() {
            return new ApiWithFile();
        }

        /**
         * 上传时，文件数组名
         */
        public ApiWithFile paramName(String fileArrayName) {
            this.mArrayName = fileArrayName;
            return this;
        }

        /**
         * 说明
         */
        public ApiWithFile des(String des) {
            mDes = des;
            return this;
        }

        /**
         * 是否为数组
         */
        public ApiWithFile isArray(boolean isArray) {
            mIsArray = isArray;
            return this;
        }
    }
}
