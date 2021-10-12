package com.bitcom.sdk.wechat.service;

import com.bitcom.sdk.wechat.common.Configure;
import com.bitcom.sdk.wechat.protocol.pay_protocol.ScanPayReqData;


public class ScanPayService
        extends BaseService {
    public ScanPayService() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.PAY_API);
    }


    public String request(ScanPayReqData scanPayReqData) throws Exception {
        String responseString = sendPost(scanPayReqData);

        return responseString;
    }
}



