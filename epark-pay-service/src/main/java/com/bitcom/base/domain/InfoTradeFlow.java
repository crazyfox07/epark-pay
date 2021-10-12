package com.bitcom.base.domain;

import java.util.Date;


public class InfoTradeFlow {
    private String nid;
    private String tradeNo;
    private String outTradeNo;
    private String merId;
    private String termimalId;
    private String payScheme;
    private String payType;
    private String payApiChannel;
    private String totalAmount;
    private Date gmtPayment;
    private String buyerId;

    public String getNid() {
        return this.nid;
    }


    public void setNid(String nid) {
        this.nid = (nid == null) ? null : nid.trim();
    }


    public String getTradeNo() {
        return this.tradeNo;
    }


    public void setTradeNo(String tradeNo) {
        this.tradeNo = (tradeNo == null) ? null : tradeNo.trim();
    }


    public String getOutTradeNo() {
        return this.outTradeNo;
    }


    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = (outTradeNo == null) ? null : outTradeNo.trim();
    }


    public String getMerId() {
        return this.merId;
    }


    public void setMerId(String merId) {
        this.merId = (merId == null) ? null : merId.trim();
    }


    public String getTermimalId() {
        return this.termimalId;
    }


    public void setTermimalId(String termimalId) {
        this.termimalId = (termimalId == null) ? null : termimalId.trim();
    }


    public String getPayScheme() {
        return this.payScheme;
    }


    public void setPayScheme(String payScheme) {
        this.payScheme = (payScheme == null) ? null : payScheme.trim();
    }


    public String getPayType() {
        return this.payType;
    }


    public void setPayType(String payType) {
        this.payType = (payType == null) ? null : payType.trim();
    }


    public String getPayApiChannel() {
        return this.payApiChannel;
    }


    public void setPayApiChannel(String payApiChannel) {
        this.payApiChannel = (payApiChannel == null) ? null : payApiChannel.trim();
    }


    public String getTotalAmount() {
        return this.totalAmount;
    }


    public void setTotalAmount(String totalAmount) {
        this.totalAmount = (totalAmount == null) ? null : totalAmount.trim();
    }


    public Date getGmtPayment() {
        return this.gmtPayment;
    }


    public void setGmtPayment(Date gmtPayment) {
        this.gmtPayment = gmtPayment;
    }


    public String getBuyerId() {
        return this.buyerId;
    }


    public void setBuyerId(String buyerId) {
        this.buyerId = (buyerId == null) ? null : buyerId.trim();
    }
}



