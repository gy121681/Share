package com.shareshenghuo.app.user.network.response;

/**
 * Created by Snow on 2017/7/21.
 */

public class BankInfoResponse{

    /**
     * reason : 查询成功
     * result : {"bank":"工商银行","type":"E时代卡","nature":"借记卡","kefu":"95588","logo":"http://apiserver.qiniudn.com/gongshang.png","info":"四川省-成都"}
     * error_code : 0
     */

    public String reason;
    public ResultBean result;
    public int error_code;


    public static class ResultBean {
        /**
         * bank : 工商银行
         * type : E时代卡
         * nature : 借记卡
         * kefu : 95588
         * logo : http://apiserver.qiniudn.com/gongshang.png
         * info : 四川省-成都
         */

        public String bank;
        public String type;
        public String nature;
        public String kefu;
        public String logo;
        public String info;

    }
}
