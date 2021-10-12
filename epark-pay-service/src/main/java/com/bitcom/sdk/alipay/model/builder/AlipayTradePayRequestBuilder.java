package com.bitcom.sdk.alipay.model.builder;

import com.bitcom.sdk.alipay.model.ExtendParams;
import com.bitcom.sdk.alipay.model.GoodsDetail;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


public class AlipayTradePayRequestBuilder
        extends RequestBuilder {
    private BizContent bizContent = new BizContent();


    public BizContent getBizContent() {
        return this.bizContent;
    }


    public boolean validate() {
        if (StringUtils.isEmpty(this.bizContent.scene)) {
            throw new NullPointerException("scene should not be NULL!");
        }
        if (StringUtils.isEmpty(this.bizContent.authCode)) {
            throw new NullPointerException("auth_code should not be NULL!");
        }
        if (!Pattern.matches("^\\d{10,}$", this.bizContent.authCode)) {
            throw new IllegalStateException("invalid auth_code!");
        }
        if (StringUtils.isEmpty(this.bizContent.outTradeNo)) {
            throw new NullPointerException("out_trade_no should not be NULL!");
        }
        if (StringUtils.isEmpty(this.bizContent.totalAmount)) {
            throw new NullPointerException("total_amount should not be NULL!");
        }
        if (StringUtils.isEmpty(this.bizContent.subject)) {
            throw new NullPointerException("subject should not be NULL!");
        }
        if (StringUtils.isEmpty(this.bizContent.storeId)) {
            throw new NullPointerException("store_id should not be NULL!");
        }
        return true;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder("AlipayTradePayRequestBuilder{");
        sb.append("bizContent=").append(this.bizContent);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }


    public AlipayTradePayRequestBuilder() {
        this.bizContent.scene = "bar_code";
    }


    public AlipayTradePayRequestBuilder setAppAuthToken(String appAuthToken) {
        return (AlipayTradePayRequestBuilder) super.setAppAuthToken(appAuthToken);
    }


    public AlipayTradePayRequestBuilder setNotifyUrl(String notifyUrl) {
        return (AlipayTradePayRequestBuilder) super.setNotifyUrl(notifyUrl);
    }


    public String getScene() {
        return this.bizContent.scene;
    }


    public AlipayTradePayRequestBuilder setScene(String scene) {
        this.bizContent.scene = scene;
        return this;
    }


    public String getAuthCode() {
        return this.bizContent.authCode;
    }


    public AlipayTradePayRequestBuilder setAuthCode(String authCode) {
        this.bizContent.authCode = authCode;
        return this;
    }


    public String getOutTradeNo() {
        return this.bizContent.outTradeNo;
    }


    public AlipayTradePayRequestBuilder setOutTradeNo(String outTradeNo) {
        this.bizContent.outTradeNo = outTradeNo;
        return this;
    }


    public String getSellerId() {
        return this.bizContent.sellerId;
    }


    public AlipayTradePayRequestBuilder setSellerId(String sellerId) {
        this.bizContent.sellerId = sellerId;
        return this;
    }


    public String getTotalAmount() {
        return this.bizContent.totalAmount;
    }


    public AlipayTradePayRequestBuilder setTotalAmount(String totalAmount) {
        this.bizContent.totalAmount = totalAmount;
        return this;
    }


    public String getDiscountableAmount() {
        return this.bizContent.discountableAmount;
    }


    public AlipayTradePayRequestBuilder setDiscountableAmount(String discountableAmount) {
        this.bizContent.discountableAmount = discountableAmount;
        return this;
    }


    public String getUndiscountableAmount() {
        return this.bizContent.undiscountableAmount;
    }


    public AlipayTradePayRequestBuilder setUndiscountableAmount(String undiscountableAmount) {
        this.bizContent.undiscountableAmount = undiscountableAmount;
        return this;
    }


    public String getSubject() {
        return this.bizContent.subject;
    }


    public AlipayTradePayRequestBuilder setSubject(String subject) {
        this.bizContent.subject = subject;
        return this;
    }


    public String getBody() {
        return this.bizContent.body;
    }


    public AlipayTradePayRequestBuilder setBody(String body) {
        this.bizContent.body = body;
        return this;
    }


    public List<GoodsDetail> getGoodsDetailList() {
        return this.bizContent.goodsDetailList;
    }


    public AlipayTradePayRequestBuilder setGoodsDetailList(List<GoodsDetail> goodsDetailList) {
        this.bizContent.goodsDetailList = goodsDetailList;
        return this;
    }


    public String getOperatorId() {
        return this.bizContent.operatorId;
    }


    public AlipayTradePayRequestBuilder setOperatorId(String operatorId) {
        this.bizContent.operatorId = operatorId;
        return this;
    }


    public String getStoreId() {
        return this.bizContent.storeId;
    }


    public AlipayTradePayRequestBuilder setStoreId(String storeId) {
        this.bizContent.storeId = storeId;
        return this;
    }


    public String getAlipayStoreId() {
        return this.bizContent.alipayStoreId;
    }


    public AlipayTradePayRequestBuilder setAlipayStoreId(String alipayStoreId) {
        this.bizContent.alipayStoreId = alipayStoreId;
        return this;
    }


    public String getTerminalId() {
        return this.bizContent.terminalId;
    }


    public AlipayTradePayRequestBuilder setTerminalId(String terminalId) {
        this.bizContent.terminalId = terminalId;
        return this;
    }


    public ExtendParams getExtendParams() {
        return this.bizContent.extendParams;
    }


    public AlipayTradePayRequestBuilder setExtendParams(ExtendParams extendParams) {
        this.bizContent.extendParams = extendParams;
        return this;
    }


    public String getTimeoutExpress() {
        return this.bizContent.timeoutExpress;
    }


    public AlipayTradePayRequestBuilder setTimeoutExpress(String timeoutExpress) {
        this.bizContent.timeoutExpress = timeoutExpress;
        return this;
    }


    public static class BizContent {
        @SerializedName("out_trade_no")
        private String outTradeNo;


        private String scene;


        @SerializedName("auth_code")
        private String authCode;


        @SerializedName("seller_id")
        private String sellerId;


        @SerializedName("total_amount")
        private String totalAmount;


        @SerializedName("discountable_amount")
        private String discountableAmount;


        @SerializedName("undiscountable_amount")
        private String undiscountableAmount;


        private String subject;


        private String body;


        @SerializedName("goods_detail")
        private List<GoodsDetail> goodsDetailList;


        @SerializedName("operator_id")
        private String operatorId;


        @SerializedName("store_id")
        private String storeId;


        @SerializedName("alipay_store_id")
        private String alipayStoreId;


        @SerializedName("terminal_id")
        private String terminalId;


        @SerializedName("extend_params")
        private ExtendParams extendParams;


        @SerializedName("timeout_express")
        private String timeoutExpress;


        public String toString() {
            StringBuilder sb = new StringBuilder("BizContent{");
            sb.append("scene='").append(this.scene).append('\'');
            sb.append(", authCode='").append(this.authCode).append('\'');
            sb.append(", outTradeNo='").append(this.outTradeNo).append('\'');
            sb.append(", sellerId='").append(this.sellerId).append('\'');
            sb.append(", totalAmount='").append(this.totalAmount).append('\'');
            sb.append(", discountableAmount='").append(this.discountableAmount).append('\'');
            sb.append(", undiscountableAmount='").append(this.undiscountableAmount).append('\'');
            sb.append(", subject='").append(this.subject).append('\'');
            sb.append(", body='").append(this.body).append('\'');
            sb.append(", goodsDetailList=").append(this.goodsDetailList);
            sb.append(", operatorId='").append(this.operatorId).append('\'');
            sb.append(", storeId='").append(this.storeId).append('\'');
            sb.append(", alipayStoreId='").append(this.alipayStoreId).append('\'');
            sb.append(", terminalId='").append(this.terminalId).append('\'');
            sb.append(", extendParams=").append(this.extendParams);
            sb.append(", timeoutExpress='").append(this.timeoutExpress).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}



