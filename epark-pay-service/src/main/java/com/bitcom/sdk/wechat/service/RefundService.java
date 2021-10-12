package com.bitcom.sdk.wechat.service;

import com.bitcom.sdk.wechat.common.Configure;
import com.bitcom.sdk.wechat.protocol.refund_protocol.RefundReqData;


public class RefundService
        extends BaseService {
    public RefundService() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.REFUND_API);
    }


    public String request(RefundReqData refundReqData) throws Exception {
        String responseString = sendPost(refundReqData);

        return responseString;
    }
}



