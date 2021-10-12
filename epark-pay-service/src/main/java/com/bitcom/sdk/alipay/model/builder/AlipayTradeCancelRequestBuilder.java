package com.bitcom.sdk.alipay.model.builder;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.StringUtils;


public class AlipayTradeCancelRequestBuilder
        extends RequestBuilder {
    private BizContent bizContent = new BizContent();


    public BizContent getBizContent() {
        return this.bizContent;
    }


    public boolean validate() {
        if (StringUtils.isEmpty(this.bizContent.outTradeNo)) {
            throw new NullPointerException("out_trade_no should not be NULL!");
        }
        return true;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder("AlipayTradeCancelRequestBuilder{");
        sb.append("bizContent=").append(this.bizContent);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }


    public AlipayTradeCancelRequestBuilder setAppAuthToken(String appAuthToken) {
        return (AlipayTradeCancelRequestBuilder) super.setAppAuthToken(appAuthToken);
    }


    public AlipayTradeCancelRequestBuilder setNotifyUrl(String notifyUrl) {
        return (AlipayTradeCancelRequestBuilder) super.setNotifyUrl(notifyUrl);
    }


    public String getOutTradeNo() {
        return this.bizContent.outTradeNo;
    }


    public AlipayTradeCancelRequestBuilder setOutTradeNo(String outTradeNo) {
        this.bizContent.outTradeNo = outTradeNo;
        return this;
    }


    public static class BizContent {
        @SerializedName("out_trade_no")
        private String outTradeNo;

        public String toString() {
            StringBuilder sb = new StringBuilder("BizContent{");
            sb.append("outTradeNo='").append(this.outTradeNo).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}



