package com.hentica.tools.apiparser;

import com.hentica.tools.MapHelper;
import com.hentica.tools.apiparser.data.ApiParseResult;
import com.hentica.tools.apiparser.data.TemplateConfig;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.FileResourceLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成代码
 */
public class ParseToCode {

    /**
     * 解析出网络请求方法
     *
     * @param config api配置
     */
    public static String parseRequestMethodStr(TemplateConfig.ApiGroupConfig config) {

        mergeParams(config);

        String root = "developtools/res/templates/";
        String templatePath = "api_template.java";
        Template template = createTemplate(root, templatePath);
        //noinspection ConstantConditions
        template.binding("apiObjMap", MapHelper.objectToMap(config));

        return template.render();
    }

    /**
     * 合并指定api参数
     */
    private static void mergeParams(TemplateConfig.ApiGroupConfig config) {

        // 把某个api的所有参数合并为一个参数
        if (config.mMergedApis != null) {

            for (ApiParseResult.OneApiInfo apiInfo : config.mApiInfos) {

//                if (config.mMergedApis.contains(apiInfo.mActionPath)) {
                if (config.mMergedApis.contains(apiInfo.action)) {

                    List<ApiParseResult.ParamInfo> oldParams = apiInfo.dataParams;
//                    List<ParamInfo> oldParams = apiInfo.dataParam;
                    ApiParseResult.ParamInfo newParam = new ApiParseResult.ParamInfo();

                    if (oldParams != null && oldParams.size() > 0) {

                        StringBuilder builder = new StringBuilder();

                        builder.append("{");
                        for (int i = 0, size = oldParams.size(); i < size; i++) {

                            ApiParseResult.ParamInfo paramInfo = oldParams.get(i);
                            String format = "\"%s\":\"%s\"";
                            builder.append(String.format(format, paramInfo.mName, paramInfo.mDes));

                            if (i < size - 1) {

                                builder.append(',');
                            }
                        }
                        builder.append("}");
                        newParam.mDes = builder.toString();
                        newParam.mName = "DataParam";
                    }

//                    apiInfo.mParams = new ArrayList<>();
//                    apiInfo.mParams.add(newParam);
//                    apiInfo.dataParam = new ArrayList<>();
                    apiInfo.dataParams.add(newParam);
                }
            }
        }
    }

    // 创建输出模板
    private static Template createTemplate(String rootDir, String tempaltePath) {

        try {
            FileResourceLoader resourceLoader = new FileResourceLoader(rootDir, "utf-8");
            Configuration cfg = Configuration.defaultConfiguration();
            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
            return gt.getTemplate(tempaltePath);

        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }
}
