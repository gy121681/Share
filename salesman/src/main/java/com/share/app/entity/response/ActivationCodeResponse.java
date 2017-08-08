package com.share.app.entity.response;

/**
 * Created by Snow on 2017/7/25.
 */

public class ActivationCodeResponse {


    /**
     * unActivationCount : 未激活数量
     * activationedCount : 已激活数量
     */

    private String unActivationCount;
    private String activationedCount;

    public String getUnActivationCount() {
        return unActivationCount;
    }

    public void setUnActivationCount(String unActivationCount) {
        this.unActivationCount = unActivationCount;
    }

    public String getActivationedCount() {
        return activationedCount;
    }

    public void setActivationedCount(String activationedCount) {
        this.activationedCount = activationedCount;
    }
}
