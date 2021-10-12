package com.bitcom.sdk.wechat.common.report;

import com.bitcom.sdk.wechat.common.report.protocol.ReportReqData;


public class ReporterFactory {
    public static Reporter getReporter(ReportReqData reportReqData) {
        return new Reporter(reportReqData);
    }
}



