package com.bitcom.protocol;


public class PayMessage {
    private String ver = "2.0";

    private String outTradeNo;
    private String bizName;
    private String payScheme;
    private String payType;
    private String payTime;
    private String totalAmount;
    private Object attach;

    public String getVer() {
        return this.ver;
    }


    public void setVer(String ver) {
        this.ver = ver;
    }


    public String getOutTradeNo() {
        return this.outTradeNo;
    }


    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }


    public String getBizName() {
        return this.bizName;
    }


    public void setBizName(String bizName) {
        this.bizName = bizName;
    }


    public String getPayType() {
        return this.payType;
    }


    public void setPayType(String payType) {
        this.payType = payType;
    }


    public String getTotalAmount() {
        return this.totalAmount;
    }


    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }


    public Object getAttach() {
        return this.attach;
    }


    public void setAttach(Object attach) {
        this.attach = attach;
    }


    public String getPayTime() {
        return this.payTime;
    }


    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }


    public String getPayScheme() {
        return this.payScheme;
    }


    public void setPayScheme(String payScheme) {
        this.payScheme = payScheme;
    }
}



