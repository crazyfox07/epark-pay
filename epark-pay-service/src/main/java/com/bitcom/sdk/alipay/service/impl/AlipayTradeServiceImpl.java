package com.bitcom.sdk.alipay.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.bitcom.config.AlipayConfig;
import com.bitcom.sdk.alipay.model.TradeStatus;
import com.bitcom.sdk.alipay.model.builder.AlipayTradePayRequestBuilder;
import com.bitcom.sdk.alipay.model.builder.AlipayTradePrecreateRequestBuilder;
import com.bitcom.sdk.alipay.model.builder.AlipayTradeQueryRequestBuilder;
import com.bitcom.sdk.alipay.model.builder.AlipayTradeRefundRequestBuilder;
import com.bitcom.sdk.alipay.model.builder.RequestBuilder;
import com.bitcom.sdk.alipay.model.result.AlipayF2FPayResult;
import com.bitcom.sdk.alipay.model.result.AlipayF2FPrecreateResult;
import com.bitcom.sdk.alipay.model.result.AlipayF2FQueryResult;
import com.bitcom.sdk.alipay.model.result.AlipayF2FRefundResult;
import org.apache.commons.lang.StringUtils;

public class AlipayTradeServiceImpl extends AbsAlipayTradeService {
    public static class ClientBuilder {
        private String gatewayUrl;
        private String appid;
        private String privateKey;

        public AlipayTradeServiceImpl build() {
            if (StringUtils.isEmpty(this.gatewayUrl)) {
                this.gatewayUrl = AlipayConfig.OPEN_API_DOMAIN;
            }
            if (StringUtils.isEmpty(this.appid)) {
                this.appid = AlipayConfig.APP_ID;
            }
            if (StringUtils.isEmpty(this.privateKey)) {
                this.privateKey = AlipayConfig.PRIVATE_KEY_RSA2;
            }
            if (StringUtils.isEmpty(this.format)) {
                this.format = "json";
            }
            if (StringUtils.isEmpty(this.charset)) {
                this.charset = "utf-8";
            }
            if (StringUtils.isEmpty(this.alipayPublicKey)) {
                this.alipayPublicKey = AlipayConfig.ZFB_PUBLIC_KEY_RSA2;
            }
            if (StringUtils.isEmpty(this.signType)) {
                this.signType = AlipayConfig.SIGN_TYPE;
            }
            return new AlipayTradeServiceImpl(this);
        }

        private String format;
        private String charset;
        private String alipayPublicKey;
        private String signType;

        public ClientBuilder setAlipayPublicKey(String alipayPublicKey) {
            this.alipayPublicKey = alipayPublicKey;
            return this;
        }

        public ClientBuilder setAppid(String appid) {
            this.appid = appid;
            return this;
        }

        public ClientBuilder setCharset(String charset) {
            this.charset = charset;
            return this;
        }

        public ClientBuilder setFormat(String format) {
            this.format = format;
            return this;
        }

        public ClientBuilder setGatewayUrl(String gatewayUrl) {
            this.gatewayUrl = gatewayUrl;
            return this;
        }

        public ClientBuilder setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
            return this;
        }

        public ClientBuilder setSignType(String signType) {
            this.signType = signType;
            return this;
        }


        public String getAlipayPublicKey() {
            return this.alipayPublicKey;
        }


        public String getSignType() {
            return this.signType;
        }


        public String getAppid() {
            return this.appid;
        }


        public String getCharset() {
            return this.charset;
        }


        public String getFormat() {
            return this.format;
        }


        public String getGatewayUrl() {
            return this.gatewayUrl;
        }


        public String getPrivateKey() {
            return this.privateKey;
        }
    }


    public AlipayTradeServiceImpl(ClientBuilder builder) {
        if (StringUtils.isEmpty(builder.getGatewayUrl())) {
            throw new NullPointerException("gatewayUrl should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getAppid())) {
            throw new NullPointerException("appid should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getPrivateKey())) {
            throw new NullPointerException("privateKey should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getFormat())) {
            throw new NullPointerException("format should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getCharset())) {
            throw new NullPointerException("charset should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getAlipayPublicKey())) {
            throw new NullPointerException("alipayPublicKey should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getSignType())) {
            throw new NullPointerException("signType should not be NULL!");
        }

        this
                .client = (AlipayClient) new DefaultAlipayClient(builder.getGatewayUrl(), builder.getAppid(), builder.getPrivateKey(), builder.getFormat(), builder.getCharset(), builder.getAlipayPublicKey(), builder.getSignType());
    }


    public AlipayF2FPayResult tradePay(AlipayTradePayRequestBuilder builder) {
        validateBuilder((RequestBuilder) builder);

        String outTradeNo = builder.getOutTradeNo();

        AlipayTradePayRequest request = new AlipayTradePayRequest();

        request.setNotifyUrl(builder.getNotifyUrl());
        String appAuthToken = builder.getAppAuthToken();


        request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());


        request.setBizContent(builder.toJsonString());
        this.log.info("trade.pay bizContent:" + request.getBizContent());


        AlipayTradePayResponse response = (AlipayTradePayResponse) getResponse(this.client, (AlipayRequest) request);

        AlipayF2FPayResult result = new AlipayF2FPayResult(response);
        if (response != null && "10000".equals(response.getCode())) {

            result.setTradeStatus(TradeStatus.SUCCESS);
        } else {
            if (response != null && "10003".equals(response.getCode())) {


                AlipayTradeQueryRequestBuilder queryBuiler = (new AlipayTradeQueryRequestBuilder()).setAppAuthToken(appAuthToken).setOutTradeNo(outTradeNo);
                AlipayTradeQueryResponse loopQueryResponse = loopQueryResult(queryBuiler);
                return checkQueryAndCancel(outTradeNo, appAuthToken, result, loopQueryResponse);
            }
            if (tradeError((AlipayResponse) response)) {


                AlipayTradeQueryRequestBuilder queryBuiler = (new AlipayTradeQueryRequestBuilder()).setAppAuthToken(appAuthToken).setOutTradeNo(outTradeNo);
                AlipayTradeQueryResponse queryResponse = tradeQuery(queryBuiler);
                return checkQueryAndCancel(outTradeNo, appAuthToken, result, queryResponse);
            }


            result.setTradeStatus(TradeStatus.FAILED);
        }

        return result;
    }
}



