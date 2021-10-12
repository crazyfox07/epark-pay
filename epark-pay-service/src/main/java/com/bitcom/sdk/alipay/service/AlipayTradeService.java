package com.bitcom.sdk.alipay.service;

import com.bitcom.sdk.alipay.model.builder.AlipayTradePayRequestBuilder;
import com.bitcom.sdk.alipay.model.builder.AlipayTradePrecreateRequestBuilder;
import com.bitcom.sdk.alipay.model.builder.AlipayTradeQueryRequestBuilder;
import com.bitcom.sdk.alipay.model.builder.AlipayTradeRefundRequestBuilder;
import com.bitcom.sdk.alipay.model.result.AlipayF2FPayResult;
import com.bitcom.sdk.alipay.model.result.AlipayF2FPrecreateResult;
import com.bitcom.sdk.alipay.model.result.AlipayF2FQueryResult;
import com.bitcom.sdk.alipay.model.result.AlipayF2FRefundResult;

public interface AlipayTradeService {
    AlipayF2FPayResult tradePay(AlipayTradePayRequestBuilder paramAlipayTradePayRequestBuilder);

    AlipayF2FQueryResult queryTradeResult(AlipayTradeQueryRequestBuilder paramAlipayTradeQueryRequestBuilder);

    AlipayF2FRefundResult tradeRefund(AlipayTradeRefundRequestBuilder paramAlipayTradeRefundRequestBuilder);

    AlipayF2FPrecreateResult tradePrecreate(AlipayTradePrecreateRequestBuilder paramAlipayTradePrecreateRequestBuilder);
}



