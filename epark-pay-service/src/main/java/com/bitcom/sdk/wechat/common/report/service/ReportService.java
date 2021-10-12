package com.bitcom.sdk.wechat.common.report.service;

import com.bitcom.sdk.wechat.common.Configure;
import com.bitcom.sdk.wechat.common.HttpsRequest;
import com.bitcom.sdk.wechat.common.Util;
import com.bitcom.sdk.wechat.common.report.protocol.ReportReqData;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ReportService {
    private ReportReqData reqData;

    public ReportService(ReportReqData reportReqData) {
        this.reqData = reportReqData;
    }


    public String request() throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String responseString = (new HttpsRequest()).sendPost(Configure.REPORT_API, this.reqData);

        Util.log("   report返回的数据：" + responseString);

        return responseString;
    }


    public static String request(ReportReqData reportReqData) throws Exception {
        String responseString = (new HttpsRequest()).sendPost(Configure.REPORT_API, reportReqData);

        Util.log("report返回的数据：" + responseString);

        return responseString;
    }


    private static String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date());
    }
}



