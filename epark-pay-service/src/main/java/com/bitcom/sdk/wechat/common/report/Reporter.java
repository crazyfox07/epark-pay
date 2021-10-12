package com.bitcom.sdk.wechat.common.report;

import com.bitcom.sdk.wechat.common.report.protocol.ReportReqData;
import com.bitcom.sdk.wechat.common.report.service.ReportService;


public class Reporter {
    private ReportRunable r;
    private Thread t;
    private ReportService rs;

    public Reporter(ReportReqData reportReqData) {
        this.rs = new ReportService(reportReqData);
    }


    public void run() {
        this.r = new ReportRunable(this.rs);
        this.t = new Thread(this.r);
        this.t.setDaemon(true);
        this.t.start();
    }
}



