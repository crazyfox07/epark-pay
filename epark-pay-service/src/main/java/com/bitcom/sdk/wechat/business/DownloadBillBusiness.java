package com.bitcom.sdk.wechat.business;

import com.bitcom.sdk.wechat.common.Configure;
import com.bitcom.sdk.wechat.common.Log;
import com.bitcom.sdk.wechat.common.Util;
import com.bitcom.sdk.wechat.common.report.Reporter;
import com.bitcom.sdk.wechat.common.report.ReporterFactory;
import com.bitcom.sdk.wechat.common.report.protocol.ReportReqData;
import com.bitcom.sdk.wechat.common.report.service.ReportService;
import com.bitcom.sdk.wechat.protocol.downloadbill_protocol.DownloadBillReqData;
import com.bitcom.sdk.wechat.protocol.downloadbill_protocol.DownloadBillResData;
import com.bitcom.sdk.wechat.service.DownloadBillService;
import com.thoughtworks.xstream.io.StreamException;
import org.slf4j.LoggerFactory;

public class DownloadBillBusiness {
    private static Log log = new Log(LoggerFactory.getLogger(DownloadBillBusiness.class));

    private static String result = "";
    private DownloadBillService downloadBillService;

    public DownloadBillBusiness()
            throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        this.downloadBillService = new DownloadBillService();
    }

    public void run(DownloadBillReqData downloadBillReqData, ResultListener resultListener)
            throws Exception {
        long costTimeStart = System.currentTimeMillis();

        log.i("对账单API返回的数据如下：");
        String downloadBillServiceResponseString = this.downloadBillService.request(downloadBillReqData);

        long costTimeEnd = System.currentTimeMillis();
        long totalTimeCost = costTimeEnd - costTimeStart;
        log.i("api请求总耗时：" + totalTimeCost + "ms");

        log.i(downloadBillServiceResponseString);

        String returnCode = "";
        String returnMsg = "";
        try {
            DownloadBillResData downloadBillResData = (DownloadBillResData) Util.getObjectFromXML(downloadBillServiceResponseString, DownloadBillResData.class);

            if ((downloadBillResData == null) || (downloadBillResData.getReturn_code() == null)) {
                setResult("Case1:对账单API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问", "logTypeError");
                resultListener.onFailByReturnCodeError(downloadBillResData);
                return;
            }
            if (downloadBillResData.getReturn_code().equals("FAIL")) {
                setResult("Case2:对账单API系统返回失败，请检测Post给API的数据是否规范合法", "logTypeError");
                resultListener.onFailByReturnCodeFail(downloadBillResData);
                returnCode = "FAIL";
                returnMsg = downloadBillResData.getReturn_msg();
            }
        } catch (StreamException e) {
            if ((downloadBillServiceResponseString.equals(null)) || (downloadBillServiceResponseString.equals(""))) {
                setResult("Case4:对账单API系统返回数据为空", "logTypeError");
                resultListener.onDownloadBillFail(downloadBillServiceResponseString);
            } else {
                setResult("Case3:对账单API系统成功返回数据", "logTypeInfo");
                resultListener.onDownloadBillSuccess(downloadBillServiceResponseString);
            }
            returnCode = "SUCCESS";
        } finally {
            ReportReqData reportReqData = new ReportReqData(downloadBillReqData
                    .getDevice_info(), Configure.DOWNLOAD_BILL_API, (int) totalTimeCost, returnCode, returnMsg, "", "", "", "",
                    Configure.getIP());

            if (Configure.isUseThreadToDoReport()) {
                ReporterFactory.getReporter(reportReqData).run();
                long timeAfterReport = System.currentTimeMillis();
                Util.log("pay+report总耗时（异步方式上报）：" + (timeAfterReport - costTimeStart) + "ms");
            } else {
                ReportService.request(reportReqData);
                long timeAfterReport = System.currentTimeMillis();
                Util.log("pay+report总耗时（同步方式上报）：" + (timeAfterReport - costTimeStart) + "ms");
            }
        }
    }

    public void setDownloadBillService(DownloadBillService service) {
        this.downloadBillService = service;
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
        public abstract void onFailByReturnCodeError(DownloadBillResData paramDownloadBillResData);

        public abstract void onFailByReturnCodeFail(DownloadBillResData paramDownloadBillResData);

        public abstract void onDownloadBillFail(String paramString);

        public abstract void onDownloadBillSuccess(String paramString);
    }
}