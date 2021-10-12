package com.bitcom.sdk.wechat.protocol.reverse_protocol;


public class ReverseResData {
    private String return_code = "";
    private String return_msg = "";


    private String result_code = "";
    private String err_code = "";
    private String err_code_des = "";
    private String sign = "";
    private String appid = "";
    private String mch_id = "";
    private String nonce_str = "";

    private String recall = "";


    public String getReturn_code() {
        return this.return_code;
    }


    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }


    public String getReturn_msg() {
        return this.return_msg;
    }


    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }


    public String getResult_code() {
        return this.result_code;
    }


    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }


    public String getErr_code() {
        return this.err_code;
    }


    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }


    public String getErr_code_des() {
        return this.err_code_des;
    }


    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }


    public String getAppid() {
        return this.appid;
    }


    public void setAppid(String appid) {
        this.appid = appid;
    }


    public String getMch_id() {
        return this.mch_id;
    }


    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }


    public String getNonce_str() {
        return this.nonce_str;
    }


    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }


    public String getSign() {
        return this.sign;
    }


    public void setSign(String sign) {
        this.sign = sign;
    }


    public String getRecall() {
        return this.recall;
    }


    public void setRecall(String recall) {
        this.recall = recall;
    }
}



