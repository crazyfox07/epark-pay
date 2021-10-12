package com.bitcom.sdk.wechat.business;

import com.bitcom.sdk.wechat.common.Configure;
import com.bitcom.sdk.wechat.common.Log;
import com.bitcom.sdk.wechat.common.Signature;
import com.bitcom.sdk.wechat.common.Util;
import com.bitcom.sdk.wechat.common.XMLParser;
import com.bitcom.sdk.wechat.common.report.Reporter;
import com.bitcom.sdk.wechat.common.report.ReporterFactory;
import com.bitcom.sdk.wechat.common.report.protocol.ReportReqData;
import com.bitcom.sdk.wechat.common.report.service.ReportService;
import com.bitcom.sdk.wechat.protocol.refund_query_protocol.RefundOrderData;
import com.bitcom.sdk.wechat.protocol.refund_query_protocol.RefundQueryReqData;
import com.bitcom.sdk.wechat.protocol.refund_query_protocol.RefundQueryResData;
import com.bitcom.sdk.wechat.service.RefundQueryService;

import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class RefundQueryBusiness {
    private static Log log = new Log(LoggerFactory.getLogger(RefundQueryBusiness.class));

    private static String result = "";

    private static String orderListResult = "";
    private RefundQueryService refundQueryService;

    public RefundQueryBusiness()
            throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        this.refundQueryService = new RefundQueryService();
    }

    public String getOrderListResult() {
        return orderListResult;
    }

    public void setOrderListResult(String orderListResult) {
        orderListResult = orderListResult;
    }

    public void run(RefundQueryReqData refundQueryReqData, ResultListener resultListener)
            throws Exception {
        long timeAfterReport;
        long costTimeStart = System.currentTimeMillis();

        log.i("退款查询API返回的数据如下：");
        String refundQueryServiceResponseString = this.refundQueryService.request(refundQueryReqData);

        long costTimeEnd = System.currentTimeMillis();
        long totalTimeCost = costTimeEnd - costTimeStart;
        log.i("api请求总耗时：" + totalTimeCost + "ms");

        log.i(refundQueryServiceResponseString);

        RefundQueryResData refundQueryResData = (RefundQueryResData) Util.getObjectFromXML(refundQueryServiceResponseString, RefundQueryResData.class);

        ReportReqData reportReqData = new ReportReqData(refundQueryReqData
                .getDevice_info(), Configure.REFUND_QUERY_API, (int) totalTimeCost, refundQueryResData
                .getReturn_code(), refundQueryResData
                .getReturn_msg(), refundQueryResData
                .getResult_code(), refundQueryResData
                .getErr_code(), refundQueryResData
                .getErr_code_des(), refundQueryResData
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

        if ((refundQueryResData == null) || (refundQueryResData.getReturn_code() == null)) {
            setResult("Case1:退款查询API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问", "logTypeError");
            resultListener.onFailByReturnCodeError(refundQueryResData);
            return;
        }

        if (refundQueryResData.getReturn_code().equals("FAIL")) {
            setResult("Case2:退款查询API系统返回失败，请检测Post给API的数据是否规范合法", "logTypeError");
            resultListener.onFailByReturnCodeFail(refundQueryResData);
        } else {
            log.i("退款查询API系统成功返回数据");

            if (!(Signature.checkIsSignValidFromResponseString(refundQueryServiceResponseString))) {
                setResult("Case3:退款查询API返回的数据签名验证失败，有可能数据被篡改了", "logTypeError");
                resultListener.onFailBySignInvalid(refundQueryResData);
                return;
            }

            if (refundQueryResData.getResult_code().equals("FAIL")) {
                Util.log("出错，错误码：" + refundQueryResData.getErr_code() + "     错误信息：" + refundQueryResData.getErr_code_des());
                setResult("Case4:【退款查询失败】", "logTypeError");
                resultListener.onRefundQueryFail(refundQueryResData);
            } else {
                getRefundOrderListResult(refundQueryServiceResponseString);
                setResult("Case5:【退款查询成功】", "logTypeInfo");
                resultListener.onRefundQuerySuccess(refundQueryResData);
            }
        }
    }

    private void getRefundOrderListResult(String refundQueryResponseString)
            throws ParserConfigurationException, SAXException, IOException {
        List<RefundOrderData> refundOrderList = XMLParser.getRefundOrderList(refundQueryResponseString);
        int count = 1;
        for (RefundOrderData refundOrderData : refundOrderList) {
            Util.log("退款订单数据NO" + count + ":");
            Util.log(refundOrderData.toMap());
            orderListResult += refundOrderData.toMap().toString();
            ++count;
        }
        log.i("查询到的结果如下：");
        log.i(orderListResult);
    }

    public void setRefundQueryService(RefundQueryService service) {
        this.refundQueryService = service;
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
        public abstract void onFailByReturnCodeError(RefundQueryResData paramRefundQueryResData);

        public abstract void onFailByReturnCodeFail(RefundQueryResData paramRefundQueryResData);

        public abstract void onFailBySignInvalid(RefundQueryResData paramRefundQueryResData);

        public abstract void onRefundQueryFail(RefundQueryResData paramRefundQueryResData);

        public abstract void onRefundQuerySuccess(RefundQueryResData paramRefundQueryResData);
    }
}