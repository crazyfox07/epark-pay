package com.bitcom.sdk.wechat.service;

import com.bitcom.sdk.wechat.common.Configure;
import com.bitcom.sdk.wechat.protocol.refund_query_protocol.RefundQueryReqData;


public class RefundQueryService
        extends BaseService {
    public RefundQueryService() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.REFUND_QUERY_API);
    }


    public String request(RefundQueryReqData refundQueryReqData) throws Exception {
        String responseString = sendPost(refundQueryReqData);

        return responseString;
    }
}



