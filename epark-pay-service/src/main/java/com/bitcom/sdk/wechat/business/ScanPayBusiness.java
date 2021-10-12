package com.bitcom.sdk.wechat.business;

import com.bitcom.sdk.wechat.common.Configure;
import com.bitcom.sdk.wechat.common.Log;
import com.bitcom.sdk.wechat.common.Signature;
import com.bitcom.sdk.wechat.common.Util;
import com.bitcom.sdk.wechat.common.report.Reporter;
import com.bitcom.sdk.wechat.common.report.ReporterFactory;
import com.bitcom.sdk.wechat.common.report.protocol.ReportReqData;
import com.bitcom.sdk.wechat.common.report.service.ReportService;
import com.bitcom.sdk.wechat.protocol.pay_protocol.ScanPayReqData;
import com.bitcom.sdk.wechat.protocol.pay_protocol.ScanPayResData;
import com.bitcom.sdk.wechat.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.bitcom.sdk.wechat.protocol.pay_query_protocol.ScanPayQueryResData;
import com.bitcom.sdk.wechat.protocol.reverse_protocol.ReverseReqData;
import com.bitcom.sdk.wechat.protocol.reverse_protocol.ReverseResData;
import com.bitcom.sdk.wechat.service.ReverseService;
import com.bitcom.sdk.wechat.service.ScanPayQueryService;
import com.bitcom.sdk.wechat.service.ScanPayService;
import org.slf4j.LoggerFactory;

public class ScanPayBusiness {
    private static Log log = new Log(LoggerFactory.getLogger(ScanPayBusiness.class));

    private int waitingTimeBeforePayQueryServiceInvoked = 5000;

    private int payQueryLoopInvokedCount = 3;

    private int waitingTimeBeforeReverseServiceInvoked = 5000;
    private ScanPayService scanPayService;
    private ScanPayQueryService scanPayQueryService;
    private ReverseService reverseService;
    private boolean needRecallReverse = false;

