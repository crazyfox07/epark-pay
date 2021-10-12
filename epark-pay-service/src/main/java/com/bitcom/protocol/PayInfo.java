package com.bitcom.protocol;


public class PayInfo {
    private String payScheme;
    private String payType;
    private String bizName;

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


    public String getPayScheme() {
        return this.payScheme;
    }


    public void setPayScheme(String payScheme) {
        this.payScheme = payScheme;
    }
}



