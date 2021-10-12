package com.bitcom.sdk.alipay.model.builder;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.StringUtils;


public class AlipayTradeQueryRequestBuilder
        extends RequestBuilder {
    private BizContent bizContent = new BizContent();


    public BizContent getBizContent() {
        return this.bizContent;
    }


    public boolean validate() {
        if (StringUtils.isEmpty(this.bizContent.tradeNo) &&
                StringUtils.isEmpty(this.bizContent.outTradeNo)) {
            throw new IllegalStateException("tradeNo and outTradeNo can not both be NULL!");
        }
        return true;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder("AlipayTradeQueryRequestBuilder{");
        sb.append("bizContent=").append(this.bizContent);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }


    public AlipayTradeQueryRequestBuilder setAppAuthToken(String appAuthToken) {
        return (AlipayTradeQueryRequestBuilder) super.setAppAuthToken(appAuthToken);
    }


    public AlipayTradeQueryRequestBuilder setNotifyUrl(String notifyUrl) {
        return (AlipayTradeQueryRequestBuilder) super.setNotifyUrl(notifyUrl);
    }


    public String getTradeNo() {
        return this.bizContent.tradeNo;
    }


    public AlipayTradeQueryRequestBuilder setTradeNo(String tradeNo) {
        this.bizContent.tradeNo = tradeNo;
        return this;
    }


    public String getOutTradeNo() {
        return this.bizContent.outTradeNo;
    }


    public AlipayTradeQueryRequestBuilder setOutTradeNo(String outTradeNo) {
        this.bizContent.outTradeNo = outTradeNo;
        return this;
    }


    public static class BizContent {
        @SerializedName("trade_no")
        private String tradeNo;

        @SerializedName("out_trade_no")
        private String outTradeNo;


        public String toString() {
            StringBuilder sb = new StringBuilder("BizContent{");
            sb.append("tradeNo='").append(this.tradeNo).append('\'');
            sb.append(", outTradeNo='").append(this.outTradeNo).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}



