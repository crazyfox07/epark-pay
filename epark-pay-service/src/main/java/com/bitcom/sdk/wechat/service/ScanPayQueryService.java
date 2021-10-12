package com.bitcom.sdk.wechat.service;

import com.bitcom.sdk.wechat.common.Configure;
import com.bitcom.sdk.wechat.protocol.pay_query_protocol.ScanPayQueryReqData;


public class ScanPayQueryService
        extends BaseService {
    public ScanPayQueryService() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.PAY_QUERY_API);
    }


    public String request(ScanPayQueryReqData scanPayQueryReqData) throws Exception {
        String responseString = sendPost(scanPayQueryReqData);

        return responseString;
    }
}



