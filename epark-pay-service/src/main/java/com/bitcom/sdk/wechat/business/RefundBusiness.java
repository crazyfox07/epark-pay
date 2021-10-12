package com.bitcom.sdk.wechat.business;

import com.bitcom.sdk.wechat.common.Configure;
import com.bitcom.sdk.wechat.common.Log;
import com.bitcom.sdk.wechat.common.Signature;
import com.bitcom.sdk.wechat.common.Util;
import com.bitcom.sdk.wechat.common.report.Reporter;
import com.bitcom.sdk.wechat.common.report.ReporterFactory;
import com.bitcom.sdk.wechat.common.report.protocol.ReportReqData;
import com.bitcom.sdk.wechat.common.report.service.ReportService;
import com.bitcom.sdk.wechat.protocol.refund_protocol.RefundReqData;
import com.bitcom.sdk.wechat.protocol.refund_protocol.RefundResData;
import com.bitcom.sdk.wechat.service.RefundService;
import org.slf4j.LoggerFactory;

public class RefundBusiness {
    private static Log log = new Log(LoggerFactory.getLogger(RefundBusiness.class));

    private static String result = "";
    private RefundService refundService;

    public RefundBusiness()
            throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        this.refundService = new RefundService();
    }

    public void run(RefundReqData refundReqData, ResultListener resultListener)
            throws Exception {
        long timeAfterReport;
        long costTimeStart = System.currentTimeMillis();

        log.i("退款查询API返回的数据如下：");
        String refundServiceResponseString = this.refundService.request(refundReqData);

        long costTimeEnd = System.currentTimeMillis();
        long totalTimeCost = costTimeEnd - costTimeStart;
        log.i("api请求总耗时：" + totalTimeCost + "ms");

        log.i(refundServiceResponseString);

        RefundResData refundResData = (RefundResData) Util.getObjectFromXML(refundServiceResponseString, RefundResData.class);

        ReportReqData reportReqData = new ReportReqData(refundResData
                .getDevice_info(), Configure.REFUND_API, (int) totalTimeCost, refundResData
                .getReturn_code(), refundResData
                .getReturn_msg(), refundResData
                .getResult_code(), refundResData
                .getErr_code(), refundResData
                .getErr_code_des(), refundResData
                .getOut_trade_no(),
                Configure.getIP());

        if (Configure.isUseThreadToDoReport()) {
            ReporterFactory.getReporter(reportReqData).run();
            timeAfterReport = System.currentTimeMillis();
            Util.log("pay+report总耗时（异步方式上报）：" + (timeAfterReport - costTimeStart) + "ms");
        } else {
            ReportService.request(reportReqData);
            timeAfterReport = System.currentTimeMillis();
            Util.log("pay+report总耗时（同步方式上报）：" + (timeAfterReport - costTimeStart) + "ms");
        }

        if ((refundResData == null) || (refundResData.getReturn_code() == null)) {
            setResult("Case1:退款API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问", "logTypeError");
            resultListener.onFailByReturnCodeError(refundResData);
            return;
        }

        if (refundResData.getReturn_code().equals("FAIL")) {
            setResult("Case2:退款API系统返回失败，请检测Post给API的数据是否规范合法", "logTypeError");
            resultListener.onFailByReturnCodeFail(refundResData);
        } else {
            log.i("退款API系统成功返回数据");

            if (!(Signature.checkIsSignValidFromResponseString(refundServiceResponseString))) {
                setResult("Case3:退款请求API返回的数据签名验证失败，有可能数据被篡改了", "logTypeError");
                resultListener.onFailBySignInvalid(refundResData);
                return;
            }

            if (refundResData.getResult_code().equals("FAIL")) {
                log.i("出错，错误码：" + refundResData.getErr_code() + "     错误信息：" + refundResData.getErr_code_des());
                setResult("Case4:【退款失败】", "logTypeError");

                resultListener.onRefundFail(refundResData);
            } else {
                setResult("Case5:【退款成功】", "logTypeInfo");
                resultListener.onRefundSuccess(refundResData);
            }
        }
    }

    public void setRefundService(RefundService service) {
        this.refundService = service;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        result = result;
    }

    public void setResult(String result, String type) {
        setResult(result);
        log.log(type, result);
    }

    public static abstract interface ResultListener {
        public abstract void onFailByReturnCodeError(RefundResData paramRefundResData);

        public abstract void onFailByReturnCodeFail(RefundResData paramRefundResData);

        public abstract void onFailBySignInvalid(RefundResData paramRefundResData);

        public abstract void onRefundFail(RefundResData paramRefundResData);

        public abstract void onRefundSuccess(RefundResData paramRefundResData);
    }
}