package com.share.app.entity.response;

/**
 * Created by Snow on 2017/7/24.
 */

public class Constans {

    public static class Common{
        public static final String C_USERMOBILE = "usermobile";
        public static final String C_USERPWD = "userpwd";
        public static final String C_RSPCOD_SUCCESS = "000000";
    }

    public static class Login{
        public static final String PERSONPIC = "PERSONPIC";
        public static final String USERRP = "userp";
        public static final String MOBILE = "mobile";
        public static final String USERID = "id";
        public static final String PASSWORD = "pass";
        public static final String MERCNUM = "MercNum";
        public static final String MERCNAM = "MercNam";
        public static final String TYPE = "type";
        public static final String LEVEL = "level";
        public static final String PHOTO = "photo";
        public static final String PAYPASSWORD = "payPassword";
        public static final String CERTIFICATIONSTEP = "certificationStep";
        public static final String CERTIFICATIONSTATUS = "certificationStatus";
        public static final String REALNAME = "realName";
        public static final String PERSONNO = "personNo";
        public static final String AGENTTYPE = "agentType";
        public static final String AGENTAREALIST = "agentAreaList";
        public static final String MERSTS = "MERSTS";
        public static final String STS = "STS";
    }

    public static class Role{
        /** 业务员 */
        public static final String NORMAl = "0";
        /** 代理商 */
        public static final String AGENT = "1";
        /** 区域经理 */
        public static final String AREA_MANAGER = "2";

    }

    public static class MemberLevel{
        public static String LEVEL_NORMAL = "0";
        public static String LEVEL_VIP = "1";
    }

    /**
     * 代理类型
     */
    public static class AgentType {
        public static final String TYPE_PROVINCE  = "1";
        public static final String TYPE_CITY  = "2";
        public static final String TYPE_AREA  = "3";
    }

    /**
     * 实名认证状态
     */
    public static class AuthenticationStatus{
        public static final String STATUS_UNAUTHENTICATION = "0";
        public static final String STATUS_FAILED = "1";
        public static final String STATUS_SUCCESS = "2";
    }

    /**
     * 实名认证步骤
     */
    public static class AuthenticationStep{
        /** 未开始 */
        public static final String STEP_UNSTART = "0";
        /** 已验证身份证信息 */
        public static final String STEP_AUTHENTICATIONED = "1";
        /** 已设置支付密码 */
        public static final String STEP_SET_PAY_PASSWORD = "2";
        /** 认证完成 */
        public static final String STEP_COMPLETE = "3";
    }

}

