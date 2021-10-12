package com.bitcom.sdk.alipay;

import com.bitcom.config.AlipayConfig;
import com.bitcom.sdk.alipay.model.builder.AlipayTradePayRequestBuilder;
import com.bitcom.sdk.alipay.model.builder.AlipayTradeRefundRequestBuilder;
import com.bitcom.sdk.alipay.model.result.AlipayF2FPayResult;
import com.bitcom.sdk.alipay.model.result.AlipayF2FRefundResult;
import com.bitcom.sdk.alipay.service.AlipayTradeService;
import com.bitcom.sdk.alipay.service.impl.AlipayTradeServiceImpl;

public class AliPay {
    private static AlipayTradeService tradeService;

    public static void initSDKConfiguration(String appid, String privateKey, String aliPubKey) {
        AlipayConfig.APP_ID = appid;
        AlipayConfig.PRIVATE_KEY_RSA2 = privateKey;
        AlipayConfig.ZFB_PUBLIC_KEY_RSA2 = aliPubKey;

        if (tradeService == null) {
            synchronized (AliPay.class) {
                if (tradeService == null) {
                    tradeService = (AlipayTradeService) (new AlipayTradeServiceImpl.ClientBuilder()).build();
                }
            }
        }
    }

    public static void initSDKConfiguration() {
        if (tradeService == null) {
            synchronized (AliPay.class) {
                if (tradeService == null) {
                    tradeService = (AlipayTradeService) (new AlipayTradeServiceImpl.ClientBuilder()).build();
                }
            }
        }
    }


    public static AlipayF2FPayResult doScanPayBusiness(AlipayTradePayRequestBuilder builder) throws Exception {
        return tradeService.tradePay(builder);
    }


    public static AlipayF2FRefundResult doRefundBusiness(AlipayTradeRefundRequestBuilder builder) throws Exception {
        return tradeService.tradeRefund(builder);
    }
}



