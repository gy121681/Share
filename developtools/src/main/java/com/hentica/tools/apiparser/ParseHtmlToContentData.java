package com.hentica.tools.apiparser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hentica.tools.apiparser.data.ApiParseResult;
import com.hentica.tools.apiparser.data.ApiParseResult.OneApiInfo;
import com.hentica.tools.apiparser.data.ApiParseResult.ParamInfo;
import com.hentica.tools.apiparser.data.ApiParseResult.ParamType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 把网页解析为ApiContent需要的字符串
 */
public class ParseHtmlToContentData {

    public static void main(String[] args) {

        String url = "http://192.167.1.12/dentistry/api/";

        ParseHtmlToContentData paser = new ParseHtmlToContentData(url);
        List<OneApiInfo> result = paser.parseToContent();
        System.out.println(result);
        // System.out.println("结束");
    }

    /**
     * 文档解析配置
     */
    public static class HtmlParseConfig {

        /**
         * 要解析的网址
         */
        public String mUrl;

        /**
         * table选择器
         */
        public String mTableSelector = ".parms table";

        /**
         * 标题在表格的第几列
         */
        public int mTitleIndexInTable = 0;
    }

    // 文档解析配置
    private HtmlParseConfig mParseConfig;

    public ParseHtmlToContentData(String url) {

        mParseConfig = new HtmlParseConfig();
        mParseConfig.mUrl = url;
    }

    public ParseHtmlToContentData(HtmlParseConfig config) {

        if (config == null){
            config = new HtmlParseConfig();
        }
        mParseConfig = config;
    }

