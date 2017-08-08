package com.hentica.allmain;

import com.hentica.tools.FileHelper;
import com.hentica.tools.apiparser.ParseHtmlToContentData;
import com.hentica.tools.apiparser.ParseToCode;
import com.hentica.tools.apiparser.data.ApiParseResult.OneApiInfo;
import com.hentica.tools.apiparser.data.TemplateConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 解析出所有的网络api
 */
public class APITool {

    public static void main(String[] args) {

        // 文档解析
        ParseHtmlToContentData.HtmlParseConfig parseConfig = new ParseHtmlToContentData.HtmlParseConfig();
        parseConfig.mUrl = "http://192.167.1.12:8080/car_peccancy/f/apiIndex";
        parseConfig.mTableSelector = "#param_1 .parms table";
        parseConfig.mTitleIndexInTable = 0;
        ParseHtmlToContentData paser = new ParseHtmlToContentData(parseConfig);
//        List<OneApiInfo> infos = paser.parseToContent();
        List<OneApiInfo> infos = paser.parseToApiInfoList();

        // 模板配置
        TemplateConfig.ApiGroupConfig config = new TemplateConfig.ApiGroupConfig();
        config.mApiInfos = infos;
        config.mPackageName = "com.hentica.app.util.request";
        config.mUrlGetterName = "ApplicationData.getInstance().getServerUrl()";
        config.mPostClassName = "AppPostWraper";
        config.mFileInfos = createPostWithFile();
        config.mMergedApis = createMergeApi();

        // 代码生成
        String outPath = "developtools/temp_out/com.hentica.app.util.request/RequestBase.java";
        String requestCloudMethodStr = ParseToCode.parseRequestMethodStr(config);
        FileHelper.writeString(outPath, requestCloudMethodStr);
        System.out.println(requestCloudMethodStr);
        System.out.print("文件已生成到 " + outPath);
    }

    /**
     * 定义带文件的post请求
     */
    private static Map<String, List<TemplateConfig.ApiWithFile>> createPostWithFile() {

        // 带文件的post请求
        Map<String, List<TemplateConfig.ApiWithFile>> allUpFileInfo = new HashMap<>();

        // 上传文件，示例
//        {
//            List<TemplateConfig.ApiWithFile> upFileConfig = new ArrayList<>();
//            upFileConfig.add(TemplateConfig.ApiWithFile.create().paramName("image").des("图片").isArray(false));
//            upFileConfig.add(TemplateConfig.ApiWithFile.create().paramName("audios").des("声音").isArray(true));
//            upFileConfig.add(TemplateConfig.ApiWithFile.create().paramName("videos").des("声音").isArray(true));
//            allUpFileInfo.put("driveLicense/listDriveLicensePeccancy", upFileConfig);
//        }

        return allUpFileInfo;
    }

    /**
     * 创建需要合并参数的api，若参数不是纯粹的字符串，而是有复合结构，则需要合并参数
     */
    private static Set<String> createMergeApi() {

        Set<String> result = new HashSet<>();

//		 如: result.add("user/uploadhealthinfo");
        result.add("user/login"); // 描述：【用户模块】用户登录
        result.add("vehicle/editVehicle"); // 描述：【车辆模块】编辑车辆
        result.add("driveLicense/editDriveLicense"); // 描述：【驾驶证模块】编辑驾驶证
        result.add("userSchedule/editSchedule");   //描述：【日程模块】编辑日程
        result.add("message/readMessage");   //描述：【通知模块】阅读通知
        result.add("message/deleteMessage");   //描述：【通知模块】删除通知
        return result;
    }
}
