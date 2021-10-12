package com.bitcom.sdk.alipay.service.impl.hb;

import com.bitcom.sdk.alipay.model.hb.HbStatus;
import com.bitcom.sdk.alipay.model.hb.SysTradeInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class HbListener
        implements TradeListener {
    private static Log log = LogFactory.getLog(HbListener.class);


    private void offerTradeInfo(String outTradeNo, long beforeCall, HbStatus status) {
        long afterCall = System.currentTimeMillis();


        double timeConsume = (afterCall - beforeCall) / 1000.0D;

        log.debug(outTradeNo + " offer " + status + ": " + timeConsume);


        HbQueue.offer(SysTradeInfo.newInstance(outTradeNo, timeConsume, status));
    }


    public void onPayTradeSuccess(String outTradeNo, long beforeCall) {
        offerTradeInfo(outTradeNo, beforeCall, HbStatus.S);
    }


    public void onPayInProgress(String outTradeNo, long beforeCall) {
        offerTradeInfo(outTradeNo, beforeCall, HbStatus.I);
    }


    public void onPayFailed(String outTradeNo, long beforeCall) {
        offerTradeInfo(outTradeNo, beforeCall, HbStatus.F);
    }


    public void onConnectException(String outTradeNo, long beforeCall) {
        offerTradeInfo(outTradeNo, beforeCall, HbStatus.X);
    }


    public void onSendException(String outTradeNo, long beforeCall) {
        offerTradeInfo(outTradeNo, beforeCall, HbStatus.Y);
    }


    public void onReceiveException(String outTradeNo, long beforeCall) {
        offerTradeInfo(outTradeNo, beforeCall, HbStatus.Z);
    }
}



