package com.hentica.tools.apiparser.data;

import java.util.ArrayList;
import java.util.List;

/** api解析结果 */
@SuppressWarnings("ALL")
public class ApiParseResult {

	/** 参数类型 */
	public enum ParamType {

		/** 对象 */
		kObject,

		/** 数组 */
		kArray,

		/** 字符串 */
		kString
	}

	/** 参数信息 */
	public static class ParamInfo {

		/** 参数类型 */
		public ParamType mType;

		/** 参数名称 */
		public String mName = "";

		/** 参数说明 */
		public String mDes = "";

		/** 构造函数 */
		public ParamInfo() {
		}

		/** 构造函数 */
		public ParamInfo(String name, String des, ParamType type) {

			mName = name;
			mDes = des;
			mType = type;
		}
	}

	/** 一条api信息 */
	public static class OneApiInfo {

		/** api名称/说明 */
//		public String mDes = "";
		public String dataParamDesc = "";
		/** api字段 */
//		public String mFieldName = "";
		public String name = "";
		/** api路径 */
//		public String mActionPath = "";
		public String action = "";

		/** 参数列表 */
		public List<ParamInfo> dataParams = new ArrayList<>();
		public String dataParam;

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
//			builder.append(mFieldName).append('\n');
			builder.append(name).append('\n');

			return builder.toString();
		}
	}
}
