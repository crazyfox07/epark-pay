package com.bitcom.base.domain;


public class InfoUmsPay {
    private Integer payId;
    private String merOrderId;
    private String notifyId;
    private String mid;
    private String tid;
    private String billFunds;
    private String billFundsDesc;
    private Integer totalAmount;
    private Integer invoiceAmount;
    private String payTime;
    private String settleDate;
    private String buyerId;
    private String buyerName;
    private String status;
    private String targetOrderId;
    private String targetSys;
    private String sign;
    private String attach;

    public Integer getPayId() {
        return this.payId;
    }


    public void setPayId(Integer payId) {
        this.payId = payId;
    }


    public String getMerOrderId() {
        return this.merOrderId;
    }


    public void setMerOrderId(String merOrderId) {
        this.merOrderId = (merOrderId == null) ? null : merOrderId.trim();
    }


    public String getNotifyId() {
        return this.notifyId;
    }


    public void setNotifyId(String notifyId) {
        this.notifyId = (notifyId == null) ? null : notifyId.trim();
    }


    public String getMid() {
        return this.mid;
    }


    public void setMid(String mid) {
        this.mid = (mid == null) ? null : mid.trim();
    }


    public String getTid() {
        return this.tid;
    }


    public void setTid(String tid) {
        this.tid = (tid == null) ? null : tid.trim();
    }


    public String getBillFunds() {
        return this.billFunds;
    }


    public void setBillFunds(String billFunds) {
        this.billFunds = (billFunds == null) ? null : billFunds.trim();
    }


    public String getBillFundsDesc() {
        return this.billFundsDesc;
    }


    public void setBillFundsDesc(String billFundsDesc) {
        this.billFundsDesc = (billFundsDesc == null) ? null : billFundsDesc.trim();
    }


    public Integer getTotalAmount() {
        return this.totalAmount;
    }


    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }


    public Integer getInvoiceAmount() {
        return this.invoiceAmount;
    }


    public void setInvoiceAmount(Integer invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }


    public String getPayTime() {
        return this.payTime;
    }


    public void setPayTime(String payTime) {
        this.payTime = (payTime == null) ? null : payTime.trim();
    }


    public String getSettleDate() {
        return this.settleDate;
    }


    public void setSettleDate(String settleDate) {
        this.settleDate = (settleDate == null) ? null : settleDate.trim();
    }


    public String getBuyerId() {
        return this.buyerId;
    }


    public void setBuyerId(String buyerId) {
        this.buyerId = (buyerId == null) ? null : buyerId.trim();
    }


    public String getBuyerName() {
        return this.buyerName;
    }


    public void setBuyerName(String buyerName) {
        this.buyerName = (buyerName == null) ? null : buyerName.trim();
    }


    public String getStatus() {
        return this.status;
    }


    public void setStatus(String status) {
        this.status = (status == null) ? null : status.trim();
    }


    public String getTargetOrderId() {
        return this.targetOrderId;
    }


    public void setTargetOrderId(String targetOrderId) {
        this.targetOrderId = (targetOrderId == null) ? null : targetOrderId.trim();
    }


    public String getTargetSys() {
        return this.targetSys;
    }


    public void setTargetSys(String targetSys) {
        this.targetSys = (targetSys == null) ? null : targetSys.trim();
    }


    public String getSign() {
        return this.sign;
    }


    public void setSign(String sign) {
        this.sign = (sign == null) ? null : sign.trim();
    }


    public String getAttach() {
        return this.attach;
    }


    public void setAttach(String attach) {
        this.attach = (attach == null) ? null : attach.trim();
    }
}



