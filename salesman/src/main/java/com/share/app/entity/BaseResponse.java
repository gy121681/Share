package com.share.app.entity;

/**
 * Created by Snow on 2017/7/24.
 */

public class BaseResponse {

    /**
     * request_id : 1500522792728
     * result_code : 0
     * result_desc : 请求成功
     * result_count : 0
     * timestamp : 2017-07-20 11:53:13
     */

    private String request_id;
    private int result_code;
    private String result_desc;
    private int result_count;
    private String timestamp;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public String getResult_desc() {
        return result_desc;
    }

    public void setResult_desc(String result_desc) {
        this.result_desc = result_desc;
    }

    public int getResult_count() {
        return result_count;
    }

    public void setResult_count(int result_count) {
        this.result_count = result_count;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