    public ScanPayBusiness()
            throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        this.scanPayService = new ScanPayService();
        this.scanPayQueryService = new ScanPayQueryService();
        this.reverseService = new ReverseService();
    }

    public void run(ScanPayReqData scanPayReqData, ResultListener resultListener)
            throws Exception {
        long timeAfterReport;
        String outTradeNo = scanPayReqData.getOut_trade_no();

        long costTimeStart = System.currentTimeMillis();

        log.i("支付API返回的数据如下：");
        String payServiceResponseString = this.scanPayService.request(scanPayReqData);

        long costTimeEnd = System.currentTimeMillis();
        long totalTimeCost = costTimeEnd - costTimeStart;
        log.i("api请求总耗时：" + totalTimeCost + "ms");

        log.i(payServiceResponseString);

        ScanPayResData scanPayResData = (ScanPayResData) Util.getObjectFromXML(payServiceResponseString, ScanPayResData.class);

        ReportReqData reportReqData = new ReportReqData(scanPayReqData
                .getDevice_info(), Configure.PAY_API, (int) totalTimeCost, scanPayResData
                .getReturn_code(), scanPayResData
                .getReturn_msg(), scanPayResData
                .getResult_code(), scanPayResData
                .getErr_code(), scanPayResData
                .getErr_code_des(), scanPayResData
                .getOut_trade_no(), scanPayReqData
                .getSpbill_create_ip());

        if (Configure.isUseThreadToDoReport()) {
            ReporterFactory.getReporter(reportReqData).run();
            timeAfterReport = System.currentTimeMillis();
            log.i("pay+report总耗时（异步方式上报）：" + (timeAfterReport - costTimeStart) + "ms");
        } else {
            ReportService.request(reportReqData);
            timeAfterReport = System.currentTimeMillis();
            log.i("pay+report总耗时（同步方式上报）：" + (timeAfterReport - costTimeStart) + "ms");
        }

        if ((scanPayResData == null) || (scanPayResData.getReturn_code() == null)) {
            log.e("【支付失败】支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
            resultListener.onFailByReturnCodeError(scanPayResData);
            return;
        }

        if (scanPayResData.getReturn_code().equals("FAIL")) {
            log.e("【支付失败】支付API系统返回失败，请检测Post给API的数据是否规范合法");
            resultListener.onFailByReturnCodeFail(scanPayResData);
            return;
        }
        log.i("支付API系统成功返回数据");

        if (!(Signature.checkIsSignValidFromResponseString(payServiceResponseString))) {
            log.e("【支付失败】支付请求API返回的数据签名验证失败，有可能数据被篡改了");
            resultListener.onFailBySignInvalid(scanPayResData);
            return;
        }

        String errorCode = scanPayResData.getErr_code();

        String errorCodeDes = scanPayResData.getErr_code_des();
        if (scanPayResData.getResult_code().equals("SUCCESS")) {
            log.i("【一次性支付成功】");
            resultListener.onSuccess(scanPayResData);
        } else {
            log.i("业务返回失败");
            log.i("err_code:" + errorCode);
            log.i("err_code_des:" + errorCodeDes);

            if ((errorCode.equals("AUTHCODEEXPIRE")) || (errorCode.equals("AUTH_CODE_INVALID")) || (errorCode.equals("NOTENOUGH"))) {
                doReverseLoop(outTradeNo);

                if (errorCode.equals("AUTHCODEEXPIRE")) {
                    log.w("【支付扣款明确失败】原因是：" + errorCodeDes);
                    resultListener.onFailByAuthCodeExpire(scanPayResData);
                } else if (errorCode.equals("AUTH_CODE_INVALID")) {
                    log.w("【支付扣款明确失败】原因是：" + errorCodeDes);
                    resultListener.onFailByAuthCodeInvalid(scanPayResData);
                } else {
                    if (!(errorCode.equals("NOTENOUGH")))
                        return;
                    log.w("【支付扣款明确失败】原因是：" + errorCodeDes);
                    resultListener.onFailByMoneyNotEnough(scanPayResData);
                }
            } else if (errorCode.equals("USERPAYING")) {
                if (doPayQueryLoop(this.payQueryLoopInvokedCount, outTradeNo)) {
                    ScanPayQueryReqData scanPayQueryReqData = new ScanPayQueryReqData("", outTradeNo);
                    String payQueryServiceResponseString = this.scanPayQueryService.request(scanPayQueryReqData);

                    ScanPayQueryResData scanPayQueryResData = (ScanPayQueryResData) Util.getObjectFromXML(payQueryServiceResponseString, ScanPayQueryResData.class);
                    scanPayResData.setOpenid(scanPayQueryResData.getOpenid());
                    scanPayResData.setTransaction_id(scanPayQueryResData.getTransaction_id());
                    scanPayResData.setTime_end(scanPayQueryResData.getTime_end());
                    resultListener.onSuccess(scanPayResData);
                } else {
                    log.i("【需要用户输入密码、在一定时间内没有查询到支付成功、走撤销流程】");
                    doReverseLoop(outTradeNo);
                    resultListener.onFail(scanPayResData);
                }

            } else if (doPayQueryLoop(this.payQueryLoopInvokedCount, outTradeNo)) {
                log.i("【支付扣款未知失败、查询到支付成功】");
                resultListener.onSuccess(scanPayResData);
            } else {
                log.i("【支付扣款未知失败、在一定时间内没有查询到支付成功、走撤销流程】");
                doReverseLoop(outTradeNo);
                resultListener.onFail(scanPayResData);
            }
        }
    }

    private boolean doOnePayQuery(String outTradeNo)
            throws Exception {
        Thread.sleep(this.waitingTimeBeforePayQueryServiceInvoked);

        ScanPayQueryReqData scanPayQueryReqData = new ScanPayQueryReqData("", outTradeNo);
        String payQueryServiceResponseString = this.scanPayQueryService.request(scanPayQueryReqData);

        log.i("支付订单查询API返回的数据如下：");
        log.i(payQueryServiceResponseString);

        ScanPayQueryResData scanPayQueryResData = (ScanPayQueryResData) Util.getObjectFromXML(payQueryServiceResponseString, ScanPayQueryResData.class);
        if ((scanPayQueryResData == null) || (scanPayQueryResData.getReturn_code() == null)) {
            log.i("支付订单查询请求逻辑错误，请仔细检测传过去的每一个参数是否合法");
            return false;
        }

        if (scanPayQueryResData.getReturn_code().equals("FAIL")) {
            log.i("支付订单查询API系统返回失败，失败信息为：" + scanPayQueryResData.getReturn_msg());
            return false;
        }
        if (scanPayQueryResData.getResult_code().equals("SUCCESS")) {
            if (scanPayQueryResData.getTrade_state().equals("SUCCESS")) {
                log.i("查询到订单支付成功");
                return true;
            }

            log.i("查询到订单支付不成功");
            return false;
        }

        log.i("查询出错，错误码：" + scanPayQueryResData.getErr_code() + "     错误信息：" + scanPayQueryResData.getErr_code_des());
        return false;
    }

    private boolean doPayQueryLoop(int loopCount, String outTradeNo)
            throws Exception {
        if (loopCount == 0) {
            loopCount = 1;
        }

        for (int i = 0; i < loopCount; ++i) {
            if (doOnePayQuery(outTradeNo)) {
                return true;
            }
        }
        return false;
    }

    private boolean doOneReverse(String outTradeNo)
            throws Exception {
        Thread.sleep(this.waitingTimeBeforeReverseServiceInvoked);

        ReverseReqData reverseReqData = new ReverseReqData("", outTradeNo);
        String reverseResponseString = this.reverseService.request(reverseReqData);

        log.i("撤销API返回的数据如下：");
        log.i(reverseResponseString);

        ReverseResData reverseResData = (ReverseResData) Util.getObjectFromXML(reverseResponseString, ReverseResData.class);
        if (reverseResData == null) {
            log.i("支付订单撤销请求逻辑错误，请仔细检测传过去的每一个参数是否合法");
            return false;
        }
        if (reverseResData.getReturn_code().equals("FAIL")) {
            log.i("支付订单撤销API系统返回失败，失败信息为：" + reverseResData.getReturn_msg());
            return false;
        }
        if (reverseResData.getResult_code().equals("FAIL")) {
            log.i("撤销出错，错误码：" + reverseResData.getErr_code() + "     错误信息：" + reverseResData.getErr_code_des());
            if (reverseResData.getRecall().equals("Y")) {
                this.needRecallReverse = true;
                return false;
            }

            this.needRecallReverse = false;
            return true;
        }

        log.i("支付订单撤销成功");
        return true;
    }

    private void doReverseLoop(String outTradeNo)
            throws Exception {
        this.needRecallReverse = true;

        while (this.needRecallReverse)
            if (doOneReverse(outTradeNo))
                return;
    }

    public void setWaitingTimeBeforePayQueryServiceInvoked(int duration) {
        this.waitingTimeBeforePayQueryServiceInvoked = duration;
    }

    public void setPayQueryLoopInvokedCount(int count) {
        this.payQueryLoopInvokedCount = count;
    }

    public void setWaitingTimeBeforeReverseServiceInvoked(int duration) {
        this.waitingTimeBeforeReverseServiceInvoked = duration;
    }

    public void setScanPayService(ScanPayService service) {
        this.scanPayService = service;
    }

    public void setScanPayQueryService(ScanPayQueryService service) {
        this.scanPayQueryService = service;
    }

    public void setReverseService(ReverseService service) {
        this.reverseService = service;
    }

    public static abstract interface ResultListener {
        public abstract void onFailByReturnCodeError(ScanPayResData paramScanPayResData);

        public abstract void onFailByReturnCodeFail(ScanPayResData paramScanPayResData);

        public abstract void onFailBySignInvalid(ScanPayResData paramScanPayResData);

        public abstract void onFailByAuthCodeExpire(ScanPayResData paramScanPayResData);

        public abstract void onFailByAuthCodeInvalid(ScanPayResData paramScanPayResData);

        public abstract void onFailByMoneyNotEnough(ScanPayResData paramScanPayResData);

        public abstract void onFail(ScanPayResData paramScanPayResData);

        public abstract void onSuccess(ScanPayResData paramScanPayResData);
    }
}