    /**
     * 直接解析为api
     */
    public List<OneApiInfo> parseToContent() {

        List<ApiParseResult.OneApiInfo> result = new ArrayList<>();
        try {
            Document root = Jsoup.connect(mParseConfig.mUrl).ignoreContentType(true).get();
            Elements tables = root.select(mParseConfig.mTableSelector);
            for (Element table : tables) {

                result.addAll(parseOneTable(table));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return result;
    }

    public List<OneApiInfo> parseToApiInfoList(){
        List<ApiParseResult.OneApiInfo> result = new ArrayList<>();
        try {
            Connection.Response res = Jsoup.connect(mParseConfig.mUrl).ignoreContentType(true).execute();
            String value = res.body();
            JSONObject json = new JSONObject(value);
            json = new JSONObject(json.get("data").toString());
            String apiList = json.get("apiList").toString();
            result.addAll(wrapapiInfo(parseOneApiInfo(apiList)));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /** 解析分类列表 */
    public static List<OneApiInfo> parseOneApiInfo(String json) {

        Type listType = new TypeToken<List<OneApiInfo>>() {
        }.getType();

        try {
            Gson gson = new Gson();
            return gson.fromJson(json, listType);

        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    public static List<OneApiInfo> wrapapiInfo(List<OneApiInfo> infos){
        if(infos == null || infos.isEmpty()){
            return infos;
        }
        for(OneApiInfo api : infos){
            if (api.dataParam == null || api.dataParams.isEmpty()){
                continue;
            }
            try {
                JSONObject jsonObject = new JSONObject(api.dataParam);
                Iterator<String> it = jsonObject.keys();
                List<ParamInfo> paramInfos = new ArrayList<>();
                ParamInfo param = null;
                while (it.hasNext()){
                    param = new ParamInfo();
                    param.mName = it.next();
                    param.mDes = jsonObject.getString(param.mName);
                    paramInfos.add(param);
                }
                api.dataParams = paramInfos;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return infos;
    }

    /**
     * 解析一个表格为api
     */
    private List<OneApiInfo> parseOneTable(Element table) {

        List<ApiParseResult.OneApiInfo> result = new ArrayList<>();
        try {
            Elements allTrs = table.select("tr");

            String apiTitle = this.parseApiName(allTrs);
            String apiPath = this.parseApiPath(allTrs);
            if (apiPath.endsWith("publish/caseedit")) {

                System.out.println("");
            }
            List<ParamInfo> allParams = this.parseParams(allTrs);

            OneApiInfo apiInfo = new ApiParseResult.OneApiInfo();
//            apiInfo.mDes = apiTitle;
//            apiInfo.dataParamDesc = apiTitle;
//            apiInfo.mActionPath = apiPath;
//            apiInfo.mFieldName = captureName(apiInfo.mActionPath.replaceAll("/", ""));
//            apiInfo.mParams = allParams;

            result.add(apiInfo);

        } catch (Exception e) {

            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析出api名称
     */
    private String parseApiName(Elements allTrs) {

        // 第一个 tr
        // 第二个 td， 接口名
        // 第三个 td， api路径，去掉首个/，去空格
        try {
            if (allTrs.size() > 0) {

                Element firstTr = allTrs.get(0);
                Elements allTds = firstTr.select("td");
                if (allTds.size() >= 2) {
                    return allTds.get(mParseConfig.mTitleIndexInTable).text();
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return "______API名异常______";
    }

    /**
     * 解析出api路径
     */
    private String parseApiPath(Elements allTrs) {

        // 第一个 tr
        // 第二个 td， 接口名
        // 第三个 td， api路径，去掉首个/，去空格
        try {

            if (allTrs.size() > 0) {

                Element firstTr = allTrs.get(0);
                Elements allTds = firstTr.select("td");
                if (allTds.size() >= 3) {
                    String path = allTds.get(2).text().trim();

                    if (path.startsWith("/")) {

                        path = path.substring(1);
                    }
                    return path;
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return "______API路径异常______";
    }

    /**
     * 解析出所有参数
     */
    private List<ParamInfo> parseParams(Elements allTrs) {

        // 最后一个 tr
        // 第一个 td，取得json，若不是以"参数"开头，说明没有参数
        // 每个key，参数名，去空格；每个value，参数说明

        // 第二个td为DataParam

        List<ParamInfo> result = new ArrayList<>();

        // 参数配置单元格
        Element paramTr = null;
        // paramTr = allTrs.get(allTrs.size() - 1);

        // 寻找带参数的行
        for (int i = 1, size = allTrs.size(); i < size; i++) {

            // 每一行的单元格
            Element currTr = allTrs.get(i);
            Elements lineTds = currTr.select("td");
            if (lineTds.size() >= 2) {

                // 第2个单元格
                String text = lineTds.get(1).text().trim().toUpperCase();

                if ("DataParam".toUpperCase().equals(text)) {

                    paramTr = currTr;
                    break;
                }
            }
        }

        if (paramTr != null) {

            String paramString = paramTr.select("td").first().text().trim();

            // 截取参数字符串
            // {'Username':'登陆账号','Password':'登陆密码'}
            // 第一个 { 与最后一个 } 之间
            int startIdx = paramString.indexOf('{');
            int endIdx = paramString.lastIndexOf('}');

            // 都存在，则说明有参数
            if (startIdx >= 0 && endIdx > 0) {

                String jsonString = paramString.substring(startIdx, endIdx + 1);
                // 去除中文符号
                jsonString = jsonString.replace('‘', '\'').replace('’', '\'').replace('“', '\"').replace('”', '\"');

                try {
                    JSONObject object = new JSONObject(jsonString);
                    Iterator<String> iterator = object.keys();

                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        Object value = object.get(key);

                        // 计算value类型
                        ParamType type = ParamType.kString;
                        if (value instanceof JSONObject) {

                            type = ParamType.kObject;

                        } else if (value instanceof JSONArray) {

                            type = ParamType.kArray;
                        }
                        // value转为字符串
                        String valueString = value.toString();

                        // 一个参数
                        ParamInfo oneParam = new ParamInfo(key, valueString, type);
                        result.add(oneParam);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 首字母大写
     */
    private static String captureName(String name) {

        if (name != null && !name.isEmpty()) {

            char[] cs = name.toCharArray();

            char first = cs[0];
            if (first >= 'a' && first <= 'z') {

                cs[0] -= 32;
            }
            return String.valueOf(cs);
        }
        return name;
    }
}
