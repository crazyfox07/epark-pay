package com.bitcom.sdk.alipay.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.bitcom.config.AlipayConfig;
import com.bitcom.sdk.alipay.model.TradeStatus;
import com.bitcom.sdk.alipay.model.builder.AlipayTradeCancelRequestBuilder;
import com.bitcom.sdk.alipay.model.builder.AlipayTradePrecreateRequestBuilder;
import com.bitcom.sdk.alipay.model.builder.AlipayTradeQueryRequestBuilder;
import com.bitcom.sdk.alipay.model.builder.AlipayTradeRefundRequestBuilder;
import com.bitcom.sdk.alipay.model.builder.RequestBuilder;
import com.bitcom.sdk.alipay.model.result.AlipayF2FPayResult;
import com.bitcom.sdk.alipay.model.result.AlipayF2FPrecreateResult;
import com.bitcom.sdk.alipay.model.result.AlipayF2FQueryResult;
import com.bitcom.sdk.alipay.model.result.AlipayF2FRefundResult;
import com.bitcom.sdk.alipay.service.AlipayTradeService;
import com.bitcom.sdk.alipay.util.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract class AbsAlipayTradeService extends AbsAlipayService implements AlipayTradeService {
    protected static ExecutorService executorService = Executors.newCachedThreadPool();

    protected AlipayClient client;

    public AlipayF2FQueryResult queryTradeResult(AlipayTradeQueryRequestBuilder builder) {
        AlipayTradeQueryResponse response = tradeQuery(builder);

        AlipayF2FQueryResult result = new AlipayF2FQueryResult(response);
        if (querySuccess(response)) {

            result.setTradeStatus(TradeStatus.SUCCESS);
        } else if (tradeError((AlipayResponse) response)) {

            result.setTradeStatus(TradeStatus.UNKNOWN);
        } else {

            result.setTradeStatus(TradeStatus.FAILED);
        }
        return result;
    }

    protected AlipayTradeQueryResponse tradeQuery(AlipayTradeQueryRequestBuilder builder) {
        validateBuilder((RequestBuilder) builder);

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());
        request.setBizContent(builder.toJsonString());
        this.log.info("trade.query bizContent:" + request.getBizContent());

        return (AlipayTradeQueryResponse) getResponse(this.client, (AlipayRequest) request);
    }


    public AlipayF2FRefundResult tradeRefund(AlipayTradeRefundRequestBuilder builder) {
        validateBuilder((RequestBuilder) builder);

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setNotifyUrl(builder.getNotifyUrl());
        request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());
        request.setBizContent(builder.toJsonString());
        this.log.info("trade.refund bizContent:" + request.getBizContent());

        AlipayTradeRefundResponse response = (AlipayTradeRefundResponse) getResponse(this.client, (AlipayRequest) request);

        AlipayF2FRefundResult result = new AlipayF2FRefundResult(response);
        if (response != null && "10000".equals(response.getCode())) {

            result.setTradeStatus(TradeStatus.SUCCESS);
        } else if (tradeError((AlipayResponse) response)) {

            result.setTradeStatus(TradeStatus.UNKNOWN);
        } else {

            result.setTradeStatus(TradeStatus.FAILED);
        }
        return result;
    }


    public AlipayF2FPrecreateResult tradePrecreate(AlipayTradePrecreateRequestBuilder builder) {
        validateBuilder((RequestBuilder) builder);

        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(builder.getNotifyUrl());
        request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());
        request.setBizContent(builder.toJsonString());
        this.log.info("trade.precreate bizContent:" + request.getBizContent());

        AlipayTradePrecreateResponse response = (AlipayTradePrecreateResponse) getResponse(this.client, (AlipayRequest) request);

        AlipayF2FPrecreateResult result = new AlipayF2FPrecreateResult(response);
        if (response != null && "10000".equals(response.getCode())) {

            result.setTradeStatus(TradeStatus.SUCCESS);
        } else if (tradeError((AlipayResponse) response)) {

            result.setTradeStatus(TradeStatus.UNKNOWN);
        } else {

            result.setTradeStatus(TradeStatus.FAILED);
        }
        return result;
    }


    protected AlipayF2FPayResult checkQueryAndCancel(String outTradeNo, String appAuthToken, AlipayF2FPayResult result, AlipayTradeQueryResponse queryResponse) {
        if (querySuccess(queryResponse)) {

            result.setTradeStatus(TradeStatus.SUCCESS);
            result.setResponse(toPayResponse(queryResponse));
            return result;
        }


        AlipayTradeCancelRequestBuilder builder = (new AlipayTradeCancelRequestBuilder()).setOutTradeNo(outTradeNo);
        builder.setAppAuthToken(appAuthToken);
        AlipayTradeCancelResponse cancelResponse = cancelPayResult(builder);
        if (tradeError((AlipayResponse) cancelResponse)) {

            result.setTradeStatus(TradeStatus.UNKNOWN);
        } else {

            result.setTradeStatus(TradeStatus.FAILED);
        }
        return result;
    }


    protected AlipayTradeCancelResponse tradeCancel(AlipayTradeCancelRequestBuilder builder) {
        validateBuilder((RequestBuilder) builder);

        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        request.putOtherTextParam("app_auth_token", builder.getAppAuthToken());
        request.setBizContent(builder.toJsonString());
        this.log.info("trade.cancel bizContent:" + request.getBizContent());

        return (AlipayTradeCancelResponse) getResponse(this.client, (AlipayRequest) request);
    }


    protected AlipayTradeQueryResponse loopQueryResult(AlipayTradeQueryRequestBuilder builder) {
        AlipayTradeQueryResponse queryResult = null;
        for (int i = 0; i < AlipayConfig.maxQueryRetry; i++) {
            Utils.sleep(AlipayConfig.queryDuration);

            AlipayTradeQueryResponse response = tradeQuery(builder);
            if (response != null) {
                if (stopQuery(response)) {
                    return response;
                }
                queryResult = response;
            }
        }
        return queryResult;
    }


    protected boolean stopQuery(AlipayTradeQueryResponse response) {
        if ("10000".equals(response.getCode()) && (
                "TRADE_FINISHED".equals(response.getTradeStatus()) || "TRADE_SUCCESS"
                        .equals(response.getTradeStatus()) || "TRADE_CLOSED"
                        .equals(response.getTradeStatus()))) {
            return true;
        }

        return false;
    }


    protected AlipayTradeCancelResponse cancelPayResult(AlipayTradeCancelRequestBuilder builder) {
        AlipayTradeCancelResponse response = tradeCancel(builder);
        if (cancelSuccess(response)) {
            return response;
        }


        if (needRetry(response)) {

            this.log.warn("begin async cancel request:" + builder);
            asyncCancel(builder);
        }
        return response;
    }


    protected void asyncCancel(final AlipayTradeCancelRequestBuilder builder) {
        executorService.submit(new Runnable() {
            public void run() {
                for (int i = 0; i < AlipayConfig.maxCancelRetry; i++) {
                    Utils.sleep(AlipayConfig.cancelDuration);

                    AlipayTradeCancelResponse response = AbsAlipayTradeService.this.tradeCancel(builder);
                    if (AbsAlipayTradeService.this.cancelSuccess(response) ||
                            !AbsAlipayTradeService.this.needRetry(response)) {
                        return;
                    }
                }
            }
        });
    }


    protected AlipayTradePayResponse toPayResponse(AlipayTradeQueryResponse response) {
        AlipayTradePayResponse payResponse = new AlipayTradePayResponse();

        payResponse.setCode(querySuccess(response) ? "10000" : "40004");


        StringBuilder msg = (new StringBuilder(response.getMsg())).append(" tradeStatus:").append(response.getTradeStatus());
        payResponse.setMsg(msg.toString());
        payResponse.setSubCode(response.getSubCode());
        payResponse.setSubMsg(response.getSubMsg());
        payResponse.setBody(response.getBody());
        payResponse.setParams(response.getParams());


        payResponse.setBuyerLogonId(response.getBuyerLogonId());
        payResponse.setFundBillList(response.getFundBillList());
        payResponse.setOpenId(response.getOpenId());
        payResponse.setOutTradeNo(response.getOutTradeNo());
        payResponse.setReceiptAmount(response.getReceiptAmount());
        payResponse.setTotalAmount(response.getTotalAmount());
        payResponse.setTradeNo(response.getTradeNo());
        return payResponse;
    }


    protected boolean needRetry(AlipayTradeCancelResponse response) {
        return (response == null || "Y"
                .equals(response.getRetryFlag()));
    }


    protected boolean querySuccess(AlipayTradeQueryResponse response) {
        return (response != null && "10000"
                .equals(response.getCode()) && ("TRADE_SUCCESS"
                .equals(response.getTradeStatus()) || "TRADE_FINISHED"
                .equals(response.getTradeStatus())));
    }


    protected boolean cancelSuccess(AlipayTradeCancelResponse response) {
        return (response != null && "10000"
                .equals(response.getCode()));
    }


    protected boolean tradeError(AlipayResponse response) {
        return (response == null || "20000"
                .equals(response.getCode()));
    }
}



