package com.share.app.entity.response;

/**
 * Created by Snow on 2017/8/1.
 */

public class MsgResponse {

    /**
     * RSPCOD : 000000
     * RSPMSG : 修改成功
     */

    private String RSPCOD;
    private String RSPMSG;

    public String getRSPCOD() {
        return RSPCOD;
    }

    public void setRSPCOD(String RSPCOD) {
        this.RSPCOD = RSPCOD;
    }

    public String getRSPMSG() {
        return RSPMSG;
    }

    public void setRSPMSG(String RSPMSG) {
        this.RSPMSG = RSPMSG;
    }
}
