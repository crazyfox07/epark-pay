package com.bitcom.sdk.alipay.vo;

import com.alibaba.fastjson.annotation.JSONField;


public class AlipayNotifyParam {
    @JSONField(name = "notify_time")
    private String notifyTime;
    @JSONField(name = "notify_type")
    private String notifyType;
    @JSONField(name = "notify_id")
    private String notifyId;
    @JSONField(name = "charset")
    private String charset;
    @JSONField(name = "version")
    private String version;
    @JSONField(name = "sign_type")
    private String signType;
    @JSONField(name = "sign")
    private String sign;
    @JSONField(name = "app_id")
    private String appId;
    @JSONField(name = "auth_app_id")
    private String authAppId;
    @JSONField(name = "trade_no")
    private String tradeNo;
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    @JSONField(name = "out_biz_no")
    private String outBizNo;
    @JSONField(name = "buyer_id")
    private String buyerId;
    @JSONField(name = "seller_id")
    private String sellerId;
    @JSONField(name = "subject")
    private String subject;
    @JSONField(name = "body")
    private String body;
    @JSONField(name = "trade_status")
    private String tradeStatus;
    @JSONField(name = "total_amount")
    private String totalAmount;
    @JSONField(name = "receipt_amount")
    private String receiptAmount;
    @JSONField(name = "invoice_amount")
    private String invoiceAmount;
    @JSONField(name = "buyer_pay_amount")
    private String buyerPayAmount;
    @JSONField(name = "point_amount")
    private String pointAmount;
    @JSONField(name = "refund_fee")
    private String refundFee;
    @JSONField(name = "fund_bill_list")
    private String fundBillList;
    @JSONField(name = "buyer_logon_id")
    private String buyerLogonId;
    @JSONField(name = "passback_params")
    private String passbackParams;
    @JSONField(name = "gmt_create")
    private String gmtCreate;
    @JSONField(name = "gmt_payment")
    private String gmtPayment;
    @JSONField(name = "gmt_refund")
    private String gmtRefund;
    @JSONField(name = "gmt_close")
    private String gmtClose;

    public String getNotifyTime() {
        return this.notifyTime;
    }


    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }


    public String getNotifyType() {
        return this.notifyType;
    }


    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }


    public String getNotifyId() {
        return this.notifyId;
    }


    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }


    public String getCharset() {
        return this.charset;
    }


    public void setCharset(String charset) {
        this.charset = charset;
    }


    public String getVersion() {
        return this.version;
    }


    public void setVersion(String version) {
        this.version = version;
    }


    public String getSignType() {
        return this.signType;
    }


    public void setSignType(String signType) {
        this.signType = signType;
    }


    public String getSign() {
        return this.sign;
    }


    public void setSign(String sign) {
        this.sign = sign;
    }


    public String getAppId() {
        return this.appId;
    }


    public void setAppId(String appId) {
        this.appId = appId;
    }


    public String getAuthAppId() {
        return this.authAppId;
    }


    public void setAuthAppId(String authAppId) {
        this.authAppId = authAppId;
    }


    public String getTradeNo() {
        return this.tradeNo;
    }


    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }


    public String getOutTradeNo() {
        return this.outTradeNo;
    }


    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }


    public String getOutBizNo() {
        return this.outBizNo;
    }


    public void setOutBizNo(String outBizNo) {
        this.outBizNo = outBizNo;
    }


    public String getBuyerId() {
        return this.buyerId;
    }


    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }


    public String getSellerId() {
        return this.sellerId;
    }


    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }


    public String getSubject() {
        return this.subject;
    }


    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getBody() {
        return this.body;
    }


    public void setBody(String body) {
        this.body = body;
    }


    public String getTradeStatus() {
        return this.tradeStatus;
    }


    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }


    public String getTotalAmount() {
        return this.totalAmount;
    }


    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }


    public String getReceiptAmount() {
        return this.receiptAmount;
    }


    public void setReceiptAmount(String receiptAmount) {
        this.receiptAmount = receiptAmount;
    }


    public String getInvoiceAmount() {
        return this.invoiceAmount;
    }


    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }


    public String getBuyerPayAmount() {
        return this.buyerPayAmount;
    }


    public void setBuyerPayAmount(String buyerPayAmount) {
        this.buyerPayAmount = buyerPayAmount;
    }


    public String getPointAmount() {
        return this.pointAmount;
    }


    public void setPointAmount(String pointAmount) {
        this.pointAmount = pointAmount;
    }


    public String getRefundFee() {
        return this.refundFee;
    }


    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }


    public String getFundBillList() {
        return this.fundBillList;
    }


    public void setFundBillList(String fundBillList) {
        this.fundBillList = fundBillList;
    }


    public String getBuyerLogonId() {
        return this.buyerLogonId;
    }


    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }


    public String getPassbackParams() {
        return this.passbackParams;
    }


    public void setPassbackParams(String passbackParams) {
        this.passbackParams = passbackParams;
    }


    public String getGmtCreate() {
        return this.gmtCreate;
    }


    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }


    public String getGmtPayment() {
        return this.gmtPayment;
    }


    public void setGmtPayment(String gmtPayment) {
        this.gmtPayment = gmtPayment;
    }


    public String getGmtRefund() {
        return this.gmtRefund;
    }


    public void setGmtRefund(String gmtRefund) {
        this.gmtRefund = gmtRefund;
    }


    public String getGmtClose() {
        return this.gmtClose;
    }


    public void setGmtClose(String gmtClose) {
        this.gmtClose = gmtClose;
    }
}



