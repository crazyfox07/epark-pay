package com.bitcom.sdk.alipay.model.builder;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.StringUtils;


public class AlipayTradeRefundRequestBuilder
        extends RequestBuilder {
    private BizContent bizContent = new BizContent();


    public BizContent getBizContent() {
        return this.bizContent;
    }


    public boolean validate() {
        if (StringUtils.isEmpty(this.bizContent.outTradeNo) &&
                StringUtils.isEmpty(this.bizContent.tradeNo)) {
            throw new NullPointerException("out_trade_no and trade_no should not both be NULL!");
        }
        if (StringUtils.isEmpty(this.bizContent.refundAmount)) {
            throw new NullPointerException("refund_amount should not be NULL!");
        }
        if (StringUtils.isEmpty(this.bizContent.refundReason)) {
            throw new NullPointerException("refund_reson should not be NULL!");
        }
        return true;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder("AlipayTradeRefundRequestBuilder{");
        sb.append("bizContent=").append(this.bizContent);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }


    public AlipayTradeRefundRequestBuilder setAppAuthToken(String appAuthToken) {
        return (AlipayTradeRefundRequestBuilder) super.setAppAuthToken(appAuthToken);
    }


    public AlipayTradeRefundRequestBuilder setNotifyUrl(String notifyUrl) {
        return (AlipayTradeRefundRequestBuilder) super.setNotifyUrl(notifyUrl);
    }


    public String getOutTradeNo() {
        return this.bizContent.outTradeNo;
    }


    public AlipayTradeRefundRequestBuilder setOutTradeNo(String outTradeNo) {
        this.bizContent.outTradeNo = outTradeNo;
        return this;
    }

    public AlipayTradeRefundRequestBuilder setTradeNo(String tradeNo) {
        this.bizContent.tradeNo = tradeNo;
        return this;
    }

    public AlipayTradeRefundRequestBuilder setRefundAmount(String refundAmount) {
        this.bizContent.refundAmount = refundAmount;
        return this;
    }

    public AlipayTradeRefundRequestBuilder setOutRequestNo(String outRequestNo) {
        this.bizContent.outRequestNo = outRequestNo;
        return this;
    }

    public AlipayTradeRefundRequestBuilder setRefundReason(String refundReason) {
        this.bizContent.refundReason = refundReason;
        return this;
    }

    public AlipayTradeRefundRequestBuilder setStoreId(String storeId) {
        this.bizContent.storeId = storeId;
        return this;
    }

    public AlipayTradeRefundRequestBuilder setAlipayStoreId(String alipayStoreId) {
        this.bizContent.alipayStoreId = alipayStoreId;
        return this;
    }

    public AlipayTradeRefundRequestBuilder setTerminalId(String terminalId) {
        this.bizContent.terminalId = terminalId;
        return this;
    }


    public String getTradeNo() {
        return this.bizContent.tradeNo;
    }


    public String getRefundAmount() {
        return this.bizContent.refundAmount;
    }


    public String getOutRequestNo() {
        return this.bizContent.outRequestNo;
    }


    public String getRefundReason() {
        return this.bizContent.refundReason;
    }


    public String getStoreId() {
        return this.bizContent.storeId;
    }


    public String getAlipayStoreId() {
        return this.bizContent.alipayStoreId;
    }


    public String getTerminalId() {
        return this.bizContent.terminalId;
    }


    public static class BizContent {
        @SerializedName("trade_no")
        private String tradeNo;


        @SerializedName("out_trade_no")
        private String outTradeNo;


        @SerializedName("refund_amount")
        private String refundAmount;


        @SerializedName("out_request_no")
        private String outRequestNo;


        @SerializedName("refund_reason")
        private String refundReason;


        @SerializedName("store_id")
        private String storeId;


        @SerializedName("alipay_store_id")
        private String alipayStoreId;


        @SerializedName("terminal_id")
        private String terminalId;


        public String toString() {
            StringBuilder sb = new StringBuilder("BizContent{");
            sb.append("tradeNo='").append(this.tradeNo).append('\'');
            sb.append(", outTradeNo='").append(this.outTradeNo).append('\'');
            sb.append(", refundAmount='").append(this.refundAmount).append('\'');
            sb.append(", outRequestNo='").append(this.outRequestNo).append('\'');
            sb.append(", refundReason='").append(this.refundReason).append('\'');
            sb.append(", storeId='").append(this.storeId).append('\'');
            sb.append(", alipayStoreId='").append(this.alipayStoreId).append('\'');
            sb.append(", terminalId='").append(this.terminalId).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}



