package com.bitcom.base.domain;

import java.util.Date;


public class InfoRefundFlow {
    private String nid;
    private String tradeNo;
    private String outRefundNo;
    private String refundId;
    private String refundFee;
    private Date refundTime;

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


    public String getOutRefundNo() {
        return this.outRefundNo;
    }


    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = (outRefundNo == null) ? null : outRefundNo.trim();
    }


    public String getRefundId() {
        return this.refundId;
    }


    public void setRefundId(String refundId) {
        this.refundId = (refundId == null) ? null : refundId.trim();
    }


    public String getRefundFee() {
        return this.refundFee;
    }


    public void setRefundFee(String refundFee) {
        this.refundFee = (refundFee == null) ? null : refundFee.trim();
    }


    public Date getRefundTime() {
        return this.refundTime;
    }


    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }
}



