package com.bitcom.sdk.alipay.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.MonitorHeartbeatSynRequest;
import com.alipay.api.response.MonitorHeartbeatSynResponse;
import com.bitcom.config.AlipayConfig;
import com.bitcom.sdk.alipay.model.builder.AlipayHeartbeatSynRequestBuilder;
import com.bitcom.sdk.alipay.model.builder.RequestBuilder;
import com.bitcom.sdk.alipay.service.AlipayMonitorService;
import org.apache.commons.lang.StringUtils;

public class AlipayMonitorServiceImpl
        extends AbsAlipayService implements AlipayMonitorService {
    private AlipayClient client;

    public static class ClientBuilder {
        private String gatewayUrl;
        private String appid;
        private String privateKey;
        private String format;
        private String charset;
        private String signType;

        public AlipayMonitorServiceImpl build() {
            if (StringUtils.isEmpty(this.gatewayUrl)) {
                this.gatewayUrl = AlipayConfig.MCLOUD_API_DOMAIN;
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
            if (StringUtils.isEmpty(this.signType)) {
                this.signType = AlipayConfig.SIGN_TYPE;
            }
            return new AlipayMonitorServiceImpl(this);
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


        public String getSignType() {
            return this.signType;
        }
    }


    public AlipayMonitorServiceImpl(ClientBuilder builder) {
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
        if (StringUtils.isEmpty(builder.getSignType())) {
            throw new NullPointerException("signType should not be NULL!");
        }


        this
                .client = (AlipayClient) new DefaultAlipayClient(builder.getGatewayUrl(), builder.getAppid(), builder.getPrivateKey(), builder.getFormat(), builder.getCharset(), builder.getSignType());
    }


    public MonitorHeartbeatSynResponse heartbeatSyn(AlipayHeartbeatSynRequestBuilder builder) {
        validateBuilder((RequestBuilder) builder);

        MonitorHeartbeatSynRequest request = new MonitorHeartbeatSynRequest();
        request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());
        request.setBizContent(builder.toJsonString());
        this.log.info("heartbeat.sync bizContent:" + request.getBizContent());

        return (MonitorHeartbeatSynResponse) getResponse(this.client, (AlipayRequest) request);
    }
}



