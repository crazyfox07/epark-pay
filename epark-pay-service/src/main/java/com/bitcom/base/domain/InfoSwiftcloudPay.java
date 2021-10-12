package com.bitcom.base.domain;


public class InfoSwiftcloudPay {
    private Integer payId;
    private String status;
    private String resultCode;
    private String mchId;
    private String sign;
    private String tradeType;
    private Integer payResult;
    private String transactionId;
    private String outTransactionId;
    private String outTradeNo;
    private Integer totalFee;
    private String bankType;
    private String timeEnd;
    private String attach;

    public Integer getPayId() {
        return this.payId;
    }


    public void setPayId(Integer payId) {
        this.payId = payId;
    }


    public String getStatus() {
        return this.status;
    }


    public void setStatus(String status) {
        this.status = (status == null) ? null : status.trim();
    }


    public String getResultCode() {
        return this.resultCode;
    }


    public void setResultCode(String resultCode) {
        this.resultCode = (resultCode == null) ? null : resultCode.trim();
    }


    public String getMchId() {
        return this.mchId;
    }


    public void setMchId(String mchId) {
        this.mchId = (mchId == null) ? null : mchId.trim();
    }


    public String getSign() {
        return this.sign;
    }


    public void setSign(String sign) {
        this.sign = (sign == null) ? null : sign.trim();
    }


    public String getTradeType() {
        return this.tradeType;
    }


    public void setTradeType(String tradeType) {
        this.tradeType = (tradeType == null) ? null : tradeType.trim();
    }


    public Integer getPayResult() {
        return this.payResult;
    }


    public void setPayResult(Integer payResult) {
        this.payResult = payResult;
    }


    public String getTransactionId() {
        return this.transactionId;
    }


    public void setTransactionId(String transactionId) {
        this.transactionId = (transactionId == null) ? null : transactionId.trim();
    }


    public String getOutTransactionId() {
        return this.outTransactionId;
    }


    public void setOutTransactionId(String outTransactionId) {
        this.outTransactionId = (outTransactionId == null) ? null : outTransactionId.trim();
    }


    public String getOutTradeNo() {
        return this.outTradeNo;
    }


    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = (outTradeNo == null) ? null : outTradeNo.trim();
    }


    public Integer getTotalFee() {
        return this.totalFee;
    }


    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }


    public String getBankType() {
        return this.bankType;
    }


    public void setBankType(String bankType) {
        this.bankType = (bankType == null) ? null : bankType.trim();
    }


    public String getTimeEnd() {
        return this.timeEnd;
    }


    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }


    public String getAttach() {
        return this.attach;
    }


    public void setAttach(String attach) {
        this.attach = (attach == null) ? null : attach.trim();
    }
}



