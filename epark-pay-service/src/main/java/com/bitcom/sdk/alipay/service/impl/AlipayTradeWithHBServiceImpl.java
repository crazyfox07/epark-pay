package com.bitcom.sdk.alipay.service.impl;

import com.alipay.api.AlipayApiException;
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
import com.bitcom.sdk.alipay.service.impl.hb.HbListener;
import com.bitcom.sdk.alipay.service.impl.hb.TradeListener;
import org.apache.commons.lang.StringUtils;


public class AlipayTradeWithHBServiceImpl
        extends AbsAlipayTradeService {
    private TradeListener listener;

    public static class ClientBuilder {
        private String gatewayUrl;
        private String appid;
        private String privateKey;
        private String format;
        private String charset;
        private String alipayPublicKey;
        private String signType;
        private TradeListener listener;

        public AlipayTradeWithHBServiceImpl build() {
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
            if (this.listener == null) {
                this.listener = (TradeListener) new HbListener();
            }

            return new AlipayTradeWithHBServiceImpl(this);
        }


        public TradeListener getListener() {
            return this.listener;
        }


        public ClientBuilder setListener(TradeListener listener) {
            this.listener = listener;
            return this;
        }

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


    public AlipayTradeWithHBServiceImpl(ClientBuilder builder) {
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
        if (builder.getListener() == null) {
            throw new NullPointerException("listener should not be NULL!");
        }

        this.listener = builder.getListener();
        this
                .client = (AlipayClient) new DefaultAlipayClient(builder.getGatewayUrl(), builder.getAppid(), builder.getPrivateKey(), builder.getFormat(), builder.getCharset(), builder.getAlipayPublicKey(), builder.getSignType());
    }


    private AlipayTradePayResponse getResponse(AlipayClient client, AlipayTradePayRequest request, final String outTradeNo, final long beforeCall) {
        try {
            AlipayTradePayResponse response = (AlipayTradePayResponse) client.execute((AlipayRequest) request);
            if (response != null) {
                this.log.info(response.getBody());
            }
            return response;
        } catch (AlipayApiException e) {

            Throwable cause = e.getCause();

            if (cause instanceof java.net.ConnectException || cause instanceof java.net.NoRouteToHostException) {


                executorService.submit(new Runnable() {
                    public void run() {
                        AlipayTradeWithHBServiceImpl.this.listener.onConnectException(outTradeNo, beforeCall);
                    }
                });
            } else if (cause instanceof java.net.SocketException) {

                executorService.submit(new Runnable() {
                    public void run() {
                        AlipayTradeWithHBServiceImpl.this.listener.onSendException(outTradeNo, beforeCall);
                    }
                });
            } else if (cause instanceof java.net.SocketTimeoutException) {

                executorService.submit(new Runnable() {
                    public void run() {
                        AlipayTradeWithHBServiceImpl.this.listener.onReceiveException(outTradeNo, beforeCall);
                    }
                });
            }

            e.printStackTrace();
            return null;
        }
    }


    public AlipayF2FPayResult tradePay(AlipayTradePayRequestBuilder builder) {
        validateBuilder((RequestBuilder) builder);

        final String outTradeNo = builder.getOutTradeNo();

        AlipayTradePayRequest request = new AlipayTradePayRequest();

        String appAuthToken = builder.getAppAuthToken();
        request.setNotifyUrl(builder.getNotifyUrl());
        request.putOtherTextParam("app_auth_token", appAuthToken);


        request.setBizContent(builder.toJsonString());
        this.log.info("trade.pay bizContent:" + request.getBizContent());


        final long beforeCall = System.currentTimeMillis();
        AlipayTradePayResponse response = getResponse(this.client, request, outTradeNo, beforeCall);

        AlipayF2FPayResult result = new AlipayF2FPayResult(response);
        if (response != null && "10000".equals(response.getCode())) {

            result.setTradeStatus(TradeStatus.SUCCESS);

            executorService.submit(new Runnable() {
                public void run() {
                    AlipayTradeWithHBServiceImpl.this.listener.onPayTradeSuccess(outTradeNo, beforeCall);
                }
            });
        } else {
            if (response != null && "10003".equals(response.getCode())) {

                executorService.submit(new Runnable() {
                    public void run() {
                        AlipayTradeWithHBServiceImpl.this.listener.onPayInProgress(outTradeNo, beforeCall);
                    }
                });


                AlipayTradeQueryRequestBuilder queryBuiler = (new AlipayTradeQueryRequestBuilder()).setAppAuthToken(appAuthToken).setOutTradeNo(outTradeNo);
                AlipayTradeQueryResponse loopQueryResponse = loopQueryResult(queryBuiler);
                return checkQueryAndCancel(outTradeNo, appAuthToken, result, loopQueryResponse);
            }
            if (tradeError((AlipayResponse) response)) {

                executorService.submit(new Runnable() {
                    public void run() {
                        AlipayTradeWithHBServiceImpl.this.listener.onPayFailed(outTradeNo, beforeCall);
                    }
                });


                AlipayTradeQueryRequestBuilder queryBuiler = (new AlipayTradeQueryRequestBuilder()).setAppAuthToken(appAuthToken).setOutTradeNo(outTradeNo);
                AlipayTradeQueryResponse queryResponse = tradeQuery(queryBuiler);
                return checkQueryAndCancel(outTradeNo, appAuthToken, result, queryResponse);
            }


            result.setTradeStatus(TradeStatus.FAILED);

            executorService.submit(new Runnable() {
                public void run() {
                    AlipayTradeWithHBServiceImpl.this.listener.onPayFailed(outTradeNo, beforeCall);
                }
            });
        }

        return result;
    }
}



