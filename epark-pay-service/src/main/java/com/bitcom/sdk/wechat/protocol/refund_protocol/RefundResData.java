package com.bitcom.sdk.wechat.protocol.refund_protocol;


public class RefundResData {
    private String return_code = "";
    private String return_msg = "";


    private String appid = "";
    private String mch_id = "";
    private String sub_mch_id = "";
    private String device_info = "";
    private String nonce_str = "";
    private String sign = "";
    private String result_code = "";
    private String err_code = "";
    private String err_code_des = "";


    private String transaction_id = "";
    private String out_trade_no = "";
    private String out_refund_no = "";
    private String refund_id = "";
    private String refund_fee = "";
    private String coupon_refund_fee = "";


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


    public String getSub_mch_id() {
        return this.sub_mch_id;
    }


    public void setSub_mch_id(String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }


    public String getDevice_info() {
        return this.device_info;
    }


    public void setDevice_info(String device_info) {
        this.device_info = device_info;
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


    public String getTransaction_id() {
        return this.transaction_id;
    }


    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }


    public String getOut_trade_no() {
        return this.out_trade_no;
    }


    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }


    public String getOut_refund_no() {
        return this.out_refund_no;
    }


    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }


    public String getRefund_id() {
        return this.refund_id;
    }


    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }


    public String getRefund_fee() {
        return this.refund_fee;
    }


    public void setRefund_fee(String refund_fee) {
        this.refund_fee = refund_fee;
    }


    public String getCoupon_refund_fee() {
        return this.coupon_refund_fee;
    }


    public void setCoupon_refund_fee(String coupon_refund_fee) {
        this.coupon_refund_fee = coupon_refund_fee;
    }
}



