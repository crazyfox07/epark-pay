package com.bitcom.sdk.wechat.service;

import com.bitcom.sdk.wechat.common.Configure;
import com.bitcom.sdk.wechat.protocol.downloadbill_protocol.DownloadBillReqData;

public class DownloadBillService
        extends BaseService {
    public static final String BILL_TYPE_ALL = "ALL";
    public static final String BILL_TYPE_SUCCESS = "SUCCESS";
    public static final String BILL_TYPE_REFUND = "REFUND";
    public static final String BILL_TYPE_REVOKE = "REVOKE";

    public DownloadBillService() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.DOWNLOAD_BILL_API);
    }


    public String request(DownloadBillReqData downloadBillReqData) throws Exception {
        String responseString = sendPost(downloadBillReqData);

        return responseString;
    }
}



